package tech.qiantong.qdata.module.mc.service.jobhandler;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.exception.ServiceException;

@Service
public class McTaskXxxJobService {

    // ================= XXL-Job 固定常量 =================

    // 调度类型
    private static final String SCHEDULE_TYPE_CRON = "CRON";

    // 路由策略
    private static final String ROUTE_STRATEGY_FIRST = "FIRST";

    // 阻塞策略
    private static final String BLOCK_STRATEGY_SERIAL = "SERIAL_EXECUTION";

    // 任务类型（Spring Bean）
    private static final String GLUE_TYPE_BEAN = "BEAN";

    // GLUE 备注
    private static final String GLUE_REMARK_INIT = "GLUE代码初始化";

    // 错过调度策略
    private static final String MISFIRE_STRATEGY_DO_NOTHING = "DO_NOTHING";

    // 执行器 Handler（你这个任务专用）
    private static final String EXECUTOR_HANDLER_DA_DISCOVERY = "runDaDiscoveryTask";

    // 执行控制
    private static final int EXECUTOR_TIMEOUT_DEFAULT = 0;        // 默认不超时
    private static final int EXECUTOR_FAIL_RETRY_DEFAULT = 0;     // 默认不重试


    // admin-client
    private static String addressUrl;
    private static String accessToken;
    private static int timeoutSecond;
    private static int appid;
    private static String appname;

    @Value("${xxl.job.executor.id}")
    private void setDefaultAppid(int appid) {
        this.appid = appid;
    }

    @Value("${xxl.job.executor.appname}")
    private void setDefaultAppname(String appname) {
        this.appname = appname;
    }

    @Value("${xxl.job.admin.addresses}")
    private void setDefaultAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    @Value("${xxl.job.admin.accessToken}")
    private void setDefaultAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Value("${xxl.job.admin.timeout}")
    private void setDefaulttTimeoutSecond(int timeoutSecond) {
        this.timeoutSecond = timeoutSecond;
    }


