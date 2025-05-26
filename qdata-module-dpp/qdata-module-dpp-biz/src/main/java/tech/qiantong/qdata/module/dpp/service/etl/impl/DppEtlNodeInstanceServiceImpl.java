package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeInstanceMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据集成节点实例Service业务层处理
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

    @Override
    public PageResult<DppEtlNodeInstanceDO> getDppEtlNodeInstancePage(DppEtlNodeInstancePageReqVO pageReqVO) {
        return dppEtlNodeInstanceMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO createReqVO) {
        DppEtlNodeInstanceDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeInstanceDO.class);
        dppEtlNodeInstanceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成节点实例
        DppEtlNodeInstanceDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlNodeInstanceDO.class);
        return dppEtlNodeInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlNodeInstance(Collection<Long> idList) {
        // 批量删除数据集成节点实例
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成节点实例数据
     *
     * @param importExcelList 数据集成节点实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDppEtlNodeInstance(List<DppEtlNodeInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + dppEtlNodeInstanceId + " 的数据集成节点实例记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEtlNodeInstanceId + " 的数据集成节点实例记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEtlNodeInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlNodeInstanceId);
                    DppEtlNodeInstanceDO existingDppEtlNodeInstance = dppEtlNodeInstanceMapper.selectOne(queryWrapper);
                    if (existingDppEtlNodeInstance == null) {
                        dppEtlNodeInstanceMapper.insert(dppEtlNodeInstanceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEtlNodeInstanceId + " 的数据集成节点实例记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEtlNodeInstanceId + " 的数据集成节点实例记录已存在。");
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
}
