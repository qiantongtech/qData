/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 用途:调度器请求工具
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:39
 **/
@Component
public class DsRequestUtils {

    private static String baseUrl;//ds请求接口前缀
    private static String token;//ds令牌

    @Value("${ds.token}")
    public void setToken(String token) {
        this.token = token;
    }

    @Value("${ds.base_url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 请求方法
     *
     * @param url         接口路径
     * @param method      请求方法
     * @param body        body参数
     * @param params      url拼接的参数 map
     * @param resultClass 结果class
     * @return
     */
    public static <T> T request(String url, String method, Object body, Map<String, Object> params, Class<T> resultClass) {
        //拼接url参数
        if (params != null && !params.isEmpty()) {
            String paramsStr = HttpUtil.toParams(params);
            if (url.indexOf("?") > -1) {
                url = url + "&" + paramsStr;
            } else {
                url = url + "?" + paramsStr;
            }
        }

        //封装请求对象
        HttpRequest request = HttpUtil.createRequest(Method.valueOf(method), baseUrl + url)
                .header("token", token);
        if (body != null) {
            request.body(JSON.toJSONString(body));
        }
        //获取结果
        HttpResponse response = request.execute();
        return JSON.parseObject(response.body(), resultClass);
    }

    /**
     * 请求方法(表单传参)
     *
     * @param url         接口路径
     * @param method      请求方法
     * @param params      map
     * @param resultClass 结果class
     * @return
     */
    public static <T> T requestForm(String url, String method, Map<String, Object> params, Class<T> resultClass) {
        //封装请求对象
        HttpRequest request = HttpUtil.createRequest(Method.valueOf(method), baseUrl + url)
                .header("token", token);
        if (params != null) {
            request.form(params);
        }
        //获取结果
        HttpResponse response = request.execute();
        return JSON.parseObject(response.body(), resultClass);
    }

    /**
     * 替换项目编码
     *
     * @param url
     * @param projectCode
     * @return
     */
    public static String replaceProjectCode(String url, String projectCode) {
        return StringUtils.replace(url, "{projectCode}", projectCode);
    }

    /**
     * 替换项目编码及id
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
     * 替换项目编码及code
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
