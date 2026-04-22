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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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

import jakarta.annotation.Resource;
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
