package tech.qiantong.qdata.module.dpp.controller.admin.etl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEvaluateLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEvaluateLogService;

/**
 * 评测规则结果Controller
 *
 * @author qdata
 * @date 2025-07-21
 */
@Tag(name = "评测规则结果")
@RestController
@RequestMapping("/dpp/evaluateLog")
@Validated
public class DppEvaluateLogController extends BaseController {
    @Resource
    private IDppEvaluateLogService dppEvaluateLogService;

    @Operation(summary = "查询评测规则结果列表")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEvaluateLogRespVO>> list(DppEvaluateLogPageReqVO dppEvaluateLog) {
        PageResult<DppEvaluateLogDO> page = dppEvaluateLogService.getDppEvaluateLogPage(dppEvaluateLog);
        return CommonResult.success(BeanUtils.toBean(page, DppEvaluateLogRespVO.class));
    }

    @Operation(summary = "导出评测规则结果列表")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:export')")
    @Log(title = "评测规则结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEvaluateLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEvaluateLogDO> list = (List<DppEvaluateLogDO>) dppEvaluateLogService.getDppEvaluateLogPage(exportReqVO).getRows();
        ExcelUtil<DppEvaluateLogRespVO> util = new ExcelUtil<>(DppEvaluateLogRespVO.class);
        util.exportExcel(response, DppEvaluateLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入评测规则结果列表")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:import')")
    @Log(title = "评测规则结果", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEvaluateLogRespVO> util = new ExcelUtil<>(DppEvaluateLogRespVO.class);
        List<DppEvaluateLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEvaluateLogService.importDppEvaluateLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取评测规则结果详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEvaluateLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppEvaluateLogDO dppEvaluateLogDO = dppEvaluateLogService.getDppEvaluateLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppEvaluateLogDO, DppEvaluateLogRespVO.class));
    }

    @Operation(summary = "新增评测规则结果")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:add')")
    @Log(title = "评测规则结果", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEvaluateLogSaveReqVO dppEvaluateLog) {
        dppEvaluateLog.setCreatorId(getUserId());
        dppEvaluateLog.setCreateBy(getNickName());
        dppEvaluateLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEvaluateLogService.createDppEvaluateLog(dppEvaluateLog));
    }

    @Operation(summary = "修改评测规则结果")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:edit')")
    @Log(title = "评测规则结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEvaluateLogSaveReqVO dppEvaluateLog) {
        dppEvaluateLog.setUpdatorId(getUserId());
        dppEvaluateLog.setUpdateBy(getNickName());
        dppEvaluateLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEvaluateLogService.updateDppEvaluateLog(dppEvaluateLog));
    }

    @Operation(summary = "删除评测规则结果")
//    @PreAuthorize("@ss.hasPermi('dpp:evaluateLog:remove')")
    @Log(title = "评测规则结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEvaluateLogService.removeDppEvaluateLog(Arrays.asList(ids)));
    }

    @Operation(summary = "统计评测规则结果")
    @GetMapping("/statisticsEvaluateOne/{id}")
    public CommonResult<List<DppEvaluateLogStatisticsVO>> statisticsEvaluateOne(@PathVariable Long id) {
        return CommonResult.success(dppEvaluateLogService.statisticsEvaluateOne(id));
    }


    @Operation(summary = "统计评测规则结果")
    @GetMapping("/statisticsEvaluateTow")
    public CommonResult<JSONObject> statisticsEvaluateTow( Long id , Date deDate , Date oldDate , int type) {
        return CommonResult.success(dppEvaluateLogService.statisticsEvaluateTow(id , deDate , oldDate , type));
    }

    @Operation(summary = "统计评测规则结果")
    @GetMapping("/statisticsEvaluateTable/{id}")
    public CommonResult<List<DppEvaluateLogRespVO>> statisticsEvaluateTable(@PathVariable Long id) {
        return CommonResult.success(dppEvaluateLogService.statisticsEvaluateTable(id));
    }

    @Operation(summary = "统计评测规则结果")
    @GetMapping("/pageErrorData")
    public CommonResult<JSONObject> pageErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO) {
        return CommonResult.success(dppEvaluateLogService.pageErrorData(checkErrorDataReqDTO));
    }
    @Operation(summary = "修改错误数据")
    @PostMapping("/updateErrorData")
    public CommonResult<Boolean> updateErrorData(@RequestBody CheckErrorDataReqDTO checkErrorDataReqDTO) {
        boolean success = dppEvaluateLogService.updateErrorData(checkErrorDataReqDTO);
        return CommonResult.success(success);
    }
}
