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
    @Log(title = "模型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        aiModel.setCreatorId(getUserId());
        aiModel.setCreateBy(getNickName());
        aiModel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(aiModelService.createAiModel(aiModel));
    }

    @Operation(summary = "修改模型管理")
    @PreAuthorize("@ss.hasPermi('ai:model:edit')")
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AiModelSaveReqVO aiModel) {
        aiModel.setUpdatorId(getUserId());
        aiModel.setUpdateBy(getNickName());
        aiModel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(aiModelService.updateAiModel(aiModel));
    }

    @Operation(summary = "删除模型管理")
    @PreAuthorize("@ss.hasPermi('ai:model:remove')")
    @Log(title = "模型管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(aiModelService.removeAiModel(Arrays.asList(ids)));
    }

}
