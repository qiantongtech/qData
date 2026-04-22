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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
