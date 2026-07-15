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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessTaskRelation;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskDefinition;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.constant.ScheduleConstants;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.TaskCatEnum;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.uuid.IdUtils;
import tech.qiantong.qdata.module.att.api.cat.IAttCatService;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatRespDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttDataDevCatApiService;
import tech.qiantong.qdata.module.att.api.service.cat.IAttTaskCatApiService;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.*;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskMapper;
import tech.qiantong.qdata.module.dpp.service.etl.task.DppEtlTaskDataIntegrationRunner;
import tech.qiantong.qdata.module.dpp.service.etl.task.DataDevelopmentJdbcTaskRunner;
import tech.qiantong.qdata.module.dpp.service.etl.*;
import tech.qiantong.qdata.module.dpp.service.scheduler.DppTaskQuartzService;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.module.dpp.utils.log.LogUtils;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;
import tech.qiantong.qdata.mybatis.core.util.MyBatisUtils;
import tech.qiantong.qdata.quartz.domain.ScheduleCommand;
import tech.qiantong.qdata.quartz.scheduler.ISchedulerAdapter;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * Data integration task Service business layer processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskServiceImpl extends ServiceImpl<DppEtlTaskMapper, DppEtlTaskDO> implements IDppEtlTaskService, DppEtlTaskService {

    @Resource
    private DppEtlTaskMapper dppEtlTaskMapper;

    @Resource
    private IDppEtlSchedulerService iDppEtlSchedulerService;

    @Resource
    private IDsEtlSchedulerService iDsEtlSchedulerService;

    @Resource
    private IDsEtlNodeService dsEtlNodeService;
    @Resource
    private IDsEtlTaskService dsEtlTaskService;
    @Resource
    private IDppEtlNodeService iDppEtlNodeService;

    @Resource
    private IDppEtlTaskLogService iDppEtlTaskLogService;
    @Resource
    private IDppEtlNodeLogService iDppEtlNodeLogService;
    @Resource
    private IDppEtlTaskNodeRelService iDppEtlTaskNodeRelService;
    @Resource
    private IDppEtlTaskNodeRelLogService iDppEtlTaskNodeRelLogService;
    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;
    @Resource
    private IAttCatService attCatService;
    @Resource
    private IAttTaskCatApiService iAttTaskCatApiService;
    @Resource
    private IAttDataDevCatApiService iAttDataDevCatApiService;
    @Resource
    private IDppEtlTaskExtService dppEtlTaskExtService;
    @Resource
    private IRedisService redisService;
    @Resource
    private ISchedulerAdapter schedulerAdapter;
    @Resource
    private DppTaskQuartzService dppTaskQuartzService;
    @Resource
    private DppEtlTaskDataIntegrationRunner dppEtlTaskDataIntegrationRunner;
    @Resource
    private DataDevelopmentJdbcTaskRunner dataDevelopmentJdbcTaskRunner;

    @Override
    public PageResult<DppEtlTaskDO> getDppEtlTaskPage(DppEtlTaskPageReqVO pageReqVO) {
        return dppEtlTaskMapper.selectPage(pageReqVO);
    }

    public List<DppEtlTaskNodeRelRespVO> getTaskNodeRelList(DppEtlTaskRespVO bean) {
        DppEtlTaskNodeRelPageReqVO reqVO = new DppEtlTaskNodeRelPageReqVO();
        reqVO.setTaskId(bean.getId());
        reqVO.setTaskCode(bean.getCode());
        reqVO.setTaskVersion(bean.getVersion());
        return iDppEtlTaskNodeRelService.getDppEtlTaskNodeRelRespVOList(reqVO);
    }

    @Override
    public PageResult<DppEtlTaskRespVO> getDppEtlTaskPageList(DppEtlTaskPageReqVO dppEtlTask) {
        IPage<DppEtlTaskRespVO> mpPage = dppEtlTaskMapper.getDppEtlTaskPage(MyBatisUtils.buildPage(dppEtlTask), dppEtlTask);//BeanUtils.toBean(dppEtlTaskDOPageResult, DppEtlTaskRespVO.class);
        return new PageResult(mpPage.getRecords(), mpPage.getTotal());
    }

    @Override
    public Long createDppEtlTask(DppEtlTaskSaveReqVO createReqVO) {
        DppEtlTaskDO dppEtlTaskDO = BeanUtils.toBean(createReqVO, DppEtlTaskDO.class);
        if (StringUtils.isNotEmpty(dppEtlTaskDO.getCatCode()) && StringUtils.isNotEmpty(createReqVO.getType())) {
            dppEtlTaskDO.setCatId(attCatService.getCatIdByTableNameAndCatCode(TaskCatEnum.findEnumByType(createReqVO.getType()).toString(), dppEtlTaskDO.getCatCode()));
        }
        dppEtlTaskMapper.insert(dppEtlTaskDO);
        return dppEtlTaskDO.getId();
    }

    @Override
    public int updateDppEtlTask(DppEtlTaskSaveReqVO updateReqVO) {
        // Related validation

        // Update data integration task
        DppEtlTaskDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskDO.class);
        if (StringUtils.isNotEmpty(updateReqVO.getCatCode()) && StringUtils.isNotEmpty(updateReqVO.getType())) {
            updateReqVO.setCatId(attCatService.getCatIdByTableNameAndCatCode(TaskCatEnum.findEnumByType(updateReqVO.getType()).toString(), updateReqVO.getCatCode()));
        }
        return dppEtlTaskMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlTask(Collection<Long> idList) {
        int sum = 0;
        for (Long id : idList) {
            DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
            if (dppEtlTaskDO == null) {
                sum++;
                continue;
            }
            //1: Offline task 2: Real-time task 3: Data development task 4: Job task
            String type = dppEtlTaskDO.getType();
            // Check if it is an offline task; if so, get the task code from extended info for API call
            DppEtlTaskExtDO taskExt = null;
            if (StringUtils.equals("1", type) && !StringUtils.equals("-1", dppEtlTaskDO.getStatus())) {
                // Get extended info
                taskExt = dppEtlTaskExtService.getByTaskId(dppEtlTaskDO.getId());
                if (taskExt == null) {
                    throw new ServiceException("暂无数据！");
                }
                dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
            }
            if (StringUtils.equals("1", dppEtlTaskDO.getStatus())) {
                throw new ServiceException("dpp.error.task.online.delete", "上线任务，不允删除，请先下线！");
            }
            if (dppEtlTaskDO.getDsId() != null && dppEtlTaskDO.getQuartzId() != null || (taskExt != null && StringUtils.isNotEmpty(taskExt.getEtlTaskCode()))) {
                if (dppEtlTaskDO.getQuartzId() > 0 && ScheduleConstants.QUARTZ.equals(dppEtlTaskDO.getScheduler())) {
                    schedulerAdapter.delete(ScheduleCommand.builder().jobName(dppEtlTaskDO.getName()).id(dppEtlTaskDO.getQuartzId()).build());
                } else if (dppEtlTaskDO.getDsId() != null && dppEtlTaskDO.getDsId() > 0)
                    dsEtlTaskService.deleteTask(dppEtlTaskDO.getProjectCode(), dppEtlTaskDO.getCode());
                sum += dppEtlTaskMapper.deleteById(id);
            } else {
                sum += dppEtlTaskMapper.deleteById(id);
            }
        }
        // Batch delete data integration tasks
        return sum;
    }

    public List<DppEtlNodeRespVO> removeDuplicateById(List<DppEtlNodeRespVO> etlNodeLogRespVOList, String type) {
        // Use LinkedHashMap to preserve original order after deduplication
        Map<Long, DppEtlNodeRespVO> map = etlNodeLogRespVOList.stream()
                .filter(itam -> itam != null && itam.getId() != null)
                .collect(Collectors.toMap(DppEtlNodeRespVO::getId, vo -> vo, (existing, replacement) -> existing));

        // Get deduplicated list
        ArrayList<DppEtlNodeRespVO> dppEtlNodeRespVOS = new ArrayList<>(map.values());
        if (StringUtils.equals("4", type) && CollectionUtils.isNotEmpty(dppEtlNodeRespVOS)) {
            for (DppEtlNodeRespVO dppEtlNodeRespVO : dppEtlNodeRespVOS) {
                String parameters = dppEtlNodeRespVO.getParameters();
                Map<String, Object> stringObjectMap = JSONUtils.convertTaskDefinitionJsonMap(parameters);
                long subTaskId = MapUtils.getLongValue(stringObjectMap, "subTaskId");
                DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(subTaskId);
                dppEtlNodeRespVO.setReleaseState(dppEtlTaskDO.getStatus());
            }
        }
        return dppEtlNodeRespVOS;
    }

    @Override
    public List<DppEtlTaskDO> getDppEtlTaskList() {
        return dppEtlTaskMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskDO> getDppEtlTaskMap() {
        List<DppEtlTaskDO> dppEtlTaskList = dppEtlTaskMapper.selectList();
        return dppEtlTaskList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskDO::getId,
                        dppEtlTaskDO -> dppEtlTaskDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data integration task data
     *
     * @param importExcelList Data integration task data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName        Operating user
     * @return Result
     */
    @Override
    public String importDppEtlTask(List<DppEtlTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlTaskRespVO respVO : importExcelList) {
            try {
                DppEtlTaskDO dppEtlTaskDO = BeanUtils.toBean(respVO, DppEtlTaskDO.class);
                Long dppEtlTaskId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlTaskId != null) {
                        DppEtlTaskDO existingDppEtlTask = dppEtlTaskMapper.selectById(dppEtlTaskId);
                        if (existingDppEtlTask != null) {
                            dppEtlTaskMapper.updateById(dppEtlTaskDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "数据更新成功，ID为 " + dppEtlTaskId + " 的数据集成任务记录。", dppEtlTaskId, "数据集成任务"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "数据更新失败，ID为 " + dppEtlTaskId + " 的数据集成任务记录不存在。", dppEtlTaskId, "数据集成任务"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DppEtlTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskId);
                    DppEtlTaskDO existingDppEtlTask = dppEtlTaskMapper.selectOne(queryWrapper);
                    if (existingDppEtlTask == null) {
                        dppEtlTaskMapper.insert(dppEtlTaskDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "数据插入成功，ID为 " + dppEtlTaskId + " 的数据集成任务记录。", dppEtlTaskId, "数据集成任务"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "数据插入失败，ID为 " + dppEtlTaskId + " 的数据集成任务记录已存在。", dppEtlTaskId, "数据集成任务"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }


    @Override
    public Long getNodeUniqueKey(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (ScheduleConstants.QUARTZ.equals(dppEtlNewNodeSaveReqVO.getScheduler())) {
            return dppTaskQuartzService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        }
        DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        return dsNodeGenCodeRespDTO.getData().get(0);
    }

    @Override
    public List<DppEtlTaskRespVO> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask) {
        DppEtlTaskRespVO dppEtlTaskById = this.getDppEtlTaskById(dppEtlTask.getId());
        List<DppEtlNodeRespVO> taskDefinitionList = dppEtlTaskById.getTaskDefinitionList();

        List<DppEtlTaskRespVO> dppEtlNewNodeSaveReqVOS = new ArrayList<>();
        // Loop to get custom tasks
        for (DppEtlNodeRespVO dppEtlNodeRespVO : taskDefinitionList) {
            String parameters = dppEtlNodeRespVO.getParameters();
            Map<String, Object> stringObjectMap = JSONUtils.convertTaskDefinitionJsonMap(parameters);
            long subTaskId = MapUtils.getLongValue(stringObjectMap, "subTaskId");
            DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(subTaskId);
            if (dppEtlTaskDO != null) {
                dppEtlNewNodeSaveReqVOS.add(BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskRespVO.class));
            }
        }
        return dppEtlNewNodeSaveReqVOS;
    }

    @Override
    public Map<String, Object> updateReleaseJobTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskId(dppEtlTaskDO.getId());
        dppEtlSchedulerPageReqVO.setTaskCode(dppEtlTaskDO.getCode());
        //1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlTaskDO.getType();
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        // If task status has not changed, return directly
        if (StringUtils.equals(dppEtlTaskDO.getStatus(), dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-3", dppEtlTaskDO.getStatus()) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }


        if (StringUtils.equals("4", type) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            wrapCustomNodeStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        }

        if (StringUtils.equals("1", dppEtlSchedulerById.getStatus()) && StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            throw new ServiceException("调度上线中，请先下线调度！");
        }

        // Check if it is an offline task; if so, get the task code from extended info for API call
        if (StringUtils.equals("1", type)) {
            // Get extended info
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        if (ScheduleConstants.DOLPHINSCHEDULER.equals(dppEtlTaskDO.getScheduler())) {
            // Offline operation
            if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
                if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                    throw new ServiceException("dpp.error.task.publish.fail", "发布或下线任务，失败！");
                }

                // Update task status
                if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                    updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
                } else {
                    updateTaskStatus(dppEtlTaskDO.getId(), "-2");
                }
                return new HashMap<>();
            }

            // Online operation
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            String responseMsg = dsStatusRespDTO.getMsg();
            if (responseMsg.contains("SubWorkflowDefinition") && responseMsg.contains("is not online")) {
                throw new RuntimeException("存在未上线的子工作流，请先将所有子工作流上线");
            }
            if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                throw new ServiceException("发布任务失败！");
            }
        }

        // Update task status
        if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        } else {
            updateTaskStatus(dppEtlTaskDO.getId(), "-3");
        }

        return null;
    }

    @Override
    public Map<String, Object> updateReleaseSchedule(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskId(dppEtlTaskDO.getId());
        dppEtlSchedulerPageReqVO.setTaskCode(dppEtlTaskDO.getCode());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        dppEtlTaskDO.setCronExpression(dppEtlSchedulerById.getCronExpression());

        // If task status has not changed, return directly
        if (StringUtils.equals(dppEtlSchedulerById.getStatus(), dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            return new HashMap<>();
        }

        if ((StringUtils.equals("0", dppEtlTaskDO.getStatus()) || StringUtils.equals("-2", dppEtlTaskDO.getStatus()))
                && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            throw new ServiceException("任务未上线，请先上线！");
        }

        //1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlTaskDO.getType();

        // Check if it is an offline task; if so, get the task code from extended info for API call
        if (StringUtils.equals("1", type)) {
            // Get extended info
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        if (StringUtils.equals("4", type) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            wrapCustomNodeStatus(dppEtlTaskDO.getId(), "1");
        }

        // Offline operation
        if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            if (ScheduleConstants.QUARTZ.equals(dppEtlTaskDO.getScheduler())) {
                dppTaskQuartzService.offline(dppEtlSchedulerById.getQuartzId());
            } else {
                if (dppEtlSchedulerById.getDsId() != null && dppEtlSchedulerById.getDsId() > 0) {
                    DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.offlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerById.getDsId());
                    if (!dsStatusRespDTO1.getData()) {
                        throw new ServiceException("dpp.error.scheduler.offline", "下线调度器，失败！");
                    }
                }
            }

            // Update scheduler and bring online
            DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setStatus(dppEtlNewNodeSaveReqVO.getSchedulerState());
            // Update scheduler
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
            return null;
        }

        DsSchedulerRespDTO dsSchedulerRespDTO;
        // 更新调度器并上线
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO;
        if (ScheduleConstants.QUARTZ.equals(dppEtlSchedulerById.getTaskScheduler())) {
            Long quartzId;
            dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
            if (dppEtlSchedulerById.getQuartzId() == null || dppEtlSchedulerById.getQuartzId() < 1) {
                quartzId = dppTaskQuartzService.create(dppEtlTaskDO, "dppQuartzJob.dataIntegration(%sL)");
                dppEtlSchedulerSaveReqVO.setQuartzId(quartzId);
            } else {
                quartzId = dppEtlSchedulerById.getQuartzId();
            }
            dppTaskQuartzService.online(quartzId);
        } else {
            if (dppEtlSchedulerById.getDsId() == null || dppEtlSchedulerById.getDsId() < 1) {
                dsSchedulerRespDTO = createOrUpdateScheduler(dppEtlSchedulerById, dppEtlTaskDO);
            } else {
                dsSchedulerRespDTO = updateExistingScheduler(dppEtlSchedulerById, dppEtlTaskDO);
            }
            dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);

            DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerSaveReqVO.getDsId());
            if (!dsStatusRespDTO1.getData()) {
                throw new ServiceException("dpp.error.scheduler.online", "上线调度器，失败！");
            }
        }

        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
        dppEtlSchedulerSaveReqVO.setStatus(dppEtlNewNodeSaveReqVO.getSchedulerState());
        // Update scheduler
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        return null;
    }

    /**
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    @Override
    public DppEtlTaskSaveReqVO createEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (ScheduleConstants.QUARTZ.equals(dppEtlNewNodeSaveReqVO.getScheduler())) {
            return createLocalDataXEtlTask(dppEtlNewNodeSaveReqVO);
        }
        // Compatible with creating task first, then enriching details later
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);
        String taskCode = getDsTaskGenCode(dppEtlNewNodeSaveReqVO, isUpdate);

        // Generate node code
        DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        String nodeCode = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));
        // Generate node name
        String nodeName = dppEtlNewNodeSaveReqVO.getName() + "-" + DateUtil.today();

        // Create response entity
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // Wrap basic parameters
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        // Build task info
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        List<DsResource> resourceList = new ArrayList<>();
        // Build ETL program required data
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), new HashMap<>(), taskInfo, resourceList);

        // Wrap node info DATAX, SPARK
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(null, nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        // Node relations
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(null, nodeCode);

        // Location info
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();

        // Convert task save request object
        DppEtlTaskSaveReqVO taskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        taskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        taskSaveReqVO.setCode(taskCode);

        Long dppEtlTask;

        if (isUpdate) {
            taskSaveReqVO.setId(JSONUtils.convertToLong(dppEtlNewNodeSaveReqVO.getId()));
            this.updateDppEtlTask(taskSaveReqVO);
            dppEtlTask = taskSaveReqVO.getId();
        } else {
            dppEtlTask = this.createDppEtlTask(taskSaveReqVO);
            taskSaveReqVO.setId(dppEtlTask);
        }

        // Build scheduler object
        DppEtlSchedulerSaveReqVO schedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(
                dppEtlTask, taskSaveReqVO.getCode(), dppEtlNewNodeSaveReqVO
        );

        if (isUpdate) {
            DppEtlSchedulerDO schedulerDO = getDppEtlScheduler(taskSaveReqVO.getCode(), taskSaveReqVO.getId());
            schedulerSaveReqVO.setTaskCode(taskSaveReqVO.getCode());
            schedulerSaveReqVO.setTaskId(taskSaveReqVO.getId());
            schedulerSaveReqVO.setId(schedulerDO.getId());
            iDppEtlSchedulerService.updateDppEtlScheduler(schedulerSaveReqVO);
        } else {
            iDppEtlSchedulerService.createDppEtlScheduler(schedulerSaveReqVO);
        }

        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskLogSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        dppEtlTaskLogSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskLogSaveReqVO.setCode(taskCode);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        // Create ETL task extended data
        dppEtlTaskExtService.createDppEtlTaskExt(DppEtlTaskExtSaveReqVO.builder()
                .taskId(dppEtlTask)
                .etlTaskCode(data.getCode())
                .etlTaskVersion(data.getVersion())
                .etlNodeId(data.getTaskDefinitionList().get(0).getId())
                .etlNodeName(nodeName)
                .etlNodeCode(nodeCode)
                .etlNodeVersion(data.getTaskDefinitionList().get(0).getVersion())
                .etlRelationId(data.getTaskRelationList().get(0).getId())
                .build());

        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(dppEtlNewNodeSaveReqVO, dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(dppEtlNodeSaveReqVOList);
        iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeBatch, dppEtlNewNodeSaveReqVO, taskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return taskSaveReqVO; // Return creation result
    }

    /**
     * 创建 Quartz + DataX 数据集成任务
     */
    private DppEtlTaskSaveReqVO createLocalDataXEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);

        // 创建调度器
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(Long.valueOf(saveReqVOId));
        dppEtlTaskDO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        Long quartzId = dppTaskQuartzService.create(dppEtlTaskDO, "dppQuartzJob.dataIntegration(%sL)");

        //生成节点名称
        String taskCode = String.valueOf(dppEtlTaskDO.getCode());
        String nodeName = dppEtlNewNodeSaveReqVO.getName() + "-" + DateUtil.today();

        // 创建或者更新调度器信息
        Long taskId = JSONUtils.convertToLong(saveReqVOId);
        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtil.copyProperties(dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setVersion(1);
        dppEtlTaskSaveReqVO.setCode(taskCode);
        dppEtlTaskSaveReqVO.setStatus("0");
        dppEtlTaskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        if (isUpdate) {
            dppEtlTaskSaveReqVO.setQuartzId(quartzId);
            this.updateDppEtlTask(dppEtlTaskSaveReqVO);
        } else {
            taskId = this.createDppEtlTask(dppEtlTaskSaveReqVO);
        }
        dppEtlTaskSaveReqVO.setId(taskId);
        dppEtlTaskSaveReqVO.setDsId((long) -1);

        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(taskId, dppEtlTaskSaveReqVO.getCode(), dppEtlNewNodeSaveReqVO);
        if (isUpdate) {
            // 更新任务时复用原来的调度记录，只把调度器和执行引擎等配置刷新掉。
            DppEtlSchedulerDO dppEtlSchedulerById = getDppEtlScheduler(dppEtlTaskSaveReqVO.getCode(), dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setTaskCode(taskCode);
            dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setQuartzId(quartzId);
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        } else {
            // 新任务第一次保存时，需要同步创建一条 DPP 调度配置。
            iDppEtlSchedulerService.createDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        }

        // 保存一份任务日志快照，后面版本追溯和执行记录会用到。
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskSaveReqVO(dppEtlTaskSaveReqVO);
        dppEtlTaskLogSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskLogSaveReqVO.setCode(taskCode);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        if (isUpdate) {
            // 更新本地 DataX 任务时，先清掉旧节点和旧关系，再按这次画布重新保存。
            List<String> nodeCodeList = getLocalNodeCodeList(dppEtlNewNodeSaveReqVO);
            if (CollectionUtils.isNotEmpty(nodeCodeList)) {
                // 有节点编码才删除旧节点，避免空集合生成无效 SQL。
                iDppEtlNodeService.removeOldDppEtlNode(nodeCodeList);
            }
            iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(taskCode);
        }

        //创建etl任务扩展数据
        dppEtlTaskExtService.createDppEtlTaskExt(DppEtlTaskExtSaveReqVO.builder()
                .taskId(taskId)
                .etlTaskCode(taskCode)
                .etlNodeCode(taskCode)
                .etlNodeName(nodeName)
                .build());

        // 把前端画布里的节点保存到 DPP 自己的节点表，DataX 执行时会读取这些配置生成 job.json。
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(dppEtlNewNodeSaveReqVO, 1);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // 节点日志表也保存一份，方便后续看历史版本。
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(dppEtlNodeSaveReqVOList);
        iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        // 保存节点之间的连线关系，详情页和执行链路都要靠它还原任务结构。
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeBatch, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // 关系日志也保存一份，和节点日志一样用于历史追溯。
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);
        return dppEtlTaskSaveReqVO;
    }

    /**
     * 组装本地 DataX 任务保存对象。
     */
    private DppEtlTaskSaveReqVO buildLocalDataXTaskSaveReqVO(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskSaveReqVO createReqVO = new DppEtlTaskSaveReqVO();
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setName(dppEtlNewNodeSaveReqVO.getName());
        createReqVO.setVersion(1);
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId());
        createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()));
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        createReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        createReqVO.setDsId((long) -1);
        createReqVO.setStatus(resolveLocalTaskStatus(dppEtlNewNodeSaveReqVO.getReleaseState()));
        createReqVO.setRemark("");
        createReqVO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());
        createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId());
        createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy());
        createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime());
        createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId());
        createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy());
        createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime());
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setContactNumber(dppEtlNewNodeSaveReqVO.getContactNumber());
        createReqVO.setCatCode(dppEtlNewNodeSaveReqVO.getCatCode());
        createReqVO.setScheduler(ScheduleConstants.QUARTZ);
        createReqVO.setActuator(dppEtlNewNodeSaveReqVO.getActuator());
        createReqVO.setTimeout(dppEtlNewNodeSaveReqVO.getTimeout());
        createReqVO.setDraftJson(dppEtlNewNodeSaveReqVO.getDraftJson());
        return createReqVO;
    }

    private String resolveLocalTaskStatus(String releaseState) {
        if (StringUtils.equals("-2", releaseState) || StringUtils.equals("-3", releaseState)) {
            // -2/-3 是系统已有的特殊上下线状态，传进来时要原样保留。
            return releaseState;
        }
        // 新建本地 DataX 任务默认先不上线，等用户显式上线任务和调度。
        return "0";
    }

    /**
     * 从画布节点里取出节点编码。
     * 更新本地 DataX 任务时，需要用这些编码删除旧节点。
     */
    private List<String> getLocalNodeCodeList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);
        if (CollectionUtils.isEmpty(nodeList)) {
            // 没有节点就返回空列表，上层会跳过删除，避免生成 where in ()。
            return Collections.emptyList();
        }
        return nodeList.stream()
                .map(DppEtlNodeSaveReqVO::getCode)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
    }

    @Override
    public DppEtlTaskSaveReqVO updateEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        if (StringUtils.equals("1", dppEtlTaskDO.getStatus()) || StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            throw new ServiceException("上线任务，不允许修改，请先下线！");
        }

        // Create ETL task extended data
        DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        // Generate task code
        String taskCode = taskExt.getEtlTaskCode();
        // Generate node code
        String nodeCode = taskExt.getEtlNodeCode();
        // Generate node name
        String nodeName = taskExt.getEtlNodeName();

        // Create response entity
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // Wrap basic parameters
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        // Build task info
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", dppEtlTaskDO.getCode());
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setName(dppEtlNewNodeSaveReqVO.getName());
        dppEtlTaskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskSaveReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());


        // Process node data
        List<DppEtlNodeSaveReqVO> newTaskDefinitionLogs = new ArrayList<>();
        List<DppEtlNodeSaveReqVO> updateTaskDefinitionLogs = new ArrayList<>();

        // Extract input parameter info
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        List<DppEtlNodeDO> dppEtlNodeDOList = new ArrayList<>();

        Map<String, DppEtlNodeSaveReqVO> nodeMap = nodeList.stream().collect(Collectors.toMap(DppEtlNodeSaveReqVO::getCode, node -> node));

        // Iterate taskDefinitionList in ProcessDefinition
        for (DppEtlNodeSaveReqVO createReqVO : nodeList) {
            // 1. Task related info
            createReqVO.setType(createReqVO.getTaskType());// Node type
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Task type
            if (createReqVO.getVersion() == 0) {
                createReqVO.setVersion(1);
            }
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Project ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Project code
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));

            DppEtlNodeLogDO nodeCodeAndVersion = iDppEtlNodeLogService.getByNodeCodeAndVersion(
                    createReqVO.getCode(), createReqVO.getVersion());
            if (nodeCodeAndVersion == null) {
                createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Assume project ID as creator ID (adjust as needed)
                createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Assume task name as creator (adjust as needed)
                createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Set current time as creation time
                newTaskDefinitionLogs.add(createReqVO);
                continue;
            } else {
                // Check if it is an input component with ID increment
                if (StringUtils.equals(TaskComponentTypeEnum.DB_READER.getCode(), String.valueOf(createReqVO.getTaskParams().get("type"))) &&
                        StringUtils.equals("2", String.valueOf(createReqVO.getTaskParams().get("readModeType")))) {
                    JSONObject idIncrementConfig = JSONObject.parseObject(String.valueOf(createReqVO.getTaskParams().get("idIncrementConfig")));
                    String incrementColumn = idIncrementConfig.getString("incrementColumn");
                    Integer incrementStart = idIncrementConfig.getInteger("incrementStart");
                    String cacheKey = TaskConverter.ETL_READER_ID_KEY + createReqVO.getCode() + ":" + incrementColumn;
                    // Check if cache exists and cache value does not equal current value, delete cache if so
                    if (redisService.hasKey(cacheKey) && Integer.parseInt(redisService.get(cacheKey)) != incrementStart) {
                        redisService.delete(cacheKey);
                    }
                }
                createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Assume project ID as updater ID (adjust as needed)
                createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Assume task name as updater (adjust as needed)
                createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Set current time as update time
            }

            // Check if data is the same
            if (createReqVO.equals(nodeCodeAndVersion)) {
                DppEtlNodeDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeDO.class);
                dppEtlNodeDOList.add(dictType);
                continue;
            }

            // Get the current maximum version
            Integer version = iDppEtlNodeLogService.getMaxVersionByNodeCode(createReqVO.getCode());
            createReqVO.setVersion(version + 1);
            updateTaskDefinitionLogs.add(createReqVO);
        }

        // Add node logs
        List<DppEtlNodeSaveReqVO> newInsertTaskDefinitionLogs = newTaskDefinitionLogs.stream()
                .filter(taskDefinitionLog -> !updateTaskDefinitionLogs.contains(taskDefinitionLog))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newInsertTaskDefinitionLogs)) {
            List<DppEtlNodeLogSaveReqVO> saveNodeList = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(newInsertTaskDefinitionLogs);
            saveNodeList.stream().forEach(dppEtlNodeLogSaveReqVO -> {
                dppEtlNodeLogSaveReqVO.setId(null);
            });
            iDppEtlNodeLogService.createDppEtlNodeLogBatch(TaskConverter.convertToDppEtlNodeLogSaveReqVOList(newInsertTaskDefinitionLogs));
        }
        if (CollectionUtils.isNotEmpty(updateTaskDefinitionLogs)) {
            List<DppEtlNodeLogSaveReqVO> saveNodeList = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(newInsertTaskDefinitionLogs);
            saveNodeList.stream().forEach(dppEtlNodeLogSaveReqVO -> {
                dppEtlNodeLogSaveReqVO.setId(null);
            });
            iDppEtlNodeLogService.createDppEtlNodeLogBatch(TaskConverter.convertToDppEtlNodeLogSaveReqVOList(updateTaskDefinitionLogs));
        }

        // Add node data
        if (CollectionUtils.isNotEmpty(newTaskDefinitionLogs)) {
            dppEtlNodeDOList.addAll(iDppEtlNodeService.createDppEtlNodeBatch(newTaskDefinitionLogs));
        }

        // Modify node data
        if (CollectionUtils.isNotEmpty(updateTaskDefinitionLogs)) {
            log.info("update task definition>>>>>>>>>>>");
            for (DppEtlNodeSaveReqVO taskDefinitionLog : updateTaskDefinitionLogs) {
                DppEtlNodeDO dppEtlNodeDO = BeanUtils.toBean(taskDefinitionLog, DppEtlNodeDO.class);
                dppEtlNodeDOList.add(dppEtlNodeDO);
                iDppEtlNodeService.update(dppEtlNodeDO,
                        Wrappers.lambdaUpdate(DppEtlNodeDO.class)
                                .eq(DppEtlNodeDO::getCode, taskDefinitionLog.getCode()));
            }
        }


        // Process relation data
        List<DppEtlTaskNodeRelSaveReqVO> taskRelationList = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);

        boolean isChange = false;
        // Get relation log data by task code and version
        List<DppEtlTaskNodeRelLogDO> dppEtlTaskNodeRelLogDOList = iDppEtlTaskNodeRelLogService.list(Wrappers.lambdaQuery(DppEtlTaskNodeRelLogDO.class)
                .eq(DppEtlTaskNodeRelLogDO::getTaskCode, dppEtlTaskDO.getCode())
                .eq(DppEtlTaskNodeRelLogDO::getTaskVersion, dppEtlTaskDO.getVersion())
        );
        List<DppEtlTaskNodeRelSaveReqVO> processTaskRelationLogList = new ArrayList<>();
        if (dppEtlTaskNodeRelLogDOList.size() > 0) {
            for (DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO : dppEtlTaskNodeRelLogDOList) {
                processTaskRelationLogList.add(BeanUtils.toBean(dppEtlTaskNodeRelLogDO, DppEtlTaskNodeRelSaveReqVO.class));
            }
        }

        if (taskRelationList.size() == processTaskRelationLogList.size()) {
            Set<DppEtlTaskNodeRelLogDO> taskRelationSet = new HashSet(taskRelationList);
            Set<DppEtlTaskNodeRelLogDO> processTaskRelationLogSet = new HashSet(processTaskRelationLogList);
            if (taskRelationSet.size() == processTaskRelationLogSet.size()) {
                taskRelationSet.removeAll(processTaskRelationLogSet);
                if (!taskRelationSet.isEmpty()) {
                    isChange = true;
                }
            } else {
                isChange = true;
            }
        } else {
            isChange = true;
        }
        Integer taskVersion = 0;
        if (isChange) {
            // Get the maximum version
            taskVersion = iDppEtlTaskLogService.queryMaxVersionByCode(dppEtlTaskDO.getCode());
            taskVersion += 1;
            dppEtlTaskSaveReqVO.setVersion(taskVersion);

            // Add or update task log
            DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskLogSaveReqVO(dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
            dppEtlTaskLogSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
            dppEtlTaskLogSaveReqVO.setCode(dppEtlTaskDO.getCode());
            dppEtlTaskLogSaveReqVO.setVersion(taskVersion);
            taskInfo.put("taskVersion", taskVersion);
            iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        }
        this.updateDppEtlTask(dppEtlTaskSaveReqVO);

        Set<Integer> taskRelationSet = taskRelationList.stream().map(Objects::hashCode).collect(toSet());
        Set<Integer> processTaskRelationLogSet = processTaskRelationLogList.stream().map(Objects::hashCode).collect(toSet());

        boolean result = CollectionUtils.isEqualCollection(processTaskRelationLogSet, taskRelationSet);
        if (result) {
            return dppEtlTaskSaveReqVO;
        }

        // Delete relations first, then add new ones
        iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());

        // Add relations
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // Add relation logs
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            dppEtlTaskNodeRelLogSaveReqVO.setTaskVersion(taskVersion);
            dppEtlTaskNodeRelLogSaveReqVO.setId(null);
        }
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        List<DsResource> resourceList = new ArrayList<>();
        // Build ETL program required data
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), nodeMap, taskInfo, resourceList);

        // Wrap node info DATAX, SPARK
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(taskExt.getEtlNodeId(), nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        // Node relations
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(taskExt.getEtlRelationId(), nodeCode);

        // Location info
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);

        if (ScheduleConstants.DOLPHINSCHEDULER.equals(dppEtlTaskSaveReqVO.getScheduler())) {
            DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO, String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), taskCode);

            if (!task.getSuccess()) {
                throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // Throw exception for task definition creation error
            }

            ProcessDefinition data = task.getData();

            // Update extended data
            taskExt.setEtlTaskVersion(data.getVersion());
            taskExt.setEtlNodeVersion(data.getTaskDefinitionList().get(0).getVersion());
            taskExt.setEtlRelationId(data.getTaskRelationList().get(0).getId());
            dppEtlTaskExtService.updateById(taskExt);
        }
        return dppEtlTaskSaveReqVO; // Return creation result
    }

    @Override
    public Map<String, Object> updateReleaseTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskId(dppEtlTaskDO.getId());
        dppEtlSchedulerPageReqVO.setTaskCode(dppEtlTaskDO.getCode());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);

        if (dppEtlSchedulerById == null) {
            throw new ServiceException("dpp.error.template.scheduler.missing", "任务模版错误，未查询到调度信息！");
        }

        // If task status has not changed, return directly
        if (StringUtils.equals(dppEtlTaskDO.getStatus(), dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-3", dppEtlTaskDO.getStatus()) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        //1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlTaskDO.getType();
        if (StringUtils.equals("4", type)) {
            wrapCustomNodeStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        }

//        try{
        collectMainTaskIdsForStatusChange(dppEtlNewNodeSaveReqVO, dppEtlTaskDO, dppEtlSchedulerById);
//        }catch (Exception e){
//            if(StringUtils.equals("4",type)){
//                String releaseState = dppEtlNewNodeSaveReqVO.getReleaseState();
//                wrapCustomNodeStatus(dppEtlTaskDO.getId(),StringUtils.equals("1",releaseState) ? "0":"1");
//            }
//            throw e;
//        }
        return new HashMap<>();
    }

    private void collectMainTaskIdsForStatusChange(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskDO dppEtlTaskDO, DppEtlSchedulerDO dppEtlSchedulerById) {

        // Offline operation
        if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            if (dppEtlSchedulerById.getDsId() != null && dppEtlSchedulerById.getDsId() > 0) {
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
                if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                    throw new ServiceException("dpp.error.task.publish.fail", "发布或下线任务，失败！");
                }
                DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.offlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerById.getDsId());
                if (!dsStatusRespDTO1.getData()) {
                    throw new ServiceException("dpp.error.scheduler.offline", "下线调度器，失败！");
                }
            }
            // Update task status
            if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
            } else {
                updateTaskStatus(dppEtlTaskDO.getId(), "-2");
            }
        }

        // Online operation
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
        String responseMsg = dsStatusRespDTO.getMsg();
        if (responseMsg.contains("SubWorkflowDefinition") && responseMsg.contains("is not online")) {
            throw new RuntimeException("存在未上线的子工作流，请先将所有子工作流上线");
        }
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("发布任务失败！");
        }

        DsSchedulerRespDTO dsSchedulerRespDTO;
        if (dppEtlSchedulerById.getDsId() == null || dppEtlSchedulerById.getDsId() < 1) {
            dsSchedulerRespDTO = createOrUpdateScheduler(dppEtlSchedulerById, dppEtlTaskDO);
        } else {
            dsSchedulerRespDTO = updateExistingScheduler(dppEtlSchedulerById, dppEtlTaskDO);
        }

        // Update scheduler and bring online
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);
        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerSaveReqVO.getDsId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("dpp.error.scheduler.online", "上线调度器，失败！");
        }

        // Update scheduler
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);

        // Update task status
        if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        } else {
            updateTaskStatus(dppEtlTaskDO.getId(), "-3");
        }
    }

    /**
     * @param releaseState // Upper/lower limit 0: not online, 1: online
     */
    private void wrapCustomNodeStatus(Long id, String releaseState) {
        DppEtlTaskRespVO dppEtlTaskById = this.getDppEtlTaskById(id);
        List<DppEtlNodeRespVO> taskDefinitionList = dppEtlTaskById.getTaskDefinitionList();

        // Loop to get custom tasks
        for (DppEtlNodeRespVO dppEtlNodeRespVO : taskDefinitionList) {
            buildSubCustomTaskIdList(dppEtlNodeRespVO, releaseState);
        }
    }

    private DppEtlNewNodeSaveReqVO buildSubCustomTaskIdList(DppEtlNodeRespVO dppEtlNodeRespVO, String releaseState) {
        String parameters = dppEtlNodeRespVO.getParameters();
        Map<String, Object> stringObjectMap = JSONUtils.convertTaskDefinitionJsonMap(parameters);
        long subTaskId = MapUtils.getLongValue(stringObjectMap, "subTaskId");
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(subTaskId);
        if ((StringUtils.equals("0", dppEtlTaskDO.getStatus()) || StringUtils.equals("-2", dppEtlTaskDO.getStatus()))
                && StringUtils.equals("1", releaseState)) {
            throw new RuntimeException("存在未上线的子工作流，请先将所有子工作流上线");
        }
//
//
//        if(StringUtils.equals("-2",dppEtlTaskDO.getStatus()) || StringUtils.equals("-3",dppEtlTaskDO.getStatus())){
//            DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO = new DppEtlNewNodeSaveReqVO();
//            dppEtlNewNodeSaveReqVO.setId(String.valueOf(dppEtlTaskDO.getId()));
//            return dppEtlNewNodeSaveReqVO;
//        }
        return null;
    }

    // Update task status
    private void updateTaskStatus(Long taskId, String releaseState) {
        DppEtlTaskSaveReqVO updateReqVO = new DppEtlTaskSaveReqVO();
        updateReqVO.setId(taskId);
        updateReqVO.setStatus(releaseState);
        this.updateDppEtlTask(updateReqVO);
    }

    // Create or update scheduler
    private DsSchedulerRespDTO createOrUpdateScheduler(DppEtlSchedulerDO dppEtlSchedulerById, DppEtlTaskDO dppEtlTaskDO) {
        DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = TaskConverter.createSchedulerRequest(dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(dppEtlTaskDO.getProjectCode()));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            if (byTaskCode == null || !byTaskCode.getSuccess()) {
                throw new ServiceException("dpp.error.scheduler.create", "创建调度器，失败！");
            }
            Schedule data = byTaskCode.getData();
            DsSchedulerUpdateReqDTO schedulerUpdateRequest = TaskConverter.createSchedulerUpdateRequest(data.getId(), dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
            dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
            if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                throw new ServiceException("dpp.error.scheduler.update", "更新调度器，失败！");
            }
        }
        return dsSchedulerRespDTO;
    }

    // Update existing scheduler
    private DsSchedulerRespDTO updateExistingScheduler(DppEtlSchedulerDO dppEtlSchedulerById, DppEtlTaskDO dppEtlTaskDO) {
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = TaskConverter.createSchedulerUpdateRequest(dppEtlSchedulerById.getDsId(), dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            if (byTaskCode == null || !byTaskCode.getSuccess()) {
                DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = TaskConverter.createSchedulerRequest(dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
                DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(dppEtlTaskDO.getProjectCode()));
                if (saveScheduler == null || !saveScheduler.getSuccess()) {
                    throw new ServiceException("dpp.error.scheduler.create", "创建调度器，失败！");
                }
                return byTaskCode;
            }
            Schedule data = byTaskCode.getData();
            DsSchedulerUpdateReqDTO updateRequest = TaskConverter.createSchedulerUpdateRequest(data.getId(), dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
            dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(updateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
            if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                throw new ServiceException("修改调度器，失败！");
            }
        }
        return dsSchedulerRespDTO;
    }


    @Override
    public DppEtlTaskRespVO getDppEtlTaskById(Long id) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
        DppEtlTaskRespVO bean = BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskRespVO.class);

        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = this.getTaskNodeRelList(bean);
        bean.setTaskRelationJson(dppEtlTaskNodeRelRespVOList);
        String type = bean.getType();

        List<DppEtlNodeRespVO> etlNodeLogRespVOList = this.getNodeRespListByTaskNodeRelList(dppEtlTaskNodeRelRespVOList);

        bean.setTaskDefinitionList(removeDuplicateById(etlNodeLogRespVOList, type));
        return bean;
    }


    @Override
    @Transactional
    public DppEtlTaskSaveReqVO createProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (ScheduleConstants.QUARTZ.equals(dppEtlNewNodeSaveReqVO.getScheduler())) {
            return createProcessDefinitionQuartz(dppEtlNewNodeSaveReqVO);
        }
        // Compatible with creating task first, then enriching details later
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        //
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();

        // Save
        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        Long dppEtlTask;
        if (isUpdate) {
            dppEtlTask = JSONUtils.convertToLong(saveReqVOId);
            dppEtlTaskSaveReqVO.setId(dppEtlTask);
            this.updateDppEtlTask(dppEtlTaskSaveReqVO);
        } else {
            dppEtlTask = this.createDppEtlTask(dppEtlTaskSaveReqVO);
        }
        dppEtlTaskSaveReqVO.setId(dppEtlTask);

        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dppEtlTask, dppEtlTaskSaveReqVO.getCode(), dppEtlNewNodeSaveReqVO);
        if (isUpdate) {
            DppEtlSchedulerDO dppEtlSchedulerById = getDppEtlScheduler(dppEtlTaskSaveReqVO.getCode(), dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setTaskCode(dppEtlTaskSaveReqVO.getCode());
            dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        } else {
            iDppEtlSchedulerService.createDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        }

        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskLogSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(data, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(data, dppEtlNewNodeSaveReqVO);

        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(data.getTaskRelationList(), dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, data.getCode(), data.getVersion());
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(data.getTaskRelationList(), dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO, data.getCode(), data.getVersion());
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return dppEtlTaskSaveReqVO; // Return creation result
    }

    /**
     * 创建任务
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    private DppEtlTaskSaveReqVO createProcessDefinitionQuartz(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        //兼容先创建任务，再丰满信息
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);
        // 创建调度器
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(Long.valueOf(saveReqVOId));
        dppEtlTaskDO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        Long quartzId = dppTaskQuartzService.create(dppEtlTaskDO, "dppQuartzJob.dataDevelopment(%sL)");

        // 创建或者更新调度器信息
        Long taskId = JSONUtils.convertToLong(saveReqVOId);
        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtil.copyProperties(dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setStatus("0");
        dppEtlTaskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        if (isUpdate) {
            dppEtlTaskSaveReqVO.setQuartzId(quartzId);
            this.updateDppEtlTask(dppEtlTaskSaveReqVO);
        } else {
            taskId = this.createDppEtlTask(dppEtlTaskSaveReqVO);
        }
        dppEtlTaskSaveReqVO.setId(taskId);

        // 创建调度信息
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(taskId, dppEtlTaskSaveReqVO.getCode(), dppEtlNewNodeSaveReqVO);
        if (isUpdate) {
            DppEtlSchedulerDO dppEtlSchedulerById = getDppEtlScheduler(dppEtlTaskSaveReqVO.getCode(), dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setTaskCode(dppEtlTaskSaveReqVO.getCode());
            dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setQuartzId(quartzId);
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        } else {
            iDppEtlSchedulerService.createDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        }

        // 创建任务日志
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = BeanUtils.toBean(dppEtlNewNodeSaveReqVO, DppEtlTaskLogSaveReqVO.class);
        dppEtlTaskLogSaveReqVO.setId(null);
        dppEtlTaskLogSaveReqVO.setDsId(0L);
        dppEtlTaskLogSaveReqVO.setQuartzId(quartzId);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        // 创建节点
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        List<TaskDefinition> taskDefinitionList = BeanUtil.copyToList(list, TaskDefinition.class);
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(taskDefinitionList, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // 创建节点日志
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = BeanUtil.copyToList(dppEtlNodeSaveReqVOList, DppEtlNodeLogSaveReqVO.class);
        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        Integer version = dppEtlNewNodeSaveReqVO.getVersion();
        String code = dppEtlNewNodeSaveReqVO.getCode();

        // 创建节点关系信息
        list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());
        List<ProcessTaskRelation> processTaskRelations = BeanUtil.copyToList(list, ProcessTaskRelation.class);
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, code, version);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // 创建节点关系日志
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO, code, version);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return dppEtlTaskSaveReqVO; // 返回创建结果
    }

    @Override
    public DppEtlTaskSaveReqVO updateProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        if (StringUtils.equals("1", dppEtlTaskDO.getStatus()) || StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            throw new ServiceException("上线任务，不允许修改，请先下线！");
        }

        // 兼容创建任务，再丰满信息
        if (ScheduleConstants.QUARTZ.equals(dppEtlTaskDO.getScheduler())) {
            return updateProcessDefinitionQuartz(dppEtlNewNodeSaveReqVO, dppEtlTaskDO);
        }

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO
                , String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), String.valueOf(dppEtlTaskDO.getCode()));

        if (!task.getSuccess()) {
            throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        dppEtlTaskSaveReqVO.setId(dppEtlTaskDO.getId());
        this.updateDppEtlTask(dppEtlTaskSaveReqVO);


        // Delete relations first, then add new ones
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());
        // Delete nodes first, then add new ones
        iDppEtlNodeService.removeOldDppEtlNode(TaskConverter.getPreAndPostNodeCodeList(dppEtlTaskNodeRelRespVOList));

        // Add new
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(data, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);
        // Add new
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(data.getTaskRelationList(), dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, data.getCode(), data.getVersion());
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);


        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskLogSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        DppEtlTaskLogRespVO dppEtlTaskLogByRequest = this.getDppEtlTaskLogByRequest(dppEtlTaskLogSaveReqVO);
        if (dppEtlTaskLogByRequest == null) {
            Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);
        } else {
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLogByRequest.getId());
            iDppEtlTaskLogService.updateDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        }


        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(data, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = new ArrayList<>();
        for (DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO : dppEtlNodeLogSaveReqVOS) {
            DppEtlNodeLogDO dppEtlNodeLogRespVOByReqVO = this.getDppEtlNodeLogByCodeAndVersion(dppEtlNodeLogSaveReqVO);
            if (dppEtlNodeLogRespVOByReqVO == null) {
                dppEtlNodeLogRespVOByReqVO = iDppEtlNodeLogService.createDppEtlNodeLogNew(dppEtlNodeLogSaveReqVO);
            }
            dppEtlNodeLogBatch.add(dppEtlNodeLogRespVOByReqVO);
        }

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(data.getTaskRelationList(), dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO, data.getCode(), data.getVersion());
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            DppEtlTaskNodeRelLogRespVO dppEtlTaskNodeRelLogById = this.getDppEtlTaskNodeRelLogByRequest(dppEtlTaskNodeRelLogSaveReqVO);
            if (dppEtlTaskNodeRelLogById == null) {
                iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLog(dppEtlTaskNodeRelLogSaveReqVO);

            }
        }
        return dppEtlTaskSaveReqVO;
    }

    private DppEtlTaskSaveReqVO updateProcessDefinitionQuartz(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, DppEtlTaskDO dppEtlTaskDO) {

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtil.copyProperties(dppEtlNewNodeSaveReqVO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setId(dppEtlTaskDO.getId());
        dppEtlTaskSaveReqVO.setLocations(JSONUtils.toJson(dppEtlNewNodeSaveReqVO.getLocations()));
        this.updateDppEtlTask(dppEtlTaskSaveReqVO);

        //rel 先删除，再新增
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());
        //node 先删除，再新增
        iDppEtlNodeService.removeOldDppEtlNode(TaskConverter.getPreAndPostNodeCodeList(dppEtlTaskNodeRelRespVOList));

        // 创建节点
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        List<TaskDefinition> taskDefinitionList = BeanUtil.copyToList(list, TaskDefinition.class);
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(taskDefinitionList, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // 创建节点关系信息
        Integer version = dppEtlNewNodeSaveReqVO.getVersion();
        String code = dppEtlNewNodeSaveReqVO.getCode();
        list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());
        List<ProcessTaskRelation> processTaskRelations = BeanUtil.copyToList(list, ProcessTaskRelation.class);
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, code, version);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // 创建任务日志
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = BeanUtils.toBean(dppEtlNewNodeSaveReqVO, DppEtlTaskLogSaveReqVO.class);
        DppEtlTaskLogRespVO dppEtlTaskLogByRequest = this.getDppEtlTaskLogByRequest(dppEtlTaskLogSaveReqVO);
        if (dppEtlTaskLogByRequest == null) {
            Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);
        } else {
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLogByRequest.getId());
            iDppEtlTaskLogService.updateDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        }

        // 创建节点日志
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = BeanUtil.copyToList(dppEtlNodeSaveReqVOList, DppEtlNodeLogSaveReqVO.class);
        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = new ArrayList<>();
        for (DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO : dppEtlNodeLogSaveReqVOS) {
            DppEtlNodeLogDO dppEtlNodeLogRespVOByReqVO = this.getDppEtlNodeLogByCodeAndVersion(dppEtlNodeLogSaveReqVO);
            if (dppEtlNodeLogRespVOByReqVO == null) {
                dppEtlNodeLogRespVOByReqVO = iDppEtlNodeLogService.createDppEtlNodeLogNew(dppEtlNodeLogSaveReqVO);
            }
            dppEtlNodeLogBatch.add(dppEtlNodeLogRespVOByReqVO);
        }

        // 创建节点关系日志
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO, code, version);
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            DppEtlTaskNodeRelLogRespVO dppEtlTaskNodeRelLogById = this.getDppEtlTaskNodeRelLogByRequest(dppEtlTaskNodeRelLogSaveReqVO);
            if (dppEtlTaskNodeRelLogById == null) {
                iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLog(dppEtlTaskNodeRelLogSaveReqVO);

            }
        }
        return dppEtlTaskSaveReqVO;
    }

    @Override
    public Map<String, Object> releaseTaskCrontab(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskId(dppEtlTaskDO.getId());
        dppEtlSchedulerPageReqVO.setTaskCode(dppEtlTaskDO.getCode());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        // Compensation
        if (dppEtlSchedulerById == null) {
            DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dppEtlTaskDO.getId(), dppEtlTaskDO.getCode(), dppEtlNewNodeSaveReqVO);
            dppEtlSchedulerById = iDppEtlSchedulerService.createDppEtlSchedulerNew(dppEtlSchedulerSaveReqVO);
        }

        if (StringUtils.equals("1", dppEtlSchedulerById.getStatus())) {
            throw new ServiceException("调度上线中，不允许改，请先下线！");
        }

        if (StringUtils.isEmpty(dppEtlNewNodeSaveReqVO.getCrontab()) ||
                StringUtils.equals(dppEtlSchedulerById.getCronExpression(), dppEtlNewNodeSaveReqVO.getCrontab())) {
            return new HashMap<>();
        }
        DsSchedulerRespDTO dsSchedulerRespDTO = null;
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
        if (dppEtlSchedulerById.getDsId() != null && dppEtlSchedulerById.getDsId() > 0) {
            //     * Update scheduler (only callable after task is published)
            DsSchedulerUpdateReqDTO schedulerUpdateRequest = TaskConverter.createSchedulerUpdateRequest(dppEtlSchedulerById.getDsId(), dppEtlNewNodeSaveReqVO.getCrontab(), dppEtlTaskDO.getCode());
            dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
            if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
                if (byTaskCode != null && byTaskCode.getSuccess()) {
                    Schedule data = byTaskCode.getData();
                    DsSchedulerUpdateReqDTO updateRequest = TaskConverter.createSchedulerUpdateRequest(data.getId(), dppEtlNewNodeSaveReqVO.getCrontab(), dppEtlTaskDO.getCode());
                    dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(updateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
                    if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                        throw new ServiceException("修改调度器，失败！");
                    }
                }
            }
            dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);
        } else {
            dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
            dppEtlSchedulerSaveReqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        }
        // 更新Quartz调度任务
        if (dppEtlSchedulerById.getQuartzId() != null && dppEtlSchedulerById.getQuartzId() > 0) {
            dppTaskQuartzService.update(dppEtlSchedulerById.getQuartzId(), dppEtlNewNodeSaveReqVO.getCrontab());
        }
        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);


        return new HashMap<>();
    }

    @Override
    public DppEtlTaskUpdateQueryRespVO getuUpdateQueryInfo(Long id) {
        MPJLambdaWrapper<DppEtlTaskDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DppEtlTaskDO.class)
                .select("t3.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DppEtlTaskDO::getId, id);

        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectJoinOne(DppEtlTaskDO.class, lambdaWrapper);
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = this.getTaskNodeRelList(BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskRespVO.class));

        DppEtlTaskUpdateQueryRespVO bean = new DppEtlTaskUpdateQueryRespVO(dppEtlTaskDO);
        bean.setTaskRelationJsonFromNodeRelList(dppEtlTaskNodeRelRespVOList);
        String type = bean.getType();

        // Get scheduler info
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskCode(bean.getCode());
        dppEtlSchedulerPageReqVO.setTaskId(bean.getId());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        dppEtlSchedulerById = dppEtlSchedulerById == null ? new DppEtlSchedulerDO() : dppEtlSchedulerById;
        bean.setCrontab(dppEtlSchedulerById.getCronExpression());
        bean.setSchedulerState(dppEtlSchedulerById.getStatus());

        // Get the last execution instance
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceService.getLastTaskInstanceByTaskCode(bean.getCode());
        if (dppEtlTaskInstanceDO != null) {
            bean.setLastExecuteTime(dppEtlTaskInstanceDO.getStartTime());
            bean.setLastExecuteStatus(dppEtlTaskInstanceDO.getStatus());
        }
        List<DppEtlNodeRespVO> etlNodeLogRespVOList = this.getNodeRespListByTaskNodeRelList(dppEtlTaskNodeRelRespVOList);
        if (etlNodeLogRespVOList.size() > 0) {
            for (DppEtlNodeRespVO dppEtlNodeRespVO : etlNodeLogRespVOList) {
                if (StringUtils.equals(TaskComponentTypeEnum.DB_READER.getCode(), dppEtlNodeRespVO.getComponentType())) {
                    String nodeCode = dppEtlNodeRespVO.getCode();
                    JSONObject taskParams = JSONObject.parse(dppEtlNodeRespVO.getParameters());
                    // Read mode 1: Full 2: ID increment 3: Time range increment, default Full
                    String readModeType = taskParams.getString("readModeType");
                    if (StringUtils.equals("2", readModeType)) {
                        JSONObject idIncrementConfig = taskParams.getJSONObject("idIncrementConfig");
                        String incrementColumn = idIncrementConfig.getString("incrementColumn");
                        String cacheKey = TaskConverter.ETL_READER_ID_KEY + nodeCode + ":" + incrementColumn;
                        if (redisService.hasKey(cacheKey)) {
                            idIncrementConfig.put("incrementStart", redisService.get(cacheKey));
                        }
                    } else if (StringUtils.equals("3", readModeType)) {
                        JSONObject dateIncrementConfig = taskParams.getJSONObject("dateIncrementConfig");
                        List<JSONObject> columnList = dateIncrementConfig.getJSONArray("column").stream().map(e -> {
                            return (JSONObject) e;
                        }).collect(Collectors.toList());
                        for (int i = 0; i < columnList.size(); i++) {
                            JSONObject jsonObject = columnList.get(i);
                            // Type 1: Fixed value 2: Time range 3: SQL expression
                            if (!StringUtils.equals("2", jsonObject.getString("type"))) {
                                continue;
                            }
                            // Increment column
                            String incrementColumn = jsonObject.getString("incrementColumn");
                            String cacheKey = TaskConverter.ETL_READER_DATE_KEY + nodeCode + ":" + incrementColumn;
                            if (redisService.hasKey(cacheKey)) {
                                jsonObject.put("cursorTime", redisService.get(cacheKey));
                            }
                        }
                    }
                    dppEtlNodeRespVO.setParameters(taskParams.toJSONString());
                }
            }
        }
        bean.setTaskDefinitionList(removeDuplicateById(etlNodeLogRespVOList, type));
        bean.createTaskConfig();
        return bean;
    }


    public List<DppEtlNodeRespVO> getNodeRespListByTaskNodeRelList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {

        if (CollectionUtils.isEmpty(dppEtlTaskNodeRelRespVOList)) {
            return new ArrayList<>();
        }

        // Collect all preNodeCode and postNodeCode
        Set<String> nodeCodeSet = new HashSet<>();
        for (DppEtlTaskNodeRelRespVO relVO : dppEtlTaskNodeRelRespVOList) {
            if (relVO.getPreNodeCode() != null) {
                nodeCodeSet.add(relVO.getPreNodeCode());
            }
            if (relVO.getPostNodeCode() != null) {
                nodeCodeSet.add(relVO.getPostNodeCode());
            }
        }

        if (CollectionUtils.isEmpty(nodeCodeSet)) {
            return new ArrayList<>();
        }

        // Query node info
        DppEtlNodePageReqVO pageReqVO = new DppEtlNodePageReqVO();
        pageReqVO.setCodeList(new ArrayList<>(nodeCodeSet));
        return iDppEtlNodeService.getDppEtlNodeRespList(pageReqVO);
    }

    @Override
    public Long getTaskIdByTaskCode(String taskCode) {
        DppEtlTaskDO dppEtlTaskDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlTaskDO.class)
                .eq(DppEtlTaskDO::getCode, taskCode)
                .select(DppEtlTaskDO::getId));
        if (dppEtlTaskDO != null) {
            return dppEtlTaskDO.getId();
        }
        return null;
    }

    @Override
    public DppEtlTaskRespDTO getTaskByTaskCode(String taskCode) {
        DppEtlTaskDO dppEtlTaskDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlTaskDO.class)
                .eq(DppEtlTaskDO::getCode, taskCode));
        if (dppEtlTaskDO != null) {
            return BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskRespDTO.class);
        }
        return null;
    }

    /**
     * Create request object and get node log by dsId
     *
     * @param dppEtlNodeLogSaveReqVO Node log save request object
     * @return DppEtlNodeLogDO Return node log info
     */
    public DppEtlNodeLogDO getDppEtlNodeLogByDsId(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // Create request object
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setDsId(dppEtlNodeLogSaveReqVO.getDsId());

        // Call service method to get node log info
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    public DppEtlNodeLogDO getDppEtlNodeLogByCodeAndVersion(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // Create request object
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setCode(dppEtlNodeLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlNodeLogSaveReqVO.getVersion());

        // Call service method to get node log info
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    /**
     * Create request object and get log by task node log info
     *
     * @param dppEtlTaskNodeRelLogSaveReqVO Task node log save request object
     * @return DppEtlTaskNodeRelLogRespVO Return task node log response object
     */
    public DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogByRequest(DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO) {
        // Create request object
        DppEtlTaskNodeRelLogPageReqVO reqVO = new DppEtlTaskNodeRelLogPageReqVO();
        reqVO.setTaskCode(dppEtlTaskNodeRelLogSaveReqVO.getTaskCode());
        reqVO.setTaskVersion(dppEtlTaskNodeRelLogSaveReqVO.getTaskVersion());

        List<DppEtlTaskNodeRelLogRespVO> dppEtlTaskNodeRelLogRespVOList = iDppEtlTaskNodeRelLogService.getDppEtlTaskNodeRelLogRespVOList(reqVO);
        if (CollectionUtils.isNotEmpty(dppEtlTaskNodeRelLogRespVOList)) {
            for (DppEtlTaskNodeRelLogRespVO dppEtlTaskNodeRelLogRespVO : dppEtlTaskNodeRelLogRespVOList) {
                if (dppEtlTaskNodeRelLogRespVO != null) {
                    return dppEtlTaskNodeRelLogRespVO;
                }
            }
        }

        // Call service method to get task node log info
        return null;
    }

    /**
     * Create request object and get task by task log info
     *
     * @param dppEtlTaskLogSaveReqVO Task log save request object
     * @return DppEtlTaskLogRespVO Return task log response object
     */
    public DppEtlTaskLogRespVO getDppEtlTaskLogByRequest(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO) {
        // Create request object
        DppEtlTaskLogPageReqVO reqVO = new DppEtlTaskLogPageReqVO();
        reqVO.setCode(dppEtlTaskLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlTaskLogSaveReqVO.getVersion());

        // Call service method to get task log info
        return iDppEtlTaskLogService.getDppEtlTaskLogById(reqVO);
    }

    @Override
    public AjaxResult startDppEtlTask(Long id) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
        if (dppEtlTaskDO == null) {
            return error("任务不存在，请刷新后重试！");
        }
        if (!StringUtils.equals("1", dppEtlTaskDO.getStatus())) {
            DppEtlNewNodeSaveReqVO nodeSaveReqVO = new DppEtlNewNodeSaveReqVO();
            nodeSaveReqVO.setId(String.valueOf(id));
            nodeSaveReqVO.setReleaseState("1");
            this.updateReleaseJobTask(nodeSaveReqVO);
//            return error("Task status error, please refresh and try again!");
        }
        if (StringUtils.equals(dppEtlTaskDO.getScheduler(), ScheduleConstants.QUARTZ)) {
            // Quartz + DataX 是本地执行，不再调用 DS 的 startTask。
            return startLocalDataXTask(dppEtlTaskDO);
        }

        //1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlTaskDO.getType();

        // Check if it is an offline task; if so, get the task code from extended info for API call
        if (StringUtils.equals("1", type)) {
            // Get extended info
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(dppEtlTaskDO.getId());
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }


        DsStartTaskReqDTO dsStartTaskReqDTO = TaskConverter.createDsStartTaskReqDTO(dppEtlTaskDO.getCode());

        try {
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(dsStartTaskReqDTO, dppEtlTaskDO.getProjectCode());
            return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
        } catch (Exception e) {
            throw new ServiceException("dpp.error.scheduler.start", "执行调度器，失败！");
        }
    }

    /**
     * 启动 Quartz + DataX 本地任务。
     * 这里是页面手动执行一次 DataX，会生成 DataX JSON、调用 datax.py、采集日志，并把状态写回任务实例。
     */
    private AjaxResult startLocalDataXTask(DppEtlTaskDO dppEtlTaskDO) {
        try {
            schedulerAdapter.trigger(ScheduleCommand.builder().id(dppEtlTaskDO.getQuartzId()).build());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return success("DataX任务执行成功");
    }

    /**
     * 启动数据集成任务
     *
     * @param id
     */
    @Override
    public void startDppEtlTaskDataIntegration(Long id) {
        // 数据集成任务由本地创建 JSON 调用datax.py 执行器处理，先查询完整任务信息和节点配置。
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
        if (dppEtlTaskDO == null) {
            throw new ServiceException("任务不存在，请刷新后重试！");
        }

        // 先创建任务实例，保证后续状态和日志都能关联到同一次执行。
        DppEtlTaskInstanceDO instance = dppEtlTaskDataIntegrationRunner.createLocalDataXTaskInstance(dppEtlTaskDO);
        StringBuilder taskLog = new StringBuilder();
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Initialize DataX task context *******************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Begin to initialize task");
        LogUtils.appendLocalLogLine(taskLog, "Set task startTime: " + instance.getStartTime().getTime());
        LogUtils.appendLocalLogLine(taskLog, "Set task appId: " + dppEtlTaskDO.getId() + "_" + instance.getId());

        // 启动数据集成任务
        dppEtlTaskDataIntegrationRunner.startDppEtlTaskDataIntegration(dppEtlTaskDO, instance, taskLog);
    }

    /**
     * 启动数据开发任务
     *
     * @param id 任务id
     */
    @Override
    public void startDppEtlTaskDataDevelopment(Long id) {
        // 数据开发任务由本地 JDBC 执行器处理，先查询完整任务信息和节点配置。
        DppEtlTaskDO task = dppEtlTaskMapper.selectById(id);
        // 任务不存在时直接返回错误，避免执行器创建无效实例。
        if (task == null) {
            error("任务不存在，请刷新后重试！");
            return;
        }
        // 将 JDBC 执行、实例记录、日志记录等细节封装到 dpp.jdbc 包中。
        // 先落任务实例，再开始执行，确保后续所有日志都有实例可以挂载。
        DppEtlTaskInstanceDO instance = dataDevelopmentJdbcTaskRunner.createDataDevelopmentTaskInstance(task);
        StringBuilder taskLog = new StringBuilder();
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Initialize task context ***********************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Begin to initialize task");
        LogUtils.appendLocalLogLine(taskLog, "Set task startTime: " + instance.getStartTime().getTime());
        LogUtils.appendLocalLogLine(taskLog, "Set task appId: " + task.getId() + "_" + instance.getId());
        LogUtils.appendLocalLogLine(taskLog, "End initialize task " + JSONUtils.formatJson(JSONUtils.toJson(task)));
        LogUtils.appendLocalLogLine(taskLog, "End initialize instance " + JSONUtils.formatJson(JSONUtils.toJson(instance)));

        dataDevelopmentJdbcTaskRunner.run(task, instance, taskLog);
    }

    @Override
    public List<DppEtlTaskTreeRespVO> getDppEtlTaskListTree(DppEtlTaskPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskDO.class)
                .ne(DppEtlTaskDO::getStatus, "-2")
                .ne(DppEtlTaskDO::getStatus, "-3")
                .eq(reqVO.getProjectId() != null, DppEtlTaskDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotEmpty(reqVO.getProjectCode()), DppEtlTaskDO::getProjectCode, reqVO.getProjectCode())
                .ne(DppEtlTaskDO::getType, "4");
        List<DppEtlTaskDO> dppEtlTaskDOS = dppEtlTaskMapper.selectList(wrapper);
        List<DppEtlTaskRespVO> dppEtlTaskRespVOList = BeanUtils.toBean(dppEtlTaskDOS, DppEtlTaskRespVO.class);

        List<AttTaskCatRespDTO> attTaskCatApiList = iAttTaskCatApiService.getAttTaskCatApiList(new AttTaskCatReqDTO());
        List<AttDataDevCatRespDTO> attDataDevCatApiList = iAttDataDevCatApiService.getAttDataDevCatApiList(new AttDataDevCatReqDTO());

        List<DppEtlTaskTreeRespVO> result = new ArrayList<>();
        DppEtlTaskTreeRespVO builtTaskCatTree = buildTaskCatTree(dppEtlTaskRespVOList, attTaskCatApiList);
        DppEtlTaskTreeRespVO builtTaskDevCaTree = buildTaskDevCaTree(dppEtlTaskRespVOList, attDataDevCatApiList);


        result.add(builtTaskCatTree);
        result.add(builtTaskDevCaTree);
        return result;
    }

    private DppEtlTaskTreeRespVO buildTaskDevCaTree(List<DppEtlTaskRespVO> dppEtlTaskRespVOList, List<AttDataDevCatRespDTO> attDataDevCatApiList) {

        // 1. Create top-level directory node
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        root.setId(IdUtils.generateArtificialId());
        root.setTreeId(IdUtils.generateArtificialId());
        root.setLabel("数据开发");
        root.setChildren(new ArrayList<>());

        // 2. Organize category list: convert each AttTaskCatRespDTO to DppEtlTaskTreeRespVO node and put into map (key is category id)
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // Regenerate an ID
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // Category node's dppEtlTaskCount will be assigned later
            catNodeMap.put(cat.getId(), node);
        }

        // 3. Build category hierarchy, construct tree structure based on parentId
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // If parent category exists, add to parent's children
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // No parent category, it is a root-level category
                catRoots.add(node);
            }
        }

        // 4. Filter tasks with type "3" from the task list
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "3".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // For easy lookup of category nodes by category code, build a code-to-node mapping
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // 5. Iterate tasks, mount each task under the corresponding category node (match condition: task's catCode equals category node's code)
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                // Convert task to tree node
                DppEtlTaskTreeRespVO taskNode = new DppEtlTaskTreeRespVO();
                taskNode.setTreeId(IdUtils.generateArtificialId());
                taskNode.setId(task.getId());
                taskNode.setLabel(task.getName());
                taskNode.setType(task.getType());
                taskNode.setName(task.getName());
                taskNode.setCode(task.getCode());
                taskNode.setVersion(task.getVersion());
                taskNode.setProjectId(task.getProjectId());
                taskNode.setProjectCode(task.getProjectCode());
                taskNode.setPersonCharge(task.getPersonCharge());
                taskNode.setContactNumber(task.getContactNumber());
                taskNode.setLocations(task.getLocations());
                taskNode.setDescription(task.getDescription());
                taskNode.setExecutionType(task.getExecutionType());
                taskNode.setStatus(task.getStatus());
                taskNode.setDsId(task.getDsId());
                taskNode.setChildren(new ArrayList<>());

                // Add task node to the corresponding category node's children
                categoryNode.getChildren().add(taskNode);
            }
        }
