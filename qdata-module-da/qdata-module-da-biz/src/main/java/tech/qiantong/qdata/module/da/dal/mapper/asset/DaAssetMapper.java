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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.dal.mapper.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.mybatis.config.MasterDataSourceConfig;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.*;

/**
 * 数据资产Mapper接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaAssetMapper extends BaseMapperX<DaAssetDO> {

    default PageResult<DaAssetDO> selectPage(DaAssetPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapperX<DaAssetDO> lambdaWrapper = new MPJLambdaWrapperX();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select(
                        "t2.NAME AS catName",
                        "t3.NAME AS dataLayerName",
                        "t3.ENG_NAME AS dataLayerEngName",
                        "t4.NAME AS businessCategoryName",
                        "t4.ENG_NAME AS businessCategoryEngName",
                        "t5.NAME AS dataDomainName",
                        "t5.ENG_NAME AS dataDomainEngName",
                        "t6.NAME AS themeDomainName",
                        "t6.ENG_NAME AS themeDomainEngName")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")

                .leftJoin("DM_DATA_LAYER t3 ON t.DATA_LAYER_ID = t3.id AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t.BUSINESS_CATEGORY_ID = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t5 ON t.DATA_DOMAIN_ID = t5.id AND t5.DEL_FLAG = '0'")
                .leftJoin("DM_THEME_DOMAIN t6 ON t.THEME_DOMAIN_ID = t6.id AND t6.DEL_FLAG = '0'");

        //增加标签筛选
        if (CollectionUtils.isNotEmpty(reqVO.getTagIdList())) {
            lambdaWrapper.exists(
                    "SELECT 1 FROM ATT_TAG_ASSET_REL taRel WHERE t.id = taRel.ASSET_ID AND taRel.DEL_FLAG = '0' AND taRel.TAG_ID IN (" +
                            reqVO.getTagIdList().stream().map(id -> "?").reduce((a, b) -> a + "," + b).orElse("") +
                            ")"
            );
        }

        //拼接查询标签列表
        String subSelectSql = "SELECT\n" +
                "'['|| WM_CONCAT(DISTINCT '{\"tagId\":\"' || d.ID || '\",\"tagName\":\"' || d.name || '\"}' ) ||']'\n" +
                "FROM \n" +
                "     ATT_TAG d \n" +
                "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                "WHERE \n" +
                "    d.DEL_FLAG ='0' AND rel.DEL_FLAG = '0'  \n" +
                "    AND rel.ASSET_ID = t.ID \n" +
                "HAVING COUNT(d.ID) > 0";

        if (StringUtils.equals("mysql", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT(\n" +
                    "        '[', \n" +
                    "        GROUP_CONCAT(\n" +
                    "            DISTINCT CONCAT(\n" +
                    "                '{\"tagId\":\"', d.ID, \n" +
                    "                '\",\"tagName\":\"', d.name, \n" +
                    "                '\"}'\n" +
                    "            )\n" +
                    "        ), \n" +
                    "        ']'\n" +
                    "    ) AS json_result\n" +
                    "FROM \n" +
                    "     ATT_TAG d \n" +
                    "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                    "WHERE \n" +
                    "    d.DEL_FLAG ='0' \n" +
                    "    AND rel.ASSET_ID = t.ID \n" +
                    "HAVING COUNT(d.ID) > 0";
        } else if (StringUtils.equals("kingbase8", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT_WS('','[' , STRING_AGG(DISTINCT CONCAT_WS('', '{\"tagId\":\"', d.ID, '\",\"tagName\":\"', d.name, '\"}'), ',') , ']')\n" +
                    "FROM \n" +
                    "     ATT_TAG d \n" +
                    "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                    "WHERE \n" +
                    "    d.DEL_FLAG ='0' \n" +
                    "    AND rel.ASSET_ID = t.ID \n" +
                    "HAVING COUNT(d.ID) > 0";
        }
        lambdaWrapper.select("(" + subSelectSql + ") AS tags");
        lambdaWrapper
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaAssetDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DaAssetDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getDatasourceId()), DaAssetDO::getDatasourceId, reqVO.getDatasourceId())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DaAssetDO::getType, reqVO.getType())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaAssetDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaAssetDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaAssetDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DaAssetDO::getDescription, reqVO.getDescription())
                .in(reqVO.getAssetIdList() != null && reqVO.getAssetIdList()
                        .size() > 0, DaAssetDO::getId, reqVO.getAssetIdList())
                .eq(StringUtils.isNotBlank(reqVO.getTableType()), DaAssetDO::getTableType, reqVO.getTableType())
                .eq(reqVO.getDataLayerId() != null, DaAssetDO::getDataLayerId, reqVO.getDataLayerId())
                .eq(reqVO.getBusinessCategoryId() != null, DaAssetDO::getBusinessCategoryId, reqVO.getBusinessCategoryId())
                .likeRight(StringUtils.isNotBlank(reqVO.getBusinessCategoryCode()), DaAssetDO::getBusinessCategoryCode, reqVO.getBusinessCategoryCode())
                .eq(reqVO.getDataDomainId() != null, DaAssetDO::getDataDomainId, reqVO.getDataDomainId())
                .eq(reqVO.getThemeDomainId() != null, DaAssetDO::getThemeDomainId, reqVO.getThemeDomainId())
                .likeRight(StringUtils.isNotBlank(reqVO.getThemeDomainCode()), DaAssetDO::getThemeDomainCode, reqVO.getThemeDomainCode())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
                                                                                                                                                                                            .split(",")) : null);
        return selectJoinPage(reqVO, DaAssetDO.class, lambdaWrapper);
    }

    default PageResult<DaAssetDO> selectPageDpp(DaAssetPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName")
                .select("t3.PROJECT_ID AS projectId,t3.PROJECT_CODE AS projectCode")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_ASSET_PROJECT_REL t3 on t.id = t3.ASSET_ID AND t3.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaAssetDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DaAssetDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getDatasourceId()), DaAssetDO::getDatasourceId, reqVO.getDatasourceId())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DaAssetDO::getType, reqVO.getType())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaAssetDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaAssetDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaAssetDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DaAssetDO::getDescription, reqVO.getDescription())
                .in(reqVO.getThemeAssetIdList() != null && !reqVO.getThemeAssetIdList()
                        .isEmpty(), DaAssetDO::getId, reqVO.getThemeAssetIdList())
                .and(wrapper -> wrapper
                        .in(reqVO.getAssetIdList() != null && !reqVO.getAssetIdList()
                                .isEmpty(), DaAssetDO::getId, reqVO.getAssetIdList())
                        .or(inner -> inner
                                .eq(reqVO.getProjectId() != null, "t3.PROJECT_ID", reqVO.getProjectId())
                                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()), "t3.PROJECT_CODE", reqVO.getProjectCode())
                        )
                )
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
                                                                                                                                                                                            .split(",")) : null);

        return selectJoinPage(reqVO, DaAssetDO.class, lambdaWrapper);
    }

    void deleteAssetById(Long id);

    default List<DaAssetDO> findByDatasourceIdAndTableName(Long datasourceId, String tableName) {
        LambdaQueryWrapper<DaAssetDO> queryWrapper = Wrappers.<DaAssetDO>lambdaQuery()
                .eq(DaAssetDO::getDatasourceId, datasourceId)
                .eq(DaAssetDO::getTableName, tableName);
        return selectList(queryWrapper);
    }

    Map<String, Object> getDaAssetOverviewStatistics();


    /**
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
     */
    default int updateCatCode(String oldCatCode, String newCatCode) {
        return this.update(
                null,
                Wrappers.<DaAssetDO>lambdaUpdate()
                        .set(DaAssetDO::getCatCode, newCatCode)
                        .eq(DaAssetDO::getDelFlag, "0")
                        .eq(DaAssetDO::getCatCode, oldCatCode)
        );
    }
}
