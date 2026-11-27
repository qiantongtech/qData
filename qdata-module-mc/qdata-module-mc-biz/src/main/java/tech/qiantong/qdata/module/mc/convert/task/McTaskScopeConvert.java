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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.List;

/**
 * Collection range Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskScopeConvert {
    McTaskScopeConvert INSTANCE = Mappers.getMapper(McTaskScopeConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskScopePageReqVO request parameters
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopePageReqVO mcTaskScopePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskScopeSaveReqVO save request parameters
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopeSaveReqVO mcTaskScopeSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskScopeDO entity object
     * @return McTaskScopeRespVO
     */
     McTaskScopeRespVO convertToRespVO(McTaskScopeDO mcTaskScopeDO);

    /**
     * DOList to RespVOList
     * @param mcTaskScopeDOList entity object list
     * @return List<McTaskScopeRespVO>
     */
     List<McTaskScopeRespVO> convertToRespVOList(List<McTaskScopeDO> mcTaskScopeDOList);
}
