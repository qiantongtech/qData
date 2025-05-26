package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskInstanceMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    private IDsEtlExecutorService dsEtlExecutorService;

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
    public AjaxResult execute(Long taskInstanceId, ExecuteType executeType) {
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = this.getById(taskInstanceId);
        if (dppEtlTaskInstanceDO == null) {
            return error("任务实例不存在，请刷新后重试！");
        }
        String status = dppEtlTaskInstanceDO.getStatus();
        if (!StringUtils.equals(status, "7") &&
                !StringUtils.equals(status, "3") &&
                !StringUtils.equals(status, "6") &&
                !StringUtils.equals(status, "5")) {
            return error("当前状态无法重跑，请刷新后重试！");
        }
        DsStatusRespDTO dsStatusRespDTO = dsEtlExecutorService.execute(DSExecuteDTO.builder()
                .processInstanceId(taskInstanceId)
                .executeType(executeType)
                .build(), dppEtlTaskInstanceDO.getProjectCode());
        return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
    }
}
