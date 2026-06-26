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
