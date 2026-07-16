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

package tech.qiantong.qdata.module.dpp.service.etl.task;

import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskService;

/**
 * DPP 的 Quartz 调用入口。
 * Quartz 到点后只负责调用这里，真正的数据集成执行逻辑仍然交给 DPP 任务服务处理。
 */
@Component("dppQuartzJob")
public class DppQuartzJob {

    private final IDppEtlTaskService dppEtlTaskService;

    public DppQuartzJob(IDppEtlTaskService dppEtlTaskService) {
        this.dppEtlTaskService = dppEtlTaskService;
    }

    /**
     * 数据集成
     */
    public void dataIntegration(Long taskId) {
        try {
            dppEtlTaskService.startDppEtlTaskDataIntegration(taskId);
            CommonResult.success("任务id:" + taskId + "执行成功");
        } catch (NumberFormatException e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "任务ID格式错误：" + taskId);
        } catch (Exception e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "任务执行失败：" + e.getMessage());
        }
    }

    /**
     * 数据开发
     */
    public void dataDevelopment(Long id) {
        try {
            dppEtlTaskService.startDppEtlTaskDataDevelopment(id);
            CommonResult.success("任务id:" + id + "执行成功");
        } catch (NumberFormatException e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "任务ID格式错误：" + id);
        } catch (Exception e) {
            CommonResult.error(GlobalErrorCodeConstants.ERROR.getCode(), "任务执行失败：" + e.getMessage());
        }
    }
}
