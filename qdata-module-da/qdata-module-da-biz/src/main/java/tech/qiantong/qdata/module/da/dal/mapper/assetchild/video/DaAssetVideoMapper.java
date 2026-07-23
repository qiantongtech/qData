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

package tech.qiantong.qdata.module.da.dal.mapper.assetchild.video;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Asset - Video Data Mapper Interface
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface DaAssetVideoMapper extends BaseMapperX<DaAssetVideoDO> {

    default PageResult<DaAssetVideoDO> selectPage(DaAssetVideoPageReqVO reqVO) {
        // Define sortable fields (prevent SQL injection, must match database column names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Build dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetVideoDO>()
                .eqIfPresent(DaAssetVideoDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetVideoDO::getIp, reqVO.getIp())
                .eqIfPresent(DaAssetVideoDO::getPort, reqVO.getPort())
                .eqIfPresent(DaAssetVideoDO::getProtocol, reqVO.getProtocol())
                .eqIfPresent(DaAssetVideoDO::getPlatform, reqVO.getPlatform())
                .eqIfPresent(DaAssetVideoDO::getConfig, reqVO.getConfig())
                .eqIfPresent(DaAssetVideoDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact match condition for name (name = '<name>')
                // .likeIfPresent(DaAssetVideoDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
