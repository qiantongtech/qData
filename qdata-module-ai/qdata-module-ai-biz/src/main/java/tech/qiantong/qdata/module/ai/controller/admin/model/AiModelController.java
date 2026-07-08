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

package tech.qiantong.qdata.module.ai.controller.admin.model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

import cn.hutool.core.date.DateUtil;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelPageReqVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelRespVO;
import tech.qiantong.qdata.module.ai.controller.admin.model.vo.AiModelSaveReqVO;
import tech.qiantong.qdata.module.ai.convert.model.AiModelConvert;
import tech.qiantong.qdata.module.ai.dal.dataobject.model.AiModelDO;
import tech.qiantong.qdata.module.ai.service.model.IAiModelService;

/**
 * 模型管理Controller
 *
 * @author FXB
 * @date 2026-04-01
 */
@Tag(name = "模型管理")
@RestController
@RequestMapping("/ai/model")
@Validated
public class AiModelController extends BaseController {
    @Resource
    private IAiModelService aiModelService;

    @Operation(summary = "查询模型管理列表")
    @PreAuthorize("@ss.hasPermi('ai:model:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AiModelRespVO>> list(AiModelPageReqVO aiModel) {
        PageResult<AiModelDO> page = aiModelService.getAiModelPage(aiModel);
        return CommonResult.success(BeanUtils.toBean(page, AiModelRespVO.class));
    }


    @Operation(summary = "查询启用模型管理列表")
    @GetMapping("/enableList")
    public CommonResult<List<AiModelRespVO>> enableList(AiModelPageReqVO aiModel) {
        List<AiModelDO> aiModelList = aiModelService.list(Wrappers.lambdaQuery(AiModelDO.class)
                .eq(AiModelDO::getValidFlag, true));
        return CommonResult.success(BeanUtils.toBean(aiModelList, AiModelRespVO.class));
    }


    @Operation(summary = "获取模型管理详细信息")
    @PreAuthorize("@ss.hasPermi('ai:model:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AiModelRespVO> getInfo(@PathVariable("id") Long id) {
        AiModelDO aiModelDO = aiModelService.getAiModelById(id);
        return CommonResult.success(BeanUtils.toBean(aiModelDO, AiModelRespVO.class));
    }

    @Operation(summary = "新增模型管理")
    @PreAuthorize("@ss.hasPermi('ai:model:add')")
    @Log(title = "log.op.title.ai.model", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        aiModel.setCreatorId(getUserId());
        aiModel.setCreateBy(getNickName());
        aiModel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(aiModelService.createAiModel(aiModel));
    }

    @Operation(summary = "修改模型管理")
    @PreAuthorize("@ss.hasPermi('ai:model:edit')")
    @Log(title = "log.op.title.ai.model", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        aiModel.setUpdatorId(getUserId());
        aiModel.setUpdateBy(getNickName());
        aiModel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiModelService.updateAiModel(aiModel));
    }

    @Operation(summary = "删除模型管理")
    @PreAuthorize("@ss.hasPermi('ai:model:remove')")
    @Log(title = "log.op.title.ai.model", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(aiModelService.removeAiModel(Arrays.asList(ids)));
    }

}
