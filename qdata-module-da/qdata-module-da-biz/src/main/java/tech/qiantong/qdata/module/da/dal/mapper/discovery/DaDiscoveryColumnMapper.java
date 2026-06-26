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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据发现字段Mapper接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface DaDiscoveryColumnMapper extends BaseMapperX<DaDiscoveryColumnDO> {

    default PageResult<DaDiscoveryColumnDO> selectPage(DaDiscoveryColumnPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDiscoveryColumnDO>()
                .eqIfPresent(DaDiscoveryColumnDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaDiscoveryColumnDO::getTableId, reqVO.getTableId())
                .likeIfPresent(DaDiscoveryColumnDO::getColumnName, reqVO.getColumnName())
                .eqIfPresent(DaDiscoveryColumnDO::getColumnComment, reqVO.getColumnComment())
                .eqIfPresent(DaDiscoveryColumnDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DaDiscoveryColumnDO::getColumnLength, reqVO.getColumnLength())
                .eqIfPresent(DaDiscoveryColumnDO::getColumnScale, reqVO.getColumnScale())
                .eqIfPresent(DaDiscoveryColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eqIfPresent(DaDiscoveryColumnDO::getPkFlag, reqVO.getPkFlag())
                .eqIfPresent(DaDiscoveryColumnDO::getDefaultValue, reqVO.getDefaultValue())
                .eqIfPresent(DaDiscoveryColumnDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaDiscoveryColumnDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
