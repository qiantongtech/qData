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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;

import java.util.List;

/**
 * 数据服务类目管理 Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttApiCatConvert {
    AttApiCatConvert INSTANCE = Mappers.getMapper(AttApiCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attApiCatPageReqVO 请求参数
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatPageReqVO attApiCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attApiCatSaveReqVO 保存请求参数
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatSaveReqVO attApiCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attApiCatDO 实体对象
     * @return AttApiCatRespVO
     */
     AttApiCatRespVO convertToRespVO(AttApiCatDO attApiCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attApiCatDOList 实体对象列表
     * @return List<AttApiCatRespVO>
     */
     List<AttApiCatRespVO> convertToRespVOList(List<AttApiCatDO> attApiCatDOList);
}
