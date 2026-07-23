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

package tech.qiantong.qdata.module.mc.controller.admin.job;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;

import javax.annotation.Resource;

/**
 * DPP 的 Quartz 调用入口。
 * Quartz 到点后只负责调用这里，真正的数据集成执行逻辑仍然交给 DPP 任务服务处理。
 */
@Component("mcTaskExecutorJob")
public class McTaskExecutorJob {
    @Resource
    private IMcTaskService mcTaskService;

    /**
     * Quartz
     * 回调执行采集任务
     *
     * @param id 任务ID
     * @return 执行结果
     */
    public void runExecuteTask(Long id) {
        try {
            mcTaskService.runDaDiscoveryTask(id);
            CommonResult.success("任务id:" + id + "执行成功");
        } catch (NumberFormatException e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "Invalid task ID format: " + id);
        } catch (Exception e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "Task execution failed: " + e.getMessage());
        }
    }
}
