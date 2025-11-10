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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttDataElemCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttDataElemCatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据元类目管理Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "数据元类目管理")
@RestController
@RequestMapping("/att/dataElemCat")
@Validated
public class AttDataElemCatController extends BaseController {
    @Resource
    private IAttDataElemCatService attDataElemCatService;

    @Operation(summary = "查询数据元类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:list')")
    @GetMapping("/list")
    public CommonResult<List<AttDataElemCatRespVO>> list(AttDataElemCatPageReqVO attDataElemCat) {
        List<AttDataElemCatDO> attDataElemCatList = attDataElemCatService.getAttDataElemCatList(attDataElemCat);
        return CommonResult.success(BeanUtils.toBean(attDataElemCatList, AttDataElemCatRespVO.class));
    }

    @Operation(summary = "导出数据元类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:export')")
    @Log(title = "数据元类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttDataElemCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttDataElemCatDO> list = (List<AttDataElemCatDO>) attDataElemCatService.getAttDataElemCatPage(exportReqVO).getRows();
        ExcelUtil<AttDataElemCatRespVO> util = new ExcelUtil<>(AttDataElemCatRespVO.class);
        util.exportExcel(response, AttDataElemCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据元类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:import')")
    @Log(title = "数据元类目管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttDataElemCatRespVO> util = new ExcelUtil<>(AttDataElemCatRespVO.class);
        List<AttDataElemCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attDataElemCatService.importAttDataElemCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据元类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttDataElemCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttDataElemCatDO attDataElemCatDO = attDataElemCatService.getAttDataElemCatById(id);
        return CommonResult.success(BeanUtils.toBean(attDataElemCatDO, AttDataElemCatRespVO.class));
    }

    @Operation(summary = "新增数据元类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:add')")
    @Log(title = "数据元类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttDataElemCatSaveReqVO attDataElemCat) {
        attDataElemCat.setCreatorId(getUserId());
        attDataElemCat.setCreateBy(getNickName());
        attDataElemCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attDataElemCatService.createAttDataElemCat(attDataElemCat));
    }

    @Operation(summary = "修改数据元类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:edit')")
    @Log(title = "数据元类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttDataElemCatSaveReqVO attDataElemCat) {
        attDataElemCat.setUpdatorId(getUserId());
        attDataElemCat.setUpdateBy(getNickName());
        attDataElemCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attDataElemCatService.updateAttDataElemCat(attDataElemCat));
    }

    @Operation(summary = "删除数据元类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataElemCat:remove')")
    @Log(title = "数据元类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attDataElemCatService.removeAttDataElemCat(Arrays.asList(ids)));
    }

}
