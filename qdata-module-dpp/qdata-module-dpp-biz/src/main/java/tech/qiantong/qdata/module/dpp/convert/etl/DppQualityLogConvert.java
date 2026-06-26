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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;

import java.util.List;

/**
 * 数据质量日志 Convert
 *
 * @author qdata
 * @date 2025-07-19
 */
@Mapper
public interface DppQualityLogConvert {
    DppQualityLogConvert INSTANCE = Mappers.getMapper(DppQualityLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityLogPageReqVO 请求参数
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogPageReqVO dppQualityLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityLogSaveReqVO 保存请求参数
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogSaveReqVO dppQualityLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityLogDO 实体对象
     * @return DppQualityLogRespVO
     */
     DppQualityLogRespVO convertToRespVO(DppQualityLogDO dppQualityLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityLogDOList 实体对象列表
     * @return List<DppQualityLogRespVO>
     */
     List<DppQualityLogRespVO> convertToRespVOList(List<DppQualityLogDO> dppQualityLogDOList);
}
