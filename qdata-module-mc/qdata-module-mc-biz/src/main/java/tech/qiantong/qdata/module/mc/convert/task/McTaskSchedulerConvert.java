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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;

import java.util.List;

/**
 * Data integration scheduling information Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskSchedulerConvert {
    McTaskSchedulerConvert INSTANCE = Mappers.getMapper(McTaskSchedulerConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskSchedulerPageReqVO request parameters
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerPageReqVO mcTaskSchedulerPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskSchedulerSaveReqVO save request parameters
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerSaveReqVO mcTaskSchedulerSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskSchedulerDO entity object
     * @return McTaskSchedulerRespVO
     */
     McTaskSchedulerRespVO convertToRespVO(McTaskSchedulerDO mcTaskSchedulerDO);

    /**
     * DOList to RespVOList
     * @param mcTaskSchedulerDOList entity object list
     * @return List<McTaskSchedulerRespVO>
     */
     List<McTaskSchedulerRespVO> convertToRespVOList(List<McTaskSchedulerDO> mcTaskSchedulerDOList);
}
