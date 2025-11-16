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

package tech.qiantong.qdata.module.dpp.controller.admin.qa;

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
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.qa.DppQualityTaskObjConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskObjService;

/**
 * 数据质量任务-稽查对象Controller
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Tag(name = "数据质量任务-稽查对象")
@RestController
@RequestMapping("/dpp/qualityTaskObj")
@Validated
public class DppQualityTaskObjController extends BaseController {
    @Resource
    private IDppQualityTaskObjService dppQualityTaskObjService;

    @Operation(summary = "查询数据质量任务-稽查对象列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityTaskObjRespVO>> list(DppQualityTaskObjPageReqVO dppQualityTaskObj) {
        PageResult<DppQualityTaskObjDO> page = dppQualityTaskObjService.getDppQualityTaskObjPage(dppQualityTaskObj);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityTaskObjRespVO.class));
    }

    @Operation(summary = "导出数据质量任务-稽查对象列表")
    @Log(title = "数据质量任务-稽查对象", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityTaskObjPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityTaskObjDO> list = (List<DppQualityTaskObjDO>) dppQualityTaskObjService.getDppQualityTaskObjPage(exportReqVO).getRows();
        ExcelUtil<DppQualityTaskObjRespVO> util = new ExcelUtil<>(DppQualityTaskObjRespVO.class);
        util.exportExcel(response, DppQualityTaskObjConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量任务-稽查对象列表")
    @Log(title = "数据质量任务-稽查对象", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityTaskObjRespVO> util = new ExcelUtil<>(DppQualityTaskObjRespVO.class);
        List<DppQualityTaskObjRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityTaskObjService.importDppQualityTaskObj(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量任务-稽查对象详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityTaskObjRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityTaskObjDO dppQualityTaskObjDO = dppQualityTaskObjService.getDppQualityTaskObjById(id);
        return CommonResult.success(BeanUtils.toBean(dppQualityTaskObjDO, DppQualityTaskObjRespVO.class));
    }

    @Operation(summary = "新增数据质量任务-稽查对象")
    @Log(title = "数据质量任务-稽查对象", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityTaskObjSaveReqVO dppQualityTaskObj) {
        dppQualityTaskObj.setCreatorId(getUserId());
        dppQualityTaskObj.setCreateBy(getNickName());
        dppQualityTaskObj.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskObjService.createDppQualityTaskObj(dppQualityTaskObj));
    }

    @Operation(summary = "修改数据质量任务-稽查对象")
    @Log(title = "数据质量任务-稽查对象", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityTaskObjSaveReqVO dppQualityTaskObj) {
        dppQualityTaskObj.setUpdatorId(getUserId());
        dppQualityTaskObj.setUpdateBy(getNickName());
        dppQualityTaskObj.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskObjService.updateDppQualityTaskObj(dppQualityTaskObj));
    }

    @Operation(summary = "删除数据质量任务-稽查对象")
    @Log(title = "数据质量任务-稽查对象", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityTaskObjService.removeDppQualityTaskObj(Arrays.asList(ids)));
    }

}
