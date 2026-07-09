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
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;

import java.util.List;

/**
 * Logical Model Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpModelConvert {
    DpModelConvert INSTANCE = Mappers.getMapper(DpModelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dpModelPageReqVO Request params
     * @return DpModelDO
     */
     DpModelDO convertToDO(DpModelPageReqVO dpModelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dpModelSaveReqVO Save request params
     * @return DpModelDO
     */
     DpModelDO convertToDO(DpModelSaveReqVO dpModelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dpModelDO Entity object
     * @return DpModelRespVO
     */
     DpModelRespVO convertToRespVO(DpModelDO dpModelDO);

    /**
     * Convert DOList to RespVOList
     * @param dpModelDOList Entity object list
     * @return List<DpModelRespVO>
     */
     List<DpModelRespVO> convertToRespVOList(List<DpModelDO> dpModelDOList);
}
