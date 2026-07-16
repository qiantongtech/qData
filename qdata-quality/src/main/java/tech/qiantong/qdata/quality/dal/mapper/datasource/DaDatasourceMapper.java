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

package tech.qiantong.qdata.quality.dal.mapper.datasource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data source Mapper interface
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaDatasourceMapper extends BaseMapperX<DaDatasourceDO> {

    default PageResult<DaDatasourceDO> selectPage(DaDatasourcePageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDatasourceDO>()
                .likeIfPresent(DaDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .inIfPresent(DaDatasourceDO::getDatasourceType, StringUtils.isNotEmpty(reqVO.getDatasourceType()) ? reqVO.getDatasourceType().split(",") : null)
                .eqIfPresent(DaDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eqIfPresent(DaDatasourceDO::getIp, reqVO.getIp())
                .eqIfPresent(DaDatasourceDO::getPort, reqVO.getPort())
                .eqIfPresent(DaDatasourceDO::getListCount, reqVO.getListCount())
                .eqIfPresent(DaDatasourceDO::getSyncCount, reqVO.getSyncCount())
                .eqIfPresent(DaDatasourceDO::getDataSize, reqVO.getDataSize())
                .eqIfPresent(DaDatasourceDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DaDatasourceDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(DaDatasourceDO::getId, reqVO.getIdList())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(DaDatasourceDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    public List<DaDatasourceDO> getDataSourceByAsset();
}
