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
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;

import java.util.List;

/**
 * Logical Model Column Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpModelColumnConvert {
    DpModelColumnConvert INSTANCE = Mappers.getMapper(DpModelColumnConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dpModelColumnPageReqVO Request params
     * @return DpModelColumnDO
     */
     DpModelColumnDO convertToDO(DpModelColumnPageReqVO dpModelColumnPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dpModelColumnSaveReqVO Save request params
     * @return DpModelColumnDO
     */
     DpModelColumnDO convertToDO(DpModelColumnSaveReqVO dpModelColumnSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dpModelColumnDO Entity object
     * @return DpModelColumnRespVO
     */
     DpModelColumnRespVO convertToRespVO(DpModelColumnDO dpModelColumnDO);

    /**
     * Convert DOList to RespVOList
     * @param dpModelColumnDOList Entity object list
     * @return List<DpModelColumnRespVO>
     */
     List<DpModelColumnRespVO> convertToRespVOList(List<DpModelColumnDO> dpModelColumnDOList);
}
