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
 * 标签类目管理Controller
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
    @Log(title = "标签类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTagCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTagCatDO> list = (List<AttTagCatDO>) attTagCatService.getAttTagCatPage(exportReqVO).getRows();
        ExcelUtil<AttTagCatRespVO> util = new ExcelUtil<>(AttTagCatRespVO.class);
        util.exportExcel(response, AttTagCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入标签类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:tagCat:import')")
    @Log(title = "标签类目管理", businessType = BusinessType.IMPORT)
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
    @Log(title = "标签类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTagCatSaveReqVO attTagCat) {
        attTagCat.setCreatorId(getUserId());
        attTagCat.setCreateBy(getNickName());
        attTagCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTagCatService.createAttTagCat(attTagCat));
    }

    @Operation(summary = "修改标签类目管理")
    @PreAuthorize("@ss.hasPermi('att:tagCat:edit')")
    @Log(title = "标签类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTagCatSaveReqVO attTagCat) {
        attTagCat.setUpdatorId(getUserId());
        attTagCat.setUpdateBy(getNickName());
        attTagCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTagCatService.updateAttTagCat(attTagCat));
    }

//    @Operation(summary = "删除标签类目管理")
//    @PreAuthorize("@ss.hasPermi('att:tagCat:remove')")
//    @Log(title = "标签类目管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(attTagCatService.removeAttTagCat(Arrays.asList(ids)));
//    }

    //删除
    @Operation(summary = "删除标签类目管理")
    @PreAuthorize("@ss.hasPermi('att:tagCat:remove')")
    @Log(title = "标签类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ID}")
    public CommonResult<Integer> remove(@PathVariable Long ID) {
        return CommonResult.toAjax(attTagCatService.removeAttTagCat(ID));
    }

}
