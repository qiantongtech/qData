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
