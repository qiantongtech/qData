package tech.qiantong.qdata.module.dpp.service.etl.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessTaskRelation;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.TaskCatEnum;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
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
import tech.qiantong.qdata.module.dpp.service.etl.*;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.module.dpp.utils.model.DsResource;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * 数据集成任务Service业务层处理
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
        PageResult<DppEtlTaskDO> dppEtlTaskDOPageResult = dppEtlTaskMapper.selectPage(dppEtlTask);
        PageResult<DppEtlTaskRespVO> dppEtlTaskRespVOPageResult = BeanUtils.toBean(dppEtlTaskDOPageResult, DppEtlTaskRespVO.class);
        List<DppEtlTaskRespVO> dppEtlTaskDOList = (List<DppEtlTaskRespVO>) dppEtlTaskRespVOPageResult.getRows();
        if (CollectionUtils.isEmpty(dppEtlTaskDOList)) {
            return dppEtlTaskRespVOPageResult;
        }
        if ("3".equals(dppEtlTask.getType())) {
            for (DppEtlTaskRespVO dppEtlTaskDO : dppEtlTaskDOList) {
                if (StringUtils.equals("-1", dppEtlTaskDO.getStatus())) {
                    String draftJson = dppEtlTaskDO.getDraftJson();
                    if (StringUtils.isNotEmpty(draftJson)) {
                        JSONObject draftJsonObj = JSONUtil.parseObj(draftJson);
                        dppEtlTaskDO.setDatasourceType(draftJsonObj.getStr("typaCode"));
                    }
                    continue;
                }
                List<DppEtlNodeRespVO> etlNodeLogRespVOList = iDppEtlNodeService.listNodeByTaskId(dppEtlTaskDO.getId());
                if (CollectionUtils.isNotEmpty(etlNodeLogRespVOList)) {
                    DppEtlNodeRespVO dppEtlNodeDO = CollectionUtils.isNotEmpty(etlNodeLogRespVOList) && etlNodeLogRespVOList.size() >= 1
                            ? etlNodeLogRespVOList.get(0) : null;
                    if (dppEtlNodeDO != null) {
                        JSONObject parametersJsonObj = JSONUtil.parseObj(dppEtlNodeDO.getParameters());
                        dppEtlTaskDO.setDatasourceType(parametersJsonObj.getStr("typaCode"));
                    }
                }
            }
        }
        return dppEtlTaskRespVOPageResult;
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
        // 相关校验

        // 更新数据集成任务
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
            //1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
            String type = dppEtlTaskDO.getType();
            //判断是否是离线任务 是需要获取扩展信息的任务编码进行接口调用
            DppEtlTaskExtDO taskExt = null;
            if (StringUtils.equals("1", type) && !StringUtils.equals("-1", dppEtlTaskDO.getStatus())) {
                //获取扩展信息
                taskExt = dppEtlTaskExtService.getByTaskId(dppEtlTaskDO.getId());
                if (taskExt == null) {
                    throw new ServiceException("暂无数据！");
                }
                dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
            }
            if (StringUtils.equals("1", dppEtlTaskDO.getStatus())) {
                throw new ServiceException("上线任务，不允删除，请先下线！");
            }
            if (dppEtlTaskDO.getDsId() != null || (taskExt != null && StringUtils.isNotEmpty(taskExt.getEtlTaskCode()))) {
                dsEtlTaskService.deleteTask(dppEtlTaskDO.getProjectCode(), dppEtlTaskDO.getCode());
                sum += dppEtlTaskMapper.deleteById(id);
            } else {
                sum += dppEtlTaskMapper.deleteById(id);
            }
        }
        // 批量删除数据集成任务
        return sum;
    }

    public List<DppEtlNodeRespVO> removeDuplicateById(List<DppEtlNodeRespVO> etlNodeLogRespVOList, String type) {
        // 使用 LinkedHashMap 保证去重后保持原顺序
        Map<Long, DppEtlNodeRespVO> map = etlNodeLogRespVOList.stream()
                .filter(itam -> itam != null && itam.getId() != null)
                .collect(Collectors.toMap(DppEtlNodeRespVO::getId, vo -> vo, (existing, replacement) -> existing));

        // 获取去重后的 List
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成任务数据
     *
     * @param importExcelList 数据集成任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDppEtlTask(List<DppEtlTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + dppEtlTaskId + " 的数据集成任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEtlTaskId + " 的数据集成任务记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEtlTaskDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskId);
                    DppEtlTaskDO existingDppEtlTask = dppEtlTaskMapper.selectOne(queryWrapper);
                    if (existingDppEtlTask == null) {
                        dppEtlTaskMapper.insert(dppEtlTaskDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEtlTaskId + " 的数据集成任务记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEtlTaskId + " 的数据集成任务记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
    }


    @Override
    public Long getNodeUniqueKey(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        return dsNodeGenCodeRespDTO.getData().get(0);
    }

    @Override
    public List<DppEtlTaskRespVO> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask) {
        DppEtlTaskRespVO dppEtlTaskById = this.getDppEtlTaskById(dppEtlTask.getId());
        List<DppEtlNodeRespVO> taskDefinitionList = dppEtlTaskById.getTaskDefinitionList();

        List<DppEtlTaskRespVO> dppEtlNewNodeSaveReqVOS = new ArrayList<>();
        //循环获取自定义任务
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
        //1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
        String type = dppEtlTaskDO.getType();
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        // 若任务状态未变化，则直接返回
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

        //判断是否是离线任务 是需要获取扩展信息的任务编码进行接口调用
        if (StringUtils.equals("1", type)) {
            //获取扩展信息
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        // 下线操作
        if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                throw new ServiceException("发布或下线任务，失败！");
            }

            // 更新任务状态
            if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
            } else {
                updateTaskStatus(dppEtlTaskDO.getId(), "-2");
            }
            return new HashMap<>();
        }

        // 上线操作
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
        String responseMsg = dsStatusRespDTO.getMsg();
        if (responseMsg.contains("SubWorkflowDefinition") && responseMsg.contains("is not online")) {
            throw new RuntimeException("存在未上线的子工作流，请先将所有子工作流上线");
        }
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("发布任务失败！");
        }

        // 更新任务状态
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

        // 若任务状态未变化，则直接返回
        if (StringUtils.equals(dppEtlSchedulerById.getStatus(), dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            return new HashMap<>();
        }

        if ((StringUtils.equals("0", dppEtlTaskDO.getStatus()) || StringUtils.equals("-2", dppEtlTaskDO.getStatus()))
                && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            throw new ServiceException("任务未上线，请先上线！");
        }

        //1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
        String type = dppEtlTaskDO.getType();

        //判断是否是离线任务 是需要获取扩展信息的任务编码进行接口调用
        if (StringUtils.equals("1", type)) {
            //获取扩展信息
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }

        if (StringUtils.equals("4", type) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            wrapCustomNodeStatus(dppEtlTaskDO.getId(), "1");
        }

        // 下线操作
        if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getSchedulerState())) {
            if (dppEtlSchedulerById.getDsId() != null && dppEtlSchedulerById.getDsId() > 0) {
                DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.offlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerById.getDsId());
                if (!dsStatusRespDTO1.getData()) {
                    throw new ServiceException("下线调度器，失败！");
                }
            }

            // 更新调度器并上线
            DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
            dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
            dppEtlSchedulerSaveReqVO.setStatus(dppEtlNewNodeSaveReqVO.getSchedulerState());
            // 更新调度器
            iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
            return null;
        }

        DsSchedulerRespDTO dsSchedulerRespDTO;
        if (dppEtlSchedulerById.getDsId() == null || dppEtlSchedulerById.getDsId() < 1) {
            dsSchedulerRespDTO = createOrUpdateScheduler(dppEtlSchedulerById, dppEtlTaskDO);
        } else {
            dsSchedulerRespDTO = updateExistingScheduler(dppEtlSchedulerById, dppEtlTaskDO);
        }

        // 更新调度器并上线
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);
        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());
        dppEtlSchedulerSaveReqVO.setStatus(dppEtlNewNodeSaveReqVO.getSchedulerState());

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerSaveReqVO.getDsId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("上线调度器，失败！");
        }

        // 更新调度器
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);
        return null;
    }

    /**
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    @Override
    public DppEtlTaskSaveReqVO createEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        //兼容先创建任务，再丰满信息
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);
        String taskCode = getDsTaskGenCode(dppEtlNewNodeSaveReqVO, isUpdate);

        //生成节点编码
        DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        String nodeCode = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));
        //生成节点名称
        String nodeName = dppEtlNewNodeSaveReqVO.getName() + "-" + DateUtil.today();

        //创建返回实体
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        //封装基础参数
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        //构建任务任务信息
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        List<DsResource> resourceList = new ArrayList<>();
        //构建etl程序所需数据
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), new HashMap<>(), taskInfo, resourceList);

        //封装节点信息 DATAX、SPARK
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(null, nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        //节点关系
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(null, nodeCode);

        //坐标信息
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();

        // 转换任务保存请求对象
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

        // 调度器对象构建
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

        //创建etl任务扩展数据
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

        return taskSaveReqVO; // 返回创建结果
    }


    @Override
    public DppEtlTaskSaveReqVO updateEtlTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        if (StringUtils.equals("1", dppEtlTaskDO.getStatus()) || StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            throw new ServiceException("上线任务，不允许修改，请先下线！");
        }

        //创建etl任务扩展数据
        DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(dppEtlNewNodeSaveReqVO.getId()));

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        //生成任务编码
        String taskCode = taskExt.getEtlTaskCode();
        //生成节点编码
        String nodeCode = taskExt.getEtlNodeCode();
        //生成节点名称
        String nodeName = taskExt.getEtlNodeName();

        //创建返回实体
        DsTaskSaveReqDTO dsTaskSaveReqDTO = new DsTaskSaveReqDTO();
        //封装基础参数
        dsTaskSaveReqDTO.setName(dppEtlNewNodeSaveReqVO.getName());
        dsTaskSaveReqDTO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());
        dsTaskSaveReqDTO.setExecutionType(dppEtlNewNodeSaveReqVO.getExecutionType());

        //构建任务任务信息
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", dppEtlTaskDO.getCode());
        taskInfo.put("name", dppEtlNewNodeSaveReqVO.getName());

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = BeanUtils.toBean(dppEtlTaskDO, DppEtlTaskSaveReqVO.class);
        dppEtlTaskSaveReqVO.setName(dppEtlNewNodeSaveReqVO.getName());
        dppEtlTaskSaveReqVO.setLocations(JSON.toJSONString(dppEtlNewNodeSaveReqVO.getLocations()));
        dppEtlTaskSaveReqVO.setDescription(dppEtlNewNodeSaveReqVO.getDescription());


        //处理节点数据
        List<DppEtlNodeSaveReqVO> newTaskDefinitionLogs = new ArrayList<>();
        List<DppEtlNodeSaveReqVO> updateTaskDefinitionLogs = new ArrayList<>();

        //取出入参数的信息
        List<DppEtlNodeSaveReqVO> nodeList = JSON.parseArray(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), DppEtlNodeSaveReqVO.class);

        List<DppEtlNodeDO> dppEtlNodeDOList = new ArrayList<>();

        Map<String, DppEtlNodeSaveReqVO> nodeMap = nodeList.stream().collect(Collectors.toMap(DppEtlNodeSaveReqVO::getCode, node -> node));

        // 遍历 ProcessDefinition 中的 taskDefinitionList
        for (DppEtlNodeSaveReqVO createReqVO : nodeList) {
            // 1. 任务相关信息
            createReqVO.setType(createReqVO.getTaskType());//节点类型
            createReqVO.setTaskType(dppEtlNewNodeSaveReqVO.getType());//任务类型
            if (createReqVO.getVersion() == 0) {
                createReqVO.setVersion(1);
            }
            createReqVO.setProjectId(dppEtlNewNodeSaveReqVO.getProjectId()); // 项目ID
            createReqVO.setProjectCode(String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode())); // 项目编码
            createReqVO.setParameters(JSON.toJSONString(createReqVO.getTaskParams()));

            DppEtlNodeLogDO nodeCodeAndVersion = iDppEtlNodeLogService.getByNodeCodeAndVersion(
                    createReqVO.getCode(), createReqVO.getVersion());
            if (nodeCodeAndVersion == null) {
                createReqVO.setCreatorId(dppEtlNewNodeSaveReqVO.getCreatorId()); // 假设项目ID为创建者ID（根据需求调整）
                createReqVO.setCreateBy(dppEtlNewNodeSaveReqVO.getCreateBy()); // 假设任务名称为创建者（根据需求调整）
                createReqVO.setCreateTime(dppEtlNewNodeSaveReqVO.getCreateTime()); // 设置当前时间为创建时间
                newTaskDefinitionLogs.add(createReqVO);
                continue;
            } else {
                createReqVO.setUpdatorId(dppEtlNewNodeSaveReqVO.getUpdatorId()); // 假设项目ID为更新者ID（根据需求调整）
                createReqVO.setUpdateBy(dppEtlNewNodeSaveReqVO.getUpdateBy()); // 假设任务名称为更新者（根据需求调整）
                createReqVO.setUpdateTime(dppEtlNewNodeSaveReqVO.getUpdateTime()); // 设置当前时间为更新时间
            }

            //判断数据是否相同
            if (createReqVO.equals(nodeCodeAndVersion)) {
                DppEtlNodeDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeDO.class);
                dppEtlNodeDOList.add(dictType);
                continue;
            }

            //获取当前最大的版本
            Integer version = iDppEtlNodeLogService.getMaxVersionByNodeCode(createReqVO.getCode());
            createReqVO.setVersion(version + 1);
            updateTaskDefinitionLogs.add(createReqVO);
        }

        //新增节点日志
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

        //新增节点数据
        if (CollectionUtils.isNotEmpty(newTaskDefinitionLogs)) {
            dppEtlNodeDOList.addAll(iDppEtlNodeService.createDppEtlNodeBatch(newTaskDefinitionLogs));
        }

        //修改节点数据
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


        //处理关系数据
        List<DppEtlTaskNodeRelSaveReqVO> taskRelationList = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);

        boolean isChange = false;
        //根据任务编码及版本获取关系日志数据
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
            //获取最大版本
            taskVersion = iDppEtlTaskLogService.queryMaxVersionByCode(dppEtlTaskDO.getCode());
            taskVersion += 1;
            dppEtlTaskSaveReqVO.setVersion(taskVersion);

            //新增或更新任务日志
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

        //rel 先删除，再新增
        iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());

        //新增关系
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(dppEtlNodeDOList, dppEtlNewNodeSaveReqVO, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        //新增关系日志
        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(dppEtlTaskNodeRelSaveReqVOS);
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            dppEtlTaskNodeRelLogSaveReqVO.setTaskVersion(taskVersion);
            dppEtlTaskNodeRelLogSaveReqVO.setId(null);
        }
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        List<DsResource> resourceList = new ArrayList<>();
        //构建etl程序所需数据
        Map<String, Object> mainArgs = TaskConverter.buildEtlTaskParams(dppEtlNewNodeSaveReqVO.getTaskDefinitionList(), nodeMap, taskInfo, resourceList);

        //封装节点信息 DATAX、SPARK
        String taskDefinition = TaskConverter.buildEtlTaskDefinitionJson(taskExt.getEtlNodeId(), nodeName, nodeCode, 0, mainArgs, dppEtlNewNodeSaveReqVO.getDraftJson());

        //节点关系
        String taskRelation = TaskConverter.buildEtlTaskRelationJson(taskExt.getEtlRelationId(), nodeCode);

        //坐标信息
        String locations = TaskConverter.buildEtlTaskLocationsJson(dppEtlNewNodeSaveReqVO.getLocations(), nodeCode);

        dsTaskSaveReqDTO.setTaskDefinitionJson(taskDefinition);
        dsTaskSaveReqDTO.setTaskRelationJson(taskRelation);
        dsTaskSaveReqDTO.setLocations(locations);

        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO, String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), taskCode);

        if (!task.getSuccess()) {
            throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // 抛出任务定义创建错误的异常
        }

        ProcessDefinition data = task.getData();

        //更新扩展数据
        taskExt.setEtlTaskVersion(data.getVersion());
        taskExt.setEtlNodeVersion(data.getTaskDefinitionList().get(0).getVersion());
        taskExt.setEtlRelationId(data.getTaskRelationList().get(0).getId());
        dppEtlTaskExtService.updateById(taskExt);
        return dppEtlTaskSaveReqVO; // 返回创建结果
    }

    @Override
    public Map<String, Object> updateReleaseTask(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskId(dppEtlTaskDO.getId());
        dppEtlSchedulerPageReqVO.setTaskCode(dppEtlTaskDO.getCode());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);

        if (dppEtlSchedulerById == null) {
            throw new ServiceException("任务模版错误，未查询到调度信息！");
        }

        // 若任务状态未变化，则直接返回
        if (StringUtils.equals(dppEtlTaskDO.getStatus(), dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        if (StringUtils.equals("-3", dppEtlTaskDO.getStatus()) && StringUtils.equals("1", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            return new HashMap<>();
        }

        //1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
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

        // 下线操作
        if (StringUtils.equals("0", dppEtlNewNodeSaveReqVO.getReleaseState())) {
            if (dppEtlSchedulerById.getDsId() != null && dppEtlSchedulerById.getDsId() > 0) {
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
                if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
                    throw new ServiceException("发布或下线任务，失败！");
                }
                DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.offlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerById.getDsId());
                if (!dsStatusRespDTO1.getData()) {
                    throw new ServiceException("下线调度器，失败！");
                }
            }
            // 更新任务状态
            if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
                updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
            } else {
                updateTaskStatus(dppEtlTaskDO.getId(), "-2");
            }
        }

        // 上线操作
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

        // 更新调度器并上线
        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = TaskConverter.convertToDppEtlSchedulerSaveReqVO(dsSchedulerRespDTO, dppEtlTaskDO);
        dppEtlSchedulerSaveReqVO.setId(dppEtlSchedulerById.getId());

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(dppEtlTaskDO.getProjectCode(), dppEtlSchedulerSaveReqVO.getDsId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("上线调度器，失败！");
        }

        // 更新调度器
        iDppEtlSchedulerService.updateDppEtlScheduler(dppEtlSchedulerSaveReqVO);

        // 更新任务状态
        if (!StringUtils.equals("-2", dppEtlTaskDO.getStatus()) && !StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            updateTaskStatus(dppEtlTaskDO.getId(), dppEtlNewNodeSaveReqVO.getReleaseState());
        } else {
            updateTaskStatus(dppEtlTaskDO.getId(), "-3");
        }
    }

    /**
     * @param releaseState //上下限  0:未上线，1:已上线
     */
    private void wrapCustomNodeStatus(Long id, String releaseState) {
        DppEtlTaskRespVO dppEtlTaskById = this.getDppEtlTaskById(id);
        List<DppEtlNodeRespVO> taskDefinitionList = dppEtlTaskById.getTaskDefinitionList();

        //循环获取自定义任务
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

    // 更新任务状态
    private void updateTaskStatus(Long taskId, String releaseState) {
        DppEtlTaskSaveReqVO updateReqVO = new DppEtlTaskSaveReqVO();
        updateReqVO.setId(taskId);
        updateReqVO.setStatus(releaseState);
        this.updateDppEtlTask(updateReqVO);
    }

    // 创建或更新调度器
    private DsSchedulerRespDTO createOrUpdateScheduler(DppEtlSchedulerDO dppEtlSchedulerById, DppEtlTaskDO dppEtlTaskDO) {
        DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = TaskConverter.createSchedulerRequest(dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(dppEtlTaskDO.getProjectCode()));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            if (byTaskCode == null || !byTaskCode.getSuccess()) {
                throw new ServiceException("创建调度器，失败！");
            }
            Schedule data = byTaskCode.getData();
            DsSchedulerUpdateReqDTO schedulerUpdateRequest = TaskConverter.createSchedulerUpdateRequest(data.getId(), dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
            dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
            if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
                throw new ServiceException("更新调度器，失败！");
            }
        }
        return dsSchedulerRespDTO;
    }

    // 更新现有调度器
    private DsSchedulerRespDTO updateExistingScheduler(DppEtlSchedulerDO dppEtlSchedulerById, DppEtlTaskDO dppEtlTaskDO) {
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = TaskConverter.createSchedulerUpdateRequest(dppEtlSchedulerById.getDsId(), dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(dppEtlTaskDO.getProjectCode()));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(dppEtlTaskDO.getProjectCode()), dppEtlTaskDO.getCode());
            if (byTaskCode == null || !byTaskCode.getSuccess()) {
                DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = TaskConverter.createSchedulerRequest(dppEtlSchedulerById.getCronExpression(), dppEtlTaskDO.getCode());
                DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(dppEtlTaskDO.getProjectCode()));
                if (saveScheduler == null || !saveScheduler.getSuccess()) {
                    throw new ServiceException("创建调度器，失败！");
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
        //兼容先创建任务，再丰满信息
        String saveReqVOId = dppEtlNewNodeSaveReqVO.getId();
        boolean isUpdate = StringUtils.isNotEmpty(saveReqVOId);

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        //
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO, dppEtlNewNodeSaveReqVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("创建任务错误:" + task.getMsg().toString()); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();

        //存
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

        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(data, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO);
        iDppEtlTaskNodeRelService.createDppEtlTaskNodeRelBatch(dppEtlTaskNodeRelSaveReqVOS);

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(data, dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO);
        iDppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLogBatch(dppEtlTaskNodeRelLogSaveReqVOS);

        return dppEtlTaskSaveReqVO; // 返回创建结果
    }


    @Override
    public DppEtlTaskSaveReqVO updateProcessDefinition(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskDO dppEtlTaskDO = dppEtlTaskMapper.selectById(dppEtlNewNodeSaveReqVO.getId());
        if (StringUtils.equals("1", dppEtlTaskDO.getStatus()) || StringUtils.equals("-3", dppEtlTaskDO.getStatus())) {
            throw new ServiceException("上线任务，不允许修改，请先下线！");
        }

        DsTaskSaveReqDTO dsTaskSaveReqDTO = TaskConverter.buildDsTaskSaveReq(dppEtlNewNodeSaveReqVO);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO
                , String.valueOf(dppEtlNewNodeSaveReqVO.getProjectCode()), String.valueOf(dppEtlTaskDO.getCode()));

        if (!task.getSuccess()) {
            throw new ServiceException("修改任务错误:" + task.getMsg().toString()); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();

        this.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);

        DppEtlTaskSaveReqVO dppEtlTaskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        dppEtlTaskSaveReqVO.setId(dppEtlTaskDO.getId());
        this.updateDppEtlTask(dppEtlTaskSaveReqVO);


        //rel 先删除，再新增
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.removeOldDppEtlTaskNodeRel(dppEtlTaskDO.getCode());
        //node 先删除，再新增
        iDppEtlNodeService.removeOldDppEtlNode(TaskConverter.getPreAndPostNodeCodeList(dppEtlTaskNodeRelRespVOList));

        //新增
        List<DppEtlNodeSaveReqVO> dppEtlNodeSaveReqVOList = TaskConverter.convertToDppEtlNodeSaveReqVOList(data, dppEtlNewNodeSaveReqVO);
        List<DppEtlNodeDO> dppEtlNodeBatch = iDppEtlNodeService.createDppEtlNodeBatch(dppEtlNodeSaveReqVOList);
        //新增
        List<DppEtlTaskNodeRelSaveReqVO> dppEtlTaskNodeRelSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelSaveReqVOList(data, dppEtlNewNodeSaveReqVO, dppEtlNodeBatch, dppEtlTaskSaveReqVO);
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

        List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS = TaskConverter.convertToDppEtlTaskNodeRelLogSaveReqVOList(data, dppEtlNewNodeSaveReqVO, dppEtlNodeLogBatch, dppEtlTaskLogSaveReqVO);
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
        //补偿
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
            //     * 修改调度器 (只有任务发布了才能调用该接口)
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

        //获取调度信息
        DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO = new DppEtlSchedulerPageReqVO();
        dppEtlSchedulerPageReqVO.setTaskCode(bean.getCode());
        dppEtlSchedulerPageReqVO.setTaskId(bean.getId());
        DppEtlSchedulerDO dppEtlSchedulerById = iDppEtlSchedulerService.getDppEtlSchedulerById(dppEtlSchedulerPageReqVO);
        dppEtlSchedulerById = dppEtlSchedulerById == null ? new DppEtlSchedulerDO() : dppEtlSchedulerById;
        bean.setCrontab(dppEtlSchedulerById.getCronExpression());
        bean.setSchedulerState(dppEtlSchedulerById.getStatus());

        //获取最后一次执行的实例
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceService.getLastTaskInstanceByTaskCode(bean.getCode());
        if (dppEtlTaskInstanceDO != null) {
            bean.setLastExecuteTime(dppEtlTaskInstanceDO.getStartTime());
            bean.setLastExecuteStatus(dppEtlTaskInstanceDO.getStatus());
        }
        List<DppEtlNodeRespVO> etlNodeLogRespVOList = this.getNodeRespListByTaskNodeRelList(dppEtlTaskNodeRelRespVOList);

        bean.setTaskDefinitionList(removeDuplicateById(etlNodeLogRespVOList, type));
        bean.createTaskConfig();
        return bean;
    }


    public List<DppEtlNodeRespVO> getNodeRespListByTaskNodeRelList(List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList) {

        if (CollectionUtils.isEmpty(dppEtlTaskNodeRelRespVOList)) {
            return new ArrayList<>();
        }

        // 收集所有 preNodeCode 和 postNodeCode
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

        // 查询节点信息
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
     * 创建请求对象并根据 dsId 获取节点日志
     *
     * @param dppEtlNodeLogSaveReqVO 节点日志保存请求对象
     * @return DppEtlNodeLogDO 返回节点日志信息
     */
    public DppEtlNodeLogDO getDppEtlNodeLogByDsId(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // 创建请求对象
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setDsId(dppEtlNodeLogSaveReqVO.getDsId());

        // 调用服务方法获取节点日志信息
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    public DppEtlNodeLogDO getDppEtlNodeLogByCodeAndVersion(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {
        // 创建请求对象
        DppEtlNodeLogPageReqVO reqVO = new DppEtlNodeLogPageReqVO();
        reqVO.setCode(dppEtlNodeLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlNodeLogSaveReqVO.getVersion());

        // 调用服务方法获取节点日志信息
        return iDppEtlNodeLogService.getDppEtlNodeLogRespVOByReqVO(reqVO);
    }

    /**
     * 创建请求对象并根据任务节点日志信息获取日志
     *
     * @param dppEtlTaskNodeRelLogSaveReqVO 任务节点日志保存请求对象
     * @return DppEtlTaskNodeRelLogRespVO 返回任务节点日志响应对象
     */
    public DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogByRequest(DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO) {
        // 创建请求对象
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

        // 调用服务方法获取任务节点日志信息
        return null;
    }

    /**
     * 创建请求对象并根据任务日志信息获取任务
     *
     * @param dppEtlTaskLogSaveReqVO 任务日志保存请求对象
     * @return DppEtlTaskLogRespVO 返回任务日志响应对象
     */
    public DppEtlTaskLogRespVO getDppEtlTaskLogByRequest(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO) {
        // 创建请求对象
        DppEtlTaskLogPageReqVO reqVO = new DppEtlTaskLogPageReqVO();
        reqVO.setCode(dppEtlTaskLogSaveReqVO.getCode());
        reqVO.setVersion(dppEtlTaskLogSaveReqVO.getVersion());

        // 调用服务方法获取任务日志信息
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
//            return error("任务状态错误，请刷新后重试！");
        }
        //1：离线任务 2：实时任务 3：数据开发任务 4：作业任务
        String type = dppEtlTaskDO.getType();

        //判断是否是离线任务 是需要获取扩展信息的任务编码进行接口调用
        if (StringUtils.equals("1", type)) {
            //获取扩展信息
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(dppEtlTaskDO.getId());
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskDO.setCode(taskExt.getEtlTaskCode());
        }


        DsStartTaskReqDTO dsStartTaskReqDTO = TaskConverter.createDsStartTaskReqDTO(dppEtlTaskDO.getCode());

        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(dsStartTaskReqDTO, dppEtlTaskDO.getProjectCode());

        return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
    }


    @Override
    public List<DppEtlTaskTreeRespVO> getDppEtlTaskListTree(DppEtlTaskPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskDO.class)
                .ne(DppEtlTaskDO::getStatus, "-2")
                .ne(DppEtlTaskDO::getStatus, "-3")
                .eq(reqVO.getProjectId() != null, DppEtlTaskDO::getProjectId, reqVO.getProjectId())
                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()), DppEtlTaskDO::getProjectCode, reqVO.getProjectCode())
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

        // 1. 构造顶级目录节点
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        root.setId(IdUtils.generateArtificialId());
        root.setTreeId(IdUtils.generateArtificialId());
        root.setLabel("数据开发");
        root.setChildren(new ArrayList<>());

        // 2. 整理类目列表：先将每个 AttTaskCatRespDTO 转换为 DppEtlTaskTreeRespVO 节点，并放入 map（key 为类目 id）
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // 重新生成一个 id
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // 类目节点的 dppEtlTaskCount 后续会赋值
            catNodeMap.put(cat.getId(), node);
        }

        // 3. 构建类目的层级关系，根据 parentId 建立树形结构
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttDataDevCatRespDTO cat : attDataDevCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // 如果存在父类，则加入父类的 children
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // 无父类，则为根级类目
                catRoots.add(node);
            }
        }

        // 4. 从任务列表中过滤出 type 为 "3" 的任务
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "3".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // 为便于根据类目 code 查找类目节点，构建一个 code 到节点的映射
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // 5. 遍历任务，将每个任务挂载到对应的类目节点下（匹配条件：任务的 catCode 与类目节点的 code 相等）
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                // 将任务转换为树节点
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

                // 将任务节点加入对应类目节点的 children
                categoryNode.getChildren().add(taskNode);
            }
        }
