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

package tech.qiantong.qdata.module.dp.convert.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;

import java.util.List;

/**
 * 物化模型记录 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpModelMaterializedConvert {
    DpModelMaterializedConvert INSTANCE = Mappers.getMapper(DpModelMaterializedConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpModelMaterializedPageReqVO 请求参数
     * @return DpModelMaterializedDO
     */
     DpModelMaterializedDO convertToDO(DpModelMaterializedPageReqVO dpModelMaterializedPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpModelMaterializedSaveReqVO 保存请求参数
     * @return DpModelMaterializedDO
     */
     DpModelMaterializedDO convertToDO(DpModelMaterializedSaveReqVO dpModelMaterializedSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpModelMaterializedDO 实体对象
     * @return DpModelMaterializedRespVO
     */
     DpModelMaterializedRespVO convertToRespVO(DpModelMaterializedDO dpModelMaterializedDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpModelMaterializedDOList 实体对象列表
     * @return List<DpModelMaterializedRespVO>
     */
     List<DpModelMaterializedRespVO> convertToRespVOList(List<DpModelMaterializedDO> dpModelMaterializedDOList);
}
