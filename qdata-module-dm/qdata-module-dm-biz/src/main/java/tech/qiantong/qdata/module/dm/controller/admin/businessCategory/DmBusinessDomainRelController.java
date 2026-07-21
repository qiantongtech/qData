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

package tech.qiantong.qdata.module.dm.controller.admin.businessCategory;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.convert.businessCategory.DmBusinessDomainRelConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessDomainRelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Business Category Domain Relation Controller
 *
 * @author qdata
 * @date 2026-04-12
 */
@Tag(name = "业务分类数据域关联关系")
@RestController
@RequestMapping("/dm/BusinessDomainRel")
@Validated
public class DmBusinessDomainRelController extends BaseController {
    @Resource
    private IDmBusinessDomainRelService dmBusinessDomainRelService;

    @Operation(summary = "查询业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmBusinessDomainRelRespVO>> list(DmBusinessDomainRelPageReqVO dmBusinessDomainRel) {
        PageResult<DmBusinessDomainRelDO> page = dmBusinessDomainRelService.getDmBusinessDomainRelPage(dmBusinessDomainRel);
        return CommonResult.success(BeanUtils.toBean(page, DmBusinessDomainRelRespVO.class));
    }

    @Operation(summary = "导出业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:export')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmBusinessDomainRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmBusinessDomainRelDO> list = (List<DmBusinessDomainRelDO>) dmBusinessDomainRelService.getDmBusinessDomainRelPage(exportReqVO).getRows();
        ExcelUtil<DmBusinessDomainRelRespVO> util = new ExcelUtil<>(DmBusinessDomainRelRespVO.class);
        util.exportExcel(response, DmBusinessDomainRelConvert.INSTANCE.convertToRespVOList(list), "Application Management Data");
    }

    @Operation(summary = "导入业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:import')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmBusinessDomainRelRespVO> util = new ExcelUtil<>(DmBusinessDomainRelRespVO.class);
        List<DmBusinessDomainRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmBusinessDomainRelService.importDmBusinessDomainRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取业务分类数据域关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmBusinessDomainRelRespVO> getInfo(@PathVariable("id") Long id) {
        DmBusinessDomainRelDO dmBusinessDomainRelDO = dmBusinessDomainRelService.getDmBusinessDomainRelById(id);
        return CommonResult.success(BeanUtils.toBean(dmBusinessDomainRelDO, DmBusinessDomainRelRespVO.class));
    }

    @Operation(summary = "新增业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:add')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmBusinessDomainRelSaveReqVO dmBusinessDomainRel) {
        dmBusinessDomainRel.setCreatorId(getUserId());
        dmBusinessDomainRel.setCreateBy(getNickName());
        dmBusinessDomainRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessDomainRelService.createDmBusinessDomainRel(dmBusinessDomainRel));
    }

    @Operation(summary = "修改业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:edit')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmBusinessDomainRelSaveReqVO dmBusinessDomainRel) {
        dmBusinessDomainRel.setUpdatorId(getUserId());
        dmBusinessDomainRel.setUpdateBy(getNickName());
        dmBusinessDomainRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessDomainRelService.updateDmBusinessDomainRel(dmBusinessDomainRel));
    }

    @Operation(summary = "删除业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:remove')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmBusinessDomainRelService.removeDmBusinessDomainRel(Arrays.asList(ids)));
    }

    @Operation(summary = "删除业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:remove')")
    @Log(title = "log.op.title.dm.business.domain.rel", businessType = BusinessType.DELETE)
    @DeleteMapping("/deletebyDomainId/{domainId}/{businessCategoryId}")
    public CommonResult<Integer> deletebyDomainId(@PathVariable Long domainId, @PathVariable Long businessCategoryId) {
        return CommonResult.toAjax(dmBusinessDomainRelService.removeDmBusinessDomainRelByDomainId(domainId, businessCategoryId));
    }

}
