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

package tech.qiantong.qdata.quartz.config;

import org.quartz.Scheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.quartz.domain.QuartzJob;
import tech.qiantong.qdata.quartz.domain.SysJob;
import tech.qiantong.qdata.quartz.mapper.QuartzJobMapper;
import tech.qiantong.qdata.quartz.mapper.SysJobMapper;
import tech.qiantong.qdata.quartz.util.ScheduleUtils;

import java.util.List;

import static tech.qiantong.qdata.common.constant.ScheduleConstants.QUARTZ_JOB_NAMESPACE;

/**
 * 统一恢复 Quartz、SysJob 调度任务，确保系统任务和业务任务按固定顺序加载。
 */
@Component
public class JobInitializer implements ApplicationRunner {

    private final Scheduler scheduler;
    private final SysJobMapper sysJobMapper;
    private final QuartzJobMapper quartzJobMapper;

    public JobInitializer(Scheduler scheduler,
                          SysJobMapper sysJobMapper,
                          QuartzJobMapper quartzJobMapper) {
        this.scheduler = scheduler;
        this.sysJobMapper = sysJobMapper;
        this.quartzJobMapper = quartzJobMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduler.clear();
        loadSystemJobs();
        loadBusinessJobs();
    }

    private void loadSystemJobs() throws Exception {
        List<SysJob> jobList = sysJobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    private void loadBusinessJobs() throws Exception {
        List<QuartzJob> jobList = quartzJobMapper.selectJobAll();
        for (QuartzJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job, QUARTZ_JOB_NAMESPACE);
        }
    }
}
