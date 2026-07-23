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

package tech.qiantong.qdata.module.att.convert.Rel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;

import java.util.List;

/**
 * Tag-Asset Relationship Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagAssetRelConvert {
    AttTagAssetRelConvert INSTANCE = Mappers.getMapper(AttTagAssetRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attTagAssetRelPageReqVO Request parameters
     * @return AttTagAssetRelDO
     */
     AttTagAssetRelDO convertToDO(AttTagAssetRelPageReqVO attTagAssetRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attTagAssetRelSaveReqVO Save request parameters
     * @return AttTagAssetRelDO
     */
     AttTagAssetRelDO convertToDO(AttTagAssetRelSaveReqVO attTagAssetRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attTagAssetRelDO Entity object
     * @return AttTagAssetRelRespVO
     */
     AttTagAssetRelRespVO convertToRespVO(AttTagAssetRelDO attTagAssetRelDO);

    /**
     * Convert DOList to RespVOList
     * @param attTagAssetRelDOList Entity object list
     * @return List<AttTagAssetRelRespVO>
     */
     List<AttTagAssetRelRespVO> convertToRespVOList(List<AttTagAssetRelDO> attTagAssetRelDOList);
}
