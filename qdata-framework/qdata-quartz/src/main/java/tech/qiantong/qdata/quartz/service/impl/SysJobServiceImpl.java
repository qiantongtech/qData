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

package tech.qiantong.qdata.quartz.service.impl;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.enums.JobErrorEnum;
import tech.qiantong.qdata.quartz.mapper.SysJobMapper;
import tech.qiantong.qdata.quartz.service.ISysJobService;
import tech.qiantong.qdata.quartz.util.CronUtils;
import tech.qiantong.qdata.quartz.util.ScheduleUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Scheduled task scheduling information service layer
 *
 * @author qdata
 */
@Service
public class SysJobServiceImpl implements ISysJobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysJobMapper jobMapper;

    /**
     * When the project starts, the timer is initialized mainly to prevent manual modification of the database from being synchronized to the scheduled task processing (note: the database ID and task group name cannot be modified manually, otherwise dirty data will result)
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * Get the scheduled task list of the quartz scheduler
     *
     * @param job scheduling information
     * @return
     */
    @Override
    public List<SysJob> selectJobList(SysJob job) {
        return jobMapper.selectJobList(job);
    }

    /**
     * Query scheduling information by scheduling task ID
     *
     * @param jobId scheduling task ID
     * @return Scheduling task object information
     */
    @Override
    public SysJob selectJobById(Long jobId) {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * Pause task
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * Recovery task
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * After deleting a task, the corresponding trigger will also be deleted.
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * Delete scheduling information in batches
     *
     * @param jobIds The task ID to be deleted
     * @return result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            SysJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * Task scheduling status modification
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * Run task now
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob job) throws SchedulerException {
        boolean result = false;
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        SysJob properties = selectJobById(job.getJobId());
        // Parameters
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }

    /**
     * Add new task
     *
     * @param job scheduling information scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * Added new tasks with verification
     *
     * @param job scheduling information
     * @return result
     */
    @Override
    public Long insertJobReturnId(SysJob job) throws SchedulerException, TaskException {
        // Verify the legality of task configuration
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return job.getJobId();
    }

    /**
     * Update task time expression
     *
     * @param job scheduling information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob job) throws SchedulerException, TaskException {
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * Update tasks with verification
     *
     * @param job scheduling information
     * @return result returns the task primary key ID
     */
    @Override
    public Long updateJobReturnId(SysJob job) throws SchedulerException, TaskException {
        // Verify the legality of task configuration
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return job.getJobId();
    }

    /**
     * Update task
     *
     * @param job task object
     * @param jobGroup task group name
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // Determine whether it exists
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // To prevent data problems during creation, remove first and then perform the creation operation
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * Verify whether cron expression is valid
     *
     * @param cronExpression expression
     * @return result
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    /**
     * Verify the legality of task configuration
     *
     * @param job task information
     * @return verification result code, 0 means verification passed
     */
    private Long validateJobConfig(SysJob job) {
        // 1. Verify Cron expression
        if (!CronUtils.isValid(job.getCronExpression())) {
            return JobErrorEnum.CRON_INVALID.getCode();
        }

        // 2. Verify target string
        String invokeTarget = job.getInvokeTarget();

        // 2.1 Check RMI calls
        if (StringUtils.containsIgnoreCase(invokeTarget, Constants.LOOKUP_RMI)) {
            return JobErrorEnum.RMI_NOT_ALLOWED.getCode();
        }

        // 2.2 Check LDAP calls
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return JobErrorEnum.LDAP_NOT_ALLOWED.getCode();
        }

        // 2.3 Check HTTP calls
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.HTTP, Constants.HTTPS})) {
            return JobErrorEnum.HTTP_NOT_ALLOWED.getCode();
        }

        // 2.4 Check illegal strings
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_ERROR_STR)) {
            return JobErrorEnum.INVALID_TARGET.getCode();
        }

// // 2.5 Check whitelist
//        if (!ScheduleUtils.whiteList(invokeTarget)) {
//            return JobErrorEnum.NOT_IN_WHITELIST.getCode();
//        }

        return 0L;
    }
}
