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

package tech.qiantong.qdata.module.att.convert.project;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;

import java.util.List;

/**
 * Project-User Relationship Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface AttProjectUserRelConvert {
    AttProjectUserRelConvert INSTANCE = Mappers.getMapper(AttProjectUserRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attProjectUserRelPageReqVO Request parameters
     * @return AttProjectUserRelDO
     */
     AttProjectUserRelDO convertToDO(AttProjectUserRelPageReqVO attProjectUserRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attProjectUserRelSaveReqVO Save request parameters
     * @return AttProjectUserRelDO
     */
     AttProjectUserRelDO convertToDO(AttProjectUserRelSaveReqVO attProjectUserRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attProjectUserRelDO Entity object
     * @return AttProjectUserRelRespVO
     */
     AttProjectUserRelRespVO convertToRespVO(AttProjectUserRelDO attProjectUserRelDO);

    /**
     * Convert DOList to RespVOList
     * @param attProjectUserRelDOList Entity object list
     * @return List<AttProjectUserRelRespVO>
     */
     List<AttProjectUserRelRespVO> convertToRespVOList(List<AttProjectUserRelDO> attProjectUserRelDOList);
}
