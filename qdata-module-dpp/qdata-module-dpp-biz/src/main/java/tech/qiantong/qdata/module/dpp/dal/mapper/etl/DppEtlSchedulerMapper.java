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

package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Integration Scheduler Info Mapper
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlSchedulerMapper extends BaseMapperX<DppEtlSchedulerDO> {

    default PageResult<DppEtlSchedulerDO> selectPage(DppEtlSchedulerPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlSchedulerDO>()
                .eqIfPresent(DppEtlSchedulerDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DppEtlSchedulerDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DppEtlSchedulerDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(DppEtlSchedulerDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(DppEtlSchedulerDO::getTimezoneId, reqVO.getTimezoneId())
                .eqIfPresent(DppEtlSchedulerDO::getCronExpression, reqVO.getCronExpression())
                .eqIfPresent(DppEtlSchedulerDO::getFailureStrategy, reqVO.getFailureStrategy())
                .eqIfPresent(DppEtlSchedulerDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlSchedulerDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact name match condition (name = '<name>')
                // .likeIfPresent(DppEtlSchedulerDO::getName, reqVO.getName())
                // Order by createTime descending
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
