package tech.qiantong.qdata.common.httpClient;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HttpUtils {
    private static final CloseableHttpClient httpClient;

    // HTTP 方法常量
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";  // 新增 DELETE 常量

    static {
        // 设置连接池
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200); // 最大连接数
        connManager.setDefaultMaxPerRoute(20); // 每个路由的最大连接数

        // 设置请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)  // 连接超时
                .setSocketTimeout(60000)   // 数据传输超时
                .setConnectionRequestTimeout(10000)  // 请求超时
                .build();

        // 创建HttpClient实例
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * feng
     *
     * @param url
     * @param params
     * @return
     */
    @SneakyThrows
    public static String packGetRequestURL(String url, Map<String, Object> params) {
        StringBuilder urlPack = new StringBuilder(url);
        //封装请求头
        if (url.indexOf("?") > -1) {
            urlPack.append("&");
        } else {
            urlPack.append("?");
        }

        int size = params.entrySet().size();
        int sum = 1;
        //取出所有请求参数
        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            System.out.println("key= " + entry.getKey() + " ； value= " + entry.getValue());
            if (sum == size) {
                urlPack.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            } else {
                urlPack.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8")).append("&");
                sum++;
            }
        }
        //返回
        return urlPack.toString();
    }

    public static void sendGet(String url,
                               HttpServletResponse response,
                               List<HeaderEntity> headers) throws IOException {
        HttpRequest request = HttpUtil.createRequest(Method.GET, url)
                .setFollowRedirects(true);  // 🚀 开启自动重定向
        if (headers != null && !headers.isEmpty()) {
            request.addHeaders(
                    headers.stream()
                            .collect(Collectors.toMap(HeaderEntity::getKey, HeaderEntity::getValue))
            );
        }
        HttpResponse res = request.execute();

        // 1. 读取并设置远端 Content-Type（否则用默认）
        String remoteCt = res.header("Content-Type");
        if (StringUtils.isNotBlank(remoteCt)) {
            response.setContentType(remoteCt);
        } else {
            response.setContentType("application/json;charset=UTF-8");
        }
        response.setCharacterEncoding("UTF-8");
        // 2. 状态码同步
        response.setStatus(res.getStatus());

        // 3. 根据类型决定写字符流或二进制流
        byte[]   bodyBytes = res.bodyBytes();
        String   bodyText  = res.body();
        if (remoteCt != null &&
                (remoteCt.contains("application/json")
                        || remoteCt.contains("text")
                        || remoteCt.contains("xml")
                        || remoteCt.contains("application/x-www-form-urlencoded"))
        ) {
            try (PrintWriter writer = response.getWriter()) {
                writer.print(bodyText);
            }
        } else {
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(bodyBytes);
                out.flush();
            }
        }
    }

    public static void sendPost(String url,
                                Map<String,Object> params,
                                HttpServletResponse response,
                                List<HeaderEntity> headers) throws IOException {
        HttpRequest request = HttpUtil.createRequest(Method.POST, url)
                .body(JSONObject.toJSONString(params))
                .setFollowRedirects(true);  // 🚀 开启自动重定向
        if (headers != null && !headers.isEmpty()) {
            request.addHeaders(
                    headers.stream()
                            .collect(Collectors.toMap(HeaderEntity::getKey, HeaderEntity::getValue))
            );
        }
        HttpResponse res = request.execute();

        String remoteCt = res.header("Content-Type");
        if (StringUtils.isNotBlank(remoteCt)) {
            response.setContentType(remoteCt);
        } else {
            response.setContentType("application/json;charset=UTF-8");
        }
        response.setCharacterEncoding("UTF-8");
        response.setStatus(res.getStatus());

        byte[]  bodyBytes = res.bodyBytes();
        String  bodyText  = res.body();
        if (remoteCt != null &&
                (remoteCt.contains("application/json")
                        || remoteCt.contains("text")
                        || remoteCt.contains("xml")
                        || remoteCt.contains("application/x-www-form-urlencoded"))
        ) {
            try (PrintWriter writer = response.getWriter()) {
                writer.print(bodyText);
            }
        } else {
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(bodyBytes);
                out.flush();
            }
        }
    }


