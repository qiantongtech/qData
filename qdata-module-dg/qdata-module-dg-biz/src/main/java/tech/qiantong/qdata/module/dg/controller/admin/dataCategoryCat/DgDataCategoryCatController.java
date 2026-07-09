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

package tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat;

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
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.dataCategoryCat.DgDataCategoryCatConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import tech.qiantong.qdata.module.dg.service.dataCategoryCat.IDgDataCategoryCatService;

/**
 * Data Category - Category Controller
 *
 * @author FXB
 * @date 2026-04-07
 */
@Tag(name = "数据分类-类目")
@RestController
@RequestMapping("/dg/dataCategoryCat")
@Validated
public class DgDataCategoryCatController extends BaseController {
    @Resource
    private IDgDataCategoryCatService dgDataCategoryCatService;

    @Operation(summary = "查询数据分类-类目列表")
    @GetMapping("/list")
    public CommonResult<List<DgDataCategoryCatRespVO>> list() {
        List<DgDataCategoryCatDO> dgDataCategoryCatDOList = dgDataCategoryCatService.getDgDataCategoryCatList();
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryCatDOList, DgDataCategoryCatRespVO.class));
    }

    @Operation(summary = "获取数据分类-类目详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataCategoryCatRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataCategoryCatDO dgDataCategoryCatDO = dgDataCategoryCatService.getDgDataCategoryCatById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryCatDO, DgDataCategoryCatRespVO.class));
    }

    @Operation(summary = "新增数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:add')")
    @Log(title = "log.op.title.dg.data.category.cat", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataCategoryCatSaveReqVO dgDataCategoryCat) {
        dgDataCategoryCat.setCreatorId(getUserId());
        dgDataCategoryCat.setCreateBy(getNickName());
        dgDataCategoryCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryCatService.createDgDataCategoryCat(dgDataCategoryCat));
    }

    @Operation(summary = "修改数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:edit')")
    @Log(title = "log.op.title.dg.data.category.cat", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataCategoryCatSaveReqVO dgDataCategoryCat) {
        dgDataCategoryCat.setUpdatorId(getUserId());
        dgDataCategoryCat.setUpdateBy(getNickName());
        dgDataCategoryCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryCatService.updateDgDataCategoryCat(dgDataCategoryCat));
    }

    @Operation(summary = "删除数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:remove')")
    @Log(title = "log.op.title.dg.data.category.cat", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataCategoryCatService.removeDgDataCategoryCat(Arrays.asList(ids)));
    }

}
