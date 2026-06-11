/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
