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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;

import java.util.List;

/**
 * Collection task Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskConvert {
    McTaskConvert INSTANCE = Mappers.getMapper(McTaskConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskPageReqVO request parameters
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskPageReqVO mcTaskPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskSaveReqVO save request parameters
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskSaveReqVO mcTaskSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskDO entity object
     * @return McTaskRespVO
     */
     McTaskRespVO convertToRespVO(McTaskDO mcTaskDO);

    /**
     * DOList to RespVOList
     * @param mcTaskDOList entity object list
     * @return List<McTaskRespVO>
     */
     List<McTaskRespVO> convertToRespVOList(List<McTaskDO> mcTaskDOList);
}
