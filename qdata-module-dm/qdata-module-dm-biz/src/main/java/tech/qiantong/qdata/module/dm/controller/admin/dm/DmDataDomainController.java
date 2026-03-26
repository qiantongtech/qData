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

package tech.qiantong.qdata.module.dm.controller.admin.dm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.convert.dm.DmDataDomainConvert;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataDomainService;

/**
 * 数据域管理Controller
 *
 * @author FXB
 * @date 2026-03-24
 */
@Tag(name = "数据域管理")
@RestController
@RequestMapping("/dm/dataDomain")
@Validated
public class DmDataDomainController extends BaseController {
    @Resource
    private IDmDataDomainService dmDataDomainService;

    @Operation(summary = "查询数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DmDataDomainRespVO>> list(DmDataDomainPageReqVO dmDataDomain) {
        PageResult<DmDataDomainDO> page = dmDataDomainService.getDmDataDomainPage(dmDataDomain);
        return CommonResult.success(BeanUtils.toBean(page, DmDataDomainRespVO.class));
    }

    @Operation(summary = "导出数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:export')")
    @Log(title = "数据域管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DmDataDomainPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DmDataDomainDO> list = (List<DmDataDomainDO>) dmDataDomainService.getDmDataDomainPage(exportReqVO).getRows();
        ExcelUtil<DmDataDomainRespVO> util = new ExcelUtil<>(DmDataDomainRespVO.class);
        util.exportExcel(response, DmDataDomainConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据域管理列表")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:import')")
    @Log(title = "数据域管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DmDataDomainRespVO> util = new ExcelUtil<>(DmDataDomainRespVO.class);
        List<DmDataDomainRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dmDataDomainService.importDmDataDomain(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据域管理详细信息")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DmDataDomainRespVO> getInfo(@PathVariable("id") Long id) {
        DmDataDomainDO dmDataDomainDO = dmDataDomainService.getDmDataDomainById(id);
        return CommonResult.success(BeanUtils.toBean(dmDataDomainDO, DmDataDomainRespVO.class));
    }

    @Operation(summary = "新增数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:add')")
    @Log(title = "数据域管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DmDataDomainSaveReqVO dmDataDomain) {
        dmDataDomain.setCreatorId(getUserId());
        dmDataDomain.setCreateBy(getNickName());
        dmDataDomain.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataDomainService.createDmDataDomain(dmDataDomain));
    }

    @Operation(summary = "修改数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:edit')")
    @Log(title = "数据域管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DmDataDomainSaveReqVO dmDataDomain) {
        dmDataDomain.setUpdatorId(getUserId());
        dmDataDomain.setUpdateBy(getNickName());
        dmDataDomain.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dmDataDomainService.updateDmDataDomain(dmDataDomain));
    }

    @Operation(summary = "删除数据域管理")
    @PreAuthorize("@ss.hasPermi('dm:dataDomain:remove')")
    @Log(title = "数据域管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dmDataDomainService.removeDmDataDomain(Arrays.asList(ids)));
    }

}
