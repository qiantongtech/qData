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

package tech.qiantong.qdata.module.system.controller.admin.system;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;
import tech.qiantong.qdata.module.system.service.ISystemContentService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * System Configuration Controller
 *
 * @author qdata
 * @date 2024-12-31
 */
@Tag(name = "System Configuration")
@RestController
@Validated
public class SystemContentController extends BaseController {
    @Resource
    private ISystemContentService systemContentService;

    @Operation(summary = "Query system configuration list")
    @PreAuthorize("@ss.hasPermi('system:system:content:list')")
    @GetMapping("/system/content/list")
    public CommonResult<PageResult<SystemContentRespVO>> list(SystemContentPageReqVO systemContent) {
        PageResult<SystemContentDO> page = systemContentService.getSystemContentPage(systemContent);
        return CommonResult.success(BeanUtils.toBean(page, SystemContentRespVO.class));
    }

    @Operation(summary = "Get system configuration details")
    //Logo info needs to be fetched when the homepage is accessed without login
//    @PreAuthorize("@ss.hasPermi('system:system:content:query')")
    @GetMapping(value = "sys/content/{id}")
    public CommonResult<SystemContentRespVO> getInfo(@PathVariable("id") Long id) {
        SystemContentDO systemContentDO = systemContentService.getSystemContentById(id);
        return CommonResult.success(BeanUtils.toBean(systemContentDO, SystemContentRespVO.class));
    }

//    @Operation(summary = "Add system configuration")
//    @PreAuthorize("@ss.hasPermi('system:system:content:add')")
//    @Log(title = "log.op.title.system.config", businessType = BusinessType.INSERT)
//    @PostMapping
//    public CommonResult<Long> add(@Valid @RequestBody SystemContentSaveReqVO systemContent) {
//        systemContent.setCreatorId(getUserId());
//        systemContent.setCreateBy(getNickName());
//        systemContent.setCreateTime(DateUtil.date());
//        return CommonResult.toAjax(systemContentService.createSystemContent(systemContent));
//    }

    @Operation(summary = "Modify system configuration")
    @PreAuthorize("@ss.hasPermi('system:system:content:edit')")
    @Log(title = "log.op.title.system.config", businessType = BusinessType.UPDATE)
    @PostMapping("/system/content/edit")
    public CommonResult<Integer> edit(@Valid @RequestBody SystemContentSaveReqVO systemContent) {
        systemContent.setUpdatorId(getUserId());
        systemContent.setUpdateBy(getNickName());
        systemContent.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(systemContentService.updateSystemContent(systemContent));
    }

//    @Operation(summary = "Delete system configuration")
//    @PreAuthorize("@ss.hasPermi('system:system:content:remove')")
//    @Log(title = "log.op.title.system.config", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(systemContentService.removeSystemContent(Arrays.asList(ids)));
//    }

}
