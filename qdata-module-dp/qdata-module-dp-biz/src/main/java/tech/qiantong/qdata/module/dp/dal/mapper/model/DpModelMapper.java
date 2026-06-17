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
 * 逻辑模型Mapper接口
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
        //拼接查询发布数据源列表

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
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
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
