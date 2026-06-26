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

package tech.qiantong.qdata.module.da.dal.mapper.discovery;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryLogBodyPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryLogBodyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据发现节点实例-日志Mapper接口
 *
 * @author qdata
 * @date 2025-10-15
 */
public interface DaDiscoveryLogBodyMapper extends BaseMapperX<DaDiscoveryLogBodyDO> {

    default PageResult<DaDiscoveryLogBodyDO> selectPage(DaDiscoveryLogBodyPageReqVO reqVO) {
        // 允许排序字段，防止 SQL 注入
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("tm", "task_id", "create_time", "update_time"));

        return selectPage(reqVO, new LambdaQueryWrapperX<DaDiscoveryLogBodyDO>()
                .eqIfPresent(DaDiscoveryLogBodyDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaDiscoveryLogBodyDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DaDiscoveryLogBodyDO::getDelFlag, reqVO.getDelFlag())
                .likeIfPresent(DaDiscoveryLogBodyDO::getLogContent, reqVO.getLogContent())
                .betweenIfPresent(DaDiscoveryLogBodyDO::getTm, reqVO.getBeginTm(), reqVO.getEndTm())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
