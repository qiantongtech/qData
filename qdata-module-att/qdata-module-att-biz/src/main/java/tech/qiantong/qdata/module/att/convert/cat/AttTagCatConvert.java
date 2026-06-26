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
