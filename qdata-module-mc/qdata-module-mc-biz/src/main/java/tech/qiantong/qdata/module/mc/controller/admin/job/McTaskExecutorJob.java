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
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;

import javax.annotation.Resource;

/**
 * Quartz entry point for DPP.
 * Quartz invokes this endpoint when triggered, while the DPP task service performs the actual data integration.
 */
@Component("mcTaskExecutorJob")
public class McTaskExecutorJob {
    @Resource
    private IMcTaskService mcTaskService;

    /**
     * Quartz
     * Executes a collection task through the callback.
     *
     * @param id task ID
     * @return execution result
     */
    public void runExecuteTask(Long id) {
        try {
            mcTaskService.runDaDiscoveryTask(id);
            CommonResult.success(MessageUtils.messageWithFallback(
                    "mc.job.task.execute.success", "Task {0} executed successfully", id));
        } catch (NumberFormatException e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), MessageUtils.messageWithFallback(
                    "mc.job.task.id.invalid", "Invalid task ID format: {0}", id));
        } catch (Exception e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), MessageUtils.messageWithFallback(
                    "mc.job.task.execute.fail", "Task execution failed: {0}", e.getMessage()));
        }
    }
}
