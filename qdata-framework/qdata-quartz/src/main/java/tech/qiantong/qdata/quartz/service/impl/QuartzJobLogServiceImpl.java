/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 */

package tech.qiantong.qdata.quartz.service.impl;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.quartz.domain.SysJobLog;
import tech.qiantong.qdata.quartz.mapper.QuartzJobLogMapper;
import tech.qiantong.qdata.quartz.service.IQuartzJobLogService;

/**
 * Quartz 业务任务日志服务实现。
 */
@Service
public class QuartzJobLogServiceImpl implements IQuartzJobLogService
{
    private final QuartzJobLogMapper jobLogMapper;

    public QuartzJobLogServiceImpl(QuartzJobLogMapper jobLogMapper)
    {
        this.jobLogMapper = jobLogMapper;
    }

    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }
}
