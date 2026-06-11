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
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.convert.businessCategory.DmBusinessCategoryConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessCategoryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 业务分类Controller
 *
 * @author qdata
 * @date 2026-04-08
 */
@Tag(name = "业务分类")
@RestController
@RequestMapping("/dm/businessCategory")
@Validated
public class DmBusinessCategoryController extends BaseController {
    @Resource
    private IDmBusinessCategoryService dmBusinessCategoryService;

    @Operation(summary = "查询业务分类列表")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:list')")
    @GetMapping("/listPage")
    public CommonResult<PageResult<DmBusinessCategoryRespVO>> list(DmBusinessCategoryPageReqVO dmBusinessCategory) {
        PageResult<DmBusinessCategoryDO> page = dmBusinessCategoryService.getDmBusinessCategoryPage(dmBusinessCategory);
        return CommonResult.success(BeanUtils.toBean(page, DmBusinessCategoryRespVO.class));
    }

    @Operation(summary = "查询业务分类列表")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:list')")
    @GetMapping("/list")
    public CommonResult<List<DmBusinessCategoryRespVO>> listAll(DmBusinessCategoryPageReqVO dmBusinessCategory) {
        List<DmBusinessCategoryDO> page = dmBusinessCategoryService.getDmBusinessCategoryList(dmBusinessCategory);
        return CommonResult.success(BeanUtils.toBean(page, DmBusinessCategoryRespVO.class));
    }


    @Operation(summary = "导出业务分类列表")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:export')")
    @Log(title = "业务分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmBusinessCategoryPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmBusinessCategoryDO> list = (List<DmBusinessCategoryDO>) dmBusinessCategoryService.getDmBusinessCategoryPage(exportReqVO).getRows();
        ExcelUtil<DmBusinessCategoryRespVO> util = new ExcelUtil<>(DmBusinessCategoryRespVO.class);
        util.exportExcel(response, DmBusinessCategoryConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入业务分类列表")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:import')")
    @Log(title = "业务分类", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmBusinessCategoryRespVO> util = new ExcelUtil<>(DmBusinessCategoryRespVO.class);
        List<DmBusinessCategoryRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmBusinessCategoryService.importDmBusinessCategory(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取业务分类详细信息")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmBusinessCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        DmBusinessCategoryDO dmBusinessCategoryDO = dmBusinessCategoryService.getDmBusinessCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(dmBusinessCategoryDO, DmBusinessCategoryRespVO.class));
    }

    @Operation(summary = "新增业务分类")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:add')")
    @Log(title = "业务分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmBusinessCategorySaveReqVO dmBusinessCategory) {
        dmBusinessCategory.setCreatorId(getUserId());
        dmBusinessCategory.setCreateBy(getNickName());
        dmBusinessCategory.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessCategoryService.createDmBusinessCategory(dmBusinessCategory));
    }

    @Operation(summary = "修改业务分类")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:edit')")
    @Log(title = "业务分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmBusinessCategorySaveReqVO dmBusinessCategory) {
        dmBusinessCategory.setUpdatorId(getUserId());
        dmBusinessCategory.setUpdateBy(getNickName());
        dmBusinessCategory.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessCategoryService.updateDmBusinessCategory(dmBusinessCategory));
    }

    @Operation(summary = "删除业务分类")
    @PreAuthorize("@ss.hasPermi('dm:businesscategory:remove')")
    @Log(title = "业务分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmBusinessCategoryService.removeDmBusinessCategory(Arrays.asList(ids)));
    }

}
