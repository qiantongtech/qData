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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.api;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.api.DaAssetApiParamConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;

/**
 * 数据资产-外部API-参数Controller
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-外部API-参数")
@RestController
@RequestMapping("/da/assetApiParam")
@Validated
public class DaAssetApiParamController extends BaseController {
    @Resource
    private IDaAssetApiParamService daAssetApiParamService;

    @Operation(summary = "查询数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetApiParamRespVO>> list(DaAssetApiParamPageReqVO daAssetApiParam) {
        PageResult<DaAssetApiParamDO> page = daAssetApiParamService.getDaAssetApiParamPage(daAssetApiParam);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "查询数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:list')")
    @GetMapping("/getDaAssetApiParamList")
    public CommonResult<List<DaAssetApiParamRespVO>> getDaAssetApiParamList(DaAssetApiParamPageReqVO daAssetApiParam) {
        return CommonResult.success(BeanUtils.toBean(daAssetApiParamService.getDaAssetApiParamList(daAssetApiParam.getApiId()), DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "导出数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:export')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetApiParamPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetApiParamDO> list = (List<DaAssetApiParamDO>) daAssetApiParamService.getDaAssetApiParamPage(exportReqVO).getRows();
        ExcelUtil<DaAssetApiParamRespVO> util = new ExcelUtil<>(DaAssetApiParamRespVO.class);
        util.exportExcel(response, DaAssetApiParamConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:import')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetApiParamRespVO> util = new ExcelUtil<>(DaAssetApiParamRespVO.class);
        List<DaAssetApiParamRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetApiParamService.importDaAssetApiParam(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-外部API-参数详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetApiParamRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetApiParamDO daAssetApiParamDO = daAssetApiParamService.getDaAssetApiParamById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetApiParamDO, DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "新增数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:add')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetApiParamSaveReqVO daAssetApiParam) {
        daAssetApiParam.setCreatorId(getUserId());
        daAssetApiParam.setCreateBy(getNickName());
        daAssetApiParam.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiParamService.createDaAssetApiParam(daAssetApiParam));
    }

    @Operation(summary = "修改数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:edit')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetApiParamSaveReqVO daAssetApiParam) {
        daAssetApiParam.setUpdatorId(getUserId());
        daAssetApiParam.setUpdateBy(getNickName());
        daAssetApiParam.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiParamService.updateDaAssetApiParam(daAssetApiParam));
    }

    @Operation(summary = "删除数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:remove')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetApiParamService.removeDaAssetApiParam(Arrays.asList(ids)));
    }

}
