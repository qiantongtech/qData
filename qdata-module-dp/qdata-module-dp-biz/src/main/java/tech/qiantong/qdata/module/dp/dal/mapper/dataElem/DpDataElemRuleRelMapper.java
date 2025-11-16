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
 * 数据元数据规则关联信息Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemRuleRelMapper extends BaseMapperX<DpDataElemRuleRelDO> {

    default PageResult<DpDataElemRuleRelDO> selectPage(DpDataElemRuleRelPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
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
