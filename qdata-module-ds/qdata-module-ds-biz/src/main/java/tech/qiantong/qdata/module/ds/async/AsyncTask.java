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

package tech.qiantong.qdata.module.ds.async;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.handler.MappingHandlerMapping;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;
import tech.qiantong.qdata.module.ds.service.apiLog.IDsApiLogService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Saves API service logs asynchronously.
 */
@Slf4j
@Component
public class AsyncTask {

    @Autowired
    private IDsApiLogService apiLogService;


    @Autowired
    private MappingHandlerMapping mappingHandlerMapping;

    @Lazy
    @Resource
    private IDsApiService iDsApiService;

    private static String HANDLER_RELEASE = "1";
    private static String HANDLER_CANCEL = "2";

    /**
     * Saves logs asynchronously.
     * @param apiLogDto
     */
    @Async("threadPoolTaskExecutor")
    public void doTask(DsApiLogDO apiLogDto) {
        apiLogService.save(apiLogDto);
    }


    @Async("threadPoolTaskExecutor")
    public void releaseOrCancelDataApi(Map<String, Object> map) {
        try {
            String id =(String) map.get("id");
            String type = (String) map.get("type");//0: cancel, 1: publish
            DsApiDO dsApiById = iDsApiService.getDsApiById(Long.valueOf(id));
            if (dsApiById != null) {
                if (HANDLER_RELEASE.equals(type)) {
                    mappingHandlerMapping.registerMapping(dsApiById);
                } else if (HANDLER_CANCEL.equals(type)) {
                    mappingHandlerMapping.unregisterMapping(dsApiById);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
