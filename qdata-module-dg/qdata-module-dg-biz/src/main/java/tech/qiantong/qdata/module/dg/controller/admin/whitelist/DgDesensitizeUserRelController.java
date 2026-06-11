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

package tech.qiantong.qdata.module.dg.controller.admin.whitelist;

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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.whitelist.DgDesensitizeUserRelConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeUserRelService;

/**
 * 脱敏白名单与用户关联关系Controller
 *
 * @author qdata
 * @date 2026-04-09
 */
@Tag(name = "脱敏白名单与用户关联关系")
@RestController
@RequestMapping("/dg/desensitizeUserRel")
@Validated
public class DgDesensitizeUserRelController extends BaseController {
    @Resource
    private IDgDesensitizeUserRelService dgDesensitizeUserRelService;

    @Operation(summary = "查询脱敏白名单与用户关联关系列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDesensitizeUserRelRespVO>> list(DgDesensitizeUserRelPageReqVO dgDesensitizeUserRel) {
        PageResult<DgDesensitizeUserRelDO> page = dgDesensitizeUserRelService.getDgDesensitizeUserRelPage(dgDesensitizeUserRel);
        return CommonResult.success(BeanUtils.toBean(page, DgDesensitizeUserRelRespVO.class));
    }

    @Operation(summary = "导出脱敏白名单与用户关联关系列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:export')")
    @Log(title = "脱敏白名单与用户关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DgDesensitizeUserRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DgDesensitizeUserRelDO> list = (List<DgDesensitizeUserRelDO>) dgDesensitizeUserRelService.getDgDesensitizeUserRelPage(exportReqVO).getRows();
        ExcelUtil<DgDesensitizeUserRelRespVO> util = new ExcelUtil<>(DgDesensitizeUserRelRespVO.class);
        util.exportExcel(response, DgDesensitizeUserRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入脱敏白名单与用户关联关系列表")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:import')")
    @Log(title = "脱敏白名单与用户关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DgDesensitizeUserRelRespVO> util = new ExcelUtil<>(DgDesensitizeUserRelRespVO.class);
        List<DgDesensitizeUserRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dgDesensitizeUserRelService.importDgDesensitizeUserRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取脱敏白名单与用户关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDesensitizeUserRelRespVO> getInfo(@PathVariable("id") Long id) {
        DgDesensitizeUserRelDO dgDesensitizeUserRelDO = dgDesensitizeUserRelService.getDgDesensitizeUserRelById(id);
        return CommonResult.success(BeanUtils.toBean(dgDesensitizeUserRelDO, DgDesensitizeUserRelRespVO.class));
    }

    @Operation(summary = "新增脱敏白名单与用户关联关系")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:add')")
    @Log(title = "脱敏白名单与用户关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDesensitizeUserRelSaveReqVO dgDesensitizeUserRel) {
        dgDesensitizeUserRel.setCreatorId(getUserId());
        dgDesensitizeUserRel.setCreateBy(getNickName());
        dgDesensitizeUserRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeUserRelService.createDgDesensitizeUserRel(dgDesensitizeUserRel));
    }

    @Operation(summary = "修改脱敏白名单与用户关联关系")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:edit')")
    @Log(title = "脱敏白名单与用户关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDesensitizeUserRelSaveReqVO dgDesensitizeUserRel) {
        dgDesensitizeUserRel.setUpdatorId(getUserId());
        dgDesensitizeUserRel.setUpdateBy(getNickName());
        dgDesensitizeUserRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDesensitizeUserRelService.updateDgDesensitizeUserRel(dgDesensitizeUserRel));
    }

    @Operation(summary = "删除脱敏白名单与用户关联关系")
    @PreAuthorize("@ss.hasPermi('dg:desensitizewhitelist:remove')")
    @Log(title = "脱敏白名单与用户关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDesensitizeUserRelService.removeDgDesensitizeUserRel(Arrays.asList(ids)));
    }

}
