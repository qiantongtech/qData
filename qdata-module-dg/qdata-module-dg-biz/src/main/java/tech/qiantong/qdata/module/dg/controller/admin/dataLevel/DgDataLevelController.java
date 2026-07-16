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

package tech.qiantong.qdata.module.dg.controller.admin.dataLevel;

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
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.dataLevel.DgDataLevelConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
import tech.qiantong.qdata.module.dg.service.dataLevel.IDgDataLevelService;

/**
 * Data Level Controller
 *
 * @author qdata
 * @date 2026-04-03
 */
@Tag(name = "数据分级")
@RestController
@RequestMapping("/dg/dataLevel")
@Validated
public class DgDataLevelController extends BaseController {
    @Resource
    private IDgDataLevelService dgDataLevelService;

    @Operation(summary = "查询数据分级列表")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDataLevelRespVO>> list(DgDataLevelPageReqVO dgDataLevel) {
        PageResult<DgDataLevelDO> page = dgDataLevelService.getDgDataLevelPage(dgDataLevel);
        return CommonResult.success(BeanUtils.toBean(page, DgDataLevelRespVO.class));
    }

    @Operation(summary = "查询数据分级列表")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:list')")
    @GetMapping("/listAll")
    public CommonResult<List<DgDataLevelRespVO>> listAll(DgDataLevelPageReqVO dgDataLevel) {
        List<DgDataLevelDO> list = dgDataLevelService.getDgDataLevelListAll(dgDataLevel);
        return CommonResult.success(BeanUtils.toBean(list, DgDataLevelRespVO.class));
    }

    @Operation(summary = "导出数据分级列表")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:export')")
    @Log(title = "log.op.title.dg.data.level", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDataLevelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDataLevelDO> list = (List<DgDataLevelDO>) dgDataLevelService.getDgDataLevelPage(exportReqVO).getRows();
        ExcelUtil<DgDataLevelRespVO> util = new ExcelUtil<>(DgDataLevelRespVO.class);
        util.exportExcel(response, DgDataLevelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据分级列表")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:import')")
    @Log(title = "log.op.title.dg.data.level", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDataLevelRespVO> util = new ExcelUtil<>(DgDataLevelRespVO.class);
        List<DgDataLevelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDataLevelService.importDgDataLevel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据分级详细信息")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataLevelRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataLevelDO dgDataLevelDO = dgDataLevelService.getDgDataLevelById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataLevelDO, DgDataLevelRespVO.class));
    }

    @Operation(summary = "新增数据分级")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:add')")
    @Log(title = "log.op.title.dg.data.level", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataLevelSaveReqVO dgDataLevel) {
        dgDataLevel.setCreatorId(getUserId());
        dgDataLevel.setCreateBy(getNickName());
        dgDataLevel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataLevelService.createDgDataLevel(dgDataLevel));
    }



    @Operation(summary = "修改数据分级")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:edit')")
    @Log(title = "log.op.title.dg.data.level", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataLevelSaveReqVO dgDataLevel) {
        dgDataLevel.setUpdatorId(getUserId());
        dgDataLevel.setUpdateBy(getNickName());
        dgDataLevel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataLevelService.updateDgDataLevel(dgDataLevel));
    }

    @Operation(summary = "删除数据分级")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:remove')")
    @Log(title = "log.op.title.dg.data.level", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataLevelService.removeDgDataLevel(Arrays.asList(ids)));
    }

}
