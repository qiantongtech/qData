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
 */

package tech.qiantong.qdata.api.ds.service.etl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DSExecuteDTO;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlExecutorService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

/**
 * <P>
 * 用途:执行相关相关接口实现类
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:37
 **/
@Slf4j
@Service
public class DsEtlExecutorServiceImpl implements IDsEtlExecutorService {
    @Override
    public DsStatusRespDTO execute(DSExecuteDTO dsExecuteDTO, String projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.POST_EXECUTORS_EXECUTE;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCode(apiType.getUrl(), projectCode),
                apiType.getMethod(), JSONObject.parseObject(JSONObject.toJSONString(dsExecuteDTO)),
                DsStatusRespDTO.class);
    }
}
