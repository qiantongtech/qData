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

package tech.qiantong.qdata.module.dg.convert.whitelist;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;

/**
 * Desensitization Whitelist User Association Convert
 *
 * @author qdata
 * @date 2026-04-09
 */
@Mapper
public interface DgDesensitizeUserRelConvert {
    DgDesensitizeUserRelConvert INSTANCE = Mappers.getMapper(DgDesensitizeUserRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dgDesensitizeUserRelPageReqVO request params
     * @return DgDesensitizeUserRelDO
     */
     DgDesensitizeUserRelDO convertToDO(DgDesensitizeUserRelPageReqVO dgDesensitizeUserRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dgDesensitizeUserRelSaveReqVO save request params
     * @return DgDesensitizeUserRelDO
     */
     DgDesensitizeUserRelDO convertToDO(DgDesensitizeUserRelSaveReqVO dgDesensitizeUserRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dgDesensitizeUserRelDO entity object
     * @return DgDesensitizeUserRelRespVO
     */
     DgDesensitizeUserRelRespVO convertToRespVO(DgDesensitizeUserRelDO dgDesensitizeUserRelDO);

    /**
     * Convert DO List to RespVO List
     * @param dgDesensitizeUserRelDOList entity object list
     * @return List<DgDesensitizeUserRelRespVO>
     */
     List<DgDesensitizeUserRelRespVO> convertToRespVOList(List<DgDesensitizeUserRelDO> dgDesensitizeUserRelDOList);
}
