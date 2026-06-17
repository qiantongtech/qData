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

package tech.qiantong.qdata.module.att.controller.admin.sourceSystem;

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
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.convert.sourceSystem.AttSourceSystemConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 来源系统Controller
 *
 * @author qdata
 * @date 2026-04-03
 */
@Tag(name = "来源系统")
@RestController
@RequestMapping("/att/sourceSystem")
@Validated
public class AttSourceSystemController extends BaseController {
    @Resource
    private IAttSourceSystemService attSourceSystemService;

    @Operation(summary = "查询来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttSourceSystemRespVO>> list(AttSourceSystemPageReqVO attSourceSystem) {
        PageResult<AttSourceSystemDO> page = attSourceSystemService.getAttSourceSystemPage(attSourceSystem);
        return CommonResult.success(BeanUtils.toBean(page, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "查询来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:list')")
    @GetMapping("/listValid")
    public CommonResult<List<AttSourceSystemRespVO>> list() {
        List<AttSourceSystemDO> attSourceSystemList = attSourceSystemService.getAttSourceSystemListByValidFlag(true);
        return CommonResult.success(BeanUtils.toBean(attSourceSystemList, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "导出来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:export')")
    @Log(title = "来源系统", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttSourceSystemPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttSourceSystemDO> list = (List<AttSourceSystemDO>) attSourceSystemService.getAttSourceSystemPage(exportReqVO).getRows();
        ExcelUtil<AttSourceSystemRespVO> util = new ExcelUtil<>(AttSourceSystemRespVO.class);
        util.exportExcel(response, AttSourceSystemConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:import')")
    @Log(title = "来源系统", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttSourceSystemRespVO> util = new ExcelUtil<>(AttSourceSystemRespVO.class);
        List<AttSourceSystemRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attSourceSystemService.importAttSourceSystem(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取来源系统详细信息")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttSourceSystemRespVO> getInfo(@PathVariable("id") Long id) {
        AttSourceSystemDO attSourceSystemDO = attSourceSystemService.getAttSourceSystemById(id);
        return CommonResult.success(BeanUtils.toBean(attSourceSystemDO, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "新增来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:add')")
    @Log(title = "来源系统", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttSourceSystemSaveReqVO attSourceSystem) {
        attSourceSystem.setCreatorId(getUserId());
        attSourceSystem.setCreateBy(getNickName());
        attSourceSystem.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attSourceSystemService.createAttSourceSystem(attSourceSystem));
    }

    @Operation(summary = "修改来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:edit')")
    @Log(title = "来源系统", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttSourceSystemSaveReqVO attSourceSystem) {
        attSourceSystem.setUpdatorId(getUserId());
        attSourceSystem.setUpdateBy(getNickName());
        attSourceSystem.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attSourceSystemService.updateAttSourceSystem(attSourceSystem));
    }

    @Operation(summary = "删除来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:remove')")
    @Log(title = "来源系统", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attSourceSystemService.removeAttSourceSystem(Arrays.asList(ids)));
    }

}
