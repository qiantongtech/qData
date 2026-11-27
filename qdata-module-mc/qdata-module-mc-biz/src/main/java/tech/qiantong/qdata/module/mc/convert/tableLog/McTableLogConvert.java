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

package tech.qiantong.qdata.module.mc.convert.tableLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;

import java.util.List;

/**
 * Metadata Information - Log Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableLogConvert {
    McTableLogConvert INSTANCE = Mappers.getMapper(McTableLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTableLogPageReqVO request parameters
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogPageReqVO mcTableLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableLogSaveReqVO save request parameters
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogSaveReqVO mcTableLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableLogDO entity object
     * @return McTableLogRespVO
     */
     McTableLogRespVO convertToRespVO(McTableLogDO mcTableLogDO);

    /**
     * DOList to RespVOList
     * @param mcTableLogDOList entity object list
     * @return List<McTableLogRespVO>
     */
     List<McTableLogRespVO> convertToRespVOList(List<McTableLogDO> mcTableLogDOList);
}
