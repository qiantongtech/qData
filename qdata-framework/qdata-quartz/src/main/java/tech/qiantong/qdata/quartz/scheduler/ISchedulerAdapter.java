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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.quartz.scheduler;

import org.quartz.SchedulerException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;

/**
 * Unified scheduler interface.
 * Business modules use these methods regardless of whether the backend is Quartz, DolphinScheduler, or another scheduler.
 */
public interface ISchedulerAdapter {
    /**
     * Queries schedule information by scheduled task ID.
     *
     * @param command scheduled task
     * @return scheduled task information
     */
    ScheduleRespDTO selectScheduleById(ScheduleCommand command);

    /**
     * Creates a scheduled task and returns its ID in the external scheduler.
     *
     */
    Long createSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * Updates an existing scheduled task and returns its external scheduler ID.
     *
     */
    Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * Restores a scheduled task to a triggerable state.
     *
     */
    void online(ScheduleCommand command) throws SchedulerException;

    /**
     * Pauses a scheduled task without deleting its configuration.
     *
     */
    void offline(ScheduleCommand command) throws SchedulerException;

    /**
     * Manually triggers a scheduled task immediately.
     *
     */
    void trigger(ScheduleCommand command) throws SchedulerException;

    /**
     * Deletes a task from the external scheduler.
     *
     */
    void delete(ScheduleCommand command);

    /**
     * Generates a unique task code.
     * The code uniquely identifies a scheduled task for management and tracking.
     *
     * @return the generated task code
     */
    long generateTaskCode(Long projectCode);
}
