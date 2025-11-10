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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.projectRel.DaAssetProjectRelConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;
import tech.qiantong.qdata.module.da.service.assetchild.projectRel.IDaAssetProjectRelService;

/**
 * 数据资产与项目关联关系Controller
 *
 * @author qdata
 * @date 2025-04-18
 */
@Tag(name = "数据资产与项目关联关系")
@RestController
@RequestMapping("/da/assetProjectRel")
@Validated
public class DaAssetProjectRelController extends BaseController {
    @Resource
    private IDaAssetProjectRelService daAssetProjectRelService;

    @Operation(summary = "查询数据资产与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetProjectRelRespVO>> list(DaAssetProjectRelPageReqVO daAssetProjectRel) {
        PageResult<DaAssetProjectRelDO> page = daAssetProjectRelService.getDaAssetProjectRelPage(daAssetProjectRel);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetProjectRelRespVO.class));
    }

    @Operation(summary = "导出数据资产与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:export')")
    @Log(title = "数据资产与项目关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetProjectRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetProjectRelDO> list = (List<DaAssetProjectRelDO>) daAssetProjectRelService.getDaAssetProjectRelPage(exportReqVO).getRows();
        ExcelUtil<DaAssetProjectRelRespVO> util = new ExcelUtil<>(DaAssetProjectRelRespVO.class);
        util.exportExcel(response, DaAssetProjectRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:import')")
    @Log(title = "数据资产与项目关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetProjectRelRespVO> util = new ExcelUtil<>(DaAssetProjectRelRespVO.class);
        List<DaAssetProjectRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetProjectRelService.importDaAssetProjectRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产与项目关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetProjectRelRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetProjectRelDO daAssetProjectRelDO = daAssetProjectRelService.getDaAssetProjectRelById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetProjectRelDO, DaAssetProjectRelRespVO.class));
    }

    @Operation(summary = "新增数据资产与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:add')")
    @Log(title = "数据资产与项目关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetProjectRelSaveReqVO daAssetProjectRel) {
        daAssetProjectRel.setCreatorId(getUserId());
        daAssetProjectRel.setCreateBy(getNickName());
        daAssetProjectRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetProjectRelService.createDaAssetProjectRel(daAssetProjectRel));
    }

    @Operation(summary = "修改数据资产与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:edit')")
    @Log(title = "数据资产与项目关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetProjectRelSaveReqVO daAssetProjectRel) {
        daAssetProjectRel.setUpdatorId(getUserId());
        daAssetProjectRel.setUpdateBy(getNickName());
        daAssetProjectRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetProjectRelService.updateDaAssetProjectRel(daAssetProjectRel));
    }

    @Operation(summary = "删除数据资产与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetProjectRel:remove')")
    @Log(title = "数据资产与项目关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetProjectRelService.removeDaAssetProjectRel(Arrays.asList(ids)));
    }

}
