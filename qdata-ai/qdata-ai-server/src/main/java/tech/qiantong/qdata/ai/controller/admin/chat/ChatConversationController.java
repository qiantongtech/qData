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

package tech.qiantong.qdata.ai.controller.admin.chat;

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
 * 用途:ai聊天对话
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
        appChatConversation.setTitle(Optional.ofNullable(appChatConversation.getTitle()).orElse("新对话"));
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
            throw new RuntimeException("请设置关联关系！");
        }
        appChatConversation.setUpdatorId(getUserId());
        appChatConversation.setUpdateBy(getNickName());
        appChatConversation.setUpdateTime(DateUtil.date());
        appChatConversation.setJoinConditionMatchFlag(true);
        return CommonResult.toAjax(aiChatConversationService.updateAiChatConversation(appChatConversation));
    }
}
