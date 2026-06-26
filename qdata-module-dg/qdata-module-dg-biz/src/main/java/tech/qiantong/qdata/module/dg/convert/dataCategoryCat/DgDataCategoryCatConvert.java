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

package tech.qiantong.qdata.module.dg.convert.dataCategoryCat;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;

/**
 * 数据分类-类目 Convert
 *
 * @author FXB
 * @date 2026-04-07
 */
@Mapper
public interface DgDataCategoryCatConvert {
    DgDataCategoryCatConvert INSTANCE = Mappers.getMapper(DgDataCategoryCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dgDataCategoryCatPageReqVO 请求参数
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatPageReqVO dgDataCategoryCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dgDataCategoryCatSaveReqVO 保存请求参数
     * @return DgDataCategoryCatDO
     */
     DgDataCategoryCatDO convertToDO(DgDataCategoryCatSaveReqVO dgDataCategoryCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dgDataCategoryCatDO 实体对象
     * @return DgDataCategoryCatRespVO
     */
     DgDataCategoryCatRespVO convertToRespVO(DgDataCategoryCatDO dgDataCategoryCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param dgDataCategoryCatDOList 实体对象列表
     * @return List<DgDataCategoryCatRespVO>
     */
     List<DgDataCategoryCatRespVO> convertToRespVOList(List<DgDataCategoryCatDO> dgDataCategoryCatDOList);
}
