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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 异步保存API服务日志
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
     * 异步保存日志
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
            String type = (String) map.get("type");//0:取消 1:发布
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
