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

package tech.qiantong.qdata.module.dp.convert.codeMap;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;

import java.util.List;

/**
 * 数据元代码映射 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpCodeMapConvert {
    DpCodeMapConvert INSTANCE = Mappers.getMapper(DpCodeMapConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpCodeMapPageReqVO 请求参数
     * @return DpCodeMapDO
     */
     DpCodeMapDO convertToDO(DpCodeMapPageReqVO dpCodeMapPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpCodeMapSaveReqVO 保存请求参数
     * @return DpCodeMapDO
     */
     DpCodeMapDO convertToDO(DpCodeMapSaveReqVO dpCodeMapSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpCodeMapDO 实体对象
     * @return DpCodeMapRespVO
     */
     DpCodeMapRespVO convertToRespVO(DpCodeMapDO dpCodeMapDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpCodeMapDOList 实体对象列表
     * @return List<DpCodeMapRespVO>
     */
     List<DpCodeMapRespVO> convertToRespVOList(List<DpCodeMapDO> dpCodeMapDOList);
}
