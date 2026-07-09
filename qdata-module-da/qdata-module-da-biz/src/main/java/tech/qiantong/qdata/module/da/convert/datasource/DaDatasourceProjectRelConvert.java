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

package tech.qiantong.qdata.module.da.convert.datasource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;

import java.util.List;

/**
 * Datasource-Project Relation Convert
 *
 * @author qdata
 * @date 2025-03-13
 */
@Mapper
public interface DaDatasourceProjectRelConvert {
    DaDatasourceProjectRelConvert INSTANCE = Mappers.getMapper(DaDatasourceProjectRelConvert.class);

    /**
     * PageReqVO to DO
     * @param daDatasourceProjectRelPageReqVO request parameters
     * @return DaDatasourceProjectRelDO
     */
     DaDatasourceProjectRelDO convertToDO(DaDatasourceProjectRelPageReqVO daDatasourceProjectRelPageReqVO);

    /**
     * SaveReqVO to DO
     * @param daDatasourceProjectRelSaveReqVO save request parameters
     * @return DaDatasourceProjectRelDO
     */
     DaDatasourceProjectRelDO convertToDO(DaDatasourceProjectRelSaveReqVO daDatasourceProjectRelSaveReqVO);

    /**
     * DO to RespVO
     * @param daDatasourceProjectRelDO entity object
     * @return DaDatasourceProjectRelRespVO
     */
     DaDatasourceProjectRelRespVO convertToRespVO(DaDatasourceProjectRelDO daDatasourceProjectRelDO);

    /**
     * DOList to RespVOList
     * @param daDatasourceProjectRelDOList entity object list
     * @return List<DaDatasourceProjectRelRespVO>
     */
     List<DaDatasourceProjectRelRespVO> convertToRespVOList(List<DaDatasourceProjectRelDO> daDatasourceProjectRelDOList);
}
