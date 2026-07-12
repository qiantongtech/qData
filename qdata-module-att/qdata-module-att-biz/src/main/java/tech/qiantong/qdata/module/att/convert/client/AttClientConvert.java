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
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;

import java.util.List;

/**
 * App Management Convert
 *
 * @author qdata
 * @date 2025-02-18
 */
@Mapper
public interface AttClientConvert {
    AttClientConvert INSTANCE = Mappers.getMapper(AttClientConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attClientPageReqVO Request parameters
     * @return AttClientDO
     */
     AttClientDO convertToDO(AttClientPageReqVO attClientPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attClientSaveReqVO Save request parameters
     * @return AttClientDO
     */
     AttClientDO convertToDO(AttClientSaveReqVO attClientSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attClientDO Entity object
     * @return AttClientRespVO
     */
     AttClientRespVO convertToRespVO(AttClientDO attClientDO);

    /**
     * Convert DOList to RespVOList
     * @param attClientDOList Entity object list
     * @return List<AttClientRespVO>
     */
     List<AttClientRespVO> convertToRespVOList(List<AttClientDO> attClientDOList);
}
