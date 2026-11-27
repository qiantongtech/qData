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

package tech.qiantong.qdata.module.mc.convert.tableColumnRelLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;

import java.util.List;

/**
 * Metadata database and information and field information relationship-Log Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableColumnRelLogConvert {
    McTableColumnRelLogConvert INSTANCE = Mappers.getMapper(McTableColumnRelLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTableColumnRelLogPageReqVO request parameters
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogPageReqVO mcTableColumnRelLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableColumnRelLogSaveReqVO Save request parameters
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogSaveReqVO mcTableColumnRelLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableColumnRelLogDO entity object
     * @return McTableColumnRelLogRespVO
     */
     McTableColumnRelLogRespVO convertToRespVO(McTableColumnRelLogDO mcTableColumnRelLogDO);

    /**
     * DOList to RespVOList
     * @param mcTableColumnRelLogDOList entity object list
     * @return List<McTableColumnRelLogRespVO>
     */
     List<McTableColumnRelLogRespVO> convertToRespVOList(List<McTableColumnRelLogDO> mcTableColumnRelLogDOList);
}
