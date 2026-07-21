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


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.database.core.PageResult;
import tech.qiantong.qdata.common.utils.IPUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.ds.annotation.DsCheckClientToken;
import tech.qiantong.qdata.module.ds.async.AsyncTask;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.service.api.impl.ApiMappingEngine;
import tech.qiantong.qdata.module.ds.utils.DataTimeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestHandler {

    private static final String X_ACCESS_TOKEN = "Authorization";

    private RequestInterceptor requestInterceptor;

    private ApiMappingEngine apiMappingEngine;

    private ObjectMapper objectMapper;

    @Lazy
    @Resource
    private AsyncTask asyncTask;

//    @Autowired
//    private DataApiApplyServiceFeign dataApiApplyService;
//
//    @Autowired
//    SysUserServiceFeign sysUserService;


    public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    public void setApiMappingEngine(ApiMappingEngine apiMappingEngine) {
        this.apiMappingEngine = apiMappingEngine;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @ResponseBody
    @DsCheckClientToken
    public Object invoke(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable(required = false) Map<String, Object> pathVariables,
                         @RequestParam(required = false) Map<String, Object> requestParams,
                         @RequestBody(required = false) Map<String, Object> requestBodys) {

        LocalDateTime now = LocalDateTime.now();//Capture the request time.
        // Request timestamp used as the log start time.
        //Convert the timestamp.
        long timestamp = DataTimeUtil.timeByTimeStamp(now);

        DsApiDO api;
        Map<String, Object> params = new HashMap<>();
        if (MapUtil.isNotEmpty(pathVariables)) {
            log.info("pathVariables:{}", pathVariables.toString());
            params.putAll(pathVariables);
        }
        if (MapUtil.isNotEmpty(requestParams)) {
            log.info("requestParams:{}", requestParams.toString());
            params.putAll(requestParams);
        }
        if (MapUtil.isNotEmpty(requestBodys)) {
            log.info("requestBodys:{}", requestBodys.toString());
            params.putAll(requestBodys);
        }
        String msg = null;
        int caller_size = 0;
        Long api_id = 0L;
        String caller_url = "";
        String caller_params = "";
        String caller_ip = "";
        Long cat_id = null;
        String cat_code = "";
        try {
            api = MappingHandlerMapping.getMappingApiInfo(request);
            {//Build parameters.
                api_id = api.getId();
                cat_id = api.getCatId();
                cat_code = api.getCatCode();
                caller_params = JSON.toJSONString(api.getReqParams());
                caller_url = request.getRequestURI();
                caller_ip = IPUtil.getIpAddr(request);
            }

            // Serialize the result.
            api = objectMapper.readValue(objectMapper.writeValueAsString(api), DsApiDO.class);
            // Execute pre-interceptors.
            requestInterceptor.preHandle(request, response, api, params);

            //Create the response value.
            Object responseValuel;
            //Determine the API request type: 1 data service, 2 model data service, or 3 third-party API service.
            String isIntegrate = api.getApiServiceType();
            if (StringUtils.equals("3", isIntegrate)) {//Third-party API service.
                //Execute the request.
                apiMappingEngine.executeServiceForwarding(api, params, response);
                //The third-party response size is unknown, so default the called record count to 1.
                caller_size = 1;
                return null;
            } else if (StringUtils.equals("4", isIntegrate)) {//File service.
                //Return the file.
                apiMappingEngine.executeFileService(api, response);
                //The third-party response size is unknown, so default the called record count to 1.
                caller_size = 1;
                return null;
            } else {
                //Execute the request.
                Object value = apiMappingEngine.execute(api, params);
                try {
                    if(StringUtils.isNotEmpty(api.getResDataType())){
                        if(StringUtils.equals("1", api.getResDataType())){//A detail response contains one record.
                            caller_size = 1;
                        }else if(StringUtils.equals("2", api.getResDataType())){//List response.
                            List<Map<String, Object>> list = (List<Map<String, Object>>)value;
                            caller_size = list.size();
                        }else{//Paginated response.
                            PageResult<Map<String, Object>> r = (PageResult<Map<String, Object>>)value;
                            List<Map<String, Object>> data = r.getData();
                            if(StringUtils.isNotNull(data)){
                                caller_size = data.size();
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("Failed to count query result volume",e);
                }
                responseValuel = value;
                // Execute post-interceptors.
                requestInterceptor.postHandle(request, response, api, params, responseValuel);
                return AjaxResult.success(responseValuel);
            }
        } catch (Exception e) {
            msg = e.getMessage();
            throw e;
        } finally {
            //Create the log entity.
            DsApiLogDO apiLogDto = new DsApiLogDO();
            apiLogDto.setCallerStartDate(now);
            // Calculate the response time.
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - timestamp;
            //Elapsed time.
            apiLogDto.setCallerTime(responseTime);
            //Information record
            apiLogDto.setMsg(msg);
            //Determine whether the request succeeded.
            Integer status = 1;
            if (msg != null) {
                status = 0;
            }
            apiLogDto.setStatus(status);
            //Called record count
            apiLogDto.setCallerSize(caller_size);
            apiLogDto.setApiId(api_id);
            apiLogDto.setCallerUrl(caller_url);
            apiLogDto.setCallerParams(caller_params);
            apiLogDto.setCallerIp(caller_ip);
            apiLogDto.setCallerId("0");
            apiLogDto.setCallerBy("-");
            apiLogDto.setCatId(cat_id);
            apiLogDto.setCatCode(cat_code);
            log.info("asyncTask.doTask(apiLogDto);");
            // Record the API log asynchronously.
            asyncTask.doTask(apiLogDto);
        }
    }
}
