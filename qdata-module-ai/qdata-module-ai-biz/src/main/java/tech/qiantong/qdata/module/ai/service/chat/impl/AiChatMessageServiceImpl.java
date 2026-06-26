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

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessagePageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;
import tech.qiantong.qdata.module.ai.dal.mapper.chat.AiChatMessageMapper;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatMessageService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * ai聊天消息Service业务层处理
 *
 * @author FXB
 * @date 2026-04-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AiChatMessageServiceImpl extends ServiceImpl<AiChatMessageMapper, AiChatMessageDO> implements IAiChatMessageService {
    @Resource
    private AiChatMessageMapper aiChatMessageMapper;

    @Override
    public PageResult<AiChatMessageDO> getAiChatMessagePage(AiChatMessagePageReqVO pageReqVO) {
        return aiChatMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAiChatMessage(AiChatMessageSaveReqVO createReqVO) {
        AiChatMessageDO dictType = BeanUtils.toBean(createReqVO, AiChatMessageDO.class);
        aiChatMessageMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAiChatMessage(AiChatMessageSaveReqVO updateReqVO) {
        // 相关校验

        // 更新ai聊天消息
        AiChatMessageDO updateObj = BeanUtils.toBean(updateReqVO, AiChatMessageDO.class);
        return aiChatMessageMapper.updateById(updateObj);
    }

    @Override
    public int removeAiChatMessage(Collection<Long> idList) {
//        //删除所有回复
//        aiChatMessageMapper.delete(Wrappers.lambdaUpdate(AiChatMessageDO.class)
//                .notIn(AiChatMessageDO::getReplyId, idList));
        // 批量删除ai聊天消息
        return aiChatMessageMapper.deleteBatchIds(idList);
    }

    @Override
    public AiChatMessageDO getAiChatMessageById(Long id) {
        return aiChatMessageMapper.selectById(id);
    }

    @Override
    public List<AiChatMessageDO> getAiChatMessageList() {
        return aiChatMessageMapper.selectList();
    }

    @Override
    public Map<Long, AiChatMessageDO> getAiChatMessageMap() {
        List<AiChatMessageDO> aiChatMessageList = aiChatMessageMapper.selectList();
        return aiChatMessageList.stream()
                .collect(Collectors.toMap(
                        AiChatMessageDO::getId,
                        aiChatMessageDO -> aiChatMessageDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public List<AiChatMessageDO> getChatMessageListByConversationId(Long conversationId) {
        return baseMapper.selectList(new LambdaQueryWrapperX<AiChatMessageDO>()
                .eq(AiChatMessageDO::getConversationId, conversationId)
                .orderByAsc(AiChatMessageDO::getId));
    }
}
