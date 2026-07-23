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

package tech.qiantong.qdata.quality.dal.mapper.qa;


import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data quality task-evaluation rule Mapper interface
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface DppQualityTaskEvaluateMapper extends BaseMapperX<DppQualityTaskEvaluateDO> {

    default PageResult<DppQualityTaskEvaluateDO> selectPage(DppQualityTaskEvaluatePageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityTaskEvaluateDO>()
                .eqIfPresent(DppQualityTaskEvaluateDO::getTaskId, reqVO.getTaskId())
                .likeIfPresent(DppQualityTaskEvaluateDO::getName, reqVO.getName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(DppQualityTaskEvaluateDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getWarningLevel, reqVO.getWarningLevel())
                .eqIfPresent(DppQualityTaskEvaluateDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRuleDescription, reqVO.getRuleDescription())
                .eqIfPresent(DppQualityTaskEvaluateDO::getErrDescription, reqVO.getErrDescription())
                .eqIfPresent(DppQualityTaskEvaluateDO::getSuggestion, reqVO.getSuggestion())
                .eqIfPresent(DppQualityTaskEvaluateDO::getWhereClause, reqVO.getWhereClause())
                .eqIfPresent(DppQualityTaskEvaluateDO::getObjId, reqVO.getObjId())
                .likeIfPresent(DppQualityTaskEvaluateDO::getObjName, reqVO.getObjName())
                .likeIfPresent(DppQualityTaskEvaluateDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DppQualityTaskEvaluateDO::getEvaColumn, reqVO.getEvaColumn())
                .eqIfPresent(DppQualityTaskEvaluateDO::getRule, reqVO.getRule())
                .eqIfPresent(DppQualityTaskEvaluateDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(DppQualityTaskEvaluateDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
