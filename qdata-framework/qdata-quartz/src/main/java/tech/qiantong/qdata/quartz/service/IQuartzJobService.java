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
import tech.qiantong.qdata.quartz.domain.QuartzJob;

import java.util.List;

/**
 * Service layer for Quartz business schedules.
 *
 * @author qdata
 */
public interface IQuartzJobService {

    List<QuartzJob> selectJobList(QuartzJob job);

    QuartzJob selectJobById(Long jobId);

    int pauseJob(QuartzJob job) throws SchedulerException;

    int resumeJob(QuartzJob job) throws SchedulerException;

    int deleteJob(QuartzJob job) throws SchedulerException;

    void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    int changeStatus(QuartzJob job) throws SchedulerException;

    boolean run(QuartzJob job) throws SchedulerException;

    int insertJob(QuartzJob job) throws SchedulerException, TaskException;

    Long insertJobReturnId(QuartzJob job) throws SchedulerException, TaskException;

    int updateJob(QuartzJob job) throws SchedulerException, TaskException;

    Long updateJobReturnId(QuartzJob job) throws SchedulerException, TaskException;

    boolean checkCronExpressionIsValid(String cronExpression);
}
