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

package tech.qiantong.qdata.module.att.controller.admin.tag;

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
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.convert.Tag.AttTagConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 标签管理Controller
 *
 * @author qdata
 * @date 2025-07-11
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("/att/tag")
@Validated
public class AttTagController extends BaseController {
    @Resource
    private IAttTagService attTagService;

    @Operation(summary = "查询标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttTagRespVO>> list(AttTagPageReqVO attTag) {
        PageResult<AttTagDO> page = attTagService.getAttTagPage(attTag);
        return CommonResult.success(BeanUtils.toBean(page, AttTagRespVO.class));
    }

    @Operation(summary = "查询标签管理列表")
    @GetMapping("/listDict")
    public CommonResult<List<AttTagRespVO>> list() {
        List<AttTagDO> attTagList = attTagService.getAttTagList();
        return CommonResult.success(BeanUtils.toBean(attTagList, AttTagRespVO.class));
    }

    @Operation(summary = "导出标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:export')")
    @Log(title = "标签管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTagPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTagDO> list = (List<AttTagDO>) attTagService.getAttTagPage(exportReqVO).getRows();
        ExcelUtil<AttTagRespVO> util = new ExcelUtil<>(AttTagRespVO.class);
        util.exportExcel(response, AttTagConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入标签管理列表")
    @PreAuthorize("@ss.hasPermi('att:tag:import')")
    @Log(title = "标签管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttTagRespVO> util = new ExcelUtil<>(AttTagRespVO.class);
        List<AttTagRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attTagService.importAttTag(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取标签管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:tag:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttTagRespVO> getInfo(@PathVariable("id") Long id) {
        AttTagRespVO attTagDO = attTagService.getAttTagById(id);
        return CommonResult.success(attTagDO);
    }

    @Operation(summary = "新增标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:add')")
    @Log(title = "标签管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTagSaveReqVO attTag) {
        attTag.setCreatorId(getUserId());
        attTag.setCreateBy(getNickName());
        attTag.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTagService.createAttTag(attTag));
    }

    @Operation(summary = "修改标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:edit')")
    @Log(title = "标签管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTagSaveReqVO attTag) {
        attTag.setUpdatorId(getUserId());
        attTag.setUpdateBy(getNickName());
        attTag.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTagService.updateAttTag(attTag));
    }

    @Operation(summary = "删除标签管理")
    @PreAuthorize("@ss.hasPermi('att:tag:remove')")
    @Log(title = "标签管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attTagService.removeAttTag(Arrays.asList(ids)));
    }

}
