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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;

import java.util.List;

/**
 * 数据集成SQL模版 Convert
 *
 * @author FXB
 * @date 2025-06-25
 */
@Mapper
public interface DppEtlSqlTempConvert {
    DppEtlSqlTempConvert INSTANCE = Mappers.getMapper(DppEtlSqlTempConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlSqlTempPageReqVO 请求参数
     * @return DppEtlSqlTempDO
     */
     DppEtlSqlTempDO convertToDO(DppEtlSqlTempPageReqVO dppEtlSqlTempPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlSqlTempSaveReqVO 保存请求参数
     * @return DppEtlSqlTempDO
     */
     DppEtlSqlTempDO convertToDO(DppEtlSqlTempSaveReqVO dppEtlSqlTempSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlSqlTempDO 实体对象
     * @return DppEtlSqlTempRespVO
     */
     DppEtlSqlTempRespVO convertToRespVO(DppEtlSqlTempDO dppEtlSqlTempDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlSqlTempDOList 实体对象列表
     * @return List<DppEtlSqlTempRespVO>
     */
     List<DppEtlSqlTempRespVO> convertToRespVOList(List<DppEtlSqlTempDO> dppEtlSqlTempDOList);
}
