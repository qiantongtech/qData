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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.List;

/**
 * 数据集成节点实例 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeInstanceConvert {
    DppEtlNodeInstanceConvert INSTANCE = Mappers.getMapper(DppEtlNodeInstanceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlNodeInstancePageReqVO 请求参数
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstancePageReqVO dppEtlNodeInstancePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlNodeInstanceSaveReqVO 保存请求参数
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstanceSaveReqVO dppEtlNodeInstanceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlNodeInstanceDO 实体对象
     * @return DppEtlNodeInstanceRespVO
     */
     DppEtlNodeInstanceRespVO convertToRespVO(DppEtlNodeInstanceDO dppEtlNodeInstanceDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlNodeInstanceDOList 实体对象列表
     * @return List<DppEtlNodeInstanceRespVO>
     */
     List<DppEtlNodeInstanceRespVO> convertToRespVOList(List<DppEtlNodeInstanceDO> dppEtlNodeInstanceDOList);
}
