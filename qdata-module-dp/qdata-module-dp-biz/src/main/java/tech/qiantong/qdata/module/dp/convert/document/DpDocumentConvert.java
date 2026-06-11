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
