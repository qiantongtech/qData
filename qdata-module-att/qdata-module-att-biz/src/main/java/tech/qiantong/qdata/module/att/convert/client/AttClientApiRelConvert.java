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

package tech.qiantong.qdata.module.att.convert.client;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;

import java.util.List;

/**
 * 应用API服务关联 Convert
 *
 * @author FXB
 * @date 2025-08-21
 */
@Mapper
public interface AttClientApiRelConvert {
    AttClientApiRelConvert INSTANCE = Mappers.getMapper(AttClientApiRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attClientApiRelPageReqVO 请求参数
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelPageReqVO attClientApiRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attClientApiRelSaveReqVO 保存请求参数
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelSaveReqVO attClientApiRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attClientApiRelDO 实体对象
     * @return AttClientApiRelRespVO
     */
     AttClientApiRelRespVO convertToRespVO(AttClientApiRelDO attClientApiRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param attClientApiRelDOList 实体对象列表
     * @return List<AttClientApiRelRespVO>
     */
     List<AttClientApiRelRespVO> convertToRespVOList(List<AttClientApiRelDO> attClientApiRelDOList);
}
