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
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.chat.vo.AiChatConversationSaveReqVO;
import tech.qiantong.qdata.module.ai.convert.chat.AiChatConversationConvert;
import tech.qiantong.qdata.module.ai.dal.dataobject.chat.AiChatConversationDO;
import tech.qiantong.qdata.module.ai.service.chat.IAiChatConversationService;

/**
 * ai聊天对话Controller
 *
 * @author FXB
 * @date 2026-04-01
 */
@Tag(name = "ai聊天对话")
@RestController
@RequestMapping("/ai/chatConversation")
@Validated
public class AiChatConversationController extends BaseController {
    @Resource
    private IAiChatConversationService aiChatConversationService;

    @Operation(summary = "查询ai聊天对话列表")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AiChatConversationRespVO>> list(AiChatConversationPageReqVO aiChatConversation) {
        PageResult<AiChatConversationDO> page = aiChatConversationService.getAiChatConversationPage(aiChatConversation);
        return CommonResult.success(BeanUtils.toBean(page, AiChatConversationRespVO.class));
    }

    @Operation(summary = "导出ai聊天对话列表")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:export')")
    @Log(title = "ai聊天对话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiChatConversationPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AiChatConversationDO> list = (List<AiChatConversationDO>) aiChatConversationService.getAiChatConversationPage(exportReqVO).getRows();
        ExcelUtil<AiChatConversationRespVO> util = new ExcelUtil<>(AiChatConversationRespVO.class);
        util.exportExcel(response, AiChatConversationConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取ai聊天对话详细信息")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AiChatConversationRespVO> getInfo(@PathVariable("id") Long id) {
        AiChatConversationDO aiChatConversationDO = aiChatConversationService.getAiChatConversationById(id);
        return CommonResult.success(BeanUtils.toBean(aiChatConversationDO, AiChatConversationRespVO.class));
    }

    @Operation(summary = "新增ai聊天对话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:add')")
    @Log(title = "ai聊天对话", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiChatConversationSaveReqVO aiChatConversation) {
        aiChatConversation.setCreatorId(getUserId());
        aiChatConversation.setCreateBy(getNickName());
        aiChatConversation.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatConversationService.createAiChatConversation(aiChatConversation));
    }

    @Operation(summary = "修改ai聊天对话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:edit')")
    @Log(title = "ai聊天对话", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiChatConversationSaveReqVO aiChatConversation) {
        aiChatConversation.setUpdatorId(getUserId());
        aiChatConversation.setUpdateBy(getNickName());
        aiChatConversation.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiChatConversationService.updateAiChatConversation(aiChatConversation));
    }

    @Operation(summary = "删除ai聊天对话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:remove')")
    @Log(title = "ai聊天对话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(aiChatConversationService.removeAiChatConversation(Arrays.asList(ids)));
    }

}
