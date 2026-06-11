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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;

/**
 * 主题域管理 Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmThemeDomainConvert {
    DmThemeDomainConvert INSTANCE = Mappers.getMapper(DmThemeDomainConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmThemeDomainPageReqVO 请求参数
     * @return DmThemeDomainDO
     */
     DmThemeDomainDO convertToDO(DmThemeDomainPageReqVO dmThemeDomainPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmThemeDomainSaveReqVO 保存请求参数
     * @return DmThemeDomainDO
     */
     DmThemeDomainDO convertToDO(DmThemeDomainSaveReqVO dmThemeDomainSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmThemeDomainDO 实体对象
     * @return DmThemeDomainRespVO
     */
     DmThemeDomainRespVO convertToRespVO(DmThemeDomainDO dmThemeDomainDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmThemeDomainDOList 实体对象列表
     * @return List<DmThemeDomainRespVO>
     */
     List<DmThemeDomainRespVO> convertToRespVOList(List<DmThemeDomainDO> dmThemeDomainDOList);
}
