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

package tech.qiantong.qdata.module.att.controller.admin.cat;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttDocumentCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttDocumentCatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 标准信息分类管理Controller
 *
 * @author qdata
 * @date 2025-08-21
 */
@Tag(name = "标准信息分类管理")
@RestController
@RequestMapping("/att/documentCat")
@Validated
public class AttDocumentCatController extends BaseController {
    @Resource
    private IAttDocumentCatService attDocumentCatService;

    @Operation(summary = "查询标准信息分类管理列表")
    @PreAuthorize("@ss.hasPermi('att:documentCat:list')")
    @GetMapping("/list")
    public CommonResult<List<AttDocumentCatRespVO>> list(AttDocumentCatPageReqVO attDocumentCat) {
        attDocumentCat.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttDocumentCatDO> list = (List<AttDocumentCatDO>) attDocumentCatService.getAttDocumentCatPage(attDocumentCat).getRows();
        return CommonResult.success(BeanUtils.toBean(list, AttDocumentCatRespVO.class));
    }

    @Operation(summary = "查询标准信息分类管理列表")
    @GetMapping("/getAttDocumentCatList")
    public CommonResult<List<AttDocumentCatRespVO>> getAttDocumentCatList(AttDocumentCatPageReqVO attDocumentCat) {
        attDocumentCat.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttDocumentCatDO> list = attDocumentCatService.getAttDocumentCatList(attDocumentCat);
        return CommonResult.success(BeanUtils.toBean(list, AttDocumentCatRespVO.class));
    }

    @Operation(summary = "导出标准信息分类管理列表")
    @PreAuthorize("@ss.hasPermi('att:documentCat:export')")
    @Log(title = "log.op.title.att.document.cat", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttDocumentCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttDocumentCatDO> list = (List<AttDocumentCatDO>) attDocumentCatService.getAttDocumentCatPage(exportReqVO).getRows();
        ExcelUtil<AttDocumentCatRespVO> util = new ExcelUtil<>(AttDocumentCatRespVO.class);
        util.exportExcel(response, AttDocumentCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }


    @Operation(summary = "获取标准信息分类管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:documentCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttDocumentCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttDocumentCatDO attDocumentCatDO = attDocumentCatService.getAttDocumentCatById(id);
        return CommonResult.success(BeanUtils.toBean(attDocumentCatDO, AttDocumentCatRespVO.class));
    }

    @Operation(summary = "新增标准信息分类管理")
    @PreAuthorize("@ss.hasPermi('att:documentCat:add')")
    @Log(title = "log.op.title.att.document.cat", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttDocumentCatSaveReqVO attDocumentCat) {
        attDocumentCat.setCreatorId(getUserId());
        attDocumentCat.setCreateBy(getNickName());
        attDocumentCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attDocumentCatService.createAttDocumentCat(attDocumentCat));
    }

    @Operation(summary = "修改标准信息分类管理")
    @PreAuthorize("@ss.hasPermi('att:documentCat:edit')")
    @Log(title = "log.op.title.att.document.cat", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttDocumentCatSaveReqVO attDocumentCat) {
        attDocumentCat.setUpdatorId(getUserId());
        attDocumentCat.setUpdateBy(getNickName());
        attDocumentCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attDocumentCatService.updateAttDocumentCat(attDocumentCat));
    }

    @Operation(summary = "删除标准信息分类管理")
    @PreAuthorize("@ss.hasPermi('att:documentCat:remove')")
    @Log(title = "log.op.title.att.document.cat", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public CommonResult<Integer> remove(@PathVariable Long id) {
        if (attDocumentCatService.hasChildByAttDocumentCatId(id)) {
            return CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(),"存在子标准信息分类管理，无法删除。");
        }
        return CommonResult.toAjax(attDocumentCatService.removeAttDocumentCat(id));
    }

}
