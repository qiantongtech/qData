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
 * Handle task-related data and operations.
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
        // Validate the input and configuration.

        // Handle task-related data and operations.
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
            // Handle task-related data and operations.
            String type = dppEtlTaskDO.getType();
            // Handle task-related data and operations.
            DppEtlTaskExtDO taskExt = null;
            if (StringUtils.equals("1", type) && !StringUtils.equals("-1", dppEtlTaskDO.getStatus())) {
                // Retrieve the required data.
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
        // Handle task-related data and operations.
        return sum;
    }

    public List<DppEtlNodeRespVO> removeDuplicateById(List<DppEtlNodeRespVO> etlNodeLogRespVOList, String type) {
        // Implementation details.
        Map<Long, DppEtlNodeRespVO> map = etlNodeLogRespVOList.stream()
                .filter(itam -> itam != null && itam.getId() != null)
                .collect(Collectors.toMap(DppEtlNodeRespVO::getId, vo -> vo, (existing, replacement) -> existing));

        // Retrieve the required data.
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
                        // Implementation details.
                        (existing, replacement) -> existing
                ));
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
        // Handle task-related data and operations.
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
        // Handle task-related data and operations.
        String type = dppEtlTaskDO.getType();
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        // Handle task-related data and operations.
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

        // Handle task-related data and operations.
        if (StringUtils.equals("1", type)) {
            // Retrieve the required data.
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        if (ScheduleConstants.DOLPHINSCHEDULER.equals(dppEtlTaskDO.getScheduler())) {
            // Implementation details.
            if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
                if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                    throw new ServiceException("dpp.error.task.publish.fail", "发布或下线任务，失败！");
                }

                // Handle task-related data and operations.
                if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                    updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
                } else {
                    updateTaskStatus(dppEtlTaskDO.getId(), "-2");
                }
                return new HashMap<>();
            }

            // Implementation details.
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            String responseMsg = dsStatusRespDTO.getMsg();
            if (responseMsg.contains("SubWorkflowDefinition") && responseMsg.contains("is not online")) {
                throw new RuntimeException("存在未上线的子工作流，请先将所有子工作流上线");
            }
            if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                throw new ServiceException("发布任务失败！");
            }
        }

        // Handle task-related data and operations.
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

        // Handle task-related data and operations.
        if (StringUtils.equals(dppEtlSchedulerById.getStatus(), dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            return new HashMap<>();
        }

        if ((StringUtils.equals("0", dppEtlTaskDO.getStatus()) || StringUtils.equals("-2", dppEtlTaskDO.getStatus()))
                && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            throw new ServiceException("任务未上线，请先上线！");
        }

        // Handle task-related data and operations.
        String type = dppEtlTaskDO.getType();

        // Handle task-related data and operations.
        if (StringUtils.equals("1", type)) {
            // Retrieve the required data.
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        if (StringUtils.equals("4", type) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            wrapCustomNodeStatus(dppEtlTaskDO.getId(), "1");
        }

        // Implementation details.
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

            // Handle scheduling configuration and operations.
            DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setStatus(dppEtlNewNodeSaveReqVO.getSchedulerState());
            // Handle scheduling configuration and operations.
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
            return null;
        }

        DsSchedulerRespDTO dsSchedulerRespDTO;
        // Handle scheduling configuration and operations.
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
        // Handle scheduling configuration and operations.
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
        // Handle task-related data and operations.
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);
        String taskCode = getDsTaskGenCode(dppEtlNewNodeSaveReqVO, isUpdate);

        // Handle node-related data and operations.
        DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        String nodeCode = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));
        // Handle node-related data and operations.
        String nodeName = dppEtlNewNodeSaveReqVO.getName() + "-" + DateUtil.today();

        // Create the required record.
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // Implementation details.
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        // Handle task-related data and operations.
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        List<DsResource> resourceList = new ArrayList<>();
        // Implementation details.
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), new HashMap<>(), taskInfo, resourceList);

        // Handle node-related data and operations.
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(null, nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        // Handle node-related data and operations.
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(null, nodeCode);

        // Implementation details.
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // Handle task-related data and operations.
        }
        ProcessDefinition data = task.getData();

        // Handle task-related data and operations.
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

        // Handle scheduling configuration and operations.
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

        // Handle task-related data and operations.
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

        return taskSaveReqVO; // Create the required record.
    }

    /**
     * Handle Quartz and DataX task execution.
     */
    private DppEtlTaskSaveReqVO createLocalDataXEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);

        // Create the scheduler.
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(Long.valueOf(saveReqVOId));
        dppEtlTaskDO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        Long quartzId = dppTaskQuartzService.create(dppEtlTaskDO, "dppQuartzJob.dataIntegration(%sL)");

        // Handle node-related data and operations.
        String taskCode = String.valueOf(dppEtlTaskDO.getCode());
        String nodeName = dppEtlNewNodeSaveReqVO.getName() + "-" + DateUtil.today();

        // Handle scheduling configuration and operations.
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
            // Handle task-related data and operations.
            DppEtlSchedulerDO dppEtlSchedulerById = getDppEtlScheduler(dppEtlTaskSaveReqVO.getCode(), dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setTaskCode(taskCode);
            dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTaskSaveReqVO.getId());
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setQuartzId(quartzId);
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        } else {
            // Handle task-related data and operations.
            iDppEtlSchedulerService.createDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        }

        // Handle task-related data and operations.
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = TaskConverter.fromDppEtlTaskSaveReqVO(dppEtlTaskSaveReqVO);
        dppEtlTaskLogSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskLogSaveReqVO.setCode(taskCode);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        if (isUpdate) {
            // Handle DataX task configuration and execution.
            List<String> nodeCodeList = getLocalNodeCodeList(dppEtlNewNodeSaveReqVO);
            if (CollectionUtils.isNotEmpty(nodeCodeList)) {
                // Handle JDBC SQL execution.
                iDppEtlNodeService.removeOldDppEtlNode(nodeCodeList);
            }
            iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(taskCode);
        }

        // Handle task-related data and operations.
        dppEtlTaskExtService.createDppEtlTaskExt(DppEtlTaskExtSaveReqVO.builder()
                .taskId(taskId)
                .etlTaskCode(taskCode)
                .etlNodeCode(taskCode)
                .etlNodeName(nodeName)
                .build());

        // Handle DataX task configuration and execution.
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(dppEtlNewNodeSaveReqVO, 1);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // Handle node-related data and operations.
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = TaskConverter.convertToDppEtlNodeLogSaveReqVOList(dppEtlNodeSaveReqVOList);
        iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        // Handle task-related data and operations.
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeBatch, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // Handle node-related data and operations.
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);
        return dppEtlTaskSaveReqVO;
    }

    /**
     * Handle DataX task configuration and execution.
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
            // Implementation details.
            return releaseState;
        }
        // Handle DataX task configuration and execution.
        return "0";
    }

    /**
     * Handle node-related data and operations.
     * Handle DataX task configuration and execution.
     */
    private List<String> getLocalNodeCodeList(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);
        if (CollectionUtils.isEmpty(nodeList)) {
            // Handle node-related data and operations.
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

        // Handle task-related data and operations.
        DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        // Handle task-related data and operations.
        String taskCode = taskExt.getEtlTaskCode();
        // Handle node-related data and operations.
        String nodeCode = taskExt.getEtlNodeCode();
        // Handle node-related data and operations.
        String nodeName = taskExt.getEtlNodeName();

        // Create the required record.
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        // Implementation details.
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        // Handle task-related data and operations.
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", dppEtlTaskDO.getCode());
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setName(dppEtlNewNodeSaveReqVO.getName());
        dppEtlTaskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskSaveReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());


        // Handle node-related data and operations.
        List<DppEtlNodeSaveReqVO> newTaskDefinitionLogs = new ArrayList<>();
        List<DppEtlNodeSaveReqVO> updateTaskDefinitionLogs = new ArrayList<>();

        // Implementation details.
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        List<DppEtlNodeDO> dppEtlNodeDOList = new ArrayList<>();

        Map<String, DppEtlNodeSaveReqVO> nodeMap = nodeList.stream().collect(Collectors.toMap(DppEtlNodeSaveReqVO::getCode, node -> node));

        // Implementation details.
        for (DppEtlNodeSaveReqVO createReqVO : nodeList) {
            // Handle task-related data and operations.
            createReqVO.setType(createReqVO.getTaskType());// Handle node-related data and operations.
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());// Handle task-related data and operations.
            if (createReqVO.getVersion() == 0) {
                createReqVO.setVersion(1);
            }
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // Implementation details.
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // Implementation details.
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));

            DppEtlNodeLogDO nodeCodeAndVersion = iDppEtlNodeLogService.getByNodeCodeAndVersion(
                    createReqVO.getCode(), createReqVO.getVersion());
            if (nodeCodeAndVersion == null) {
                createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // Create the required record.
                createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // Handle task-related data and operations.
                createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // Create the required record.
                newTaskDefinitionLogs.add(createReqVO);
                continue;
            } else {
                // Implementation details.
                if (StringUtils.equals(TaskComponentTypeEnum.DB_READER.getCode(), String.valueOf(createReqVO.getTaskParams().get("type"))) &&
                        StringUtils.equals("2", String.valueOf(createReqVO.getTaskParams().get("readModeType")))) {
                    JSONObject idIncrementConfig = JSONObject.parseObject(String.valueOf(createReqVO.getTaskParams().get("idIncrementConfig")));
                    String incrementColumn = idIncrementConfig.getString("incrementColumn");
                    Integer incrementStart = idIncrementConfig.getInteger("incrementStart");
                    String cacheKey = TaskConverter.ETL_READER_ID_KEY + createReqVO.getCode() + ":" + incrementColumn;
                    // Delete the related record.
                    if (redisService.hasKey(cacheKey) && Integer.parseInt(redisService.get(cacheKey)) != incrementStart) {
                        redisService.delete(cacheKey);
                    }
                }
                createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // Update the related record.
                createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // Handle task-related data and operations.
                createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // Update the related record.
            }

            // Implementation details.
            if (createReqVO.equals(nodeCodeAndVersion)) {
                DppEtlNodeDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeDO.class);
                dppEtlNodeDOList.add(dictType);
                continue;
            }

            // Retrieve the required data.
            Integer version = iDppEtlNodeLogService.getMaxVersionByNodeCode(createReqVO.getCode());
            createReqVO.setVersion(version + 1);
            updateTaskDefinitionLogs.add(createReqVO);
        }

        // Handle node-related data and operations.
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

        // Handle node-related data and operations.
        if (CollectionUtils.isNotEmpty(newTaskDefinitionLogs)) {
            dppEtlNodeDOList.addAll(iDppEtlNodeService.createDppEtlNodeBatch(newTaskDefinitionLogs));
        }

        // Handle node-related data and operations.
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


        // Implementation details.
        List<DppEtlTaskNodeRelSaveReqVO> taskRelationList = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);

        boolean isChange = false;
        // Handle task-related data and operations.
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
            // Retrieve the required data.
            taskVersion = iDppEtlTaskLogService.queryMaxVersionByCode(dppEtlTaskDO.getCode());
            taskVersion += 1;
            dppEtlTaskSaveReqVO.setVersion(taskVersion);

            // Handle task-related data and operations.
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

        // Delete the related record.
        iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());

        // Implementation details.
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // Handle execution logging.
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            dppEtlTaskNodeRelLogSaveReqVO.setTaskVersion(taskVersion);
            dppEtlTaskNodeRelLogSaveReqVO.setId(null);
        }
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        List<DsResource> resourceList = new ArrayList<>();
        // Implementation details.
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), nodeMap, taskInfo, resourceList);

        // Handle node-related data and operations.
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(taskExt.getEtlNodeId(), nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        // Handle node-related data and operations.
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(taskExt.getEtlRelationId(), nodeCode);

        // Implementation details.
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);

        if (ScheduleConstants.DOLPHINSCHEDULER.equals(dppEtlTaskSaveReqVO.getScheduler())) {
            DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO, String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), taskCode);

            if (!task.getSuccess()) {
                throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // Handle task-related data and operations.
            }

            ProcessDefinition data = task.getData();

            // Update the related record.
            taskExt.setEtlTaskVersion(data.getVersion());
            taskExt.setEtlNodeVersion(data.getTaskDefinitionList().get(0).getVersion());
            taskExt.setEtlRelationId(data.getTaskRelationList().get(0).getId());
            dppEtlTaskExtService.updateById(taskExt);
        }
        return dppEtlTaskSaveReqVO; // Create the required record.
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

        // Handle task-related data and operations.
        if (StringUtils.equals(dppEtlTaskDO.getStatus(), dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-3", dppEtlTaskDO.getStatus()) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        // Handle task-related data and operations.
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

        // Implementation details.
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
            // Handle task-related data and operations.
            if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
            } else {
                updateTaskStatus(dppEtlTaskDO.getId(), "-2");
            }
        }

        // Implementation details.
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

        // Handle scheduling configuration and operations.
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);
        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerSaveReqVO.getDsId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("dpp.error.scheduler.online", "上线调度器，失败！");
        }

        // Handle scheduling configuration and operations.
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);

        // Handle task-related data and operations.
        if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        } else {
            updateTaskStatus(dppEtlTaskDO.getId(), "-3");
        }
    }

    /**
     * @param releaseState parameter value
     */
    private void wrapCustomNodeStatus(Long id, String releaseState) {
        DppEtlTaskRespVO dppEtlTaskById = this.getDppEtlTaskById(id);
        List<DppEtlNodeRespVO> taskDefinitionList = dppEtlTaskById.getTaskDefinitionList();

        // Handle task-related data and operations.
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

    // Handle task-related data and operations.
    private void updateTaskStatus(Long taskId, String releaseState) {
        DppEtlTaskSaveReqVO updateReqVO = new DppEtlTaskSaveReqVO();
        updateReqVO.setId(taskId);
        updateReqVO.setStatus(releaseState);
        this.updateDppEtlTask(updateReqVO);
    }

    // Handle scheduling configuration and operations.
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

    // Handle scheduling configuration and operations.
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
        // Handle task-related data and operations.
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        //
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // Handle task-related data and operations.
        }
        ProcessDefinition data = task.getData();

        // Implementation details.
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

        return dppEtlTaskSaveReqVO; // Create the required record.
    }

    /**
     * Handle task-related data and operations.
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    private DppEtlTaskSaveReqVO createProcessDefinitionQuartz(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        // Handle task-related data and operations.
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);
        // Create the scheduler.
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(Long.valueOf(saveReqVOId));
        dppEtlTaskDO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        Long quartzId = dppTaskQuartzService.create(dppEtlTaskDO, "dppQuartzJob.dataDevelopment(%sL)");

        // Handle scheduling configuration and operations.
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

        // Handle scheduling configuration and operations.
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

        // Handle task-related data and operations.
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = BeanUtils.toBean(dppEtlNewNodeSaveReqVO, DppEtlTaskLogSaveReqVO.class);
        dppEtlTaskLogSaveReqVO.setId(null);
        dppEtlTaskLogSaveReqVO.setDsId(0L);
        dppEtlTaskLogSaveReqVO.setQuartzId(quartzId);
        Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);

        // Handle node-related data and operations.
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        List<TaskDefinition> taskDefinitionList = BeanUtil.copyToList(list, TaskDefinition.class);
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(taskDefinitionList, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // Handle node-related data and operations.
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = BeanUtil.copyToList(dppEtlNodeSaveReqVOList, DppEtlNodeLogSaveReqVO.class);
        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = iDppEtlNodeLogService.createDppEtlNodeLogBatch(dppEtlNodeLogSaveReqVOS);

        Integer version = dppEtlNewNodeSaveReqVO.getVersion();
        String code = dppEtlNewNodeSaveReqVO.getCode();

        // Handle node-related data and operations.
        list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());
        List<ProcessTaskRelation> processTaskRelations = BeanUtil.copyToList(list, ProcessTaskRelation.class);
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, code, version);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // Handle node-related data and operations.
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO, code, version);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return dppEtlTaskSaveReqVO; // Create the required record.
    }

    @Override
    public DppEtlTaskSaveReqVO updateProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        if (StringUtils.equals("1", dppEtlTaskDO.getStatus()) || StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            throw new ServiceException("上线任务，不允许修改，请先下线！");
        }

        // Handle task-related data and operations.
        if (ScheduleConstants.QUARTZ.equals(dppEtlTaskDO.getScheduler())) {
            return updateProcessDefinitionQuartz(dppEtlNewNodeSaveReqVO, dppEtlTaskDO);
        }

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO
                , String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), String.valueOf(dppEtlTaskDO.getCode()));

        if (!task.getSuccess()) {
            throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // Handle task-related data and operations.
        }
        ProcessDefinition data = task.getData();

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        dppEtlTaskSaveReqVO.setId(dppEtlTaskDO.getId());
        this.updateDppEtlTask(dppEtlTaskSaveReqVO);


        // Delete the related record.
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());
        // Delete the related record.
        iDppEtlNodeService.removeOldDppEtlNode(TaskConverter.getPreAndPostNodeCodeList(dppEtlTaskNodeRelRespVOList));

        // Implementation details.
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(data, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);
        // Implementation details.
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

        // Delete the related record.
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());
        // Delete the related record.
        iDppEtlNodeService.removeOldDppEtlNode(TaskConverter.getPreAndPostNodeCodeList(dppEtlTaskNodeRelRespVOList));

        // Handle node-related data and operations.
        List<Map<String, Object>> list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskDefinitionList());
        List<TaskDefinition> taskDefinitionList = BeanUtil.copyToList(list, TaskDefinition.class);
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(taskDefinitionList, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);

        // Handle node-related data and operations.
        Integer version = dppEtlNewNodeSaveReqVO.getVersion();
        String code = dppEtlNewNodeSaveReqVO.getCode();
        list = JSONUtils.convertTaskDefinitionJson(dppEtlNewNodeSaveReqVO.getTaskRelationJson());
        List<ProcessTaskRelation> processTaskRelations = BeanUtil.copyToList(list, ProcessTaskRelation.class);
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(processTaskRelations, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO, code, version);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        // Handle task-related data and operations.
        DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO = BeanUtils.toBean(dppEtlNewNodeSaveReqVO, DppEtlTaskLogSaveReqVO.class);
        DppEtlTaskLogRespVO dppEtlTaskLogByRequest = this.getDppEtlTaskLogByRequest(dppEtlTaskLogSaveReqVO);
        if (dppEtlTaskLogByRequest == null) {
            Long dppEtlTaskLog = iDppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLog);
        } else {
            dppEtlTaskLogSaveReqVO.setId(dppEtlTaskLogByRequest.getId());
            iDppEtlTaskLogService.updateDppEtlTaskLog(dppEtlTaskLogSaveReqVO);
        }

        // Handle node-related data and operations.
        List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS = BeanUtil.copyToList(dppEtlNodeSaveReqVOList, DppEtlNodeLogSaveReqVO.class);
        List<DppEtlNodeLogDO> dppEtlNodeLogBatch = new ArrayList<>();
        for (DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO : dppEtlNodeLogSaveReqVOS) {
            DppEtlNodeLogDO dppEtlNodeLogRespVOByReqVO = this.getDppEtlNodeLogByCodeAndVersion(dppEtlNodeLogSaveReqVO);
            if (dppEtlNodeLogRespVOByReqVO == null) {
                dppEtlNodeLogRespVOByReqVO = iDppEtlNodeLogService.createDppEtlNodeLogNew(dppEtlNodeLogSaveReqVO);
            }
            dppEtlNodeLogBatch.add(dppEtlNodeLogRespVOByReqVO);
        }

        // Handle node-related data and operations.
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
        // Implementation details.
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
            // Handle task-related data and operations.
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
        // Handle Quartz scheduling operations.
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

        // Handle scheduling configuration and operations.
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskCode(bean.getCode());
        dppEtlSchedulerPageReqVO.setTaskId(bean.getId());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        dppEtlSchedulerById = dppEtlSchedulerById == null ? new DppEtlSchedulerDO() : dppEtlSchedulerById;
        bean.setCrontab(dppEtlSchedulerById.getCronExpression());
        bean.setSchedulerState(dppEtlSchedulerById.getStatus());

        // Retrieve the required data.
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
                    // Implementation details.
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
                            // Handle JDBC SQL execution.
                            if (!StringUtils.equals("2", jsonObject.getString("type"))) {
                                continue;
                            }
                            // Implementation details.
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

        // Implementation details.
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

        // Handle node-related data and operations.
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
     * Handle node-related data and operations.
     *
     * @param dppEtlNodeLogSaveReqVO parameter value
     * @return the operation result
     */
    public DppEtlNodeLogDO getDppEtlNodeLogByDsId(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // Create the required record.
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setDsId(dppEtlNodeLogSaveReqVO.getDsId());

        // Handle node-related data and operations.
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    public DppEtlNodeLogDO getDppEtlNodeLogByCodeAndVersion(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // Create the required record.
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setCode(dppEtlNodeLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlNodeLogSaveReqVO.getVersion());

        // Handle node-related data and operations.
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param dppEtlTaskNodeRelLogSaveReqVO parameter value
     * @return the operation result
     */
    public DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogByRequest(DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO) {
        // Create the required record.
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

        // Handle task-related data and operations.
        return null;
    }

    /**
     * Handle task-related data and operations.
     *
     * @param dppEtlTaskLogSaveReqVO parameter value
     * @return the operation result
     */
    public DppEtlTaskLogRespVO getDppEtlTaskLogByRequest(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO) {
        // Create the required record.
        DppEtlTaskLogPageReqVO reqVO = new DppEtlTaskLogPageReqVO();
        reqVO.setCode(dppEtlTaskLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlTaskLogSaveReqVO.getVersion());

        // Handle task-related data and operations.
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
// Handle task-related data and operations.
        }
        if (StringUtils.equals(dppEtlTaskDO.getScheduler(), ScheduleConstants.QUARTZ)) {
            // Handle Quartz and DataX task execution.
            return startLocalDataXTask(dppEtlTaskDO);
        }

        // Handle task-related data and operations.
        String type = dppEtlTaskDO.getType();

        // Handle task-related data and operations.
        if (StringUtils.equals("1", type)) {
            // Retrieve the required data.
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
     * Handle Quartz and DataX task execution.
     * Handle DataX task configuration and execution.
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
     * Handle task-related data and operations.
     *
     * @param id
     */
    @Override
    public void startDppEtlTaskDataIntegration(Long id) {
        // Handle JSON data for this operation.
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(id);
        if (dppEtlTaskDO == null) {
            throw new ServiceException("任务不存在，请刷新后重试！");
        }

        // Handle task-related data and operations.
        DppEtlTaskInstanceDO instance = dppEtlTaskDataIntegrationRunner.createLocalDataXTaskInstance(dppEtlTaskDO);
        StringBuilder taskLog = new StringBuilder();
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "********************************* Initialize DataX task context *******************************");
        LogUtils.appendLocalLogLine(taskLog, "***********************************************************************************************");
        LogUtils.appendLocalLogLine(taskLog, "Begin to initialize task");
        LogUtils.appendLocalLogLine(taskLog, "Set task startTime: " + instance.getStartTime().getTime());
        LogUtils.appendLocalLogLine(taskLog, "Set task appId: " + dppEtlTaskDO.getId() + "_" + instance.getId());

        // Handle task-related data and operations.
        dppEtlTaskDataIntegrationRunner.startDppEtlTaskDataIntegration(dppEtlTaskDO, instance, taskLog);
    }

    /**
     * Handle task-related data and operations.
     *
     * @param id parameter value
     */
    @Override
    public void startDppEtlTaskDataDevelopment(Long id) {
        // Handle JDBC SQL execution.
        DppEtlTaskDO task = dppEtlTaskMapper.selectById(id);
        // Handle task-related data and operations.
        if (task == null) {
            error("任务不存在，请刷新后重试！");
            return;
        }
        // Handle JDBC SQL execution.
        // Handle task-related data and operations.
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

        // Handle node-related data and operations.
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        root.setId(IdUtils.generateArtificialId());
        root.setTreeId(IdUtils.generateArtificialId());
        root.setLabel("数据开发");
        root.setChildren(new ArrayList<>());

        // Handle node-related data and operations.
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // Implementation details.
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // Handle node-related data and operations.
            catNodeMap.put(cat.getId(), node);
        }

        // Implementation details.
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // Implementation details.
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // Implementation details.
                catRoots.add(node);
            }
        }

        // Handle task-related data and operations.
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "3".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // Handle node-related data and operations.
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // Handle task-related data and operations.
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                // Handle task-related data and operations.
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

                // Handle task-related data and operations.
                categoryNode.getChildren().add(taskNode);
            }
        }
