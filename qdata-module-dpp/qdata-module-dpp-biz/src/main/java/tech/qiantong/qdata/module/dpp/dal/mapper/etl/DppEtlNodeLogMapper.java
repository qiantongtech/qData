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

package tech.qiantong.qdata.module.dpp.dal.mapper.etl;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Integration Node-Log Mapper
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlNodeLogMapper extends BaseMapperX<DppEtlNodeLogDO> {

    default PageResult<DppEtlNodeLogDO> selectPage(DppEtlNodeLogPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlNodeLogDO>()
                .eqIfPresent(DppEtlNodeLogDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlNodeLogDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlNodeLogDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlNodeLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlNodeLogDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlNodeLogDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlNodeLogDO::getParameters, reqVO.getParameters())
                .eqIfPresent(DppEtlNodeLogDO::getPriority, reqVO.getPriority())
                .eqIfPresent(DppEtlNodeLogDO::getFailRetryTimes, reqVO.getFailRetryTimes())
                .eqIfPresent(DppEtlNodeLogDO::getFailRetryInterval, reqVO.getFailRetryInterval())
                .eqIfPresent(DppEtlNodeLogDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlNodeLogDO::getDelayTime, reqVO.getDelayTime())
                .eqIfPresent(DppEtlNodeLogDO::getCpuQuota, reqVO.getCpuQuota())
                .eqIfPresent(DppEtlNodeLogDO::getMemoryMax, reqVO.getMemoryMax())
                .eqIfPresent(DppEtlNodeLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlNodeLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlNodeLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact name match condition (name = '<name>')
                // .likeIfPresent(DppEtlNodeLogDO::getName, reqVO.getName())
                // Order by createTime descending
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    Integer getMaxVersionByNodeCode(@Param("nodeCode") String nodeCode);
}
