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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules;

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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.desensitizeRules.DgDesensitizeIntervalConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeIntervalService;

/**
 * 脱敏区间Controller
 *
 * @author qdata
 * @date 2026-04-10
 */
@Tag(name = "脱敏区间")
@RestController
@RequestMapping("/dg/desensitizeInterval")
@Validated
public class DgDesensitizeIntervalController extends BaseController {
    @Resource
    private IDgDesensitizeIntervalService dgDesensitizeIntervalService;

    @Operation(summary = "查询脱敏区间列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDesensitizeIntervalRespVO>> list(DgDesensitizeIntervalPageReqVO dgDesensitizeInterval) {
        PageResult<DgDesensitizeIntervalDO> page = dgDesensitizeIntervalService.getDgDesensitizeIntervalPage(dgDesensitizeInterval);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeIntervalRespVO.class));
    }

    @Operation(summary = "导出脱敏区间列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:export')")
    @Log(title = "脱敏区间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDesensitizeIntervalPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDesensitizeIntervalDO> list = (List<DgDesensitizeIntervalDO>) dgDesensitizeIntervalService.getDgDesensitizeIntervalPage(exportReqVO).getRows();
        ExcelUtil<DgDesensitizeIntervalRespVO> util = new ExcelUtil<>(DgDesensitizeIntervalRespVO.class);
        util.exportExcel(response, DgDesensitizeIntervalConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入脱敏区间列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:import')")
    @Log(title = "脱敏区间", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDesensitizeIntervalRespVO> util = new ExcelUtil<>(DgDesensitizeIntervalRespVO.class);
        List<DgDesensitizeIntervalRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDesensitizeIntervalService.importDgDesensitizeInterval(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取脱敏区间详细信息")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDesensitizeIntervalRespVO> getInfo(@PathVariable("id") Long id) {
        DgDesensitizeIntervalDO dgDesensitizeIntervalDO = dgDesensitizeIntervalService.getDgDesensitizeIntervalById(id);
        return CommonResult.success(BeanUtils.toBean(dgDesensitizeIntervalDO, DgDesensitizeIntervalRespVO.class));
    }

    @Operation(summary = "新增脱敏区间")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:add')")
    @Log(title = "脱敏区间", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDesensitizeIntervalSaveReqVO dgDesensitizeInterval) {
        dgDesensitizeInterval.setCreatorId(getUserId());
        dgDesensitizeInterval.setCreateBy(getNickName());
        dgDesensitizeInterval.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeIntervalService.createDgDesensitizeInterval(dgDesensitizeInterval));
    }

    @Operation(summary = "修改脱敏区间")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:edit')")
    @Log(title = "脱敏区间", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDesensitizeIntervalSaveReqVO dgDesensitizeInterval) {
        dgDesensitizeInterval.setUpdatorId(getUserId());
        dgDesensitizeInterval.setUpdateBy(getNickName());
        dgDesensitizeInterval.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeIntervalService.updateDgDesensitizeInterval(dgDesensitizeInterval));
    }

    @Operation(summary = "删除脱敏区间")
    @PreAuthorize("@ss.hasPermi('dg:desensitizeinterval:remove')")
    @Log(title = "脱敏区间", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDesensitizeIntervalService.removeDgDesensitizeInterval(Arrays.asList(ids)));
    }

}
