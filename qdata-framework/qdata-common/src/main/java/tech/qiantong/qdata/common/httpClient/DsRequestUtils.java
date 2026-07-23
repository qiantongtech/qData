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
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <P>
 * Purpose: Scheduler request tool
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:39
 **/
@Component
public class DsRequestUtils {

    private static String baseUrl;//ds request interface prefix
    private static String token;//ds token

    @Value("${ds.token}")
    public void setToken(String token) {
        this.token = token;
    }

    @Value("${ds.base_url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Request method
     *
     * @param url interface path
     * @param method request method
     * @param body body parameter
     * @param params url splicing parameter map
     * @param resultClass result class
     * @return
     */
    public static <T> T request(String url, String method, Object body, Map<String, Object> params, Class<T> resultClass) {
        //Splicing url parameters
        if (params != null && !params.isEmpty()) {
            String paramsStr = HttpUtil.toParams(params);
            if (url.indexOf("?") > -1) {
                url = url + "&" + paramsStr;
            } else {
                url = url + "?" + paramsStr;
            }
        }

        //Encapsulate request object
        HttpRequest request = HttpUtil.createRequest(Method.valueOf(method), baseUrl + url)
                .header("token", token);
        if (body != null) {
            request.body(JSON.toJSONString(body));
        }
        //Get results
        HttpResponse response = request.execute();
        return JSON.parseObject(response.body(), resultClass);
    }

    /**
     * Request method (form parameter passing)
     *
     * @param url interface path
     * @param method request method
     * @param params      map
     * @param resultClass result class
     * @return
     */
    public static <T> T requestForm(String url, String method, Map<String, Object> params, Class<T> resultClass) {
        //Encapsulate request object
        HttpRequest request = HttpUtil.createRequest(Method.valueOf(method), baseUrl + url)
                .header("token", token);
        if (params != null) {
            request.form(params);
        }
        //Get results
        HttpResponse response = request.execute();
        return JSON.parseObject(response.body(), resultClass);
    }

    /**
     * Replace project encoding
     *
     * @param url
     * @param projectCode
     * @return
     */
    public static String replaceProjectCode(String url, String projectCode) {
        return StringUtils.replace(url, "{projectCode}", projectCode);
    }

    /**
     * Replace project code and id
     *
     * @param url
     * @param projectCode
     * @param id
     * @return
     */
    public static String replaceProjectCodeAndId(String url, String projectCode, Long id) {
        return StringUtils.replace(StringUtils.replace(url, "{projectCode}", projectCode), "{id}", String.valueOf(id));
    }

    /**
     * Replace project encoding and code
     *
     * @param url
     * @param projectCode
     * @param code
     * @return
     */
    public static String replaceProjectCodeAndCode(String url, String projectCode, String code) {
        return StringUtils.replace(StringUtils.replace(url, "{projectCode}", projectCode), "{code}", String.valueOf(code));
    }
}
