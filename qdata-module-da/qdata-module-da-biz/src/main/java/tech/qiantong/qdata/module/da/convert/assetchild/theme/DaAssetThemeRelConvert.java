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

package tech.qiantong.qdata.module.da.convert.assetchild.theme;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme.DaAssetThemeRelDO;

import java.util.List;

/**
 * 数据资产-主题关联关系 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetThemeRelConvert {
    DaAssetThemeRelConvert INSTANCE = Mappers.getMapper(DaAssetThemeRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetThemeRelPageReqVO 请求参数
     * @return DaAssetThemeRelDO
     */
     DaAssetThemeRelDO convertToDO(DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetThemeRelSaveReqVO 保存请求参数
     * @return DaAssetThemeRelDO
     */
     DaAssetThemeRelDO convertToDO(DaAssetThemeRelSaveReqVO daAssetThemeRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetThemeRelDO 实体对象
     * @return DaAssetThemeRelRespVO
     */
     DaAssetThemeRelRespVO convertToRespVO(DaAssetThemeRelDO daAssetThemeRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetThemeRelDOList 实体对象列表
     * @return List<DaAssetThemeRelRespVO>
     */
     List<DaAssetThemeRelRespVO> convertToRespVOList(List<DaAssetThemeRelDO> daAssetThemeRelDOList);
}
