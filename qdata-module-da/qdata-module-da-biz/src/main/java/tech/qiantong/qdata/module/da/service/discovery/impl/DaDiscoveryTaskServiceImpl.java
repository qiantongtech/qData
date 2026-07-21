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

package tech.qiantong.qdata.module.da.service.discovery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskDefinition;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.service.IDaDiscoveryTaskApiService;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryTaskMapper;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTableService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskService;
import tech.qiantong.qdata.module.da.utils.DaTaskConverter;
import tech.qiantong.qdata.module.da.utils.model.TaskSaveReqInput;
import tech.qiantong.qdata.quartz.domain.SysJob;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * Data Discovery Task Service business layer processing
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryTaskServiceImpl extends ServiceImpl<DaDiscoveryTaskMapper, DaDiscoveryTaskDO> implements IDaDiscoveryTaskService, IDaDiscoveryTaskApiService {

    private static String projectCode;

    @Value("${ds.http_projectCode:}")
    private void setDefaultProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }


    @Resource
    private IDsEtlSchedulerService iDsEtlSchedulerService;

    @Resource
    private DaDiscoveryTaskMapper daDiscoveryTaskMapper;
    @Resource
    @Lazy
    private IDaDatasourceService iDaDatasourceService;

    @Autowired
    @Lazy
    private IDaDiscoveryTableService iDaDiscoveryTableService;

    @Resource
    private IDsEtlNodeService dsEtlNodeService;
    @Resource
    private IDsEtlTaskService dsEtlTaskService;

    @Override
    public PageResult<DaDiscoveryTaskDO> getDaDiscoveryTaskPage(DaDiscoveryTaskPageReqVO pageReqVO) {
        return daDiscoveryTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<DaDiscoveryTaskRespVO> getDaDiscoveryTaskListPage(DaDiscoveryTaskPageReqVO pageReqVO) {
        PageResult<DaDiscoveryTaskDO> daDiscoveryTaskDOPageResult = daDiscoveryTaskMapper.selectPage(pageReqVO);
        PageResult<DaDiscoveryTaskRespVO> pageResult = BeanUtils.toBean(daDiscoveryTaskDOPageResult, DaDiscoveryTaskRespVO.class);
        List<DaDiscoveryTaskRespVO> rows = (List<DaDiscoveryTaskRespVO>) pageResult.getRows();
        if (CollectionUtils.isEmpty(rows)) {
            return pageResult;
        }
        List<DaDatasourceDO> daDatasourceList = iDaDatasourceService.getDaDatasourceList();
        for (DaDiscoveryTaskRespVO row : rows) {
            DaDatasourceDO daDatasourceById = this.getDaDatasourceById(row.getDatasourceId(), daDatasourceList);
            daDatasourceById = daDatasourceById != null ? daDatasourceById : new DaDatasourceDO();
            row.setDatasourceName(daDatasourceById.getDatasourceName());
            row.setDatasourceType(daDatasourceById.getDatasourceType());

            //Scheduling task encapsulation related

        }
        pageResult.setRows(rows);
        return pageResult;
    }

    private DaDatasourceDO getDaDatasourceById(Long datasourceId, List<DaDatasourceDO> daDatasourceList) {
        if (CollectionUtils.isEmpty(daDatasourceList)) {
            return new DaDatasourceDO();
        }
        for (DaDatasourceDO daDatasourceDO : daDatasourceList) {
            if (daDatasourceDO.getId() == datasourceId) {
                return daDatasourceDO;
            }
        }
        return new DaDatasourceDO();
    }

    @Override
    public Long createDaDiscoveryTask(DaDiscoveryTaskSaveReqVO createReqVO) {
        DaDiscoveryTaskDO dictType = BeanUtils.toBean(createReqVO, DaDiscoveryTaskDO.class);
//        SysJob sysJob = daDiscoveryTaskDOToSysJob(createReqVO);
//        try {
//            Long jobReturnId = iSysJobService.insertJobReturnId(sysJob);
//            dictType.setSystemJobId(jobReturnId);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        } catch (TaskException e) {
//            throw new RuntimeException(e);
//        }
        MPJLambdaWrapper<DaDiscoveryTaskDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.eq(DaDiscoveryTaskDO::getName, createReqVO.getName());
        Long count = daDiscoveryTaskMapper.selectCount(mpjLambdaWrapper);
        if (count != null && count > 0) {
            throw new ServiceException("da.error.task.name.duplicate", "The task name already exists. Creation failed.");
        }


        //TODO Store data, ensure testing, delete when integrating tasks
        dictType.setNodeCode("0");
        dictType.setNodeId(0L);
        dictType.setTaskId(0L);
        dictType.setTaskCode("0");
        daDiscoveryTaskMapper.insert(dictType);
        return dictType.getId();
    }

    public static SysJob daDiscoveryTaskDOToSysJob(DaDiscoveryTaskSaveReqVO daDiscoveryTaskDO) {
        SysJob sysJob = new SysJob();
        sysJob.setJobName(daDiscoveryTaskDO.getName());
        sysJob.setJobGroup(daDiscoveryTaskDO.getJobGroup());
        sysJob.setInvokeTarget("daDatasourceServiceImpl.detectTableSchemaUpdates(" + daDiscoveryTaskDO.getId() + "L)");
        sysJob.setCronExpression(daDiscoveryTaskDO.getCronExpression());
        sysJob.setMisfirePolicy(daDiscoveryTaskDO.getMisfirePolicy());
        sysJob.setConcurrent(daDiscoveryTaskDO.getConcurrent());
        sysJob.setStatus(daDiscoveryTaskDO.getStatus());
        return sysJob;
    }

    @Override
    public int updateDaDiscoveryTask(DaDiscoveryTaskSaveReqVO updateReqVO) {
//        // Related validation
//        SysJob sysJob = daDiscoveryTaskDOToSysJob(updateReqVO);
//
//        try {
//            iSysJobService.updateJobReturnId(sysJob);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        } catch (TaskException e) {
//            throw new RuntimeException(e);
//        }

        MPJLambdaWrapper<DaDiscoveryTaskDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.eq(DaDiscoveryTaskDO::getName, updateReqVO.getName());
        mpjLambdaWrapper.ne(DaDiscoveryTaskDO::getId, updateReqVO.getId());
        Long count = daDiscoveryTaskMapper.selectCount(mpjLambdaWrapper);
        if (count != null && count > 0) {
            throw new ServiceException("da.error.task.name.duplicate", "The task name already exists. Creation failed.");
        }

        // Update data discovery task
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTaskDO.class);
        DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(updateReqVO.getId());
        if (StringUtils.equals(daDiscoveryTaskDO.getCronExpression(), updateReqVO.getCronExpression())) {
            this.updateDaDiscoveryTaskCronExpression(updateReqVO);
            return 1;
        }

        return daDiscoveryTaskMapper.updateById(updateObj);
    }


    @Override
    public boolean runDaDiscoveryTask(Long taskId) {
//        Long systemJobId = discoveryTaskPageReqVO.getSystemJobId();
//        SysJob sysJob = iSysJobService.selectJobById(systemJobId);
//        try {
//            return iSysJobService.run(sysJob);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
        iDaDatasourceService.detectTableSchemaUpdates(taskId);
        return true;
    }

    @Override
    public int updateDaDiscoveryTask(DaDiscoveryTaskRespVO updateReqVO) {
        // Related validation

        // Update data discovery task
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTaskDO.class);
        return daDiscoveryTaskMapper.updateById(updateObj);
    }

    @Override
    public int removeDaDiscoveryTask(Collection<Long> idList) {
//        // Iterate over each id in idList
//        for (Long id : idList) {
//            // Query DaDiscoveryTaskDO detail
//            DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
//
//            if (daDiscoveryTaskDO != null && daDiscoveryTaskDO.getSystemJobId() != null) {
//                // Extract systemJobId
//                Long systemJobId = daDiscoveryTaskDO.getSystemJobId();
//                SysJob sysJob = iSysJobService.selectJobById(systemJobId);
//                if(sysJob != null){
//                    try{
//                        iSysJobService.deleteJob(sysJob);
//                    } catch (SchedulerException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }

        // Iterate over each id in idList
        for (Long id : idList) {
            // Query DaDiscoveryTaskDO detail
            DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
            if (daDiscoveryTaskDO != null &&
                    (daDiscoveryTaskDO.getSystemJobId() != null || !StringUtils.equals("0", daDiscoveryTaskDO.getTaskCode()))) {
                // Extract systemJobId
                if (StringUtils.equals("0", daDiscoveryTaskDO.getStatus())) {
                    throw new ServiceException("da.error.task.online.delete", "Task is online, deletion not allowed. Please take it offline first!");
                }
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.deleteTask(projectCode, daDiscoveryTaskDO.getTaskCode());
            }
        }

        // Batch delete data discovery tasks
        return daDiscoveryTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDiscoveryTaskRespVO getDaDiscoveryTaskById(Long id) {

        MPJLambdaWrapper<DaDiscoveryTaskDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.selectAll(DaDiscoveryTaskDO.class)
                .select("t2.name AS catName")
                .leftJoin("ATT_DISCOVER_TASK_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .eq(DaDiscoveryTaskDO::getId, id);
        DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectJoinOne(DaDiscoveryTaskDO.class, mpjLambdaWrapper);

        DaDiscoveryTaskRespVO bean = BeanUtils.toBean(daDiscoveryTaskDO, DaDiscoveryTaskRespVO.class);

        DaDatasourceDO daDatasourceById = iDaDatasourceService.getDaDatasourceById(bean.getDatasourceId());
        daDatasourceById = daDatasourceById == null ? new DaDatasourceDO() : daDatasourceById;
        bean.setDatasourceName(daDatasourceById.getDatasourceName());
        bean.setDatasourceType(daDatasourceById.getDatasourceType());
        bean.setIp(daDatasourceById.getIp());

        List<DaDiscoveryTableDO> daDiscoveryTableDOList = fetchDiscoveryTableList(bean);
        daDiscoveryTableDOList = daDiscoveryTableDOList == null ? new ArrayList<>() : daDiscoveryTableDOList;


        long countPending = daDiscoveryTableDOList.stream()
                .filter(item -> StringUtils.equals("1", item.getStatus()))
                .count();

        long countSubmitted = daDiscoveryTableDOList.stream()
                .filter(item -> StringUtils.equals("2", item.getStatus()))
                .count();

        //0: No, 1: Yes
        long countIgnoreFlag = daDiscoveryTableDOList.stream()
                .filter(item -> StringUtils.equals("1", item.getIgnoreFlag()))
                .count();
        bean.setCountPending(countPending);
        bean.setCountSubmitted(countSubmitted);
        bean.setCountIgnoreFlag(countIgnoreFlag);


//        Long systemJobId = bean.getSystemJobId();
//        SysJob sysJob = iSysJobService.selectJobById(systemJobId);
//        sysJob = sysJob == null ? new SysJob():sysJob;
//        bean.setMisfirePolicy(sysJob.getMisfirePolicy());
//        bean.setJobGroup(sysJob.getJobGroup());
//        bean.setConcurrent(sysJob.getConcurrent());


        return bean;
    }

    private List<DaDiscoveryTableDO> fetchDiscoveryTableList(DaDiscoveryTaskRespVO daDiscoveryTaskDO) {
        DaDiscoveryTablePageReqVO daDiscoveryTablePageReqVO = new DaDiscoveryTablePageReqVO();
        daDiscoveryTablePageReqVO.setTaskId(daDiscoveryTaskDO.getId());

        return iDaDiscoveryTableService.getDaDiscoveryTableList(daDiscoveryTablePageReqVO);
    }

    @Override
    public List<DaDiscoveryTaskDO> getDaDiscoveryTaskList() {
        return daDiscoveryTaskMapper.selectList();
    }

    @Override
    public Map<Long, DaDiscoveryTaskDO> getDaDiscoveryTaskMap() {
        List<DaDiscoveryTaskDO> daDiscoveryTaskList = daDiscoveryTaskMapper.selectList();
        return daDiscoveryTaskList.stream()
                .collect(Collectors.toMap(
                        DaDiscoveryTaskDO::getId,
                        daDiscoveryTaskDO -> daDiscoveryTaskDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data discovery task data
     *
     * @param importExcelList Data discovery task data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName        Operating user
     * @return result
     */
    @Override
    public String importDaDiscoveryTask(List<DaDiscoveryTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDiscoveryTaskRespVO respVO : importExcelList) {
            try {
                DaDiscoveryTaskDO daDiscoveryTaskDO = BeanUtils.toBean(respVO, DaDiscoveryTaskDO.class);
                Long daDiscoveryTaskId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDiscoveryTaskId != null) {
                        DaDiscoveryTaskDO existingDaDiscoveryTask = daDiscoveryTaskMapper.selectById(daDiscoveryTaskId);
                        if (existingDaDiscoveryTask != null) {
                            daDiscoveryTaskMapper.updateById(daDiscoveryTaskDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daDiscoveryTaskId, MessageUtils.messageWithFallback("da.entity.discovery.task", "Data discovery task")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daDiscoveryTaskId, MessageUtils.messageWithFallback("da.entity.discovery.task", "Data discovery task")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaDiscoveryTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDiscoveryTaskId);
                    DaDiscoveryTaskDO existingDaDiscoveryTask = daDiscoveryTaskMapper.selectOne(queryWrapper);
                    if (existingDaDiscoveryTask == null) {
                        daDiscoveryTaskMapper.insert(daDiscoveryTaskDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daDiscoveryTaskId, MessageUtils.messageWithFallback("da.entity.discovery.task", "Data discovery task")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daDiscoveryTaskId, MessageUtils.messageWithFallback("da.entity.discovery.task", "Data discovery task")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public boolean updateDaDiscoveryTaskStatus(DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        DaDiscoveryTaskRespVO daDiscoveryTaskById = this.getDaDiscoveryTaskById(daDiscoveryTask.getId());
        String daDiscoveryTaskStatus = daDiscoveryTask.getStatus();

        validateTaskStatus(daDiscoveryTaskById, daDiscoveryTaskStatus);

        daDiscoveryTask.setCronExpression(daDiscoveryTaskById.getCronExpression());
        Long systemJobId = daDiscoveryTaskById.getSystemJobId();
        if (StringUtils.equals(daDiscoveryTaskStatus, daDiscoveryTaskById.getStatus())) {
            return true;
        }

        if (StringUtils.equals("1", daDiscoveryTaskStatus)) {
            handleOfflineTask(daDiscoveryTaskById, systemJobId, daDiscoveryTask);
            return true;
        }

        handleOnlineTask(daDiscoveryTaskById, systemJobId, daDiscoveryTask);

        updateTaskStatusAndScheduler(daDiscoveryTask, systemJobId);

        return true;
    }

    private void validateTaskStatus(DaDiscoveryTaskRespVO daDiscoveryTaskById, String daDiscoveryTaskStatus) {
        if (daDiscoveryTaskById == null || daDiscoveryTaskStatus == null) {
            throw new ServiceException("da.error.template.scheduler.missing", "Task template error, scheduler info not found!");
        }
    }

    private void handleOfflineTask(DaDiscoveryTaskRespVO daDiscoveryTaskById, Long systemJobId, DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        if (daDiscoveryTaskById.getSystemJobId() != null && systemJobId > 0) {
            DsStatusRespDTO respDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(projectCode), daDiscoveryTaskById.getTaskCode());
            if (respDTO == null || !respDTO.getSuccess()) {
                throw new ServiceException("da.error.task.publish.fail", "Failed to publish or offline task!");
            }

            DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(projectCode, systemJobId);
            if (!offlined.getData()) {
                throw new ServiceException("da.error.scheduler.offline", "Failed to offline scheduler!");
            }
        }

        // Update data discovery task
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DaDiscoveryTaskDO.class);
        daDiscoveryTaskMapper.updateById(updateObj);
    }

    private void handleOnlineTask(DaDiscoveryTaskRespVO daDiscoveryTaskById, Long systemJobId, DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        if (systemJobId == null || systemJobId < 1) {
            createNewProcessDefinition(daDiscoveryTaskById, daDiscoveryTask);
        } else if (daDiscoveryTaskById.getTaskId() != null) {
            updateExistingProcessDefinition(daDiscoveryTaskById, daDiscoveryTask);
        }
    }

    private void createNewProcessDefinition(DaDiscoveryTaskRespVO daDiscoveryTaskById, DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(daDiscoveryTaskById.getName() + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", daDiscoveryTaskById.getId());
        input.setId(daDiscoveryTaskById.getId());
        ProcessDefinition definition = this.createProcessDefinition(input);
        TaskDefinition firstTaskDefinition = DaTaskConverter.getFirstTaskDefinition(definition);

        daDiscoveryTask.setTaskId(definition.getId());
        daDiscoveryTask.setTaskCode(String.valueOf(definition.getCode()));
        daDiscoveryTask.setNodeId(firstTaskDefinition.getId());
        daDiscoveryTask.setNodeCode(String.valueOf(firstTaskDefinition.getCode()));
    }

    private void updateExistingProcessDefinition(DaDiscoveryTaskRespVO daDiscoveryTaskById, DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(daDiscoveryTaskById.getName() + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", daDiscoveryTaskById.getId());
        input.setId(daDiscoveryTaskById.getId());

        input.setTaskId(daDiscoveryTaskById.getTaskId());
        input.setTaskCode(String.valueOf(daDiscoveryTaskById.getTaskCode()));
        input.setNodeId(daDiscoveryTaskById.getNodeId());
        input.setNodeCode(String.valueOf(daDiscoveryTaskById.getNodeCode()));

        ProcessDefinition definition = this.updateProcessDefinition(input);
        TaskDefinition firstTaskDefinition = DaTaskConverter.getFirstTaskDefinition(definition);

        daDiscoveryTask.setTaskId(definition.getId());
        daDiscoveryTask.setTaskCode(String.valueOf(definition.getCode()));
        daDiscoveryTask.setNodeId(firstTaskDefinition.getId());
        daDiscoveryTask.setNodeCode(String.valueOf(firstTaskDefinition.getCode()));
    }

    private void updateTaskStatusAndScheduler(DaDiscoveryTaskSaveReqVO daDiscoveryTask, Long systemJobId) {
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(projectCode), daDiscoveryTask.getTaskCode());
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("da.error.task.publish.fail", "Failed to publish or offline task!");
        }

        if (systemJobId != null && systemJobId > 0) {
            updateExistingScheduler(daDiscoveryTask, systemJobId);
        } else {
            createNewScheduler(daDiscoveryTask);
        }

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(projectCode, daDiscoveryTask.getSystemJobId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("da.error.scheduler.online", "Failed to online scheduler!");
        }

        // Update data discovery task
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DaDiscoveryTaskDO.class);
        daDiscoveryTaskMapper.updateById(updateObj);
    }

    private void updateExistingScheduler(DaDiscoveryTaskSaveReqVO daDiscoveryTask, Long systemJobId) {
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = DaTaskConverter.createSchedulerUpdateRequest(systemJobId, daDiscoveryTask.getCronExpression(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            createSchedulerIfNeeded(daDiscoveryTask);
        } else {
            Schedule schedule = dsSchedulerRespDTO.getData();
            daDiscoveryTask.setSystemJobId(schedule.getId());
        }
    }

    private void createNewScheduler(DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DaTaskConverter.createSchedulerRequest(daDiscoveryTask.getCronExpression(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            createSchedulerIfNeeded(daDiscoveryTask);
        } else {
            Schedule schedule = dsSchedulerRespDTO.getData();
            daDiscoveryTask.setSystemJobId(schedule.getId());
        }
    }

    private void createSchedulerIfNeeded(DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(projectCode), daDiscoveryTask.getTaskCode());
        if (byTaskCode == null || !byTaskCode.getSuccess()) {
            // Create scheduler (only available after the task is published)
            DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DaTaskConverter.createSchedulerRequest(daDiscoveryTask.getCronExpression(), daDiscoveryTask.getTaskCode());
            DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
            if (saveScheduler == null || !saveScheduler.getSuccess()) {
                throw new ServiceException("da.error.scheduler.create", "Failed to create scheduler!");
            }
            Schedule schedule = saveScheduler.getData();
            daDiscoveryTask.setSystemJobId(schedule.getId());
            return;
        }
        Schedule schedule = byTaskCode.getData();
        daDiscoveryTask.setSystemJobId(schedule.getId());
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = DaTaskConverter.createSchedulerUpdateRequest(schedule.getId(), daDiscoveryTask.getCronExpression(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO updated = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
        if (updated == null || !updated.getSuccess()) {
            throw new ServiceException("da.error.scheduler.update", "Failed to update scheduler!");
        }
    }

    @Override
    public AjaxResult startDaDiscoveryTask(Long id) {
        DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
        if (daDiscoveryTaskDO == null) {
            return error(MessageUtils.messageWithFallback(
                    "da.error.task.notfound.refresh", "Task does not exist; refresh and try again"));
        }
        if (!StringUtils.equals("0", daDiscoveryTaskDO.getStatus())) {
            return error(MessageUtils.messageWithFallback(
                    "da.error.task.status.invalid", "The task status is invalid; refresh and try again"));
        }

        DsStartTaskReqDTO dsStartTaskReqDTO = DaTaskConverter.createDsStartTaskReqDTO(daDiscoveryTaskDO.getTaskCode());

        try {
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(dsStartTaskReqDTO, projectCode);
            return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
        } catch (Exception e) {
            throw new ServiceException("dpp.error.scheduler.start", "Executing the scheduler, failed!");
        }
    }


    @Override
    public boolean updateDaDiscoveryTaskCronExpression(DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        DaDiscoveryTaskRespVO daDiscoveryTaskById = this.getDaDiscoveryTaskById(daDiscoveryTask.getId());
        Long systemJobId = daDiscoveryTaskById.getSystemJobId();
        if (systemJobId != null) {
            try {
                // Create scheduler (only available after the task is published)
                DsSchedulerUpdateReqDTO schedulerUpdateRequest = DaTaskConverter.createSchedulerUpdateRequest(systemJobId, daDiscoveryTask.getCronExpression(), daDiscoveryTaskById.getTaskCode());
                DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
                if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                    daDiscoveryTask.setTaskId(daDiscoveryTaskById.getTaskId());
                    daDiscoveryTask.setTaskCode(String.valueOf(daDiscoveryTaskById.getTaskCode()));
                    daDiscoveryTask.setNodeId(daDiscoveryTaskById.getNodeId());
                    daDiscoveryTask.setNodeCode(String.valueOf(daDiscoveryTaskById.getNodeCode()));
                    createSchedulerIfNeeded(daDiscoveryTask);
                } else {
                    Schedule schedule = dsSchedulerRespDTO.getData();
                    daDiscoveryTask.setSystemJobId(schedule.getId());
                }
            } catch (Exception e) {
                throw new ServiceException("da.error.schedule.period.update", "Failed to modify schedule period, please contact admin!");

            }
        }

        // Update data discovery task
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DaDiscoveryTaskDO.class);
        daDiscoveryTaskMapper.updateById(updateObj);
//        this.updateDaDiscoveryTask(daDiscoveryTask);
        return true;
    }

    public ProcessDefinition createProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DaTaskConverter.stringToLong(projectCode));
        input.setNodeCode(DaTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DaTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, DaTaskConverter.stringToLong(projectCode));

        if (!task.getSuccess()) {
            throw new ServiceException("da.error.task.status.update", "Task status update failed. Contact system administrator."); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();
        return data; // Return creation result
    }


    public ProcessDefinition updateProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DaTaskConverter.stringToLong(projectCode));
        input.setNodeCode(DaTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DaTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO, projectCode, input.getTaskCode());

        if (!task.getSuccess()) {
            throw new ServiceException("da.error.task.status.update", "Task status update failed. Contact system administrator."); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();
        return data; // Return creation result
    }

    public Long getNodeUniqueKey(Long projectCode) {
        try {
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(projectCode);
            return dsNodeGenCodeRespDTO.getData().get(0);
        } catch (Exception e) {
            throw new ServiceException("da.error.task.status.update", "Task status update failed. Contact system administrator."); // Throw exception for task definition creation error
        }
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DaDiscoveryTaskDO.class)
                .likeRight(DaDiscoveryTaskDO::getCatCode, catCode));
    }

    @Override
    public int updateCatCode(String oldCatCode, String newCatCode) {
        return daDiscoveryTaskMapper.updateCatCode(oldCatCode, newCatCode);
    }
}