//
// Handle task-related data and operations.
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
// Handle task-related data and operations.
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // Implementation details.
        root.getChildren().addAll(catRoots);

        // Handle task-related data and operations.
        computeTaskCount(root);
        // Handle node-related data and operations.
        return root;
    }

    /**
     * Handle task-related data and operations.
     *
     * @param dppEtlTaskRespVOList parameter value
     * @param attTaskCatApiList parameter value
     * @return the operation result
     */
    public DppEtlTaskTreeRespVO buildTaskCatTree(List<DppEtlTaskRespVO> dppEtlTaskRespVOList,
                                                 List<AttTaskCatRespDTO> attTaskCatApiList) {
        // Handle node-related data and operations.
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        // Implementation details.
        root.setTreeId(IdUtils.generateArtificialId());
        root.setId(IdUtils.generateArtificialId());
        root.setLabel("数据集成");
        root.setChildren(new ArrayList<>());

        // Handle node-related data and operations.
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // Implementation details.
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // Handle node-related data and operations.
            catNodeMap.put(cat.getId(), node);
        }

        // Implementation details.
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // Implementation details.
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // Implementation details.
                catRoots.add(node);
            }
        }

        // Handle task-related data and operations.
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "1".equals(task.getType()) || "2".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // Handle node-related data and operations.
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // Handle task-related data and operations.
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                DppEtlTaskExtDO etlTaskExtDO = dppEtlTaskExtService.getByTaskId(task.getId());
                // Handle task-related data and operations.
                DppEtlTaskTreeRespVO taskNode = new DppEtlTaskTreeRespVO();
                // Implementation details.
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

                // Handle task-related data and operations.
                categoryNode.getChildren().add(taskNode);
            }
        }
