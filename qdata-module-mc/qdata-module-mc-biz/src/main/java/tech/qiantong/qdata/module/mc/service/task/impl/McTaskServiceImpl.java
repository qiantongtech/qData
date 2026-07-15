package tech.qiantong.qdata.module.mc.service.task.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.HttpStatus;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.sourceSystem.dto.AttSourceSystemRespDTO;
import tech.qiantong.qdata.module.att.api.sourceSystem.service.IAttSourceSystemApiService;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.mc.api.service.task.IMcTaskApiService;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.*;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.*;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McColumnMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McDbMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.metadata.McTableMapper;
import tech.qiantong.qdata.module.mc.dal.mapper.task.McTaskMapper;
import tech.qiantong.qdata.module.mc.enums.CollectionScopeEnum;
import tech.qiantong.qdata.module.mc.enums.SchedulerStatusEnum;
import tech.qiantong.qdata.module.mc.service.columnLog.IMcColumnLogService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcColumnService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;
import tech.qiantong.qdata.module.mc.service.metadata.IMcTableService;
import tech.qiantong.qdata.module.mc.service.metadata.dialect.DatabaseDialect;
import tech.qiantong.qdata.module.mc.service.metadata.dialect.DatabaseDialectFactory;
import tech.qiantong.qdata.module.mc.service.scheduler.McTaskDolphinSchedulerService;
import tech.qiantong.qdata.module.mc.service.scheduler.McTaskQuartzService;
import tech.qiantong.qdata.module.mc.service.tableColumnRelLog.IMcTableColumnRelLogService;
import tech.qiantong.qdata.module.mc.service.tableLog.IMcTableLogService;
import tech.qiantong.qdata.module.mc.service.task.*;
import tech.qiantong.qdata.module.system.service.ISysUserService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskServiceImpl extends ServiceImpl<McTaskMapper, McTaskDO> implements IMcTaskService, IMcTaskApiService {
    @Resource
    private McTaskMapper mcTaskMapper;

    @Resource
    private IMcTaskSchedulerService mcTaskSchedulerService;
    @Resource
    private IMcTaskScopeService mcTaskScopeService;
    @Resource
    private IDaDatasourceApiService daDatasourceApiService;
    @Resource
    private IMcTaskInstanceService mcTaskInstanceService;
    @Resource
    private IMcTaskInstanceLogService mcTaskInstanceLogService;

    @Autowired
    @Lazy
    private DataSourceFactory dataSourceFactory;

    @Resource
    @Lazy
    private IRedisService redisService;

    // Implementation details.
    @Resource
    @Lazy
    private IMcDbService mcDbService;
    @Resource
    private IMcTableService mcTableService;
    @Resource
    private IMcColumnService mcColumnService;

    @Resource
    private McDbTxService mcDbTxService;
    @Resource
    private McTableTxService mcTableTxService;
    @Resource
    private McTaskDolphinSchedulerService mcTaskDolphinSchedulerService;
    @Resource
    private McTaskQuartzService mcTaskQuartzService;

    @Resource
    private IMcTableLogService mcTableLogService;
    @Resource
    private IMcColumnLogService mcColumnLogService;
    @Resource
    private IMcTableColumnRelLogService mcTableColumnRelLogService;
    @Resource
    private McDbMapper mcDbMapper;
    @Resource
    private McTableMapper mcTableMapper;
    @Resource
    private McColumnMapper mcColumnMapper;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private IAttSourceSystemApiService attSourceSystemApiService;


    @Override
    public boolean existsBySourceSystemName(String sourceSystemName) {
        return mcTaskMapper.existsBySourceSystemName(sourceSystemName);
    }


    @Override
    public PageResult<McTaskDO> getMcTaskPage(McTaskPageReqVO pageReqVO) {
        PageResult<McTaskDO> mcTaskDOPageResult = mcTaskMapper.selectPage(pageReqVO);

        List<McTaskDO> rows = mcTaskDOPageResult.getRows();

        if (CollectionUtils.isEmpty(rows)) {
            return mcTaskDOPageResult;
        }

        // Retrieve the required data.
        Map<Long, SysUser> userCache = Maps.newHashMap();
        for (McTaskDO row : rows) {
            // Create the required record.
            Long creatorId = row.getCreatorId();
            if (creatorId != null && !userCache.containsKey(creatorId)) {
                SysUser sysUser = sysUserService.selectUserById(creatorId);
                if (sysUser != null) {
                    userCache.put(creatorId, sysUser);
                }
            }
            SysUser creatorUser = userCache.get(creatorId);
            if (creatorUser != null) {
                row.setCreatePhoneNumber(creatorUser.getPhonenumber());
            }

            McTaskInstanceDO mcTaskInstanceByTaskId = mcTaskInstanceService.getMcTaskInstanceByTaskId(row.getId());
            if (mcTaskInstanceByTaskId != null) {
                row.setLastExecuteTime(DateUtil.format(mcTaskInstanceByTaskId.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }

        }
        mcTaskDOPageResult.setRows(rows);

        return mcTaskDOPageResult;
    }

    @Override
    public Long createMcTask(McTaskSaveReqVO createReqVO) {
        daDatasourceApiService.getDatabaseListByDatasourceId(createReqVO.getDatasourceId());
        // Handle task-related data and operations.
        validateDuplicateTask(createReqVO, null);

        McTaskDO dictType = BeanUtils.toBean(createReqVO, McTaskDO.class);
        // Implementation details.
        if (StringUtils.isEmpty(dictType.getScheduler())) {
            dictType.setScheduler(ScheduleConstants.DOLPHINSCHEDULER);
        }
        if (StringUtils.isEmpty(dictType.getStatus())) {
            dictType.setStatus(SchedulerStatusEnum.DISABLED.getValue());
        }
        mcTaskMapper.insert(dictType);
        Long id = dictType.getId();

        String taskCode;
        Long schedulerId;
        // Handle Quartz scheduling operations.
        if (ScheduleConstants.QUARTZ.equals(dictType.getScheduler())) {
            schedulerId = mcTaskQuartzService.createSchedulerQuartz(dictType);
            taskCode = String.valueOf(schedulerId);
        } else {
            // Handle DolphinScheduler operations.
            taskCode = mcTaskDolphinSchedulerService.createTaskDefinition(dictType.getName(), id);
            // Handle DolphinScheduler operations.
            mcTaskDolphinSchedulerService.onlineTask(taskCode);
            // Create the scheduler.
            schedulerId = mcTaskDolphinSchedulerService.createScheduler(taskCode, dictType.getCronExpression());
        }

        // Handle scheduling configuration and operations.
        McTaskSchedulerSaveReqVO schedulerSaveReqVO = new McTaskSchedulerSaveReqVO(dictType);
        schedulerSaveReqVO.setJobId(String.valueOf(schedulerId));
        schedulerSaveReqVO.setTaskCode(taskCode);  // Handle task-related data and operations.
        // Handle Quartz and DataX task execution.
        schedulerSaveReqVO.setTaskScheduler(dictType.getScheduler());
        schedulerSaveReqVO.setStatus(SchedulerStatusEnum.DISABLED.getValue());
        mcTaskSchedulerService.createMcTaskScheduler(schedulerSaveReqVO);

        if (StringUtils.equals("1", createReqVO.getCollectionScope())) {
            List<McTaskScopeSaveReqVO> scopeSaveReqVOS = createReqVO.getScopeSaveReqVOS();
            for (McTaskScopeSaveReqVO scopeSaveReqVO : scopeSaveReqVOS) {
                scopeSaveReqVO.setTaskId(id);
                mcTaskScopeService.createMcTaskScope(scopeSaveReqVO);
            }
        }

        return dictType.getId();
    }

    @Override
    public int updateMcTask(McTaskSaveReqVO updateReqVO) {
        // Handle task-related data and operations.
        validateDuplicateTask(updateReqVO, updateReqVO.getId());

        // Handle task-related data and operations.
        McTaskDO updateObj = BeanUtils.toBean(updateReqVO, McTaskDO.class);
        // Implementation details.
        if (StringUtils.isEmpty(updateObj.getScheduler())) {
            updateObj.setScheduler(ScheduleConstants.DOLPHINSCHEDULER);
        }
        int rows = mcTaskMapper.updateById(updateObj);

        // Handle scheduling configuration and operations.
        McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(updateObj.getId());
        if (scheduler != null) {
            // Update the related record.
            McTaskSchedulerSaveReqVO schedulerSaveReqVO = new McTaskSchedulerSaveReqVO();
            schedulerSaveReqVO.setId(scheduler.getId());

            boolean needUpdate = false;

            // Implementation details.
            String cronExpression = updateReqVO.getCronExpression();
            if (StringUtils.isNotEmpty(cronExpression) && !StringUtils.equals(cronExpression, scheduler.getCronExpression())) {
                schedulerSaveReqVO.setCronExpression(cronExpression);

                // Handle task-related data and operations.
                String taskCode = scheduler.getTaskCode();

                if (ScheduleConstants.QUARTZ.equals(scheduler.getTaskScheduler())) {
                    mcTaskQuartzService.updateScheduleQuartz(updateObj, scheduler, cronExpression);
                } else if (StringUtils.isNotEmpty(taskCode)) {
                    Long newSchedulerId = mcTaskDolphinSchedulerService.updateScheduler(
                            Long.parseLong(scheduler.getJobId()), taskCode, cronExpression);
                    schedulerSaveReqVO.setJobId(String.valueOf(newSchedulerId));
                }
                needUpdate = true;
            }

            // Handle scheduling configuration and operations.
            String schedulerStatus = updateReqVO.getSchedulerStatus();
            if (StringUtils.isNotEmpty(schedulerStatus) && !StringUtils.equals(schedulerStatus, scheduler.getStatus())) {
                schedulerSaveReqVO.setStatus(schedulerStatus);
                needUpdate = true;
            }

            // Implementation details.
            if (needUpdate) {
                mcTaskSchedulerService.updateMcTaskScheduler(schedulerSaveReqVO);
            }

        }

        if (StringUtils.equals("1", updateReqVO.getCollectionScope())) {
            // Delete the related record.
            mcTaskScopeService.removeMcTaskScopeBytaskId(updateObj.getId());
            // Implementation details.
            List<McTaskScopeSaveReqVO> scopeSaveReqVOS = updateReqVO.getScopeSaveReqVOS();
            for (McTaskScopeSaveReqVO scopeSaveReqVO : scopeSaveReqVOS) {
                scopeSaveReqVO.setId(null);
                scopeSaveReqVO.setTaskId(updateObj.getId());
                mcTaskScopeService.createMcTaskScope(scopeSaveReqVO);
            }
        }
        return rows;
    }

    @Override
    public int removeMcTask(Collection<Long> idList) {
        for (Long id : idList) {
            McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(id);
            McTaskDO task = mcTaskMapper.selectById(id);

            // Handle task-related data and operations.
            if (task != null && scheduler != null && StringUtils.isNotEmpty(scheduler.getTaskCode())) {
                if (ScheduleConstants.QUARTZ.equals(scheduler.getTaskScheduler())) {
                    Long jobId = Long.valueOf(scheduler.getJobId());
                    mcTaskQuartzService.offlineSchedulerOnlyQuartz(jobId);
                    mcTaskQuartzService.deleteSchedulerQuartz(jobId);
                } else {
                    try {
                        Long schedulerId = StringUtils.isNotEmpty(scheduler.getJobId()) ?
                                Long.parseLong(scheduler.getJobId()) : null;
                        mcTaskDolphinSchedulerService.offlineTaskAndScheduler(scheduler.getTaskCode(), schedulerId);
                    } catch (Exception e) {
                        log.warn("Failed to take the task offline, taskId={}", id, e);
                    }

                    try {
                        mcTaskDolphinSchedulerService.deleteTask(scheduler.getTaskCode());
                    } catch (Exception e) {
                        log.warn("Failed to delete the DolphinScheduler task, taskId={}", id, e);
                    }
                }
            }
        }
        // Handle task-related data and operations.
        return mcTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list) {
        int cannotDeleteCount = 0;
        List<Long> canDeleteIds = new ArrayList<>();
        for (Long id : list) {
            McTaskRespVO mcTaskByIdNew = this.getMcTaskByIdNew(id);
            if (StringUtils.equals("1", mcTaskByIdNew.getStatus())
                    || StringUtils.equals("1", mcTaskByIdNew.getSchedulerStatus())) {
                cannotDeleteCount++;
                continue;
            }
            canDeleteIds.add(id);
        }
        return new BatchDeleteCheck<>(cannotDeleteCount, canDeleteIds);
    }


    @Override
    public McTaskDO getMcTaskById(Long id) {
        MPJLambdaWrapper<McTaskDO> lambdaWrapper = new MPJLambdaWrapper();

        lambdaWrapper.selectAll(McTaskDO.class)
                .select("t5.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t5 ON t.LEADER = t5.USER_ID AND t5.DEL_FLAG = '0'")
                .eq(McTaskDO::getId, id);

        return mcTaskMapper.selectOne(lambdaWrapper);
    }

    @Override
    public McTaskRespVO getMcTaskByIdNew(Long id) {
        McTaskRespVO bean = BeanUtils.toBean(this.getMcTaskById(id), McTaskRespVO.class);

        McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(id);
        if (scheduler != null) {
            bean.setCronExpression(scheduler.getCronExpression());
            bean.setSchedulerStatus(scheduler.getStatus());
            bean.setJobId(scheduler.getJobId());
            bean.setTaskCode(scheduler.getTaskCode());  // Handle scheduling configuration and operations.
        }

        List<McTaskScopeDO> mcTaskScopeDOS = mcTaskScopeService.getMcTaskScopeListBytaskId(id);
        bean.setScopeSaveReqVOS(mcTaskScopeDOS);

        DaDatasourceRespDTO mcDatasourceById = daDatasourceApiService.getDatasourceById(bean.getDatasourceId());
        bean.setDatasourceDO(mcDatasourceById);

        McTaskInstanceDO mcTaskInstanceByTaskId = mcTaskInstanceService.getMcTaskInstanceByTaskId(id);
        if (mcTaskInstanceByTaskId != null) {
            bean.setLastExecuteTime(DateUtil.format(mcTaskInstanceByTaskId.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        return bean;
    }

    @Override
    public List<McTaskDO> getMcTaskList() {
        return mcTaskMapper.selectList();
    }

    @Override
    public Map<Long, McTaskDO> getMcTaskMap() {
        List<McTaskDO> mcTaskList = mcTaskMapper.selectList();
        return mcTaskList.stream().collect(Collectors.toMap(McTaskDO::getId, mcTaskDO -> mcTaskDO,
                // Implementation details.
                (existing, replacement) -> existing));
    }


    /**
     * Handle task-related data and operations.
     *
     * @param importExcelList parameter value
     * @param isUpdateSupport parameter value
     * @param operName parameter value
     * @return the operation result
     */
    @Override
    public String importMcTask(List<McTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("mc.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (McTaskRespVO respVO : importExcelList) {
            try {
                McTaskDO mcTaskDO = BeanUtils.toBean(respVO, McTaskDO.class);
                Long mcTaskId = respVO.getId();
                if (isUpdateSupport) {
                    if (mcTaskId != null) {
                        McTaskDO existingMcTask = mcTaskMapper.selectById(mcTaskId);
                        if (existingMcTask != null) {
                            mcTaskMapper.updateById(mcTaskDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("mc.import.update.success",
                                    "数据更新成功，ID为 " + mcTaskId + " 的采集任务记录。", mcTaskId, "采集任务"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.fail",
                                    "数据更新失败，ID为 " + mcTaskId + " 的采集任务记录不存在。", mcTaskId, "采集任务"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<McTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskId);
                    McTaskDO existingMcTask = mcTaskMapper.selectOne(queryWrapper);
                    if (existingMcTask == null) {
                        mcTaskMapper.insert(mcTaskDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("mc.import.insert.success",
                                "数据插入成功，ID为 " + mcTaskId + " 的采集任务记录。", mcTaskId, "采集任务"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.insert.fail",
                                "数据插入失败，ID为 " + mcTaskId + " 的采集任务记录已存在。", mcTaskId, "采集任务"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("mc.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("mc.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("mc.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("mc.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public List<McTaskScopeDO> getRealtimeMcTaskScopeList(Long id) {
        List<DbName> dbNameList = daDatasourceApiService.getDatabaseListByDatasourceId(id);
        List<McTaskScopeDO> taskScopeDOList = new ArrayList<>();

        if (CollectionUtils.isEmpty(dbNameList)) {
            return taskScopeDOList;
        }

        for (DbName dbName : dbNameList) {
            List<DbName> children = dbName.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (DbName child : children) {
                    McTaskScopeDO mcTaskScopeDO = new McTaskScopeDO();
                    mcTaskScopeDO.setDbName(dbName.getDbName());
                    mcTaskScopeDO.setSchemaName(child.getDbName());
                    taskScopeDOList.add(mcTaskScopeDO);
                }
            } else {
                McTaskScopeDO mcTaskScopeDO = new McTaskScopeDO();
                mcTaskScopeDO.setDbName(dbName.getDbName());
                taskScopeDOList.add(mcTaskScopeDO);
            }
        }
        return taskScopeDOList;
    }

    @Override
    public Map<String, Object> updateReleaseJobTask(McTaskSaveReqVO mcTask) {
        McTaskRespVO mcTaskByIdNew = this.getMcTaskByIdNew(mcTask.getId());
        String status = mcTaskByIdNew.getStatus();
        if (StringUtils.equals(status, mcTask.getStatus())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("0", mcTask.getStatus()) && StringUtils.equals("1", mcTask.getSchedulerStatus())) {
            throw new ServiceException("请先将调度下线！");
        }

        McTaskDO updateObj = new McTaskDO();
        updateObj.setId(mcTask.getId());
        updateObj.setStatus(mcTask.getStatus());

        int rows = mcTaskMapper.updateById(updateObj);

        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return result;
    }

    @Override
    public Map<String, Object> updateReleaseSchedule(McTaskSaveReqVO mcTask) {
        McTaskRespVO mcTaskByIdNew = this.getMcTaskByIdNew(mcTask.getId());
        String schedulerStatus = mcTaskByIdNew.getSchedulerStatus();
        if (StringUtils.equals(schedulerStatus, mcTask.getStatus())) {
            return new HashMap<>();
        }

        // Handle scheduling configuration and operations.
        McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(mcTask.getId());

        if (scheduler != null && StringUtils.isNotEmpty(scheduler.getTaskCode())) {
            Long schedulerId = StringUtils.isNotEmpty(scheduler.getJobId()) ?
                    Long.parseLong(scheduler.getJobId()) : null;

            // Handle scheduling configuration and operations.
            if (SchedulerStatusEnum.isDisabled(mcTask.getStatus())) {
                if (ScheduleConstants.QUARTZ.equals(scheduler.getTaskScheduler())) {
                    mcTaskQuartzService.offlineSchedulerOnlyQuartz(schedulerId);
                } else
                    mcTaskDolphinSchedulerService.offlineSchedulerOnly(schedulerId);
            }

            // Handle scheduling configuration and operations.
            if (SchedulerStatusEnum.isEnabled(mcTask.getStatus())) {
                if (ScheduleConstants.QUARTZ.equals(scheduler.getTaskScheduler())) {
                    mcTaskQuartzService.onlineSchedulerOnlyQuartz(schedulerId);
                } else
                    mcTaskDolphinSchedulerService.onlineSchedulerOnly(schedulerId);
            }
        }

        McTaskSchedulerSaveReqVO updateReqVO = new McTaskSchedulerSaveReqVO();
        updateReqVO.setTaskId(mcTask.getId());
        updateReqVO.setStatus(mcTask.getStatus());
        mcTaskSchedulerService.updateReleaseSchedule(updateReqVO);

        return new HashMap<>();
    }

    @Override
    public Map<String, Object> runJobOnce(McTaskSaveReqVO mcTask) {
        String redisKey = buildRunLockKey(mcTask.getId());
        if (!checkTaskRunLock(redisKey)) {
            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
        }
        // Implementation details.
        redisService.set(redisKey + ":creatorId", mcTask.getCreatorId().toString(), 60 * 60 * 12);
        redisService.set(redisKey + ":createBy", mcTask.getCreateBy().toString(), 60 * 60 * 12);

        McTaskRespVO mcTaskByIdNew = this.getMcTaskByIdNew(mcTask.getId());

        // Handle DolphinScheduler operations.
        if (mcTaskByIdNew != null) {
            // Handle scheduling configuration and operations.
            McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(mcTask.getId());
            if (scheduler != null && StringUtils.isNotEmpty(scheduler.getTaskCode())) {
                if (ScheduleConstants.QUARTZ.equals(scheduler.getTaskScheduler())) {
                    mcTaskQuartzService.startTaskQuartz(Long.valueOf(scheduler.getJobId()));
                } else {
                    mcTaskDolphinSchedulerService.startTask(scheduler.getTaskCode());
                }
            }
        }

        return new HashMap<>();
    }

    @Override
    public boolean runDaDiscoveryTask(Long taskId) {

        String redisKey = buildRunLockKey(taskId);
        if (!acquireRunLock(redisKey)) {
            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
        }

        McTaskRespVO task = loadTask(taskId);

        McTaskInstanceDO instance = createTaskInstance(task);
        Long instanceId = instance.getId();

        // Handle execution logging.
        safeLog(instanceId, taskId, "任务开始执行");

        try {
            TableProcessResult tableProcessResult = executeTaskSafely(task, instance);

            if (tableProcessResult != null) {
                Long addCount = tableProcessResult.getAddCount();
                Long delCount = tableProcessResult.getDelCount();
                Long updateCount = tableProcessResult.getUpdateCount();
                instance.setAddCount(addCount);
                instance.setDelCount(delCount);
                instance.setUpdateCount(updateCount);
                instance.setTotalCount(tableProcessResult.getTotalCount());
                instance.setSuccessCount(tableProcessResult.getSuccessCount());
                instance.setFailCount(tableProcessResult.getTotalCount() - tableProcessResult.getSuccessCount());

                if (StringUtils.equals("2", task.getCollectType())) {
                    if ((addCount != null && addCount != 0L)
                            || (delCount != null && delCount != 0L)
                            || (updateCount != null && updateCount != 0L)) {
                        instance.setValidFlag(Boolean.TRUE);
                    }
                }
            }
            markSuccess(instance);
            safeLog(instanceId, taskId, "任务执行成功");
            return true;
        } catch (Exception e) {
            redisService.delete(redisKey);
            markFail(instance, e);
            safeLog(instanceId, taskId, "任务执行失败：" + e.getMessage());
            return false;
        } finally {
            safeLog(instanceId, taskId, String.format("任务执行完成汇总：表总数=%d，成功表=%d，失败表=%d，耗时=%d秒", instance.getTotalCount(), instance.getSuccessCount(), instance.getFailCount(), instance.getDuration()));
            finalizeTask(redisKey, instance);
        }
    }

    private String buildRunLockKey(Long taskId) {
        // Implementation details.
        return "mc:task:run:" + taskId;
    }

    private McTaskRespVO loadTask(Long taskId) {
        McTaskRespVO task = this.getMcTaskByIdNew(taskId);
        if (task == null) {
            throw new DataQueryException("采集任务不存在，taskId=" + taskId);
        }
        return task;
    }

    private boolean checkTaskRunLock(String redisKey) {
        String status = redisService.get(redisKey);
        if (StringUtils.isNotBlank(status) && "1".equals(status)) {
            return false;
        }
        return true;
    }

    private boolean acquireRunLock(String redisKey) {
        String status = redisService.get(redisKey);
        if (StringUtils.isNotBlank(status) && "1".equals(status)) {
            return false;
        }
        redisService.set(redisKey, "1", 60 * 60 * 12);
        return true;
    }

    private McTaskInstanceDO createTaskInstance(McTaskRespVO task) {
        McTaskInstanceDO instance = McTaskInstanceDO.builder()
                .taskId(task.getId())
                .sourceSystemId(task.getSourceSystemId())
                .sourceSystemName(task.getSourceSystemName())
                .collectionMode(task.getCollectionMode())
                .collectionScope(task.getCollectionScope())
                .status("1")
                .successCount(0L)
                .failCount(0L)
                .totalCount(0L)
                .addCount(0L)
                .delCount(0L)
                .updateCount(0L)
                .startTime(new Date())
                .validFlag(Boolean.TRUE)
                .delFlag(Boolean.FALSE)
                .build();
        String creatorId = redisService.get(buildRunLockKey(task.getId()) + ":creatorId");
        String createBy = redisService.get(buildRunLockKey(task.getId()) + ":createBy");
        instance.setCreateBy(StringUtils.isNotEmpty(createBy) ? createBy : "System Collection Task");
        instance.setCreatorId(StringUtils.isNotEmpty(creatorId) ? Long.parseLong(creatorId) : 1L);

        String collectType = task.getCollectType();
        if (StringUtils.equals("2", collectType)) {
            instance.setValidFlag(Boolean.FALSE);
        }

        Long mcTaskInstance = mcTableTxService.runInNewTx(() -> mcTaskInstanceService.createMcTaskInstance(instance));
//        Long mcTaskInstance = mcTaskInstanceService.createMcTaskInstance(instance);
        instance.setId(mcTaskInstance);
        return instance;
    }

    private void safeLog(Long instanceId, Long taskId, String msg) {
        try {
            mcTaskInstanceLogService.taskInstanceLogAppend(instanceId, taskId, msg);
        } catch (Exception e) {
            // Handle execution logging.
            log.error("Failed to write the task instance log, instanceId={}, msg={}", instanceId, msg, e);
        }
    }


    private void markSuccess(McTaskInstanceDO instance) {
        instance.setStatus("9");
        instance.setEndTime(new Date());
        instance.setDuration((instance.getEndTime().getTime() - instance.getStartTime().getTime()) / 1000);
        mcTaskInstanceService.updateById(instance);
    }


    private void markFail(McTaskInstanceDO instance, Exception e) {
        instance.setStatus("2");
        instance.setFailCause(e.getMessage());
        instance.setEndTime(new Date());
        instance.setDuration((instance.getEndTime().getTime() - instance.getStartTime().getTime()) / 1000);
        mcTaskInstanceService.updateById(instance);
    }

    private void finalizeTask(String redisKey, McTaskInstanceDO instance) {
        safeLog(instance.getId(), instance.getTaskId(), "FINALIZE_SESSION");
        safeLog(instance.getId(), instance.getTaskId(), "任务结束");
        redisService.set(redisKey, "2", 300);
    }

    private DaDatasourceRespDTO prepareDatasource(McTaskRespVO task, McTaskInstanceDO instance) {

        Long taskId = task.getId();
        Long instanceId = instance.getId();

        safeLog(instanceId, taskId, "任务执行-开始获取数据源信息");

        Long datasourceId = task.getDatasourceId();
        if (datasourceId == null) {
            safeLog(instanceId, taskId, "任务执行-数据源ID为空，无法继续");
            throw new DataQueryException("数据源ID为空");
        }

        DaDatasourceRespDTO datasource;
        try {
            datasource = daDatasourceApiService.getDatasourceById(datasourceId);
        } catch (Exception e) {
            safeLog(instanceId, taskId, "任务执行-获取数据源信息异常：" + e.getMessage());
            throw e;
        }

        if (datasource == null) {
            safeLog(instanceId, taskId, "任务执行-数据源详情信息查询失败，datasourceId=" + datasourceId);
            throw new DataQueryException("数据源详情信息查询失败");
        }

        safeLog(instanceId, taskId, "任务执行-获取数据源信息成功，datasourceId=" + datasourceId);
        return datasource;
    }

    public static boolean isInBlacklist(String input, String blacklist) {
        if (StringUtils.isBlank(blacklist) || StringUtils.isBlank(input)) {
            return false;
        }

        String[] list = blacklist.split(",");

        for (String item : list) {
            String black = item.trim();
            if (input.equalsIgnoreCase(black)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implementation details.
     *
     * @param task
     * @param instance
     */
    private TableProcessResult executeTaskSafely(McTaskRespVO task, McTaskInstanceDO instance) {

        safeLog(instance.getId(), task.getId(), "开始准备数据源");

        DaDatasourceRespDTO datasource = prepareDatasource(task, instance);

        safeLog(instance.getId(), task.getId(), "开始加载数据源连接");


        Long instanceId = instance.getId();
        Long taskId = task.getId();

        String scopeDesc;
        if ("1".equals(task.getCollectionScope())) {
            scopeDesc = "自定义库";
        } else if ("2".equals(task.getCollectionScope())) {
            scopeDesc = "整个数据源";
        } else {
            scopeDesc = "未知类型(" + task.getCollectionScope() + ")";
        }
        safeLog(instanceId, taskId, "开始解析采集范围，采集范围类型=" + scopeDesc);

        safeLog(instanceId, taskId, String.format("任务执行参数汇总：采集范围=%s，数据源ID=%s，来源系统=%s(%s)", scopeDesc, task.getDatasourceId(), task.getSourceSystemName(), task.getSourceSystemId()));

        // Retrieve the required data.
        List<McTaskScopeDO> databaseScopes;
        if ("2".equalsIgnoreCase(task.getCollectionScope())) {
            // Handle database and data source configuration.
            databaseScopes = loadDatabaseScopesFromDatasource(task, instance, datasource);
        } else {
            // Handle task-related data and operations.
            databaseScopes = loadDatabaseScopesFromTask(task, instance);
        }
        safeLog(instanceId, taskId, "数据库范围解析完成，共需处理数据库数量：" + databaseScopes.size());

        if (CollectionUtils.isEmpty(databaseScopes)) {
            safeLog(instanceId, taskId, "未获取到任何数据库范围，任务结束");
            return null;
        }

        // Delete the related record.
        List<McDbSaveReqVO> dbReqDTOList = compareAndRecordDatabaseScope(task, instance, databaseScopes, datasource);

        List<McDbRespVO> mcDbByTaskId = mcDbService.getMcDbByTaskId(taskId);


        Long addCount = 0L;
        Long delCount = 0L;
        Long updateCount = 0L;

        Long totalCount = 0L;
        Long successCount = 0L;
        List<Long> updateTableIds = new ArrayList<>();
        String blacklist = task.getBlacklist();
        safeLog(instanceId, taskId,
                String.format("【任务配置】数据库/表黑名单：%s",
                        StringUtils.defaultIfBlank(blacklist, "空")));

        int dbIndex = 1;
        // Implementation details.
        for (McDbSaveReqVO dbScope : dbReqDTOList) {
            String dbName = dbScope.getDbName();
            safeLog(instanceId, taskId, String.format("【数据库 %d/%d】开始处理：db=%s%s", dbIndex++, databaseScopes.size(), dbName, StringUtils.isNotBlank(dbScope.getSchemaName()) ? ", schema=" + dbScope.getSchemaName() : ""));

            // Implementation details.
            if (StringUtils.isNotEmpty(blacklist)) {
                if (isInBlacklist(dbName, blacklist)) {
                    safeLog(instanceId, taskId,
                            String.format("【数据库跳过】db=%s 命中黑名单：%s",
                                    dbName,
                                    StringUtils.defaultIfBlank(blacklist, "空")));
                    continue;
                }
                safeLog(instanceId, taskId,
                        String.format("【数据库通过】db=%s 未命中黑名单%s",
                                dbName,
                                StringUtils.isBlank(blacklist) ? "（黑名单为空）" : ""));
            }

            McDbRespVO matchedDb = findMatchedDb(dbScope, datasource, mcDbByTaskId);
            if (matchedDb == null) {
                Long mcDbId = mcDbTxService.createDbAndCommit(dbScope);
                dbScope.setId(mcDbId);
            } else {
                dbScope.setId(matchedDb.getId());
            }

            DbQueryContext dbQuery = createDbQueryForScope(datasource, dbScope, task, instance);
            try {
                TableProcessResult tableProcessResult = executeSingleDatabase(dbQuery, task, instance, dbScope, datasource);

                if (tableProcessResult != null) {
                    addCount = addCount + tableProcessResult.getAddCount();
                    updateCount = updateCount + tableProcessResult.getUpdateCount();
                    delCount = delCount + tableProcessResult.getDelCount();
                    totalCount = totalCount + tableProcessResult.getTotalCount();
                    successCount = successCount + tableProcessResult.getSuccessCount();
                    if (tableProcessResult.getUpdateTableIds().size() > 0) {
                        updateTableIds.addAll(tableProcessResult.getUpdateTableIds());
                    }
                }
            } finally {
                closeDbQuerySafely(dbQuery, task, instance, dbScope);
            }
        }

        // Update the related record.
        mcTableTxService.runInNewTx(() -> this.updateResponsibleInfoForMetadata(task));

        //
        List<McDbRespVO> dbsOnlyInResp = findDbsOnlyInResp(databaseScopes, datasource, mcDbByTaskId);
        if (CollectionUtils.isNotEmpty(dbsOnlyInResp)) {

            if (StringUtils.isNotEmpty(blacklist)) {
                dbsOnlyInResp.removeIf(db -> {
                    String dbName = db.getDbName();
                    boolean hit = isInBlacklist(dbName, blacklist);
                    if (hit) {
                        safeLog(instanceId, taskId,
                                String.format("【RESP库删除】db=%s 命中黑名单：%s",
                                        dbName,
                                        StringUtils.defaultIfBlank(blacklist, "空")));
                    }
                    return hit;
                });
            }

            List<Long> collect = mcDbByTaskId.stream().map(a -> a.getId()).collect(Collectors.toList());

            List<McTableRespVO> mcTableByDbId = mcTableService.getMcTableByDbId(collect);
            if (CollectionUtils.isNotEmpty(mcTableByDbId)) {
                delCount = delCount + mcTableByDbId.size();
            }

            List<Long> tableIds = mcTableByDbId.stream().map(a -> a.getId()).collect(Collectors.toList());
            mcTableService.removeMcTable(tableIds);

            mcDbService.removeMcDb(collect);
        }
        return new TableProcessResult(addCount, delCount, updateCount, totalCount, successCount, updateTableIds);

    }

    private Void updateResponsibleInfoForMetadata(McTaskRespVO task) {
        Long leader = task.getLeader();
        Long dept = task.getResponsibleDept();
        LambdaUpdateWrapper<McDbDO> updateDbWrapper = new LambdaUpdateWrapper<>();
        updateDbWrapper.eq(McDbDO::getTaskId, task.getId());

        LambdaUpdateWrapper<McTableDO> updateTbWrapper = new LambdaUpdateWrapper<>();
        updateTbWrapper.eq(McTableDO::getTaskId, task.getId());

        LambdaUpdateWrapper<McColumnDO> updateColWrapper = new LambdaUpdateWrapper<>();
        updateColWrapper.eq(McColumnDO::getTaskId, task.getId());

        boolean hasUpdate = false;
        if (leader != null) {
            updateDbWrapper.set(McDbDO::getBusinessLeader, leader);
            updateTbWrapper.set(McTableDO::getBusinessLeader, leader);
            updateColWrapper.set(McColumnDO::getBusinessLeader, leader);
            hasUpdate = true;
        }
        if (dept != null) {
            updateDbWrapper.set(McDbDO::getResponsibleDept, dept);
            updateTbWrapper.set(McTableDO::getResponsibleDept, dept);
            updateColWrapper.set(McColumnDO::getResponsibleDept, dept);
            hasUpdate = true;
        }

        if (hasUpdate) {
            mcDbMapper.update(null, updateDbWrapper);
            mcTableMapper.update(null, updateTbWrapper);
            mcColumnMapper.update(null, updateColWrapper);
        }
        return null;
    }

    private List<McDbRespVO> findDbsOnlyInResp(List<McTaskScopeDO> databaseScopes, DaDatasourceRespDTO datasource, List<McDbRespVO> mcDbByTaskId) {

        List<McDbRespVO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(mcDbByTaskId)) {
            return result;
        }

        for (McDbRespVO resp : mcDbByTaskId) {
            boolean exists = false;

            if (CollectionUtils.isNotEmpty(databaseScopes)) {
                for (McTaskScopeDO scope : databaseScopes) {
                    if (Objects.equals(resp.getIp(), datasource.getIp()) && Objects.equals(resp.getPort(), datasource.getPort() == null ? null : datasource.getPort()
                                                                                                                                                 .intValue()) && Objects.equals(resp.getDatasourceConfig(), datasource.getDatasourceConfig()) && Objects.equals(resp.getDbType(), datasource.getDatasourceType()) && Objects.equals(resp.getDbName(), scope.getDbName()) && Objects.equals(resp.getSchemaName(), scope.getSchemaName())) {
                        exists = true;
                        break;
                    }
                }
            }

            if (!exists) {
                result.add(resp);
            }
        }
        return result;
    }

    private McDbRespVO findMatchedDb(McDbSaveReqVO dbScope, DaDatasourceRespDTO datasource, List<McDbRespVO> mcDbByTaskId) {

        if (dbScope == null || CollectionUtils.isEmpty(mcDbByTaskId)) {
            return null;
        }

        for (McDbRespVO resp : mcDbByTaskId) {
            if (Objects.equals(resp.getIp(), datasource.getIp()) && Objects.equals(resp.getPort(), datasource.getPort() == null ? null : datasource.getPort()
                                                                                                                                         .intValue()) && Objects.equals(resp.getDatasourceConfig(), datasource.getDatasourceConfig()) && Objects.equals(resp.getDbType(), datasource.getDatasourceType()) && Objects.equals(resp.getDbName(), dbScope.getDbName()) && Objects.equals(resp.getSchemaName(), dbScope.getSchemaName())) {
                return resp;
            }
        }
        return null;
    }

    private DbQueryContext createDbQueryForScope(DaDatasourceRespDTO datasource, McDbSaveReqVO dbScope, McTaskRespVO task, McTaskInstanceDO instance) {
        DbQueryProperty property = new DbQueryProperty(datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());

        // Implementation details.
        if (DbType.KINGBASE8.getDb().equals(property.getDbType()) || DbType.POSTGRE_SQL.getDb()
                .equals(property.getDbType())) {
            property.setDbName(dbScope.getDbName());
            property.setSid(dbScope.getSchemaName());
        }

        DbQuery dbQuery = dataSourceFactory.createDbQuery(property);
        if (!dbQuery.valid()) {
            safeLog(instance.getId(), task.getId(), "数据库连接失败，db=" + dbScope.getDbName());
            throw new DataQueryException("数据库连接失败");
        }

        property.setDbName(dbScope.getDbName());
        property.setSid(dbScope.getSchemaName());
        return new DbQueryContext(dbQuery, property);
    }

    private TableProcessResult executeSingleDatabase(DbQueryContext dbQuery, McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope, DaDatasourceRespDTO datasource) {
        String blacklist = task.getBlacklist();
        boolean notEmptyBlacklist = StringUtils.isNotEmpty(blacklist);
        Long taskId = task.getId();
        Long instanceId = instance.getId();

        Long addCount = 0L;
        Long delCount = 0L;
        Long updateCount = 0L;

        Long totalCount = 0L;
        Long successCount = 0L;


        safeLog(instanceId, taskId, "开始处理数据库：" + dbScope.getDbName() + (StringUtils.isNotBlank(dbScope.getSchemaName()) ? "，schema=" + dbScope.getSchemaName() : ""));

        safeLog(instanceId, taskId, String.format("[DB] 当前计数快照：新增=%d，更新=%d，删除=%d", addCount, updateCount, delCount));

        // Implementation details.
        List<DbTable> tables = loadTablesByDatabase(dbQuery, task, instance, dbScope);
        safeLog(instanceId, taskId, String.format("数据库 %s 表加载完成，表数量=%d", dbScope.getDbName(), tables.size()));
        if (CollectionUtils.isEmpty(tables)) {
            return null;
        }
        int size = tables.size();
        safeLog(instanceId, taskId, String.format("[DB] 表加载完成：db=%s%s，表数量=%d", dbScope.getDbName(), StringUtils.isNotBlank(dbScope.getSchemaName()) ? ", schema=" + dbScope.getSchemaName() : "", size));
        totalCount = size + totalCount;
        List<McTableRespVO> tableRespDTOList = getMcTableById(task, instance, dbScope);

        // Implementation details.
        List<McTableSaveReqVO> mcTables = compareAndRecordTables(task, instance, dbScope, tables);


        safeLog(instanceId, taskId, String.format("[DB] 开始处理表列表，共 %d 张表", mcTables.size()));


        List<DbColumn> columns = loadColumnsByTable(dbQuery, task, instance, dbScope);


        safeLog(instanceId, taskId, String.format("库 %s 字段加载完成，字段数量=%d", dbScope.getDbName(), columns.size()));


        Map<String, List<DbColumn>> tableColumnMap = columns.stream()
                .collect(Collectors.groupingBy(DbColumn::getTableName));

        List<McColumnSaveReqVO> mcColumnReqDTOList = new ArrayList<>();
        List<Long> updateTableIds = new ArrayList<>();
        // Implementation details.
        for (McTableSaveReqVO table : mcTables) {
            if (notEmptyBlacklist) {
                String dbName = dbScope.getDbName();
                String tableName = table.getTableName();
                String fullTableName = dbName + "." + tableName;
                if (isInBlacklist(fullTableName, blacklist)) {
                    safeLog(instanceId, taskId,
                            String.format("【表跳过】table=%s 命中黑名单：%s",
                                    fullTableName,
                                    StringUtils.defaultIfBlank(blacklist, "空")));
                    continue;
                }

                safeLog(instanceId, taskId,
                        String.format("【表通过】table=%s 未命中黑名单%s",
                                fullTableName,
                                StringUtils.isBlank(blacklist) ? "（黑名单为空）" : ""));

            }

            List<DbColumn> dbColumns = tableColumnMap.get(table.getTableName());
            final List<DbColumn> finalDbColumns;
            if (DbType.HIVE.getDb().equals(dbQuery.getProperty().getDbType())) {
                finalDbColumns = dbQuery.getDbQuery().getTableColumns(dbQuery.getProperty(), table.getTableName());
            } else {
                finalDbColumns = dbColumns;
            }

            if (CollectionUtils.isEmpty(finalDbColumns)) {
                safeLog(instanceId, taskId, "[TABLE] 单表处理失败，已回滚，table=" + table.getTableName() + "，原因：字段未获取到");
                continue;
            }

            try {

                TableProcessResult result = mcTableTxService.runInNewTx(() -> doProcessSingleTable(task, instance, dbScope, table, tableRespDTOList, finalDbColumns));
//                TableProcessResult result = doProcessSingleTable(task, instance, dbScope, table, tableRespDTOList, dbColumns);
                //
                if (result != null) {
                    addCount = addCount + result.getAddCount();
                    updateCount = updateCount + result.getUpdateCount();
                    successCount = successCount + result.getSuccessCount();
                    mcColumnReqDTOList.addAll(result.getMcColumnReqList());
                    if (result.getUpdateCount() != 0 || result.getAddCount() != 0) {
                        updateTableIds.add(table.getId());
                    }
                }

            } catch (Exception e) {
                safeLog(instanceId, taskId, "[TABLE] 单表处理失败，已回滚，table=" + table.getTableName() + "，原因：" + e.getMessage());
            }
        }

        // Update the related record.
        mcTableTxService.runInNewTx(() -> mcDbMapper.updateStorageSizeById(dbScope.getId()));
        // Update the related record.
        mcTableTxService.runInNewTx(() -> mcDbMapper.updateDataRowCountById(dbScope.getId()));

        if (CollectionUtils.isNotEmpty(mcColumnReqDTOList)) {
            List<McColumnDO> columnDOList = mcTableTxService.runInNewTx(() -> mcColumnService.createMcColumnList(mcColumnReqDTOList));

            // Handle execution logging.
            mcTableTxService.runInNewTx(() -> mcColumnLogService.createMcColumnLog(columnDOList));
        }
        // Update the related record.
        mcTableTxService.runInNewTx(() -> mcDbMapper.updateColumnCountByDbId(dbScope.getId()));


        List<McTableRespVO> tablesOnlyInResp = findTablesOnlyInResp(mcTables, tableRespDTOList);
        if (notEmptyBlacklist) {
            Iterator<McTableRespVO> iterator = tablesOnlyInResp.iterator();
            while (iterator.hasNext()) {
                McTableRespVO table = iterator.next();

                String dbName = table.getDbName();      // Implementation details.
                String tableName = table.getTableName();
                String fullName = dbName + "." + tableName;

                // Implementation details.
                if (isInBlacklist(fullName, blacklist)) {
                    safeLog(instanceId, taskId,
                            String.format("【RESP表删除】table=%s 命中黑名单：%s",
                                    fullName,
                                    StringUtils.defaultIfBlank(blacklist, "空")));

                    iterator.remove(); // Delete the related record.
                }
            }
        }

        delCount = delCount + tablesOnlyInResp.size();
        if (CollectionUtils.isNotEmpty(tablesOnlyInResp)) {
            safeLog(instanceId, taskId, String.format("[DB] 发现待删除表数量=%d", tablesOnlyInResp.size()));
            for (McTableRespVO resp : tablesOnlyInResp) {
                safeLog(instanceId, taskId, "[TABLE] 表未在本次采集中出现，标记为删除：" + resp.getTableName());
            }
            List<Long> collect = tablesOnlyInResp.stream().map(a -> a.getId()).collect(Collectors.toList());
            mcTableService.removeMcTable(collect);
        }
        safeLog(instanceId, taskId, String.format("[DB] 数据库处理完成：db=%s，新增表=%d，更新表=%d，删除表=%d，成功表=%d", dbScope.getDbName(), addCount, updateCount, delCount, successCount));


        return new TableProcessResult(addCount, delCount, updateCount, totalCount, successCount, updateTableIds);
    }

    private TableProcessResult doProcessSingleTable(McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope, McTableSaveReqVO table, List<McTableRespVO> tableRespDTOList, List<DbColumn> columns) {
        Long taskId = task.getId();
        Long instanceId = instance.getId();
        Long addCount = 0L;
        Long updateCount = 0L;
        Long successCount = 0L;

        safeLog(instanceId, taskId, "开始处理表：" + dbScope.getDbName() + "." + table.getTableName());

        safeLog(instanceId, taskId, String.format("表 %s 字段加载完成，字段数量=%d", table.getTableName(), columns.size()));

        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }

        List<McColumnSaveReqVO> columnReqDTOS = compareAndRecordColumns(task, instance, dbScope, table, columns);

        safeLog(instanceId, taskId, String.format("[COLUMN] 表 %s 字段加载完成，字段数量=%d", table.getTableName(), columns.size()));

        McTableRespVO matched = findMatchedTable(table, tableRespDTOList);

        if (matched != null) {

            safeLog(instanceId, taskId, "[TABLE] 表已存在，进入结构比对：" + table.getTableName());

            table.setId(matched.getId());

            List<McColumnRespVO> mcColumnRespDTOList = getMcColumnByTaskId(table, instance, dbScope);

            boolean updated = isTableUpdated(table, matched, columnReqDTOS, mcColumnRespDTOList);
            boolean updated2 = isTableUpdated2(table, matched);
            if (updated || updated2) {

                safeLog(instanceId, taskId, "[TABLE] 表结构发生变更，执行更新：" + table.getTableName());

                updateCount++;//11

                mcTableTxService.runInNewTx(() -> mcTableService.updateMcTable(table));
//                mcTableService.updateMcTable(table);

                safeLog(instanceId, taskId, "[TABLE] 表已更新，准备删除并重建字段：" + table.getTableName());

                removeMcColumn(table, instance, dbScope);
            } else {
                safeLog(instanceId, taskId, "[TABLE] 表结构未变化，跳过更新：" + table.getTableName());
                successCount++;
                return new TableProcessResult(addCount, updateCount, successCount, new ArrayList<>());
            }

        } else {

            safeLog(instanceId, taskId, "[TABLE] 新表发现，准备创建元数据表：" + table.getTableName());

            Long mcTableId = mcTableTxService.runInNewTx(() -> mcTableService.createMcTable(table));
//            Long mcTableId = mcTableService.createMcTable(table);

            safeLog(instanceId, taskId, "[TABLE] 新表创建完成，mcTableId=" + mcTableId + "，table=" + table.getTableName());

            table.setId(mcTableId);
            addCount++;
            // Implementation details.
            columnReqDTOS.forEach(columnReqDTO -> columnReqDTO.setVersion(1));
        }
        // Implementation details.
        Long mcTableLogId = mcTableTxService.runInNewTx(() -> mcTableLogService.createMcTableLog(table));

        for (McColumnSaveReqVO columnReqDTO : columnReqDTOS) {
            columnReqDTO.setTableId(table.getId());
            columnReqDTO.setMcTableLogId(mcTableLogId);
            columnReqDTO.setTbPartitionKey(table.getPartitionKey());
        }

        safeLog(instanceId, taskId, String.format("[COLUMN] 表 %s 字段处理完成，创建字段数=%d", table.getTableName(), columnReqDTOS.size()));

        successCount++;


        return new TableProcessResult(addCount, updateCount, successCount, columnReqDTOS);
    }


    private void removeMcColumn(McTableSaveReqVO table, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {

        McColumnRespVO createReqVO = new McColumnRespVO();
        createReqVO.setTaskId(table.getTaskId());
        createReqVO.setTableId(table.getId());
        mcColumnService.removeMcColumn(createReqVO);
    }

    private boolean isTableUpdated(McTableSaveReqVO reqTable, McTableRespVO respTable, List<McColumnSaveReqVO> reqColumns, List<McColumnRespVO> respColumns) {

        // Update the related record.
        boolean result = false;
        StringBuilder updateMsg = new StringBuilder();
        Set<String> type = new HashSet<>();// Implementation details.
        String reqComment = StringUtils.defaultString(reqTable.getTableComment());
        String respComment = StringUtils.defaultString(respTable.getTableComment());
        if (!reqComment.equals(respComment)) {
            result = true;
            updateMsg.append("表注释变更旧注释：")
                    .append(respComment)
                    .append("，新注释：")
                    .append(reqComment)
                    .append("；\n");
            type.add("1");
        }

        // Update the related record.
        int reqSize = reqColumns == null ? 0 : reqColumns.size();
        int respSize = respColumns == null ? 0 : respColumns.size();
        if (reqSize != respSize) {
            result = true;
        }

        // Implementation details.
        Map<String, McColumnRespVO> respColumnMap = new HashMap<>();
        if (respColumns != null) {
            for (McColumnRespVO respCol : respColumns) {
                respColumnMap.put(respCol.getColumnName(), respCol);
            }
        }
        Set<String> addColumnNames = new HashSet<>(), updateColumnNames = new HashSet<>(), deleteColumnNames = new HashSet<>();
        // Implementation details.
        if (reqColumns != null) {
            for (McColumnSaveReqVO reqCol : reqColumns) {

                McColumnRespVO respCol = respColumnMap.get(reqCol.getColumnName());

                // Update the related record.
                if (respCol == null) {
                    result = true;
                    reqCol.setVersion(1);
                    reqCol.setUpdateType("1");
                    reqCol.setUpdateMsg("新增字段");
                    addColumnNames.add(reqCol.getColumnName());
                }

                // Update the related record.
                if (respCol != null && isColumnUpdated(reqCol, respCol)) {
                    result = true;
                    updateColumnNames.add(reqCol.getColumnName());
                }
            }
        }

        // Delete the related record.
        if (respColumns != null) {
            for (McColumnRespVO respCol : respColumns) {
                boolean found = false;
                if (reqColumns != null) {
                    for (McColumnSaveReqVO reqCol : reqColumns) {
                        if (respCol.getColumnName().equals(reqCol.getColumnName())) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    result = true;
                    deleteColumnNames.add(respCol.getColumnName());
                }
            }
        }

        if (!addColumnNames.isEmpty()) {
            type.add("2");
            updateMsg.append("新增：")
                    .append(addColumnNames.size())
                    .append("个字段：")
                    .append(String.join(",", addColumnNames))
                    .append("；\n");
        }
        if (!deleteColumnNames.isEmpty()) {
            type.add("2");
            updateMsg.append("删除：")
                    .append(deleteColumnNames.size())
                    .append("个字段：")
                    .append(String.join(",", deleteColumnNames))
                    .append("；\n");
        }
        if (!updateColumnNames.isEmpty()) {
            type.add("2");
            updateMsg.append("更新：")
                    .append(updateColumnNames.size())
                    .append("个字段：")
                    .append(String.join(",", updateColumnNames))
                    .append("；\n");
        }

        reqTable.setUpdateMsg(updateMsg.toString());
        reqTable.setUpdateType(String.join(",", type));
        return result;
    }

    private boolean isTableUpdated2(McTableSaveReqVO reqTable, McTableRespVO respTable) {
        boolean result = false;
        StringBuilder updateMsg = new StringBuilder();
        Set<String> type = new HashSet<>();// Implementation details.

        // Validate the input and configuration.
        McTableDO mcTableDO = BeanUtils.toBean(respTable, McTableDO.class);
        // Retrieve the required data.
        String tbIndex = mcTableDO.getTbIndex();
        Integer storageSize = mcTableDO.getStorageSize();
        if (storageSize == null) {
            storageSize = 0;
        }
        // Handle database and data source configuration.
        McDbDO mcDbDO = mcDbMapper.findById(mcTableDO.getDbId());
        if (mcDbDO != null) {
            // Handle database and data source configuration.
            DatabaseDialect dialect = DatabaseDialectFactory.getDialect(mcDbDO);
            if (dialect != null) {
                // Retrieve the required data.
                DatabaseDialect.TableMetadata metadata = dialect.getTableMetadata(mcDbDO, mcTableDO.getTableName());
                // Implementation details.
                if (StringUtils.isNotBlank(tbIndex) && !tbIndex.equals(metadata.getIndexes())) {
                    result = true;
                    updateMsg.append("表索引字段变更旧索引：")
                            .append(tbIndex)
                            .append("，新索引字段：")
                            .append(metadata.getIndexes())
                            .append("；\n");
                    type.add("3");
                }

                if (metadata.getTableSize() != null && storageSize != metadata.getTableSize().intValue()) {
                    result = true;
                    updateMsg.append("表存储大小变更旧存储大小：")
                            .append(storageSize)
                            .append("，新存储大小：")
                            .append(metadata.getTableSize())
                            .append("；\n");
                    type.add("4");
                }
            }
        }
        reqTable.setUpdateMsg(updateMsg.toString());
        reqTable.setUpdateType(String.join(",", type));
        return result;
    }

    private boolean isColumnUpdated(McColumnSaveReqVO req, McColumnRespVO resp) {

        StringBuilder updateMsg = new StringBuilder();
        Set<String> type = new HashSet<>();// Implementation details.
        boolean result = false;

        // Implementation details.
        String reqComment = StringUtils.defaultString(req.getColumnComment());
        String respComment = StringUtils.defaultString(resp.getColumnComment());
        if (!reqComment.equals(respComment)) {
            result = true;
            updateMsg.append("字段注释变更旧注释：")
                    .append(respComment)
                    .append("，新注释：")
                    .append(reqComment)
                    .append("；\n");
            type.add("1");
        }

        // Implementation details.
        String reqType = StringUtils.defaultString(req.getColumnType());
        String respType = StringUtils.defaultString(resp.getColumnType());
        if (!reqType.equals(respType)) {
            result = true;
            updateMsg.append("字段类型变更旧类型：").append(respType).append("，新类型：").append(reqType).append("；\n");
            type.add("2");
        }

        // Implementation details.
        if (!Objects.equals(req.getColumnLength(), resp.getColumnLength())) {
            result = true;
            updateMsg.append("字段长度变更旧长度：")
                    .append(resp.getColumnLength())
                    .append("，新长度：")
                    .append(req.getColumnLength())
                    .append("；\n");
            type.add("3");
        }

        // Implementation details.
        if (!Objects.equals(req.getColumnPrecision(), resp.getColumnPrecision())) {
            result = true;
            updateMsg.append("字段精度变更旧精度：")
                    .append(resp.getColumnPrecision())
                    .append("，新精度：")
                    .append(req.getColumnPrecision())
                    .append("；\n");
            type.add("4");
        }

        // Implementation details.
        if (!Objects.equals(req.getColumnScale(), resp.getColumnScale())) {
            result = true;
            updateMsg.append("字段小数位数变更旧小数位数：")
                    .append(resp.getColumnScale())
                    .append("，新小数位数：")
                    .append(req.getColumnScale())
                    .append("；\n");
            type.add("5");
        }

        // Implementation details.
        String reqDefault = StringUtils.defaultString(req.getDefaultValue());
        String respDefault = StringUtils.defaultString(resp.getDefaultValue());
        if (!reqDefault.equals(respDefault)) {
            result = true;
            updateMsg.append("字段默认值变更旧默认值：")
                    .append(respDefault)
                    .append("，新默认值：")
                    .append(reqDefault)
                    .append("；\n");
            type.add("6");
        }

        // Implementation details.
        String reqPkFlag = StringUtils.defaultString(req.getPkFlag());
        String respPkFlag = StringUtils.defaultString(resp.getPkFlag());
        if (!reqPkFlag.equals(respPkFlag)) {
            result = true;
            updateMsg.append("主键标识变更旧标识：")
                    .append(respPkFlag)
                    .append("，新标识：")
                    .append(reqPkFlag)
                    .append("；\n");
            type.add("7");
        }

        // Implementation details.
        String reqFkFlag = StringUtils.defaultString(req.getFkFlag());
        String respFkFlag = StringUtils.defaultString(resp.getFkFlag());
        if (!reqFkFlag.equals(respFkFlag)) {
            result = true;
            updateMsg.append("外键标识变更旧标识：")
                    .append(respFkFlag)
                    .append("，新标识：")
                    .append(reqFkFlag)
                    .append("；\n");
            type.add("8");
        }

        // Implementation details.
        String reqNullableFlag = StringUtils.defaultString(req.getNullableFlag());
        String respNullableFlag = StringUtils.defaultString(resp.getNullableFlag());
        if (!reqNullableFlag.equals(respNullableFlag)) {
            result = true;
            updateMsg.append("可空标识变更旧标识：")
                    .append(respNullableFlag)
                    .append("，新标识：")
                    .append(reqNullableFlag)
                    .append("；\n");
            type.add("9");
        }

        req.setUpdateMsg(updateMsg.toString());
        req.setUpdateType(String.join(",", type));
        // Implementation details.
        if (result) {
            req.setVersion(resp.getVersion() != null ? resp.getVersion() + 1 : 1);
        } else {
            req.setVersion(resp.getVersion());
        }
        return result;
    }


    private List<McColumnRespVO> getMcColumnByTaskId(McTableSaveReqVO table, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {
        McColumnRespVO createReqVO = new McColumnRespVO();
        createReqVO.setTaskId(table.getTaskId());
        createReqVO.setTableId(table.getId());
        return mcColumnService.getMcColumnList(createReqVO);
    }

    private List<McTableRespVO> findTablesOnlyInResp(List<McTableSaveReqVO> mcTables, List<McTableRespVO> tableRespDTOList) {

        List<McTableRespVO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(tableRespDTOList)) {
            return result;
        }

        for (McTableRespVO resp : tableRespDTOList) {
            boolean exists = false;
            if (CollectionUtils.isNotEmpty(mcTables)) {
                for (McTableSaveReqVO req : mcTables) {
                    if (Objects.equals(req.getDbName(), resp.getDbName()) && Objects.equals(req.getSchemaName(), resp.getSchemaName()) && Objects.equals(req.getTableName(), resp.getTableName())) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                result.add(resp);
            }
        }
        return result;
    }


    private McTableRespVO findMatchedTable(McTableSaveReqVO req, List<McTableRespVO> tableRespDTOList) {
        if (req == null || CollectionUtils.isEmpty(tableRespDTOList)) {
            return null;
        }

        for (McTableRespVO resp : tableRespDTOList) {
            if (Objects.equals(req.getDbId(), resp.getDbId()) && Objects.equals(req.getTableName(), resp.getTableName())) {
                return resp;
            }
        }
        return null;
    }


    private List<McTableRespVO> getMcTableById(McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {
        McTableRespVO mcTableReqDTO = new McTableRespVO();
        mcTableReqDTO.setTaskId(task.getId());
        mcTableReqDTO.setDbId(dbScope.getId());
        return mcTableService.getMcTableById(mcTableReqDTO);
    }

    private List<DbTable> loadTablesByDatabase(DbQueryContext dbQuery, McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {

        try {
            List<DbTable> tables = dbQuery.getDbQuery().getTables(dbQuery.getProperty());
            return tables == null ? new ArrayList<>() : tables;
        } catch (Exception e) {
            safeLog(instance.getId(), task.getId(), "加载表异常，db=" + dbScope.getDbName() + "，原因：" + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<DbColumn> loadColumnsByTable(DbQueryContext dbQuery, McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {

        try {
            List<DbColumn> tableColumns = dbQuery.getDbQuery().getDbColumns(dbQuery.getProperty());
            return tableColumns == null ? new ArrayList<>() : tableColumns;
        } catch (Exception e) {
            safeLog(instance.getId(), task.getId(), "加载字段异常，库=" + dbScope.getDbName() + "，原因：" + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void closeDbQuerySafely(DbQueryContext ctx, McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope) {
        try {
            ctx.getDbQuery().close();
        } catch (Exception e) {
            safeLog(instance.getId(), task.getId(), "关闭数据库连接异常，db=" + dbScope.getDbName() + "，原因：" + e.getMessage());
        }
    }


    private List<McTaskScopeDO> loadDatabaseScopesFromDatasource(McTaskRespVO task, McTaskInstanceDO instance, DaDatasourceRespDTO datasource) {

        Long taskId = task.getId();
        Long instanceId = instance.getId();

        safeLog(instanceId, taskId, "全量模式：从数据源加载数据库信息");

        // Implementation details.
        DbQueryProperty baseProperty = new DbQueryProperty(datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());

        // Handle database and data source configuration.
        List<DbName> dbNames;
        DbQuery rootQuery = dataSourceFactory.createDbQuery(baseProperty);
        try {
            if (!rootQuery.valid()) {
                safeLog(instanceId, taskId, "数据库连接失败");
                throw new DataQueryException("数据库连接失败");
            }
            dbNames = rootQuery.getDbNames(null);
        } finally {
            rootQuery.close();
        }

        List<McTaskScopeDO> scopeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dbNames)) {
            safeLog(instanceId, taskId, "未获取到任何数据库");
            return scopeList;
        }

        // Implementation details.
        if (dbNames.get(0).getLevel() == 1 && dbNames.get(0).getTotalLevels() == 1) {
            for (DbName dbName : dbNames) {
                McTaskScopeDO scope = new McTaskScopeDO();
                scope.setDbName(dbName.getDbName());
                scopeList.add(scope);
            }
            return scopeList;
        }

        // Implementation details.
        for (DbName dbName : dbNames) {

            DbQueryProperty childProperty = baseProperty;
            if (DbType.KINGBASE8.getDb().equals(baseProperty.getDbType()) || DbType.POSTGRE_SQL.getDb()
                    .equals(baseProperty.getDbType())) {

                childProperty = baseProperty.copy();
                childProperty.setDbName(dbName.getDbName());
            }

            DbQuery childQuery = dataSourceFactory.createDbQuery(childProperty);
            try {
                if (!childQuery.valid()) {
                    continue;
                }
                List<DbName> children = childQuery.getDbNames(dbName);
                dbName.setChildren(children);
            } catch (Exception e) {
                safeLog(instanceId, taskId, "获取数据库下级失败，db=" + dbName.getDbName() + "，原因：" + e.getMessage());
            } finally {
                childQuery.close();
            }

            List<DbName> children = dbName.getChildren();
            if (CollectionUtils.isNotEmpty(children)) {
                for (DbName child : children) {
                    McTaskScopeDO scope = new McTaskScopeDO();
                    scope.setDbName(dbName.getDbName());
                    scope.setSchemaName(child.getDbName());
                    scopeList.add(scope);
                }
            } else {
                McTaskScopeDO scope = new McTaskScopeDO();
                scope.setDbName(dbName.getDbName());
                scopeList.add(scope);
            }
        }

        safeLog(instanceId, taskId, "数据库加载完成，范围数量：" + scopeList.size());
        return scopeList;
    }


    private List<McTaskScopeDO> loadDatabaseScopesFromTask(McTaskRespVO task, McTaskInstanceDO instance) {

        safeLog(instance.getId(), task.getId(), "增量模式：使用任务配置的采集范围");

        return task.getScopeSaveReqVOS();
    }

    private List<McDbSaveReqVO> compareAndRecordDatabaseScope(McTaskRespVO task, McTaskInstanceDO instance, List<McTaskScopeDO> databaseScopes, DaDatasourceRespDTO datasource) {
        List<McDbSaveReqVO> dbReqDTOList = new ArrayList<>();
        String creatorId = redisService.get(buildRunLockKey(task.getId()) + ":creatorId");
        String createBy = redisService.get(buildRunLockKey(task.getId()) + ":createBy");
        // Implementation details.
        for (McTaskScopeDO databaseScope : databaseScopes) {

            McDbSaveReqVO createReqVO = new McDbSaveReqVO();
            // Implementation details.
            createReqVO.setTaskId(task.getId());

            // Implementation details.
            createReqVO.setSourceSystemId(task.getSourceSystemId());
            createReqVO.setSourceSystemName(task.getSourceSystemName());

            // Handle database and data source configuration.
            createReqVO.setDatasourceId(datasource.getId());
            createReqVO.setDbType(datasource.getDatasourceType());
            createReqVO.setIp(datasource.getIp());
            createReqVO.setPort(datasource.getPort() != null ? datasource.getPort().intValue() : null);
            createReqVO.setDatasourceConfig(datasource.getDatasourceConfig());
            createReqVO.setBelongingSystem(datasource.getDatasourceName());

            createReqVO.setCreateBy(StringUtils.isNotEmpty(createBy) ? createBy : "System Collection Task");
            createReqVO.setCreatorId(StringUtils.isNotEmpty(creatorId) ? Long.parseLong(creatorId) : 1L);

            // Implementation details.
            createReqVO.setDbName(databaseScope.getDbName());
            createReqVO.setSchemaName(databaseScope.getSchemaName());

            // Implementation details.
            createReqVO.setDescription(databaseScope.getDescription());

            // Implementation details.
            createReqVO.setStatus("0");      // Implementation details.
            createReqVO.setAuditStatus("2");
            createReqVO.setVersion(1);
            createReqVO.setAuditTime(new Date());

            dbReqDTOList.add(createReqVO);
        }

        return dbReqDTOList;
    }

    private List<McTableSaveReqVO> compareAndRecordTables(McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope, List<DbTable> tables) {
        List<McTableSaveReqVO> mcTableReqDTOList = new ArrayList<>();
        String creatorId = redisService.get(buildRunLockKey(task.getId()) + ":creatorId");
        String createBy = redisService.get(buildRunLockKey(task.getId()) + ":createBy");

        for (DbTable table : tables) {

            McTableSaveReqVO mcTableReqDTO = new McTableSaveReqVO();

            // Implementation details.
            mcTableReqDTO.setTaskId(task.getId());
            mcTableReqDTO.setDbId(dbScope.getId());
            mcTableReqDTO.setDatasourceId(task.getDatasourceId());

            // Implementation details.
            mcTableReqDTO.setTableName(table.getTableName());
            mcTableReqDTO.setTableComment(StringUtils.isEmpty(table.getTableComment()) ? "" : table.getTableComment());

            // Implementation details.
            mcTableReqDTO.setDbName(dbScope.getDbName());
            mcTableReqDTO.setSchemaName(dbScope.getSchemaName());

            mcTableReqDTO.setCreateBy(StringUtils.isNotEmpty(createBy) ? createBy : "System Collection Task");
            mcTableReqDTO.setCreatorId(StringUtils.isNotEmpty(creatorId) ? Long.parseLong(creatorId) : 1L);

            // Implementation details.
            mcTableReqDTO.setStatus("0");     // Implementation details.
            mcTableReqDTO.setVersion(1);
            mcTableReqDTO.setMasterFlag("1");
            mcTableReqDTO.setTempFlag("0");
            mcTableReqDTO.setAuditStatus("2");
            mcTableReqDTO.setAuditTime(new Date());

            // Implementation details.
            mcTableReqDTO.setDescription(table.getTableComment());

            // Implementation details.
//            Long mcTableId = mcTableService.createMcTable(mcTableReqDTO);
//
//            mcTableReqDTO.setId(mcTableId);
            mcTableReqDTOList.add(mcTableReqDTO);
        }

        return mcTableReqDTOList;
    }

    private List<McColumnSaveReqVO> compareAndRecordColumns(McTaskRespVO task, McTaskInstanceDO instance, McDbSaveReqVO dbScope, McTableSaveReqVO table, List<DbColumn> columns) {

        List<McColumnSaveReqVO> columnReqDTOS = new ArrayList<>();
        String creatorId = redisService.get(buildRunLockKey(task.getId()) + ":creatorId");
        String createBy = redisService.get(buildRunLockKey(task.getId()) + ":createBy");
        for (DbColumn column : columns) {
            if (null != column) {
                McColumnSaveReqVO createReqVO = new McColumnSaveReqVO();

                // Implementation details.
                createReqVO.setTaskId(task.getId());
                createReqVO.setDbId(dbScope.getId());
                createReqVO.setTableId(table.getId());
                createReqVO.setDatasourceId(task.getDatasourceId());

                // Implementation details.
                createReqVO.setColumnName(StringUtils.isEmpty(column.getColName()) ? "" : column.getColName());
                createReqVO.setColumnComment(StringUtils.isEmpty(column.getColComment()) ? "" : column.getColComment());
                createReqVO.setColumnType(StringUtils.isEmpty(column.getDataType()) ? "" : column.getDataType());

                // Implementation details.
                createReqVO.setColumnLength(parseInt(column.getDataLength()));
                createReqVO.setColumnPrecision(parseInt(column.getDataPrecision()));
                createReqVO.setColumnScale(parseInt(column.getDataScale()));

                // Implementation details.
                createReqVO.setDefaultValue(column.getDataDefault());

                // Implementation details.
                createReqVO.setPkFlag(Boolean.TRUE.equals(column.getColKey()) ? "1" : "0");
                createReqVO.setNullableFlag(Boolean.FALSE.equals(column.getNullable()) ? "1" : "0");
                createReqVO.setFkFlag("0");

                createReqVO.setCreateBy(StringUtils.isNotEmpty(createBy) ? createBy : "System Collection Task");
                createReqVO.setCreatorId(StringUtils.isNotEmpty(creatorId) ? Long.parseLong(creatorId) : 1L);

                // Implementation details.
                createReqVO.setStatus("0");     // Implementation details.
                createReqVO.setVersion(1);
                createReqVO.setAuditStatus("2");
                createReqVO.setAuditTime(new Date());

                // Implementation details.
                createReqVO.setDescription(column.getColComment());


                columnReqDTOS.add(createReqVO);
                // Implementation details.

                // Implementation details.
            }
        }
        return columnReqDTOS;
    }

    /**
     * Implementation details.
     */
    private Integer parseInt(String val) {
        if (val == null || val.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Handle task-related data and operations.
     *
     * @param reqVO parameter value
     * @param excludeTaskId parameter value
     */
    private void validateDuplicateTask(McTaskSaveReqVO reqVO, Long excludeTaskId) {
        String collectionScope = reqVO.getCollectionScope();
        Long datasourceId = reqVO.getDatasourceId();

        if (datasourceId == null) {
            return;
        }

        // Implementation details.
        if (CollectionScopeEnum.isAll(collectionScope)) {
            // Handle task-related data and operations.
            boolean exists = mcTaskMapper.existsByDatasourceId(datasourceId, excludeTaskId);
            if (exists) {
                throw new ServiceException("该数据源已被其他任务使用，无法重复添加", HttpStatus.CONFLICT);
            }
        }
        // Implementation details.
        else if (CollectionScopeEnum.isCustom(collectionScope)) {
            // Handle database and data source configuration.
            boolean hasAllScope = mcTaskMapper.existsByDatasourceAndScope(datasourceId, CollectionScopeEnum.ALL.getScope(), excludeTaskId);
            if (hasAllScope) {
                throw new ServiceException("该数据源已被全量采集任务使用，无法创建增量任务", HttpStatus.CONFLICT);
            }

            // Handle task-related data and operations.
            List<McTaskDO> existCustomTasks = mcTaskMapper.selectByDatasourceAndScope(datasourceId, CollectionScopeEnum.CUSTOM.getScope(), excludeTaskId);
            if (!CollectionUtils.isEmpty(existCustomTasks)) {
                // Handle task-related data and operations.
                List<McTaskScopeSaveReqVO> currentScopes = reqVO.getScopeSaveReqVOS();
                if (CollectionUtils.isEmpty(currentScopes)) {
                    return;
                }

                for (McTaskDO existTask : existCustomTasks) {
                    // Handle task-related data and operations.
                    List<McTaskScopeDO> existScopes = mcTaskScopeService.getMcTaskScopeListBytaskId(existTask.getId());
                    if (CollectionUtils.isEmpty(existScopes)) {
                        continue;
                    }

                    // Validate the input and configuration.
                    for (McTaskScopeSaveReqVO currentScope : currentScopes) {
                        for (McTaskScopeDO existScope : existScopes) {
                            if (isSameDatabase(currentScope, existScope)) {
                                String dbName = currentScope.getDbName();
                                String schemaName = currentScope.getSchemaName();
                                String dbInfo = StringUtils.isNotBlank(schemaName)
                                        ? dbName + "." + schemaName
                                        : dbName;
                                throw new ServiceException("采集范围中的数据库 [" + dbInfo + "] 已被其他任务使用", HttpStatus.CONFLICT);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Handle database and data source configuration.
     *
     * @param scope1 parameter value
     * @param scope2 parameter value
     * @return the operation result
     */
    private boolean isSameDatabase(McTaskScopeSaveReqVO scope1, McTaskScopeDO scope2) {
        if (scope1 == null || scope2 == null) {
            return false;
        }
        return Objects.equals(scope1.getDbName(), scope2.getDbName()) &&
                Objects.equals(scope1.getSchemaName(), scope2.getSchemaName());
    }

    @Override
    public List<McTaskSourceTreeRespVO> getSourceSystemTree() {
        // Retrieve the required data.
        List<AttSourceSystemRespDTO> validSourceSystems = attSourceSystemApiService.getValidSourceSystems();
        if (CollectionUtils.isEmpty(validSourceSystems)) {
            return Lists.newArrayList();
        }

        // Handle task-related data and operations.
        List<McTaskDO> allTasks = mcTaskMapper.selectList();
        Map<Long, List<McTaskDO>> tasksBySourceSystemMap = Maps.newHashMap();
        List<DaDatasourceRespDTO> daDatasourceRespDTOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(allTasks)) {
            // Handle task-related data and operations.
            tasksBySourceSystemMap = allTasks.stream()
                    .filter(task -> task.getSourceSystemId() != null)
                    .collect(Collectors.groupingBy(McTaskDO::getSourceSystemId));
            // Handle task-related data and operations.
            Set<Long> datasourceIds = allTasks.stream()
                    .map(McTaskDO::getDatasourceId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            daDatasourceRespDTOList = daDatasourceApiService
                    .getDatabaseListByIds(new ArrayList<>(datasourceIds));
        }

        Map<Long, DaDatasourceRespDTO> daDatasourceRespDTOMap = daDatasourceRespDTOList
                .stream()
                .collect(Collectors.toMap(DaDatasourceRespDTO::getId, daDatasourceRespDTO -> daDatasourceRespDTO));

        List<McTaskSourceTreeRespVO> treeList = Lists.newArrayList();

        // Handle node-related data and operations.
        for (AttSourceSystemRespDTO sourceSystem : validSourceSystems) {
            McTaskSourceTreeRespVO sourceNode = new McTaskSourceTreeRespVO();
            sourceNode.setId(sourceSystem.getId());
            sourceNode.setName(sourceSystem.getName());
            sourceNode.setType("SOURCE");

            // Handle task-related data and operations.
            List<McTaskDO> sourceSystemTasks = tasksBySourceSystemMap.getOrDefault(sourceSystem.getId(), Lists.newArrayList());

            // Handle node-related data and operations.
            Map<Long, List<McTaskDO>> datasourceGroupMap = sourceSystemTasks.stream()
                    .filter(task -> task.getDatasourceId() != null)
                    .collect(Collectors.groupingBy(McTaskDO::getDatasourceId));

            List<McTaskSourceTreeRespVO> datasourceChildren = Lists.newArrayList();
            for (Map.Entry<Long, List<McTaskDO>> dsEntry : datasourceGroupMap.entrySet()) {
                Long datasourceId = dsEntry.getKey();
                List<McTaskDO> dsTasks = dsEntry.getValue();
                if (CollectionUtils.isEmpty(dsTasks)) {
                    continue;
                }

                DaDatasourceRespDTO daDatasourceRespDTO = daDatasourceRespDTOMap.get(datasourceId);

                McTaskSourceTreeRespVO datasourceNode = new McTaskSourceTreeRespVO();
                datasourceNode.setId(datasourceId);
                datasourceNode.setName(daDatasourceRespDTO == null ? "" : daDatasourceRespDTO.getDatasourceName());
                datasourceNode.setDatasourceType(daDatasourceRespDTO == null ? "" : daDatasourceRespDTO.getDatasourceType());
                datasourceNode.setType("DATASOURCE");

                // Handle task-related data and operations.
                List<McTaskSourceTreeRespVO> dbChildren = Lists.newArrayList();
                for (McTaskDO task : dsTasks) {
                    List<McTaskSourceTreeRespVO> taskDbNodes = buildDatabaseNodes(task);
                    dbChildren.addAll(taskDbNodes);
                }
                datasourceNode.setChildren(dbChildren);

                datasourceChildren.add(datasourceNode);
            }

            sourceNode.setChildren(datasourceChildren);
            treeList.add(sourceNode);
        }

        return treeList;
    }

    /**
     * Handle node-related data and operations.
     *
     * @param task parameter value
     * @return the operation result
     */
    private List<McTaskSourceTreeRespVO> buildDatabaseNodes(McTaskDO task) {
        List<McTaskSourceTreeRespVO> dbNodes = Lists.newArrayList();

        // Implementation details.
        if (CollectionScopeEnum.isAll(task.getCollectionScope())) {
            // Handle database and data source configuration.
            List<McDbDO> allDbs = mcDbMapper.selectList(
                    new QueryWrapper<McDbDO>()
                            .eq("DATASOURCE_ID", task.getDatasourceId())
                            .eq("DEL_FLAG", "0")
            );

            if (CollectionUtils.isNotEmpty(allDbs)) {
                for (McDbDO db : allDbs) {
                    McTaskSourceTreeRespVO dbNode = new McTaskSourceTreeRespVO();
                    dbNode.setId(db.getId());
                    dbNode.setName(db.getDbName());
                    dbNode.setType("DATABASE");
                    dbNode.setTaskId(task.getId());
                    dbNodes.add(dbNode);
                }
            }
        } else if (CollectionScopeEnum.isCustom(task.getCollectionScope())) {
            List<McDbDO> allDbs = mcDbMapper.selectList(
                    new QueryWrapper<McDbDO>()
                            .eq("DATASOURCE_ID", task.getDatasourceId())
                            .eq("DEL_FLAG", "0")
            );

            if (CollectionUtils.isNotEmpty(allDbs)) {
                for (McDbDO db : allDbs) {
                    McTaskSourceTreeRespVO dbNode = new McTaskSourceTreeRespVO();
                    dbNode.setId(db.getId());
                    dbNode.setName(db.getDbName());
                    dbNode.setType("DATABASE");
                    dbNode.setTaskId(task.getId());
                    dbNodes.add(dbNode);
                }
            }
            // Handle task-related data and operations.
            //List<McTaskScopeDO> scopes = mcTaskScopeService.getMcTaskScopeListBytaskId(task.getId());
            //
            //if (CollectionUtils.isNotEmpty(scopes)) {
            //    for (McTaskScopeDO scope : scopes) {
            //        McTaskSourceTreeRespVO dbNode = new McTaskSourceTreeRespVO();
            //        dbNode.setId(scope.getId());
            //        dbNode.setName(StringUtils.isNotBlank(scope.getSchemaName())
            //                ? scope.getDbName() + "." + scope.getSchemaName()
            //                : scope.getDbName());
            //        dbNode.setType("DATABASE");
            //        dbNode.setTaskId(task.getId());
            //        dbNodes.add(dbNode);
            //    }
            //}
        }

        return dbNodes;
    }
}
