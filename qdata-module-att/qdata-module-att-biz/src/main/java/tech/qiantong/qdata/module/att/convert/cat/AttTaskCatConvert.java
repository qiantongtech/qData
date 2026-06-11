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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;

import java.util.List;

/**
 * 数据集成任务类目管理 Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttTaskCatConvert {
    AttTaskCatConvert INSTANCE = Mappers.getMapper(AttTaskCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attTaskCatPageReqVO 请求参数
     * @return AttTaskCatDO
     */
     AttTaskCatDO convertToDO(AttTaskCatPageReqVO attTaskCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attTaskCatSaveReqVO 保存请求参数
     * @return AttTaskCatDO
     */
     AttTaskCatDO convertToDO(AttTaskCatSaveReqVO attTaskCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attTaskCatDO 实体对象
     * @return AttTaskCatRespVO
     */
     AttTaskCatRespVO convertToRespVO(AttTaskCatDO attTaskCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attTaskCatDOList 实体对象列表
     * @return List<AttTaskCatRespVO>
     */
     List<AttTaskCatRespVO> convertToRespVOList(List<AttTaskCatDO> attTaskCatDOList);
}
