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

package tech.qiantong.qdata.module.dg.dal.mapper.dataCategory;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.Set;

import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Data Category Mapper Interface
 *
 * @author qdata
 * @date 2026-04-07
 */
public interface DgDataCategoryMapper extends BaseMapperX<DgDataCategoryDO> {

    default PageResult<DgDataCategoryDO> selectPage(DgDataCategoryPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapperX<DgDataCategoryDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        String subQuery = "(CASE WHEN r.id IS NOT NULL THEN '1' ELSE '0' END) AS desensitizationRulesFlag";
        lambdaWrapper.selectAll(DgDataCategoryDO.class)
                .select("t2.NAME AS catName", "t3.SHORT_NAME AS dataLevelShortName","r.id AS desensitizationRulesId", subQuery)
                .leftJoin("DG_DATA_CATEGORY_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DG_DATA_LEVEL t3 on t.DATA_LEVEL_ID = t3.ID AND t3.DEL_FLAG = '0'")
                .leftJoin("DG_DESENSITIZE_RULE r on t.id = r.DATA_CATEGORY_ID AND r.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DgDataCategoryDO::getCatCode, reqVO.getCatCode());

        lambdaWrapper
                .likeIfPresent(DgDataCategoryDO::getName, reqVO.getName())
                .likeIfPresent(DgDataCategoryDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DgDataCategoryDO::getDataLevelId, reqVO.getDataLevelId())
                .eqIfPresent(DgDataCategoryDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DgDataCategoryDO::getValidFlag, reqVO.getValidFlag())
                // Sort by createTime field in descending order
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DgDataCategoryDO.class, lambdaWrapper);
    }
}
