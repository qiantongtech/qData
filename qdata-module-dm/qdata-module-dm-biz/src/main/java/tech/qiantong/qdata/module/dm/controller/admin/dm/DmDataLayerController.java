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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerTreeRespVO;
import tech.qiantong.qdata.module.dm.convert.dm.DmDataLayerConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataLayerService;

/**
 * 数仓分层管理Controller
 *
 * @author FXB
 * @date 2026-03-24
 */
@Tag(name = "数仓分层管理")
@RestController
@RequestMapping("/dm/dataLayer")
@Validated
public class DmDataLayerController extends BaseController {
    @Resource
    private IDmDataLayerService dmDataLayerService;

    @Operation(summary = "查询数仓分层管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmDataLayerRespVO>> list(DmDataLayerPageReqVO dmDataLayer) {
        PageResult<DmDataLayerDO> page = dmDataLayerService.getDmDataLayerPage(dmDataLayer);
        return CommonResult.success(BeanUtils.toBean(page, DmDataLayerRespVO.class));
    }

    @Operation(summary = "查询数仓分层管理树")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:list')")
    @GetMapping("/tree")
    public CommonResult<List<DmDataLayerTreeRespVO>> tree() {
        return CommonResult.success(dmDataLayerService.tree());
    }

    @Operation(summary = "导出数仓分层管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:export')")
    @Log(title = "数仓分层管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmDataLayerPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmDataLayerDO> list = (List<DmDataLayerDO>) dmDataLayerService.getDmDataLayerPage(exportReqVO).getRows();
        ExcelUtil<DmDataLayerRespVO> util = new ExcelUtil<>(DmDataLayerRespVO.class);
        util.exportExcel(response, DmDataLayerConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数仓分层管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:import')")
    @Log(title = "数仓分层管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmDataLayerRespVO> util = new ExcelUtil<>(DmDataLayerRespVO.class);
        List<DmDataLayerRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmDataLayerService.importDmDataLayer(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数仓分层管理详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmDataLayerRespVO> getInfo(@PathVariable("id") Long id) {
        DmDataLayerDO dmDataLayerDO = dmDataLayerService.getDmDataLayerById(id);
        return CommonResult.success(BeanUtils.toBean(dmDataLayerDO, DmDataLayerRespVO.class));
    }

    @Operation(summary = "新增数仓分层管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:add')")
    @Log(title = "数仓分层管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDataLayerSaveReqVO dmDataLayer) {
        dmDataLayer.setCreatorId(getUserId());
        dmDataLayer.setCreateBy(getNickName());
        dmDataLayer.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerService.createDmDataLayer(dmDataLayer));
    }

    @Operation(summary = "修改数仓分层管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:edit')")
    @Log(title = "数仓分层管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDataLayerSaveReqVO dmDataLayer) {
        dmDataLayer.setUpdatorId(getUserId());
        dmDataLayer.setUpdateBy(getNickName());
        dmDataLayer.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataLayerService.updateDmDataLayer(dmDataLayer));
    }

    @Operation(summary = "删除数仓分层管理")
    @PreAuthorize("@ss.hasPermi('dm:dataLayer:remove')")
    @Log(title = "数仓分层管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmDataLayerService.removeDmDataLayer(Arrays.asList(ids)));
    }

}
