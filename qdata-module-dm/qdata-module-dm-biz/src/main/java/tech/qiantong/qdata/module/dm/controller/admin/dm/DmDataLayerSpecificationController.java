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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 数仓分层-规范管理Controller
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
    @Log(title = "数仓分层-规范管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDataLayerSpecificationSaveReqVO dmDataLayerSpecification) {
        dmDataLayerSpecification.setCreatorId(getUserId());
        dmDataLayerSpecification.setCreateBy(getNickName());
        dmDataLayerSpecification.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerSpecificationService.createDmDataLayerSpecification(dmDataLayerSpecification));
    }

    @Operation(summary = "修改数仓分层-规范管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:edit')")
    @Log(title = "数仓分层-规范管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDataLayerSpecificationSaveReqVO dmDataLayerSpecification) {
        dmDataLayerSpecification.setUpdatorId(getUserId());
        dmDataLayerSpecification.setUpdateBy(getNickName());
        dmDataLayerSpecification.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerSpecificationService.updateDmDataLayerSpecification(dmDataLayerSpecification));
    }

    @Operation(summary = "删除数仓分层-规范管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:remove')")
    @Log(title = "数仓分层-规范管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmDataLayerSpecificationService.removeDmDataLayerSpecification(Arrays.asList(ids)));
    }

}
