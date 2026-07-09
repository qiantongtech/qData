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

package tech.qiantong.qdata.module.da.dal.mapper.discovery;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Discovery Table Info Mapper Interface
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface DaDiscoveryTableMapper extends BaseMapperX<DaDiscoveryTableDO> {

    default PageResult<DaDiscoveryTableDO> selectPage(DaDiscoveryTablePageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDiscoveryTableDO>()
                .eqIfPresent(DaDiscoveryTableDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(DaDiscoveryTableDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DaDiscoveryTableDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(DaDiscoveryTableDO::getDataCount, reqVO.getDataCount())
                .eqIfPresent(DaDiscoveryTableDO::getFieldCount, reqVO.getFieldCount())
                .eqIfPresent(DaDiscoveryTableDO::getChangeFlag, reqVO.getChangeFlag())
                .eqIfPresent(DaDiscoveryTableDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DaDiscoveryTableDO::getIgnoreFlag, reqVO.getIgnoreFlag())
                .eqIfPresent(DaDiscoveryTableDO::getCreateTime, reqVO.getCreateTime())
                // New keyword fuzzy match
                .likeIfPresent(DaDiscoveryTableDO::getTableName, reqVO.getKeyword())
                .or()
                .likeIfPresent(DaDiscoveryTableDO::getTableComment, reqVO.getKeyword())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(DaDiscoveryTableDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
