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

package tech.qiantong.qdata.module.system.controller.admin.updater;

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
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackPageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackRespVO;
import tech.qiantong.qdata.module.system.controller.admin.updater.vo.SystemVersionTrackSaveReqVO;
import tech.qiantong.qdata.module.system.convert.updater.SystemVersionTrackConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.updater.SystemVersionTrackDO;
import tech.qiantong.qdata.module.system.service.updater.ISystemVersionTrackService;

/**
 * system.updater.swagger.tagController
 *
 * @author qdata
 * @date 2026-08-12
 */
@Tag(name = "system.updater.swagger.tag")
@RestController
@RequestMapping("/system/VersionTrack")
@Validated
public class SystemVersionTrackController extends BaseController {
    @Resource
    private ISystemVersionTrackService systemVersionTrackService;

    @Operation(summary = "system.updater.swagger.get.list")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<SystemVersionTrackRespVO>> list(SystemVersionTrackPageReqVO systemVersionTrack) {
        PageResult<SystemVersionTrackDO> page = systemVersionTrackService.getSystemVersionTrackPage(systemVersionTrack);
        return CommonResult.success(BeanUtils.toBean(page, SystemVersionTrackRespVO.class));
    }

    @Operation(summary = "system.updater.swagger.post.export")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:export')")
    @Log(title = "system.updater.swagger.tag", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SystemVersionTrackPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SystemVersionTrackDO> list = (List<SystemVersionTrackDO>) systemVersionTrackService.getSystemVersionTrackPage(exportReqVO).getRows();
        ExcelUtil<SystemVersionTrackRespVO> util = new ExcelUtil<>(SystemVersionTrackRespVO.class);
        util.exportExcel(response, SystemVersionTrackConvert.INSTANCE.convertToRespVOList(list), "system.updater.swagger.tag");
    }

    @Operation(summary = "system.updater.swagger.post.import")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:import')")
    @Log(title = "system.updater.swagger.tag", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SystemVersionTrackRespVO> util = new ExcelUtil<>(SystemVersionTrackRespVO.class);
        List<SystemVersionTrackRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = systemVersionTrackService.importSystemVersionTrack(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "system.updater.swagger.get.detail")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<SystemVersionTrackRespVO> getInfo(@PathVariable("id") Long id) {
        SystemVersionTrackDO systemVersionTrackDO = systemVersionTrackService.getSystemVersionTrackById(id);
        return CommonResult.success(BeanUtils.toBean(systemVersionTrackDO, SystemVersionTrackRespVO.class));
    }

    @Operation(summary = "system.updater.swagger.post.create")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:add')")
    @Log(title = "system.updater.swagger.tag", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody SystemVersionTrackSaveReqVO systemVersionTrack) {
        systemVersionTrack.setCreatorId(getUserId());
        systemVersionTrack.setCreateBy(getNickName());
        systemVersionTrack.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(systemVersionTrackService.createSystemVersionTrack(systemVersionTrack));
    }

    @Operation(summary = "system.updater.swagger.put.update")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:edit')")
    @Log(title = "system.updater.swagger.tag", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody SystemVersionTrackSaveReqVO systemVersionTrack) {
        systemVersionTrack.setUpdatorId(getUserId());
        systemVersionTrack.setUpdateBy(getNickName());
        systemVersionTrack.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(systemVersionTrackService.updateSystemVersionTrack(systemVersionTrack));
    }

    @Operation(summary = "system.updater.swagger.delete.remove")
    @PreAuthorize("@ss.hasPermi('system:updater:versiontrack:remove')")
    @Log(title = "system.updater.swagger.tag", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(systemVersionTrackService.removeSystemVersionTrack(Arrays.asList(ids)));
    }

}
