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

package tech.qiantong.qdata.quartz.util;

import org.quartz.*;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.exception.job.TaskException.Code;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.spring.SpringUtils;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.enums.ScheduleExecutionTypeEnum;

/**
 * Scheduled task tools
 *
 * @author qdata
 *
 */
public class ScheduleUtils
{
    /**
     * Get quartz task class
     *
     * @param sysJob execution plan
     * @return specific execution task class
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob)
    {
        String executionTypeValue = sysJob instanceof QuartzJob
                ? ((QuartzJob) sysJob).getExecutionType() : null;
        ScheduleExecutionTypeEnum executionType = ScheduleExecutionTypeEnum.resolve(
                executionTypeValue, sysJob.getConcurrent());
        return executionType.shouldUseDisallowConcurrentJob()
                ? QuartzDisallowConcurrentExecution.class
                : QuartzJobExecution.class;
    }

    /**
     * Build task trigger object
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup)
    {
        return getTriggerKey(jobId, jobGroup, "");
    }

    /**
     * Builds a task trigger.
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup, String namespace)
    {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + namespace + jobId, jobGroup);
    }

    /**
     * Build task key object
     */
    public static JobKey getJobKey(Long jobId, String jobGroup)
    {
        return getJobKey(jobId, jobGroup, "");
    }

    /**
     * Builds a namespaced task key to prevent auto-increment IDs from different task tables from colliding.
     */
    public static JobKey getJobKey(Long jobId, String jobGroup, String namespace)
    {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + namespace + jobId, jobGroup);
    }

    /**
     * Create a scheduled task
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException, TaskException
    {
        createScheduleJob(scheduler, job, "");
    }

    /**
     * Creates a namespaced scheduled task.
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job, String namespace)
            throws SchedulerException, TaskException
    {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        // Build job information
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(getJobKey(jobId, jobGroup, namespace)).build();

        // Expression dispatch builder
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // Build a new trigger based on the new cronExpression expression
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(jobId, jobGroup, namespace))
                .withSchedule(cronScheduleBuilder).build();

        // Put in the parameters and the runtime method can get them
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // Determine whether it exists
        if (scheduler.checkExists(getJobKey(jobId, jobGroup, namespace)))
        {
            // To prevent data problems during creation, remove first and then perform the creation operation
            scheduler.deleteJob(getJobKey(jobId, jobGroup, namespace));
        }

        // Determine whether the task is expired
        if (StringUtils.isNotNull(CronUtils.getNextExecution(job.getCronExpression())))
        {
            // Execute scheduled tasks
            scheduler.scheduleJob(jobDetail, trigger);
        }

        // Pause task
        if (job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue()))
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup, namespace));
        }
    }

    /**
     * Set scheduled task strategy
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb)
            throws TaskException
    {
        switch (job.getMisfirePolicy())
        {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        }
    }

    /**
     * Check whether the package name is configured in the whitelist
     *
     * @param invokeTarget target string
     * @return result
     */
    public static boolean whiteList(String invokeTarget)
    {
        String packageName = StringUtils.substringBefore(invokeTarget, "(");
        int count = StringUtils.countMatches(packageName, ".");
        if (count > 1)
        {
            return StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_WHITELIST_STR);
        }
        Object obj = SpringUtils.getBean(StringUtils.split(invokeTarget, ".")[0]);
        String beanPackageName = obj.getClass().getPackage().getName();
        return StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_WHITELIST_STR)
                && !StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_ERROR_STR);
    }
}
