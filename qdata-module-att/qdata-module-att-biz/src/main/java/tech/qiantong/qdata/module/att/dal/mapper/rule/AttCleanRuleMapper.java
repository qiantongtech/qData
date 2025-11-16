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

package tech.qiantong.qdata.module.att.dal.mapper.rule;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 清洗规则Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttCleanRuleMapper extends BaseMapperX<AttCleanRuleDO> {

    default PageResult<AttCleanRuleDO> selectPage(AttCleanRulePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<AttCleanRuleDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttCleanRuleDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_CLEAN_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), AttCleanRuleDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), AttCleanRuleDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getType()), AttCleanRuleDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getCode()), AttCleanRuleDO::getCode, reqVO.getCode())
                .eq(StringUtils.isNotBlank(reqVO.getLevel()), AttCleanRuleDO::getLevel, reqVO.getLevel())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, AttCleanRuleDO.class, lambdaWrapper);
    }

    List<AttCleanRuleDO> selectAttCleanRuleList(@Param("dataElemId") Long dataElemId);

    List<AttCleanRuleDO> getCleaningRuleTreeIds(Long[] dataElemId);
}
