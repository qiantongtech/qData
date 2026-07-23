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
 * Data access layer for Quartz business task logs.
 */
public interface QuartzJobLogMapper
{
    /**
     * Inserts a business task execution log.
     *
     * @param jobLog scheduling log information
     * @return the number of affected rows
     */
    int insertJobLog(SysJobLog jobLog);
}