//
//        // 6. Assign task count for each category node (dppEtlTaskCount only counts directly mounted tasks)
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
//            // Here, among child nodes, those with non-null type are treated as task nodes
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // 6. Mount the organized category tree under the top-level directory
        root.getChildren().addAll(catRoots);

        // 7. Recursively compute task count for each node (including all tasks in child nodes)
        computeTaskCount(root);
        // Return top-level directory node list (only one root node)
        return root;
    }

    /**
     * Build data integration task tree
     *
     * @param dppEtlTaskRespVOList Task list (where catCode stores the code from AttTaskCatRespDTO)
     * @param attTaskCatApiList    Category list, AttTaskCatRespDTO has parent-child relationships, example: parent code “A01”, first child code “A01A01”
     * @return List<DppEtlTaskTreeRespVO> Constructed task tree, top-level directory is “Data Integration”
     */
    public DppEtlTaskTreeRespVO buildTaskCatTree(List<DppEtlTaskRespVO> dppEtlTaskRespVOList,
                                                 List<AttTaskCatRespDTO> attTaskCatApiList) {
        // 1. Create top-level directory node
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        // Regenerate an ID
        root.setTreeId(IdUtils.generateArtificialId());
        root.setId(IdUtils.generateArtificialId());
        root.setLabel("数据集成");
        root.setChildren(new ArrayList<>());

        // 2. Organize category list: convert each AttTaskCatRespDTO to DppEtlTaskTreeRespVO node and put into map (key is category id)
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // Regenerate an ID
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // Category node's dppEtlTaskCount will be assigned later
            catNodeMap.put(cat.getId(), node);
        }

        // 3. Build category hierarchy, construct tree structure based on parentId
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // If parent category exists, add to parent's children
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // No parent category, it is a root-level category
                catRoots.add(node);
            }
        }

        // 4. Filter tasks with type "1" or "2" from the task list
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "1".equals(task.getType()) || "2".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // For easy lookup of category nodes by category code, build a code-to-node mapping
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // 5. Iterate tasks, mount each task under the corresponding category node (match condition: task's catCode equals category node's code)
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                DppEtlTaskExtDO etlTaskExtDO = dppEtlTaskExtService.getByTaskId(task.getId());
                // Convert task to tree node
                DppEtlTaskTreeRespVO taskNode = new DppEtlTaskTreeRespVO();
                // Regenerate an ID
                taskNode.setTreeId(IdUtils.generateArtificialId());
                taskNode.setId(task.getId());
                taskNode.setLabel(task.getName());
                taskNode.setType(task.getType());
                taskNode.setName(task.getName());
                taskNode.setCode(task.getCode());
                if (etlTaskExtDO != null) {
                    taskNode.setExtCode(etlTaskExtDO.getEtlTaskCode());
                }
                taskNode.setVersion(task.getVersion());
                taskNode.setProjectId(task.getProjectId());
                taskNode.setProjectCode(task.getProjectCode());
                taskNode.setPersonCharge(task.getPersonCharge());
                taskNode.setContactNumber(task.getContactNumber());
                taskNode.setLocations(task.getLocations());
                taskNode.setDescription(task.getDescription());
                taskNode.setExecutionType(task.getExecutionType());
                taskNode.setStatus(task.getStatus());
                taskNode.setDsId(task.getDsId());
                taskNode.setChildren(new ArrayList<>());

                // Add task node to the corresponding category node's children
                categoryNode.getChildren().add(taskNode);
            }
        }
