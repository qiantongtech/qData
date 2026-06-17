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

package tech.qiantong.qdata.module.dp.controller.admin.dataElem;

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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.dataElem.DpDataElemConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemService;

/**
 * 数据元Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "数据元")
@RestController
@RequestMapping("/dp/dataElem")
@Validated
public class DpDataElemController extends BaseController {
    @Resource
    private IDpDataElemService dpDataElemService;

    @Operation(summary = "查询数据元列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpDataElemRespVO>> list(DpDataElemPageReqVO dpDataElem) {
        PageResult<DpDataElemDO> page = dpDataElemService.getDpDataElemPage(dpDataElem);
        return CommonResult.success(BeanUtils.toBean(page, DpDataElemRespVO.class));
    }

    @Operation(summary = "查询数据元列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:list')")
    @GetMapping("/getDpDataElemList")
    public CommonResult<List<DpDataElemRespVO>> getDpDataElemList(DpDataElemPageReqVO dpDataElem) {
        List<DpDataElemDO> list = dpDataElemService.getDpDataElemList(dpDataElem);
        return CommonResult.success(BeanUtils.toBean(list, DpDataElemRespVO.class));
    }

    @Operation(summary = "导出数据元列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:export')")
    @Log(title = "数据元", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpDataElemPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpDataElemDO> list = (List<DpDataElemDO>) dpDataElemService.getDpDataElemPage(exportReqVO).getRows();
        ExcelUtil<DpDataElemRespVO> util = new ExcelUtil<>(DpDataElemRespVO.class);
        util.exportExcel(response, DpDataElemConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据元列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:import')")
    @Log(title = "数据元", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpDataElemRespVO> util = new ExcelUtil<>(DpDataElemRespVO.class);
        List<DpDataElemRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpDataElemService.importDpDataElem(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据元详细信息")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpDataElemRespVO> getInfo(@PathVariable("id") Long id) {
        DpDataElemDO dpDataElemDO = dpDataElemService.getDpDataElemById(id);
        return CommonResult.success(BeanUtils.toBean(dpDataElemDO, DpDataElemRespVO.class));
    }

    @Operation(summary = "新增数据元")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:add')")
    @Log(title = "数据元", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpDataElemSaveReqVO dpDataElem) {
        dpDataElem.setCreatorId(getUserId());
        dpDataElem.setCreateBy(getNickName());
        dpDataElem.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpDataElemService.createDpDataElem(dpDataElem));
    }

    @Operation(summary = "修改数据元")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:edit')")
    @Log(title = "数据元", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpDataElemSaveReqVO dpDataElem) {
        dpDataElem.setUpdatorId(getUserId());
        dpDataElem.setUpdateBy(getNickName());
        dpDataElem.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpDataElemService.updateDpDataElem(dpDataElem));
    }

    @Operation(summary = "删除数据元")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:remove')")
    @Log(title = "数据元", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpDataElemService.removeDpDataElem(Arrays.asList(ids)));
    }

    @Operation(summary = "更改数据元状态")
    @PreAuthorize("@ss.hasPermi('dp:dataElem:edit')")
    @Log(title = "更改数据元状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResult<Boolean> updateStatus(@PathVariable Long id,@PathVariable Long status) {
        return CommonResult.toAjax(dpDataElemService.updateStatus(id,status));
    }


}
