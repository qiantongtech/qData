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
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttTagCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttTagCatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Tag Category Management Controller
 *
 * @author qdata
 * @date 2025-07-11
 */
@Tag(name = "标签类目管理")
@RestController
@RequestMapping("/att/tagCat")
@Validated
public class AttTagCatController extends BaseController {
    @Resource
    private IAttTagCatService attTagCatService;

    @Operation(summary = "查询标签类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:tagCat:list')")
    @GetMapping("/list")
    public CommonResult<List<AttTagCatRespVO>> list(AttTagCatPageReqVO attTagCat) {
        List<AttTagCatDO> page = attTagCatService.getAttTagCatLIst(attTagCat);
        return CommonResult.success(BeanUtils.toBean(page, AttTagCatRespVO.class));
    }

    @Operation(summary = "导出标签类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:tagCat:export')")
    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTagCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTagCatDO> list = (List<AttTagCatDO>) attTagCatService.getAttTagCatPage(exportReqVO).getRows();
        ExcelUtil<AttTagCatRespVO> util = new ExcelUtil<>(AttTagCatRespVO.class);
        util.exportExcel(response, AttTagCatConvert.INSTANCE.convertToRespVOList(list), "Application Management Data");
    }

    @Operation(summary = "导入标签类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:tagCat:import')")
    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttTagCatRespVO> util = new ExcelUtil<>(AttTagCatRespVO.class);
        List<AttTagCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attTagCatService.importAttTagCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取标签类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:tagCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttTagCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttTagCatDO attTagCatDO = attTagCatService.getAttTagCatById(id);
        return CommonResult.success(BeanUtils.toBean(attTagCatDO, AttTagCatRespVO.class));
    }

    @Operation(summary = "新增标签类目管理")
    @PreAuthorize("@ss.hasPermi('att:tagCat:add')")
    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTagCatSaveReqVO attTagCat) {
        attTagCat.setCreatorId(getUserId());
        attTagCat.setCreateBy(getNickName());
        attTagCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTagCatService.createAttTagCat(attTagCat));
    }

    @Operation(summary = "修改标签类目管理")
    @PreAuthorize("@ss.hasPermi('att:tagCat:edit')")
    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTagCatSaveReqVO attTagCat) {
        attTagCat.setUpdatorId(getUserId());
        attTagCat.setUpdateBy(getNickName());
        attTagCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTagCatService.updateAttTagCat(attTagCat));
    }

//    @Operation(summary = "Delete tag category")
//    @PreAuthorize("@ss.hasPermi('att:tagCat:remove')")
//    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(attTagCatService.removeAttTagCat(Arrays.asList(ids)));
//    }

    // Delete
    @Operation(summary = "删除标签类目管理")
    @PreAuthorize("@ss.hasPermi('att:tagCat:remove')")
    @Log(title = "log.op.title.att.tag.cat", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ID}")
    public CommonResult<Integer> remove(@PathVariable Long ID) {
        return CommonResult.toAjax(attTagCatService.removeAttTagCat(ID));
    }

}
