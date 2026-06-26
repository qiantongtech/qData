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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;

import java.util.List;

/**
 * 评测规则结果 Convert
 *
 * @author qdata
 * @date 2025-07-21
 */
@Mapper
public interface DppEvaluateLogConvert {
    DppEvaluateLogConvert INSTANCE = Mappers.getMapper(DppEvaluateLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEvaluateLogPageReqVO 请求参数
     * @return DppEvaluateLogDO
     */
     DppEvaluateLogDO convertToDO(DppEvaluateLogPageReqVO dppEvaluateLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEvaluateLogSaveReqVO 保存请求参数
     * @return DppEvaluateLogDO
     */
     DppEvaluateLogDO convertToDO(DppEvaluateLogSaveReqVO dppEvaluateLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEvaluateLogDO 实体对象
     * @return DppEvaluateLogRespVO
     */
     DppEvaluateLogRespVO convertToRespVO(DppEvaluateLogDO dppEvaluateLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEvaluateLogDOList 实体对象列表
     * @return List<DppEvaluateLogRespVO>
     */
     List<DppEvaluateLogRespVO> convertToRespVOList(List<DppEvaluateLogDO> dppEvaluateLogDOList);
}
