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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 系统配置Controller
 *
 * @author qdata
 * @date 2024-12-31
 */
@Tag(name = "系统配置")
@RestController
@Validated
public class SystemContentController extends BaseController {
    @Resource
    private ISystemContentService systemContentService;

    @Operation(summary = "查询系统配置列表")
    @PreAuthorize("@ss.hasPermi('system:system:content:list')")
    @GetMapping("/system/content/list")
    public CommonResult<PageResult<SystemContentRespVO>> list(SystemContentPageReqVO systemContent) {
        PageResult<SystemContentDO> page = systemContentService.getSystemContentPage(systemContent);
        return CommonResult.success(BeanUtils.toBean(page, SystemContentRespVO.class));
    }

    @Operation(summary = "获取系统配置详细信息")
    //首页不登录时要获取logo信息
//    @PreAuthorize("@ss.hasPermi('system:system:content:query')")
    @GetMapping(value = "sys/content/{id}")
    public CommonResult<SystemContentRespVO> getInfo(@PathVariable("id") Long id) {
        SystemContentDO systemContentDO = systemContentService.getSystemContentById(id);
        return CommonResult.success(BeanUtils.toBean(systemContentDO, SystemContentRespVO.class));
    }

//    @Operation(summary = "新增系统配置")
//    @PreAuthorize("@ss.hasPermi('system:system:content:add')")
//    @Log(title = "系统配置", businessType = BusinessType.INSERT)
//    @PostMapping
//    public CommonResult<Long> add(@Valid @RequestBody SystemContentSaveReqVO systemContent) {
//        systemContent.setCreatorId(getUserId());
//        systemContent.setCreateBy(getNickName());
//        systemContent.setCreateTime(DateUtil.date());
//        return CommonResult.toAjax(systemContentService.createSystemContent(systemContent));
//    }

    @Operation(summary = "修改系统配置")
    @PreAuthorize("@ss.hasPermi('system:system:content:edit')")
    @Log(title = "系统配置", businessType = BusinessType.UPDATE)
    @PostMapping("/system/content/edit")
    public CommonResult<Integer> edit(@Valid @RequestBody SystemContentSaveReqVO systemContent) {
        systemContent.setUpdatorId(getUserId());
        systemContent.setUpdateBy(getNickName());
        systemContent.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(systemContentService.updateSystemContent(systemContent));
    }

//    @Operation(summary = "删除系统配置")
//    @PreAuthorize("@ss.hasPermi('system:system:content:remove')")
//    @Log(title = "系统配置", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(systemContentService.removeSystemContent(Arrays.asList(ids)));
//    }

}
