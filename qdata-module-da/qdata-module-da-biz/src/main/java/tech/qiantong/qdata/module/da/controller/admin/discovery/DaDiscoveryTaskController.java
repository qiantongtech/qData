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

package tech.qiantong.qdata.module.da.controller.admin.discovery;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.page.TableDataInfo;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskSaveReqVO;
import tech.qiantong.qdata.module.da.convert.discovery.DaDiscoveryTaskConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskService;
import tech.qiantong.qdata.quartz.domain.SysJobLog;
import tech.qiantong.qdata.quartz.service.ISysJobLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据发现任务Controller
 *
 * @author qdata
 * @date 2025-02-11
 */
@Tag(name = "数据发现任务")
@RestController
@RequestMapping("/da/discoveryTask")
@Validated
public class DaDiscoveryTaskController extends BaseController {
    @Resource
    private IDaDiscoveryTaskService daDiscoveryTaskService;
    @Resource
    private ISysJobLogService jobLogService;

    @Operation(summary = "查询数据发现任务列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDiscoveryTaskRespVO>> list(DaDiscoveryTaskPageReqVO daDiscoveryTask) {
        PageResult<DaDiscoveryTaskDO> page = daDiscoveryTaskService.getDaDiscoveryTaskPage(daDiscoveryTask);
        return CommonResult.success(BeanUtils.toBean(page, DaDiscoveryTaskRespVO.class));
    }

    @Operation(summary = "查询数据发现任务列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:list')")
    @GetMapping("/getDaDiscoveryTaskListPage")
    public CommonResult<PageResult<DaDiscoveryTaskRespVO>> getDaDiscoveryTaskListPage(DaDiscoveryTaskPageReqVO daDiscoveryTask) {
        return CommonResult.success(daDiscoveryTaskService.getDaDiscoveryTaskListPage(daDiscoveryTask));
    }

    @Operation(summary = "导出数据发现任务列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:export')")
    @Log(title = "log.op.title.da.discovery.task", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDiscoveryTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDiscoveryTaskDO> list = (List<DaDiscoveryTaskDO>) daDiscoveryTaskService.getDaDiscoveryTaskPage(exportReqVO).getRows();
        ExcelUtil<DaDiscoveryTaskRespVO> util = new ExcelUtil<>(DaDiscoveryTaskRespVO.class);
        util.exportExcel(response, DaDiscoveryTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据发现任务列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:import')")
    @Log(title = "log.op.title.da.discovery.task", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDiscoveryTaskRespVO> util = new ExcelUtil<>(DaDiscoveryTaskRespVO.class);
        List<DaDiscoveryTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDiscoveryTaskService.importDaDiscoveryTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据发现任务详细信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDiscoveryTaskRespVO> getInfo(@PathVariable("id") Long id) {
        return CommonResult.success(daDiscoveryTaskService.getDaDiscoveryTaskById(id));
    }

    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:list')")
    @GetMapping("/jobLog/list")
    public TableDataInfo jobList(SysJobLog sysJobLog)
    {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 根据调度编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:query')")
    @GetMapping(value = "/jobLog/{jobLogId}")
    public AjaxResult getJobLogInfo(@PathVariable Long jobLogId)
    {
        return success(jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 清空定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:remove')")
    @Log(title = "log.op.title.da.discovery.task.schedule.log", businessType = BusinessType.CLEAN)
    @DeleteMapping("/jobLog/clean")
    public AjaxResult clean()
    {
        jobLogService.cleanJobLog();
        return success();
    }

    @Operation(summary = "新增数据发现任务")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:add')")
    @Log(title = "log.op.title.da.discovery.task", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        daDiscoveryTask.setCreatorId(getUserId());
        daDiscoveryTask.setCreateBy(getNickName());
        daDiscoveryTask.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTaskService.createDaDiscoveryTask(daDiscoveryTask));
    }

    @Operation(summary = "修改数据发现任务")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:edit')")
    @Log(title = "log.op.title.da.discovery.task", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        daDiscoveryTask.setUpdatorId(getUserId());
        daDiscoveryTask.setUpdateBy(getNickName());
        daDiscoveryTask.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTaskService.updateDaDiscoveryTask(daDiscoveryTask));
    }

    @Operation(summary = "删除数据发现任务")
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:remove')")
    @Log(title = "log.op.title.da.discovery.task", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDiscoveryTaskService.removeDaDiscoveryTask(Arrays.asList(ids)));
    }

    @Log(title = "log.op.title.da.discovery.task.job", businessType = BusinessType.UPDATE)
    @PutMapping("/runDaDiscoveryTask/{id}")
    public AjaxResult runDaDiscoveryTask(@PathVariable("id") Long id) throws SchedulerException
    {
        boolean result = daDiscoveryTaskService.runDaDiscoveryTask(id);
        return result ? success() : error("任务不存在或已过期！");
    }


    @Log(title = "log.op.title.da.discovery.task.trigger", businessType = BusinessType.UPDATE)
    @PutMapping("/startDaDiscoveryTask/{id}")
    public AjaxResult startDaDiscoveryTask(@PathVariable("id") Long id)
    {
        return daDiscoveryTaskService.startDaDiscoveryTask(id);
    }


    @Log(title = "log.op.title.da.discovery.task.status", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:edit')")
    @PostMapping("/updateDaDiscoveryTaskStatus")
    public AjaxResult updateDaDiscoveryTaskStatus(@RequestBody DaDiscoveryTaskSaveReqVO daDiscoveryTask) throws SchedulerException
    {
        boolean result = daDiscoveryTaskService.updateDaDiscoveryTaskStatus(daDiscoveryTask);
        return result ? success() : error("任务不存在或已过期！");
    }




    @Log(title = "log.op.title.da.discovery.task.status", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('da:discoveryTask:edit')")
    @PostMapping("/updateDaDiscoveryTaskCronExpression")
    public AjaxResult updateDaDiscoveryTaskCronExpression(@RequestBody DaDiscoveryTaskSaveReqVO daDiscoveryTask) throws SchedulerException
    {
        boolean result = daDiscoveryTaskService.updateDaDiscoveryTaskCronExpression(daDiscoveryTask);
        return result ? success() : error("任务不存在或已过期！");
    }


}
