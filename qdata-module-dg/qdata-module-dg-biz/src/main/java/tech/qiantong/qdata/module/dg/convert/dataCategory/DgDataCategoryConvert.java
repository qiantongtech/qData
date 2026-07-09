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

package tech.qiantong.qdata.module.dg.convert.dataCategory;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategorySaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;

/**
 * Data Category Convert
 *
 * @author qdata
 * @date 2026-04-07
 */
@Mapper
public interface DgDataCategoryConvert {
    DgDataCategoryConvert INSTANCE = Mappers.getMapper(DgDataCategoryConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDataCategoryPageReqVO request params
     * @return DgDataCategoryDO
     */
     DgDataCategoryDO convertToDO(DgDataCategoryPageReqVO dgDataCategoryPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDataCategorySaveReqVO save request params
     * @return DgDataCategoryDO
     */
     DgDataCategoryDO convertToDO(DgDataCategorySaveReqVO dgDataCategorySaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDataCategoryDO entity object
     * @return DgDataCategoryRespVO
     */
     DgDataCategoryRespVO convertToRespVO(DgDataCategoryDO dgDataCategoryDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDataCategoryDOList entity object list
     * @return List<DgDataCategoryRespVO>
     */
     List<DgDataCategoryRespVO> convertToRespVOList(List<DgDataCategoryDO> dgDataCategoryDOList);
}
