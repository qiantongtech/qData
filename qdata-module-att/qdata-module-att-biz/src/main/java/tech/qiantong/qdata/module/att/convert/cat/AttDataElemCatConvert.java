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
