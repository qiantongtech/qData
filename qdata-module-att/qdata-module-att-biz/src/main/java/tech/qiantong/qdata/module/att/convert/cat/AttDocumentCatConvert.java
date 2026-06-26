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

package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;

/**
 * 标准信息分类管理 Convert
 *
 * @author qdata
 * @date 2025-08-21
 */
@Mapper
public interface AttDocumentCatConvert {
    AttDocumentCatConvert INSTANCE = Mappers.getMapper(AttDocumentCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attDocumentCatPageReqVO 请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatPageReqVO attDocumentCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attDocumentCatSaveReqVO 保存请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatSaveReqVO attDocumentCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attDocumentCatDO 实体对象
     * @return AttDocumentCatRespVO
     */
     AttDocumentCatRespVO convertToRespVO(AttDocumentCatDO attDocumentCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attDocumentCatDOList 实体对象列表
     * @return List<AttDocumentCatRespVO>
     */
     List<AttDocumentCatRespVO> convertToRespVOList(List<AttDocumentCatDO> attDocumentCatDOList);
}
