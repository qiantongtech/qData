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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.convert.dm.DmDataDomainConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataDomainService;

/**
 * 数据域管理Controller
 *
 * @author FXB
 * @date 2026-03-24
 */
@Tag(name = "数据域管理")
@RestController
@RequestMapping("/dm/dataDomain")
@Validated
public class DmDataDomainController extends BaseController {
    @Resource
    private IDmDataDomainService dmDataDomainService;

    @Operation(summary = "查询数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmDataDomainRespVO>> list(DmDataDomainPageReqVO dmDataDomain) {
        PageResult<DmDataDomainDO> page = dmDataDomainService.getDmDataDomainPage(dmDataDomain);
        return CommonResult.success(BeanUtils.toBean(page, DmDataDomainRespVO.class));
    }

    @Operation(summary = "导出数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:export')")
    @Log(title = "数据域管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmDataDomainPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmDataDomainDO> list = (List<DmDataDomainDO>) dmDataDomainService.getDmDataDomainPage(exportReqVO).getRows();
        ExcelUtil<DmDataDomainRespVO> util = new ExcelUtil<>(DmDataDomainRespVO.class);
        util.exportExcel(response, DmDataDomainConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:import')")
    @Log(title = "数据域管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmDataDomainRespVO> util = new ExcelUtil<>(DmDataDomainRespVO.class);
        List<DmDataDomainRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmDataDomainService.importDmDataDomain(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据域管理详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmDataDomainRespVO> getInfo(@PathVariable("id") Long id) {
        DmDataDomainDO dmDataDomainDO = dmDataDomainService.getDmDataDomainById(id);
        return CommonResult.success(BeanUtils.toBean(dmDataDomainDO, DmDataDomainRespVO.class));
    }

    @Operation(summary = "新增数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:add')")
    @Log(title = "数据域管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDataDomainSaveReqVO dmDataDomain) {
        dmDataDomain.setCreatorId(getUserId());
        dmDataDomain.setCreateBy(getNickName());
        dmDataDomain.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataDomainService.createDmDataDomain(dmDataDomain));
    }

    @Operation(summary = "修改数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:edit')")
    @Log(title = "数据域管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDataDomainSaveReqVO dmDataDomain) {
        dmDataDomain.setUpdatorId(getUserId());
        dmDataDomain.setUpdateBy(getNickName());
        dmDataDomain.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataDomainService.updateDmDataDomain(dmDataDomain));
    }

    @Operation(summary = "删除数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:remove')")
    @Log(title = "数据域管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmDataDomainService.removeDmDataDomain(Arrays.asList(ids)));
    }

    @Operation(summary = "查询数据域管理列通过业务分类id")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:list')")
    @GetMapping("/listByCategoryId")
    public CommonResult<PageResult<DmDataDomainRespVO>> listByCategoryId(DmDataDomainPageReqVO dmDataDomain) {
        PageResult<DmDataDomainDO> page = dmDataDomainService.getDmDataDomainByCategoryId(dmDataDomain);
        return CommonResult.success(BeanUtils.toBean(page, DmDataDomainRespVO.class));
    }
}
