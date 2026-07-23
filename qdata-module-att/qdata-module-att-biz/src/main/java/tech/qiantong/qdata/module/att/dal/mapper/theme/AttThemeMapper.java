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

package tech.qiantong.qdata.module.att.dal.mapper.theme;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Theme Mapper Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttThemeMapper extends BaseMapperX<AttThemeDO> {

    default PageResult<AttThemeDO> selectPage(AttThemePageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id","sort_order", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AttThemeDO>()
                .likeIfPresent(AttThemeDO::getName, reqVO.getName())
                .eqIfPresent(AttThemeDO::getIcon, reqVO.getIcon())
                .eqIfPresent(AttThemeDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttThemeDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttThemeDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(AttThemeDO::getName, reqVO.getName())
                // Sort by createTime in descending order
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
                //Sort by createTime in descending order, sort_order ascending
                .orderByAsc(AttThemeDO::getSortOrder)
                .orderByDesc(AttThemeDO::getCreateTime));


    }
}
