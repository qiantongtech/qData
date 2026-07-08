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

package tech.qiantong.qdata.module.dp.controller.admin.codeMap;

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
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.codeMap.DpCodeMapConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;
import tech.qiantong.qdata.module.dp.service.codeMap.IDpCodeMapService;

/**
 * 数据元代码映射Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "数据元代码映射")
@RestController
@RequestMapping("/dp/codeMap")
@Validated
public class DpCodeMapController extends BaseController {
    @Resource
    private IDpCodeMapService dpCodeMapService;

    @Operation(summary = "查询数据元代码映射列表")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpCodeMapRespVO>> list(DpCodeMapPageReqVO dpCodeMap) {
        PageResult<DpCodeMapDO> page = dpCodeMapService.getDpCodeMapPage(dpCodeMap);
        return CommonResult.success(BeanUtils.toBean(page, DpCodeMapRespVO.class));
    }

    @Operation(summary = "导出数据元代码映射列表")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:export')")
    @Log(title = "log.op.title.dp.code.map", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpCodeMapPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpCodeMapDO> list = (List<DpCodeMapDO>) dpCodeMapService.getDpCodeMapPage(exportReqVO).getRows();
        ExcelUtil<DpCodeMapRespVO> util = new ExcelUtil<>(DpCodeMapRespVO.class);
        util.exportExcel(response, DpCodeMapConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据元代码映射列表")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:import')")
    @Log(title = "log.op.title.dp.code.map", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpCodeMapRespVO> util = new ExcelUtil<>(DpCodeMapRespVO.class);
        List<DpCodeMapRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpCodeMapService.importDpCodeMap(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据元代码映射详细信息")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpCodeMapRespVO> getInfo(@PathVariable("id") Long id) {
        DpCodeMapDO dpCodeMapDO = dpCodeMapService.getDpCodeMapById(id);
        return CommonResult.success(BeanUtils.toBean(dpCodeMapDO, DpCodeMapRespVO.class));
    }

    @Operation(summary = "新增数据元代码映射")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:add')")
    @Log(title = "log.op.title.dp.code.map", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpCodeMapSaveReqVO dpCodeMap) {
        dpCodeMap.setCreatorId(getUserId());
        dpCodeMap.setCreateBy(getNickName());
        dpCodeMap.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpCodeMapService.createDpCodeMap(dpCodeMap));
    }

    @Operation(summary = "修改数据元代码映射")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:edit')")
    @Log(title = "log.op.title.dp.code.map", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpCodeMapSaveReqVO dpCodeMap) {
        dpCodeMap.setUpdatorId(getUserId());
        dpCodeMap.setUpdateBy(getNickName());
        dpCodeMap.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpCodeMapService.updateDpCodeMap(dpCodeMap));
    }

    @Operation(summary = "删除数据元代码映射")
    @PreAuthorize("@ss.hasPermi('dp:codeMap:remove')")
    @Log(title = "log.op.title.dp.code.map", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpCodeMapService.removeDpCodeMap(Arrays.asList(ids)));
    }

}
