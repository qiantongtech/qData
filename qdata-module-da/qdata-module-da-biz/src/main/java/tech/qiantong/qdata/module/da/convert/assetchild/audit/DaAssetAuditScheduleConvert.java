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

package tech.qiantong.qdata.module.da.convert.assetchild.audit;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;

import java.util.List;

/**
 * Asset Audit Schedule Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditScheduleConvert {
    DaAssetAuditScheduleConvert INSTANCE = Mappers.getMapper(DaAssetAuditScheduleConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetAuditSchedulePageReqVO request parameters
     * @return DaAssetAuditScheduleDO
     */
     DaAssetAuditScheduleDO convertToDO(DaAssetAuditSchedulePageReqVO daAssetAuditSchedulePageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetAuditScheduleSaveReqVO save request parameters
     * @return DaAssetAuditScheduleDO
     */
     DaAssetAuditScheduleDO convertToDO(DaAssetAuditScheduleSaveReqVO daAssetAuditScheduleSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetAuditScheduleDO entity object
     * @return DaAssetAuditScheduleRespVO
     */
     DaAssetAuditScheduleRespVO convertToRespVO(DaAssetAuditScheduleDO daAssetAuditScheduleDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetAuditScheduleDOList entity object list
     * @return List<DaAssetAuditScheduleRespVO>
     */
     List<DaAssetAuditScheduleRespVO> convertToRespVOList(List<DaAssetAuditScheduleDO> daAssetAuditScheduleDOList);
}
