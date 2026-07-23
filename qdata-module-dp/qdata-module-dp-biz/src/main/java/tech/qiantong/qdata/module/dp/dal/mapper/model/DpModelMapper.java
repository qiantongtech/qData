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

package tech.qiantong.qdata.module.dp.dal.mapper.model;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.mybatis.config.MasterDataSourceConfig;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Logical Model Mapper Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpModelMapper extends BaseMapperX<DpModelDO> {

    default PageResult<DpModelDO> selectPage(DpModelPageReqVO reqVO) {
        MPJLambdaWrapperX<DpModelDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper
                .selectAll(DpModelDO.class)
                .select("t2.NAME AS catName",
                        "t3.NAME AS dataLayerName",
                        "t3.ENG_NAME AS dataLayerEngName",
                        "t4.NAME AS businessCategoryName",
                        "t4.ENG_NAME AS businessCategoryEngName",
                        "t5.NAME AS dataDomainName",
                        "t5.ENG_NAME AS dataDomainEngName",
                        "t6.NAME AS themeDomainName",
                        "t6.ENG_NAME AS themeDomainEngName",
                        "u.PHONENUMBER AS createUserPhoneNumber"
                )
                .leftJoin("SYSTEM_USER u on t.CREATOR_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin("ATT_MODEL_CAT t2 ON t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_LAYER t3 ON t.DATA_LAYER_ID = t3.id AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t.BUSINESS_CATEGORY_ID = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t5 ON t.DATA_DOMAIN_ID = t5.id AND t5.DEL_FLAG = '0'")
                .leftJoin("DM_THEME_DOMAIN t6 ON t.THEME_DOMAIN_ID = t6.id AND t6.DEL_FLAG = '0'");
        lambdaWrapper
                .like(StringUtils.isNotBlank(reqVO.getModelName()), DpModelDO::getModelName, reqVO.getModelName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DpModelDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getModelComment()), DpModelDO::getModelComment, reqVO.getModelComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DpModelDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDocumentId() != null, DpModelDO::getDocumentId, reqVO.getDocumentId())
                .eq(StringUtils.isNotBlank(reqVO.getTableType()), DpModelDO::getTableType, reqVO.getTableType())
                .eq(reqVO.getDataLayerId() != null, DpModelDO::getDataLayerId, reqVO.getDataLayerId())
                .eq(reqVO.getBusinessCategoryId() != null, DpModelDO::getBusinessCategoryId, reqVO.getBusinessCategoryId())
                .likeRight(StringUtils.isNotBlank(reqVO.getBusinessCategoryCode()), DpModelDO::getBusinessCategoryCode, reqVO.getBusinessCategoryCode())
                .eq(reqVO.getDataDomainId() != null, DpModelDO::getDataDomainId, reqVO.getDataDomainId())
                .eq(reqVO.getThemeDomainId() != null, DpModelDO::getThemeDomainId, reqVO.getThemeDomainId())
                .likeRight(StringUtils.isNotBlank(reqVO.getThemeDomainCode()), DpModelDO::getThemeDomainCode, reqVO.getThemeDomainCode())
//                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
//                                                                                                                                                                                            .split(",")) : null);
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
                                                                                                                                      .split(","))
                                                                                                                        .stream()
                                                                                                                        .map(e -> "t." + LambdaQueryWrapperX.camelToUnderline(e))
                                                                                                                        .collect(Collectors.toList()) : null);

        return selectJoinPage(reqVO, DpModelDO.class, lambdaWrapper);
    }

    default PageResult<DpModelDO> getReleaseListPage(DpModelPageReqVO reqVO) {
        MPJLambdaWrapperX<DpModelDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper
                .selectAll(DpModelDO.class)
                .select("t2.NAME AS catName",
                        "t3.NAME AS dataLayerName",
                        "t3.ENG_NAME AS dataLayerEngName",
                        "t4.NAME AS businessCategoryName",
                        "t4.ENG_NAME AS businessCategoryEngName",
                        "t5.NAME AS dataDomainName",
                        "t5.ENG_NAME AS dataDomainEngName",
                        "t6.NAME AS themeDomainName",
                        "t6.ENG_NAME AS themeDomainEngName",
                        "(CASE WHEN ti.STATUS IS NOT NULL THEN ti.STATUS ELSE '1' END)  AS releaseStatus",
                        "u.PHONENUMBER AS createUserPhoneNumber"
                )
                .leftJoin("SYSTEM_USER u on t.CREATOR_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin("ATT_MODEL_CAT t2 ON t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_LAYER t3 ON t.DATA_LAYER_ID = t3.id AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t.BUSINESS_CATEGORY_ID = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t5 ON t.DATA_DOMAIN_ID = t5.id AND t5.DEL_FLAG = '0'")
                .leftJoin("DM_THEME_DOMAIN t6 ON t.THEME_DOMAIN_ID = t6.id AND t6.DEL_FLAG = '0'");
        String joinReleaseStatusStr = "(SELECT\n" +
                "                ti1.MODEL_ID, ti1.CREATE_TIME, ti1.STATUS\n" +
                "            FROM\n" +
                "                DP_MODEL_MATERIALIZED ti1\n" +
                "                JOIN (\n" +
                "                    SELECT\n" +
                "                        ti2.MODEL_ID,MAX(ti2.CREATE_TIME) AS CREATE_TIME\n" +
                "                    FROM\n" +
                "                        DP_MODEL_MATERIALIZED ti2\n" +
                "                    WHERE\n" +
                "                        ti2.DEL_FLAG = '0'\n" +
                "                    GROUP BY ti2.MODEL_ID\n" +
                "                ) maxData ON maxData.MODEL_ID = ti1.MODEL_ID AND maxData.CREATE_TIME = ti1.CREATE_TIME\n" +
                "            WHERE\n" +
                "                ti1.DEL_FLAG = '0') ti ON ti.MODEL_ID = t.ID";
        lambdaWrapper.leftJoin(joinReleaseStatusStr);
        // Build query for published data source list

        String subSelectSql = "SELECT\n" +
                "'['|| WM_CONCAT(DISTINCT '{\"DATASOURCE_NAME\":\"' || d.DATASOURCE_NAME || '\",\"DATASOURCE_TYPE\":\"' || d.DATASOURCE_TYPE || '\"}' ) ||']'\n" +
                "FROM\n" +
                "DP_MODEL_MATERIALIZED d\n" +
                "WHERE \n" +
                "d.STATUS = '3'\n" +
                "AND d.DEL_FLAG ='0'\n" +
                "AND d.MODEL_ID = t.ID";

        if (org.apache.commons.lang3.StringUtils.equals("mysql", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT(\n" +
                    "        '[', \n" +
                    "        GROUP_CONCAT(\n" +
                    "            DISTINCT CONCAT(\n" +
                    "                '{\"DATASOURCE_NAME\":\"', d.DATASOURCE_NAME, \n" +
                    "                '\",\"DATASOURCE_TYPE\":\"', d.DATASOURCE_TYPE, \n" +
                    "                '\"}'\n" +
                    "            )\n" +
                    "        ), \n" +
                    "        ']'\n" +
                    "    ) AS json_result\n" +
                    "FROM \n" +
                    "    DP_MODEL_MATERIALIZED d\n" +
                    "WHERE \n" +
                    "    d.STATUS = '3'\n" +
                    "    AND d.DEL_FLAG = '0'" +
                    "    AND d.MODEL_ID = t.ID";
        } else if (org.apache.commons.lang3.StringUtils.equals("kingbase8", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT_WS('','[' , STRING_AGG(DISTINCT CONCAT_WS('', '{\"DATASOURCE_NAME\":\"', d.DATASOURCE_NAME, '\",\"DATASOURCE_TYPE\":\"', d.DATASOURCE_TYPE, '\"}'), ',') , ']')\n" +
                    "FROM \n" +
                    "    DP_MODEL_MATERIALIZED d\n" +
                    "WHERE \n" +
                    "    d.STATUS = '3'\n" +
                    "    AND d.DEL_FLAG = '0'" +
                    "    AND d.MODEL_ID = t.ID";
        }
        lambdaWrapper.select("(" + subSelectSql + ") AS releaseDatabaseList");
        lambdaWrapper
                .eq(DpModelDO::getStatus, "1")
                .like(StringUtils.isNotBlank(reqVO.getModelName()), DpModelDO::getModelName, reqVO.getModelName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DpModelDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getModelComment()), DpModelDO::getModelComment, reqVO.getModelComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DpModelDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDocumentId() != null, DpModelDO::getDocumentId, reqVO.getDocumentId())
                .eq(StringUtils.isNotBlank(reqVO.getTableType()), DpModelDO::getTableType, reqVO.getTableType())
                .eq(reqVO.getDataLayerId() != null, DpModelDO::getDataLayerId, reqVO.getDataLayerId())
                .eq(reqVO.getBusinessCategoryId() != null, DpModelDO::getBusinessCategoryId, reqVO.getBusinessCategoryId())
                .likeRight(StringUtils.isNotBlank(reqVO.getBusinessCategoryCode()), DpModelDO::getBusinessCategoryCode, reqVO.getBusinessCategoryCode())
                .eq(reqVO.getDataDomainId() != null, DpModelDO::getDataDomainId, reqVO.getDataDomainId())
                .eq(reqVO.getThemeDomainId() != null, DpModelDO::getThemeDomainId, reqVO.getThemeDomainId())
                .likeRight(StringUtils.isNotBlank(reqVO.getThemeDomainCode()), DpModelDO::getThemeDomainCode, reqVO.getThemeDomainCode())
//                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
//                                                                                                                                                                                            .split(",")) : null);
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
                                                                                                                                      .split(","))
                                                                                                                        .stream()
                                                                                                                        .map(e -> "t." + LambdaQueryWrapperX.camelToUnderline(e))
                                                                                                                        .collect(Collectors.toList()) : null);

        return selectJoinPage(reqVO, DpModelDO.class, lambdaWrapper);
    }

/**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode Old category code
     * @param newCatCode New category code
     * @return Number of affected rows
     */
    default int updateCatCode(String oldCatCode, String newCatCode) {
        return this.update(
                null,
                Wrappers.<DpModelDO>lambdaUpdate()
                        .set(DpModelDO::getCatCode, newCatCode)
                        .eq(DpModelDO::getDelFlag, "0")
                        .eq(DpModelDO::getCatCode, oldCatCode)
        );
    }
}
