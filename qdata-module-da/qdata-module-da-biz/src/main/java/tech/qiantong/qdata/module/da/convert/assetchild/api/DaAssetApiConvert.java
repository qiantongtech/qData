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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;

import java.util.List;

/**
 * 数据资产-外部API Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetApiConvert {
    DaAssetApiConvert INSTANCE = Mappers.getMapper(DaAssetApiConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetApiPageReqVO 请求参数
     * @return DaAssetApiDO
     */
     DaAssetApiDO convertToDO(DaAssetApiPageReqVO daAssetApiPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetApiSaveReqVO 保存请求参数
     * @return DaAssetApiDO
     */
     DaAssetApiDO convertToDO(DaAssetApiSaveReqVO daAssetApiSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetApiDO 实体对象
     * @return DaAssetApiRespVO
     */
     DaAssetApiRespVO convertToRespVO(DaAssetApiDO daAssetApiDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetApiDOList 实体对象列表
     * @return List<DaAssetApiRespVO>
     */
     List<DaAssetApiRespVO> convertToRespVOList(List<DaAssetApiDO> daAssetApiDOList);
}
