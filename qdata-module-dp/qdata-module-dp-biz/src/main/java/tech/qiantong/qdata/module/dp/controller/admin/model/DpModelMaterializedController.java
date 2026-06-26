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

package tech.qiantong.qdata.module.dp.controller.admin.model;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpMaterializedMethodReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;
import tech.qiantong.qdata.module.dp.service.model.IDpModelMaterializedService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * 物化模型记录Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "物化模型记录")
@RestController
@RequestMapping("/dp/modelMaterialized")
@Validated
public class DpModelMaterializedController extends BaseController {
    @Resource
    private IDpModelMaterializedService dpModelMaterializedService;

    @Operation(summary = "查询物化模型记录列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpModelMaterializedRespVO>> list(DpModelMaterializedPageReqVO dpModelMaterialized) {
        PageResult<DpModelMaterializedDO> page = dpModelMaterializedService.getDpModelMaterializedPage(dpModelMaterialized);
        return CommonResult.success(BeanUtils.toBean(page, DpModelMaterializedRespVO.class));
    }

    @Operation(summary = "获取物化模型记录详细信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpModelMaterializedRespVO> getInfo(@PathVariable("id") Long id) {
        DpModelMaterializedDO dpModelMaterializedDO = dpModelMaterializedService.getDpModelMaterializedById(id);
        return CommonResult.success(BeanUtils.toBean(dpModelMaterializedDO, DpModelMaterializedRespVO.class));
    }

    @Operation(summary = "新增物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:add')")
    @Log(title = "物化模型记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpModelMaterializedSaveReqVO dpModelMaterialized) {
        dpModelMaterialized.setCreatorId(getUserId());
        dpModelMaterialized.setCreateBy(getNickName());
        dpModelMaterialized.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelMaterializedService.createDpModelMaterialized(dpModelMaterialized));
    }

    @Operation(summary = "新增物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:add')")
    @Log(title = "物化模型记录", businessType = BusinessType.INSERT)
    @PostMapping("/createMaterializedTable")
    public CommonResult<Long> createMaterializedTable(@Valid @RequestBody DpMaterializedMethodReqVO dpModelMaterialized) {
        dpModelMaterialized.setCreatorId(getUserId());
        dpModelMaterialized.setCreateBy(getNickName());
        dpModelMaterialized.setCreateTime(DateUtil.date());
        return CommonResult.success(dpModelMaterializedService.createMaterializedTable(dpModelMaterialized));
    }

    @Operation(summary = "修改物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:edit')")
    @Log(title = "物化模型记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpModelMaterializedSaveReqVO dpModelMaterialized) {
        dpModelMaterialized.setUpdatorId(getUserId());
        dpModelMaterialized.setUpdateBy(getNickName());
        dpModelMaterialized.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelMaterializedService.updateDpModelMaterialized(dpModelMaterialized));
    }

    @Operation(summary = "删除物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:remove')")
    @Log(title = "物化模型记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpModelMaterializedService.removeDpModelMaterialized(Arrays.asList(ids)));
    }

}
