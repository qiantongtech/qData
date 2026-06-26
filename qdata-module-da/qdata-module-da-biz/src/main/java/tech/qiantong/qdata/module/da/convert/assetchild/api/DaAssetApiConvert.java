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
