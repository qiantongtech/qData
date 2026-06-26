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

package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;

import java.util.List;

/**
 * 数据集成任务 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskConvert {
    DppEtlTaskConvert INSTANCE = Mappers.getMapper(DppEtlTaskConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskPageReqVO 请求参数
     * @return DppEtlTaskDO
     */
     DppEtlTaskDO convertToDO(DppEtlTaskPageReqVO dppEtlTaskPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskSaveReqVO 保存请求参数
     * @return DppEtlTaskDO
     */
     DppEtlTaskDO convertToDO(DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskDO 实体对象
     * @return DppEtlTaskRespVO
     */
     DppEtlTaskRespVO convertToRespVO(DppEtlTaskDO dppEtlTaskDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskDOList 实体对象列表
     * @return List<DppEtlTaskRespVO>
     */
     List<DppEtlTaskRespVO> convertToRespVOList(List<DppEtlTaskDO> dppEtlTaskDOList);
}
