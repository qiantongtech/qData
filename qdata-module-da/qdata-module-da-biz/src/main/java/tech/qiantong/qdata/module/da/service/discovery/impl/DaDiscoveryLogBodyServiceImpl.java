/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.service.discovery.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryLogBodyDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryLogBodyMapper;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryLogBodyService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 数据发现节点实例-日志Service业务层处理
 *
 * @author qdata
 * @date 2025-10-15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryLogBodyServiceImpl extends ServiceImpl<DaDiscoveryLogBodyMapper, DaDiscoveryLogBodyDO> implements IDaDiscoveryLogBodyService {

    // 可放到常量类，这里内联
    public static final String DISCOVERY_TASK_LOG_KEY_PREFIX = "DA_DISCOVERY:LOG:TASK:";
    public static final String FINALIZE_TOKEN = "FINALIZE_SESSION";

    @Resource
    private DaDiscoveryLogBodyMapper daDiscoveryLogBodyMapper;

    @Resource
    @Lazy
    private IRedisService redisService;

    @Override
    public int saveOrUpdateByPk(DaDiscoveryLogBodyDO entity) {
        DaDiscoveryLogBodyDO old = this.getOne(Wrappers.lambdaQuery(DaDiscoveryLogBodyDO.class)
                .eq(DaDiscoveryLogBodyDO::getTaskId, entity.getTaskId()));
        if (old != null) {
            old.setLogContent(entity.getLogContent());
            old.setValidFlag(entity.getValidFlag());
            old.setDelFlag(entity.getDelFlag());
            old.setUpdateBy(entity.getUpdateBy());
            old.setUpdaterId(entity.getUpdaterId());
            old.setRemark(entity.getRemark());
            return daDiscoveryLogBodyMapper.update(old, Wrappers.lambdaUpdate(DaDiscoveryLogBodyDO.class)
                    .eq(DaDiscoveryLogBodyDO::getTaskId, entity.getTaskId()));
        } else {
            return daDiscoveryLogBodyMapper.insert(entity);
        }
    }

    @Override
    public String getLatestLog(Long taskId) {
        Page<DaDiscoveryLogBodyDO> page = this.page(new Page<>(1, 1),
                Wrappers.lambdaQuery(DaDiscoveryLogBodyDO.class)
                        .eq(DaDiscoveryLogBodyDO::getTaskId, taskId)
                        .orderByDesc(DaDiscoveryLogBodyDO::getTm));
        if (page.getRecords().isEmpty()) {
            return null;
        }
        return page.getRecords().get(0).getLogContent();
    }

    @Override
    public String getLog(Long taskId) {
        DaDiscoveryLogBodyDO row = this.getOne(Wrappers.lambdaQuery(DaDiscoveryLogBodyDO.class)
                .eq(DaDiscoveryLogBodyDO::getTaskId, taskId));
        return row == null ? null : row.getLogContent();
    }

    /**
     * 发现任务日志写入（增量累积 + 会话结束一次性落库）
     * 1) 先把增量日志落到 Redis；
     * 2) 如果本次增量包含 FINALIZE_SESSION，则将累计全文写入 DA_DISCOVERY_LOG_BODY；
     * 3) 为兼容前端会话结束后短时间内查询，将最终日志再缓存 5 分钟。
     *
     * @param taskId  发现任务 ID（必填）
     * @param logStr  本次追加的增量日志
     */
    @Override
    public void taskLogAppend(Long taskId, String logStr) {
        // 1. 基本校验
        if (taskId == null || StringUtils.isBlank(logStr)) {
            return;
        }

        final String taskLogKey = DISCOVERY_TASK_LOG_KEY_PREFIX + taskId;

        // 2. 读取 Redis 既有日志（没有则置空串）
        String taskLog = redisService.get(taskLogKey);
        if (taskLog == null) {
            taskLog = "";
        }
        String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
        logStr = time + " - " + logStr+ "\n";

        // 3. 追加本次增量（若没有换行，以换行收尾）
        taskLog += logStr + (logStr.matches(".*\\r?\\n.*") ? "" : "\n");
        redisService.set(taskLogKey, taskLog);

        // 4. 如检测到会话结束标记，则一次性入库，并做 5 分钟缓存
        if (StringUtils.contains(logStr, FINALIZE_TOKEN)) {
            // 入库：复合主键 (taskId, tm)；tm 取当前时间
            DaDiscoveryLogBodyDO entity = DaDiscoveryLogBodyDO.builder()
                    .taskId(taskId)
                    .tm(new Date())
                    .logContent(taskLog)
                    .validFlag(Boolean.TRUE)
                    .delFlag(Boolean.FALSE)
                    .build();

            // 复用你之前实现的复合主键保存/更新语义
            this.saveOrUpdateByPk(entity);

            // 重置并短期缓存，便于前端“会话结束后”仍可查询
            redisService.delete(taskLogKey);
            redisService.set(taskLogKey, taskLog, 60 * 5);
        }
    }

    @Override
    public boolean deleteByPk(Long taskId) {
        if (taskId == null) {
            return false;
        }
        return this.remove(
                Wrappers.lambdaQuery(DaDiscoveryLogBodyDO.class)
                        .eq(DaDiscoveryLogBodyDO::getTaskId, taskId)
        );
    }
}
