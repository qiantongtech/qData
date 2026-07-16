package tech.qiantong.qdata.module.dpp.service.scheduler;

import lombok.extern.slf4j.Slf4j;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;

import javax.annotation.Resource;

/**
 * DolphinScheduler 调度器服务
 * 用于管理数据采集任务的调度和执行
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
public class DppTaskQuartzService {

    @Resource
    private ISchedulerAdapter schedulerAdapter;

    /**
     * 创建调度器
     */
    public Long create(DppEtlTaskDO dppEtlTaskDO, String invokeTarget) {
        ScheduleCommand build = ScheduleCommand.builder().jobName(dppEtlTaskDO.getName())
                .jobGroup(dppEtlTaskDO.getName()).remark(dppEtlTaskDO.getRemark()).invokeTarget(String.format(invokeTarget, dppEtlTaskDO.getId()))
                .cronExpression(dppEtlTaskDO.getCronExpression()).executionType(dppEtlTaskDO.getExecutionType()).build();
        Long schedule = 0L;
        try {
            schedule = schedulerAdapter.createSchedule(build);
        } catch (Exception e) {
            ScheduleRespDTO scheduleRespDTO = schedulerAdapter.selectScheduleById(build);
            if (scheduleRespDTO == null) {
                throw new ServiceException("dpp.error.scheduler.create", "创建调度器失败！");
            }
        }
        return schedule;
    }

    /**
     * 修改调度器
     */
    public void update(Long id,String cronExpression){
        try {
            ScheduleRespDTO scheduleRespDTO = schedulerAdapter.selectScheduleById(ScheduleCommand.builder().id(id).build());
            schedulerAdapter.updateSchedule(ScheduleCommand.builder().id(scheduleRespDTO.getJobId()).remark(scheduleRespDTO.getRemark()).jobId(scheduleRespDTO.getJobId())
                            .jobName(scheduleRespDTO.getJobName()).jobGroup(scheduleRespDTO.getJobGroup()).invokeTarget(scheduleRespDTO.getInvokeTarget()).misfirePolicy(scheduleRespDTO.getMisfirePolicy())
                    .concurrent(scheduleRespDTO.getConcurrent()).executionType(scheduleRespDTO.getExecutionType()).status(scheduleRespDTO.getStatus()).cronExpression(cronExpression).build());
        } catch (Exception e) {
            log.error("修改调度器失败！",e);
            throw new ServiceException("dpp.error.scheduler.update", "修改调度器失败！");
        }
    }

    /**
     * 下线
     *
     * @param quartzId
     */
    public void offline(Long quartzId) {
        try {
            schedulerAdapter.offline(ScheduleCommand.builder().id(quartzId).build());
        } catch (SchedulerException e) {
            throw new ServiceException("dpp.error.scheduler.offline", "下线调度器失败！");
        }
    }

    /**
     * 上线
     *
     * @param quartzId
     */
    public void online(Long quartzId) {
        try {
            schedulerAdapter.online(ScheduleCommand.builder().id(quartzId).build());
        } catch (SchedulerException e) {
            throw new ServiceException("dpp.error.scheduler.online", "上线调度器失败！");
        }
    }

    /**
     * 生成任务编号
     *
     * @param projectCode
     * @return
     */
    public Long genCode(Long projectCode) {
        return schedulerAdapter.generateTaskCode(projectCode);
    }
}
