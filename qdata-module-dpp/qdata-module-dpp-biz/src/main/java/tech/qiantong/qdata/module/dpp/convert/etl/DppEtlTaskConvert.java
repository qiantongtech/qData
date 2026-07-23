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

package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;

import java.util.List;

/**
 * Data Integration Task Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskConvert {
    DppEtlTaskConvert INSTANCE = Mappers.getMapper(DppEtlTaskConvert.class);

    /**
     * Convert PageReqVO to DO
     * @param dppEtlTaskPageReqVO request parameters
     * @return DppEtlTaskDO
     */
     DppEtlTaskDO convertToDO(DppEtlTaskPageReqVO dppEtlTaskPageReqVO);

    /**
     * Convert SaveReqVO to DO
     * @param dppEtlTaskSaveReqVO save request parameters
     * @return DppEtlTaskDO
     */
     DppEtlTaskDO convertToDO(DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO);

    /**
     * Convert DO to RespVO
     * @param dppEtlTaskDO entity object
     * @return DppEtlTaskRespVO
     */
     DppEtlTaskRespVO convertToRespVO(DppEtlTaskDO dppEtlTaskDO);

    /**
     * Convert DO List to RespVO List
     * @param dppEtlTaskDOList entity object list
     * @return List<DppEtlTaskRespVO>
     */
     List<DppEtlTaskRespVO> convertToRespVOList(List<DppEtlTaskDO> dppEtlTaskDOList);
}
