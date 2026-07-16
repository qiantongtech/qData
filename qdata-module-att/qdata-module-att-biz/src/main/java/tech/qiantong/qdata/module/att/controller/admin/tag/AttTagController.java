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

package tech.qiantong.qdata.module.att.controller.admin.tag;

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
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.convert.Tag.AttTagConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Tag Management Controller
 *
 * @author qdata
 * @date 2025-07-11
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("/att/tag")
@Validated
public class AttTagController extends BaseController {
    @Resource
    private IAttTagService attTagService;

    @Operation(summary = "查询标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttTagRespVO>> list(AttTagPageReqVO attTag) {
        PageResult<AttTagDO> page = attTagService.getAttTagPage(attTag);
        return CommonResult.success(BeanUtils.toBean(page, AttTagRespVO.class));
    }

    @Operation(summary = "查询标签管理列表")
    @GetMapping("/listDict")
    public CommonResult<List<AttTagRespVO>> list() {
        List<AttTagDO> attTagList = attTagService.getAttTagList();
        return CommonResult.success(BeanUtils.toBean(attTagList, AttTagRespVO.class));
    }

    @Operation(summary = "导出标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:export')")
    @Log(title = "log.op.title.att.tag", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTagPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTagDO> list = (List<AttTagDO>) attTagService.getAttTagPage(exportReqVO).getRows();
        ExcelUtil<AttTagRespVO> util = new ExcelUtil<>(AttTagRespVO.class);
        util.exportExcel(response, AttTagConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:import')")
    @Log(title = "log.op.title.att.tag", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttTagRespVO> util = new ExcelUtil<>(AttTagRespVO.class);
        List<AttTagRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attTagService.importAttTag(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取标签管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:tag:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttTagRespVO> getInfo(@PathVariable("id") Long id) {
        AttTagRespVO attTagDO = attTagService.getAttTagById(id);
        return CommonResult.success(attTagDO);
    }

    @Operation(summary = "新增标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:add')")
    @Log(title = "log.op.title.att.tag", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTagSaveReqVO attTag) {
        attTag.setCreatorId(getUserId());
        attTag.setCreateBy(getNickName());
        attTag.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTagService.createAttTag(attTag));
    }

    @Operation(summary = "修改标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:edit')")
    @Log(title = "log.op.title.att.tag", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTagSaveReqVO attTag) {
        attTag.setUpdatorId(getUserId());
        attTag.setUpdateBy(getNickName());
        attTag.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTagService.updateAttTag(attTag));
    }

    @Operation(summary = "删除标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:remove')")
    @Log(title = "log.op.title.att.tag", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attTagService.removeAttTag(Arrays.asList(ids)));
    }

}