//
// Handle task-related data and operations.
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
// Handle task-related data and operations.
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // Implementation details.
        root.getChildren().addAll(catRoots);

        // Handle task-related data and operations.
        computeTaskCount(root);

        // Handle node-related data and operations.
        return root;
    }


    /**
     * Handle task-related data and operations.
     * Handle task-related data and operations.
     * Handle task-related data and operations.
     *
     * @param node parameter value
     * @return the operation result
     */
    private static int computeTaskCount(DppEtlTaskTreeRespVO node) {
        int count = 0;
        // Handle task-related data and operations.
        if (node.getType() != null) {
            count = 1;
        }
        // Handle node-related data and operations.
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
    public DppEtlNewNodeSaveReqVO createEtlTaskFront(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {

        // Handle task-related data and operations.
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

        // Implementation details.
        createReqVO.setCode(taskCode);
        createReqVO.setStatus("-1");// Implementation details.
        createReqVO.setLocations("");
        createReqVO.setTimeout(0L);
        createReqVO.setDsId(0L);
        createReqVO.setQuartzId(0L);

        Long dppEtlTask = this.createDppEtlTask(createReqVO);

        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
        dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTask);
        dppEtlSchedulerSaveReqVO.setTaskCode(taskCode);
        // Retrieve the required data.
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);
        dppEtlSchedulerSaveReqVO.setStartTime(new Date());
        dppEtlSchedulerSaveReqVO.setEndTime(date);
        dppEtlSchedulerSaveReqVO.setTimezoneId("Asia/Shanghai"); // Implementation details.
        dppEtlSchedulerSaveReqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        dppEtlSchedulerSaveReqVO.setFailureStrategy("1");
        dppEtlSchedulerSaveReqVO.setStatus("0");
        // Handle Quartz scheduling operations.
        dppEtlSchedulerSaveReqVO.setTaskScheduler(dppEtlNewNodeSaveReqVO.getScheduler());
        dppEtlSchedulerSaveReqVO.setTaskActuator(dppEtlNewNodeSaveReqVO.getActuator());
        // Handle Quartz scheduling operations.
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
        // Handle task-related data and operations.
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
     * Handle task-related data and operations.
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
        // Handle task-related data and operations.
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

        // Handle task-related data and operations.
        if (StringUtils.equals("1", dppEtlTaskUpdateQueryRespVO.getType())) {
            // Retrieve the required data.
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(nodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskUpdateQueryRespVO.setCode(taskExt.getEtlTaskCode());
        }

        DsTaskSaveRespDTO task = dsEtlTaskService.batchCopy(dppEtlTaskUpdateQueryRespVO.getCode()
                , dppEtlTaskUpdateQueryRespVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("copy任务错误:" + task.getMsg().toString()); // Handle task-related data and operations.
        }
        ProcessDefinition data = task.getData();

        // Handle task-related data and operations.
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

        // Handle task-related data and operations.
        DppEtlTaskSaveReqVO taskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        taskSaveReqVO.setCode(taskCode);
        taskSaveReqVO.setDraftJson(src.getDraftJson());

        for (Map<String, Object> location : locations) {
            Long codeold = MapUtils.getLong(location, "taskCode");
            // Handle node-related data and operations.
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
            String codeNew = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));

            definitionCopyVO.put(codeold, JSONUtils.convertToLong(codeNew));
            location.put("taskCode", codeNew);
        }

        // Handle node-related data and operations.
        remapTaskCodes(dppEtlNewNodeSaveReqVO, definitionCopyVO);

        taskSaveReqVO.setLocations(JSONUtils.toJson(locations));
        Long dppEtlTask = this.createDppEtlTask(taskSaveReqVO);
        taskSaveReqVO.setId(dppEtlTask);

        // Handle task-related data and operations.
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", name);

        // Handle scheduling configuration and operations.
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

        // Handle task-related data and operations.
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

        return taskSaveReqVO; // Create the required record.
    }


    public static void remapTaskCodes(DppEtlNewNodeSaveReqVO vo, Map<Long, Long> definitionCopyVO) {
        if (vo == null || definitionCopyVO == null || definitionCopyVO.isEmpty()) {
            return;
        }

        // Implementation details.
        String taskDefJson = vo.getTaskDefinitionList();
        if (taskDefJson != null && !taskDefJson.isEmpty()) {
            List<DppEtlNodeSaveReqVO> nodeList =
                    JSON.parseArray(taskDefJson, DppEtlNodeSaveReqVO.class);

            if (nodeList != null && !nodeList.isEmpty()) {
                for (DppEtlNodeSaveReqVO node : nodeList) {
                    node.setId(null);
                    // Implementation details.
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

        // Implementation details.
        String relJson = vo.getTaskRelationJson();
        if (relJson != null && !relJson.isEmpty()) {
            List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList =
                    JSON.parseArray(relJson, DppEtlTaskNodeRelRespVO.class);

            List<ProcessTaskRelation> relList = new ArrayList<>();
            if (dppEtlTaskNodeRelRespVOList != null && !dppEtlTaskNodeRelRespVOList.isEmpty()) {
                for (DppEtlTaskNodeRelRespVO srcRel : dppEtlTaskNodeRelRespVOList) {
                    // Implementation details.
                    ProcessTaskRelation rel = toProcessTaskRelation(srcRel, definitionCopyVO);
                    relList.add(rel);
                }
                vo.setTaskRelationJson(JSON.toJSONString(relList));
            }
        }
    }

    /**
     * Implementation details.
     */
    private static ProcessTaskRelation toProcessTaskRelation(DppEtlTaskNodeRelRespVO src,
                                                             Map<Long, Long> definitionCopyVO) {
        ProcessTaskRelation rel = new ProcessTaskRelation();

        // Implementation details.
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
