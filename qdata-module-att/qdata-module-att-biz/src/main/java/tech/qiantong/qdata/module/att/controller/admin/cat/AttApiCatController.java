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
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttApiCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttApiCatService;

/**
 * 数据服务类目管理Controller
 *
 * @author qdata
 * @date 2025-03-11
 */
@Tag(name = "数据服务类目管理")
@RestController
@RequestMapping("/att/apiCat")
@Validated
public class AttApiCatController extends BaseController {
    @Resource
    private IAttApiCatService attApiCatService;

    @Operation(summary = "查询数据服务类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:apiCat:list')")
    @GetMapping("/list")
    public CommonResult<List<AttApiCatRespVO>> list(AttApiCatPageReqVO attApiCat) {
        List<AttApiCatDO> attApiCatList = attApiCatService.getAttApiCatList(attApiCat);
        return CommonResult.success(BeanUtils.toBean(attApiCatList, AttApiCatRespVO.class));
    }

    @Operation(summary = "导出数据服务类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:apiCat:export')")
    @Log(title = "数据服务类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttApiCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttApiCatDO> list = (List<AttApiCatDO>) attApiCatService.getAttApiCatPage(exportReqVO).getRows();
        ExcelUtil<AttApiCatRespVO> util = new ExcelUtil<>(AttApiCatRespVO.class);
        util.exportExcel(response, AttApiCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据服务类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:apiCat:import')")
    @Log(title = "数据服务类目管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttApiCatRespVO> util = new ExcelUtil<>(AttApiCatRespVO.class);
        List<AttApiCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attApiCatService.importAttApiCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据服务类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:apiCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttApiCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttApiCatDO attApiCatDO = attApiCatService.getAttApiCatById(id);
        return CommonResult.success(BeanUtils.toBean(attApiCatDO, AttApiCatRespVO.class));
    }

    @Operation(summary = "新增数据服务类目管理")
    @PreAuthorize("@ss.hasPermi('att:apiCat:add')")
    @Log(title = "数据服务类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttApiCatSaveReqVO attApiCat) {
        attApiCat.setCreatorId(getUserId());
        attApiCat.setCreateBy(getNickName());
        attApiCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attApiCatService.createAttApiCat(attApiCat));
    }

    @Operation(summary = "修改数据服务类目管理")
    @PreAuthorize("@ss.hasPermi('att:apiCat:edit')")
    @Log(title = "数据服务类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttApiCatSaveReqVO attApiCat) {
        attApiCat.setUpdatorId(getUserId());
        attApiCat.setUpdateBy(getNickName());
        attApiCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attApiCatService.updateAttApiCat(attApiCat));
    }

    @Operation(summary = "删除数据服务类目管理")
    @PreAuthorize("@ss.hasPermi('att:apiCat:remove')")
    @Log(title = "数据服务类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attApiCatService.removeAttApiCat(Arrays.asList(ids)));
    }

}
