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

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import tech.qiantong.qdata.ai.core.service.IChatMessageService;
import tech.qiantong.qdata.ai.core.vo.ChatMessageExportDetailDataReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendReqVO;
import tech.qiantong.qdata.ai.core.vo.ChatMessageSendRespVO;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageRespVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatMessageService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ai聊天消息Controller
 *
 * @author qknow
 * @date 2025-02-17
 */
@Tag(name = "ai聊天消息")
@RestController
@RequestMapping("/chat/message")
public class ChatMessageController extends BaseController {

    @Resource
    private IChatMessageService chatMessageService;

    @Resource
    private IAiChatMessageService aiChatMessageService;

    @Resource
    private IAiChatConversationService aiChatConversationService;

    @Operation(summary = "发送消息（流式）", description = "流式返回，响应较快")
    @PostMapping(value = "/send-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CommonResult<ChatMessageSendRespVO>> sendChatMessageStream(@RequestBody ChatMessageSendReqVO sendReqVO) {
        return chatMessageService.sendChatMessageStream(sendReqVO, 1l)
                .map(CommonResult::success)
                // 错误转换
                .onErrorResume(e -> Flux.just(CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), e.getMessage())));
    }

    @Operation(summary = "获得指定对话的消息列表")
    @GetMapping("/list-by-conversation-id")
    public CommonResult<List<AiChatMessageRespVO>> getChatMessageListByConversationId(
            @RequestParam("conversationId") Long conversationId) {
        AiChatConversationDO conversation = aiChatConversationService.getAiChatConversationById(conversationId);
        if (conversation == null || ObjUtil.notEqual(conversation.getUserId(), getUserId())) {
            return CommonResult.success(Collections.emptyList());
        }
        List<AiChatMessageDO> messageDOList = aiChatMessageService.list(Wrappers.lambdaQuery(AiChatMessageDO.class)
                .eq(AiChatMessageDO::getConversationId, conversationId));
        return CommonResult.success(BeanUtils.toBean(messageDOList, AiChatMessageRespVO.class));
    }

    @Operation(summary = "删除ai聊天消息")
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable("ids") Long[] ids) {
        return CommonResult.toAjax(aiChatMessageService.removeAiChatMessage(Arrays.asList(ids)));
    }

    @Operation(summary = "清空会话聊天消息")
    @DeleteMapping("/deleteByConversationId")
    public CommonResult<Integer> deleteByConversationId(@RequestParam("conversationId") Long conversationId) {
        aiChatMessageService.remove(Wrappers.lambdaQuery(AiChatMessageDO.class)
                .eq(AiChatMessageDO::getConversationId, conversationId));
        return CommonResult.success(null);
    }


    @Operation(summary = "导出智能图表的明细数据")
    @GetMapping(value = "/exportDetailData")
    public void sendChatMessageStream(HttpServletResponse response, ChatMessageExportDetailDataReqVO exportDetailDataReqVO) {
        chatMessageService.exportDetailData(response, exportDetailDataReqVO);
    }

}