//
//        // 6. 为每个类目节点赋值任务数量（dppEtlTaskCount 仅统计直接挂载的任务数量）
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
//            // 这里判断子节点中，type 非空的节点视为任务节点
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // 6. 将整理好的类目树挂载到顶级目录下
        root.getChildren().addAll(catRoots);

        // 7. 递归计算每个节点的任务数量（包括子节点所有任务）
        computeTaskCount(root);
        // 返回顶级目录节点列表（只有一个根节点）
        return root;
    }

    /**
     * 构建数据集成任务树
     *
     * @param dppEtlTaskRespVOList 任务列表（其中 catCode 存储的是 AttTaskCatRespDTO 的 code）
     * @param attTaskCatApiList    类目列表，AttTaskCatRespDTO 中存在上下级关系，示例：父级 code "A01"，下级第一个 code "A01A01"
     * @return List<DppEtlTaskTreeRespVO> 构造后的任务树，顶级目录为“数据集成”
     */
    public DppEtlTaskTreeRespVO buildTaskCatTree(List<DppEtlTaskRespVO> dppEtlTaskRespVOList,
                                                 List<AttTaskCatRespDTO> attTaskCatApiList) {
        // 1. 构造顶级目录节点
        DppEtlTaskTreeRespVO root = new DppEtlTaskTreeRespVO();
        // 重新生成一个 id
        root.setTreeId(IdUtils.generateArtificialId());
        root.setId(IdUtils.generateArtificialId());
        root.setLabel("数据集成");
        root.setChildren(new ArrayList<>());

        // 2. 整理类目列表：先将每个 AttTaskCatRespDTO 转换为 DppEtlTaskTreeRespVO 节点，并放入 map（key 为类目 id）
        Map<Long, DppEtlTaskTreeRespVO> catNodeMap = new HashMap<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = new DppEtlTaskTreeRespVO();
            // 重新生成一个 id
            node.setTreeId(IdUtils.generateArtificialId());
            node.setId(cat.getId());
            node.setLabel(cat.getName());
            node.setCode(cat.getCode());
            node.setChildren(new ArrayList<>());
            // 类目节点的 dppEtlTaskCount 后续会赋值
            catNodeMap.put(cat.getId(), node);
        }

        // 3. 构建类目的层级关系，根据 parentId 建立树形结构
        List<DppEtlTaskTreeRespVO> catRoots = new ArrayList<>();
        for (AttTaskCatRespDTO cat : attTaskCatApiList) {
            DppEtlTaskTreeRespVO node = catNodeMap.get(cat.getId());
            if (cat.getParentId() != null && catNodeMap.containsKey(cat.getParentId())) {
                // 如果存在父类，则加入父类的 children
                DppEtlTaskTreeRespVO parentNode = catNodeMap.get(cat.getParentId());
                parentNode.getChildren().add(node);
            } else {
                // 无父类，则为根级类目
                catRoots.add(node);
            }
        }

        // 4. 从任务列表中过滤出 type 为 "1" 或 "2" 的任务
        List<DppEtlTaskRespVO> filteredTasks = dppEtlTaskRespVOList.stream()
                .filter(task -> "1".equals(task.getType()) || "2".equals(task.getType()))
                .collect(Collectors.toList());

        root.setDppEtlTaskCount(filteredTasks.size());

        // 为便于根据类目 code 查找类目节点，构建一个 code 到节点的映射
        Map<String, DppEtlTaskTreeRespVO> catCodeMap = new HashMap<>();
        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
            catCodeMap.put(catNode.getCode(), catNode);
        }

        // 5. 遍历任务，将每个任务挂载到对应的类目节点下（匹配条件：任务的 catCode 与类目节点的 code 相等）
        for (DppEtlTaskRespVO task : filteredTasks) {
            String taskCatCode = task.getCatCode();
            if (taskCatCode == null) {
                continue;
            }
            DppEtlTaskTreeRespVO categoryNode = catCodeMap.get(taskCatCode);
            if (categoryNode != null) {
                DppEtlTaskExtDO etlTaskExtDO = dppEtlTaskExtService.getByTaskId(task.getId());
                // 将任务转换为树节点
                DppEtlTaskTreeRespVO taskNode = new DppEtlTaskTreeRespVO();
                // 重新生成一个 id
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

                // 将任务节点加入对应类目节点的 children
                categoryNode.getChildren().add(taskNode);
            }
        }
