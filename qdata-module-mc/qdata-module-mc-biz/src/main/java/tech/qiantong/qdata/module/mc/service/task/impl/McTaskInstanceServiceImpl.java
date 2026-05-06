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
 * 采集任务实例Service业务层处理
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

        // FIXME(用户查询避免循环查询，临时方案)  使用 Map 缓存用户信息,避免重复查询
        Map<Long, SysUser> userCache = Maps.newHashMap();
        for (McTaskInstanceDO row : rows) {
            // 获取创建人手机号
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
        // 相关校验

        // 更新采集任务实例
        McTaskInstanceDO updateObj = BeanUtils.toBean(updateReqVO, McTaskInstanceDO.class);
        return mcTaskInstanceMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskInstance(Collection<Long> idList) {
        // 批量删除采集任务实例
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

        // 只取第 1 页、1 条
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入采集任务实例数据
     *
     * @param importExcelList 采集任务实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importMcTaskInstance(List<McTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + mcTaskInstanceId + " 的采集任务实例记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + mcTaskInstanceId + " 的采集任务实例记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<McTaskInstanceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", mcTaskInstanceId);
                    McTaskInstanceDO existingMcTaskInstance = mcTaskInstanceMapper.selectOne(queryWrapper);
                    if (existingMcTaskInstance == null) {
                        mcTaskInstanceMapper.insert(mcTaskInstanceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + mcTaskInstanceId + " 的采集任务实例记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + mcTaskInstanceId + " 的采集任务实例记录已存在。");
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
}
