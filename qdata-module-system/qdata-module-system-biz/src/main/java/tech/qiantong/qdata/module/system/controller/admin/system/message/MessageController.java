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

package tech.qiantong.qdata.module.system.controller.admin.system.message;

import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageRespVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSend;
import tech.qiantong.qdata.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import tech.qiantong.qdata.module.system.convert.message.MessageConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageDO;
import tech.qiantong.qdata.module.system.dal.dataobject.message.enums.MessageHasReadEnums;
import tech.qiantong.qdata.module.system.service.message.IMessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Message Controller
 *
 * @author qdata
 * @date 2024-10-31
 */
@Tag(name = "Message")
@RestController
@RequestMapping("/system/message")
@Validated
public class MessageController extends BaseController {
    @Resource
    private IMessageService messageService;
    @Resource
    private WebSocketMessageServer webSocketMessageServer;

    @Operation(summary = "Query message list")
//    @PreAuthorize("@ss.hasPermi('system:message:message:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<MessageRespVO>> list(MessagePageReqVO message) {
        startPage();
        PageResult<MessageDO> page = messageService.getMessagePage(message);
        return CommonResult.success(BeanUtils.toBean(page, MessageRespVO.class));
    }

    @Operation(summary = "Export message list")
    @PreAuthorize("@ss.hasPermi('system:message:message:export')")
    @Log(title = "log.op.title.system.message", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessagePageReqVO message) {
        List<MessageDO> list = (List<MessageDO>) messageService.getMessagePage(message).getRows();
        ExcelUtil<MessageRespVO> util = new ExcelUtil<>(MessageRespVO.class);
        util.exportExcel(response, MessageConvert.INSTANCE.convertToRespVOList(list), "Message Data");
    }

    @Operation(summary = "Get message details")
    @PreAuthorize("@ss.hasPermi('system:message:message:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<MessageRespVO> getInfo(@PathVariable("id") Long id) {
        MessageDO messageDO = messageService.getById(id);
        return CommonResult.success(BeanUtils.toBean(messageDO, MessageRespVO.class));
    }

    @Operation(summary = "Add message")
    @PreAuthorize("@ss.hasPermi('system:message:message:add')")
    @Log(title = "log.op.title.system.message", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody MessageSaveReqVO message) {
        MessageDO messageDO = BeanUtils.toBean(message, MessageDO.class);
        messageDO.setCreatorId(getUserId());
        messageDO.setCreateBy(getNickName());
        // Notify online users of new message
//        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
//        messagePageReqVO.setContent(messageDO.getContent());
//        messagePageReqVO.setTitle(messageDO.getTitle());
//        messagePageReqVO.setEntityType(null);
//        messagePageReqVO.setCreateTime(new Date());
//        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        return CommonResult.toAjax(messageService.save(messageDO));
    }

    @Operation(summary = "Modify message")
    @PreAuthorize("@ss.hasPermi('system:message:message:edit')")
    @Log(title = "log.op.title.system.message", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Boolean> edit(@Valid @RequestBody MessageSaveReqVO message) {
        MessageDO messageDO = BeanUtils.toBean(message, MessageDO.class);
        messageDO.setUpdatorId(getUserId());
        messageDO.setUpdateBy(getNickName());
        messageDO.setUpdateTime(new Date());
        // Notify online users of new message
//        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
//        messagePageReqVO.setContent(messageDO.getContent());
//        messagePageReqVO.setTitle(messageDO.getTitle());
//        messagePageReqVO.setEntityType(null);
//        messagePageReqVO.setCreateTime(new Date());
//        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        return CommonResult.toAjax(messageService.updateById(messageDO));
    }

    @Operation(summary = "Delete message")
    @PreAuthorize("@ss.hasPermi('system:message:message:remove')")
    @Log(title = "log.op.title.system.message", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Boolean> remove(@PathVariable Long[] ids) {
        boolean b = messageService.removeByIds(Arrays.asList(ids));
        messageService.getReceiverWDNum(getUserId());
        return CommonResult.toAjax(b);
    }

    @Operation(summary = "Query message count")
    //@PreAuthorize("@ss.hasPermi('system:message:message:list')")
    @GetMapping("/getNum")
    public CommonResult<Long> getNum(MessagePageReqVO message) {
        message.setHasRead(MessageHasReadEnums.WD.code);
        message.setReceiverId(getUserId());
        return CommonResult.success(messageService.getNum(message));
    }

    @Operation(summary = "Mark message as read")
    @PostMapping("/read")
    public CommonResult<Boolean> read(Long id) {
        return CommonResult.toAjax(messageService.read(id));
    }

    @Operation(summary = "Mark all messages as read")
    @PostMapping("/readAll")
    public CommonResult<Boolean> readAll(Integer category, Integer module) {
        return CommonResult.toAjax(messageService.readAll(getUserId(), category, module));
    }

    @Operation(summary = "Test add message")
    @GetMapping("/test")
    public CommonResult<Boolean> test(MessageSaveReqVO message, Long templateId, String context ) {
        Map<String, Object> map  = Maps.newLinkedHashMap();
        map.put("test", context);
        return CommonResult.success(messageService.send(templateId, message, map));
    }

    @Operation(summary = "Send message")
    @PostMapping("/send")
    public CommonResult<Boolean> send(@RequestBody @Valid MessageSend message) {
        MessageSaveReqVO messageSaveReqDTO = new MessageSaveReqVO();
        messageSaveReqDTO.setSenderId(1L);
        messageSaveReqDTO.setCreatorId(1L);
        messageSaveReqDTO.setCreateBy("Super Admin");
        messageSaveReqDTO.setReceiverId(message.getReceiverId());
        return CommonResult.success(messageService.send(message.getTemplateId(), messageSaveReqDTO, message.getData()));
    }

}
