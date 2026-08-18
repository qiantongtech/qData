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
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppEvaluateLogDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Evaluation rule result Mapper interface
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface DppEvaluateLogMapper extends BaseMapperX<DppEvaluateLogDO> {

    default PageResult<DppEvaluateLogDO> selectPage(DppEvaluateLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEvaluateLogDO>()
                .likeIfPresent(DppEvaluateLogDO::getTableName, reqVO.getTableName())
                .likeIfPresent(DppEvaluateLogDO::getColumnName, reqVO.getColumnName())
                .eqIfPresent(DppEvaluateLogDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(DppEvaluateLogDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(DppEvaluateLogDO::getDimensionType, reqVO.getDimensionType())
                .eqIfPresent(DppEvaluateLogDO::getRuleDescription, reqVO.getRuleDescription())
                .eqIfPresent(DppEvaluateLogDO::getTaskLogId, reqVO.getTaskLogId())
                .eqIfPresent(DppEvaluateLogDO::getEvaluateId, reqVO.getEvaluateId())
                .eqIfPresent(DppEvaluateLogDO::getTotal, reqVO.getTotal())
                .eqIfPresent(DppEvaluateLogDO::getProblemTotal, reqVO.getProblemTotal())
                .eqIfPresent(DppEvaluateLogDO::getCheckDate, reqVO.getCheckDate())
                .eqIfPresent(DppEvaluateLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(DppEvaluateLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
