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
 * 业务分类 Convert
 *
 * @author qdata
 * @date 2026-04-08
 */
@Mapper
public interface DmBusinessCategoryConvert {
    DmBusinessCategoryConvert INSTANCE = Mappers.getMapper(DmBusinessCategoryConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmBusinessCategoryPageReqVO 请求参数
     * @return DmBusinessCategoryDO
     */
     DmBusinessCategoryDO convertToDO(DmBusinessCategoryPageReqVO dmBusinessCategoryPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmBusinessCategorySaveReqVO 保存请求参数
     * @return DmBusinessCategoryDO
     */
     DmBusinessCategoryDO convertToDO(DmBusinessCategorySaveReqVO dmBusinessCategorySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmBusinessCategoryDO 实体对象
     * @return DmBusinessCategoryRespVO
     */
     DmBusinessCategoryRespVO convertToRespVO(DmBusinessCategoryDO dmBusinessCategoryDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmBusinessCategoryDOList 实体对象列表
     * @return List<DmBusinessCategoryRespVO>
     */
     List<DmBusinessCategoryRespVO> convertToRespVOList(List<DmBusinessCategoryDO> dmBusinessCategoryDOList);
}
