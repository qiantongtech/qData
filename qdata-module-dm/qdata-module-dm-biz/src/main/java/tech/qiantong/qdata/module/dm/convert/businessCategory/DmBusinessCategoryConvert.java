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

package tech.qiantong.qdata.module.dm.convert.businessCategory;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;

import java.util.List;

/**
 * Business Category Convert
 *
 * @author qdata
 * @date 2026-04-08
 */
@Mapper
public interface DmBusinessCategoryConvert {
    DmBusinessCategoryConvert INSTANCE = Mappers.getMapper(DmBusinessCategoryConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dmBusinessCategoryPageReqVO Request params
     * @return DmBusinessCategoryDO
     */
     DmBusinessCategoryDO convertToDO(DmBusinessCategoryPageReqVO dmBusinessCategoryPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dmBusinessCategorySaveReqVO Save request params
     * @return DmBusinessCategoryDO
     */
     DmBusinessCategoryDO convertToDO(DmBusinessCategorySaveReqVO dmBusinessCategorySaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dmBusinessCategoryDO Entity object
     * @return DmBusinessCategoryRespVO
     */
     DmBusinessCategoryRespVO convertToRespVO(DmBusinessCategoryDO dmBusinessCategoryDO);

    /**
     * Convert DO List to RespVO List
     * @param dmBusinessCategoryDOList Entity object list
     * @return List<DmBusinessCategoryRespVO>
     */
     List<DmBusinessCategoryRespVO> convertToRespVOList(List<DmBusinessCategoryDO> dmBusinessCategoryDOList);
}
