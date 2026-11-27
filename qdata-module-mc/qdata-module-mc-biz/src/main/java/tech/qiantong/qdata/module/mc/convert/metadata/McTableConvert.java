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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;

import java.util.List;

/**
 * Metadata information Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McTableConvert {
    McTableConvert INSTANCE = Mappers.getMapper(McTableConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTablePageReqVO request parameters
     * @return McTableDO
     */
     McTableDO convertToDO(McTablePageReqVO mcTablePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableSaveReqVO save request parameters
     * @return McTableDO
     */
     McTableDO convertToDO(McTableSaveReqVO mcTableSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableDO entity object
     * @return McTableRespVO
     */
     McTableRespVO convertToRespVO(McTableDO mcTableDO);

    /**
     * DOList to RespVOList
     * @param mcTableDOList entity object list
     * @return List<McTableRespVO>
     */
     List<McTableRespVO> convertToRespVOList(List<McTableDO> mcTableDOList);
}
