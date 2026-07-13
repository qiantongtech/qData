package tech.qiantong.qdata.module.mc.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;
import tech.qiantong.qdata.module.mc.dal.mapper.task.McTaskSchedulerMapper;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskSchedulerService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data integration scheduling information Service business layer processing
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskSchedulerServiceImpl extends ServiceImpl<McTaskSchedulerMapper, McTaskSchedulerDO> implements IMcTaskSchedulerService {
    @Resource
    private McTaskSchedulerMapper mcTaskSchedulerMapper;

    @Override
    public PageResult<McTaskSchedulerDO> getMcTaskSchedulerPage(McTaskSchedulerPageReqVO pageReqVO) {
        return mcTaskSchedulerMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcTaskScheduler(McTaskSchedulerSaveReqVO createReqVO) {
        McTaskSchedulerDO dictType = BeanUtils.toBean(createReqVO, McTaskSchedulerDO.class);
        if(StringUtils.isEmpty(dictType.getStatus())){
            dictType.setStatus("1");
        }
        mcTaskSchedulerMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateMcTaskScheduler(McTaskSchedulerSaveReqVO updateReqVO) {
        // Related verification

        // Update data integration scheduling information
        McTaskSchedulerDO updateObj = BeanUtils.toBean(updateReqVO, McTaskSchedulerDO.class);
        return mcTaskSchedulerMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskScheduler(Collection<Long> idList) {
        // Deleting data integration scheduling information in batches
        return mcTaskSchedulerMapper.deleteBatchIds(idList);
    }

    @Override
    public McTaskSchedulerDO getMcTaskSchedulerById(Long id) {
        return mcTaskSchedulerMapper.selectById(id);
    }

    @Override
    public McTaskSchedulerDO getMcTaskSchedulerBytaskId(Long taskId) {
        MPJLambdaWrapper<McTaskSchedulerDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTaskSchedulerDO::getTaskId, taskId)
                .eq(McTaskSchedulerDO::getDelFlag, "0")
                .orderByAsc(McTaskSchedulerDO::getCreateTime);
        List<McTaskSchedulerDO> mcTaskSchedulerDOS = mcTaskSchedulerMapper.selectList(wrapper);
        return CollectionUtils.isEmpty(mcTaskSchedulerDOS) ? null : mcTaskSchedulerDOS.get(0);
    }

    @Override
    public List<McTaskSchedulerDO> getMcTaskSchedulerList() {
        return mcTaskSchedulerMapper.selectList();
    }

    @Override
    public Map<Long, McTaskSchedulerDO> getMcTaskSchedulerMap() {
        List<McTaskSchedulerDO> mcTaskSchedulerList = mcTaskSchedulerMapper.selectList();
        return mcTaskSchedulerList.stream()
                .collect(Collectors.toMap(
                        McTaskSchedulerDO::getId,
                        mcTaskSchedulerDO -> mcTaskSchedulerDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data integration scheduling information data
     *
     * @param importExcelList Data integration scheduling information data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    @Override
    public String importMcTaskScheduler(List<McTaskSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("mc.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (McTaskSchedulerRespVO respVO : importExcelList) {
            try {
                McTaskSchedulerDO mcTaskSchedulerDO = BeanUtils.toBean(respVO, McTaskSchedulerDO.class);
                Long mcTaskSchedulerId = respVO.getId();
                if (isUpdateSupport) {
                    if (mcTaskSchedulerId != null) {
                        McTaskSchedulerDO existingMcTaskScheduler = mcTaskSchedulerMapper.selectById(mcTaskSchedulerId);
                        if (existingMcTaskScheduler != null) {
                            mcTaskSchedulerMapper.updateById(mcTaskSchedulerDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("mc.import.update.success",
                                    "数据更新成功，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录。", mcTaskSchedulerId, "数据集成调度信息"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.fail",
                                    "数据更新失败，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录不存在。", mcTaskSchedulerId, "数据集成调度信息"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<McTaskSchedulerDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskSchedulerId);
                    McTaskSchedulerDO existingMcTaskScheduler = mcTaskSchedulerMapper.selectOne(queryWrapper);
                    if (existingMcTaskScheduler == null) {
                        mcTaskSchedulerMapper.insert(mcTaskSchedulerDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("mc.import.insert.success",
                                "数据插入成功，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录。", mcTaskSchedulerId, "数据集成调度信息"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.insert.fail",
                                "数据插入失败，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录已存在。", mcTaskSchedulerId, "数据集成调度信息"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("mc.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("mc.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("mc.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("mc.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public void updateReleaseSchedule(McTaskSchedulerSaveReqVO mcTask) {
        LambdaUpdateWrapper<McTaskSchedulerDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McTaskSchedulerDO::getTaskId, mcTask.getTaskId())
                .set(McTaskSchedulerDO::getStatus, mcTask.getStatus());

        mcTaskSchedulerMapper.update(null, wrapper);
    }
}
