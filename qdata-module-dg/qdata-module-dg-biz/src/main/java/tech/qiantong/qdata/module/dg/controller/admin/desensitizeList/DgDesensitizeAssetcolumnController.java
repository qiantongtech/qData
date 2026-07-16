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

package tech.qiantong.qdata.module.dg.controller.admin.desensitizeList;

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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.desensitizeList.DgDesensitizeAssetcolumnConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
import tech.qiantong.qdata.module.dg.service.desensitizeList.IDgDesensitizeAssetcolumnService;

/**
 * Desensitize List Relationship Controller
 *
 * @author qdata
 * @date 2026-04-12
 */
@Tag(name = "Desensitize List Association")
@RestController
@RequestMapping("/dg/DgDesensitizeList")
@Validated
public class DgDesensitizeAssetcolumnController extends BaseController {
    @Resource
    private IDgDesensitizeAssetcolumnService dgDesensitizeAssetcolumnService;

    @Operation(summary = "Query desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDesensitizeAssetcolumnRespVO>> list(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn) {
        PageResult<DgDesensitizeAssetcolumnDO> page = dgDesensitizeAssetcolumnService.getDgDesensitizeAssetcolumnPage(dgDesensitizeAssetcolumn);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeAssetcolumnRespVO.class));
    }

    @Operation(summary = "Query desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:list')")
    @GetMapping("/listByRuleId")
    public CommonResult<PageResult<DgDesensitizeAssetcolumnRespVO>> listByRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn) {
        PageResult<DgDesensitizeAssetcolumnDO> page = dgDesensitizeAssetcolumnService.getDgDesensitizePagebyRuleId(dgDesensitizeAssetcolumn);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeAssetcolumnRespVO.class));
    }



    @Operation(summary = "Export desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:export')")
    @Log(title = "log.op.title.dg.desensitize.asset.column", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDesensitizeAssetcolumnPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDesensitizeAssetcolumnDO> list = (List<DgDesensitizeAssetcolumnDO>) dgDesensitizeAssetcolumnService.getDgDesensitizeAssetcolumnPage(exportReqVO).getRows();
        ExcelUtil<DgDesensitizeAssetcolumnRespVO> util = new ExcelUtil<>(DgDesensitizeAssetcolumnRespVO.class);
        util.exportExcel(response, DgDesensitizeAssetcolumnConvert.INSTANCE.convertToRespVOList(list), "Desensitize List Data");
    }

    @Operation(summary = "Import desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:import')")
    @Log(title = "log.op.title.dg.desensitize.asset.column", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDesensitizeAssetcolumnRespVO> util = new ExcelUtil<>(DgDesensitizeAssetcolumnRespVO.class);
        List<DgDesensitizeAssetcolumnRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDesensitizeAssetcolumnService.importDgDesensitizeAssetcolumn(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "Get desensitize list association detail")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDesensitizeAssetcolumnRespVO> getInfo(@PathVariable("id") Long id) {
        DgDesensitizeAssetcolumnDO dgDesensitizeAssetcolumnDO = dgDesensitizeAssetcolumnService.getDgDesensitizeAssetcolumnById(id);
        return CommonResult.success(BeanUtils.toBean(dgDesensitizeAssetcolumnDO, DgDesensitizeAssetcolumnRespVO.class));
    }

    @Operation(summary = "Create desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:add')")
    @Log(title = "log.op.title.dg.desensitize.asset.column", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDesensitizeAssetcolumnSaveReqVO dgDesensitizeAssetcolumn) {
        dgDesensitizeAssetcolumn.setCreatorId(getUserId());
        dgDesensitizeAssetcolumn.setCreateBy(getNickName());
        dgDesensitizeAssetcolumn.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeAssetcolumnService.createDgDesensitizeAssetcolumn(dgDesensitizeAssetcolumn));
    }

    @Operation(summary = "Update desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:edit')")
    @Log(title = "log.op.title.dg.desensitize.asset.column", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDesensitizeAssetcolumnSaveReqVO dgDesensitizeAssetcolumn) {
        dgDesensitizeAssetcolumn.setUpdatorId(getUserId());
        dgDesensitizeAssetcolumn.setUpdateBy(getNickName());
        dgDesensitizeAssetcolumn.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeAssetcolumnService.updateDgDesensitizeAssetcolumn(dgDesensitizeAssetcolumn));
    }

    @Operation(summary = "Delete desensitize list association")
    @PreAuthorize("@ss.hasPermi('dg:dgdesensitizelist:remove')")
    @Log(title = "log.op.title.dg.desensitize.asset.column", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDesensitizeAssetcolumnService.removeDgDesensitizeAssetcolumn(Arrays.asList(ids)));
    }




}
