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

package tech.qiantong.qdata.module.da.controller.admin.discovery;

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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.convert.discovery.DaDiscoveryColumnConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryColumnService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据发现字段Controller
 *
 * @author qdata
 * @date 2025-02-11
 */
@Tag(name = "数据发现字段")
@RestController
@RequestMapping("/da/discoveryColumn")
@Validated
public class DaDiscoveryColumnController extends BaseController {
    @Resource
    private IDaDiscoveryColumnService daDiscoveryColumnService;

    @Operation(summary = "查询数据发现字段列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDiscoveryColumnRespVO>> list(DaDiscoveryColumnPageReqVO daDiscoveryColumn) {
        PageResult<DaDiscoveryColumnDO> page = daDiscoveryColumnService.getDaDiscoveryColumnPage(daDiscoveryColumn);
        return CommonResult.success(BeanUtils.toBean(page, DaDiscoveryColumnRespVO.class));
    }

    @Operation(summary = "查询数据发现字段列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:list')")
    @GetMapping("/getDaDiscoveryColumnList")
    public CommonResult<List<DaDiscoveryColumnRespVO>> getDaDiscoveryColumnList(DaDiscoveryColumnPageReqVO daDiscoveryColumn) {
        List<DaDiscoveryColumnDO> daDiscoveryColumnList = daDiscoveryColumnService.getDaDiscoveryColumnList(daDiscoveryColumn);
        List<DaDiscoveryColumnRespVO> bean = BeanUtils.toBean(daDiscoveryColumnList, DaDiscoveryColumnRespVO.class);
        return CommonResult.success(bean);
    }

    @Operation(summary = "导出数据发现字段列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:export')")
    @Log(title = "数据发现字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDiscoveryColumnPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDiscoveryColumnDO> list = (List<DaDiscoveryColumnDO>) daDiscoveryColumnService.getDaDiscoveryColumnPage(exportReqVO).getRows();
        ExcelUtil<DaDiscoveryColumnRespVO> util = new ExcelUtil<>(DaDiscoveryColumnRespVO.class);
        util.exportExcel(response, DaDiscoveryColumnConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据发现字段列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:import')")
    @Log(title = "数据发现字段", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDiscoveryColumnRespVO> util = new ExcelUtil<>(DaDiscoveryColumnRespVO.class);
        List<DaDiscoveryColumnRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDiscoveryColumnService.importDaDiscoveryColumn(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据发现字段详细信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDiscoveryColumnRespVO> getInfo(@PathVariable("id") Long id) {
        DaDiscoveryColumnDO daDiscoveryColumnDO = daDiscoveryColumnService.getDaDiscoveryColumnById(id);
        return CommonResult.success(BeanUtils.toBean(daDiscoveryColumnDO, DaDiscoveryColumnRespVO.class));
    }

    @Operation(summary = "新增数据发现字段")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:add')")
    @Log(title = "数据发现字段", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDiscoveryColumnSaveReqVO daDiscoveryColumn) {
        daDiscoveryColumn.setCreatorId(getUserId());
        daDiscoveryColumn.setCreateBy(getNickName());
        daDiscoveryColumn.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryColumnService.createDaDiscoveryColumn(daDiscoveryColumn));
    }

    @Operation(summary = "修改数据发现字段")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:edit')")
    @Log(title = "数据发现字段", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDiscoveryColumnSaveReqVO daDiscoveryColumn) {
        daDiscoveryColumn.setUpdatorId(getUserId());
        daDiscoveryColumn.setUpdateBy(getNickName());
        daDiscoveryColumn.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryColumnService.updateDaDiscoveryColumn(daDiscoveryColumn));
    }

    @Operation(summary = "删除数据发现字段")
    @PreAuthorize("@ss.hasPermi('da:discoveryColumn:remove')")
    @Log(title = "数据发现字段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDiscoveryColumnService.removeDaDiscoveryColumn(Arrays.asList(ids)));
    }

}
