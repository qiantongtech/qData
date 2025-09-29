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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlNodeLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeLogService;

/**
 * 数据集成节点-日志Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成节点-日志")
@RestController
@RequestMapping("/dpp/etlNodeLog")
@Validated
public class DppEtlNodeLogController extends BaseController {
    @Resource
    private IDppEtlNodeLogService dppEtlNodeLogService;

    @Operation(summary = "查询数据集成节点-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlNodeLogRespVO>> list(DppEtlNodeLogPageReqVO dppEtlNodeLog) {
        PageResult<DppEtlNodeLogDO> page = dppEtlNodeLogService.getDppEtlNodeLogPage(dppEtlNodeLog);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlNodeLogRespVO.class));
    }

    @Operation(summary = "导出数据集成节点-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:export')")
    @Log(title = "数据集成节点-日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlNodeLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlNodeLogDO> list = (List<DppEtlNodeLogDO>) dppEtlNodeLogService.getDppEtlNodeLogPage(exportReqVO).getRows();
        ExcelUtil<DppEtlNodeLogRespVO> util = new ExcelUtil<>(DppEtlNodeLogRespVO.class);
        util.exportExcel(response, DppEtlNodeLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成节点-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:import')")
    @Log(title = "数据集成节点-日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlNodeLogRespVO> util = new ExcelUtil<>(DppEtlNodeLogRespVO.class);
        List<DppEtlNodeLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlNodeLogService.importDppEtlNodeLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成节点-日志详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlNodeLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlNodeLogDO dppEtlNodeLogDO = dppEtlNodeLogService.getDppEtlNodeLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlNodeLogDO, DppEtlNodeLogRespVO.class));
    }

    @Operation(summary = "新增数据集成节点-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:add')")
    @Log(title = "数据集成节点-日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlNodeLogSaveReqVO dppEtlNodeLog) {
        dppEtlNodeLog.setCreatorId(getUserId());
        dppEtlNodeLog.setCreateBy(getNickName());
        dppEtlNodeLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeLogService.createDppEtlNodeLog(dppEtlNodeLog));
    }

    @Operation(summary = "修改数据集成节点-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:edit')")
    @Log(title = "数据集成节点-日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlNodeLogSaveReqVO dppEtlNodeLog) {
        dppEtlNodeLog.setUpdatorId(getUserId());
        dppEtlNodeLog.setUpdateBy(getNickName());
        dppEtlNodeLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeLogService.updateDppEtlNodeLog(dppEtlNodeLog));
    }

    @Operation(summary = "删除数据集成节点-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeLog:remove')")
    @Log(title = "数据集成节点-日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlNodeLogService.removeDppEtlNodeLog(Arrays.asList(ids)));
    }

}
