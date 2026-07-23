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

package tech.qiantong.qdata.ai.controller.admin.model;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelRespVO;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;
import tech.qiantong.qdata.module.ai.service.model.IAiModelService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Model ManagementController
 *
 * @author FXB
 * @date 2026-04-01
 */
@Tag(name = "模型")
@RestController
@RequestMapping("/model")
@Validated
public class ModelController extends BaseController {
    @Resource
    private IAiModelService aiModelService;

    @Operation(summary = "模型列表")
    @GetMapping("/list")
    public CommonResult<List<AiModelRespVO>> list(AiModelPageReqVO aiModel) {
        List<AiModelDO> aiModelList = aiModelService.list(Wrappers.lambdaQuery(AiModelDO.class)
                .select(AiModelDO::getId, AiModelDO::getName, AiModelDO::getPlatform, AiModelDO::getSortOrder)
                .eq(AiModelDO::getValidFlag, true)
                .orderByAsc(AiModelDO::getSortOrder));
        return CommonResult.success(BeanUtils.toBean(aiModelList, AiModelRespVO.class));
    }
}
