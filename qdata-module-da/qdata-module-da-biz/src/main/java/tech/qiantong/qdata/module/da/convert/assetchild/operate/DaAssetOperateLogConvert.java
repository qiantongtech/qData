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

package tech.qiantong.qdata.module.da.convert.assetchild.operate;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;

import java.util.List;

/**
 * Data Asset Operate Log Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetOperateLogConvert {
    DaAssetOperateLogConvert INSTANCE = Mappers.getMapper(DaAssetOperateLogConvert.class);

    /**
     * PageReqVO Convert to DO
     * @param daAssetOperateLogPageReqVO request parameters
     * @return DaAssetOperateLogDO
     */
     DaAssetOperateLogDO convertToDO(DaAssetOperateLogPageReqVO daAssetOperateLogPageReqVO);

    /**
     * SaveReqVO Convert to DO
     * @param daAssetOperateLogSaveReqVO save request parameters
     * @return DaAssetOperateLogDO
     */
     DaAssetOperateLogDO convertToDO(DaAssetOperateLogSaveReqVO daAssetOperateLogSaveReqVO);

    /**
     * DO Convert to RespVO
     * @param daAssetOperateLogDO entity object
     * @return DaAssetOperateLogRespVO
     */
     DaAssetOperateLogRespVO convertToRespVO(DaAssetOperateLogDO daAssetOperateLogDO);

    /**
     * DOList Convert to RespVOList
     * @param daAssetOperateLogDOList entity object list
     * @return List<DaAssetOperateLogRespVO>
     */
     List<DaAssetOperateLogRespVO> convertToRespVOList(List<DaAssetOperateLogDO> daAssetOperateLogDOList);
}
