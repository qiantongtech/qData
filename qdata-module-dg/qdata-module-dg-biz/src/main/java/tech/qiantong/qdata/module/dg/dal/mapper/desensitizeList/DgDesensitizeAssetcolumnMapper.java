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

package tech.qiantong.qdata.module.dg.dal.mapper.desensitizeList;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;

import java.util.Arrays;

import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.Set;

import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Desensitization List Relationship Mapper Interface
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface DgDesensitizeAssetcolumnMapper extends BaseMapperX<DgDesensitizeAssetcolumnDO> {

    default PageResult<DgDesensitizeAssetcolumnDO> selectPage(DgDesensitizeAssetcolumnPageReqVO reqVO) {
        // Define allowed sort columns (prevent SQL injection, must match database field names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DgDesensitizeAssetcolumnDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DgDesensitizeAssetcolumnDO.class)
                .select("t2.NAME AS assetName",
                        "t2.DESCRIPTION as assetDescription",
                        "t2.TABLE_NAME AS assetTableName",
                        "t2.TABLE_COMMENT AS assetTableComment",
                        "t3.COLUMN_NAME AS  assetcolumnName",
                        "t3.COLUMN_COMMENT  AS assetcolumnComment",
                        "t4.NAME AS dataCategoryName",
                        "t5.SHORT_NAME AS dataLevelName",
                        "t6.NAME AS desensitizeRuleName")
                .innerJoin("DA_ASSET t2 ON t.ASSET_ID=t2.ID  AND t2.DEL_FLAG = '0'")
                .innerJoin("DA_ASSET_COLUMN t3 ON t.ASSETCOLUMN_ID=t3.ID  AND t3.DEL_FLAG = '0'")
                .innerJoin("DG_DATA_CATEGORY  t4 ON t.DATA_CATEGORY_ID =t4.ID AND t4.DEL_FLAG = '0'")
                .leftJoin("DG_DATA_LEVEL t5 ON t4.DATA_LEVEL_ID =t5.ID AND t5.DEL_FLAG = '0'")
                .leftJoin("DG_DESENSITIZE_RULE t6 ON t6.DATA_CATEGORY_ID =t4.ID  AND t6.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getAssetName()),"t2.NAME", reqVO.getAssetName());

        lambdaWrapper
                // Query by ValidFlag
                .eqIfPresent(DgDesensitizeAssetcolumnDO::getValidFlag, reqVO.getValidFlag())
                // Sort by createTime field in descending order
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DgDesensitizeAssetcolumnDO.class, lambdaWrapper);
    }

    default PageResult<DgDesensitizeAssetcolumnDO> selectPagebyRuleId(DgDesensitizeAssetcolumnPageReqVO reqVO) {
        // Define allowed sort columns (prevent SQL injection, must match database field names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DgDesensitizeAssetcolumnDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DgDesensitizeAssetcolumnDO.class)
                .select("t2.NAME AS assetName",
                        "t2.DESCRIPTION as assetDescription",
                        "t2.TABLE_NAME AS assetTableName",
                        "t2.TABLE_COMMENT AS assetTableComment",
                        "t3.COLUMN_NAME AS  assetcolumnName",
                        "t3.COLUMN_COMMENT  AS assetcolumnComment",
                        "t4.NAME AS dataCategoryName",
                        "t5.SHORT_NAME AS dataLevelName",
                        "t6.NAME AS desensitizeRuleName")
                .innerJoin("DA_ASSET t2 ON t.ASSET_ID=t2.ID  AND t2.DEL_FLAG = '0'")
                .innerJoin("DA_ASSET_COLUMN t3 ON t.ASSETCOLUMN_ID=t3.ID  AND t3.DEL_FLAG = '0'")
                .innerJoin("DG_DATA_CATEGORY  t4 ON t.DATA_CATEGORY_ID =t4.ID AND t4.DEL_FLAG = '0'")
                .leftJoin("DG_DATA_LEVEL t5 ON t4.DATA_LEVEL_ID =t5.ID AND t5.DEL_FLAG = '0'")
                .leftJoin("DG_DESENSITIZE_RULE t6 ON t6.DATA_CATEGORY_ID =t4.ID  AND t6.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getAssetName()), DgDesensitizeAssetcolumnDO::getAssetName, reqVO.getAssetName())
                .eq(reqVO.getRuleId() != null, "t6.ID", reqVO.getRuleId())
                // Sort by createTime field in descending order
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DgDesensitizeAssetcolumnDO.class, lambdaWrapper);
    }

    default DgDesensitizeAssetcolumnDO selectDesensitizeAssetcolumnById(Long id) {
        // Define allowed sort columns (prevent SQL injection, must match database field names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DgDesensitizeAssetcolumnDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DgDesensitizeAssetcolumnDO.class)
                .select("t2.NAME AS assetName",
                        "t2.DESCRIPTION as assetDescription",
                        "t2.TABLE_NAME AS assetTableName",
                        "t2.TABLE_COMMENT AS assetTableComment",
                        "t3.COLUMN_NAME AS  assetcolumnName",
                        "t3.COLUMN_COMMENT  AS assetcolumnComment",
                        "t4.NAME AS dataCategoryName",
                        "t5.SHORT_NAME AS dataLevelName",
                        "t6.NAME AS desensitizeRuleName")
                .innerJoin("DA_ASSET t2 ON t.ASSET_ID=t2.ID  AND t2.DEL_FLAG = '0'")
                .innerJoin("DA_ASSET_COLUMN t3 ON t.ASSETCOLUMN_ID=t3.ID  AND t3.DEL_FLAG = '0'")
                .innerJoin("DG_DATA_CATEGORY  t4 ON t.DATA_CATEGORY_ID =t4.ID AND t4.DEL_FLAG = '0'")
                .leftJoin("DG_DATA_LEVEL t5 ON t4.DATA_LEVEL_ID =t5.ID AND t5.DEL_FLAG = '0'")
                .leftJoin("DG_DESENSITIZE_RULE t6 ON t6.DATA_CATEGORY_ID =t4.ID  AND t6.DEL_FLAG = '0'")
                .eq(id != null, "t.ID", id);
        // Build dynamic query conditions
        return selectOne(lambdaWrapper);
    }


//    default PageResult<DgDesensitizeAssetcolumnDO> selectPage(DgDesensitizeAssetcolumnPageReqVO reqVO) {
//        // Define allowed sort columns (prevent SQL injection, must match database field names)
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
//        // Build dynamic query conditions
//        return selectPage(reqVO, new LambdaQueryWrapperX<DgDesensitizeAssetcolumnDO>()
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getAssetId, reqVO.getAssetId())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getAssetcolumnId, reqVO.getAssetcolumnId())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getDataCategoryId, reqVO.getDataCategoryId())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getSortOrder, reqVO.getSortOrder())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getDescription, reqVO.getDescription())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getValidFlag, reqVO.getValidFlag())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getCreateBy, reqVO.getCreateBy())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getCreatorId, reqVO.getCreatorId())
//                .eqIfPresent(DgDesensitizeAssetcolumnDO::getCreateTime, reqVO.getCreateTime())
//                // If reqVO.getName() is not empty, add an exact match condition for name
//                // .likeIfPresent(DgDesensitizeAssetcolumnDO::getName, reqVO.getName())
//                // Sort by createTime field in descending order
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
//    }


}
