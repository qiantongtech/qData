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

package tech.qiantong.qdata.module.ai.dal.mapper.model;

import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Model management Mapper interface
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface AiModelMapper extends BaseMapperX<AiModelDO> {

    default PageResult<AiModelDO> selectPage(AiModelPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time","sort_order"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AiModelDO>()
                .likeIfPresent(AiModelDO::getName, reqVO.getName())
                .eqIfPresent(AiModelDO::getPlatform, reqVO.getPlatform())
                .eqIfPresent(AiModelDO::getApiUrl, reqVO.getApiUrl())
                .eqIfPresent(AiModelDO::getApiKey, reqVO.getApiKey())
                .eqIfPresent(AiModelDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AiModelDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AiModelDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AiModelDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(AiModelDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
