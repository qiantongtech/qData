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
 * 数据分级Controller
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
    @Log(title = "数据分级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDataLevelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDataLevelDO> list = (List<DgDataLevelDO>) dgDataLevelService.getDgDataLevelPage(exportReqVO).getRows();
        ExcelUtil<DgDataLevelRespVO> util = new ExcelUtil<>(DgDataLevelRespVO.class);
        util.exportExcel(response, DgDataLevelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据分级列表")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:import')")
    @Log(title = "数据分级", businessType = BusinessType.IMPORT)
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
    @Log(title = "数据分级", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataLevelSaveReqVO dgDataLevel) {
        dgDataLevel.setCreatorId(getUserId());
        dgDataLevel.setCreateBy(getNickName());
        dgDataLevel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataLevelService.createDgDataLevel(dgDataLevel));
    }



    @Operation(summary = "修改数据分级")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:edit')")
    @Log(title = "数据分级", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataLevelSaveReqVO dgDataLevel) {
        dgDataLevel.setUpdatorId(getUserId());
        dgDataLevel.setUpdateBy(getNickName());
        dgDataLevel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataLevelService.updateDgDataLevel(dgDataLevel));
    }

    @Operation(summary = "删除数据分级")
    @PreAuthorize("@ss.hasPermi('dg:datalevel:remove')")
    @Log(title = "数据分级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataLevelService.removeDgDataLevel(Arrays.asList(ids)));
    }

}
