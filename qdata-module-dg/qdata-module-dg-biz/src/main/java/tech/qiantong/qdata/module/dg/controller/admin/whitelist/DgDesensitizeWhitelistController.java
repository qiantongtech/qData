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

package tech.qiantong.qdata.module.dg.controller.admin.whitelist;

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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.whitelist.DgDesensitizeWhitelistConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeWhitelistService;

/**
 * 脱敏白名单Controller
 *
 * @author qdata
 * @date 2026-04-09
 */
@Tag(name = "脱敏白名单")
@RestController
@RequestMapping("/dg/desensitizeWhitelist")
@Validated
public class DgDesensitizeWhitelistController extends BaseController {
    @Resource
    private IDgDesensitizeWhitelistService dgDesensitizeWhitelistService;

    @Operation(summary = "查询脱敏白名单列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDesensitizeWhitelistRespVO>> list(DgDesensitizeWhitelistPageReqVO dgDesensitizeWhitelist) {
        PageResult<DgDesensitizeWhitelistDO> page = dgDesensitizeWhitelistService.getDgDesensitizeWhitelistPage(dgDesensitizeWhitelist);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeWhitelistRespVO.class));
    }

    @Operation(summary = "导出脱敏白名单列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:export')")
    @Log(title = "log.op.title.dg.desensitize.whitelist", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDesensitizeWhitelistPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDesensitizeWhitelistDO> list = (List<DgDesensitizeWhitelistDO>) dgDesensitizeWhitelistService.getDgDesensitizeWhitelistPage(exportReqVO).getRows();
        ExcelUtil<DgDesensitizeWhitelistRespVO> util = new ExcelUtil<>(DgDesensitizeWhitelistRespVO.class);
        util.exportExcel(response, DgDesensitizeWhitelistConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入脱敏白名单列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:import')")
    @Log(title = "log.op.title.dg.desensitize.whitelist", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDesensitizeWhitelistRespVO> util = new ExcelUtil<>(DgDesensitizeWhitelistRespVO.class);
        List<DgDesensitizeWhitelistRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDesensitizeWhitelistService.importDgDesensitizeWhitelist(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取脱敏白名单详细信息")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDesensitizeWhitelistRespVO> getInfo(@PathVariable("id") Long id) {
        DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = dgDesensitizeWhitelistService.getDgDesensitizeWhitelistById(id);
        return CommonResult.success(BeanUtils.toBean(dgDesensitizeWhitelistDO, DgDesensitizeWhitelistRespVO.class));
    }

    @Operation(summary = "新增脱敏白名单")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:add')")
    @Log(title = "log.op.title.dg.desensitize.whitelist", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDesensitizeWhitelistSaveReqVO dgDesensitizeWhitelist) {
        dgDesensitizeWhitelist.setCreatorId(getUserId());
        dgDesensitizeWhitelist.setCreateBy(getNickName());
        dgDesensitizeWhitelist.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeWhitelistService.createDgDesensitizeWhitelist(dgDesensitizeWhitelist));
    }

    @Operation(summary = "修改脱敏白名单")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:edit')")
    @Log(title = "log.op.title.dg.desensitize.whitelist", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDesensitizeWhitelistSaveReqVO dgDesensitizeWhitelist) {
        dgDesensitizeWhitelist.setUpdatorId(getUserId());
        dgDesensitizeWhitelist.setUpdateBy(getNickName());
        dgDesensitizeWhitelist.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeWhitelistService.updateDgDesensitizeWhitelist(dgDesensitizeWhitelist));
    }

    @Operation(summary = "删除脱敏白名单")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:remove')")
    @Log(title = "log.op.title.dg.desensitize.whitelist", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDesensitizeWhitelistService.removeDgDesensitizeWhitelist(Arrays.asList(ids)));
    }

}
