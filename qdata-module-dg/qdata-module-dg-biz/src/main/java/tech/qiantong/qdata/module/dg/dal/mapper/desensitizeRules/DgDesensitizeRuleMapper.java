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

package tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules;

import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Desensitization Rule Mapper Interface
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface DgDesensitizeRuleMapper extends BaseMapperX<DgDesensitizeRuleDO> {

    default PageResult<DgDesensitizeRuleDO> selectPage(DgDesensitizeRulePageReqVO reqVO) {
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapperX<DgDesensitizeRuleDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DgDesensitizeRuleDO.class)
                .select("t2.NAME AS dataCategoryName")
                .leftJoin("DG_DATA_CATEGORY t2 ON t.DATA_CATEGORY_ID =t2.ID  AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DgDesensitizeRuleDO::getName, reqVO.getName())
                .eq(reqVO.getDataCategoryId() != null, DgDesensitizeRuleDO::getDataCategoryId, reqVO.getDataCategoryId())
                // Query by ValidFlag
                .eq(reqVO.getValidFlag() != null, DgDesensitizeRuleDO::getValidFlag, reqVO.getValidFlag())
                // Sort by createTime field in descending order
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // Build dynamic query conditions
        return selectJoinPage(reqVO, DgDesensitizeRuleDO.class, lambdaWrapper);





    }
}
