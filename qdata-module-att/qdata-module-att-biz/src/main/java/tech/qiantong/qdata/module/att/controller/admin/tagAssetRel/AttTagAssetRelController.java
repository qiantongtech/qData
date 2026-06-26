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

package tech.qiantong.qdata.module.att.controller.admin.tagAssetRel;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.convert.Rel.AttTagAssetRelConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;
import tech.qiantong.qdata.module.att.service.Rel.IAttTagAssetRelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 标签与资产关联关系Controller
 *
 * @author qdata
 * @date 2025-07-11
 */
@Tag(name = "标签与资产关联关系")
@RestController
@RequestMapping("/att/tagAssetRel")
@Validated
public class AttTagAssetRelController extends BaseController {
    @Resource
    private IAttTagAssetRelService attTagAssetRelService;

    @Operation(summary = "查询标签与资产关联关系列表")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttTagAssetRelRespVO>> list(AttTagAssetRelPageReqVO attTagAssetRel) {
        PageResult<AttTagAssetRelDO> page = attTagAssetRelService.getAttTagAssetRelPage(attTagAssetRel);
        return CommonResult.success(BeanUtils.toBean(page, AttTagAssetRelRespVO.class));
    }



    @Operation(summary = "导出标签与资产关联关系列表")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:export')")
    @Log(title = "标签与资产关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTagAssetRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTagAssetRelDO> list = (List<AttTagAssetRelDO>) attTagAssetRelService.getAttTagAssetRelPage(exportReqVO).getRows();
        ExcelUtil<AttTagAssetRelRespVO> util = new ExcelUtil<>(AttTagAssetRelRespVO.class);
        util.exportExcel(response, AttTagAssetRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入标签与资产关联关系列表")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:import')")
    @Log(title = "标签与资产关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttTagAssetRelRespVO> util = new ExcelUtil<>(AttTagAssetRelRespVO.class);
        List<AttTagAssetRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attTagAssetRelService.importAttTagAssetRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取标签与资产关联关系详细信息")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttTagAssetRelRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttTagAssetRelDO attTagAssetRelDO = attTagAssetRelService.getAttTagAssetRelById(ID);
        return CommonResult.success(BeanUtils.toBean(attTagAssetRelDO, AttTagAssetRelRespVO.class));
    }

    @Operation(summary = "新增标签与资产关联关系")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:add')")
    @Log(title = "标签与资产关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTagAssetRelSaveReqVO attTagAssetRel) {
        attTagAssetRel.setCreatorId(getUserId());
        attTagAssetRel.setCreateBy(getNickName());
        attTagAssetRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTagAssetRelService.createAttTagAssetRel(attTagAssetRel));
    }

    @Operation(summary = "修改标签与资产关联关系")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:edit')")
    @Log(title = "标签与资产关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTagAssetRelSaveReqVO attTagAssetRel) {
        attTagAssetRel.setUpdatorId(getUserId());
        attTagAssetRel.setUpdateBy(getNickName());
        attTagAssetRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTagAssetRelService.updateAttTagAssetRel(attTagAssetRel));
    }

    @Operation(summary = "删除标签与资产关联关系")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:remove')")
    @Log(title = "标签与资产关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{IDs}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attTagAssetRelService.removeAttTagAssetRel(Arrays.asList(ids)));
    }

    @Operation(summary = "删除标签与资产关联关系")
//    @PreAuthorize("@ss.hasPermi('att:tagAssetRel:remove')")
    @DeleteMapping("/delByTagIdAndAesstId")
    public CommonResult<Integer> delByTagIdAndAesstId(AttTagAssetRelPageReqVO attTagAssetRel) {
        PageResult<AttTagAssetRelDO> page = attTagAssetRelService.getAttTagAssetRelPage(attTagAssetRel);
        List<AttTagAssetRelDO> rows = (List<AttTagAssetRelDO>) page.getRows();
        return CommonResult.toAjax(attTagAssetRelService.removeAttTagAssetRel(rows.get(0).getId() , attTagAssetRel));
    }

}
