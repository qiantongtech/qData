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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.controller.admin.rule;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.convert.rule.AttCleanRuleConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;
import tech.qiantong.qdata.module.att.service.rule.IAttCleanRuleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 清洗规则Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "清洗规则")
@RestController
@RequestMapping("/att/cleanRule")
@Validated
public class AttCleanRuleController extends BaseController {
    @Resource
    private IAttCleanRuleService attCleanRuleService;

    @Operation(summary = "查询清洗规则列表")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttCleanRuleRespVO>> list(AttCleanRulePageReqVO attCleanRule) {
        PageResult<AttCleanRuleDO> page = attCleanRuleService.getAttCleanRulePage(attCleanRule);
        return CommonResult.success(BeanUtils.toBean(page, AttCleanRuleRespVO.class));
    }

    @Operation(summary = "查询清洗规则列表")
    @GetMapping("/listAll")
    public CommonResult<List<AttCleanRuleRespVO>> listAll(AttCleanRulePageReqVO attCleanRule) {
        List<AttCleanRuleRespVO> page = attCleanRuleService.getAttCleanRuleList(attCleanRule);
        return CommonResult.success(page);
    }

    @Operation(summary = "导出清洗规则列表")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:export')")
    @Log(title = "清洗规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttCleanRulePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttCleanRuleDO> list = (List<AttCleanRuleDO>) attCleanRuleService.getAttCleanRulePage(exportReqVO)
                .getRows();
        ExcelUtil<AttCleanRuleRespVO> util = new ExcelUtil<>(AttCleanRuleRespVO.class);
        util.exportExcel(response, AttCleanRuleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入清洗规则列表")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:import')")
    @Log(title = "清洗规则", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttCleanRuleRespVO> util = new ExcelUtil<>(AttCleanRuleRespVO.class);
        List<AttCleanRuleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attCleanRuleService.importAttCleanRule(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取清洗规则详细信息")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttCleanRuleRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttCleanRuleDO attCleanRuleDO = attCleanRuleService.getAttCleanRuleById(ID);
        return CommonResult.success(BeanUtils.toBean(attCleanRuleDO, AttCleanRuleRespVO.class));
    }

    @Operation(summary = "新增清洗规则")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:add')")
    @Log(title = "清洗规则", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttCleanRuleSaveReqVO attCleanRule) {
        attCleanRule.setCreatorId(getUserId());
        attCleanRule.setCreateBy(getNickName());
        attCleanRule.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attCleanRuleService.createAttCleanRule(attCleanRule));
    }

    @Operation(summary = "修改清洗规则")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:edit')")
    @Log(title = "清洗规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttCleanRuleSaveReqVO attCleanRule) {
        attCleanRule.setUpdatorId(getUserId());
        attCleanRule.setUpdateBy(getNickName());
        attCleanRule.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attCleanRuleService.updateAttCleanRule(attCleanRule));
    }

    @Operation(summary = "删除清洗规则")
    @PreAuthorize("@ss.hasPermi('att:cleanRule:remove')")
    @Log(title = "清洗规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attCleanRuleService.removeAttCleanRule(Arrays.asList(ids)));
    }

    @Operation(summary = "获取清洗规则树形结构")
    @GetMapping("/tree")
    public CommonResult<List<AttCleanRuleRespVO>> tree(@RequestParam Long dataElemId) {
        List<AttCleanRuleRespVO> tree = attCleanRuleService.getAttCleanRuleTree(dataElemId);
        return CommonResult.success(tree);
    }

    @Operation(summary = "获取清洗规则树形结构")
    @GetMapping("/getCleaningRuleTree")
    public CommonResult<List<AttCleanRuleRespVO>> getCleaningRuleTree(@RequestParam(name = "ids", required = false) Long[] ids) {
        List<AttCleanRuleRespVO> tree = attCleanRuleService.getCleaningRuleTree(ids);
        return CommonResult.success(tree);
    }

}
