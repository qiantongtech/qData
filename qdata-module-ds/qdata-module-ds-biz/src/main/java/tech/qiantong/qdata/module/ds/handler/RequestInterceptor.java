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


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.qiantong.qdata.common.enums.ParamType;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.IPUtil;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    private RedisTemplate<String, Object> redisTemplate;

    public RequestInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Executes before the request.
     *
     * @return an object to return directly, or null to continue processing
     * @throws Exception
     */
    public void preHandle( HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params) {

//    public void preHandle(String username, HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params, DataApiApplyServiceFeign dataApiApplyService, SysUserServiceFeign sysUserService) {
        log.info("************ApiInterceptor preHandle executed**********");
        String uri = request.getRequestURI();
        log.info("getRequestURI value: " + uri);
        String ipAddr = IPUtil.getIpAddr(request);
        log.info("ipAddr value: " + ipAddr);

        //Check whether the caller is an administrator for authorization.
//        if (sysUserService.checkAdmin(username) == 1) {
//            return;
//        }
//        R res = dataApiApplyService.getDataApiApplyByUsernameAndUrl(username, uri);
//        if (!res.isSuccess()) {
//            throw new ServiceException("Service error. Please contact the administrator.");
//        }
//        DsApiLogDO dataApiApplyEntity = JSONObject.parseObject(JSONObject.toJSONString(res.getData()), DataApiApplyEntity.class);
//        //Check whether the application is empty or the current user is neither the API publisher nor an approved applicant.
//        if(dataApiApplyEntity != null && StringUtils.equals("4",dataApiApplyEntity.getApplyStatus()) ){
//            throw new ServiceException("Permission has expired. Apply for API access in the administration console.");
//        }
//
//        if (dataApiApplyEntity == null || (!StringUtils.equals(username, dataApiApplyEntity.getCreateBy()) && !StringUtils.equals("2", dataApiApplyEntity.getApplyStatus()))) {
//            throw new ServiceException("Access denied. Apply for API access in the administration console.");
//        }

        // Validate the blacklist.
        String deny = api.getDenyIp();
        if (StrUtil.isNotBlank(deny)) {
            List<String> denyList = Arrays.asList(deny.split(","));
            if (CollUtil.isNotEmpty(denyList)) {
                for (String ip : denyList) {
                    if (ip.equals(ipAddr)) {
                        throw new ServiceException("ds.error.ip.blacklist", "IP has been added to blacklist");
                    }
                }
            }
        }
        api.setResParamsList();
        //Remove authorization parameters.
        params.remove("client_token");
        // Validate parameters.
        if (MapUtil.isNotEmpty(params)) {
            api.getReqParamsList().forEach(param -> {
                if (params.containsKey(param.getParamName())) {
                    // Check whether the parameter type is correct.
                    ParamType.parse(ParamType.getParamType(param.getParamType()), params.get(param.getParamName()));
                }
            });
        }

        //Add the request.

        // Validate rate limiting.
//        RateLimit rateLimit = api.getRateLimit();
//        if (DataConstant.TrueOrFalse.TRUE.getKey().equals(rateLimit.getEnable())) {
//            Integer times = rateLimit.getTimes();
//            Integer seconds = rateLimit.getSeconds();
//            // Request count.
//            times = Optional.ofNullable(times).orElse(5);
//            // Request time window: 60 seconds.
//            seconds = Optional.ofNullable(seconds).orElse(60);
//            // Rate limit by user and API.
//            String key = "user:" + username + ":api:" + dataApiApplyEntity.getResourceId();
//            // Get the request count by key.
//            Integer maxTimes = (Integer) redisTemplate.opsForValue().get(key);
//            if (maxTimes == null) {
//                // Always set an expiration time when storing the value.
//                redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
//            } else if (maxTimes < times) {
//                redisTemplate.opsForValue().set(key, maxTimes + 1, seconds, TimeUnit.SECONDS);
//            } else {
//                throw new DataException("API calls are too frequent");
//            }
//        }
    }

    /**
     * Executes after processing completes.
     *
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params, Object value) throws Exception {
    }
}
