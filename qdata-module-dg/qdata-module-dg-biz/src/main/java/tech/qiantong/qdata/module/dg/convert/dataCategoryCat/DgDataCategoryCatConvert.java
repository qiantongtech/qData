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

package tech.qiantong.qdata.module.dg.convert.dataCategoryCat;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;

/**
 * 数据分类-类目 Convert
 *
 * @author FXB
 * @date 2026-04-07
 */
@Mapper
public interface DgDataCategoryCatConvert {
    DgDataCategoryCatConvert INSTANCE = Mappers.getMapper(DgDataCategoryCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDataCategoryCatPageReqVO 请求参数
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatPageReqVO dgDataCategoryCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDataCategoryCatSaveReqVO 保存请求参数
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatSaveReqVO dgDataCategoryCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDataCategoryCatDO 实体对象
     * @return DgDataCategoryCatRespVO
     */
     DgDataCategoryCatRespVO convertToRespVO(DgDataCategoryCatDO dgDataCategoryCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDataCategoryCatDOList 实体对象列表
     * @return List<DgDataCategoryCatRespVO>
     */
     List<DgDataCategoryCatRespVO> convertToRespVOList(List<DgDataCategoryCatDO> dgDataCategoryCatDOList);
}
