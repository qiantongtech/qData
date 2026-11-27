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

package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;

import java.util.List;

/**
 * Collection task instance-Log Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskInstanceLogConvert {
    McTaskInstanceLogConvert INSTANCE = Mappers.getMapper(McTaskInstanceLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskInstanceLogPageReqVO request parameters
     * @return McTaskInstanceLogDO
     */
     McTaskInstanceLogDO convertToDO(McTaskInstanceLogPageReqVO mcTaskInstanceLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskInstanceLogSaveReqVO Save request parameters
     * @return McTaskInstanceLogDO
     */
     McTaskInstanceLogDO convertToDO(McTaskInstanceLogSaveReqVO mcTaskInstanceLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskInstanceLogDO entity object
     * @return McTaskInstanceLogRespVO
     */
     McTaskInstanceLogRespVO convertToRespVO(McTaskInstanceLogDO mcTaskInstanceLogDO);

    /**
     * DOList to RespVOList
     * @param mcTaskInstanceLogDOList entity object list
     * @return List<McTaskInstanceLogRespVO>
     */
     List<McTaskInstanceLogRespVO> convertToRespVOList(List<McTaskInstanceLogDO> mcTaskInstanceLogDOList);
}
