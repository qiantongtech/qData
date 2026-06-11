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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;

import java.util.List;

/**
 * 标签类目管理 Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagCatConvert {
    AttTagCatConvert INSTANCE = Mappers.getMapper(AttTagCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attTagCatPageReqVO 请求参数
     * @return AttTagCatDO
     */
     AttTagCatDO convertToDO(AttTagCatPageReqVO attTagCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attTagCatSaveReqVO 保存请求参数
     * @return AttTagCatDO
     */
     AttTagCatDO convertToDO(AttTagCatSaveReqVO attTagCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attTagCatDO 实体对象
     * @return AttTagCatRespVO
     */
     AttTagCatRespVO convertToRespVO(AttTagCatDO attTagCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attTagCatDOList 实体对象列表
     * @return List<AttTagCatRespVO>
     */
     List<AttTagCatRespVO> convertToRespVOList(List<AttTagCatDO> attTagCatDOList);
}
