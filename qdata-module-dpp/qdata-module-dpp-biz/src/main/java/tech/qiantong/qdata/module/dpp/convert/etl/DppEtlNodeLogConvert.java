/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
