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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Asset Audit Schedule Mapper Interface
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetAuditScheduleMapper extends BaseMapperX<DaAssetAuditScheduleDO> {

    default PageResult<DaAssetAuditScheduleDO> selectPage(DaAssetAuditSchedulePageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetAuditScheduleDO>()
                .eqIfPresent(DaAssetAuditScheduleDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetAuditScheduleDO::getScheduleFlag, reqVO.getScheduleFlag())
                .eqIfPresent(DaAssetAuditScheduleDO::getCronExpression, reqVO.getCronExpression())
                .eqIfPresent(DaAssetAuditScheduleDO::getNodeId, reqVO.getNodeId())
                .eqIfPresent(DaAssetAuditScheduleDO::getNodeCode, reqVO.getNodeCode())
                .eqIfPresent(DaAssetAuditScheduleDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaAssetAuditScheduleDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DaAssetAuditScheduleDO::getSystemJobId, reqVO.getSystemJobId())
                .eqIfPresent(DaAssetAuditScheduleDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(DaAssetAuditScheduleDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
