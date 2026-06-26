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

package tech.qiantong.qdata.module.da.convert.datasource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;

import java.util.List;

/**
 * 数据源 Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DaDatasourceConvert {
    DaDatasourceConvert INSTANCE = Mappers.getMapper(DaDatasourceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daDatasourcePageReqVO 请求参数
     * @return DaDatasourceDO
     */
     DaDatasourceDO convertToDO(DaDatasourcePageReqVO daDatasourcePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daDatasourceSaveReqVO 保存请求参数
     * @return DaDatasourceDO
     */
     DaDatasourceDO convertToDO(DaDatasourceSaveReqVO daDatasourceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daDatasourceDO 实体对象
     * @return DaDatasourceRespVO
     */
     DaDatasourceRespVO convertToRespVO(DaDatasourceDO daDatasourceDO);

    /**
     * DOList 转换为 RespVOList
     * @param daDatasourceDOList 实体对象列表
     * @return List<DaDatasourceRespVO>
     */
     List<DaDatasourceRespVO> convertToRespVOList(List<DaDatasourceDO> daDatasourceDOList);
}
