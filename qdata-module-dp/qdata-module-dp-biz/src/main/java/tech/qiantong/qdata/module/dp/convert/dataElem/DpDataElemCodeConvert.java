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

package tech.qiantong.qdata.module.dp.convert.dataElem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;

import java.util.List;

/**
 * 数据元代码 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpDataElemCodeConvert {
    DpDataElemCodeConvert INSTANCE = Mappers.getMapper(DpDataElemCodeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpDataElemCodePageReqVO 请求参数
     * @return DpDataElemCodeDO
     */
     DpDataElemCodeDO convertToDO(DpDataElemCodePageReqVO dpDataElemCodePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpDataElemCodeSaveReqVO 保存请求参数
     * @return DpDataElemCodeDO
     */
     DpDataElemCodeDO convertToDO(DpDataElemCodeSaveReqVO dpDataElemCodeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpDataElemCodeDO 实体对象
     * @return DpDataElemCodeRespVO
     */
     DpDataElemCodeRespVO convertToRespVO(DpDataElemCodeDO dpDataElemCodeDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpDataElemCodeDOList 实体对象列表
     * @return List<DpDataElemCodeRespVO>
     */
     List<DpDataElemCodeRespVO> convertToRespVOList(List<DpDataElemCodeDO> dpDataElemCodeDOList);
}
