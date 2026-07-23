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

package tech.qiantong.qdata.quartz.service;

import org.quartz.SchedulerException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.quartz.domain.SysJob;

import java.util.List;

/**
 * Scheduled task scheduling information service layer
 *
 * @author qdata
 */
public interface ISysJobService
{
    /**
     * Get the scheduled task of the quartz scheduler
     *
     * @param job scheduling information
     * @return Scheduled task collection
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * Query scheduling information by scheduling task ID
     *
     * @param jobId scheduling task ID
     * @return Scheduling task object information
     */
    public SysJob selectJobById(Long jobId);

    /**
     * Pause task
     *
     * @param job scheduling information
     * @return result
     */
    public int pauseJob(SysJob job) throws SchedulerException;

    /**
     * Recovery task
     *
     * @param job scheduling information
     * @return result
     */
    public int resumeJob(SysJob job) throws SchedulerException;

    /**
     * After deleting a task, the corresponding trigger will also be deleted.
     *
     * @param job scheduling information
     * @return result
     */
    public int deleteJob(SysJob job) throws SchedulerException;

    /**
     * Delete scheduling information in batches
     *
     * @param jobIds The task ID to be deleted
     * @return result
     */
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * Task scheduling status modification
     *
     * @param job scheduling information
     * @return result
     */
    public int changeStatus(SysJob job) throws SchedulerException;

    /**
     * Run task now
     *
     * @param job scheduling information
     * @return result
     */
    public boolean run(SysJob job) throws SchedulerException;

    /**
     * Add new task
     *
     * @param job scheduling information
     * @return result
     */
    public int insertJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * Added new tasks with verification
     *
     * @param job scheduling information
     * @return result
     */
    public Long insertJobReturnId(SysJob job) throws SchedulerException, TaskException;

    /**
     * Update task
     *
     * @param job scheduling information
     * @return result
     */
    public int updateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * Update tasks with verification
     *
     * @param job scheduling information
     * @return result returns the task primary key ID
     */
    public Long updateJobReturnId(SysJob job) throws SchedulerException, TaskException;

    /**
     * Verify whether cron expression is valid
     *
     * @param cronExpression expression
     * @return result
     */
    public boolean checkCronExpressionIsValid(String cronExpression);
}
