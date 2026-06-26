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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据发现任务日志Mapper接口
 *
 * @author qdata
 * @date 2025-02-17
 */
public interface DaDiscoveryTaskLogMapper extends BaseMapperX<DaDiscoveryTaskLogDO> {

    default PageResult<DaDiscoveryTaskLogDO> selectPage(DaDiscoveryTaskLogPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDiscoveryTaskLogDO>()
                .likeIfPresent(DaDiscoveryTaskLogDO::getName, reqVO.getName())
                .eqIfPresent(DaDiscoveryTaskLogDO::getNodeId, reqVO.getNodeId())
                .eqIfPresent(DaDiscoveryTaskLogDO::getNodeCode, reqVO.getNodeCode())
                .likeIfPresent(DaDiscoveryTaskLogDO::getTaskName, reqVO.getTaskName())
                .eqIfPresent(DaDiscoveryTaskLogDO::getTaskId, reqVO.getTaskId())
                .eqIfPresent(DaDiscoveryTaskLogDO::getTaskCode, reqVO.getTaskCode())
                .eqIfPresent(DaDiscoveryTaskLogDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(DaDiscoveryTaskLogDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(DaDiscoveryTaskLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DaDiscoveryTaskLogDO::getNewTableCount, reqVO.getNewTableCount())
                .eqIfPresent(DaDiscoveryTaskLogDO::getModifiedTableCount, reqVO.getModifiedTableCount())
                .eqIfPresent(DaDiscoveryTaskLogDO::getDeletedTableCount, reqVO.getDeletedTableCount())
                .eqIfPresent(DaDiscoveryTaskLogDO::getContact, reqVO.getContact())
                .eqIfPresent(DaDiscoveryTaskLogDO::getContactId, reqVO.getContactId())
                .eqIfPresent(DaDiscoveryTaskLogDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DaDiscoveryTaskLogDO::getEmail, reqVO.getEmail())
                .eqIfPresent(DaDiscoveryTaskLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DaDiscoveryTaskLogDO::getDsTaskInstanceId, reqVO.getDsTaskInstanceId())
                .eqIfPresent(DaDiscoveryTaskLogDO::getPath, reqVO.getPath())
                .eqIfPresent(DaDiscoveryTaskLogDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaDiscoveryTaskLogDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
