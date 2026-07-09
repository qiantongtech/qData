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

package tech.qiantong.qdata.module.da.convert.assetchild.projectRel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;

import java.util.List;

/**
 * Data Asset and Project Relationship Convert
 *
 * @author qdata
 * @date 2025-04-18
 */
@Mapper
public interface DaAssetProjectRelConvert {
    DaAssetProjectRelConvert INSTANCE = Mappers.getMapper(DaAssetProjectRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param daAssetProjectRelPageReqVO request parameters
     * @return DaAssetProjectRelDO
     */
     DaAssetProjectRelDO convertToDO(DaAssetProjectRelPageReqVO daAssetProjectRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param daAssetProjectRelSaveReqVO save request parameters
     * @return DaAssetProjectRelDO
     */
     DaAssetProjectRelDO convertToDO(DaAssetProjectRelSaveReqVO daAssetProjectRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param daAssetProjectRelDO entity object
     * @return DaAssetProjectRelRespVO
     */
     DaAssetProjectRelRespVO convertToRespVO(DaAssetProjectRelDO daAssetProjectRelDO);

    /**
     * Convert DOList to RespVOList
     * @param daAssetProjectRelDOList entity object list
     * @return List<DaAssetProjectRelRespVO>
     */
     List<DaAssetProjectRelRespVO> convertToRespVOList(List<DaAssetProjectRelDO> daAssetProjectRelDOList);
}
