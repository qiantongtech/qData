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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;

/**
 * 数仓分层-规范管理 Convert
 *
 * @author FXB
 * @date 2026-03-24
 */
@Mapper
public interface DmDataLayerSpecificationConvert {
    DmDataLayerSpecificationConvert INSTANCE = Mappers.getMapper(DmDataLayerSpecificationConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dmDataLayerSpecificationPageReqVO 请求参数
     * @return DmDataLayerSpecificationDO
     */
     DmDataLayerSpecificationDO convertToDO(DmDataLayerSpecificationPageReqVO dmDataLayerSpecificationPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dmDataLayerSpecificationSaveReqVO 保存请求参数
     * @return DmDataLayerSpecificationDO
     */
     DmDataLayerSpecificationDO convertToDO(DmDataLayerSpecificationSaveReqVO dmDataLayerSpecificationSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dmDataLayerSpecificationDO 实体对象
     * @return DmDataLayerSpecificationRespVO
     */
     DmDataLayerSpecificationRespVO convertToRespVO(DmDataLayerSpecificationDO dmDataLayerSpecificationDO);

    /**
     * DOList 转换为 RespVOList
     * @param dmDataLayerSpecificationDOList 实体对象列表
     * @return List<DmDataLayerSpecificationRespVO>
     */
     List<DmDataLayerSpecificationRespVO> convertToRespVOList(List<DmDataLayerSpecificationDO> dmDataLayerSpecificationDOList);
}
