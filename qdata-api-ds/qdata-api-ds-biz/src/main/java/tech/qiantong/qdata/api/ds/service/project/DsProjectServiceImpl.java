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

package tech.qiantong.qdata.api.ds.service.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;
import tech.qiantong.qdata.api.ds.api.service.project.IDsProjectService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

/**
 * <P>
 * Description: DS project service implementation
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:27
 **/
@Slf4j
@Service
public class DsProjectServiceImpl implements IDsProjectService {

    @Override
    public DsProjectRespDTO saveProject(DsProjectCreateReqDTO dsProjectCreateReqDTO) {
        QianTongDCApiType apiType = QianTongDCApiType.CREATE_PROJECT;
        return DsRequestUtils.request(apiType.getUrl(),
                apiType.getMethod(),
                dsProjectCreateReqDTO, null,
                DsProjectRespDTO.class);
    }

    @Override
    public DsProjectRespDTO updateProject(DsProjectUpdateReqDTO dsProjectUpdateReqDTO) {
        QianTongDCApiType apiType = QianTongDCApiType.UPDATE_PROJECT;
        return DsRequestUtils.request(StringUtils.replace(apiType.getUrl(), "{code}", String.valueOf(dsProjectUpdateReqDTO.getProjectCode())),
                apiType.getMethod(),
                dsProjectUpdateReqDTO, null,
                DsProjectRespDTO.class);
    }

    @Override
    public DsProjectDeleteRespDTO deleteProject(Long projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.DELETE_PROJECT;
        return DsRequestUtils.request(StringUtils.replace(apiType.getUrl(), "{code}", String.valueOf(projectCode)),
                apiType.getMethod(),
                null, null,
                DsProjectDeleteRespDTO.class);
    }
}
