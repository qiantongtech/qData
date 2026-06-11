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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;

/**
 * 数仓分层管理 Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataLayerConvert {
    DmDataLayerConvert INSTANCE = Mappers.getMapper(DmDataLayerConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmDataLayerPageReqVO 请求参数
     * @return DmDataLayerDO
     */
     DmDataLayerDO convertToDO(DmDataLayerPageReqVO dmDataLayerPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmDataLayerSaveReqVO 保存请求参数
     * @return DmDataLayerDO
     */
     DmDataLayerDO convertToDO(DmDataLayerSaveReqVO dmDataLayerSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmDataLayerDO 实体对象
     * @return DmDataLayerRespVO
     */
     DmDataLayerRespVO convertToRespVO(DmDataLayerDO dmDataLayerDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmDataLayerDOList 实体对象列表
     * @return List<DmDataLayerRespVO>
     */
     List<DmDataLayerRespVO> convertToRespVOList(List<DmDataLayerDO> dmDataLayerDOList);
}
