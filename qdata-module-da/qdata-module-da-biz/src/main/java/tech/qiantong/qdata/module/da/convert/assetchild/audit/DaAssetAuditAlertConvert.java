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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;

import java.util.List;

/**
 * Data Asset - Quality Alert Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditAlertConvert {
    DaAssetAuditAlertConvert INSTANCE = Mappers.getMapper(DaAssetAuditAlertConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetAuditAlertPageReqVO request parameters
     * @return DaAssetAuditAlertDO
     */
     DaAssetAuditAlertDO convertToDO(DaAssetAuditAlertPageReqVO daAssetAuditAlertPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetAuditAlertSaveReqVO save request parameters
     * @return DaAssetAuditAlertDO
     */
     DaAssetAuditAlertDO convertToDO(DaAssetAuditAlertSaveReqVO daAssetAuditAlertSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetAuditAlertDO entity object
     * @return DaAssetAuditAlertRespVO
     */
     DaAssetAuditAlertRespVO convertToRespVO(DaAssetAuditAlertDO daAssetAuditAlertDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetAuditAlertDOList entity object list
     * @return List<DaAssetAuditAlertRespVO>
     */
     List<DaAssetAuditAlertRespVO> convertToRespVOList(List<DaAssetAuditAlertDO> daAssetAuditAlertDOList);
}
