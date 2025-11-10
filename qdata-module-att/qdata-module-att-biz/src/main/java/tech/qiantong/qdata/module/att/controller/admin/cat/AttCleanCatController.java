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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.controller.admin.cat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttCleanCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttCleanCatService;

/**
 * 清洗规则类目Controller
 *
 * @author qdata
 * @date 2025-08-11
 */
@Tag(name = "清洗规则类目")
@RestController
@RequestMapping("/att/cleanCat")
@Validated
public class AttCleanCatController extends BaseController {
    @Resource
    private IAttCleanCatService attCleanCatService;

    @Operation(summary = "查询清洗规则类目列表")
    @GetMapping("/listPage")
    public CommonResult<PageResult<AttCleanCatRespVO>> listPage(AttCleanCatPageReqVO attCleanCat) {
        PageResult<AttCleanCatDO> page = attCleanCatService.getAttCleanCatPage(attCleanCat);
        return CommonResult.success(BeanUtils.toBean(page, AttCleanCatRespVO.class));
    }
    @Operation(summary = "查询清洗规则类目列表")
    @GetMapping("/list")
    public CommonResult<List<AttCleanCatRespVO>> list(AttCleanCatPageReqVO attCleanCat) {
        List<AttCleanCatDO> page = attCleanCatService.getAttCleanCatList(attCleanCat);
        return CommonResult.success(BeanUtils.toBean(page, AttCleanCatRespVO.class));
    }

    @Operation(summary = "导出清洗规则类目列表")
    @PreAuthorize("@ss.hasPermi('att:cleanCat:export')")
    @Log(title = "清洗规则类目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttCleanCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttCleanCatDO> list = (List<AttCleanCatDO>) attCleanCatService.getAttCleanCatPage(exportReqVO).getRows();
        ExcelUtil<AttCleanCatRespVO> util = new ExcelUtil<>(AttCleanCatRespVO.class);
        util.exportExcel(response, AttCleanCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入清洗规则类目列表")
    @PreAuthorize("@ss.hasPermi('att:cleanCat:import')")
    @Log(title = "清洗规则类目", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttCleanCatRespVO> util = new ExcelUtil<>(AttCleanCatRespVO.class);
        List<AttCleanCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attCleanCatService.importAttCleanCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取清洗规则类目详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<AttCleanCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttCleanCatDO attCleanCatDO = attCleanCatService.getAttCleanCatById(id);
        return CommonResult.success(BeanUtils.toBean(attCleanCatDO, AttCleanCatRespVO.class));
    }

    @Operation(summary = "新增清洗规则类目")
    @PreAuthorize("@ss.hasPermi('att:cleanCat:add')")
    @Log(title = "清洗规则类目", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttCleanCatSaveReqVO attCleanCat) {
        attCleanCat.setCreatorId(getUserId());
        attCleanCat.setCreateBy(getNickName());
        attCleanCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attCleanCatService.createAttCleanCat(attCleanCat));
    }

    @Operation(summary = "修改清洗规则类目")
    @PreAuthorize("@ss.hasPermi('att:cleanCat:edit')")
    @Log(title = "清洗规则类目", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttCleanCatSaveReqVO attCleanCat) {
        attCleanCat.setUpdatorId(getUserId());
        attCleanCat.setUpdateBy(getNickName());
        attCleanCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attCleanCatService.updateAttCleanCat(attCleanCat));
    }

    @Operation(summary = "删除清洗规则类目")
    @PreAuthorize("@ss.hasPermi('att:cleanCat:remove')")
    @Log(title = "清洗规则类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long ids) {
        return CommonResult.toAjax(attCleanCatService.removeAttCleanCat(ids));
    }

}
