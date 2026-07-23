/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

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

    // HTTP method constants
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";  // New DELETE constant

    static {
        // Set up connection pool
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200); // Maximum number of connections
        connManager.setDefaultMaxPerRoute(20); // Maximum number of connections per route

        // Set request configuration
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000)  // Connection timeout
                .setSocketTimeout(60000)   // Data transfer timeout
                .setConnectionRequestTimeout(10000)  // Request timeout
                .build();

        // Create HttpClient instance
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
        //Encapsulate request header
        if (url.indexOf("?") > -1) {
            urlPack.append("&");
        } else {
            urlPack.append("?");
        }

        int size = params.entrySet().size();
        int sum = 1;
        //Get all request parameters
        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            System.out.println("key= " + entry.getKey() + " ； value= " + entry.getValue());
            if (sum == size) {
                urlPack.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            } else {
                urlPack.append(entry.getKey()).append("=").append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8")).append("&");
                sum++;
            }
        }
        //Return
        return urlPack.toString();
    }

    public static void sendGet(String url,
                               HttpServletResponse response,
                               List<HeaderEntity> headers) throws IOException {
        HttpRequest request = HttpUtil.createRequest(Method.GET, url)
                .setFollowRedirects(true);  // 🚀 Turn on automatic redirection
        if (headers != null && !headers.isEmpty()) {
            request.addHeaders(
                    headers.stream()
                            .collect(Collectors.toMap(HeaderEntity::getKey, HeaderEntity::getValue))
            );
        }
        HttpResponse res = request.execute();

        // 1. Read and set the remote Content-Type (otherwise use the default)
        String remoteCt = res.header("Content-Type");
        if (StringUtils.isNotBlank(remoteCt)) {
            response.setContentType(remoteCt);
        } else {
            response.setContentType("application/json;charset=UTF-8");
        }
        response.setCharacterEncoding("UTF-8");
        // 2. Status code synchronization
        response.setStatus(res.getStatus());

        // 3. Decide whether to write character stream or binary stream according to the type
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
                .setFollowRedirects(true);  // 🚀 Turn on automatic redirection
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
// * get request (directly output the response result to response)
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
// * Send POST request (directly output the response result to response)
//     *
// * @param url target URL
// * @param params request parameters
// * @param headers request headers
// * @throws IOException A network exception occurred
//     */
//    public static void sendPost(String url, Map<String, Object> params, HttpServletResponse response, List<HeaderEntity> headers) throws IOException {
//        HttpRequest request = HttpUtil.createRequest(Method.POST, url);
// //Set request header
//        if (headers != null && headers.size() > 0) {
//            request.addHeaders(headers.stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue())));
//        }
// //Set parameters
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
     * Execute the request and return the response object
     *
     * @param method request method
     * @param url URL
     * @param params request parameters
     * @param headers request headers
     * @return response object
     * @throws IOException IO exception
     */
    private static ResponseObject executeRequest(String method, String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        // Create request
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

        // Add request header
        if (headers != null && !headers.isEmpty()) {
            for (HeaderEntity header : headers) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }

        // Execute request
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            // Construct response object
            ResponseObject responseObject = new ResponseObject();
            responseObject.setStatus(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());

            // Get response body
            String contentType = response.getFirstHeader("Content-Type").getValue();
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            // Determine the response content type based on Content-Type and return
            if (contentType.contains("application/json")) {
                responseObject.setBody(JSONObject.parseObject(body));
            } else {
                responseObject.setBody(body);
            }

            return responseObject;
        }
    }

    /**
     * Send a GET request and return a response object
     *
     * @param url URL address
     * @param headers request headers
     * @return response object
     * @throws IOException IO exception
     */
    public static ResponseObject sendGet(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(GET, url, null, headers);
    }

    /**
     * Send a POST request and return a response object
     *
     * @param url URL address
     * @param params request parameters
     * @param headers request headers
     * @return response object
     * @throws IOException IO exception
     */
    public static ResponseObject sendPost(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(POST, url, params, headers);
    }

    /**
     * Send a PUT request and return a response object
     *
     * @param url URL address
     * @param params request parameters
     * @param headers request headers
     * @return response object
     * @throws IOException IO exception
     */
    public static ResponseObject sendPut(String url, Map<String, Object> params, List<HeaderEntity> headers) throws IOException {
        return executeRequest(PUT, url, params, headers);
    }

    /**
     * Send a DELETE request and return a response object
     *
     * @param url URL address
     * @param headers request headers
     * @return response object
     * @throws IOException IO exception
     */
    public static ResponseObject sendDelete(String url, List<HeaderEntity> headers) throws IOException {
        return executeRequest(DELETE, url, null, headers);
    }

    /**
     * Response object, encapsulates the response information of the HTTP request
     */
    public static class ResponseObject {
        private int status;  // HTTP status code
        private Header[] headers;  // Response header
        private Object body;  // Response body (may be a JSON object or string)

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
