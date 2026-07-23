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

package tech.qiantong.qdata.module.dm.convert.dm;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;

/**
 * Data Warehouse Layer Specification Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataLayerSpecificationConvert {
    DmDataLayerSpecificationConvert INSTANCE = Mappers.getMapper(DmDataLayerSpecificationConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dmDataLayerSpecificationPageReqVO Request params
     * @return DmDataLayerSpecificationDO
     */
     DmDataLayerSpecificationDO convertToDO(DmDataLayerSpecificationPageReqVO dmDataLayerSpecificationPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dmDataLayerSpecificationSaveReqVO Save request params
     * @return DmDataLayerSpecificationDO
     */
     DmDataLayerSpecificationDO convertToDO(DmDataLayerSpecificationSaveReqVO dmDataLayerSpecificationSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dmDataLayerSpecificationDO Entity object
     * @return DmDataLayerSpecificationRespVO
     */
     DmDataLayerSpecificationRespVO convertToRespVO(DmDataLayerSpecificationDO dmDataLayerSpecificationDO);

    /**
     * Convert DO List to RespVO List
     * @param dmDataLayerSpecificationDOList Entity object list
     * @return List<DmDataLayerSpecificationRespVO>
     */
     List<DmDataLayerSpecificationRespVO> convertToRespVOList(List<DmDataLayerSpecificationDO> dmDataLayerSpecificationDOList);
}
