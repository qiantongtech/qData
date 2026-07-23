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
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;

import java.util.List;

/**
 * Project Convert
 *
 * @author shu
 * @date 2025-01-20
 */
@Mapper
public interface AttProjectConvert {
    AttProjectConvert INSTANCE = Mappers.getMapper(AttProjectConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param attProjectPageReqVO Request parameters
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectPageReqVO attProjectPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param attProjectSaveReqVO Save request parameters
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectSaveReqVO attProjectSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param attProjectDO Entity object
     * @return AttProjectRespVO
     */
     AttProjectRespVO convertToRespVO(AttProjectDO attProjectDO);

    /**
     * Convert DOList to RespVOList
     * @param attProjectDOList Entity object list
     * @return List<AttProjectRespVO>
     */
     List<AttProjectRespVO> convertToRespVOList(List<AttProjectDO> attProjectDOList);
}
