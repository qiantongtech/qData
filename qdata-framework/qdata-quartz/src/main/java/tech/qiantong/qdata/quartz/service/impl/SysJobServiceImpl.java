package tech.qiantong.qdata.quartz.service.impl;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.exception.job.TaskException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.enums.JobErrorEnum;
import tech.qiantong.qdata.quartz.mapper.SysJobMapper;
import tech.qiantong.qdata.quartz.service.ISysJobService;
import tech.qiantong.qdata.quartz.util.CronUtils;
import tech.qiantong.qdata.quartz.util.ScheduleUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 *
 * @author qdata
 */
@Service
public class SysJobServiceImpl implements ISysJobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysJobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     *
     * @param job 调度信息
     * @return
     */
    @Override
    public List<SysJob> selectJobList(SysJob job) {
        return jobMapper.selectJobList(job);
    }

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public SysJob selectJobById(Long jobId) {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            SysJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob job) throws SchedulerException {
        boolean result = false;
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        SysJob properties = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }

    /**
     * 新增任务
     *
     * @param job 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * 新增任务带有校验验证
     *
     * @param job 调度信息
     * @return 结果
     */
    @Override
    public Long insertJobReturnId(SysJob job) throws SchedulerException, TaskException {
        // 校验任务配置的合法性
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return job.getJobId();
    }

    /**
     * 更新任务的时间表达式
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob job) throws SchedulerException, TaskException {
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务带有校验验证
     *
     * @param job 调度信息
     * @return 结果 返回任务主键ID
     */
    @Override
    public Long updateJobReturnId(SysJob job) throws SchedulerException, TaskException {
        // 校验任务配置的合法性
        Long validationResult = validateJobConfig(job);
        if (validationResult != 0) {
            return validationResult;
        }
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return job.getJobId();
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    /**
     * 校验任务配置的合法性
     *
     * @param job 任务信息
     * @return 校验结果码，0表示校验通过
     */
    private Long validateJobConfig(SysJob job) {
        // 1. 校验Cron表达式
        if (!CronUtils.isValid(job.getCronExpression())) {
            return JobErrorEnum.CRON_INVALID.getCode();
        }

        // 2. 校验目标字符串
        String invokeTarget = job.getInvokeTarget();

        // 2.1 检查RMI调用
        if (StringUtils.containsIgnoreCase(invokeTarget, Constants.LOOKUP_RMI)) {
            return JobErrorEnum.RMI_NOT_ALLOWED.getCode();
        }

        // 2.2 检查LDAP调用
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return JobErrorEnum.LDAP_NOT_ALLOWED.getCode();
        }

        // 2.3 检查HTTP调用
        if (StringUtils.containsAnyIgnoreCase(invokeTarget,
                new String[]{Constants.HTTP, Constants.HTTPS})) {
            return JobErrorEnum.HTTP_NOT_ALLOWED.getCode();
        }

        // 2.4 检查违规字符串
        if (StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_ERROR_STR)) {
            return JobErrorEnum.INVALID_TARGET.getCode();
        }

//        // 2.5 检查白名单
//        if (!ScheduleUtils.whiteList(invokeTarget)) {
//            return JobErrorEnum.NOT_IN_WHITELIST.getCode();
//        }

        return 0L;
    }
}
