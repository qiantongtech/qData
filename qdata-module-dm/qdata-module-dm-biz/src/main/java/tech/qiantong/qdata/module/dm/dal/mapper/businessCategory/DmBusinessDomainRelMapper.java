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

package tech.qiantong.qdata.module.dm.dal.mapper.businessCategory;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Business Category Domain Relation Mapper Interface
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface DmBusinessDomainRelMapper extends BaseMapperX<DmBusinessDomainRelDO> {

    default PageResult<DmBusinessDomainRelDO> selectPage(DmBusinessDomainRelPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DmBusinessDomainRelDO>()
                .eqIfPresent(DmBusinessDomainRelDO::getBusinessCategoryId, reqVO.getBusinessCategoryId())
                .eqIfPresent(DmBusinessDomainRelDO::getDataDomainId, reqVO.getDataDomainId())
                .likeIfPresent(DmBusinessDomainRelDO::getBusinessCategoryName, reqVO.getBusinessCategoryName())
                .likeIfPresent(DmBusinessDomainRelDO::getDataDomainName, reqVO.getDataDomainName())
                .eqIfPresent(DmBusinessDomainRelDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DmBusinessDomainRelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmBusinessDomainRelDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DmBusinessDomainRelDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(DmBusinessDomainRelDO::getName, reqVO.getName())
                // Sort by createTime descending
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
