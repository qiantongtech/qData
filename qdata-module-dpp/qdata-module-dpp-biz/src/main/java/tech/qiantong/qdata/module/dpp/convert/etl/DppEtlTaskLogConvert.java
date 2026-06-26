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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;

import java.util.List;

/**
 * 数据集成任务-日志 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskLogConvert {
    DppEtlTaskLogConvert INSTANCE = Mappers.getMapper(DppEtlTaskLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskLogPageReqVO 请求参数
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogPageReqVO dppEtlTaskLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskLogSaveReqVO 保存请求参数
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskLogDO 实体对象
     * @return DppEtlTaskLogRespVO
     */
     DppEtlTaskLogRespVO convertToRespVO(DppEtlTaskLogDO dppEtlTaskLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskLogDOList 实体对象列表
     * @return List<DppEtlTaskLogRespVO>
     */
     List<DppEtlTaskLogRespVO> convertToRespVOList(List<DppEtlTaskLogDO> dppEtlTaskLogDOList);
}