    public String addJob(String taskName, String cronExpression, String taskId) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);

        JobInfoParam addParam = new JobInfoParam();

        addParam.setExecutorParam(taskId);
        // ===== 执行器相关 =====
        addParam.setJobGroup(appid);                 // 执行器分组ID（对应XXL-Job控制台的执行器）
        addParam.setExecutorHandler(EXECUTOR_HANDLER_DA_DISCOVERY); // 执行器方法名（@XxlJob 注解的 value）

        // ===== 任务基本信息 =====
        addParam.setJobDesc(taskName);          // 任务描述（展示用）
        addParam.setAuthor(appname);         // 任务负责人（展示用）

        // ===== 调度配置 =====
        addParam.setScheduleType(SCHEDULE_TYPE_CRON);         // 调度类型：CRON / FIX_RATE / FIX_DELAY
        addParam.setScheduleConf(cronExpression);// 调度配置：CRON 表达式

        // ===== 执行策略 =====
        addParam.setExecutorRouteStrategy(ROUTE_STRATEGY_FIRST);        // 路由策略（FIRST / ROUND / RANDOM 等）
        addParam.setExecutorBlockStrategy(BLOCK_STRATEGY_SERIAL); // 阻塞策略（串行执行）

        // ===== 任务类型 =====
        addParam.setGlueType(GLUE_TYPE_BEAN);             // 任务类型（BEAN = Spring Bean）
        addParam.setGlueRemark(GLUE_REMARK_INIT); // GLUE备注（BEAN模式下仅说明）

        // ===== 执行控制 =====
        addParam.setExecutorTimeout(EXECUTOR_TIMEOUT_DEFAULT);           // 执行超时时间（秒，0=不限制）
        addParam.setExecutorFailRetryCount(EXECUTOR_FAIL_RETRY_DEFAULT);    // 失败重试次数
        addParam.setMisfireStrategy(MISFIRE_STRATEGY_DO_NOTHING);// 调度错过策略（DO_NOTHING / FIRE_ONCE_NOW）

        ReturnT<String> returnT = adminBiz.addJobInfo(addParam);
        if (returnT.getCode() == ReturnT.SUCCESS_CODE) {
            return returnT.getContent();
        } else {
            throw new ServiceException("新增调度信息失败！");
        }
    }


    public String updateJob(String taskName, String cronExpression, String taskId, String jobId) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);

        JobInfoParam updateParam = new JobInfoParam();

        updateParam.setExecutorParam(taskId);

        // ===== 必填 =====
        updateParam.setId(Integer.parseInt(jobId));                     // 任务ID（必须，决定修改哪一条任务）

        // ===== 可修改字段 =====
        updateParam.setJobGroup(appid);               // 执行器分组ID
        updateParam.setJobDesc(taskName);   // 任务描述
        updateParam.setAuthor(appname);      // 负责人

        // ===== 调度配置 =====
        updateParam.setScheduleType(SCHEDULE_TYPE_CRON);      // 调度类型
        updateParam.setScheduleConf(cronExpression); // 新的CRON表达式

        // ===== 执行器配置 =====
        updateParam.setExecutorHandler(EXECUTOR_HANDLER_DA_DISCOVERY); // 执行器方法名
        updateParam.setExecutorRouteStrategy(ROUTE_STRATEGY_FIRST);    // 路由策略
        updateParam.setExecutorBlockStrategy(BLOCK_STRATEGY_SERIAL); // 阻塞策略

        // ===== 任务类型 =====
        updateParam.setGlueType(GLUE_TYPE_BEAN);          // 任务类型
        updateParam.setGlueRemark(GLUE_REMARK_INIT);// GLUE备注

        // ===== 执行控制 =====
        updateParam.setExecutorTimeout(EXECUTOR_TIMEOUT_DEFAULT);           // 执行超时时间（秒，0=不限制）
        updateParam.setExecutorFailRetryCount(EXECUTOR_FAIL_RETRY_DEFAULT);    // 失败重试次数
        updateParam.setMisfireStrategy(MISFIRE_STRATEGY_DO_NOTHING);// 调度错过策略（DO_NOTHING / FIRE_ONCE_NOW）

        ReturnT<String> returnT = adminBiz.updateJobInfo(updateParam);
        if (returnT.getCode() == ReturnT.SUCCESS_CODE) {
            return returnT.getContent();
        } else {
            throw new ServiceException("修改调度信息失败！");
        }
    }


    public String startJob(String jobId) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);
        StartJobInfoParam startJobInfoParam = new StartJobInfoParam();
        startJobInfoParam.setJobId(Integer.parseInt(jobId));
        ReturnT<String> ret = adminBiz.startJobInfo(startJobInfoParam);

        if (ret.getCode() == ReturnT.SUCCESS_CODE) {
            return ret.getContent();
        } else {
            throw new ServiceException("开启调度信息失败！");
        }
    }

    public String stopJob(String jobId) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);
        StopJobInfoParam stopJobInfoParam = new StopJobInfoParam();
        stopJobInfoParam.setJobId(Integer.parseInt(jobId));
        ReturnT<String> ret = adminBiz.stopJobInfo(stopJobInfoParam);

        if (ret.getCode() == ReturnT.SUCCESS_CODE) {
            return ret.getContent();
        } else {
            throw new ServiceException("关闭调度信息失败！");
        }

    }


    public String removeJob(String jobId) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);
        DeleteJobInfoParam deleteJobInfoParam = new DeleteJobInfoParam();
        deleteJobInfoParam.setJobId(Integer.parseInt(jobId));
        ReturnT<String> ret = adminBiz.deleteJobInfo(deleteJobInfoParam);

        if (ret.getCode() == ReturnT.SUCCESS_CODE) {
            return ret.getContent();
        } else {
            throw new ServiceException("删除调度信息失败！");
        }

    }


    public String runJobOnce(String jobId,String executorParams) {
        AdminBiz adminBiz = new AdminBizClient(addressUrl, accessToken, timeoutSecond);
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(Integer.parseInt(jobId));
        triggerParam.setExecutorParams(executorParams);
        ReturnT<String> ret = adminBiz.triggerJobInfo(triggerParam);

        if (ret.getCode() == ReturnT.SUCCESS_CODE) {
            return ret.getContent();
        } else {
            throw new ServiceException("执行调度信息失败！");
        }
    }
}
