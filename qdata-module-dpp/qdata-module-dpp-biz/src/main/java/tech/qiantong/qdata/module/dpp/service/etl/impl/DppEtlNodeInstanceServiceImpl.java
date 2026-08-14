/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceLogDetailRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceStatisticsRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceLogLineRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.*;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeInstanceMapper;
import tech.qiantong.qdata.module.dpp.service.etl.*;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

/**
 * Data Integration Node Instance Service business layer processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlNodeInstanceServiceImpl extends ServiceImpl<DppEtlNodeInstanceMapper, DppEtlNodeInstanceDO> implements IDppEtlNodeInstanceService {
    @Resource
    private DppEtlNodeInstanceMapper dppEtlNodeInstanceMapper;

    @Resource
    private IDppEtlNodeService dppEtlNodeService;

    @Resource
    private IDppEtlNodeLogService dppEtlNodeLogService;

    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;

    @Resource
    private IAttProjectApi attProjectApi;

    @Resource
    private IRedisService redisService;

    @Resource
    private IDppEtlTaskInstanceLogService dppEtlTaskInstanceLogService;

    @Resource
    private IDppEtlNodeInstanceLogService dppEtlNodeInstanceLogService;

    @Override
    public PageResult<DppEtlNodeInstanceDO> getDppEtlNodeInstancePage(DppEtlNodeInstancePageReqVO pageReqVO) {
        PageResult<DppEtlNodeInstanceDO> page = dppEtlNodeInstanceMapper.selectPage(pageReqVO);
        if (page != null && page.getRows() != null) {
            page.getRows().forEach(this::fillInstanceDisplayFields);
        }
        return page;
    }

    @Override
    public Long createDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO createReqVO) {
        DppEtlNodeInstanceDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeInstanceDO.class);
        dppEtlNodeInstanceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO updateReqVO) {
        // Validate

        // Update data integration node instance
        DppEtlNodeInstanceDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlNodeInstanceDO.class);
        return dppEtlNodeInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlNodeInstance(Collection<Long> idList) {
        // Batch delete data integration node instance
        return dppEtlNodeInstanceMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlNodeInstanceDO getDppEtlNodeInstanceById(Long id) {
        return dppEtlNodeInstanceMapper.selectById(id);
    }

    @Override
    public List<DppEtlNodeInstanceDO> getDppEtlNodeInstanceList() {
        return dppEtlNodeInstanceMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlNodeInstanceDO> getDppEtlNodeInstanceMap() {
        List<DppEtlNodeInstanceDO> dppEtlNodeInstanceList = dppEtlNodeInstanceMapper.selectList();
        return dppEtlNodeInstanceList.stream()
                .collect(Collectors.toMap(
                        DppEtlNodeInstanceDO::getId,
                        dppEtlNodeInstanceDO -> dppEtlNodeInstanceDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data integration node instance data
     *
     * @param importExcelList data integration node instance data list
     * @param isUpdateSupport whether to support update; if already exists, update the data
     * @param operName        operator user
     * @return result
     */
    @Override
    public String importDppEtlNodeInstance(List<DppEtlNodeInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlNodeInstanceRespVO respVO : importExcelList) {
            try {
                DppEtlNodeInstanceDO dppEtlNodeInstanceDO = BeanUtils.toBean(respVO, DppEtlNodeInstanceDO.class);
                Long dppEtlNodeInstanceId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlNodeInstanceId != null) {
                        DppEtlNodeInstanceDO existingDppEtlNodeInstance = dppEtlNodeInstanceMapper.selectById(dppEtlNodeInstanceId);
                        if (existingDppEtlNodeInstance != null) {
                            dppEtlNodeInstanceMapper.updateById(dppEtlNodeInstanceDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dppEtlNodeInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.node.instance", "Data integration node instance")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dppEtlNodeInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.node.instance", "Data integration node instance")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DppEtlNodeInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlNodeInstanceId);
                    DppEtlNodeInstanceDO existingDppEtlNodeInstance = dppEtlNodeInstanceMapper.selectOne(queryWrapper);
                    if (existingDppEtlNodeInstance == null) {
                        dppEtlNodeInstanceMapper.insert(dppEtlNodeInstanceDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dppEtlNodeInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.node.instance", "Data integration node instance")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dppEtlNodeInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.node.instance", "Data integration node instance")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public Boolean createNodeInstance(TaskInstance taskInstance) {
        log.info(JSONObject.toJSONString(taskInstance));
        DppEtlNodeLogDO dppEtlNodeRespDTO = dppEtlNodeLogService.getByNodeCodeAndVersion(taskInstance.getTaskCode(), taskInstance.getTaskDefinitionVersion());
        if (dppEtlNodeRespDTO == null) {
            return true;
        }
        DppEtlNodeInstanceDO dppEtlTaskInstanceDO = DppEtlNodeInstanceDO.builder()
                .id(taskInstance.getId())
                .taskType(dppEtlNodeRespDTO.getTaskType())
                .name(taskInstance.getName())
                .nodeType(taskInstance.getTaskType())
                .nodeId(dppEtlNodeRespDTO.getId())
                .nodeCode(taskInstance.getTaskCode())
                .nodeVersion(taskInstance.getTaskDefinitionVersion())
                .taskInstanceId(taskInstance.getProcessInstanceId())
                .taskInstanceName(taskInstance.getProcessInstanceName())
                .projectId(attProjectApi.getProjectIdByProjectCode(String.valueOf(taskInstance.getProjectCode())))
                .projectCode(String.valueOf(taskInstance.getProjectCode()))
                .submitTime(taskInstance.getSubmitTime())
                .startTime(taskInstance.getStartTime())
                .executePath(taskInstance.getExecutePath())
                .parameters(dppEtlNodeRespDTO.getParameters())
                .priority(String.valueOf(taskInstance.getTaskInstancePriority().getCode()))
                .retryTimes(taskInstance.getRetryTimes())
                .delayTime(taskInstance.getDelayTime())
                .cpuQuota(taskInstance.getCpuQuota())
                .memoryMax(taskInstance.getMemoryMax())
                .status(String.valueOf(taskInstance.getState().getCode()))
                .componentType(dppEtlNodeRespDTO.getComponentType())
                .dsId(taskInstance.getId())
                .dsTaskInstanceId(taskInstance.getProcessInstanceId())
                .executePath(taskInstance.getExecutePath())
                .logPath(taskInstance.getLogPath())
                .build();
        return this.save(dppEtlTaskInstanceDO);
    }

    /**
     * 独立提交本地 DataX 节点实例，避免被 DataX 执行长事务延迟到任务结束。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Boolean createLocalDataXNodeInstance(DppEtlNodeInstanceDO nodeInstance) {
        return this.save(nodeInstance);
    }

    @Override
    public Boolean updateNodeInstance(TaskInstance taskInstance) {
        log.info(JSONObject.toJSONString(taskInstance));
        DppEtlNodeInstanceDO old = this.getById(taskInstance.getId());
        if (old == null) {
            return true;
        }
        DppEtlNodeInstanceDO dppEtlTaskInstanceDO = DppEtlNodeInstanceDO.builder()
                .id(old.getId())
                .startTime(taskInstance.getStartTime())
                .endTime(taskInstance.getEndTime())
                .executePath(taskInstance.getExecutePath())
                .logPath(taskInstance.getLogPath())
                .status(String.valueOf(taskInstance.getState().getCode()))
                .build();
        return this.saveOrUpdate(dppEtlTaskInstanceDO);
    }

    @Override
    public DppEtlNodeInstanceDO getByDsId(Long dsId) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeInstanceDO.class)
                .eq(DppEtlNodeInstanceDO::getDsId, dsId));
    }

    @Override
    public void taskInstanceLogInsert(String taskInstanceId, String processInstanceId, String logStr) {
        String taskInstanceLogKey = TaskConverter.TASK_INSTANCE_LOG_KEY + taskInstanceId;
        String processInstanceLogKey = TaskConverter.PROCESS_INSTANCE_LOG_KEY + processInstanceId;
        // Check if current task instance exists
        if (processInstanceId == null || StringUtils.equals("null", processInstanceId) || (!redisService.hasKey(processInstanceLogKey) && dppEtlTaskInstanceService.count(Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(DppEtlTaskInstanceDO::getId, Long.parseLong(processInstanceId))) == 0)) {
            return;
        }
        String taskInstanceLog = redisService.get(taskInstanceLogKey);
        String processInstanceLog = redisService.get(processInstanceLogKey);
        if (taskInstanceLog == null) {
            taskInstanceLog = "";
        }
        if (processInstanceLog == null) {
            processInstanceLog = "";
        }
        taskInstanceLog += logStr + (logStr.matches(".*\r?\n.*") ? "" : "\n");
        processInstanceLog += logStr + (logStr.matches(".*\r?\n.*") ? "" : "\n");
        redisService.set(taskInstanceLogKey, taskInstanceLog);
        redisService.set(processInstanceLogKey, processInstanceLog);

        // Check if session is finished
        if (StringUtils.indexOf(logStr, "FINALIZE_SESSION") > -1) {
            // Check if current task instance is finished
            DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceService.getById(Long.parseLong(processInstanceId));
            // Check status: 5=stopped, 6=failed, 7=succeeded
            if (dppEtlTaskInstanceDO != null && Arrays.asList("5", "6", "7").contains(dppEtlTaskInstanceDO.getStatus())) {
                // Write log
                redisService.delete(processInstanceLogKey);
                // Check if it is data integration
                if (StringUtils.equals("1", dppEtlTaskInstanceDO.getTaskType())) {
                    // Write log
                    dppEtlTaskInstanceLogService.saveOrUpdate(DppEtlTaskInstanceLogDO.builder()
                            .taskInstanceId(dppEtlTaskInstanceDO.getId())
                            .tm(new Date())
                            .taskType(dppEtlTaskInstanceDO.getTaskType())
                            .taskId(dppEtlTaskInstanceDO.getTaskId())
                            .taskCode(dppEtlTaskInstanceDO.getTaskCode())
                            .logContent(processInstanceLog)
                            .build());
                }
            }

            // Get current node instance
            DppEtlNodeInstanceDO dppEtlNodeInstanceDO = this.getById(Long.parseLong(taskInstanceId));
            // Write log, 5-minute expiry for compatibility when node status hasn't changed
            redisService.delete(taskInstanceLogKey);
            redisService.set(taskInstanceLogKey, taskInstanceLog, 60 * 5);
            dppEtlNodeInstanceLogService.save(DppEtlNodeInstanceLogDO.builder()
                    .nodeInstanceId(dppEtlNodeInstanceDO.getId())
                    .tm(new Date())
                    .taskType(dppEtlNodeInstanceDO.getTaskType())
                    .nodeId(dppEtlNodeInstanceDO.getNodeId())
                    .nodeCode(dppEtlNodeInstanceDO.getNodeCode())
                    .taskInstanceId(dppEtlNodeInstanceDO.getTaskInstanceId())
                    .logContent(taskInstanceLog)
                    .build());
        }
    }

    @Override
    public String getLogByNodeInstanceId(Long nodeInstanceId) {
        DppEtlNodeInstanceDO dppEtlNodeInstanceDO = this.getDppEtlNodeInstanceById(nodeInstanceId);
        if (dppEtlNodeInstanceDO == null) {
            throw new ServiceException("Node instance does not exist");
        }
        String content = "";
        String taskInstanceLogKey = TaskConverter.TASK_INSTANCE_LOG_KEY + dppEtlNodeInstanceDO.getId();
        if (redisService.hasKey(taskInstanceLogKey)) {
            content += redisService.get(taskInstanceLogKey) + "\n";
        } else {
            // Get logs from the table
            String logContent = dppEtlNodeInstanceLogService.getLog(dppEtlNodeInstanceDO.getId());
            if (logContent != null) {
                content += logContent + "\n";
            }
        }
        return content;
    }

    @Override
    public DppEtlNodeInstanceLogDetailRespVO getLogDetailByNodeInstanceId(Long nodeInstanceId) {
        DppEtlNodeInstanceDO nodeInstance = this.getById(nodeInstanceId);
        if (nodeInstance == null) {
            throw new ServiceException("Node instance does not exist");
        }
        String logContent = getLogByNodeInstanceId(nodeInstanceId);
        List<DppEtlTaskInstanceLogLineRespVO> logLines = new ArrayList<>();
        for (String line : logContent.split("\\r?\\n")) {
            if (StringUtils.isBlank(line)) continue;
            DppEtlTaskInstanceLogLineRespVO item = new DppEtlTaskInstanceLogLineRespVO();
            item.setLineNo(logLines.size() + 1);
            item.setLevel(guessLogLevel(line));
            item.setContent(line);
            item.setDetailContent(line);
            logLines.add(item);
        }
        DppEtlNodeInstanceLogDetailRespVO result = new DppEtlNodeInstanceLogDetailRespVO();
        result.setNodeInstanceId(nodeInstanceId);
        result.setTaskName(nodeInstance.getName());
        result.setStatus(nodeInstance.getStatus());
        result.setStatusName(getInstanceStatusName(nodeInstance.getStatus()));
        result.setCurrentStatus(getInstanceCurrentStatus(nodeInstance.getStatus()));
        result.setStartTime(nodeInstance.getStartTime());
        result.setRefreshTime(new Date());
        result.setDuration(getNodeInstanceDuration(nodeInstance));
        result.setLogList(logLines);
        result.setLog(logContent);
        return result;
    }

    @Override
    public DppEtlNodeInstanceStatisticsRespVO getStatistics(
            Long projectId, String projectCode, String taskType) {
        Date now = new Date();
        Date beginOfDay = DateUtil.beginOfDay(now);
        Date endOfDay = DateUtil.endOfDay(now);
        long allCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType));
        long runningCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .in(DppEtlNodeInstanceDO::getStatus, "0", "1", "12"));
        long successCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .eq(DppEtlNodeInstanceDO::getStatus, "7"));
        long failCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .eq(DppEtlNodeInstanceDO::getStatus, "6"));
        long todayExecuteCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .ge(DppEtlNodeInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlNodeInstanceDO::getStartTime, endOfDay));
        long todayErrorCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .eq(DppEtlNodeInstanceDO::getStatus, "6")
                .ge(DppEtlNodeInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlNodeInstanceDO::getStartTime, endOfDay));
        long todaySuccessCount = this.count(buildStatisticsQuery(projectId, projectCode, taskType)
                .eq(DppEtlNodeInstanceDO::getStatus, "7")
                .ge(DppEtlNodeInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlNodeInstanceDO::getStartTime, endOfDay));
        long todayCompletedCount = todaySuccessCount + todayErrorCount;
        BigDecimal successRate = todayCompletedCount == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(todaySuccessCount).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(todayCompletedCount), 2, RoundingMode.HALF_UP);
        DppEtlNodeInstanceStatisticsRespVO result = new DppEtlNodeInstanceStatisticsRespVO();
        result.setAllCount(allCount);
        result.setRunningCount(runningCount);
        result.setSuccessCount(successCount);
        result.setFailCount(failCount);
        result.setTodayErrorCount(todayErrorCount);
        result.setTodayExecuteCount(todayExecuteCount);
        result.setTodaySuccessRate(successRate);
        result.setRefreshTime(now);
        return result;
    }

    private LambdaQueryWrapper<DppEtlNodeInstanceDO> buildStatisticsQuery(
            Long projectId, String projectCode, String taskType) {
        return Wrappers.lambdaQuery(DppEtlNodeInstanceDO.class)
                .eq(projectId != null, DppEtlNodeInstanceDO::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(projectCode), DppEtlNodeInstanceDO::getProjectCode, projectCode)
                .eq(StringUtils.isNotBlank(taskType), DppEtlNodeInstanceDO::getTaskType, taskType);
    }

    private void fillInstanceDisplayFields(DppEtlNodeInstanceDO instance) {
        instance.setCurrentStatus(getInstanceCurrentStatus(instance.getStatus()));
        instance.setCurrentStatusName(getInstanceStatusName(instance.getStatus()));
        instance.setDuration(getNodeInstanceDuration(instance));
    }

    private String getNodeInstanceDuration(DppEtlNodeInstanceDO instance) {
        if (instance == null || instance.getStartTime() == null) return null;
        Date endTime = isRunningInstanceStatus(instance.getStatus()) ? new Date() : instance.getEndTime();
        if (endTime == null) return null;
        return DateUtils.format2Duration(Math.max(0L, endTime.getTime() - instance.getStartTime().getTime()));
    }

    private boolean isRunningInstanceStatus(String status) {
        return "0".equals(status) || "1".equals(status) || "12".equals(status);
    }

    private String getInstanceCurrentStatus(String status) {
        if (isRunningInstanceStatus(status)) return "running";
        if ("7".equals(status)) return "success";
        if ("6".equals(status)) return "failed";
        return "idle";
    }

    private String getInstanceStatusName(String status) {
        if (isRunningInstanceStatus(status)) return "Running";
        if ("7".equals(status)) return "Success";
        if ("6".equals(status)) return "Failed";
        if ("5".equals(status)) return "Stopped";
        return "Idle";
    }

    private String guessLogLevel(String content) {
        if (StringUtils.containsIgnoreCase(content, "ERROR")) return "ERROR";
        if (StringUtils.containsIgnoreCase(content, "WARN")) return "WARN";
        if (StringUtils.containsIgnoreCase(content, "DEBUG")) return "DEBUG";
        return "INFO";
    }
}
