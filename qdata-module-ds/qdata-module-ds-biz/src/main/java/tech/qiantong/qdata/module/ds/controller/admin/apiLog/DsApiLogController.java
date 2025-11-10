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

package tech.qiantong.qdata.module.ds.controller.admin.apiLog;

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
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.convert.apiLog.DsApiLogConvert;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.service.apiLog.IDsApiLogService;

/**
 * API服务调用日志Controller
 *
 * @author lhs
 * @date 2025-02-12
 */
@Tag(name = "API服务调用日志")
@RestController
@RequestMapping("/ds/apiLog")
@Validated
public class DsApiLogController extends BaseController {
    @Resource
    private IDsApiLogService dsApiLogService;

    @Operation(summary = "查询API服务调用日志列表")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DsApiLogRespVO>> list(DsApiLogPageReqVO dsApiLog) {
        PageResult<DsApiLogDO> page = dsApiLogService.getDsApiLogPage(dsApiLog);
        return CommonResult.success(BeanUtils.toBean(page, DsApiLogRespVO.class));
    }

    @Operation(summary = "导出API服务调用日志列表")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:export')")
    @Log(title = "API服务调用日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DsApiLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DsApiLogDO> list = (List<DsApiLogDO>) dsApiLogService.getDsApiLogPage(exportReqVO).getRows();
        ExcelUtil<DsApiLogRespVO> util = new ExcelUtil<>(DsApiLogRespVO.class);
        util.exportExcel(response, DsApiLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入API服务调用日志列表")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:import')")
    @Log(title = "API服务调用日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DsApiLogRespVO> util = new ExcelUtil<>(DsApiLogRespVO.class);
        List<DsApiLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dsApiLogService.importDsApiLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取API服务调用日志详细信息")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<DsApiLogRespVO> getInfo(@PathVariable("ID") Long ID) {
        DsApiLogDO dsApiLogDO = dsApiLogService.getDsApiLogById(ID);
        if(dsApiLogDO == null){
            dsApiLogDO = new DsApiLogDO();
        }
        return CommonResult.success(BeanUtils.toBean(dsApiLogDO, DsApiLogRespVO.class));
    }

    @Operation(summary = "新增API服务调用日志")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:add')")
    @Log(title = "API服务调用日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DsApiLogSaveReqVO dsApiLog) {
        dsApiLog.setCreatorId(getUserId());
        dsApiLog.setCreateBy(getNickName());
        dsApiLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dsApiLogService.createDsApiLog(dsApiLog));
    }

    @Operation(summary = "修改API服务调用日志")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:edit')")
    @Log(title = "API服务调用日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DsApiLogSaveReqVO dsApiLog) {
        dsApiLog.setUpdatorId(getUserId());
        dsApiLog.setUpdateBy(getNickName());
        dsApiLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dsApiLogService.updateDsApiLog(dsApiLog));
    }

    @Operation(summary = "删除API服务调用日志")
    @PreAuthorize("@ss.hasPermi('ds:apiLog:remove')")
    @Log(title = "API服务调用日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public CommonResult<Integer> remove(@PathVariable Long[] id) {
        return CommonResult.toAjax(dsApiLogService.removeDsApiLog(Arrays.asList(id)));
    }

}
