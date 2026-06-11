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
