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
 * Description: DS data integration node related interface implementation
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
