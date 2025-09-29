package tech.qiantong.qdata.module.dpp.service.etl.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlNodeInstanceRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceRespDTO;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceTreeListRespVO;
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
import java.util.*;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * 数据集成任务实例Service业务层处理
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskInstanceServiceImpl extends ServiceImpl<DppEtlTaskInstanceMapper, DppEtlTaskInstanceDO> implements IDppEtlTaskInstanceService {
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
        return dppEtlTaskInstanceMapper.selectPage(pageReqVO);
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
        // 相关校验

        // 更新数据集成任务实例
        DppEtlTaskInstanceDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskInstanceDO.class);
        return dppEtlTaskInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlTaskInstance(Collection<Long> idList) {
        // 批量删除数据集成任务实例
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成任务实例数据
     *
     * @param importExcelList 数据集成任务实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDppEtlTaskInstance(List<DppEtlTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + dppEtlTaskInstanceId + " 的数据集成任务实例记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEtlTaskInstanceId + " 的数据集成任务实例记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEtlTaskInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskInstanceId);
                    DppEtlTaskInstanceDO existingDppEtlTaskInstance = dppEtlTaskInstanceMapper.selectOne(queryWrapper);
                    if (existingDppEtlTaskInstance == null) {
                        dppEtlTaskInstanceMapper.insert(dppEtlTaskInstanceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEtlTaskInstanceId + " 的数据集成任务实例记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEtlTaskInstanceId + " 的数据集成任务实例记录已存在。");
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
                        //判断是否是子任务
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
            return error("任务实例不存在，请刷新后重试！");
        }
        String status = dppEtlTaskInstanceDO.getStatus();
        if (ExecuteType.REPEAT_RUNNING.getCode() == executeType.getCode() && !StringUtils.equals(status, "3") &&
                !StringUtils.equals(status, "5") &&
                !StringUtils.equals(status, "6") &&
                !StringUtils.equals(status, "7")) {
            return error("当前状态无法重跑，请刷新后重试！");
        }
        if (ExecuteType.STOP.getCode() == executeType.getCode() &&
                !StringUtils.equals(status, "0") &&
                !StringUtils.equals(status, "1") &&
                !StringUtils.equals(status, "2") &&
                !StringUtils.equals(status, "3") &&
                !StringUtils.equals(status, "12") &&
                !StringUtils.equals(status, "14")) {
            return error("当前状态无法停止，请刷新后重试！");
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
        //获取任务信息
        DppEtlTaskLogRespVO dppEtlTaskLogRespVO = dppEtlTaskLogService.getDppEtlTaskLogById(DppEtlTaskLogPageReqVO.builder()
                .code(dppEtlTaskInstanceDO.getTaskCode())
                .version(dppEtlTaskInstanceDO.getTaskVersion())
                .build());
        if (dppEtlTaskLogRespVO == null) {
            throw new RuntimeException("任务不存在");
        }
        //获取节点关系数据
        JSONArray locations = JSONArray.parse(dppEtlTaskLogRespVO.getLocations());
        //获取节点数据
        List<DppEtlNodeInstanceDO> dppEtlNodeInstanceDOList = dppEtlTNodeInstanceService.list(Wrappers.lambdaQuery(DppEtlNodeInstanceDO.class)
                .select(DppEtlNodeInstanceDO::getId,
                        DppEtlNodeInstanceDO::getNodeCode,
                        DppEtlNodeInstanceDO::getName,
                        DppEtlNodeInstanceDO::getStatus)
                .eq(DppEtlNodeInstanceDO::getTaskInstanceId, taskInstanceId));

        String processInstanceLogKey = TaskConverter.PROCESS_INSTANCE_LOG_KEY + taskInstanceId;
        if (StringUtils.equals("1", dppEtlTaskInstanceDO.getTaskType())) {//判断是否是离线任务
            if (redisService.hasKey(processInstanceLogKey)) {
                log = redisService.get(processInstanceLogKey);
            } else {
                //获取表中的日志
                String logContent = dppEtlTaskInstanceLogService.getLog(taskInstanceId);
                if (logContent != null) {
                    log = logContent;
                }
            }
        } else {
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
                        //获取表中的日志
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
        //根据任务实例id获取任务信息
        MPJLambdaWrapper<DppEtlTaskInstanceDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DppEtlTaskInstanceDO.class)
                .select("t3.NICK_NAME AS personChargeName")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DppEtlTaskInstanceDO::getId, id);
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceMapper.selectJoinOne(DppEtlTaskInstanceDO.class, lambdaWrapper);

        //获取任务信息
        DppEtlTaskLogRespVO dppEtlTaskLogRespVO = dppEtlTaskLogService.getDppEtlTaskLogById(DppEtlTaskLogPageReqVO.builder()
                .code(dppEtlTaskInstanceDO.getTaskCode())
                .version(dppEtlTaskInstanceDO.getTaskVersion())
                .build());
        if (dppEtlTaskLogRespVO == null) {
            throw new RuntimeException("任务不存在");
        }
        DppEtlTaskUpdateQueryRespVO bean = new DppEtlTaskUpdateQueryRespVO(BeanUtils.toBean(dppEtlTaskLogRespVO, DppEtlTaskDO.class));
        bean.setTaskInstance(dppEtlTaskInstanceDO);
        //获取关系数据
        List<DppEtlTaskNodeRelRespVO> dppEtlTaskNodeRelRespVOList = iDppEtlTaskNodeRelService.getDppEtlTaskNodeRelRespVOList(DppEtlTaskNodeRelPageReqVO.builder()
                .taskCode(bean.getCode())
                .taskVersion(bean.getVersion())
                .build());
        bean.setTaskRelationJsonFromNodeRelList(dppEtlTaskNodeRelRespVOList);

        //获取捷信信息
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

}