//
//        // 6. Assign task count for each category node (dppEtlTaskCount only counts directly mounted tasks)
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
//            // Here, among child nodes, those with non-null type are treated as task nodes
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // 6. Mount the organized category tree under the top-level directory
        root.getChildren().addAll(catRoots);

        // 7. Recursively compute task count for each node (including all tasks in child nodes)
        computeTaskCount(root);

        // Return top-level directory node list (only one root node)
        return root;
    }


    /**
     * Recursively compute the task count for a node and assign it to dppEtlTaskCount
     * If the node is a task node (type != null), count is 1;
     * If the node is a category node (type == null), count is the sum of all child node task counts
     *
     * @param node Current node
     * @return Total task count for current node and its child nodes
     */
    private static int computeTaskCount(DppEtlTaskTreeRespVO node) {
        int count = 0;
        // If it is a task node, count is 1
        if (node.getType() != null) {
            count = 1;
        }
        // If child nodes exist, recursively accumulate
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            for (DppEtlTaskTreeRespVO child : node.getChildren()) {
                count += computeTaskCount(child);
            }
        }
        node.setDppEtlTaskCount(count);
        return count;
    }

    @Override
    public int checkTaskIdInDatasource(List<Long> datasourceIdList, List<Long> projectIdList) {
        return dppEtlTaskMapper.checkTaskIdInDatasource(datasourceIdList, projectIdList);
    }

    @Override
    public int checkTaskIdInAsset(List<Long> assetIdList) {
        return dppEtlTaskMapper.checkTaskIdInAsset(assetIdList);
    }

    @Override
    public long getCountByCatCode(String catCode, List<String> taskTypes) {
        return this.lambdaQuery()
                .likeRight(DppEtlTaskDO::getCatCode, catCode)
                .in(taskTypes != null && !taskTypes.isEmpty(), DppEtlTaskDO::getType, taskTypes)
                .count();
    }

    @Override
    public DppEtlNewNodeSaveReqVO createEtlTaskFront(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {

        // Generate task code
        String taskCode = String.valueOf(schedulerAdapter.generateTaskCode(dppEtlNewNodeSaveReqVO.getProjectCode()));
        if (ScheduleConstants.DOLPHINSCHEDULER.equals(dppEtlNewNodeSaveReqVO.getScheduler())) {
            DsNodeGenCodeRespDTO dsTaskGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
            taskCode = String.valueOf(dsTaskGenCodeRespDTO.getData().get(0));
        }

        DppEtlTaskSaveReqVO createReqVO = new DppEtlTaskSaveReqVO();
        createReqVO.setName(dppEtlNewNodeSaveReqVO.getName());
        createReqVO.setType(dppEtlNewNodeSaveReqVO.getType());
        createReqVO.setCatCode(dppEtlNewNodeSaveReqVO.getCatCode());
        createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId());
        createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()));
        createReqVO.setPersonCharge(dppEtlNewNodeSaveReqVO.getPersonCharge());
        createReqVO.setContactNumber(dppEtlNewNodeSaveReqVO.getContactNumber());
        createReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        createReqVO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());
        createReqVO.setDraftJson(dppEtlNewNodeSaveReqVO.getDraftJson());
        createReqVO.setScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        createReqVO.setActuator(dppEtlNewNodeSaveReqVO.getActuator());

        // Default
        createReqVO.setCode(taskCode);
        createReqVO.setStatus("-1");// Draft
        createReqVO.setLocations("");
        createReqVO.setTimeout(0L);
        createReqVO.setDsId(0L);
        createReqVO.setQuartzId(0L);

        Long dppEtlTask = this.createDppEtlTask(createReqVO);

        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
        dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTask);
        dppEtlSchedulerSaveReqVO.setTaskCode(taskCode);
        // Get time 100 years later
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);
        dppEtlSchedulerSaveReqVO.setStartTime(new Date());
        dppEtlSchedulerSaveReqVO.setEndTime(date);
        dppEtlSchedulerSaveReqVO.setTimezoneId("Asia/Shanghai"); // Default timezone
        dppEtlSchedulerSaveReqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        dppEtlSchedulerSaveReqVO.setFailureStrategy("1");
        dppEtlSchedulerSaveReqVO.setStatus("0");
        // 调度表也要保存调度器和执行引擎，后面上线、执行才能按 Quartz 或 DS 分流。
        dppEtlSchedulerSaveReqVO.setTaskScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        dppEtlSchedulerSaveReqVO.setTaskActuator(dppEtlNewNodeSaveReqVO.getActuator());
        // 草稿阶段还没有真正创建外部调度，Quartz 和 DS 都先不绑定外部调度 id。
        dppEtlSchedulerSaveReqVO.setDsId((long) -1);
        iDppEtlSchedulerService.createDppEtlScheduler(dppEtlSchedulerSaveReqVO);

        dppEtlNewNodeSaveReqVO.setId(String.valueOf(dppEtlTask));
        dppEtlNewNodeSaveReqVO.setStatus("-1");
        dppEtlNewNodeSaveReqVO.setCode(taskCode);
        return dppEtlNewNodeSaveReqVO;
    }

    /**
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    @Override
    public DppEtlTaskSaveReqVO createEtlTaskFrontPostposition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // Task type; 1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlNewNodeSaveReqVO.getType();
        if (StringUtils.equals("1", type)) {
            return createEtlTask(dppEtlNewNodeSaveReqVO);
        } else if (StringUtils.equals("2", type)) {
            return createEtlTaskFrontPostpositionRealTime(dppEtlNewNodeSaveReqVO);
        } else if (StringUtils.equals("3", type)) {
            return createProcessDefinition(dppEtlNewNodeSaveReqVO);
        } else if (StringUtils.equals("4", type)) {
            return createProcessDefinition(dppEtlNewNodeSaveReqVO);
        }
        return null;
    }

    private DppEtlTaskSaveReqVO createEtlTaskFrontPostpositionRealTime(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        return null;
    }


    /**
     * Get task code
     *
     * @param dppEtlNewNodeSaveReqVO
     * @param isUpdate
     * @return
     */
    private String getDsTaskGenCode(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO, boolean isUpdate) {
        if (isUpdate) {
            DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
            return dppEtlTaskDO.getCode();
        }
        // Generate task code
        DsNodeGenCodeRespDTO dsTaskGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        return String.valueOf(dsTaskGenCodeRespDTO.getData().get(0));
    }


    @Override
    public DppEtlTaskUpdateQueryRespVO getupdateQueryFront(Long id) {

        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
        DppEtlTaskUpdateQueryRespVO bean = new DppEtlTaskUpdateQueryRespVO(dppEtlTaskDO);

        DppEtlSchedulerDO dppEtlSchedulerById = getDppEtlScheduler(bean.getCode(), bean.getId());
        bean.setCrontab(dppEtlSchedulerById.getCronExpression());
        return bean;
    }

    public DppEtlSchedulerDO getDppEtlScheduler(String taskCode, Long taskId) {
        DppEtlSchedulerPageReqVO reqVO = new DppEtlSchedulerPageReqVO();
        reqVO.setTaskCode(taskCode);
        reqVO.setTaskId(taskId);
        DppEtlSchedulerDO result = iDppEtlSchedulerService.getDppEtlSchedulerById(reqVO);
        return result == null ? new DppEtlSchedulerDO() : result;
    }

    @Override
    public DppEtlTaskSaveReqVO copyCreateEtl(DppEtlNewNodeSaveReqVO nodeSaveReqVO) {
        DppEtlTaskUpdateQueryRespVO dppEtlTaskUpdateQueryRespVO = this.getuUpdateQueryInfo(JSONUtils.convertToLong(nodeSaveReqVO.getId()));

        // Check if it is an offline task; if so, get the task code from extended info for API call
        if (StringUtils.equals("1", dppEtlTaskUpdateQueryRespVO.getType())) {
            // Get extended info
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(nodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskUpdateQueryRespVO.setCode(taskExt.getEtlTaskCode());
        }

        DsTaskSaveRespDTO task = dsEtlTaskService.batchCopy(dppEtlTaskUpdateQueryRespVO.getCode()
                , dppEtlTaskUpdateQueryRespVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("copy任务错误:" + task.getMsg().toString()); // Throw exception for task definition creation error
        }
        ProcessDefinition data = task.getData();

        // Task type; 1: Offline task 2: Real-time task 3: Data development task 4: Job task
        String type = dppEtlTaskUpdateQueryRespVO.getType();
        if (StringUtils.equals("1", type)) {
            return copyCreateEtlTask(dppEtlTaskUpdateQueryRespVO, data);
        } else if (StringUtils.equals("2", type)) {
            return copyCreateEtlTaskFrontPostpositionRealTime(dppEtlTaskUpdateQueryRespVO, data);
        } else if (StringUtils.equals("3", type)) {
            return copyCreateProcessDefinition(dppEtlTaskUpdateQueryRespVO, data);
        } else if (StringUtils.equals("4", type)) {
            return copyCreateProcessDefinition(dppEtlTaskUpdateQueryRespVO, data);
        }
        return null;
    }


    private DppEtlTaskSaveReqVO copyCreateProcessDefinition(DppEtlTaskUpdateQueryRespVO dppEtlTaskUpdateQueryRespVO, ProcessDefinition data) {
        return null;
    }

    private DppEtlTaskSaveReqVO copyCreateEtlTaskFrontPostpositionRealTime(DppEtlTaskUpdateQueryRespVO dppEtlTaskUpdateQueryRespVO, ProcessDefinition data) {
        return null;
    }

    /**
     * copy
     *
     * @param src
     * @return
     */
    public DppEtlTaskSaveReqVO copyCreateEtlTask(DppEtlTaskUpdateQueryRespVO src, ProcessDefinition data) {
        DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO = new DppEtlNewNodeSaveReqVO(src);
        String taskCode = data.getCode();
        String name = data.getName();
        List<Map<String, Object>> locations = dppEtlNewNodeSaveReqVO.getLocations();
        Map<Long, Long> definitionCopyVO = new HashMap<>();

        // Convert task save request object
        DppEtlTaskSaveReqVO taskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        taskSaveReqVO.setCode(taskCode);
        taskSaveReqVO.setDraftJson(src.getDraftJson());

        for (Map<String, Object> location : locations) {
            Long codeold = MapUtils.getLong(location, "taskCode");
            // Generate node code
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
            String codeNew = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));

            definitionCopyVO.put(codeold, JSONUtils.convertToLong(codeNew));
            location.put("taskCode", codeNew);
        }

        // Wrap node code
        remapTaskCodes(dppEtlNewNodeSaveReqVO, definitionCopyVO);

        taskSaveReqVO.setLocations(JSONUtils.toJson(locations));
        Long dppEtlTask = this.createDppEtlTask(taskSaveReqVO);
        taskSaveReqVO.setId(dppEtlTask);

        // Build task info
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", name);

        // Build scheduler object
        DppEtlSchedulerSaveReqVO schedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(
                dppEtlTask, taskSaveReqVO.getCode(), dppEtlNewNodeSaveReqVO
        );
        iDppEtlSchedulerService.createDppEtlScheduler(schedulerSaveReqVO);

        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskLogSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        dppEtlTaskLogSaveReqVO.setLocations(JSONUtils.toJson(locations));
        dppEtlTaskLogSaveReqVO.setCode(taskCode);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(dppEtlNewNodeSaveReqVO, 1);

        // Create ETL task extended data
        dppEtlTaskExtService.createDppEtlTaskExt(DppEtlTaskExtSaveReqVO.builder()
                .taskId(dppEtlTask)
                .etlTaskCode(data.getCode())
                .etlTaskVersion(data.getVersion())
                .etlNodeId(data.getTaskDefinitionList().get(0).getId())
                .etlNodeName(data.getTaskDefinitionList().get(0).getName())
                .etlNodeCode(data.getTaskDefinitionList().get(0).getCode())
                .etlNodeVersion(data.getTaskDefinitionList().get(0).getVersion())
                .etlRelationId(data.getTaskRelationList().get(0).getId())
                .build());

        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(dppEtlNodeSaveReqVOList);
        iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeBatch, dppEtlNewNodeSaveReqVO, taskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return taskSaveReqVO; // Return creation result
    }


    public static void remapTaskCodes(DppEtlNewNodeSaveReqVO vo, Map<Long, Long> definitionCopyVO) {
        if (vo == null || definitionCopyVO == null || definitionCopyVO.isEmpty()) {
            return;
        }

        // 1) Parse taskDefinitionList
        String taskDefJson = vo.getTaskDefinitionList();
        if (taskDefJson != null && !taskDefJson.isEmpty()) {
            List<DppEtlNodeSaveReqVO> nodeList =
                    JSON.parseArray(taskDefJson, DppEtlNodeSaveReqVO.class);

            if (nodeList != null && !nodeList.isEmpty()) {
                for (DppEtlNodeSaveReqVO node : nodeList) {
                    node.setId(null);
                    // code may be a string, need to convert to Long for mapping
                    Long oldCode = JSONUtils.convertToLong(node.getCode());
                    if (oldCode != null) {
                        Long newCode = definitionCopyVO.get(oldCode);
                        if (newCode != null) {
                            node.setCode(String.valueOf(newCode));
                        }
                    }
                }
                vo.setTaskDefinitionList(JSON.toJSONString(nodeList));
            }
        }

        // 2) Parse taskRelationJson
        String relJson = vo.getTaskRelationJson();
        if (relJson != null && !relJson.isEmpty()) {
            List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList =
                    JSON.parseArray(relJson, DppEtlTaskNodeRelRespVO.class);

            List<ProcessTaskRelation> relList = new ArrayList<>();
            if (dppEtlTaskNodeRelRespVOList != null && !dppEtlTaskNodeRelRespVOList.isEmpty()) {
                for (DppEtlTaskNodeRelRespVO srcRel : dppEtlTaskNodeRelRespVOList) {
                    // Call mapping sub-method
                    ProcessTaskRelation rel = toProcessTaskRelation(srcRel, definitionCopyVO);
                    relList.add(rel);
                }
                vo.setTaskRelationJson(JSON.toJSONString(relList));
            }
        }
    }

    /**
     * Convert DppEtlTaskNodeRelRespVO to ProcessTaskRelation, and remap pre/post code by definitionCopyVO
     */
    private static ProcessTaskRelation toProcessTaskRelation(DppEtlTaskNodeRelRespVO src,
                                                             Map<Long, Long> definitionCopyVO) {
        ProcessTaskRelation rel = new ProcessTaskRelation();

        // Only map these four fields as required
        // preTaskCode
        String preCodeStr = src.getPreNodeCode();
        Long preOld = JSONUtils.convertToLong(preCodeStr);
        if (preOld != null && definitionCopyVO != null) {
            Long preNew = definitionCopyVO.get(preOld);
            if (preNew != null) {
                preCodeStr = String.valueOf(preNew);
            }
        }
        rel.setPreTaskCode(preCodeStr);

        // preTaskVersion
        rel.setPreTaskVersion(safeToInt(src.getPreNodeVersion()));

        // postTaskCode
        String postCodeStr = src.getPostNodeCode();
        Long postOld = JSONUtils.convertToLong(postCodeStr);
        if (postOld != null && definitionCopyVO != null) {
            Long postNew = definitionCopyVO.get(postOld);
            if (postNew != null) {
                postCodeStr = String.valueOf(postNew);
            }
        }
        rel.setPostTaskCode(postCodeStr);

        // postTaskVersion
        rel.setPostTaskVersion(safeToInt(src.getPostNodeVersion()));

        return rel;
    }

    private static int safeToInt(Long v) {
        return v == null ? 1 : (int) Math.min(Math.max(v, 1L), Integer.MAX_VALUE);
    }

}
