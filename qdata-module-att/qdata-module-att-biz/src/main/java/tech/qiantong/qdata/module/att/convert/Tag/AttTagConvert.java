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

package tech.qiantong.qdata.module.att.convert.Tag;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;

import java.util.List;

/**
 * Tag Management Convert
 *
 * @author qdata
 * @date 2025-07-11
 */
@Mapper
public interface AttTagConvert {
    AttTagConvert INSTANCE = Mappers.getMapper(AttTagConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attTagPageReqVO Request parameters
     * @return AttTagDO
     */
     AttTagDO convertToDO(AttTagPageReqVO attTagPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attTagSaveReqVO Save request parameters
     * @return AttTagDO
     */
     AttTagDO convertToDO(AttTagSaveReqVO attTagSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attTagDO Entity object
     * @return AttTagRespVO
     */
     AttTagRespVO convertToRespVO(AttTagDO attTagDO);

    /**
     * Convert DOList to RespVOList
     * @param attTagDOList Entity object list
     * @return List<AttTagRespVO>
     */
     List<AttTagRespVO> convertToRespVOList(List<AttTagDO> attTagDOList);
}
