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

package tech.qiantong.qdata.module.dg.dal.mapper.dataCategoryCat;

import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Category - Category Mapper Interface
 *
 * @author FXB
 * @date 2026-04-07
 */
public interface DgDataCategoryCatMapper extends BaseMapperX<DgDataCategoryCatDO> {

    default PageResult<DgDataCategoryCatDO> selectPage(DgDataCategoryCatPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DgDataCategoryCatDO>()
                .likeIfPresent(DgDataCategoryCatDO::getName, reqVO.getName())
                .eqIfPresent(DgDataCategoryCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DgDataCategoryCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DgDataCategoryCatDO::getCode, reqVO.getCode())
                .eqIfPresent(DgDataCategoryCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DgDataCategoryCatDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(DgDataCategoryCatDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
