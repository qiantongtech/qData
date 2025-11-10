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

package tech.qiantong.qdata.module.dpp.controller.admin.etl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlSchedulerConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSchedulerService;

/**
 * 数据集成调度信息Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成调度信息")
@RestController
@RequestMapping("/dpp/etlScheduler")
@Validated
public class DppEtlSchedulerController extends BaseController {
    @Resource
    private IDppEtlSchedulerService dppEtlSchedulerService;

    @Operation(summary = "查询数据集成调度信息列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlSchedulerRespVO>> list(DppEtlSchedulerPageReqVO dppEtlScheduler) {
        PageResult<DppEtlSchedulerDO> page = dppEtlSchedulerService.getDppEtlSchedulerPage(dppEtlScheduler);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlSchedulerRespVO.class));
    }

    @Operation(summary = "导出数据集成调度信息列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:export')")
    @Log(title = "数据集成调度信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlSchedulerPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlSchedulerDO> list = (List<DppEtlSchedulerDO>) dppEtlSchedulerService.getDppEtlSchedulerPage(exportReqVO).getRows();
        ExcelUtil<DppEtlSchedulerRespVO> util = new ExcelUtil<>(DppEtlSchedulerRespVO.class);
        util.exportExcel(response, DppEtlSchedulerConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成调度信息列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:import')")
    @Log(title = "数据集成调度信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlSchedulerRespVO> util = new ExcelUtil<>(DppEtlSchedulerRespVO.class);
        List<DppEtlSchedulerRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlSchedulerService.importDppEtlScheduler(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成调度信息详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlSchedulerRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlSchedulerDO dppEtlSchedulerDO = dppEtlSchedulerService.getDppEtlSchedulerById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlSchedulerDO, DppEtlSchedulerRespVO.class));
    }

    @Operation(summary = "新增数据集成调度信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:add')")
    @Log(title = "数据集成调度信息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlSchedulerSaveReqVO dppEtlScheduler) {
        dppEtlScheduler.setCreatorId(getUserId());
        dppEtlScheduler.setCreateBy(getNickName());
        dppEtlScheduler.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSchedulerService.createDppEtlScheduler(dppEtlScheduler));
    }

    @Operation(summary = "修改数据集成调度信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:edit')")
    @Log(title = "数据集成调度信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlSchedulerSaveReqVO dppEtlScheduler) {
        dppEtlScheduler.setUpdatorId(getUserId());
        dppEtlScheduler.setUpdateBy(getNickName());
        dppEtlScheduler.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSchedulerService.updateDppEtlScheduler(dppEtlScheduler));
    }

    @Operation(summary = "删除数据集成调度信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlScheduler:remove')")
    @Log(title = "数据集成调度信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlSchedulerService.removeDppEtlScheduler(Arrays.asList(ids)));
    }

}
