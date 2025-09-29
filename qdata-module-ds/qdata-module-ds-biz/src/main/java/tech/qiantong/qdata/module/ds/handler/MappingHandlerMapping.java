package tech.qiantong.qdata.module.ds.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import tech.qiantong.qdata.common.enums.RequestMethodEnum;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MappingHandlerMapping {

    private static Map<String, DsApiDO> mappings = new ConcurrentHashMap<>();
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private RequestHandler handler;
    private Method method;

    {
        try {
            method = RequestHandler.class.getDeclaredMethod("invoke", HttpServletRequest.class, HttpServletResponse.class, Map.class, Map.class, Map.class);
        } catch (NoSuchMethodException e) {
        }
    }

    private String ignore = "services";
    private String prefix = "v1.0.0";
    private String separator = "/";

    public MappingHandlerMapping() {}

    public void setRequestMappingHandlerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }

    public static DsApiDO getMappingApiInfo(HttpServletRequest request) {
        NativeWebRequest webRequest = new ServletWebRequest(request);
        String requestMapping = (String) webRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return getMappingApiInfo(buildMappingKey(request.getMethod(), requestMapping));
    }

    public static DsApiDO getMappingApiInfo(String key) {
        return mappings.get(key);
    }

    public static String buildMappingKey(String requestMethod, String requestMapping) {
        String valByKey = null;
        //如果requestMethod是数字判断
        if("1".equals(requestMethod)||"2".equals(requestMethod)){
            valByKey = RequestMethodEnum.getValByKey(requestMethod.toUpperCase());
        }else{
            valByKey = requestMethod.toLowerCase();
        }
        return valByKey + ":" + requestMapping;
    }

    /**
     * 注册请求映射
     *
     * @param api
     */
    public void registerMapping(DsApiDO api) {
        String mappingKey = getMappingKey(api);
        if (mappings.containsKey(mappingKey)) {
            // 取消注册
            mappings.remove(mappingKey);
            requestMappingHandlerMapping.unregisterMapping(getRequestMapping(api));
        }
        RequestMappingInfo requestMapping = getRequestMapping(api);
        mappings.put(mappingKey, api);
        requestMappingHandlerMapping.registerMapping(requestMapping, handler, method);
        mappings.keySet().forEach(key -> {
//            log.info("已注册接口:{}", mappings.get(key));
        });
    }

    /**
     * 取消注册请求映射
     *
     * @param api
     */
    public void unregisterMapping(DsApiDO api) {
        log.info("取消注册接口:{}", api.getName());
        String mappingKey = getMappingKey(api);
        if (mappings.containsKey(mappingKey)) {
            // 取消注册
            mappings.remove(mappingKey);
            requestMappingHandlerMapping.unregisterMapping(getRequestMapping(api));
        }
    }

    private String getMappingKey(DsApiDO api) {
        return buildMappingKey(api.getReqMethod().toUpperCase(), getRequestPath(api.getApiVersion(), api.getApiUrl()));
    }

    private RequestMappingInfo getRequestMapping(DsApiDO api) {
        return RequestMappingInfo.paths(getRequestPath(api.getApiVersion(), api.getApiUrl())).methods(RequestMethod.valueOf(RequestMethodEnum.getValByKey(api.getReqMethod()).toUpperCase())).build();
    }

    /**
     * 调用接口 /services/v1.0.0/user/1
     * @param version
     * @param path
     * @return
     */
    private String getRequestPath(String version, String path) {
        if (version != null) {
            prefix = version;
        }
        return separator + ignore + separator + prefix + (path.startsWith(separator) ? path : (separator + path));
    }
}
