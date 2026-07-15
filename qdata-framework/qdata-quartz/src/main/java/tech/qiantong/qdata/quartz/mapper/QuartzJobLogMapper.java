/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 */

package tech.qiantong.qdata.quartz.mapper;

import tech.qiantong.qdata.quartz.domain.SysJobLog;

/**
 * Quartz 业务任务日志数据层。
 */
public interface QuartzJobLogMapper
{
    /**
     * 新增业务任务执行日志。
     *
     * @param jobLog 调度日志信息
     * @return 影响行数
     */
    int insertJobLog(SysJobLog jobLog);
}
