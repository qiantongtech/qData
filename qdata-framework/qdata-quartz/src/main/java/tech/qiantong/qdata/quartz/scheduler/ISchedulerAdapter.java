package tech.qiantong.qdata.quartz.scheduler;

import org.quartz.SchedulerException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;

/**
 * 调度器统一接口。
 * 后续无论接 Quartz、DolphinScheduler 还是别的调度器，业务侧都按这组方法调用。
 */
public interface ISchedulerAdapter {
    /**
     * 通过调度任务ID查询调度信息
     *
     * @param command 调度任务
     * @return 调度任务对象信息
     */
    ScheduleRespDTO selectScheduleById(ScheduleCommand command);

    /**
     * 创建一条调度任务，返回外部调度器里的任务 id。
     *
     */
    Long createSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * 更新已有调度任务，返回更新后的外部调度任务 id。
     *
     */
    Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException;

    /**
     * 把调度任务恢复为可触发状态
     *
     */
    void online(ScheduleCommand command) throws SchedulerException;

    /**
     * 暂停调度任务，但不删除配置
     *
     */
    void offline(ScheduleCommand command) throws SchedulerException;

    /**
     * 立即手动触发一次调度任务
     *
     */
    void trigger(ScheduleCommand command) throws SchedulerException;

    /**
     * 删除外部调度器里的任务
     *
     */
    void delete(ScheduleCommand command);

    /**
     * 生成唯一的任务编码
     * 用于为调度任务创建唯一标识符，便于任务管理和追踪
     *
     * @return 生成的任务编码
     */
    long generateTaskCode(Long projectCode);
}
