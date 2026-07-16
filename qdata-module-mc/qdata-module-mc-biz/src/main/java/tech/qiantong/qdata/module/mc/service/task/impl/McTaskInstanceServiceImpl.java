package tech.qiantong.qdata.module.mc.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;
import tech.qiantong.qdata.module.mc.dal.mapper.task.McTaskInstanceMapper;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskInstanceService;
import tech.qiantong.qdata.module.system.service.ISysUserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collection task instance Service business layer processing
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskInstanceServiceImpl extends ServiceImpl<McTaskInstanceMapper, McTaskInstanceDO> implements IMcTaskInstanceService {
    @Resource
    private McTaskInstanceMapper mcTaskInstanceMapper;
    @Resource
    private ISysUserService sysUserService;

    @Override
    public PageResult<McTaskInstanceDO> getMcTaskInstancePage(McTaskInstancePageReqVO pageReqVO) {
        PageResult<McTaskInstanceDO> mcTaskInstanceDOPageResult = mcTaskInstanceMapper.selectPage(pageReqVO);
        List<McTaskInstanceDO> rows = mcTaskInstanceDOPageResult.getRows();

        if (CollectionUtils.isEmpty(rows)) {
            return mcTaskInstanceDOPageResult;
        }

        // FIXME (user query to avoid circular query, temporary solution) uses Map to cache user information to avoid repeated queries
        Map<Long, SysUser> userCache = Maps.newHashMap();
        for (McTaskInstanceDO row : rows) {
            // Get the creator’s mobile phone number
            Long creatorId = row.getCreatorId();
            if (creatorId != null && !userCache.containsKey(creatorId)) {
                SysUser sysUser = sysUserService.selectUserById(creatorId);
                if (sysUser != null) {
                    userCache.put(creatorId, sysUser);
                }
            }
            SysUser creatorUser = userCache.get(creatorId);
            if (creatorUser != null) {
                row.setCreatePhoneNumber(creatorUser.getPhonenumber());
            }

        }
        mcTaskInstanceDOPageResult.setRows(rows);
        return mcTaskInstanceDOPageResult;
    }

    @Override
    public Long createMcTaskInstance(McTaskInstanceSaveReqVO createReqVO) {
        McTaskInstanceDO dictType = BeanUtils.toBean(createReqVO, McTaskInstanceDO.class);
        mcTaskInstanceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public Long createMcTaskInstance(McTaskInstanceDO createReqVO) {
        mcTaskInstanceMapper.insert(createReqVO);
        return createReqVO.getId();
    }

    @Override
    public int updateMcTaskInstance(McTaskInstanceSaveReqVO updateReqVO) {
        // Related verification

        // Update collection task instance
        McTaskInstanceDO updateObj = BeanUtils.toBean(updateReqVO, McTaskInstanceDO.class);
        return mcTaskInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskInstance(Collection<Long> idList) {
        // Delete collection task instances in batches
        return mcTaskInstanceMapper.deleteBatchIds(idList);
    }

    @Override
    public McTaskInstanceDO getMcTaskInstanceById(Long id) {
        return mcTaskInstanceMapper.selectById(id);
    }

    @Override
    public McTaskInstanceDO getMcTaskInstanceByTaskId(Long taskId) {
        MPJLambdaWrapper<McTaskInstanceDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTaskInstanceDO::getTaskId, taskId)
                .orderByDesc(McTaskInstanceDO::getCreateTime);

        // Take only page 1 and item 1
        Page<McTaskInstanceDO> page = new Page<>(1, 1);
        Page<McTaskInstanceDO> result = mcTaskInstanceMapper.selectPage(page, wrapper);

        return result.getRecords().isEmpty() ? null : result.getRecords().get(0);
    }

    @Override
    public List<McTaskInstanceDO> getMcTaskInstanceList() {
        return mcTaskInstanceMapper.selectList();
    }

    @Override
    public Map<Long, McTaskInstanceDO> getMcTaskInstanceMap() {
        List<McTaskInstanceDO> mcTaskInstanceList = mcTaskInstanceMapper.selectList();
        return mcTaskInstanceList.stream()
                .collect(Collectors.toMap(
                        McTaskInstanceDO::getId,
                        mcTaskInstanceDO -> mcTaskInstanceDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import collection task instance data
     *
     * @param importExcelList Collection task instance data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    @Override
    public String importMcTaskInstance(List<McTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("mc.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (McTaskInstanceRespVO respVO : importExcelList) {
            try {
                McTaskInstanceDO mcTaskInstanceDO = BeanUtils.toBean(respVO, McTaskInstanceDO.class);
                Long mcTaskInstanceId = respVO.getId();
                if (isUpdateSupport) {
                    if (mcTaskInstanceId != null) {
                        McTaskInstanceDO existingMcTaskInstance = mcTaskInstanceMapper.selectById(mcTaskInstanceId);
                        if (existingMcTaskInstance != null) {
                            mcTaskInstanceMapper.updateById(mcTaskInstanceDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("mc.import.update.success",
                                    "数据更新成功，ID为 " + mcTaskInstanceId + " 的采集任务实例记录。", mcTaskInstanceId, "采集任务实例"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.fail",
                                    "数据更新失败，ID为 " + mcTaskInstanceId + " 的采集任务实例记录不存在。", mcTaskInstanceId, "采集任务实例"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<McTaskInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskInstanceId);
                    McTaskInstanceDO existingMcTaskInstance = mcTaskInstanceMapper.selectOne(queryWrapper);
                    if (existingMcTaskInstance == null) {
                        mcTaskInstanceMapper.insert(mcTaskInstanceDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("mc.import.insert.success",
                                "数据插入成功，ID为 " + mcTaskInstanceId + " 的采集任务实例记录。", mcTaskInstanceId, "采集任务实例"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.insert.fail",
                                "数据插入失败，ID为 " + mcTaskInstanceId + " 的采集任务实例记录已存在。", mcTaskInstanceId, "采集任务实例"));
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
}
