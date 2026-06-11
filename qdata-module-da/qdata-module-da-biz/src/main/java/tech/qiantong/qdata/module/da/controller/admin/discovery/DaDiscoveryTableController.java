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
import cn.hutool.json.JSONObject;
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
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.convert.discovery.DaDiscoveryTableConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTableService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据发现库信息Controller
 *
 * @author qdata
 * @date 2025-02-11
 */
@Tag(name = "数据发现库信息")
@RestController
@RequestMapping("/da/discoveryTable")
@Validated
public class DaDiscoveryTableController extends BaseController {
    @Resource
    private IDaDiscoveryTableService daDiscoveryTableService;
    @Resource
    private IDaAssetService daAssetService;

    @Operation(summary = "查询数据发现库信息列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDiscoveryTableRespVO>> list(DaDiscoveryTablePageReqVO daDiscoveryTable) {
        PageResult<DaDiscoveryTableDO> page = daDiscoveryTableService.getDaDiscoveryTablePage(daDiscoveryTable);
        return CommonResult.success(BeanUtils.toBean(page, DaDiscoveryTableRespVO.class));
    }
    @Operation(summary = "查询数据发现库信息列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:list')")
    @GetMapping("/getDaDiscoveryTableList")
    public CommonResult<List<DaDiscoveryTableRespVO>> getDaDiscoveryTableList(DaDiscoveryTablePageReqVO daDiscoveryTable) {
        List<DaDiscoveryTableDO> page = daDiscoveryTableService.getDaDiscoveryTableList(daDiscoveryTable);
        return CommonResult.success(BeanUtils.toBean(page, DaDiscoveryTableRespVO.class));
    }

    @Operation(summary = "导出数据发现库信息列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:export')")
    @Log(title = "数据发现库信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDiscoveryTablePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDiscoveryTableDO> list = (List<DaDiscoveryTableDO>) daDiscoveryTableService.getDaDiscoveryTablePage(exportReqVO).getRows();
        ExcelUtil<DaDiscoveryTableRespVO> util = new ExcelUtil<>(DaDiscoveryTableRespVO.class);
        util.exportExcel(response, DaDiscoveryTableConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据发现库信息列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:import')")
    @Log(title = "数据发现库信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDiscoveryTableRespVO> util = new ExcelUtil<>(DaDiscoveryTableRespVO.class);
        List<DaDiscoveryTableRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDiscoveryTableService.importDaDiscoveryTable(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据发现库信息详细信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDiscoveryTableRespVO> getInfo(@PathVariable("id") Long id) {
        DaDiscoveryTableDO daDiscoveryTableDO = daDiscoveryTableService.getDaDiscoveryTableById(id);
        return CommonResult.success(BeanUtils.toBean(daDiscoveryTableDO, DaDiscoveryTableRespVO.class));
    }

    @Operation(summary = "新增数据发现库信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:add')")
    @Log(title = "数据发现库信息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDiscoveryTableSaveReqVO daDiscoveryTable) {
        daDiscoveryTable.setCreatorId(getUserId());
        daDiscoveryTable.setCreateBy(getNickName());
        daDiscoveryTable.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTableService.createDaDiscoveryTable(daDiscoveryTable));
    }

    @Operation(summary = "修改数据发现库信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:edit')")
    @Log(title = "数据发现库信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDiscoveryTableSaveReqVO daDiscoveryTable) {
        daDiscoveryTable.setUpdatorId(getUserId());
        daDiscoveryTable.setUpdateBy(getNickName());
        daDiscoveryTable.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTableService.updateDaDiscoveryTable(daDiscoveryTable));
    }

    @Operation(summary = "删除数据发现库信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:remove')")
    @Log(title = "数据发现库信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDiscoveryTableService.removeDaDiscoveryTable(Arrays.asList(ids)));
    }

    @Operation(summary = "获取数据发现的数据预览")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:query')")
    @PostMapping(value = "/preview")
    public AjaxResult getPreview(@RequestBody JSONObject jsonObject) {
        if (jsonObject.getStr("taskId") == null){
            return error("请携带数据发现任务id");
        }
        if (jsonObject.getStr("tableName") == null){
            return error("请携带数据库表");
        }
        Map<String,Object> columnData = daAssetService.getColumnData(jsonObject);
        return success(columnData);
    }

    @Operation(summary = "数据发现库信息进行提交撤回")
    @PreAuthorize("@ss.hasPermi('da:discoveryTable:edit')")
    @PostMapping(value = "/commitOrRevokeDiscoveryInfo")
    public CommonResult<Integer> commitOrRevokeDiscoveryInfo(@RequestBody DaDiscoveryTableSaveReqVO daDiscoveryTable) {
        return CommonResult.toAjax(daDiscoveryTableService.commitOrRevokeDiscoveryInfo(daDiscoveryTable));
    }
}
