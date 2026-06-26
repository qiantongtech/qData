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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;

import java.util.List;

/**
 * 数据集成节点-日志 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeLogConvert {
    DppEtlNodeLogConvert INSTANCE = Mappers.getMapper(DppEtlNodeLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlNodeLogPageReqVO 请求参数
     * @return DppEtlNodeLogDO
     */
     DppEtlNodeLogDO convertToDO(DppEtlNodeLogPageReqVO dppEtlNodeLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlNodeLogSaveReqVO 保存请求参数
     * @return DppEtlNodeLogDO
     */
     DppEtlNodeLogDO convertToDO(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlNodeLogDO 实体对象
     * @return DppEtlNodeLogRespVO
     */
     DppEtlNodeLogRespVO convertToRespVO(DppEtlNodeLogDO dppEtlNodeLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlNodeLogDOList 实体对象列表
     * @return List<DppEtlNodeLogRespVO>
     */
     List<DppEtlNodeLogRespVO> convertToRespVOList(List<DppEtlNodeLogDO> dppEtlNodeLogDOList);
}
