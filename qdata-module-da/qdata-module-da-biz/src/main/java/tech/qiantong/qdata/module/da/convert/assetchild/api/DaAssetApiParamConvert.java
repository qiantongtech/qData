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

package tech.qiantong.qdata.module.da.convert.assetchild.api;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;

import java.util.List;

/**
 * 数据资产-外部API-参数 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetApiParamConvert {
    DaAssetApiParamConvert INSTANCE = Mappers.getMapper(DaAssetApiParamConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetApiParamPageReqVO 请求参数
     * @return DaAssetApiParamDO
     */
     DaAssetApiParamDO convertToDO(DaAssetApiParamPageReqVO daAssetApiParamPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetApiParamSaveReqVO 保存请求参数
     * @return DaAssetApiParamDO
     */
     DaAssetApiParamDO convertToDO(DaAssetApiParamSaveReqVO daAssetApiParamSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetApiParamDO 实体对象
     * @return DaAssetApiParamRespVO
     */
     DaAssetApiParamRespVO convertToRespVO(DaAssetApiParamDO daAssetApiParamDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetApiParamDOList 实体对象列表
     * @return List<DaAssetApiParamRespVO>
     */
     List<DaAssetApiParamRespVO> convertToRespVOList(List<DaAssetApiParamDO> daAssetApiParamDOList);
}
