/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 */

package tech.qiantong.qdata.quartz.service;

import tech.qiantong.qdata.quartz.domain.SysJobLog;

/**
 * Service for Quartz business task logs.
 */
public interface IQuartzJobLogService
{
    /**
     * Inserts a business task execution log.
     *
     * @param jobLog scheduling log information
     */
    void addJobLog(SysJobLog jobLog);
}
