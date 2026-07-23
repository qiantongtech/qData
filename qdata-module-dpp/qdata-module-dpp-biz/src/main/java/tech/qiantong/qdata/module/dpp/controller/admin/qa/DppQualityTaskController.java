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

package tech.qiantong.qdata.module.dpp.controller.admin.qa;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.qa.DppQualityTaskConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskService;

/**
 * Data Quality Task Controller
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Tag(name = "Data Quality Task")
@RestController
@RequestMapping("/dpp/qualityTask")
@Validated
public class DppQualityTaskController extends BaseController {
    @Resource
    private IDppQualityTaskService dppQualityTaskService;

    @Operation(summary = "查询数据质量任务列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityTaskRespVO>> list(DppQualityTaskPageReqVO dppQualityTask) {
        PageResult<DppQualityTaskDO> page = dppQualityTaskService.getDppQualityTaskPage(dppQualityTask);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityTaskRespVO.class));
    }

    @Operation(summary = "导出数据质量任务列表")
    @Log(title = "log.op.title.dpp.quality", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityTaskDO> list = (List<DppQualityTaskDO>) dppQualityTaskService.getDppQualityTaskPage(exportReqVO).getRows();
        ExcelUtil<DppQualityTaskRespVO> util = new ExcelUtil<>(DppQualityTaskRespVO.class);
        util.exportExcel(response, DppQualityTaskConvert.INSTANCE.convertToRespVOList(list), "Application Management Data");
    }

    @Operation(summary = "导入数据质量任务列表")
    @Log(title = "log.op.title.dpp.quality", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityTaskRespVO> util = new ExcelUtil<>(DppQualityTaskRespVO.class);
        List<DppQualityTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityTaskService.importDppQualityTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量任务详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityTaskRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityTaskRespVO dppQualityTaskDO = dppQualityTaskService.getDppQualityTaskById(id);
        return CommonResult.success(dppQualityTaskDO);
    }

    @Operation(summary = "获取数据质量任务详细信息")
    @GetMapping( "/getQualityTaskAsset")
    public CommonResult<DppQualityTaskRespVO> getQualityTaskAsset(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO) {
        DppQualityTaskRespVO dppQualityTaskDO = dppQualityTaskService.getQualityTaskAsset(dppQualityTaskAssetReqVO);
        return CommonResult.success(dppQualityTaskDO);
    }

    @Operation(summary = "新增数据质量任务")
    @Log(title = "log.op.title.dpp.quality", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityTaskSaveReqVO dppQualityTask) {
        dppQualityTask.setCreatorId(getUserId());
        dppQualityTask.setCreateBy(getNickName());
        dppQualityTask.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskService.createDppQualityTask(dppQualityTask));
    }

    @Operation(summary = "修改数据质量任务")
    @Log(title = "log.op.title.dpp.quality", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityTaskSaveReqVO dppQualityTask) {
        dppQualityTask.setUpdatorId(getUserId());
        dppQualityTask.setUpdateBy(getNickName());
        dppQualityTask.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskService.updateDppQualityTask(dppQualityTask));
    }

    @Operation(summary = "删除数据质量任务")
    @Log(title = "log.op.title.dpp.quality", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityTaskService.removeDppQualityTask(Arrays.asList(ids)));
    }


    @Operation(summary = "修改数据质量任务")
    @PostMapping("/updateDppQualityTaskStatus")
    public AjaxResult updateDaDiscoveryTaskStatus(@RequestBody DppQualityTaskSaveReqVO daDiscoveryTask)
    {
        boolean result = dppQualityTaskService.updateDppQualityTaskStatus(daDiscoveryTask);
        return result ? success() : error(MessageUtils.messageWithFallback(
                "dpp.error.task.notfound.expired", "The task does not exist or has expired"));
    }

    @Log(title = "log.op.title.dpp.task.trigger", businessType = BusinessType.UPDATE)
    @PutMapping("/startDppQualityTask/{id}")
    public AjaxResult startDaDiscoveryTask(@PathVariable("id") Long id)
    {
        return dppQualityTaskService.startDppQualityTask(id);
    }


    @Log(title = "log.op.title.dpp.quality.status", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDaDiscoveryTaskCronExpression")
    public AjaxResult updateDaDiscoveryTaskCronExpression(@RequestBody DppQualityTaskSaveReqVO daDiscoveryTask)
    {
        boolean result = dppQualityTaskService.updateDaDiscoveryTaskCronExpression(daDiscoveryTask);
        return result ? success() : error(MessageUtils.messageWithFallback(
                "dpp.error.task.notfound.expired", "The task does not exist or has expired"));
    }

}
