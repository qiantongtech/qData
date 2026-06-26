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

package tech.qiantong.qdata.module.dp.convert.dataElem;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;

import java.util.List;

/**
 * 数据元 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpDataElemConvert {
    DpDataElemConvert INSTANCE = Mappers.getMapper(DpDataElemConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpDataElemPageReqVO 请求参数
     * @return DpDataElemDO
     */
     DpDataElemDO convertToDO(DpDataElemPageReqVO dpDataElemPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpDataElemSaveReqVO 保存请求参数
     * @return DpDataElemDO
     */
     DpDataElemDO convertToDO(DpDataElemSaveReqVO dpDataElemSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpDataElemDO 实体对象
     * @return DpDataElemRespVO
     */
     DpDataElemRespVO convertToRespVO(DpDataElemDO dpDataElemDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpDataElemDOList 实体对象列表
     * @return List<DpDataElemRespVO>
     */
     List<DpDataElemRespVO> convertToRespVOList(List<DpDataElemDO> dpDataElemDOList);
}