//
//        // 6. 为每个类目节点赋值任务数量（dppEtlTaskCount 仅统计直接挂载的任务数量）
//        for (DppEtlTaskTreeRespVO catNode : catNodeMap.values()) {
//            int taskCount = 0;
//            // 这里判断子节点中，type 非空的节点视为任务节点
//            for (DppEtlTaskTreeRespVO child : catNode.getChildren()) {
//                if (child.getType() != null) {
//                    taskCount++;
//                }
//            }
//            catNode.setDppEtlTaskCount(taskCount);
//        }

        // 6. 将整理好的类目树挂载到顶级目录下
        root.getChildren().addAll(catRoots);

        // 7. 递归计算每个节点的任务数量（包括子节点所有任务）
        computeTaskCount(root);

        // 返回顶级目录节点列表（只有一个根节点）
        return root;
    }


    /**
     * 递归计算节点的任务数量，并赋值给 dppEtlTaskCount
     * 如果节点为任务节点（type != null），计数为 1；
     * 如果节点为类目节点（type == null），计数为其所有子节点任务数量之和
     *
     * @param node 当前节点
     * @return 当前节点及其子节点的任务总数
     */
    private static int computeTaskCount(DppEtlTaskTreeRespVO node) {
        int count = 0;
        // 如果是任务节点，计数为 1
        if (node.getType() != null) {
            count = 1;
        }
        // 如果存在子节点，则递归累加
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

        //生成任务编码
        DsNodeGenCodeRespDTO dsTaskGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
        String taskCode = String.valueOf(dsTaskGenCodeRespDTO.getData().get(0));


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

        //默认
        createReqVO.setCode(taskCode);
        createReqVO.setStatus("-1");//草稿
        createReqVO.setLocations("");
        createReqVO.setTimeout(0L);
        createReqVO.setDsId(0L);

        Long dppEtlTask = this.createDppEtlTask(createReqVO);

        DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO = new DppEtlSchedulerSaveReqVO();
        dppEtlSchedulerSaveReqVO.setTaskId(dppEtlTask);
        dppEtlSchedulerSaveReqVO.setTaskCode(taskCode);
        // 获取100年后的时间
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime + 100L * 365 * 24 * 60 * 60 * 1000);
        dppEtlSchedulerSaveReqVO.setStartTime(new Date());
        dppEtlSchedulerSaveReqVO.setEndTime(date);
        dppEtlSchedulerSaveReqVO.setTimezoneId("Asia/Shanghai"); // 默认时区
        dppEtlSchedulerSaveReqVO.setCronExpression(dppEtlNewNodeSaveReqVO.getCrontab());
        dppEtlSchedulerSaveReqVO.setFailureStrategy("1");
        dppEtlSchedulerSaveReqVO.setStatus("0");
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
        //任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务
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
     * 获取任务编码
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
        //生成任务编码
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

        //判断是否是离线任务 是需要获取扩展信息的任务编码进行接口调用
        if (StringUtils.equals("1", dppEtlTaskUpdateQueryRespVO.getType())) {
            //获取扩展信息
            DppEtlTaskExtDO taskExt = dppEtlTaskExtService.getByTaskId(Long.parseLong(nodeSaveReqVO.getId()));
            if (taskExt == null) {
                throw new ServiceException("暂无数据！");
            }
            dppEtlTaskUpdateQueryRespVO.setCode(taskExt.getEtlTaskCode());
        }

        DsTaskSaveRespDTO task = dsEtlTaskService.batchCopy(dppEtlTaskUpdateQueryRespVO.getCode()
                , dppEtlTaskUpdateQueryRespVO.getProjectCode());

        if (!task.getSuccess()) {
            throw new ServiceException("copy任务错误:" + task.getMsg().toString()); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();

        //任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务
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

        // 转换任务保存请求对象
        DppEtlTaskSaveReqVO taskSaveReqVO = TaskConverter.convertToDppEtlTaskSaveReqVO(dppEtlNewNodeSaveReqVO, data);
        taskSaveReqVO.setCode(taskCode);
        taskSaveReqVO.setDraftJson(src.getDraftJson());

        for (Map<String, Object> location : locations) {
            Long codeold = MapUtils.getLong(location, "taskCode");
            //生成节点编码
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(dppEtlNewNodeSaveReqVO.getProjectCode());
            String codeNew = String.valueOf(dsNodeGenCodeRespDTO.getData().get(0));

            definitionCopyVO.put(codeold, JSONUtils.convertToLong(codeNew));
            location.put("taskCode", codeNew);
        }

        //封装节点编码
        remapTaskCodes(dppEtlNewNodeSaveReqVO, definitionCopyVO);

        taskSaveReqVO.setLocations(JSONUtils.toJson(locations));
        Long dppEtlTask = this.createDppEtlTask(taskSaveReqVO);
        taskSaveReqVO.setId(dppEtlTask);

        //构建任务任务信息
        Map<String, Object> taskInfo = new HashMap<>();
        taskInfo.put("projectCode", dppEtlNewNodeSaveReqVO.getProjectCode());
        taskInfo.put("taskCode", taskCode);
        taskInfo.put("taskVersion", 1);
        taskInfo.put("name", name);

        // 调度器对象构建
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

        //创建etl任务扩展数据
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

        return taskSaveReqVO; // 返回创建结果
    }


    public static void remapTaskCodes(DppEtlNewNodeSaveReqVO vo, Map<Long, Long> definitionCopyVO) {
        if (vo == null || definitionCopyVO == null || definitionCopyVO.isEmpty()) {
            return;
        }

        // 1) 解析 taskDefinitionList
        String taskDefJson = vo.getTaskDefinitionList();
        if (taskDefJson != null && !taskDefJson.isEmpty()) {
            List<DppEtlNodeSaveReqVO> nodeList =
                    JSON.parseArray(taskDefJson, DppEtlNodeSaveReqVO.class);

            if (nodeList != null && !nodeList.isEmpty()) {
                for (DppEtlNodeSaveReqVO node : nodeList) {
                    node.setId(null);
                    // code 可能为字符串，需转成 Long 做映射
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

        // 2) 解析 taskRelationJson
        String relJson = vo.getTaskRelationJson();
        if (relJson != null && !relJson.isEmpty()) {
            java.util.List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList =
                    JSON.parseArray(relJson, DppEtlTaskNodeRelRespVO.class);

            List<ProcessTaskRelation> relList = new ArrayList<>();
            if (dppEtlTaskNodeRelRespVOList != null && !dppEtlTaskNodeRelRespVOList.isEmpty()) {
                for (DppEtlTaskNodeRelRespVO srcRel : dppEtlTaskNodeRelRespVOList) {
                    //调取映射子方法
                    ProcessTaskRelation rel = toProcessTaskRelation(srcRel, definitionCopyVO);
                    relList.add(rel);
                }
                vo.setTaskRelationJson(JSON.toJSONString(relList));
            }
        }
    }

    /**
     * 将 DppEtlTaskNodeRelRespVO → ProcessTaskRelation，并按 definitionCopyVO 重映射 pre/post code
     */
    private static ProcessTaskRelation toProcessTaskRelation(DppEtlTaskNodeRelRespVO src,
                                                             Map<Long, Long> definitionCopyVO) {
        ProcessTaskRelation rel = new ProcessTaskRelation();

        // 仅按你的要求映射这四个字段
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
