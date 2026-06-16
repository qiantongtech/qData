/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 数据发现任务Service业务层处理
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

            //定时任务封装相关

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
            throw new RuntimeException("任务名称重复，创建失败！");
        }


        //TODO 存储数据，保证测试，对接任务时删除
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
//        // 相关校验
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
            throw new RuntimeException("任务名称重复，创建失败！");
        }

        // 更新数据发现任务
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
        // 相关校验

        // 更新数据发现任务
        DaDiscoveryTaskDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTaskDO.class);
        return daDiscoveryTaskMapper.updateById(updateObj);
    }

    @Override
    public int removeDaDiscoveryTask(Collection<Long> idList) {
//        // 遍历 idList 中的每个 id
//        for (Long id : idList) {
//            // 查询 DaDiscoveryTaskDO 详情
//            DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
//
//            if (daDiscoveryTaskDO != null && daDiscoveryTaskDO.getSystemJobId() != null) {
//                // 提取 systemJobId
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

        // 遍历 idList 中的每个 id
        for (Long id : idList) {
            // 查询 DaDiscoveryTaskDO 详情
            DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
            if (daDiscoveryTaskDO != null &&
                    (daDiscoveryTaskDO.getSystemJobId() != null || !StringUtils.equals("0", daDiscoveryTaskDO.getTaskCode()))) {
                // 提取 systemJobId
                if (StringUtils.equals("0", daDiscoveryTaskDO.getStatus())) {
                    throw new ServiceException("da.error.task.online.delete", "上线任务，不允删除，请先下线！");
                }
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.deleteTask(projectCode, daDiscoveryTaskDO.getTaskCode());
            }
        }

        // 批量删除数据发现任务
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

        //0:否，1：是
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据发现任务数据
     *
     * @param importExcelList 数据发现任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaDiscoveryTask(List<DaDiscoveryTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
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
                                    "数据更新成功，ID为 " + daDiscoveryTaskId + " 的数据发现任务记录。", daDiscoveryTaskId, "数据发现任务"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daDiscoveryTaskId + " 的数据发现任务记录不存在。", daDiscoveryTaskId, "数据发现任务"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaDiscoveryTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDiscoveryTaskId);
                    DaDiscoveryTaskDO existingDaDiscoveryTask = daDiscoveryTaskMapper.selectOne(queryWrapper);
                    if (existingDaDiscoveryTask == null) {
                        daDiscoveryTaskMapper.insert(daDiscoveryTaskDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daDiscoveryTaskId + " 的数据发现任务记录。", daDiscoveryTaskId, "数据发现任务"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daDiscoveryTaskId + " 的数据发现任务记录已存在。", daDiscoveryTaskId, "数据发现任务"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
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
            throw new ServiceException("da.error.template.scheduler.missing", "任务模版错误，未查询到调度信息！");
        }
    }

    private void handleOfflineTask(DaDiscoveryTaskRespVO daDiscoveryTaskById, Long systemJobId, DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        if (daDiscoveryTaskById.getSystemJobId() != null && systemJobId > 0) {
            DsStatusRespDTO respDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(projectCode), daDiscoveryTaskById.getTaskCode());
            if (respDTO == null || !respDTO.getSuccess()) {
                throw new ServiceException("da.error.task.publish.fail", "发布或下线任务，失败！");
            }

            DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(projectCode, systemJobId);
            if (!offlined.getData()) {
                throw new ServiceException("da.error.scheduler.offline", "下线调度器，失败！");
            }
        }

        // 更新数据发现任务
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
            throw new ServiceException("da.error.task.publish.fail", "发布或下线任务，失败！");
        }

        if (systemJobId != null && systemJobId > 0) {
            updateExistingScheduler(daDiscoveryTask, systemJobId);
        } else {
            createNewScheduler(daDiscoveryTask);
        }

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(projectCode, daDiscoveryTask.getSystemJobId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("da.error.scheduler.online", "上线调度器，失败！");
        }

        // 更新数据发现任务
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
            //     * 创建调度器 (只有任务发布了才能调用该接口)
            DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DaTaskConverter.createSchedulerRequest(daDiscoveryTask.getCronExpression(), daDiscoveryTask.getTaskCode());
            DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
            if (saveScheduler == null || !saveScheduler.getSuccess()) {
                throw new ServiceException("da.error.scheduler.create", "创建调度器，失败！");
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
            throw new ServiceException("da.error.scheduler.update", "更新调度器，失败！");
        }
    }

    @Override
    public AjaxResult startDaDiscoveryTask(Long id) {
        DaDiscoveryTaskDO daDiscoveryTaskDO = daDiscoveryTaskMapper.selectById(id);
        if (daDiscoveryTaskDO == null) {
            return error("任务不存在，请刷新后重试！");
        }
        if (!StringUtils.equals("0", daDiscoveryTaskDO.getStatus())) {
            return error("任务状态错误，请刷新后重试！");
        }

        DsStartTaskReqDTO dsStartTaskReqDTO = DaTaskConverter.createDsStartTaskReqDTO(daDiscoveryTaskDO.getTaskCode());

        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(dsStartTaskReqDTO, projectCode);

        return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
    }


    @Override
    public boolean updateDaDiscoveryTaskCronExpression(DaDiscoveryTaskSaveReqVO daDiscoveryTask) {
        DaDiscoveryTaskRespVO daDiscoveryTaskById = this.getDaDiscoveryTaskById(daDiscoveryTask.getId());
        Long systemJobId = daDiscoveryTaskById.getSystemJobId();
        if (systemJobId != null) {
            try {
                //     * 创建调度器 (只有任务发布了才能调用该接口)
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
                throw new ServiceException("da.error.schedule.period.update", "调度周期修改失败，请联系系统管理员！");

            }
        }

        // 更新数据发现任务
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
            throw new ServiceException("da.error.task.status.update", "任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();
        return data; // 返回创建结果
    }


    public ProcessDefinition updateProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DaTaskConverter.stringToLong(projectCode));
        input.setNodeCode(DaTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DaTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO, projectCode, input.getTaskCode());

        if (!task.getSuccess()) {
            throw new ServiceException("da.error.task.status.update", "任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();
        return data; // 返回创建结果
    }

    public Long getNodeUniqueKey(Long projectCode) {
        try {
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(projectCode);
            return dsNodeGenCodeRespDTO.getData().get(0);
        } catch (Exception e) {
            throw new ServiceException("da.error.task.status.update", "任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
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
