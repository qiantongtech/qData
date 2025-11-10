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
 * 用途:ds项目service实现
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
