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
 * ai聊天消息Controller
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
    @Log(title = "ai聊天消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiChatMessagePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AiChatMessageDO> list = (List<AiChatMessageDO>) aiChatMessageService.getAiChatMessagePage(exportReqVO).getRows();
        ExcelUtil<AiChatMessageRespVO> util = new ExcelUtil<>(AiChatMessageRespVO.class);
        util.exportExcel(response, AiChatMessageConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
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
    @Log(title = "ai聊天消息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiChatMessageSaveReqVO aiChatMessage) {
        aiChatMessage.setCreatorId(getUserId());
        aiChatMessage.setCreateBy(getNickName());
        aiChatMessage.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatMessageService.createAiChatMessage(aiChatMessage));
    }

    @Operation(summary = "修改ai聊天消息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:edit')")
    @Log(title = "ai聊天消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiChatMessageSaveReqVO aiChatMessage) {
        aiChatMessage.setUpdatorId(getUserId());
        aiChatMessage.setUpdateBy(getNickName());
        aiChatMessage.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatMessageService.updateAiChatMessage(aiChatMessage));
    }

    @Operation(summary = "删除ai聊天消息")
    @PreAuthorize("@ss.hasPermi('ai:chatmessage:remove')")
    @Log(title = "ai聊天消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(aiChatMessageService.removeAiChatMessage(Arrays.asList(ids)));
    }

}
