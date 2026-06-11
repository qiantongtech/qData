/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
 * 模型管理Controller
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
