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

package tech.qiantong.qdata.module.ai.controller.admin.chat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessagePageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatMessageSaveReqVO;
import tech.qiantong.qdata.module.ai.convert.chat.AiChatMessageConvert;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatMessageDO;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatMessageService;

/**
 * ai chat message controller
 *
 * @author FXB
 * @date 2026-04-01
 */
@Tag(name = "ai聊天消息")
@RestController
@RequestMapping("/ai/chatMessage")
@Validated
public class AiChatMessageController extends BaseController {
    @Resource
    private IAiChatMessageService aiChatMessageService;

    @Operation(summary = "查询ai聊天消息列表")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AiChatMessageRespVO>> list(AiChatMessagePageReqVO aiChatMessage) {
        PageResult<AiChatMessageDO> page = aiChatMessageService.getAiChatMessagePage(aiChatMessage);
        return CommonResult.success(BeanUtils.toBean(page, AiChatMessageRespVO.class));
    }

    @Operation(summary = "导出ai聊天消息列表")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:export')")
    @Log(title = "log.op.title.ai.chat.message", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiChatMessagePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AiChatMessageDO> list = (List<AiChatMessageDO>) aiChatMessageService.getAiChatMessagePage(exportReqVO).getRows();
        ExcelUtil<AiChatMessageRespVO> util = new ExcelUtil<>(AiChatMessageRespVO.class);
        util.exportExcel(response, AiChatMessageConvert.INSTANCE.convertToRespVOList(list), "Application Management Data");
    }

    @Operation(summary = "获取ai聊天消息详细信息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AiChatMessageRespVO> getInfo(@PathVariable("id") Long id) {
        AiChatMessageDO aiChatMessageDO = aiChatMessageService.getAiChatMessageById(id);
        return CommonResult.success(BeanUtils.toBean(aiChatMessageDO, AiChatMessageRespVO.class));
    }

    @Operation(summary = "新增ai聊天消息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:add')")
    @Log(title = "log.op.title.ai.chat.message", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiChatMessageSaveReqVO aiChatMessage) {
        aiChatMessage.setCreatorId(getUserId());
        aiChatMessage.setCreateBy(getNickName());
        aiChatMessage.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatMessageService.createAiChatMessage(aiChatMessage));
    }

    @Operation(summary = "修改ai聊天消息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:edit')")
    @Log(title = "log.op.title.ai.chat.message", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiChatMessageSaveReqVO aiChatMessage) {
        aiChatMessage.setUpdatorId(getUserId());
        aiChatMessage.setUpdateBy(getNickName());
        aiChatMessage.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatMessageService.updateAiChatMessage(aiChatMessage));
    }

    @Operation(summary = "删除ai聊天消息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:remove')")
    @Log(title = "log.op.title.ai.chat.message", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(aiChatMessageService.removeAiChatMessage(Arrays.asList(ids)));
    }

}
