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

package tech.qiantong.qdata.module.ai.dal.mapper.chat;

import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import java.util.Arrays;
import com.github.yulichang.base.MPJBaseMapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import java.util.HashSet;
import java.util.Set;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * ai chat conversation Mapper interface
 *
 * @author FXB
 * @date 2026-04-01
 */
public interface AiChatConversationMapper extends BaseMapperX<AiChatConversationDO> {

    default PageResult<AiChatConversationDO> selectPage(AiChatConversationPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<AiChatConversationDO>()
                .eqIfPresent(AiChatConversationDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AiChatConversationDO::getTitle, reqVO.getTitle())
                .eqIfPresent(AiChatConversationDO::getPinned, reqVO.getPinned())
                .eqIfPresent(AiChatConversationDO::getPinnedTime, reqVO.getPinnedTime())
                .eqIfPresent(AiChatConversationDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(AiChatConversationDO::getFactTableName, reqVO.getFactTableName())
                .eqIfPresent(AiChatConversationDO::getFactTableComment, reqVO.getFactTableComment())
                .eqIfPresent(AiChatConversationDO::getDimensionTable, reqVO.getDimensionTable())
                .eqIfPresent(AiChatConversationDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AiChatConversationDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(AiChatConversationDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
