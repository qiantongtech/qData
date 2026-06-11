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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;

import java.util.List;

/**
 * 数据元类目管理 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttDataElemCatConvert {
    AttDataElemCatConvert INSTANCE = Mappers.getMapper(AttDataElemCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attDataElemCatPageReqVO 请求参数
     * @return AttDataElemCatDO
     */
     AttDataElemCatDO convertToDO(AttDataElemCatPageReqVO attDataElemCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attDataElemCatSaveReqVO 保存请求参数
     * @return AttDataElemCatDO
     */
     AttDataElemCatDO convertToDO(AttDataElemCatSaveReqVO attDataElemCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attDataElemCatDO 实体对象
     * @return AttDataElemCatRespVO
     */
     AttDataElemCatRespVO convertToRespVO(AttDataElemCatDO attDataElemCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attDataElemCatDOList 实体对象列表
     * @return List<AttDataElemCatRespVO>
     */
     List<AttDataElemCatRespVO> convertToRespVOList(List<AttDataElemCatDO> attDataElemCatDOList);
}
