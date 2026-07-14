package tech.qiantong.qdata.module.mc.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskDefinition;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.utils.McTaskConverter;
import tech.qiantong.qdata.module.mc.utils.model.TaskSaveReqInput;

import javax.annotation.Resource;

/**
 * DolphinScheduler scheduler service
 * Used to manage the scheduling and execution of data collection tasks
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
public class McTaskDolphinSchedulerService {

    @Value("${ds.http_mc_projectCode:}")
    private Long projectCode;

    @Resource
    private IDsEtlTaskService dsEtlTaskService;

    @Resource
    private IDsEtlSchedulerService iDsEtlSchedulerService;

    @Resource
    private IDsEtlNodeService dsEtlNodeService;

    // ==================== DolphinScheduler core methods ====================

    /**
     * Create task process definition
     *
     * @param taskName task name
     * @param taskId task ID
     * @return task code
     */
    public String createTaskDefinition(String taskName, Long taskId) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(taskName + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", String.valueOf(taskId));
        input.setId(taskId);

        ProcessDefinition definition = this.createProcessDefinition(input);
        TaskDefinition firstTaskDefinition = McTaskConverter.getFirstTaskDefinition(definition);

        return String.valueOf(definition.getCode());
    }

    /**
     * Update task process definition
     *
     * @param taskName task name
     * @param taskId task ID
     * @param taskCode task code
     * @param nodeCode node code
     * @return task code
     */
    public String updateTaskDefinition(String taskName, Long taskId, String taskCode, String nodeCode) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(taskName + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", String.valueOf(taskId));
        input.setId(taskId);
        input.setTaskCode(taskCode);
        input.setNodeCode(nodeCode);

        ProcessDefinition definition = this.updateProcessDefinition(input);
        return String.valueOf(definition.getCode());
    }

    /**
     * Create scheduler
     *
     * @param taskCode task code
     * @param cronExpression cron expression
     * @return scheduler ID
     */
    public Long createScheduler(String taskCode, String cronExpression) {
        DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = McTaskConverter.createSchedulerRequest(
                cronExpression, taskCode);
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.saveScheduler(
                dsSchedulerSaveReqDTO, String.valueOf(projectCode));

        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            throw new ServiceException("mc.error.scheduler.create", "创建调度器失败！");
        }

        Schedule schedule = dsSchedulerRespDTO.getData();
        return schedule.getId();
    }

    /**
     * Update scheduler
     *
     * @param schedulerId scheduler ID
     * @param taskCode task code
     * @param cronExpression cron expression
     * @return scheduler ID
     */
    public Long updateScheduler(Long schedulerId, String taskCode, String cronExpression) {
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = McTaskConverter.createSchedulerUpdateRequest(
                schedulerId, cronExpression, taskCode);
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(
                schedulerUpdateRequest, String.valueOf(projectCode));

        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            throw new ServiceException("mc.error.scheduler.update", "更新调度器失败！");
        }

        Schedule schedule = dsSchedulerRespDTO.getData();
        return schedule.getId();
    }

    /**
     * Online tasks (online tasks alone, without operating the scheduler)
     *
     * @param taskCode task code
     */
    public void onlineTask(String taskCode) {
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE",
                String.valueOf(projectCode), taskCode);
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("mc.error.task.publish.fail", "发布任务失败！");
        }
    }

    /**
     * Offline scheduler (separate offline scheduler, does not operate tasks)
     *
     * @param schedulerId scheduler ID
     */
    public void offlineScheduler(String schedulerId) {
        DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(
                String.valueOf(projectCode), Long.parseLong(schedulerId));
        if (!offlined.getData()) {
            throw new ServiceException("mc.error.scheduler.offline", "下线调度器失败！");
        }
    }

    /**
     * Online scheduler (online scheduler alone, does not operate tasks)
     *
     * @param schedulerId scheduler ID
     */
    public void onlineSchedulerOnly(Long schedulerId) {
        DsStatusRespDTO dsStatusRespDTO = iDsEtlSchedulerService.onlineScheduler(
                String.valueOf(projectCode), schedulerId);
        if (!dsStatusRespDTO.getData()) {
            throw new ServiceException("mc.error.scheduler.online", "上线调度器失败！");
        }
    }

    /**
     * Offline scheduler (separate offline scheduler, does not operate tasks)
     *
     * @param schedulerId scheduler ID
     */
    public void offlineSchedulerOnly(Long schedulerId) {
        DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(
                String.valueOf(projectCode), schedulerId);
        if (!offlined.getData()) {
            throw new ServiceException("mc.error.scheduler.offline", "下线调度器失败！");
        }
    }

    /**
     * Online tasks and scheduler
     *
     * @param taskCode task code
     * @param schedulerId scheduler ID
     */
    public void onlineTaskAndScheduler(String taskCode, Long schedulerId) {
        // Online tasks
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE",
                String.valueOf(projectCode), taskCode);
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("mc.error.task.publish.fail", "发布任务失败！");
        }

        // Online scheduler
        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(
                String.valueOf(projectCode), schedulerId);
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("mc.error.scheduler.online", "上线调度器失败！");
        }
    }

    /**
     * Offline tasks and scheduler
     *
     * @param taskCode task code
     * @param schedulerId scheduler ID
     */
    public void offlineTaskAndScheduler(String taskCode, Long schedulerId) {
        // Offline tasks (will automatically handle scheduler offline)
        DsStatusRespDTO respDTO = dsEtlTaskService.releaseTask("OFFLINE",
                String.valueOf(projectCode), taskCode);
        if (respDTO == null || !respDTO.getSuccess()) {
            if (respDTO == null) log.error("respDTO is null");
            else log.error("respDTO={}", JSONUtils.toJson(respDTO));
            throw new ServiceException("mc.error.task.offline", "下线任务失败！");
        }

        // Additionally make sure the scheduler is also offline
        if (schedulerId != null && schedulerId > 0) {
            DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(
                    String.valueOf(projectCode), schedulerId);
            if (!offlined.getData()) {
                throw new ServiceException("mc.error.scheduler.offline", "下线调度器失败！");
            }
        }
    }

    /**
     * Delete task
     *
     * @param taskCode task code
     */
    public void deleteTask(String taskCode) {
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.deleteTask(
                String.valueOf(projectCode), taskCode);
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("mc.error.task.delete", "删除任务失败！");
        }
    }

    /**
     * Start the task (execute it once immediately)
     *
     * @param taskCode task code
     */
    public void startTask(String taskCode) {
        DsStartTaskReqDTO dsStartTaskReqDTO = McTaskConverter.createDsStartTaskReqDTO(taskCode);
        try {
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(
                    dsStartTaskReqDTO, String.valueOf(projectCode));
            if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                throw new ServiceException("mc.error.task.start", "启动任务失败：" + (dsStatusRespDTO != null ? dsStatusRespDTO.getMsg() : "未知错误"), dsStatusRespDTO != null ? dsStatusRespDTO.getMsg() : "未知错误");
            }
        } catch (Exception e) {
            throw new ServiceException("dpp.error.scheduler.start", "执行调度器，失败！");
        }
    }

    // ==================== Internal helper methods ====================

    /**
     * Create process definition
     */
    private ProcessDefinition createProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(projectCode);
        input.setNodeCode(McTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = McTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO,
                McTaskConverter.stringToLong(String.valueOf(projectCode)));

        if (!task.getSuccess()) {
            throw new ServiceException("mc.error.task.create", "创建任务定义失败，请联系系统管理员");
        }
        return task.getData();
    }

    /**
     * Update process definition
     */
    private ProcessDefinition updateProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(
                McTaskConverter.stringToLong(String.valueOf(projectCode)));
        input.setNodeCode(McTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = McTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO,
                String.valueOf(projectCode), input.getTaskCode());

        if (!task.getSuccess()) {
            throw new ServiceException("mc.error.task.update", "更新任务定义失败，请联系系统管理员");
        }
        return task.getData();
    }

    /**
     * Get the unique code of the node
     */
    private Long getNodeUniqueKey(Long projectCode) {
        try {
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(projectCode);
            return dsNodeGenCodeRespDTO.getData().get(0);
        } catch (Exception e) {
            log.error("生成节点编码失败", e);
            throw new ServiceException("mc.error.node.code", "生成节点编码失败，请联系系统管理员");
        }
    }
}
