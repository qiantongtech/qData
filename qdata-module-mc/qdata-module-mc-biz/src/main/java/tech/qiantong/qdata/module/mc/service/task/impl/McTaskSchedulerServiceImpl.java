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
 * 数据集成调度信息Service业务层处理
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
        // 相关校验

        // 更新数据集成调度信息
        McTaskSchedulerDO updateObj = BeanUtils.toBean(updateReqVO, McTaskSchedulerDO.class);
        return mcTaskSchedulerMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskScheduler(Collection<Long> idList) {
        // 批量删除数据集成调度信息
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据集成调度信息数据
     *
     * @param importExcelList 数据集成调度信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importMcTaskScheduler(List<McTaskSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<McTaskSchedulerDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskSchedulerId);
                    McTaskSchedulerDO existingMcTaskScheduler = mcTaskSchedulerMapper.selectOne(queryWrapper);
                    if (existingMcTaskScheduler == null) {
                        mcTaskSchedulerMapper.insert(mcTaskSchedulerDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + mcTaskSchedulerId + " 的数据集成调度信息记录已存在。");
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
    public void updateReleaseSchedule(McTaskSchedulerSaveReqVO mcTask) {
        LambdaUpdateWrapper<McTaskSchedulerDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(McTaskSchedulerDO::getTaskId, mcTask.getTaskId())
                .set(McTaskSchedulerDO::getStatus, mcTask.getStatus());

        mcTaskSchedulerMapper.update(null, wrapper);
    }
}
