package tech.qiantong.qdata.module.mc.service.task.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;
import tech.qiantong.qdata.module.mc.dal.mapper.task.McTaskInstanceLogMapper;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskInstanceLogService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Collection task instance-Log Service business layer processing
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskInstanceLogServiceImpl extends ServiceImpl<McTaskInstanceLogMapper, McTaskInstanceLogDO> implements IMcTaskInstanceLogService {

    // Can be placed in constant class, inline here
    public static final String MC_TASK_INSTANCE_LOG_KEY_PREFIX = "MC_TASK_INSTANCE_LOG:LOG:TASK:";
    public static final String FINALIZE_TOKEN = "FINALIZE_SESSION";

    @Resource
    private McTaskInstanceLogMapper mcTaskInstanceLogMapper;

    @Resource
    @Lazy
    private IRedisService redisService;

    @Override
    public PageResult<McTaskInstanceLogDO> getMcTaskInstanceLogPage(McTaskInstanceLogPageReqVO pageReqVO) {
        return mcTaskInstanceLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createMcTaskInstanceLog(McTaskInstanceLogSaveReqVO createReqVO) {
        McTaskInstanceLogDO dictType = BeanUtils.toBean(createReqVO, McTaskInstanceLogDO.class);
        mcTaskInstanceLogMapper.insert(dictType);
        return 1L;
    }

    @Override
    public int updateMcTaskInstanceLog(McTaskInstanceLogSaveReqVO updateReqVO) {
        // Related verification

        // Update collection task instance-log
        McTaskInstanceLogDO updateObj = BeanUtils.toBean(updateReqVO, McTaskInstanceLogDO.class);
        return mcTaskInstanceLogMapper.updateById(updateObj);
    }

    @Override
    public int removeMcTaskInstanceLog(Collection<Long> idList) {
        // Batch deletion of collection task instances-logs
        return mcTaskInstanceLogMapper.deleteBatchIds(idList);
    }

    @Override
    public McTaskInstanceLogDO getMcTaskInstanceLogById(Long id) {
        McTaskInstanceLogDO mcTaskInstanceLogDO = buildRealtimeLog(id);
        if(mcTaskInstanceLogDO != null){
            return mcTaskInstanceLogDO;
        }

        MPJLambdaWrapper<McTaskInstanceLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(McTaskInstanceLogDO::getTaskInstanceId, id)
                .eq(McTaskInstanceLogDO::getDelFlag, "0")
                .orderByAsc(McTaskInstanceLogDO::getCreateTime);
        List<McTaskInstanceLogDO> mcTaskSchedulerDOS = mcTaskInstanceLogMapper.selectList(wrapper);
        McTaskInstanceLogDO taskInstanceLogDO = CollectionUtils.isEmpty(mcTaskSchedulerDOS) ? null : mcTaskSchedulerDOS.get(0);
        taskInstanceLogDO.setStatus("2");
        return taskInstanceLogDO;
    }


    /**
     * Obtain logs from Redis in real time and encapsulate them as McTaskInstanceLogDO
     */
    private McTaskInstanceLogDO buildRealtimeLog(Long taskInstanceId) {
        final String taskLogKey = MC_TASK_INSTANCE_LOG_KEY_PREFIX + taskInstanceId;
        String taskLog = redisService.get(taskLogKey);

        if (StringUtils.isBlank(taskLog)) {
            return null;
        }

        McTaskInstanceLogDO entity = new McTaskInstanceLogDO();
        entity.setTaskInstanceId(taskInstanceId);
        entity.setLogContent(taskLog);
        entity.setTime(new Date());
        entity.setValidFlag(Boolean.TRUE);
        entity.setDelFlag(Boolean.FALSE);

        String lastLine = getLastNotBlankLine(taskLog);
        if (StringUtils.contains(lastLine, FINALIZE_TOKEN)){
            entity.setStatus("2");
        }else {
            entity.setStatus("1");
        }
        return entity;
    }

    private String getLastNotBlankLine(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        int end = text.length();

        while (end > 0) {
            int start = text.lastIndexOf("\n", end - 1);
            String line = text.substring(start + 1, end).trim();

            if (StringUtils.isNotBlank(line)) {
                return line;
            }

            end = start; // Keep looking up
        }

        return null;
    }


    @Override
    public List<McTaskInstanceLogDO> getMcTaskInstanceLogList() {
        return mcTaskInstanceLogMapper.selectList();
    }


    /**
     * Collection task instance log writing (incremental accumulation + one-time drop at the end of the session)
     *
     * @param taskInstanceId task instance ID
     * @param taskId task ID
     * @param logStr append log this time
     */
    @Override
    public void taskInstanceLogAppend(Long taskInstanceId, Long taskId, String logStr) {
        if (taskInstanceId == null || StringUtils.isBlank(logStr)) {
            return;
        }

        final String taskLogKey = MC_TASK_INSTANCE_LOG_KEY_PREFIX + taskInstanceId;

        // 1. Read existing logs
        String taskLog = redisService.get(taskLogKey);
        if (taskLog == null) {
            taskLog = "";
        }

        String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
        logStr = time + " - " + logStr + "\n";

        // 2. Append
        taskLog += logStr + (logStr.matches(".*\\r?\\n.*") ? "" : "\n");
        redisService.set(taskLogKey, taskLog);

        // 3. Session end mark, unified storage
        if (StringUtils.contains(logStr, FINALIZE_TOKEN)) {
            McTaskInstanceLogDO entity = McTaskInstanceLogDO.builder()
                    .taskInstanceId(taskInstanceId)
                    .taskId(taskId)
                    .time(new Date())
                    .logContent(taskLog)
                    .validFlag(Boolean.TRUE)
                    .delFlag(Boolean.FALSE)
                    .build();

            this.saveOrUpdateByPk(entity);

            // 4. Clear Redis and cache it briefly (5 minutes)
            redisService.delete(taskLogKey);
            redisService.set(taskLogKey, taskLog, 60 * 5);
        }
    }

    @Override
    public int saveOrUpdateByPk(McTaskInstanceLogDO entity) {
        McTaskInstanceLogDO old = this.getOne(
                Wrappers.lambdaQuery(McTaskInstanceLogDO.class)
                        .eq(McTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId())
        );

        if (old != null) {
            old.setLogContent(entity.getLogContent());
            old.setValidFlag(entity.getValidFlag());
            old.setDelFlag(entity.getDelFlag());
            old.setUpdateBy(entity.getUpdateBy());
            old.setUpdatorId(entity.getUpdatorId());
            old.setTime(entity.getTime());

            return mcTaskInstanceLogMapper.update(
                    old,
                    Wrappers.lambdaUpdate(McTaskInstanceLogDO.class)
                            .eq(McTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId())
            );
        } else {
            return mcTaskInstanceLogMapper.insert(entity);
        }
    }
}
