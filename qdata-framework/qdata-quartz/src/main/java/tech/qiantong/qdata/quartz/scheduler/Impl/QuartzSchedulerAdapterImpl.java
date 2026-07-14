package tech.qiantong.qdata.quartz.scheduler.Impl;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;
import tech.qiantong.qdata.quartz.service.ISysJobService;

/**
 * Handle Quartz scheduling operations.
 * Handle Quartz scheduling operations.
 */
@Component
public class QuartzSchedulerAdapterImpl implements ISchedulerAdapter {

    private static final String DEFAULT_CONCURRENT = "1";

    private final ISysJobService sysJobService;

    public QuartzSchedulerAdapterImpl(ISysJobService sysJobService) {
        this.sysJobService = sysJobService;
    }
    @Override
    public ScheduleRespDTO selectScheduleById(ScheduleCommand command) {
        SysJob sysJob = sysJobService.selectJobById(command.getId());
        return BeanUtils.toBean(sysJob, ScheduleRespDTO.class);
    }
    @Override
    public Long createSchedule(ScheduleCommand command) throws SchedulerException, TaskException {
        // Handle Quartz scheduling operations.
        return sysJobService.insertJobReturnId(toSysJob(command));
    }
    @Override
    public Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException {
        SysJob job = toSysJob(command);
        job.setJobId(command.getId());
        return (long) sysJobService.updateJob(job);
    }
    @Override
    public void online(ScheduleCommand command) throws SchedulerException {
        sysJobService.resumeJob(loadJob(command));
    }
    @Override
    public void offline(ScheduleCommand command) throws SchedulerException {
        sysJobService.pauseJob(loadJob(command));
    }
    @Override
    public void trigger(ScheduleCommand command) throws SchedulerException {
        sysJobService.run(loadJob(command));
    }
    @Override
    public void delete(ScheduleCommand command) {
        try {
            sysJobService.deleteJob(loadJob(command));
        } catch (SchedulerException e) {
            throw new ServiceException("删除Quartz调度任务失败：" + e.getMessage());
        }
    }
    @Override
    public long generateTaskCode(Long projectCode) {
        return System.currentTimeMillis() ^ (projectCode << 10);
    }

    private SysJob loadJob(ScheduleCommand command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("Quartz调度任务id不能为空");
        }
        // Handle task-related data and operations.
        SysJob job = sysJobService.selectJobById(command.getId());
        if (job == null) {
            throw new ServiceException("Quartz调度任务不存在：" + command.getId());
        }
        return job;
    }

    private SysJob toSysJob(ScheduleCommand command) {
        SysJob job = new SysJob();
        job.setJobName(command.getJobName());
        job.setJobGroup(command.getJobGroup());
        job.setInvokeTarget(command.getInvokeTarget());
        job.setCronExpression(command.getCronExpression());
        job.setMisfirePolicy(defaultIfBlank(command.getMisfirePolicy(), ScheduleConstants.MISFIRE_DEFAULT));
        job.setConcurrent(defaultIfBlank(command.getConcurrent(), DEFAULT_CONCURRENT));
        job.setExecutionType(command.getExecutionType());
        job.setRemark(command.getRemark());
        job.setStatus(command.getStatus());

        return job;
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }
}
