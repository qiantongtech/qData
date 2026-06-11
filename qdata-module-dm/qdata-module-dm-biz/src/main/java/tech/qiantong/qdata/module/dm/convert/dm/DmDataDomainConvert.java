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

package tech.qiantong.qdata.module.dm.convert.dm;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;

/**
 * 数据域管理 Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataDomainConvert {
    DmDataDomainConvert INSTANCE = Mappers.getMapper(DmDataDomainConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmDataDomainPageReqVO 请求参数
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainPageReqVO dmDataDomainPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmDataDomainSaveReqVO 保存请求参数
     * @return DmDataDomainDO
     */
     DmDataDomainDO convertToDO(DmDataDomainSaveReqVO dmDataDomainSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmDataDomainDO 实体对象
     * @return DmDataDomainRespVO
     */
     DmDataDomainRespVO convertToRespVO(DmDataDomainDO dmDataDomainDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmDataDomainDOList 实体对象列表
     * @return List<DmDataDomainRespVO>
     */
     List<DmDataDomainRespVO> convertToRespVOList(List<DmDataDomainDO> dmDataDomainDOList);
}
