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

package tech.qiantong.qdata.module.ds.convert.apiLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;

import java.util.List;

/**
 * API service call log converter
 *
 * @author lhs
 * @date 2025-02-12
 */
@Mapper
public interface DsApiLogConvert {
    DsApiLogConvert INSTANCE = Mappers.getMapper(DsApiLogConvert.class);

    /**
     * Converts a PageReqVO to a DO.
     * @param dsApiLogPageReqVO request parameters
     * @return DsApiLogDO
     */
     DsApiLogDO convertToDO(DsApiLogPageReqVO dsApiLogPageReqVO);

    /**
     * Converts a SaveReqVO to a DO.
     * @param dsApiLogSaveReqVO save request parameters
     * @return DsApiLogDO
     */
     DsApiLogDO convertToDO(DsApiLogSaveReqVO dsApiLogSaveReqVO);

    /**
     * Converts a DO to a RespVO.
     * @param dsApiLogDO entity
     * @return DsApiLogRespVO
     */
     DsApiLogRespVO convertToRespVO(DsApiLogDO dsApiLogDO);

    /**
     * Converts a DO list to a RespVO list.
     * @param dsApiLogDOList entity list
     * @return List<DsApiLogRespVO>
     */
     List<DsApiLogRespVO> convertToRespVOList(List<DsApiLogDO> dsApiLogDOList);
}
