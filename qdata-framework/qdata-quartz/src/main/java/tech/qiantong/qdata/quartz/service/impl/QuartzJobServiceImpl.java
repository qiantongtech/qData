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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.enums.JobErrorEnum;
import tech.qiantong.qdata.quartz.mapper.QuartzJobMapper;
import tech.qiantong.qdata.quartz.service.IQuartzJobService;
import tech.qiantong.qdata.quartz.util.CronUtils;
import tech.qiantong.qdata.quartz.util.ScheduleUtils;

import java.util.List;

import static tech.qiantong.qdata.common.constant.ScheduleConstants.QUARTZ_JOB_NAMESPACE;

/**
 * Quartz Business scheduling task service implementation
 *
 * @author qdata
 */
@Service
public class QuartzJobServiceImpl implements IQuartzJobService {

    private final Scheduler scheduler;
    private final QuartzJobMapper jobMapper;

    public QuartzJobServiceImpl(Scheduler scheduler, QuartzJobMapper jobMapper) {
        this.scheduler = scheduler;
        this.jobMapper = jobMapper;
    }

    @Override
    public List<QuartzJob> selectJobList(QuartzJob job) {
        return jobMapper.selectJobList(job);
    }

    @Override
    public QuartzJob selectJobById(Long jobId) {
        return jobMapper.selectJobById(jobId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(QuartzJob job) throws SchedulerException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.pauseJob(getJobKey(job));
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(QuartzJob job) throws SchedulerException {
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.resumeJob(getJobKey(job));
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(QuartzJob job) throws SchedulerException {
        int rows = jobMapper.deleteJobById(job.getJobId());
        if (rows > 0) {
            scheduler.deleteJob(getJobKey(job));
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            QuartzJob job = jobMapper.selectJobById(jobId);
            if (job != null) {
                deleteJob(job);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(QuartzJob job) throws SchedulerException {
        if (ScheduleConstants.Status.NORMAL.getValue().equals(job.getStatus())) {
            return resumeJob(job);
        }
        if (ScheduleConstants.Status.PAUSE.getValue().equals(job.getStatus())) {
            return pauseJob(job);
        }
        return 0;
    }

    @Override
    public boolean run(QuartzJob job) throws SchedulerException {
        QuartzJob properties = selectJobById(job.getJobId());
        if (properties == null) {
            return false;
        }
        JobKey jobKey = getJobKey(properties);
        if (!scheduler.checkExists(jobKey)) {
            return false;
        }
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(jobKey, dataMap);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(QuartzJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job, QUARTZ_JOB_NAMESPACE);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertJobReturnId(QuartzJob job) throws SchedulerException, TaskException {
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job, QUARTZ_JOB_NAMESPACE);
        }
        return job.getJobId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(QuartzJob job) throws SchedulerException, TaskException {
        QuartzJob properties = selectJobById(job.getJobId());
        if (properties == null) {
            return 0;
        }
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateJobReturnId(QuartzJob job) throws SchedulerException, TaskException {
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        QuartzJob properties = selectJobById(job.getJobId());
        if (properties == null) {
            return 0L;
        }
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return job.getJobId();
    }

    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    private void updateSchedulerJob(QuartzJob job, String oldJobGroup)
            throws SchedulerException, TaskException {
        JobKey oldJobKey = ScheduleUtils.getJobKey(job.getJobId(), oldJobGroup, QUARTZ_JOB_NAMESPACE);
        if (scheduler.checkExists(oldJobKey)) {
            scheduler.deleteJob(oldJobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job, QUARTZ_JOB_NAMESPACE);
    }

    private JobKey getJobKey(QuartzJob job) {
        return ScheduleUtils.getJobKey(job.getJobId(), job.getJobGroup(), QUARTZ_JOB_NAMESPACE);
    }

    private Long validateJobConfig(QuartzJob job) {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return JobErrorEnum.CRON_INVALID.getCode();
        }

        String invokeTarget = job.getInvokeTarget();
        if (StringUtils.containsIgnoreCase(invokeTarget, Constants.LOOKUP_RMI)) {
            return JobErrorEnum.RMI_NOT_ALLOWED.getCode();
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return JobErrorEnum.LDAP_NOT_ALLOWED.getCode();
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.HTTP, Constants.HTTPS})) {
            return JobErrorEnum.HTTP_NOT_ALLOWED.getCode();
        }
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_ERROR_STR)) {
            return JobErrorEnum.INVALID_TARGET.getCode();
        }
        return 0L;
    }
}
