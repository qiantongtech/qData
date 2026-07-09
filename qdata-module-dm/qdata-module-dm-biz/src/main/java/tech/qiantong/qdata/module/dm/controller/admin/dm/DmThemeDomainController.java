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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.convert.dm.DmThemeDomainConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;
import tech.qiantong.qdata.module.dm.service.dm.IDmThemeDomainService;

/**
 * Theme Domain Controller
 *
 * @author FXB
 * @date 2026-03-24
 */
@Tag(name = "主题域管理")
@RestController
@RequestMapping("/dm/themeDomain")
@Validated
public class DmThemeDomainController extends BaseController {
    @Resource
    private IDmThemeDomainService dmThemeDomainService;

    @Operation(summary = "查询主题域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:list')")
    @GetMapping("/list")
    public CommonResult<List<DmThemeDomainRespVO>> list(DmThemeDomainPageReqVO dmThemeDomain) {
        List<DmThemeDomainDO> dmThemeDomainList = dmThemeDomainService.getDmThemeDomainList(dmThemeDomain);
        return CommonResult.success(BeanUtils.toBean(dmThemeDomainList, DmThemeDomainRespVO.class));
    }

    @Operation(summary = "导出主题域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:export')")
    @Log(title = "log.op.title.dm.theme.domain", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmThemeDomainPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmThemeDomainDO> list = (List<DmThemeDomainDO>) dmThemeDomainService.getDmThemeDomainPage(exportReqVO).getRows();
        ExcelUtil<DmThemeDomainRespVO> util = new ExcelUtil<>(DmThemeDomainRespVO.class);
        util.exportExcel(response, DmThemeDomainConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入主题域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:import')")
    @Log(title = "log.op.title.dm.theme.domain", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmThemeDomainRespVO> util = new ExcelUtil<>(DmThemeDomainRespVO.class);
        List<DmThemeDomainRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmThemeDomainService.importDmThemeDomain(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取主题域管理详细信息")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmThemeDomainRespVO> getInfo(@PathVariable("id") Long id) {
        DmThemeDomainDO dmThemeDomainDO = dmThemeDomainService.getDmThemeDomainById(id);
        return CommonResult.success(BeanUtils.toBean(dmThemeDomainDO, DmThemeDomainRespVO.class));
    }

    @Operation(summary = "新增主题域管理")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:add')")
    @Log(title = "log.op.title.dm.theme.domain", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmThemeDomainSaveReqVO dmThemeDomain) {
        dmThemeDomain.setCreatorId(getUserId());
        dmThemeDomain.setCreateBy(getNickName());
        dmThemeDomain.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmThemeDomainService.createDmThemeDomain(dmThemeDomain));
    }

    @Operation(summary = "修改主题域管理")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:edit')")
    @Log(title = "log.op.title.dm.theme.domain", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmThemeDomainSaveReqVO dmThemeDomain) {
        dmThemeDomain.setUpdatorId(getUserId());
        dmThemeDomain.setUpdateBy(getNickName());
        dmThemeDomain.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmThemeDomainService.updateDmThemeDomain(dmThemeDomain));
    }

    @Operation(summary = "删除主题域管理")
    @PreAuthorize("@ss.hasPermi('dm:themeDomain:remove')")
    @Log(title = "log.op.title.dm.theme.domain", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmThemeDomainService.removeDmThemeDomain(Arrays.asList(ids)));
    }

}
