package tech.qiantong.qdata.module.dpp.controller.admin.etl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlTaskLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskLogService;

/**
 * 数据集成任务-日志Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成任务-日志")
@RestController
@RequestMapping("/dpp/etlTaskLog")
@Validated
public class DppEtlTaskLogController extends BaseController {
    @Resource
    private IDppEtlTaskLogService dppEtlTaskLogService;

    @Operation(summary = "查询数据集成任务-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlTaskLogRespVO>> list(DppEtlTaskLogPageReqVO dppEtlTaskLog) {
        PageResult<DppEtlTaskLogDO> page = dppEtlTaskLogService.getDppEtlTaskLogPage(dppEtlTaskLog);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlTaskLogRespVO.class));
    }

    @Operation(summary = "导出数据集成任务-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:export')")
    @Log(title = "数据集成任务-日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlTaskLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlTaskLogDO> list = (List<DppEtlTaskLogDO>) dppEtlTaskLogService.getDppEtlTaskLogPage(exportReqVO).getRows();
        ExcelUtil<DppEtlTaskLogRespVO> util = new ExcelUtil<>(DppEtlTaskLogRespVO.class);
        util.exportExcel(response, DppEtlTaskLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成任务-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:import')")
    @Log(title = "数据集成任务-日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlTaskLogRespVO> util = new ExcelUtil<>(DppEtlTaskLogRespVO.class);
        List<DppEtlTaskLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlTaskLogService.importDppEtlTaskLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成任务-日志详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlTaskLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlTaskLogDO dppEtlTaskLogDO = dppEtlTaskLogService.getDppEtlTaskLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlTaskLogDO, DppEtlTaskLogRespVO.class));
    }

    @Operation(summary = "新增数据集成任务-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:add')")
    @Log(title = "数据集成任务-日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlTaskLogSaveReqVO dppEtlTaskLog) {
        dppEtlTaskLog.setCreatorId(getUserId());
        dppEtlTaskLog.setCreateBy(getNickName());
        dppEtlTaskLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskLogService.createDppEtlTaskLog(dppEtlTaskLog));
    }

    @Operation(summary = "修改数据集成任务-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:edit')")
    @Log(title = "数据集成任务-日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlTaskLogSaveReqVO dppEtlTaskLog) {
        dppEtlTaskLog.setUpdatorId(getUserId());
        dppEtlTaskLog.setUpdateBy(getNickName());
        dppEtlTaskLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskLogService.updateDppEtlTaskLog(dppEtlTaskLog));
    }

    @Operation(summary = "删除数据集成任务-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskLog:remove')")
    @Log(title = "数据集成任务-日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlTaskLogService.removeDppEtlTaskLog(Arrays.asList(ids)));
    }

}
