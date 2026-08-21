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

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DSExecuteDTO;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlExecutorService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.common.enums.Flag;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeInstanceRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskInstanceMapper;
import tech.qiantong.qdata.module.dpp.service.etl.*;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * Data Integration Task Instance Service business layer processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskInstanceServiceImpl extends ServiceImpl<DppEtlTaskInstanceMapper, DppEtlTaskInstanceDO> implements IDppEtlTaskInstanceService, DppEtlTaskInstanceService {
    @Resource
    private DppEtlTaskInstanceMapper dppEtlTaskInstanceMapper;

    @Resource
    private IAttProjectApi attProjectApi;

    @Resource
    private IDppEtlTaskService dppEtlTaskService;

    @Resource
    private IDppEtlTaskLogService dppEtlTaskLogService;

    @Resource
    private IDsEtlExecutorService dsEtlExecutorService;

    @Resource
    private IDppEtlNodeInstanceService dppEtlTNodeInstanceService;


    @Resource
    private IRedisService redisService;

    @Resource
    private IDppEtlTaskInstanceLogService dppEtlTaskInstanceLogService;

    @Resource
    private IDppEtlNodeInstanceLogService dppEtlNodeInstanceLogService;


    @Resource
    private IDppEtlNodeLogService dppEtlNodeLogService;

    @Resource
    private IDppEtlTaskNodeRelService iDppEtlTaskNodeRelService;

    @Override
    public PageResult<DppEtlTaskInstanceDO> getDppEtlTaskInstancePage(DppEtlTaskInstancePageReqVO pageReqVO) {
        PageResult<DppEtlTaskInstanceDO> page = dppEtlTaskInstanceMapper.selectPage(pageReqVO);
        if (page.getRows() != null) {
            page.getRows().forEach(this::fillInstanceDisplayFields);
        }
        return page;
    }

    @Override
    public DppEtlTaskInstanceRespVO getDppEtlTaskInstanceById(DppEtlTaskInstancePageReqVO reqVO) {
//        MPJLambdaWrapper<DppEtlTaskInstanceDO> wrapper = new MPJLambdaWrapper<>();
//        wrapper.selectAll(DppEtlTaskInstanceDO.class)
//                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()), DppEtlTaskInstanceDO::getTaskCode, reqVO.getTaskCode())
//                .orderByStr(true,
//                       false,
//                        Arrays.asList( "create_time","id"));
//        List<DppEtlTaskInstanceDO> dppEtlTaskInstanceDOList = dppEtlTaskInstanceMapper.selectList(wrapper);
//        if (CollectionUtils.isNotEmpty(dppEtlTaskInstanceDOList)){
//            return BeanUtils.toBean(dppEtlTaskInstanceDOList.get(0), DppEtlTaskInstanceRespVO.class);
//
//        }
        DppEtlTaskInstanceDO dictType = BeanUtils.toBean(reqVO, DppEtlTaskInstanceDO.class);

        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceMapper.selectOneNew(dictType);

        return BeanUtils.toBean(dppEtlTaskInstanceDO, DppEtlTaskInstanceRespVO.class);
    }

    @Override
    public Long createDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO createReqVO) {
        DppEtlTaskInstanceDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskInstanceDO.class);
        dppEtlTaskInstanceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO updateReqVO) {
        // Validate

        // Update data integration task instance
        DppEtlTaskInstanceDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskInstanceDO.class);
        return dppEtlTaskInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlTaskInstance(Collection<Long> idList) {
        // Batch delete data integration task instance
        return dppEtlTaskInstanceMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskInstanceDO getDppEtlTaskInstanceById(Long id) {
        return dppEtlTaskInstanceMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskInstanceDO> getDppEtlTaskInstanceList() {
        return dppEtlTaskInstanceMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskInstanceDO> getDppEtlTaskInstanceMap() {
        List<DppEtlTaskInstanceDO> dppEtlTaskInstanceList = dppEtlTaskInstanceMapper.selectList();
        return dppEtlTaskInstanceList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskInstanceDO::getId,
                        dppEtlTaskInstanceDO -> dppEtlTaskInstanceDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data integration task instance data
     *
     * @param importExcelList data integration task instance data list
     * @param isUpdateSupport whether to support update; if already exists, update the data
     * @param operName        operator user
     * @return result
     */
    @Override
    public String importDppEtlTaskInstance(List<DppEtlTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlTaskInstanceRespVO respVO : importExcelList) {
            try {
                DppEtlTaskInstanceDO dppEtlTaskInstanceDO = BeanUtils.toBean(respVO, DppEtlTaskInstanceDO.class);
                Long dppEtlTaskInstanceId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlTaskInstanceId != null) {
                        DppEtlTaskInstanceDO existingDppEtlTaskInstance = dppEtlTaskInstanceMapper.selectById(dppEtlTaskInstanceId);
                        if (existingDppEtlTaskInstance != null) {
                            dppEtlTaskInstanceMapper.updateById(dppEtlTaskInstanceDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dppEtlTaskInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.task.instance", "Data integration task instance")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dppEtlTaskInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.task.instance", "Data integration task instance")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DppEtlTaskInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskInstanceId);
                    DppEtlTaskInstanceDO existingDppEtlTaskInstance = dppEtlTaskInstanceMapper.selectOne(queryWrapper);
                    if (existingDppEtlTaskInstance == null) {
                        dppEtlTaskInstanceMapper.insert(dppEtlTaskInstanceDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dppEtlTaskInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.task.instance", "Data integration task instance")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dppEtlTaskInstanceId, MessageUtils.messageWithFallback("dpp.entity.etl.task.instance", "Data integration task instance")));
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
    public Boolean createTaskInstance(ProcessInstance processInstance) {
        log.info(JSONObject.toJSONString(processInstance));
        DppEtlTaskRespDTO dppEtlTaskRespDTO = dppEtlTaskService.getTaskByTaskCode(String.valueOf(processInstance.getProcessDefinitionCode()));
        if (dppEtlTaskRespDTO == null) {
            return true;
        }
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = DppEtlTaskInstanceDO.builder()
                .id(processInstance.getId())
                .catId(dppEtlTaskRespDTO.getCatId())
                .catCode(dppEtlTaskRespDTO.getCatCode())
                .taskType(dppEtlTaskRespDTO.getType())
                .name(processInstance.getName())
                .taskId(dppEtlTaskRespDTO.getId())
                .taskCode(String.valueOf(processInstance.getProcessDefinitionCode()))
                .taskVersion(processInstance.getProcessDefinitionVersion())
                .projectId(attProjectApi.getProjectIdByProjectCode(String.valueOf(processInstance.getProjectCode())))
                .projectCode(String.valueOf(processInstance.getProjectCode()))
                .scheduleTime(processInstance.getCommandStartTime())
                .startTime(processInstance.getStartTime())
                .endTime(processInstance.getEndTime())
                .runTimes(processInstance.getRunTimes())
                .commandType(String.valueOf(processInstance.getCommandType().getCode()))
                .maxTryTimes(processInstance.getMaxTryTimes())
                .failureStrategy(String.valueOf(processInstance.getFailureStrategy().getCode()))
                .subTaskFlag(String.valueOf(processInstance.getIsSubProcess().getCode()))
                .status(String.valueOf(processInstance.getState().getCode()))
                .statusHistory(processInstance.getStateHistory())
                .personCharge(dppEtlTaskRespDTO.getPersonCharge())
                .contactNumber(dppEtlTaskRespDTO.getContactNumber())
                .dsId(processInstance.getId())
                .build();
        if (processInstance.getIsSubProcess().getCode() == Flag.YES.getCode() && StringUtils.isNotEmpty(processInstance.getCommandParam())) {
            JSONObject commandParam = JSONObject.parseObject(processInstance.getCommandParam());
            if (commandParam.containsKey("parentProcessInstanceId")) {
                dppEtlTaskInstanceDO.setParentTaskInstanceId(commandParam.getLong("parentProcessInstanceId"));
            }
            if (commandParam.containsKey("parentTaskInstanceId")) {
                dppEtlTaskInstanceDO.setParentNodeInstanceId(commandParam.getLong("parentTaskInstanceId"));
            }
        }
        return this.save(dppEtlTaskInstanceDO);
    }

    @Override
    public Boolean updateTaskInstance(ProcessInstance processInstance) {
        log.info(JSONObject.toJSONString(processInstance));
        DppEtlTaskInstanceDO old = this.getById(processInstance.getId());
        if (old == null) {
            return true;
        }
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = DppEtlTaskInstanceDO.builder()
                .id(old.getId())
                .scheduleTime(processInstance.getCommandStartTime())
                .startTime(processInstance.getStartTime())
                .endTime(processInstance.getEndTime())
                .status(String.valueOf(processInstance.getState().getCode()))
                .statusHistory(processInstance.getStateHistory())
                .subTaskFlag(String.valueOf(processInstance.getIsSubProcess().getCode()))
                .runTimes(processInstance.getRunTimes())
                .commandType(processInstance.getCommandType() != null ? String.valueOf(processInstance.getCommandType().getCode()) : null)
                .build();
        if (processInstance.getIsSubProcess().getCode() == Flag.YES.getCode() && StringUtils.isNotEmpty(processInstance.getCommandParam())) {
            JSONObject commandParam = JSONObject.parseObject(processInstance.getCommandParam());
            if (commandParam.containsKey("parentProcessInstanceId")) {
                dppEtlTaskInstanceDO.setParentTaskInstanceId(commandParam.getLong("parentProcessInstanceId"));
            }
            if (commandParam.containsKey("parentTaskInstanceId")) {
                dppEtlTaskInstanceDO.setParentNodeInstanceId(commandParam.getLong("parentTaskInstanceId"));
            }
        }
        return this.saveOrUpdate(dppEtlTaskInstanceDO);
    }

    @Override
    public DppEtlTaskInstanceDO getByDsId(Long dsId) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(DppEtlTaskInstanceDO::getDsId, dsId));
    }

    @Override
    public Long getIdByDsId(Long dsId) {
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(DppEtlTaskInstanceDO::getDsId, dsId));
        if (dppEtlTaskInstanceDO != null) {
            return dppEtlTaskInstanceDO.getId();
        }
        return null;
    }

    @Override
    public PageResult<DppEtlTaskInstanceTreeListRespVO> treeList(DppEtlTaskInstanceTreeListReqVO reqVO) {
        if (StringUtils.isNotEmpty(reqVO.getStartTime())) {
            reqVO.setStartTime(reqVO.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(reqVO.getEndTime())) {
            reqVO.setEndTime(reqVO.getEndTime() + " 23:59:59");
        }
        IPage<DppEtlTaskInstanceTreeListRespVO> page = baseMapper.treeList(new Page(reqVO.getPageNum(), reqVO.getPageSize()), reqVO);
        if (page != null && page.getRecords() != null && page.getRecords().size() > 0) {
            for (DppEtlTaskInstanceTreeListRespVO record : page.getRecords()) {
                record.setDataId("1_" + record.getId());
                if (record.getStartTime() != null && record.getEndTime() != null) {
                    record.setDuration(DateUtils.format2Duration(record.getEndTime().getTime() - record.getStartTime().getTime()));
                }
                if (record.getChildren() != null && record.getChildren().size() > 0) {
                    for (DppEtlTaskInstanceTreeListRespVO child : record.getChildren()) {
                        child.setDataId(child.getDataType() + "_" + child.getId());
                        if (child.getStartTime() != null && child.getEndTime() != null) {
                            child.setDuration(DateUtils.format2Duration(child.getEndTime().getTime() - child.getStartTime().getTime()));
                        }
                        // Check if it is a sub-task
                        if (StringUtils.equals(String.valueOf(TaskComponentTypeEnum.SUB_PROCESS), child.getNodeType())) {
                            child.setHasChildren(true);
                        }
                    }

                }
            }
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public AjaxResult execute(Long taskInstanceId, ExecuteType executeType) {
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = this.getById(taskInstanceId);
        if (dppEtlTaskInstanceDO == null) {
            return error(MessageUtils.messageWithFallback("dpp.error.task.instance.notfound.refresh",
                    "Task instance does not exist; refresh and try again"));
        }
        String status = dppEtlTaskInstanceDO.getStatus();
        if (ExecuteType.REPEAT_RUNNING.getCode() == executeType.getCode() && !StringUtils.equals(status, "3") &&
                !StringUtils.equals(status, "5") &&
                !StringUtils.equals(status, "6") &&
                !StringUtils.equals(status, "7")) {
            return error(MessageUtils.messageWithFallback("dpp.error.task.instance.rerun.status.invalid",
                    "The task instance cannot be rerun in its current state; refresh and try again"));
        }
        if (ExecuteType.STOP.getCode() == executeType.getCode() &&
                !StringUtils.equals(status, "0") &&
                !StringUtils.equals(status, "1") &&
                !StringUtils.equals(status, "2") &&
                !StringUtils.equals(status, "3") &&
                !StringUtils.equals(status, "12") &&
                !StringUtils.equals(status, "14")) {
            return error(MessageUtils.messageWithFallback("dpp.error.task.instance.stop.status.invalid",
                    "The task instance cannot be stopped in its current state; refresh and try again"));
        }
        DsStatusRespDTO dsStatusRespDTO = dsEtlExecutorService.execute(DSExecuteDTO.builder()
                .processInstanceId(taskInstanceId)
                .executeType(executeType)
                .build(), dppEtlTaskInstanceDO.getProjectCode());
        return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
    }

    @Override
    public List<DppEtlTaskInstanceTreeListRespVO> subNodelist(Long taskInstanceId, Long nodeInstanceId) {
        List<DppEtlTaskInstanceTreeListRespVO> list = baseMapper.listSubNodeInstance(taskInstanceId, nodeInstanceId);
        if (list != null && list.size() > 0) {
            list.stream().forEach(e -> {
                e.setDataId("3_" + e.getId());
                if (e.getStartTime() != null && e.getEndTime() != null) {
                    e.setDuration(DateUtils.format2Duration(e.getEndTime().getTime() - e.getStartTime().getTime()));
                }
            });
        }
        return list;
    }

    @Override
    public DppEtlTaskInstanceLogStatusRespDTO getLogByTaskInstanceId(Long taskInstanceId) {
        String log = "";
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = this.getById(taskInstanceId);
        if (dppEtlTaskInstanceDO == null) {
            throw new ServiceException("Task instance does not exist");
        }
        // Get node data
        List<DppEtlNodeInstanceDO> dppEtlNodeInstanceDOList = dppEtlTNodeInstanceService.list(Wrappers.lambdaQuery(DppEtlNodeInstanceDO.class)
                .select(DppEtlNodeInstanceDO::getId,
                        DppEtlNodeInstanceDO::getNodeCode,
                        DppEtlNodeInstanceDO::getName,
                        DppEtlNodeInstanceDO::getStatus)
                .eq(DppEtlNodeInstanceDO::getTaskInstanceId, taskInstanceId));

        String processInstanceLogKey = TaskConverter.PROCESS_INSTANCE_LOG_KEY + taskInstanceId;
        if (StringUtils.equals("1", dppEtlTaskInstanceDO.getTaskType())) {// Check if it is an offline task
            if (redisService.hasKey(processInstanceLogKey)) {
                log = redisService.get(processInstanceLogKey);
            } else {
                // Get logs from the table
                String logContent = dppEtlTaskInstanceLogService.getLog(taskInstanceId);
                if (logContent != null) {
                    log = logContent;
                }
            }
        } else if (StringUtils.equals("3", dppEtlTaskInstanceDO.getTaskType())) {
            // Data-development JDBC tasks persist their realtime output as node logs and
            // do not have a DolphinScheduler task-log locations payload. Aggregate the
            // node logs directly, matching the data-development instance log dialog.
            for (DppEtlNodeInstanceDO nodeInstance : dppEtlNodeInstanceDOList) {
                String taskInstanceLogKey = TaskConverter.TASK_INSTANCE_LOG_KEY + nodeInstance.getId();
                if (redisService.hasKey(taskInstanceLogKey)) {
                    log += redisService.get(taskInstanceLogKey) + "\n";
                } else {
                    String logContent = dppEtlNodeInstanceLogService.getLog(nodeInstance.getId());
                    if (logContent != null) {
                        log += logContent + "\n";
                    }
                }
            }
        } else {
            // Integration tasks use the saved task definition to keep node logs ordered.
            DppEtlTaskLogRespVO dppEtlTaskLogRespVO = dppEtlTaskLogService.getDppEtlTaskLogById(DppEtlTaskLogPageReqVO.builder()
                    .code(dppEtlTaskInstanceDO.getTaskCode())
                    .version(dppEtlTaskInstanceDO.getTaskVersion())
                    .build());
            if (dppEtlTaskLogRespVO == null || StringUtils.isBlank(dppEtlTaskLogRespVO.getLocations())) {
                throw new RuntimeException(MessageUtils.messageWithFallback(
                        "dpp.error.task.notfound", "Task does not exist"));
            }
            JSONArray locations = JSONArray.parse(dppEtlTaskLogRespVO.getLocations());
            Map<String, DppEtlNodeInstanceDO> nodeInstanceMap = dppEtlNodeInstanceDOList.stream().collect(Collectors.toMap(key -> key.getNodeCode(), value -> value));

            for (int i = 0; i < locations.size(); i++) {
                JSONObject location = (JSONObject) locations.get(i);
                String code = String.valueOf(location.getLong("taskCode"));
                DppEtlNodeInstanceDO dppEtlNodeInstanceDO = nodeInstanceMap.get(code);

                if (dppEtlNodeInstanceDO != null) {
                    String taskInstanceLogKey = TaskConverter.TASK_INSTANCE_LOG_KEY + dppEtlNodeInstanceDO.getId();
                    if (redisService.hasKey(taskInstanceLogKey)) {
                        log += redisService.get(taskInstanceLogKey) + "\n";
                    } else {
                        // Get logs from the table
                        String logContent = dppEtlNodeInstanceLogService.getLog(dppEtlNodeInstanceDO.getId());
                        if (logContent != null) {
                            log += logContent + "\n";
                        }
                    }
                }
            }
        }


        return DppEtlTaskInstanceLogStatusRespDTO.builder()
                .log(log)
                .status(dppEtlTaskInstanceDO.getStatus())
                .nodeInstanceList(BeanUtils.toBean(dppEtlNodeInstanceDOList, DppEtlNodeInstanceRespDTO.class))
                .build();
    }

    @Override
    public DppEtlTaskInstanceLogDetailRespVO getLogDetailByTaskInstanceId(Long taskInstanceId) {
        DppEtlTaskInstanceDO taskInstance = this.getById(taskInstanceId);
        if (taskInstance == null) {
            throw new ServiceException("Task instance does not exist");
        }
        DppEtlTaskInstanceLogStatusRespDTO logStatus = getLogByTaskInstanceId(taskInstanceId);
        String logContent = logStatus == null || logStatus.getLog() == null ? "" : logStatus.getLog();
        List<DppEtlTaskInstanceLogLineRespVO> logLines = new ArrayList<>();
        String[] lines = logContent.split("\\r?\\n");
        for (String line : lines) {
            if (StringUtils.isBlank(line)) continue;
            DppEtlTaskInstanceLogLineRespVO item = new DppEtlTaskInstanceLogLineRespVO();
            item.setLineNo(logLines.size() + 1);
            item.setLevel(guessLogLevel(line));
            item.setContent(line);
            item.setDetailContent(line);
            logLines.add(item);
        }
        DppEtlTaskInstanceLogDetailRespVO result = new DppEtlTaskInstanceLogDetailRespVO();
        result.setTaskInstanceId(taskInstanceId);
        result.setTaskName(taskInstance.getName());
        result.setStatus(taskInstance.getStatus());
        result.setStatusName(getInstanceStatusName(taskInstance.getStatus()));
        result.setCurrentStatus(getInstanceCurrentStatus(taskInstance.getStatus()));
        result.setStartTime(taskInstance.getStartTime());
        result.setRefreshTime(new Date());
        result.setDuration(getTaskInstanceDuration(taskInstance));
        result.setLogList(logLines);
        result.setLog(logContent);
        return result;
    }

    private String guessLogLevel(String content) {
        if (StringUtils.containsIgnoreCase(content, "ERROR")
                || content.contains("失败") || content.contains("异常")) return "ERROR";
        if (StringUtils.containsIgnoreCase(content, "WARN") || content.contains("警告")) return "WARN";
        if (StringUtils.containsIgnoreCase(content, "DEBUG")) return "DEBUG";
        return "INFO";
    }

    @Override
    public Long getRunTaskInstance(Long taskId) {
        List<DppEtlTaskInstanceDO> dppEtlTaskInstanceDO = this.list(Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(DppEtlTaskInstanceDO::getTaskId, taskId)
                .in(DppEtlTaskInstanceDO::getStatus, "0", "1", "12")
                .orderByDesc(DppEtlTaskInstanceDO::getStartTime));
        if (dppEtlTaskInstanceDO.size() > 0) {
            return dppEtlTaskInstanceDO.get(0).getId();
        }
        return null;
    }

    @Override
    public DppEtlTaskUpdateQueryRespVO getTaskInfo(Long id) {
        // Get task info by task instance ID
        MPJLambdaWrapper<DppEtlTaskInstanceDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DppEtlTaskInstanceDO.class)
                .select("t3.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DppEtlTaskInstanceDO::getId, id);
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceMapper.selectJoinOne(DppEtlTaskInstanceDO.class, lambdaWrapper);

        // Get task info
        DppEtlTaskLogRespVO dppEtlTaskLogRespVO = dppEtlTaskLogService.getDppEtlTaskLogById(DppEtlTaskLogPageReqVO.builder()
                .code(dppEtlTaskInstanceDO.getTaskCode())
                .version(dppEtlTaskInstanceDO.getTaskVersion())
                .build());
        if (dppEtlTaskLogRespVO == null) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "dpp.error.task.notfound", "Task does not exist"));
        }
        DppEtlTaskUpdateQueryRespVO bean = new DppEtlTaskUpdateQueryRespVO(BeanUtils.toBean(dppEtlTaskLogRespVO, DppEtlTaskDO.class));
        bean.setTaskInstance(dppEtlTaskInstanceDO);
        // Get relation data
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.getDppEtlTaskNodeRelRespVOList(DppEtlTaskNodeRelPageReqVO.builder()
                .taskCode(bean.getCode())
                .taskVersion(bean.getVersion())
                .build());
        bean.setTaskRelationJsonFromNodeRelList(dppEtlTaskNodeRelRespVOList);

        // Get node info
        List<DppEtlNodeLogDO> dppEtlNodeLogDOList = dppEtlNodeLogService.listByTaskCode(dppEtlTaskInstanceDO.getTaskCode(), dppEtlTaskInstanceDO.getTaskVersion());
        bean.setTaskDefinitionList(BeanUtils.toBean(dppEtlNodeLogDOList, DppEtlNodeRespVO.class));
        bean.createTaskConfig();
        return bean;
    }

    @Override
    public DppEtlTaskInstanceDO getLastTaskInstanceByTaskCode(String code) {
        IPage<DppEtlTaskInstanceDO> page = this.page(new Page(1, 1), Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(DppEtlTaskInstanceDO::getTaskCode, code)
                .orderByDesc(DppEtlTaskInstanceDO::getStartTime));
        if (page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        }
        return null;
    }
    @Override
    public List<DppEtlTaskInstanceRespDTO> getLastTaskInstance(List<Long> taskIdList) {
        List<DppEtlTaskInstanceDO> dppEtlTaskInstanceDO = dppEtlTaskInstanceMapper.getLastTaskInstance(taskIdList);
        return BeanUtils.toBean(dppEtlTaskInstanceDO, DppEtlTaskInstanceRespDTO.class);
    }

    @Override
    public DppEtlTaskInstanceStatisticsRespVO getStatistics(
            Long projectId, String projectCode, Long taskId, String taskType) {
        Date now = new Date();
        Date beginOfDay = DateUtil.beginOfDay(now);
        Date endOfDay = DateUtil.endOfDay(now);
        long allCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType));
        long runningCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .in(DppEtlTaskInstanceDO::getStatus, "0", "1", "12"));
        long successCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .eq(DppEtlTaskInstanceDO::getStatus, "7"));
        long failCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .eq(DppEtlTaskInstanceDO::getStatus, "6"));
        long todayExecuteCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .ge(DppEtlTaskInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlTaskInstanceDO::getStartTime, endOfDay));
        long todayErrorCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .eq(DppEtlTaskInstanceDO::getStatus, "6")
                .ge(DppEtlTaskInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlTaskInstanceDO::getStartTime, endOfDay));
        long todaySuccessCount = this.count(buildStatisticsQuery(projectId, projectCode, taskId, taskType)
                .eq(DppEtlTaskInstanceDO::getStatus, "7")
                .ge(DppEtlTaskInstanceDO::getStartTime, beginOfDay)
                .le(DppEtlTaskInstanceDO::getStartTime, endOfDay));
        long todayCompletedCount = todaySuccessCount + todayErrorCount;
        BigDecimal successRate = todayCompletedCount == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(todaySuccessCount).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(todayCompletedCount), 2, RoundingMode.HALF_UP);
        DppEtlTaskInstanceStatisticsRespVO result = new DppEtlTaskInstanceStatisticsRespVO();
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

    private LambdaQueryWrapper<DppEtlTaskInstanceDO> buildStatisticsQuery(
            Long projectId, String projectCode, Long taskId, String taskType) {
        return Wrappers.lambdaQuery(DppEtlTaskInstanceDO.class)
                .eq(projectId != null, DppEtlTaskInstanceDO::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(projectCode), DppEtlTaskInstanceDO::getProjectCode, projectCode)
                .eq(taskId != null, DppEtlTaskInstanceDO::getTaskId, taskId)
                .eq(StringUtils.isNotBlank(taskType), DppEtlTaskInstanceDO::getTaskType, taskType);
    }

    private void fillInstanceDisplayFields(DppEtlTaskInstanceDO instance) {
        instance.setCurrentStatus(getInstanceCurrentStatus(instance.getStatus()));
        instance.setCurrentStatusName(getInstanceStatusName(instance.getStatus()));
        instance.setDuration(getTaskInstanceDuration(instance));
    }

    private String getTaskInstanceDuration(DppEtlTaskInstanceDO instance) {
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

}