//
//
//    /**
//     * get请求(直接将响应结果输出到response)
//     *
//     * @param url
//     * @param headers
//     * @throws IOException
//     */
//    public static void sendGet(String url, HttpServletResponse response, List<HeaderEntity> headers) throws IOException {
//        HttpRequest request = HttpUtil.createRequest(Method.GET, url);
//        if (headers != null && headers.size() > 0) {
//            request.addHeaders(headers.stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue())));
//        }
//        HttpResponse res = request.execute();
//        Map<String, List<String>> map = res.headers();
//        String contentType = "";
//        for (String key : map.keySet()) {
//            if (StringUtils.isBlank(key) || StringUtils.equals("Content-Encoding", key)) {
//                continue;
//            }
//            List<String> valueList = map.get(key);
//            for (String val : valueList) {
//                if (StringUtils.equals("Content-Type", key)) {
//                    contentType = key;
//                    response.setContentType(val);
//                    break;
//                }
//                response.setHeader(key, val);
//            }
//        }
//
//        response.setStatus(200);
//        if (contentType.contains("application/json") || contentType.contains("text") || contentType.contains("xml") || contentType.contains("application/x-www-form-urlencoded")) {
//            PrintWriter writer = response.getWriter();
//            try {
//                writer.print(JSONObject.parseObject(res.body()));
//            } catch (Exception e) {
//                log.info(e.getMessage());
//            } finally {
//                writer.close();
//            }
//        } else {
//            ServletOutputStream outputStream = response.getOutputStream();
//            try {
//                outputStream.write(res.bodyBytes());
//            } catch (Exception e) {
//                log.info(e.getMessage());
//            } finally {
//                outputStream.flush();
//            }
//        }
//    }
//
//    /**
//     * 发送POST请求(直接将响应结果输出到response)
//     *
//     * @param url     目标URL
//     * @param params  请求参数
//     * @param headers 请求头
//     * @throws IOException 发生网络异常
//     */
//    public static void sendPost(String url, Map<String, Object> params, HttpServletResponse response, List<HeaderEntity> headers) throws IOException {
//        HttpRequest request = HttpUtil.createRequest(Method.POST, url);
//        //设置请求头
//        if (headers != null && headers.size() > 0) {
//            request.addHeaders(headers.stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue())));
//        }
//        //设置参数
//        request.body(JSONObject.toJSONString(params));
//        HttpResponse res = request.execute();
//        Map<String, List<String>> map = res.headers();
//        String contentType = "";
//        for (String key : map.keySet()) {
//            if (StringUtils.isBlank(key) || StringUtils.equals("Content-Encoding", key)) {
//                continue;
//            }
//            List<String> valueList = map.get(key);
//            for (String val : valueList) {
//                if (StringUtils.equals("Content-Type", key)) {
//                    contentType = key;
//                    response.setContentType(val);
//                    break;
//                }
//                response.setHeader(key, val);
//            }
//        }
//
//        response.setStatus(200);
//        if (contentType.contains("application/json") || contentType.contains("text") || contentType.contains("xml") || contentType.contains("application/x-www-form-urlencoded")) {
//            PrintWriter writer = response.getWriter();
//            try {
//                writer.print(JSONObject.parseObject(res.body()));
//            } catch (Exception e) {
//                log.info(e.getMessage());
//            } finally {
//                writer.close();
//            }
//        } else {
//            ServletOutputStream outputStream = response.getOutputStream();
//            try {
//                outputStream.write(res.bodyBytes());
//            } catch (Exception e) {
//                log.info(e.getMessage());
//            } finally {
//                outputStream.flush();
//            }
//        }
//    }


    /**
     * 执行请求并返回响应对象
     *
     * @param method 请求方法
     * @param url URL
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    private static ResponseObject executeRequest(String method, String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        // 创建请求
        HttpUriRequest request;
        if (POST.equals(method)) {
            request = new HttpPost(url);
            StringEntity entity = new StringEntity(JSONObject.toJSONString(params), StandardCharsets.UTF_8);
            ((HttpPost) request).setEntity(entity);
        } else if (GET.equals(method)) {
            request = new HttpGet(url);
        } else if (PUT.equals(method)) {
            request = new HttpPut(url);
            StringEntity entity = new StringEntity(JSONObject.toJSONString(params), StandardCharsets.UTF_8);
            ((HttpPut) request).setEntity(entity);
        } else if (DELETE.equals(method)) {
            request = new HttpDelete(url);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (HeaderEntity header : headers) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }

        // 执行请求
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // 构造响应对象
            ResponseObject responseObject = new ResponseObject();
            responseObject.setStatus(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());

            // 获取响应体
            String contentType = response.getFirstHeader("Content-Type").getValue();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            // 根据Content-Type判断响应内容类型并返回
            if (contentType.contains("application/json")) {
                responseObject.setBody(JSONObject.parseObject(body));
            } else {
                responseObject.setBody(body);
            }

            return responseObject;
        }
    }

    /**
     * 发送GET请求并返回响应对象
     *
     * @param url URL地址
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendGet(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(GET, url, null, headers);
    }

    /**
     * 发送POST请求并返回响应对象
     *
     * @param url URL地址
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendPost(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(POST, url, params, headers);
    }

    /**
     * 发送PUT请求并返回响应对象
     *
     * @param url URL地址
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendPut(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(PUT, url, params, headers);
    }

    /**
     * 发送DELETE请求并返回响应对象
     *
     * @param url URL地址
     * @param headers 请求头
     * @return 响应对象
     * @throws IOException IO异常
     */
    public static ResponseObject sendDelete(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(DELETE, url, null, headers);
    }

    /**
     * 响应对象，封装HTTP请求的响应信息
     */
    public static class ResponseObject {
        private int status;  // HTTP 状态码
        private Header[] headers;  // 响应头
        private Object body;  // 响应体（可能是 JSON 对象或字符串）

        // Getters and setters
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        public Object getBody() {
            return body;
        }

        public void setBody(Object body) {
            this.body = body;
        }
    }
}
