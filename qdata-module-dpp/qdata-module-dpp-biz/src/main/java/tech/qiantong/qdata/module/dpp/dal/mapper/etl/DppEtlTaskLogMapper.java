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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Integration Task-Log Mapper
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface DppEtlTaskLogMapper extends BaseMapperX<DppEtlTaskLogDO> {

    default PageResult<DppEtlTaskLogDO> selectPage(DppEtlTaskLogPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DppEtlTaskLogDO>()
                .eqIfPresent(DppEtlTaskLogDO::getType, reqVO.getType())
                .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                .eqIfPresent(DppEtlTaskLogDO::getCode, reqVO.getCode())
                .eqIfPresent(DppEtlTaskLogDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DppEtlTaskLogDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(DppEtlTaskLogDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(DppEtlTaskLogDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DppEtlTaskLogDO::getLocations, reqVO.getLocations())
                .eqIfPresent(DppEtlTaskLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DppEtlTaskLogDO::getTimeout, reqVO.getTimeout())
                .eqIfPresent(DppEtlTaskLogDO::getExtractionCount, reqVO.getExtractionCount())
                .eqIfPresent(DppEtlTaskLogDO::getWriteCount, reqVO.getWriteCount())
                .eqIfPresent(DppEtlTaskLogDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DppEtlTaskLogDO::getDsId, reqVO.getDsId())
                .eqIfPresent(DppEtlTaskLogDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact name match condition (name = '<name>')
                // .likeIfPresent(DppEtlTaskLogDO::getName, reqVO.getName())
                // Order by createTime descending
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    /**
     * Get max version by task code
     *
     * @param taskCode
     * @return
     */
    Integer queryMaxVersionByCode(@Param("taskCode") String taskCode);
}
