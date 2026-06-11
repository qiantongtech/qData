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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategory;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.*;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;
import tech.qiantong.qdata.module.dg.service.dataCategory.IDgDataCategoryService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据分类Controller
 *
 * @author qdata
 * @date 2026-04-07
 */
@Tag(name = "数据分类")
@RestController
@RequestMapping("/dg/dataCategory")
@Validated
public class DgDataCategoryController extends BaseController {
    @Resource
    private IDgDataCategoryService dgDataCategoryService;

    @Operation(summary = "查询数据分类树列表")
    @GetMapping("/selectTree")
    public CommonResult<List<DgDataCategoryTreeRespVO>> selectTree(@RequestParam(required = false) String type) {
        if (StringUtils.isBlank(type)) {
            type = "1";
        }
        return CommonResult.success(dgDataCategoryService.selectTree(type));
    }


    @Operation(summary = "查询数据分类列表")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDataCategoryRespVO>> list(DgDataCategoryPageReqVO dgDataCategory) {
        PageResult<DgDataCategoryDO> page = dgDataCategoryService.getDgDataCategoryPage(dgDataCategory);
        return CommonResult.success(BeanUtils.toBean(page, DgDataCategoryRespVO.class));
    }

    @Operation(summary = "查询数据分类列表")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:list')")
    @GetMapping("/listAll")
    public CommonResult<List<DgDataCategoryRespVO>> listAll(DgDataCategoryPageReqVO dgDataCategory) {
        List<DgDataCategoryDO> page = dgDataCategoryService.getDgDataCategoryList(dgDataCategory);
        return CommonResult.success(BeanUtils.toBean(page, DgDataCategoryRespVO.class));
    }


    @Operation(summary = "获取数据分类详细信息")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataCategoryDO dgDataCategoryDO = dgDataCategoryService.getDgDataCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryDO, DgDataCategoryRespVO.class));
    }

    @Operation(summary = "新增数据分类")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:add')")
    @Log(title = "数据分类", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataCategorySaveReqVO dgDataCategory) {
        dgDataCategory.setCreatorId(getUserId());
        dgDataCategory.setCreateBy(getNickName());
        dgDataCategory.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryService.createDgDataCategory(dgDataCategory));
    }

    @Operation(summary = "修改数据分类")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:edit')")
    @Log(title = "数据分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataCategorySaveReqVO dgDataCategory) {
        dgDataCategory.setUpdatorId(getUserId());
        dgDataCategory.setUpdateBy(getNickName());
        dgDataCategory.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryService.updateDgDataCategory(dgDataCategory));
    }

    @Operation(summary = "批量設置数据分級")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:edit')")
    @Log(title = "批量設置数据分級", businessType = BusinessType.UPDATE)
    @PutMapping("/batchDataLevel")
    public CommonResult<Boolean> batchDataLevel(@RequestBody DgDataCategoryBatchDataLevelReqVO reqVO) {
        return CommonResult.toAjax(dgDataCategoryService.update(Wrappers.lambdaUpdate(DgDataCategoryDO.class).set(DgDataCategoryDO::getDataLevelId, reqVO.getDataLevelId()).in(DgDataCategoryDO::getId, reqVO.getIds())));
    }

    @Operation(summary = "删除数据分类")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:remove')")
    @Log(title = "数据分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataCategoryService.removeDgDataCategory(Arrays.asList(ids)));
    }

}
