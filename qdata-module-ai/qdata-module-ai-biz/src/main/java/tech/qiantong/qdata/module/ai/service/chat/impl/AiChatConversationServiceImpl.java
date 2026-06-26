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

package tech.qiantong.qdata.module.ai.service.chat.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import tech.qiantong.qdata.module.ai.dal.mapper.chat.AiChatConversationMapper;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;

/**
 * ai聊天对话Service业务层处理
 *
 * @author FXB
 * @date 2026-04-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiChatConversationServiceImpl extends ServiceImpl<AiChatConversationMapper, AiChatConversationDO> implements IAiChatConversationService {
    @Resource
    private AiChatConversationMapper aiChatConversationMapper;

    @Override
    public PageResult<AiChatConversationDO> getAiChatConversationPage(AiChatConversationPageReqVO pageReqVO) {
        return aiChatConversationMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAiChatConversation(AiChatConversationSaveReqVO createReqVO) {
        AiChatConversationDO dictType = BeanUtils.toBean(createReqVO, AiChatConversationDO.class);
        aiChatConversationMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAiChatConversation(AiChatConversationSaveReqVO updateReqVO) {
        // 相关校验

        // 更新ai聊天对话
        AiChatConversationDO updateObj = BeanUtils.toBean(updateReqVO, AiChatConversationDO.class);
        return aiChatConversationMapper.updateById(updateObj);
    }

    @Override
    public int removeAiChatConversation(Collection<Long> idList) {
        // 批量删除ai聊天对话
        return aiChatConversationMapper.deleteBatchIds(idList);
    }

    @Override
    public AiChatConversationDO getAiChatConversationById(Long id) {
        return aiChatConversationMapper.selectById(id);
    }

    @Override
    public List<AiChatConversationDO> getAiChatConversationList() {
        return aiChatConversationMapper.selectList();
    }

    @Override
    public Map<Long, AiChatConversationDO> getAiChatConversationMap() {
        List<AiChatConversationDO> aiChatConversationList = aiChatConversationMapper.selectList();
        return aiChatConversationList.stream()
                .collect(Collectors.toMap(
                        AiChatConversationDO::getId,
                        aiChatConversationDO -> aiChatConversationDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public List<AiChatConversationDO> getChatConversationListByUserId(Long userId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(AiChatConversationDO.class)
                .eq(AiChatConversationDO::getUserId, userId)
                .orderByDesc(BaseEntity::getCreateTime));
    }
}
