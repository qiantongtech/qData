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

package tech.qiantong.qdata.module.dm.controller.admin.dm;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

import cn.hutool.core.date.DateUtil;

import java.util.List;

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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataLayerSpecificationService;

/**
 * Data Warehouse Layer Specification Controller
 *
 * @author FXB
 * @date 2026-03-24
 */
@Tag(name = "数仓分层-规范管理")
@RestController
@RequestMapping("/dm/dataLayerSpecification")
@Validated
public class DmDataLayerSpecificationController extends BaseController {
    @Resource
    private IDmDataLayerSpecificationService dmDataLayerSpecificationService;

    @Operation(summary = "查询数仓分层-规范管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmDataLayerSpecificationRespVO>> list(DmDataLayerSpecificationPageReqVO dmDataLayerSpecification) {
        PageResult<DmDataLayerSpecificationDO> page = dmDataLayerSpecificationService.getDmDataLayerSpecificationPage(dmDataLayerSpecification);
        return CommonResult.success(BeanUtils.toBean(page, DmDataLayerSpecificationRespVO.class));
    }

    @Operation(summary = "获取数仓分层-规范管理详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmDataLayerSpecificationRespVO> getInfo(@PathVariable("id") Long id) {
        DmDataLayerSpecificationDO dmDataLayerSpecificationDO = dmDataLayerSpecificationService.getDmDataLayerSpecificationById(id);
        return CommonResult.success(BeanUtils.toBean(dmDataLayerSpecificationDO, DmDataLayerSpecificationRespVO.class));
    }

    @Operation(summary = "新增数仓分层-规范管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:add')")
    @Log(title = "log.op.title.dm.data.layer.spec", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDataLayerSpecificationSaveReqVO dmDataLayerSpecification) {
        dmDataLayerSpecification.setCreatorId(getUserId());
        dmDataLayerSpecification.setCreateBy(getNickName());
        dmDataLayerSpecification.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerSpecificationService.createDmDataLayerSpecification(dmDataLayerSpecification));
    }

    @Operation(summary = "修改数仓分层-规范管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:edit')")
    @Log(title = "log.op.title.dm.data.layer.spec", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDataLayerSpecificationSaveReqVO dmDataLayerSpecification) {
        dmDataLayerSpecification.setUpdatorId(getUserId());
        dmDataLayerSpecification.setUpdateBy(getNickName());
        dmDataLayerSpecification.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerSpecificationService.updateDmDataLayerSpecification(dmDataLayerSpecification));
    }

    @Operation(summary = "删除数仓分层-规范管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:remove')")
    @Log(title = "log.op.title.dm.data.layer.spec", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmDataLayerSpecificationService.removeDmDataLayerSpecification(Arrays.asList(ids)));
    }

}
