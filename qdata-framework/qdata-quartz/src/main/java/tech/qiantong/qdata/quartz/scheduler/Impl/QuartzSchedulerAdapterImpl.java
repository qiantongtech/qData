package tech.qiantong.qdata.quartz.scheduler.Impl;

import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.domain.ScheduleRespDTO;
import tech.qiantong.qdata.quartz.enums.JobErrorEnum;
import tech.qiantong.qdata.quartz.service.IQuartzJobService;

/**
 * Quartz scheduler adapter.
 * Converts common scheduling parameters from business modules into qData's existing Quartz SysJob calls.
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
        return checkJobResult(quartzJobService.insertJobReturnId(job), job, "create");
    }
    @Override
    public Long updateSchedule(ScheduleCommand command) throws SchedulerException, TaskException {
        QuartzJob job = toQuartzJob(command);
        job.setJobId(command.getId());
        return checkJobResult(quartzJobService.updateJobReturnId(job), job, "update");
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
            throw new ServiceException("sys.error.quartz.delete.fail", "Failed to delete Quartz scheduler job: {0}",
                    e.getMessage());
        }
    }
    @Override
    public long generateTaskCode(Long projectCode) {
        return System.currentTimeMillis() ^ (projectCode << 10);
    }

    private QuartzJob loadJob(ScheduleCommand command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException(MessageUtils.messageWithFallback(
                    "sys.error.quartz.id.empty", "Quartz scheduler job ID cannot be empty"));
        }
        QuartzJob job = quartzJobService.selectJobById(command.getId());
        if (job == null) {
            throw new ServiceException("sys.error.quartz.notfound", "Quartz scheduler job does not exist: {0}",
                    command.getId());
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
            throw localizedJobValidationException(error, job.getJobName(), operationType);
        }
        if (result == null || result <= 0) {
            String code = "create".equals(operationType)
                    ? "sys.error.quartz.create.fail" : "sys.error.quartz.update.fail";
            String fallback = "create".equals(operationType)
                    ? "Failed to create Quartz scheduler job: the job does not exist or data was not written"
                    : "Failed to update Quartz scheduler job: the job does not exist or data was not written";
            throw new ServiceException(code, fallback);
        }
        return result;
    }

    private ServiceException localizedJobValidationException(JobErrorEnum error, String jobName, String operationType) {
        String action = "create".equals(operationType) ? "create" : "update";
        String codeSuffix;
        String reason;
        switch (error) {
            case CRON_INVALID:
                codeSuffix = "cron.invalid";
                reason = "invalid Cron expression";
                break;
            case RMI_NOT_ALLOWED:
                codeSuffix = "rmi.denied";
                reason = "RMI calls are not allowed in the target string";
                break;
            case LDAP_NOT_ALLOWED:
                codeSuffix = "ldap.denied";
                reason = "LDAP(S) calls are not allowed in the target string";
                break;
            case HTTP_NOT_ALLOWED:
                codeSuffix = "http.denied";
                reason = "HTTP(S) calls are not allowed in the target string";
                break;
            case NOT_IN_WHITELIST:
                codeSuffix = "target.not.whitelisted";
                reason = "the target string is not in the whitelist";
                break;
            case INVALID_TARGET:
            default:
                codeSuffix = "target.invalid";
                reason = "the target string contains prohibited content";
                break;
        }
        String code = "sys.error.quartz.job." + action + "." + codeSuffix;
        String fallback = "Failed to " + action + " task ''{0}'': " + reason;
        return new ServiceException(code, fallback, jobName);
    }
}
