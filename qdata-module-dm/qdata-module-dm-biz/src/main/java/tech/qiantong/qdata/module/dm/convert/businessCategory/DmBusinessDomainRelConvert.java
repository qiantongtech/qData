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
 * 业务分类数据域关联关系 Convert
 *
 * @author qdata
 * @date 2026-04-12
 */
@Mapper
public interface DmBusinessDomainRelConvert {
    DmBusinessDomainRelConvert INSTANCE = Mappers.getMapper(DmBusinessDomainRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmBusinessDomainRelPageReqVO 请求参数
     * @return DmBusinessDomainRelDO
     */
     DmBusinessDomainRelDO convertToDO(DmBusinessDomainRelPageReqVO dmBusinessDomainRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmBusinessDomainRelSaveReqVO 保存请求参数
     * @return DmBusinessDomainRelDO
     */
     DmBusinessDomainRelDO convertToDO(DmBusinessDomainRelSaveReqVO dmBusinessDomainRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmBusinessDomainRelDO 实体对象
     * @return DmBusinessDomainRelRespVO
     */
     DmBusinessDomainRelRespVO convertToRespVO(DmBusinessDomainRelDO dmBusinessDomainRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmBusinessDomainRelDOList 实体对象列表
     * @return List<DmBusinessDomainRelRespVO>
     */
     List<DmBusinessDomainRelRespVO> convertToRespVOList(List<DmBusinessDomainRelDO> dmBusinessDomainRelDOList);
}
