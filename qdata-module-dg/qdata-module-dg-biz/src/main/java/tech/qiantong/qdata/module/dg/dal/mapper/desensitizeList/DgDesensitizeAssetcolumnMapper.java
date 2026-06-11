/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
 * 脱敏清单关联关系Mapper接口
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface DgDesensitizeAssetcolumnMapper extends BaseMapperX<DgDesensitizeAssetcolumnDO> {

    default PageResult<DgDesensitizeAssetcolumnDO> selectPage(DgDesensitizeAssetcolumnPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
                //根据ValidFlag查询
                .eqIfPresent(DgDesensitizeAssetcolumnDO::getValidFlag, reqVO.getValidFlag())
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // 构造动态查询条件
        return selectJoinPage(reqVO, DgDesensitizeAssetcolumnDO.class, lambdaWrapper);
    }

    default PageResult<DgDesensitizeAssetcolumnDO> selectPagebyRuleId(DgDesensitizeAssetcolumnPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // 构造动态查询条件
        return selectJoinPage(reqVO, DgDesensitizeAssetcolumnDO.class, lambdaWrapper);
    }

    default DgDesensitizeAssetcolumnDO selectDesensitizeAssetcolumnById(Long id) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
        // 构造动态查询条件
        return selectOne(lambdaWrapper);
    }


//    default PageResult<DgDesensitizeAssetcolumnDO> selectPage(DgDesensitizeAssetcolumnPageReqVO reqVO) {
//        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
//        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
//
//        // 构造动态查询条件
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
//                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
//                // .likeIfPresent(DgDesensitizeAssetcolumnDO::getName, reqVO.getName())
//                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
//    }


}
