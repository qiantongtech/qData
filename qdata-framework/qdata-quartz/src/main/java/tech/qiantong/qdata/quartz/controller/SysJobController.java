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

package tech.qiantong.qdata.quartz.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.TableDataInfo;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.service.ISysJobService;
import tech.qiantong.qdata.quartz.util.CronUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Scheduling task information operation processing
 *
 * @author qdata
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController
{
    @Autowired
    private ISysJobService jobService;

    /**
     * Query scheduled task list
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * Export scheduled task list
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        util.exportExcel(response, list, "Scheduled Jobs");
    }

    /**
     * Get scheduled task details
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * Add a new scheduled task
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.create.cron.invalid",
                    "Failed to create task ''{0}'': invalid Cron expression", job.getJobName()));
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.create.rmi.denied",
                    "Failed to create task ''{0}'': RMI calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS }))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.create.ldap.denied",
                    "Failed to create task ''{0}'': LDAP(S) calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.create.http.denied",
                    "Failed to create task ''{0}'': HTTP(S) calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.create.target.invalid",
                    "Failed to create task ''{0}'': the target string contains prohibited content", job.getJobName()));
        }
//        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
//        {
// return error("New task'" + job.getJobName() + "'Failed, the target string is not in the whitelist");
//        }
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }

    /**
     * Modify scheduled tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.update.cron.invalid",
                    "Failed to update task ''{0}'': invalid Cron expression", job.getJobName()));
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.update.rmi.denied",
                    "Failed to update task ''{0}'': RMI calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS }))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.update.ldap.denied",
                    "Failed to update task ''{0}'': LDAP(S) calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.update.http.denied",
                    "Failed to update task ''{0}'': HTTP(S) calls are not allowed in the target string", job.getJobName()));
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error(MessageUtils.messageWithFallback("sys.error.quartz.job.update.target.invalid",
                    "Failed to update task ''{0}'': the target string contains prohibited content", job.getJobName()));
        }
//        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
//        {
// return error("Modify task'" + job.getJobName() + "'Failed, the target string is not in the whitelist");
//        }
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }

    /**
     * Scheduled task status modification
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * Scheduled tasks are executed immediately
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException
    {
        boolean result = jobService.run(job);
        return result ? success() : error(MessageUtils.messageWithFallback(
                "sys.error.quartz.job.notfound.expired", "The task does not exist or has expired"));
    }

    /**
     * Delete scheduled tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "log.op.title.sys.job", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return success();
    }
}
