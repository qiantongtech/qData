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

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Business Category Mapper Interface
 *
 * @author qdata
 * @date 2026-04-08
 */
public interface DmBusinessCategoryMapper extends BaseMapperX<DmBusinessCategoryDO> {

    default PageResult<DmBusinessCategoryDO> selectPage(DmBusinessCategoryPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DmBusinessCategoryDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DmBusinessCategoryDO.class)
                .select("t2.NICK_NAME AS ownerName")
                .leftJoin("SYSTEM_USER t2 ON t.OWNER_ID=t2.USER_ID  AND t2.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_DOMAIN_REL t3 ON t3.BUSINESS_CATEGORY_ID=t.ID  AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t4 ON t4.ID=t3.DATA_DOMAIN_ID  AND t4.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DmBusinessCategoryDO::getName, reqVO.getName())
                .eq(reqVO.getValidFlag() != null, DmBusinessCategoryDO::getValidFlag, reqVO.getValidFlag())
                .and(reqVO.getParentId() != null, w -> w
                        .eq(DmBusinessCategoryDO::getParentId, reqVO.getParentId())
                        .or()
                        .eq(DmBusinessCategoryDO::getId, reqVO.getParentId())
                )
                .eq(reqVO.getDomainId() != null, "t4.ID", reqVO.getDomainId());

        // Sort by createTime descending
//                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
//                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
//                .orderByStr(
//                    StringUtils.isNotBlank(reqVO.getOrderByColumn()), // Condition: sort only when sort field is not empty
//                    StringUtils.equals("asc", reqVO.getIsAsc()),       // Sort direction: true=ASC, false=DESC
//                    reqVO.getOrderByColumn(),                           // Sort field (database underscore format, e.g. create_time)
//                    allowedColumns                                      // Allowed field whitelist, prevent SQL injection
//                );
        lambdaWrapper.orderBy(StringUtils.isNotBlank(reqVO.getOrderByColumn()), !"asc".equals(reqVO.getIsAsc()), DmBusinessCategoryDO::getCreateTime);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DmBusinessCategoryDO.class, lambdaWrapper);
    /*    // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DmBusinessCategoryDO>()
                .likeIfPresent(DmBusinessCategoryDO::getName, reqVO.getName())
                .eqIfPresent(DmBusinessCategoryDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DmBusinessCategoryDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DmBusinessCategoryDO::getDescription, reqVO.getDescription())
                .likeIfPresent(DmBusinessCategoryDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmBusinessCategoryDO::getOwnerPhone, reqVO.getOwnerPhone())
                .eqIfPresent(DmBusinessCategoryDO::getOwnerId, reqVO.getOwnerId())
                .eqIfPresent(DmBusinessCategoryDO::getDomainId, reqVO.getDomainId())
                .eqIfPresent(DmBusinessCategoryDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DmBusinessCategoryDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(DmBusinessCategoryDO::getName, reqVO.getName())
                // Sort by createTime descending
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));*/
    }


    default List<DmBusinessCategoryDO> selectAllList(DmBusinessCategoryPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DmBusinessCategoryDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DmBusinessCategoryDO.class)
                .select("t2.NICK_NAME AS ownerName")
                .leftJoin("SYSTEM_USER t2 ON t.OWNER_ID=t2.USER_ID  AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DmBusinessCategoryDO::getName, reqVO.getName())
                .eq(reqVO.getValidFlag() != null, DmBusinessCategoryDO::getValidFlag, reqVO.getValidFlag())
                .and(reqVO.getParentId() != null, w -> w
                        .eq(DmBusinessCategoryDO::getParentId, reqVO.getParentId())
                        .or()
                        .eq(DmBusinessCategoryDO::getId, reqVO.getParentId())
                )
                .eq(reqVO.getOwnerId() != null, DmBusinessCategoryDO::getOwnerId, reqVO.getOwnerId());
        lambdaWrapper.orderBy(StringUtils.isNotBlank(reqVO.getOrderByColumn()), !"asc".equals(reqVO.getIsAsc()), DmBusinessCategoryDO::getCreateTime);
        // Build dynamic query conditions
        return selectList(lambdaWrapper);
    }

    @Update(value = "update DM_BUSINESS_CATEGORY set VALID_FLAG=#{validFlag} where code like concat(#{prefixCode}, '%')")
    void updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);
}
