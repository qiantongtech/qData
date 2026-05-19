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
 * 数据分类Mapper接口
 *
 * @author qdata
 * @date 2026-04-07
 */
public interface DgDataCategoryMapper extends BaseMapperX<DgDataCategoryDO> {

    default PageResult<DgDataCategoryDO> selectPage(DgDataCategoryPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
                // 按照 createTime 字段降序排序
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // 构造动态查询条件
        return selectJoinPage(reqVO, DgDataCategoryDO.class, lambdaWrapper);
    }
}
