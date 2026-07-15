package tech.qiantong.qdata.quartz.scheduler.Impl;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;
import tech.qiantong.qdata.quartz.enums.JobErrorEnum;
import tech.qiantong.qdata.quartz.service.IQuartzJobService;

/**
 * Handle Quartz scheduling operations.
 * Handle Quartz scheduling operations.
 */
@Component
public class QuartzSchedulerAdapterImpl implements ISchedulerAdapter {

    private static final String DEFAULT_CONCURRENT = "1";

    private final IQuartzJobService quartzJobService;

    public QuartzSchedulerAdapterImpl(IQuartzJobService quartzJobService) {
        this.quartzJobService = quartzJobService;
    }
    @Override
    public ScheduleRespDTO selectScheduleById(ScheduleCommand command) {
        QuartzJob quartzJob = quartzJobService.selectJobById(command.getId());
        ScheduleRespDTO response = BeanUtils.toBean(quartzJob, ScheduleRespDTO.class);
        if (response != null) {
            response.setQuartzId(quartzJob.getJobId());
        }
        return response;
    }
    @Override
    public Long createSchedule(ScheduleCommand command) throws SchedulerException, TaskException {
        QuartzJob job = toQuartzJob(command);
        return checkJobResult(quartzJobService.insertJobReturnId(job), job, "创建");
    }
    @Override
    public Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException {
        QuartzJob job = toQuartzJob(command);
        job.setJobId(command.getId());
        return checkJobResult(quartzJobService.updateJobReturnId(job), job, "修改");
    }
    @Override
    public void online(ScheduleCommand command) throws SchedulerException {
        quartzJobService.resumeJob(loadJob(command));
    }
    @Override
    public void offline(ScheduleCommand command) throws SchedulerException {
        quartzJobService.pauseJob(loadJob(command));
    }
    @Override
    public void trigger(ScheduleCommand command) throws SchedulerException {
        quartzJobService.run(loadJob(command));
    }
    @Override
    public void delete(ScheduleCommand command) {
        try {
            quartzJobService.deleteJob(loadJob(command));
        } catch (SchedulerException e) {
            throw new ServiceException("删除Quartz调度任务失败：" + e.getMessage());
        }
    }
    @Override
    public long generateTaskCode(Long projectCode) {
        return System.currentTimeMillis() ^ (projectCode << 10);
    }

    private QuartzJob loadJob(ScheduleCommand command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("Quartz调度任务id不能为空");
        }
        QuartzJob job = quartzJobService.selectJobById(command.getId());
        if (job == null) {
            throw new ServiceException("Quartz调度任务不存在：" + command.getId());
        }
        return job;
    }

    private QuartzJob toQuartzJob(ScheduleCommand command) {
        QuartzJob job = new QuartzJob();
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

    private Long checkJobResult(Long result, QuartzJob job, String operationType) {
        JobErrorEnum error = JobErrorEnum.getByCode(result);
        if (error != null) {
            throw new ServiceException(error.getMessage(job.getJobName(), operationType));
        }
        if (result == null || result <= 0) {
            throw new ServiceException(operationType + "Quartz调度任务失败：任务不存在或数据未写入");
        }
        return result;
    }
}
