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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate;

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
import tech.qiantong.qdata.common.core.domain.R;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.operate.DaAssetOperateLogConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;
import tech.qiantong.qdata.module.da.service.assetchild.operate.IDaAssetOperateLogService;

/**
 * 数据资产操作记录Controller
 *
 * @author qdata
 * @date 2025-05-09
 */
@Tag(name = "数据资产操作记录")
@RestController
@RequestMapping("/da/assetOperateLog")
@Validated
public class DaAssetOperateLogController extends BaseController {
    @Resource
    private IDaAssetOperateLogService daAssetOperateLogService;

    @Operation(summary = "查询数据资产操作记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetOperateLogRespVO>> list(DaAssetOperateLogPageReqVO daAssetOperateLog) {
        PageResult<DaAssetOperateLogDO> page = daAssetOperateLogService.getDaAssetOperateLogPage(daAssetOperateLog);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetOperateLogRespVO.class));
    }

    @Operation(summary = "导出数据资产操作记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:export')")
    @Log(title = "数据资产操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetOperateLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetOperateLogDO> list = (List<DaAssetOperateLogDO>) daAssetOperateLogService.getDaAssetOperateLogPage(exportReqVO).getRows();
        ExcelUtil<DaAssetOperateLogRespVO> util = new ExcelUtil<>(DaAssetOperateLogRespVO.class);
        util.exportExcel(response, DaAssetOperateLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产操作记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:import')")
    @Log(title = "数据资产操作记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetOperateLogRespVO> util = new ExcelUtil<>(DaAssetOperateLogRespVO.class);
        List<DaAssetOperateLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetOperateLogService.importDaAssetOperateLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产操作记录详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetOperateLogRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetOperateLogDO daAssetOperateLogDO = daAssetOperateLogService.getDaAssetOperateLogById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetOperateLogDO, DaAssetOperateLogRespVO.class));
    }

    @Operation(summary = "新增数据资产操作记录")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:add')")
    @Log(title = "数据资产操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetOperateLogSaveReqVO daAssetOperateLog) {
        daAssetOperateLog.setCreatorId(getUserId());
        daAssetOperateLog.setCreateBy(getNickName());
        daAssetOperateLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetOperateLogService.createDaAssetOperateLog(daAssetOperateLog));
    }

    @Operation(summary = "修改数据资产操作记录")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:edit')")
    @Log(title = "数据资产操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetOperateLogSaveReqVO daAssetOperateLog) {
        daAssetOperateLog.setUpdatorId(getUserId());
        daAssetOperateLog.setUpdateBy(getNickName());
        daAssetOperateLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetOperateLogService.updateDaAssetOperateLog(daAssetOperateLog));
    }

    @Operation(summary = "删除数据资产操作记录")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:remove')")
    @Log(title = "数据资产操作记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetOperateLogService.removeDaAssetOperateLog(Arrays.asList(ids)));
    }



    @Operation(summary = "回滚")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:add')")
    @PostMapping("/rollBack/{id}")
    public R rollBack(@PathVariable Long id) {
        daAssetOperateLogService.rollBack(id);
        return R.ok();
    }

    @Operation(summary = "查询数据资产操作记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateLog:list')")
    @GetMapping("/queryDaAssetOperateLogPage")
    public CommonResult<PageResult<DaAssetOperateLogRespVO>> queryDaAssetOperateLogPage(DaAssetOperateLogPageReqVO daAssetOperateLog) {
        PageResult<DaAssetOperateLogDO> page = daAssetOperateLogService.queryDaAssetOperateLogPage(daAssetOperateLog);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetOperateLogRespVO.class));
    }





}
