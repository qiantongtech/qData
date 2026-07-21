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

package tech.qiantong.qdata.ai.controller.admin.chat;
import tech.qiantong.qdata.common.exception.ServiceException;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.ai.core.service.IChatConversationService;
import tech.qiantong.qdata.ai.core.vo.AiChatConversationSaveRespVO;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import tech.qiantong.qdata.module.ai.dal.mapper.model.AiModelMapper;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <P>
 * Purpose: ai chat conversation
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-08 14:12
 **/
@Tag(name = "ai聊天对话")
@RestController
@RequestMapping("/chat/conversation")
public class ChatConversationController extends BaseController {

    @Resource
    private IAiChatConversationService aiChatConversationService;

    @Resource
    private IChatConversationService chatConversationService;

    @Operation(summary = "查询ai聊天对话列表")
    @GetMapping("/myList")
    public CommonResult<List<AiChatConversationRespVO>> myList() {
        List<AiChatConversationDO> list = aiChatConversationService.getChatConversationListByUserId(getUserId());
        return CommonResult.success(BeanUtils.toBean(list, AiChatConversationRespVO.class));
    }

    @Operation(summary = "获取ai聊天对话详细信息")
    @PreAuthorize("@ss.hasPermi('app:chat:conversation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AiChatConversationRespVO> getInfo(@PathVariable("id") Long id) {
        AiChatConversationDO aiChatConversationDO = aiChatConversationService.getAiChatConversationById(id);
        return CommonResult.success(BeanUtils.toBean(aiChatConversationDO, AiChatConversationRespVO.class));
    }

    @Operation(summary = "新增ai聊天对话")
    @PostMapping
    public CommonResult<AiChatConversationSaveRespVO> add(@Valid @RequestBody AiChatConversationSaveReqVO appChatConversation) {
        appChatConversation.setCreatorId(getUserId());
        appChatConversation.setCreateBy(getNickName());
        appChatConversation.setCreateTime(DateUtil.date());
        appChatConversation.setTitle(Optional.ofNullable(appChatConversation.getTitle()).orElseGet(() ->
                MessageUtils.messageWithFallback("ai.chat.title.default", "New Conversation")));
        return CommonResult.success(chatConversationService.createAiChatConversation(appChatConversation));
    }

    @Operation(summary = "修改ai聊天对话")
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiChatConversationSaveReqVO appChatConversation) {
        appChatConversation.setUpdatorId(getUserId());
        appChatConversation.setUpdateBy(getNickName());
        appChatConversation.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatConversationService.updateAiChatConversation(appChatConversation));
    }

    @Operation(summary = "删除ai聊天对话")
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(aiChatConversationService.removeAiChatConversation(Arrays.asList(ids)));
    }

    @Operation(summary = "设置关联关系")
    @PostMapping("/setAssociations")
    public CommonResult<Integer> associations(@RequestBody AiChatConversationSaveReqVO appChatConversation) {
        if (StringUtils.isBlank(appChatConversation.getAssociations())) {
            throw new ServiceException("ai.error.relation.required", "Please configure the relationship.");
        }
        appChatConversation.setUpdatorId(getUserId());
        appChatConversation.setUpdateBy(getNickName());
        appChatConversation.setUpdateTime(DateUtil.date());
        appChatConversation.setJoinConditionMatchFlag(true);
        return CommonResult.toAjax(aiChatConversationService.updateAiChatConversation(appChatConversation));
    }
}
