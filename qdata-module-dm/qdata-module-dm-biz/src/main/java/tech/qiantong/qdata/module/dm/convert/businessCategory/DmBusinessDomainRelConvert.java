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

package tech.qiantong.qdata.module.dm.convert.businessCategory;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;

import java.util.List;

/**
 * Business Category Domain Relation Convert
 *
 * @author qdata
 * @date 2026-04-12
 */
@Mapper
public interface DmBusinessDomainRelConvert {
    DmBusinessDomainRelConvert INSTANCE = Mappers.getMapper(DmBusinessDomainRelConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dmBusinessDomainRelPageReqVO Request params
     * @return DmBusinessDomainRelDO
     */
     DmBusinessDomainRelDO convertToDO(DmBusinessDomainRelPageReqVO dmBusinessDomainRelPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dmBusinessDomainRelSaveReqVO Save request params
     * @return DmBusinessDomainRelDO
     */
     DmBusinessDomainRelDO convertToDO(DmBusinessDomainRelSaveReqVO dmBusinessDomainRelSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dmBusinessDomainRelDO Entity object
     * @return DmBusinessDomainRelRespVO
     */
     DmBusinessDomainRelRespVO convertToRespVO(DmBusinessDomainRelDO dmBusinessDomainRelDO);

    /**
     * Convert DO List to RespVO List
     * @param dmBusinessDomainRelDOList Entity object list
     * @return List<DmBusinessDomainRelRespVO>
     */
     List<DmBusinessDomainRelRespVO> convertToRespVOList(List<DmBusinessDomainRelDO> dmBusinessDomainRelDOList);
}
