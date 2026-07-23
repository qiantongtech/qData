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
 * Data Category Controller
 *
 * @author qdata
 * @date 2026-04-07
 */
@Tag(name = "Data Category")
@RestController
@RequestMapping("/dg/dataCategory")
@Validated
public class DgDataCategoryController extends BaseController {
    @Resource
    private IDgDataCategoryService dgDataCategoryService;

    @Operation(summary = "Query data category tree list")
    @GetMapping("/selectTree")
    public CommonResult<List<DgDataCategoryTreeRespVO>> selectTree(@RequestParam(required = false) String type) {
        if (StringUtils.isBlank(type)) {
            type = "1";
        }
        return CommonResult.success(dgDataCategoryService.selectTree(type));
    }


    // Query the data category list with pagination
    @Operation(summary = "Query data category list")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDataCategoryRespVO>> list(DgDataCategoryPageReqVO dgDataCategory) {
        PageResult<DgDataCategoryDO> page = dgDataCategoryService.getDgDataCategoryPage(dgDataCategory);
        return CommonResult.success(BeanUtils.toBean(page, DgDataCategoryRespVO.class));
    }

    @Operation(summary = "Query data category list")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:list')")
    @GetMapping("/listAll")
    public CommonResult<List<DgDataCategoryRespVO>> listAll(DgDataCategoryPageReqVO dgDataCategory) {
        List<DgDataCategoryDO> page = dgDataCategoryService.getDgDataCategoryList(dgDataCategory);
        return CommonResult.success(BeanUtils.toBean(page, DgDataCategoryRespVO.class));
    }


    @Operation(summary = "Get data category detail info")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataCategoryRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataCategoryDO dgDataCategoryDO = dgDataCategoryService.getDgDataCategoryById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryDO, DgDataCategoryRespVO.class));
    }

    @Operation(summary = "Create data category")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:add')")
    @Log(title = "log.op.title.dg.data.category", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataCategorySaveReqVO dgDataCategory) {
        dgDataCategory.setCreatorId(getUserId());
        dgDataCategory.setCreateBy(getNickName());
        dgDataCategory.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryService.createDgDataCategory(dgDataCategory));
    }

    @Operation(summary = "Update data category")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:edit')")
    @Log(title = "log.op.title.dg.data.category", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataCategorySaveReqVO dgDataCategory) {
        dgDataCategory.setUpdatorId(getUserId());
        dgDataCategory.setUpdateBy(getNickName());
        dgDataCategory.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryService.updateDgDataCategory(dgDataCategory));
    }

    @Operation(summary = "Batch set data level")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:edit')")
    @Log(title = "log.op.title.dg.data.category.batch.set", businessType = BusinessType.UPDATE)
    @PutMapping("/batchDataLevel")
    public CommonResult<Boolean> batchDataLevel(@RequestBody DgDataCategoryBatchDataLevelReqVO reqVO) {
        return CommonResult.toAjax(dgDataCategoryService.update(Wrappers.lambdaUpdate(DgDataCategoryDO.class).set(DgDataCategoryDO::getDataLevelId, reqVO.getDataLevelId()).in(DgDataCategoryDO::getId, reqVO.getIds())));
    }

    @Operation(summary = "Delete data category")
    //@PreAuthorize("@ss.hasPermi('dg:dataCategory:remove')")
    @Log(title = "log.op.title.dg.data.category", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataCategoryService.removeDgDataCategory(Arrays.asList(ids)));
    }

}
