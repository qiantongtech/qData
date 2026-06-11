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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;

import java.util.List;

/**
 * 数据质量类目 Convert
 *
 * @author qdata
 * @date 2025-07-19
 */
@Mapper
public interface AttQualityCatConvert {
    AttQualityCatConvert INSTANCE = Mappers.getMapper(AttQualityCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attQualityCatPageReqVO 请求参数
     * @return AttQualityCatDO
     */
     AttQualityCatDO convertToDO(AttQualityCatPageReqVO attQualityCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attQualityCatSaveReqVO 保存请求参数
     * @return AttQualityCatDO
     */
     AttQualityCatDO convertToDO(AttQualityCatSaveReqVO attQualityCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attQualityCatDO 实体对象
     * @return AttQualityCatRespVO
     */
     AttQualityCatRespVO convertToRespVO(AttQualityCatDO attQualityCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attQualityCatDOList 实体对象列表
     * @return List<AttQualityCatRespVO>
     */
     List<AttQualityCatRespVO> convertToRespVOList(List<AttQualityCatDO> attQualityCatDOList);
}
