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

package tech.qiantong.qdata.module.att.dal.mapper.cat;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Integration Task Category Management Mapper Interface
 *
 * @author qdata
 * @date 2025-03-11
 */
public interface AttTaskCatMapper extends BaseMapperX<AttTaskCatDO> {

    default PageResult<AttTaskCatDO> selectPage(AttTaskCatPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AttTaskCatDO>()
                .likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTaskCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTaskCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTaskCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttTaskCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTaskCatDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AttTaskCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttTaskCatDO::getProjectCode,reqVO.getProjectCode())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
