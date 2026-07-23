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
