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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.controller.admin.task;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;

import javax.annotation.Resource;

/**
 * Collection task executorController
 * Used for DolphinScheduler callback to perform collection tasks
 *
 * @author qdata
 * @date 2026-05-11
 */
@Tag(name = "采集任务执行器")
@RestController
@RequestMapping("/mc/taskExecutor")
@Validated
public class McTaskExecutorController extends BaseController {

    @Resource
    private IMcTaskService mcTaskService;

    /**
     * DolphinScheduler callback to perform collection tasks
     *
     * @param id task ID
     * @return execution result
     */
    @PutMapping("/runExecuteTask/{id}")
    public CommonResult<String> runExecuteTask(@PathVariable("id") Long id) {
        try {
            mcTaskService.runDaDiscoveryTask(id);
            return CommonResult.success(MessageUtils.message("mc.task.execute.success", id));
        } catch (NumberFormatException e) {
            return CommonResult.error( GlobalErrorCodeConstants.ERROR.getCode(), MessageUtils.message("mc.task.execute.id.format.error", id));
        } catch (Exception e) {
            return CommonResult.error( GlobalErrorCodeConstants.ERROR.getCode(), MessageUtils.message("mc.task.execute.fail", e.getMessage()));
        }
    }
}
