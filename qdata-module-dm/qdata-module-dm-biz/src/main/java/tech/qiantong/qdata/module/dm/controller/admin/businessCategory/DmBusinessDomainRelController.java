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

package tech.qiantong.qdata.module.dm.controller.admin.businessCategory;

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
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.convert.businessCategory.DmBusinessDomainRelConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessDomainRelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 业务分类数据域关联关系Controller
 *
 * @author qdata
 * @date 2026-04-12
 */
@Tag(name = "业务分类数据域关联关系")
@RestController
@RequestMapping("/dm/BusinessDomainRel")
@Validated
public class DmBusinessDomainRelController extends BaseController {
    @Resource
    private IDmBusinessDomainRelService dmBusinessDomainRelService;

    @Operation(summary = "查询业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmBusinessDomainRelRespVO>> list(DmBusinessDomainRelPageReqVO dmBusinessDomainRel) {
        PageResult<DmBusinessDomainRelDO> page = dmBusinessDomainRelService.getDmBusinessDomainRelPage(dmBusinessDomainRel);
        return CommonResult.success(BeanUtils.toBean(page, DmBusinessDomainRelRespVO.class));
    }

    @Operation(summary = "导出业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:export')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmBusinessDomainRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmBusinessDomainRelDO> list = (List<DmBusinessDomainRelDO>) dmBusinessDomainRelService.getDmBusinessDomainRelPage(exportReqVO).getRows();
        ExcelUtil<DmBusinessDomainRelRespVO> util = new ExcelUtil<>(DmBusinessDomainRelRespVO.class);
        util.exportExcel(response, DmBusinessDomainRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入业务分类数据域关联关系列表")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:import')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmBusinessDomainRelRespVO> util = new ExcelUtil<>(DmBusinessDomainRelRespVO.class);
        List<DmBusinessDomainRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmBusinessDomainRelService.importDmBusinessDomainRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取业务分类数据域关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmBusinessDomainRelRespVO> getInfo(@PathVariable("id") Long id) {
        DmBusinessDomainRelDO dmBusinessDomainRelDO = dmBusinessDomainRelService.getDmBusinessDomainRelById(id);
        return CommonResult.success(BeanUtils.toBean(dmBusinessDomainRelDO, DmBusinessDomainRelRespVO.class));
    }

    @Operation(summary = "新增业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:add')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmBusinessDomainRelSaveReqVO dmBusinessDomainRel) {
        dmBusinessDomainRel.setCreatorId(getUserId());
        dmBusinessDomainRel.setCreateBy(getNickName());
        dmBusinessDomainRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessDomainRelService.createDmBusinessDomainRel(dmBusinessDomainRel));
    }

    @Operation(summary = "修改业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:edit')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmBusinessDomainRelSaveReqVO dmBusinessDomainRel) {
        dmBusinessDomainRel.setUpdatorId(getUserId());
        dmBusinessDomainRel.setUpdateBy(getNickName());
        dmBusinessDomainRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmBusinessDomainRelService.updateDmBusinessDomainRel(dmBusinessDomainRel));
    }

    @Operation(summary = "删除业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:remove')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmBusinessDomainRelService.removeDmBusinessDomainRel(Arrays.asList(ids)));
    }

    @Operation(summary = "删除业务分类数据域关联关系")
    @PreAuthorize("@ss.hasPermi('dm:businessdomainrel:remove')")
    @Log(title = "业务分类数据域关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/deletebyDomainId/{domainId}/{businessCategoryId}")
    public CommonResult<Integer> deletebyDomainId(@PathVariable Long domainId, @PathVariable Long businessCategoryId) {
        return CommonResult.toAjax(dmBusinessDomainRelService.removeDmBusinessDomainRelByDomainId(domainId, businessCategoryId));
    }

}
