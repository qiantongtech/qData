/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Data Discovery Node Instance - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-10-15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryLogBodyServiceImpl extends ServiceImpl<DaDiscoveryLogBodyMapper, DaDiscoveryLogBodyDO> implements IDaDiscoveryLogBodyService {

    // Can be placed in a constants class, inlined here for now
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
     * Discovery task log writing (incremental accumulation + session-end batch write to DB)
     * 1) First write the incremental log to Redis;
     * 2) If the current chunk contains FINALIZE_SESSION, write the full accumulated text to DA_DISCOVERY_LOG_BODY;
     * 3) For compatibility with frontend querying shortly after session ends, cache the final log for 5 minutes.
     *
     * @param taskId  Discovery task ID (required)
     * @param logStr  Incremental log chunk to append this time
     */
    @Override
    public void taskLogAppend(Long taskId, String logStr) {
        // 1. Basic validation
        if (taskId == null || StringUtils.isBlank(logStr)) {
            return;
        }

        final String taskLogKey = DISCOVERY_TASK_LOG_KEY_PREFIX + taskId;

        // 2. Read existing Redis log (empty string if none)
        String taskLog = redisService.get(taskLogKey);
        if (taskLog == null) {
            taskLog = “”;
        }
        String time = DateUtil.format(new Date(), “yyyy-MM-dd HH:mm:ss.SSS”);
        logStr = time + “ - “ + logStr+ “\n”;

        // 3. Append this chunk (add trailing newline if missing)
        taskLog += logStr + (logStr.matches(“.*\\r?\\n.*”) ? “” : “\n”);
        redisService.set(taskLogKey, taskLog);

        // 4. If session end marker detected, write to DB and cache for 5 minutes
        if (StringUtils.contains(logStr, FINALIZE_TOKEN)) {
            // Write to DB: composite primary key (taskId, tm); tm takes current time
            DaDiscoveryLogBodyDO entity = DaDiscoveryLogBodyDO.builder()
                    .taskId(taskId)
                    .tm(new Date())
                    .logContent(taskLog)
                    .validFlag(Boolean.TRUE)
                    .delFlag(Boolean.FALSE)
                    .build();

            // Reuse the previously implemented composite key save-or-update semantics
            this.saveOrUpdateByPk(entity);

            // Reset and short-term cache, so frontend can still query after session ends
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
