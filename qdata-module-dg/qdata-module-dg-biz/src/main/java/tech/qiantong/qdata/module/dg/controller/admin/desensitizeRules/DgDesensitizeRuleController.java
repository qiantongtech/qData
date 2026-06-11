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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.desensitizeRules.DgDesensitizeRuleConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeRuleService;

/**
 * 脱敏规则Controller
 *
 * @author qdata
 * @date 2026-04-10
 */
@Tag(name = "脱敏规则")
@RestController
@RequestMapping("/dg/desensitizeRules")
@Validated
public class DgDesensitizeRuleController extends BaseController {
    @Resource
    private IDgDesensitizeRuleService dgDesensitizeRuleService;

    @Operation(summary = "查询脱敏规则列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDesensitizeRuleRespVO>> list(DgDesensitizeRulePageReqVO dgDesensitizeRule) {
        PageResult<DgDesensitizeRuleDO> page = dgDesensitizeRuleService.getDgDesensitizeRulePage(dgDesensitizeRule);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeRuleRespVO.class));
    }

    @Operation(summary = "导出脱敏规则列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:export')")
    @Log(title = "脱敏规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDesensitizeRulePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDesensitizeRuleDO> list = (List<DgDesensitizeRuleDO>) dgDesensitizeRuleService.getDgDesensitizeRulePage(exportReqVO).getRows();
        ExcelUtil<DgDesensitizeRuleRespVO> util = new ExcelUtil<>(DgDesensitizeRuleRespVO.class);
        util.exportExcel(response, DgDesensitizeRuleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入脱敏规则列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:import')")
    @Log(title = "脱敏规则", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDesensitizeRuleRespVO> util = new ExcelUtil<>(DgDesensitizeRuleRespVO.class);
        List<DgDesensitizeRuleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDesensitizeRuleService.importDgDesensitizeRule(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取脱敏规则详细信息")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDesensitizeRuleRespVO> getInfo(@PathVariable("id") Long id) {
        DgDesensitizeRuleDO dgDesensitizeRuleDO = dgDesensitizeRuleService.getDgDesensitizeRuleById(id);
        return CommonResult.success(BeanUtils.toBean(dgDesensitizeRuleDO, DgDesensitizeRuleRespVO.class));
    }

    @Operation(summary = "新增脱敏规则")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:add')")
    @Log(title = "脱敏规则", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDesensitizeRuleSaveReqVO dgDesensitizeRule) {
        dgDesensitizeRule.setCreatorId(getUserId());
        dgDesensitizeRule.setCreateBy(getNickName());
        dgDesensitizeRule.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeRuleService.createDgDesensitizeRule(dgDesensitizeRule));
    }

    @Operation(summary = "修改脱敏规则")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:edit')")
    @Log(title = "脱敏规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDesensitizeRuleSaveReqVO dgDesensitizeRule) {
        dgDesensitizeRule.setUpdatorId(getUserId());
        dgDesensitizeRule.setUpdateBy(getNickName());
        dgDesensitizeRule.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeRuleService.updateDgDesensitizeRule(dgDesensitizeRule));
    }

    @Operation(summary = "删除脱敏规则")
    @PreAuthorize("@ss.hasPermi('dg:desensitizerules:remove')")
    @Log(title = "脱敏规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDesensitizeRuleService.removeDgDesensitizeRule(Arrays.asList(ids)));
    }

}
