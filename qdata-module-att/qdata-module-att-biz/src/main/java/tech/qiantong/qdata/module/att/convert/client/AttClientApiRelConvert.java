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
 * App API Service Association Convert
 *
 * @author FXB
 * @date 2025-08-21
 */
@Mapper
public interface AttClientApiRelConvert {
    AttClientApiRelConvert INSTANCE = Mappers.getMapper(AttClientApiRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attClientApiRelPageReqVO Request parameters
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelPageReqVO attClientApiRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attClientApiRelSaveReqVO Save request parameters
     * @return AttClientApiRelDO
     */
     AttClientApiRelDO convertToDO(AttClientApiRelSaveReqVO attClientApiRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attClientApiRelDO Entity object
     * @return AttClientApiRelRespVO
     */
     AttClientApiRelRespVO convertToRespVO(AttClientApiRelDO attClientApiRelDO);

    /**
     * Convert DOList to RespVOList
     * @param attClientApiRelDOList Entity object list
     * @return List<AttClientApiRelRespVO>
     */
     List<AttClientApiRelRespVO> convertToRespVOList(List<AttClientApiRelDO> attClientApiRelDOList);
}
