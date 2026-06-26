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

package tech.qiantong.qdata.module.dp.convert.document;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;

import java.util.List;

/**
 * 标准信息登记 Convert
 *
 * @author qdata
 * @date 2025-08-21
 */
@Mapper
public interface DpDocumentConvert {
    DpDocumentConvert INSTANCE = Mappers.getMapper(DpDocumentConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpDocumentPageReqVO 请求参数
     * @return DpDocumentDO
     */
     DpDocumentDO convertToDO(DpDocumentPageReqVO dpDocumentPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpDocumentSaveReqVO 保存请求参数
     * @return DpDocumentDO
     */
     DpDocumentDO convertToDO(DpDocumentSaveReqVO dpDocumentSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpDocumentDO 实体对象
     * @return DpDocumentRespVO
     */
     DpDocumentRespVO convertToRespVO(DpDocumentDO dpDocumentDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpDocumentDOList 实体对象列表
     * @return List<DpDocumentRespVO>
     */
     List<DpDocumentRespVO> convertToRespVOList(List<DpDocumentDO> dpDocumentDOList);
}
