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

package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.*;

/**
 * Data Element Rule Relationship Mapper Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemRuleRelMapper extends BaseMapperX<DpDataElemRuleRelDO> {

    default PageResult<DpDataElemRuleRelDO> selectPage(DpDataElemRuleRelPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpDataElemRuleRelDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DpDataElemRuleRelDO.class);
        lambdaWrapper.eq(StringUtils.isNotBlank(reqVO.getType()), DpDataElemRuleRelDO::getType, reqVO.getType())
                .eq(reqVO.getDataElemId() != null, DpDataElemRuleRelDO::getDataElemId, reqVO.getDataElemId())
                .eq(reqVO.getRuleId() != null, DpDataElemRuleRelDO::getRuleId, reqVO.getRuleId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DpDataElemRuleRelDO.class, lambdaWrapper);
    }

    default List<DpDataElemRuleRelDO> listByDataElemIdList(Collection<Long> dataElemIdList, String type) {
        LambdaQueryWrapper<DpDataElemRuleRelDO> queryWrapper = Wrappers.<DpDataElemRuleRelDO>lambdaQuery().in(DpDataElemRuleRelDO::getDataElemId, dataElemIdList)
                .eq(DpDataElemRuleRelDO::getType, type);
        return selectList(queryWrapper);
    }

}
