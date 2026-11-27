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
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.List;

/**
 * Metadata field information Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McColumnConvert {
    McColumnConvert INSTANCE = Mappers.getMapper(McColumnConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcColumnPageReqVO request parameters
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnPageReqVO mcColumnPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcColumnSaveReqVO save request parameters
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnSaveReqVO mcColumnSaveReqVO);

    /**
     * DO to RespVO
     * @param mcColumnDO entity object
     * @return McColumnRespDTO
     */
     McColumnRespVO convertToRespVO(McColumnDO mcColumnDO);

    /**
     * DOList to RespVOList
     * @param mcColumnDOList entity object list
     * @return List<McColumnRespDTO>
     */
     List<McColumnRespVO> convertToRespVOList(List<McColumnDO> mcColumnDOList);
}
