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
