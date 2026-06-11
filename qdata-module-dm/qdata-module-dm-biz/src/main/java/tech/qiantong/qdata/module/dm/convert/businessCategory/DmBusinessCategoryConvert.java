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
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
