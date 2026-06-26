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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;

import java.util.List;

/**
 * 数据集成任务节点关系-日志 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskNodeRelLogConvert {
    DppEtlTaskNodeRelLogConvert INSTANCE = Mappers.getMapper(DppEtlTaskNodeRelLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskNodeRelLogPageReqVO 请求参数
     * @return DppEtlTaskNodeRelLogDO
     */
     DppEtlTaskNodeRelLogDO convertToDO(DppEtlTaskNodeRelLogPageReqVO dppEtlTaskNodeRelLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskNodeRelLogSaveReqVO 保存请求参数
     * @return DppEtlTaskNodeRelLogDO
     */
     DppEtlTaskNodeRelLogDO convertToDO(DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskNodeRelLogDO 实体对象
     * @return DppEtlTaskNodeRelLogRespVO
     */
     DppEtlTaskNodeRelLogRespVO convertToRespVO(DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskNodeRelLogDOList 实体对象列表
     * @return List<DppEtlTaskNodeRelLogRespVO>
     */
     List<DppEtlTaskNodeRelLogRespVO> convertToRespVOList(List<DppEtlTaskNodeRelLogDO> dppEtlTaskNodeRelLogDOList);
}
