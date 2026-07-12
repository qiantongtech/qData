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

package tech.qiantong.qdata.module.system.mapper;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * System configuration Mapper interface
 *
 * @author qdata
 * @date 2024-12-31
 */
public interface SystemContentMapper extends BaseMapperX<SystemContentDO> {

    default PageResult<SystemContentDO> selectPage(SystemContentPageReqVO reqVO) {
        // Define sortable columns (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemContentDO>()
                .likeIfPresent(SystemContentDO::getSysName, reqVO.getSysName())
                .eqIfPresent(SystemContentDO::getLogo, reqVO.getLogo())
                .eqIfPresent(SystemContentDO::getCarouselImage, reqVO.getCarouselImage())
                .eqIfPresent(SystemContentDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(SystemContentDO::getEmail, reqVO.getEmail())
                .eqIfPresent(SystemContentDO::getCopyright, reqVO.getCopyright())
                .eqIfPresent(SystemContentDO::getRecordNumber, reqVO.getRecordNumber())
                .eqIfPresent(SystemContentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(SystemContentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SystemContentDO::getRemark, reqVO.getRemark())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(SystemContentDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
