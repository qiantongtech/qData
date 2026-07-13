package tech.qiantong.qdata.module.mc.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;
import tech.qiantong.qdata.module.mc.dal.mapper.task.McTaskScopeMapper;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskScopeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collection scope Service business layer processing
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskScopeServiceImpl extends ServiceImpl<McTaskScopeMapper, McTaskScopeDO> implements IMcTaskScopeService {
    @Resource
    private McTaskScopeMapper mcTaskScopeMapper;

    @Override
    public PageResult<McTaskScopeDO> getMcTaskScopePage(McTaskScopePageReqVO pageReqVO) {
        return mcTaskScopeMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcTaskScope(McTaskScopeSaveReqVO createReqVO) {
        McTaskScopeDO dictType = BeanUtils.toBean(createReqVO, McTaskScopeDO.class);
        mcTaskScopeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateMcTaskScope(McTaskScopeSaveReqVO updateReqVO) {
        // Related verification

        // Update collection range
        McTaskScopeDO updateObj = BeanUtils.toBean(updateReqVO, McTaskScopeDO.class);
        return mcTaskScopeMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskScope(Collection<Long> idList) {
        // Delete collection ranges in batches
        return mcTaskScopeMapper.deleteBatchIds(idList);
    }

    @Override
    public McTaskScopeDO getMcTaskScopeById(Long id) {
        return mcTaskScopeMapper.selectById(id);
    }

    @Override
    public List<McTaskScopeDO> getMcTaskScopeList() {
        return mcTaskScopeMapper.selectList();
    }

    @Override
    public Map<Long, McTaskScopeDO> getMcTaskScopeMap() {
        List<McTaskScopeDO> mcTaskScopeList = mcTaskScopeMapper.selectList();
        return mcTaskScopeList.stream()
                .collect(Collectors.toMap(
                        McTaskScopeDO::getId,
                        mcTaskScopeDO -> mcTaskScopeDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import collection range data
     *
     * @param importExcelList collection range data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    @Override
    public String importMcTaskScope(List<McTaskScopeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("mc.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (McTaskScopeRespVO respVO : importExcelList) {
            try {
                McTaskScopeDO mcTaskScopeDO = BeanUtils.toBean(respVO, McTaskScopeDO.class);
                Long mcTaskScopeId = respVO.getId();
                if (isUpdateSupport) {
                    if (mcTaskScopeId != null) {
                        McTaskScopeDO existingMcTaskScope = mcTaskScopeMapper.selectById(mcTaskScopeId);
                        if (existingMcTaskScope != null) {
                            mcTaskScopeMapper.updateById(mcTaskScopeDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("mc.import.update.success",
                                    "数据更新成功，ID为 " + mcTaskScopeId + " 的采集范围记录。", mcTaskScopeId, "采集范围"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.fail",
                                    "数据更新失败，ID为 " + mcTaskScopeId + " 的采集范围记录不存在。", mcTaskScopeId, "采集范围"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<McTaskScopeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskScopeId);
                    McTaskScopeDO existingMcTaskScope = mcTaskScopeMapper.selectOne(queryWrapper);
                    if (existingMcTaskScope == null) {
                        mcTaskScopeMapper.insert(mcTaskScopeDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("mc.import.insert.success",
                                "数据插入成功，ID为 " + mcTaskScopeId + " 的采集范围记录。", mcTaskScopeId, "采集范围"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("mc.import.insert.fail",
                                "数据插入失败，ID为 " + mcTaskScopeId + " 的采集范围记录已存在。", mcTaskScopeId, "采集范围"));
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
    public void removeMcTaskScopeBytaskId(Long taskId) {
        MPJLambdaWrapper<McTaskScopeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTaskScopeDO::getTaskId, taskId)
                .eq(McTaskScopeDO::getDelFlag, "0");

        mcTaskScopeMapper.delete(wrapper);
    }

    @Override
    public List<McTaskScopeDO> getMcTaskScopeListBytaskId(Long taskId) {
        MPJLambdaWrapper<McTaskScopeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTaskScopeDO::getTaskId, taskId)
                .eq(McTaskScopeDO::getDelFlag, "0");
        return mcTaskScopeMapper.selectList(wrapper);
    }
}
