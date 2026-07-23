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

package tech.qiantong.qdata.module.dp.dal.mapper.model;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Materialized Model Record Mapper Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpModelMaterializedMapper extends BaseMapperX<DpModelMaterializedDO> {

    default PageResult<DpModelMaterializedDO> selectPage(DpModelMaterializedPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DpModelMaterializedDO>()
                .likeIfPresent(DpModelMaterializedDO::getModelName, reqVO.getModelName())
                .eqIfPresent(DpModelMaterializedDO::getModelAlias, reqVO.getModelAlias())
                .eqIfPresent(DpModelMaterializedDO::getModelId, reqVO.getModelId())
                .eqIfPresent(DpModelMaterializedDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DpModelMaterializedDO::getMessage, reqVO.getMessage())
                .eqIfPresent(DpModelMaterializedDO::getSqlCommand, reqVO.getSqlCommand())
                .eqIfPresent(DpModelMaterializedDO::getDatasourceId, reqVO.getDatasourceId())
                .eqIfPresent(DpModelMaterializedDO::getDatasourceType, reqVO.getDatasourceType())
                .likeIfPresent(DpModelMaterializedDO::getDatasourceName, reqVO.getDatasourceName())
                .eqIfPresent(DpModelMaterializedDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DpModelMaterializedDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(DpModelMaterializedDO::getName, reqVO.getName())
                // Sort in descending order by createTime field
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
