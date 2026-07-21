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
        //Handle numeric requestMethod values.
        if("1".equals(requestMethod)||"2".equals(requestMethod)){
            valByKey = RequestMethodEnum.getValByKey(requestMethod.toUpperCase());
        }else{
            valByKey = requestMethod.toLowerCase();
        }
        return valByKey + ":" + requestMapping;
    }

    /**
     * Registers request mappings.
     *
     * @param api
     */
    public void registerMapping(DsApiDO api) {
        String mappingKey = getMappingKey(api);
        if (mappings.containsKey(mappingKey)) {
            // Unregister the mapping.
            mappings.remove(mappingKey);
            requestMappingHandlerMapping.unregisterMapping(getRequestMapping(api));
        }
        RequestMappingInfo requestMapping = getRequestMapping(api);
        mappings.put(mappingKey, api);
        requestMappingHandlerMapping.registerMapping(requestMapping, handler, method);
        mappings.keySet().forEach(key -> {
//            log.info("Registered API:{}", mappings.get(key));
        });
    }

    /**
     * Unregisters request mappings.
     *
     * @param api
     */
    public void unregisterMapping(DsApiDO api) {
        log.info("Unregistered API:{}", api.getName());
        String mappingKey = getMappingKey(api);
        if (mappings.containsKey(mappingKey)) {
            // Unregister the mapping.
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
     * Calls the API /services/v1.0.0/user/1.
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
