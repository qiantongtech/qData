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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data quality log Mapper interface
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface DppQualityLogMapper extends BaseMapperX<DppQualityLogDO> {

    default PageResult<DppQualityLogDO> selectPage(DppQualityLogPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityLogDO>()
                .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
                .eqIfPresent(DppQualityLogDO::getSuccessFlag, reqVO.getSuccessFlag())
                .eqIfPresent(DppQualityLogDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(DppQualityLogDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(DppQualityLogDO::getQualityId, reqVO.getQualityId())
                .eqIfPresent(DppQualityLogDO::getScore, reqVO.getScore())
                .eqIfPresent(DppQualityLogDO::getProblemData, reqVO.getProblemData())
                .eqIfPresent(DppQualityLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(DppQualityLogDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
