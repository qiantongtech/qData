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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.etl.DsNodeGenCodeRespDTO;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:ds数据集成节点相关接口实现
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 17:01
 **/
@Slf4j
@Service
public class DsEtlNodeServiceImpl implements IDsEtlNodeService {
    @Override
    public DsNodeGenCodeRespDTO genCode(Long projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.GEN_TASK_DEFINITION_CODES;
        Map<String, Object> params = new HashMap<>();
        params.put("genNum", 1);
        return DsRequestUtils.request(apiType.getUrl(),
                apiType.getMethod(),
                null, params,
                DsNodeGenCodeRespDTO.class);
    }
}
