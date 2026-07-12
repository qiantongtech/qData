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

package tech.qiantong.qdata.module.att.dal.mapper.rule;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Audit Rule Mapper Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttAuditRuleMapper extends BaseMapperX<AttAuditRuleDO> {

    default PageResult<AttAuditRuleDO> selectPage(AttAuditRulePageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AttAuditRuleDO>()
                .likeIfPresent(AttAuditRuleDO::getName, reqVO.getName())
                .eqIfPresent(AttAuditRuleDO::getQualityDim, reqVO.getQualityDim())
                .eqIfPresent(AttAuditRuleDO::getType, reqVO.getType())
                .eqIfPresent(AttAuditRuleDO::getLevel, reqVO.getLevel())
                .eqIfPresent(AttAuditRuleDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttAuditRuleDO::getCode, reqVO.getCode())
                .likeIfPresent(AttAuditRuleDO::getUseCase, reqVO.getUseCase())
                .likeIfPresent(AttAuditRuleDO::getExample, reqVO.getExample())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(AttAuditRuleDO::getName, reqVO.getName())
                // Sort by createTime in descending order
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
         .orderByDesc(AttAuditRuleDO::getCreateTime));

    }

    List<AttAuditRuleDO> selectAttAuditRuleList(@Param("dataElemId") Long dataElemId);
}
