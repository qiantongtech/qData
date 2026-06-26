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
 * 数据分类 Convert
 *
 * @author qdata
 * @date 2026-04-07
 */
@Mapper
public interface DgDataCategoryConvert {
    DgDataCategoryConvert INSTANCE = Mappers.getMapper(DgDataCategoryConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDataCategoryPageReqVO 请求参数
     * @return DgDataCategoryDO
     */
     DgDataCategoryDO convertToDO(DgDataCategoryPageReqVO dgDataCategoryPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDataCategorySaveReqVO 保存请求参数
     * @return DgDataCategoryDO
     */
     DgDataCategoryDO convertToDO(DgDataCategorySaveReqVO dgDataCategorySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDataCategoryDO 实体对象
     * @return DgDataCategoryRespVO
     */
     DgDataCategoryRespVO convertToRespVO(DgDataCategoryDO dgDataCategoryDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDataCategoryDOList 实体对象列表
     * @return List<DgDataCategoryRespVO>
     */
     List<DgDataCategoryRespVO> convertToRespVOList(List<DgDataCategoryDO> dgDataCategoryDOList);
}
