package tech.qiantong.qdata.module.mc.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;

import javax.annotation.Resource;

/**
 * Handle Quartz scheduling operations.
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2026-07-07
 */
@Slf4j
@Service
public class McTaskQuartzService {

    @Resource
    private ISchedulerAdapter schedulerAdapter;

    /**
     * Quartz
     * Create the scheduler.
     *
     * @param taskCode parameter value
     * @return the operation result
     */
    public Long createSchedulerQuartz(McTaskDO taskCode) {
        Long schedule = null;
        try {
            schedule = schedulerAdapter.createSchedule(ScheduleCommand.builder().jobName(taskCode.getName()).jobGroup(taskCode.getName())
                    .cronExpression(taskCode.getCronExpression()).invokeTarget("mcTaskExecutorJob.runExecuteTask(" + taskCode.getId() + "L)").remark(taskCode.getRemark()).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.scheduler.create", "创建调度器失败！");
        }
        return schedule;
    }

    /**
     * update Quartz
     * @param taskCode
     * @param scheduler
     */
    public void updateScheduleQuartz(McTaskDO taskCode, McTaskSchedulerDO scheduler, String cronExpression) {
        try {
            schedulerAdapter.updateSchedule(ScheduleCommand.builder().id(Long.valueOf(scheduler.getJobId())).remark(taskCode.getRemark()).jobId(Long.valueOf(scheduler.getJobId()))
                    .jobName(taskCode.getName()).jobGroup(taskCode.getName()).status(scheduler.getStatus()).invokeTarget("mcTaskExecutorJob.runExecuteTask(" + taskCode.getId() + "L)").cronExpression(cronExpression).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.scheduler.update", "更新调度器失败！");
        }
    }

    /**
     * Quartz
     * Handle task-related data and operations.
     *
     * @param schedulerId
     */
    public void onlineSchedulerOnlyQuartz(Long schedulerId) {
        try {
            schedulerAdapter.online(ScheduleCommand.builder().id(schedulerId).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.scheduler.online", "上线调度器失败！");
        }
    }

    /**
     * Quartz
     * Handle task-related data and operations.
     *
     * @param schedulerId
     */
    public void offlineSchedulerOnlyQuartz(Long schedulerId) {
        try {
            schedulerAdapter.offline(ScheduleCommand.builder().id(schedulerId).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.scheduler.offline", "下线调度器失败！");
        }
    }

    /**
     * Quartz
     * Handle task-related data and operations.
     *
     * @param taskId parameter value
     */
    public void startTaskQuartz(Long taskId) {
        try {
            schedulerAdapter.trigger(ScheduleCommand.builder().id(taskId).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.task.start", "启动任务失败：" + e.getMessage());
        }
    }

    /**
     * delete quartz
     * @param taskId
     */
    public void deleteSchedulerQuartz(Long taskId) {
        try {
            schedulerAdapter.delete(ScheduleCommand.builder().id(taskId).build());
        } catch (Exception e) {
            throw new ServiceException("mc.error.task.delete", "删除任务失败：" + e.getMessage());
        }
    }
}
