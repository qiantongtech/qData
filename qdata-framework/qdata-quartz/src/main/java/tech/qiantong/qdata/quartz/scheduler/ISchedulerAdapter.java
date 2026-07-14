package tech.qiantong.qdata.quartz.scheduler;

import org.quartz.SchedulerException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;

/**
 * Handle scheduling configuration and operations.
 * Handle Quartz scheduling operations.
 */
public interface ISchedulerAdapter {
    /**
     * Handle task-related data and operations.
     *
     * @param command parameter value
     * @return the operation result
     */
    ScheduleRespDTO selectScheduleById(ScheduleCommand command);

    /**
     * Handle task-related data and operations.
     *
     */
    Long createSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * Handle task-related data and operations.
     *
     */
    Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * Handle task-related data and operations.
     *
     */
    void online(ScheduleCommand command) throws SchedulerException;

    /**
     * Handle task-related data and operations.
     *
     */
    void offline(ScheduleCommand command) throws SchedulerException;

    /**
     * Handle task-related data and operations.
     *
     */
    void trigger(ScheduleCommand command) throws SchedulerException;

    /**
     * Handle task-related data and operations.
     *
     */
    void delete(ScheduleCommand command);

    /**
     * Handle task-related data and operations.
     * Handle task-related data and operations.
     *
     * @return the operation result
     */
    long generateTaskCode(Long projectCode);
}
