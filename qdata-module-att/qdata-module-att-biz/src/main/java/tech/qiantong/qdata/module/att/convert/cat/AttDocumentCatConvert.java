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
