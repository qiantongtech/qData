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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.files;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.files.DaAssetFilesConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;
import tech.qiantong.qdata.module.da.service.assetchild.files.IDaAssetFilesService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据资产-文件服务Controller
 *
 * @author qdata
 * @date 2025-06-26
 */
@Tag(name = "数据资产-文件服务")
@RestController
@RequestMapping("/da/assetFiles")
@Validated
public class DaAssetFilesController extends BaseController {
    @Resource
    private IDaAssetFilesService daAssetFilesService;

    @Operation(summary = "查询数据资产-文件服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetFilesRespVO>> list(DaAssetFilesPageReqVO daAssetFiles) {
        PageResult<DaAssetFilesDO> page = daAssetFilesService.getDaAssetFilesPage(daAssetFiles);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetFilesRespVO.class));
    }

    @Operation(summary = "导出数据资产-文件服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:export')")
    @Log(title = "数据资产-文件服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetFilesPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetFilesDO> list = (List<DaAssetFilesDO>) daAssetFilesService.getDaAssetFilesPage(exportReqVO).getRows();
        ExcelUtil<DaAssetFilesRespVO> util = new ExcelUtil<>(DaAssetFilesRespVO.class);
        util.exportExcel(response, DaAssetFilesConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-文件服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:import')")
    @Log(title = "数据资产-文件服务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetFilesRespVO> util = new ExcelUtil<>(DaAssetFilesRespVO.class);
        List<DaAssetFilesRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetFilesService.importDaAssetFiles(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-文件服务详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetFilesRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetFilesDO daAssetFilesDO = daAssetFilesService.getDaAssetFilesById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetFilesDO, DaAssetFilesRespVO.class));
    }

    @Operation(summary = "新增数据资产-文件服务")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:add')")
    @Log(title = "数据资产-文件服务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetFilesSaveReqVO daAssetFiles) {
        daAssetFiles.setCreatorId(getUserId());
        daAssetFiles.setCreateBy(getNickName());
        daAssetFiles.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetFilesService.createDaAssetFiles(daAssetFiles));
    }

    @Operation(summary = "修改数据资产-文件服务")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:edit')")
    @Log(title = "数据资产-文件服务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetFilesSaveReqVO daAssetFiles) {
        daAssetFiles.setUpdatorId(getUserId());
        daAssetFiles.setUpdateBy(getNickName());
        daAssetFiles.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetFilesService.updateDaAssetFiles(daAssetFiles));
    }

    @Operation(summary = "删除数据资产-文件服务")
    @PreAuthorize("@ss.hasPermi('da:assetFiles:remove')")
    @Log(title = "数据资产-文件服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetFilesService.removeDaAssetFiles(Arrays.asList(ids)));
    }

}
