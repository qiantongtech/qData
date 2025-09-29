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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlTaskNodeRelLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskNodeRelLogService;

/**
 * 数据集成任务节点关系-日志Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成任务节点关系-日志")
@RestController
@RequestMapping("/dpp/etlTaskNodeRelLog")
@Validated
public class DppEtlTaskNodeRelLogController extends BaseController {
    @Resource
    private IDppEtlTaskNodeRelLogService dppEtlTaskNodeRelLogService;

    @Operation(summary = "查询数据集成任务节点关系-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlTaskNodeRelLogRespVO>> list(DppEtlTaskNodeRelLogPageReqVO dppEtlTaskNodeRelLog) {
        PageResult<DppEtlTaskNodeRelLogDO> page = dppEtlTaskNodeRelLogService.getDppEtlTaskNodeRelLogPage(dppEtlTaskNodeRelLog);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlTaskNodeRelLogRespVO.class));
    }

    @Operation(summary = "导出数据集成任务节点关系-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:export')")
    @Log(title = "数据集成任务节点关系-日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlTaskNodeRelLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlTaskNodeRelLogDO> list = (List<DppEtlTaskNodeRelLogDO>) dppEtlTaskNodeRelLogService.getDppEtlTaskNodeRelLogPage(exportReqVO).getRows();
        ExcelUtil<DppEtlTaskNodeRelLogRespVO> util = new ExcelUtil<>(DppEtlTaskNodeRelLogRespVO.class);
        util.exportExcel(response, DppEtlTaskNodeRelLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成任务节点关系-日志列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:import')")
    @Log(title = "数据集成任务节点关系-日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlTaskNodeRelLogRespVO> util = new ExcelUtil<>(DppEtlTaskNodeRelLogRespVO.class);
        List<DppEtlTaskNodeRelLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlTaskNodeRelLogService.importDppEtlTaskNodeRelLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成任务节点关系-日志详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlTaskNodeRelLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO = dppEtlTaskNodeRelLogService.getDppEtlTaskNodeRelLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlTaskNodeRelLogDO, DppEtlTaskNodeRelLogRespVO.class));
    }

    @Operation(summary = "新增数据集成任务节点关系-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:add')")
    @Log(title = "数据集成任务节点关系-日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLog) {
        dppEtlTaskNodeRelLog.setCreatorId(getUserId());
        dppEtlTaskNodeRelLog.setCreateBy(getNickName());
        dppEtlTaskNodeRelLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskNodeRelLogService.createDppEtlTaskNodeRelLog(dppEtlTaskNodeRelLog));
    }

    @Operation(summary = "修改数据集成任务节点关系-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:edit')")
    @Log(title = "数据集成任务节点关系-日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLog) {
        dppEtlTaskNodeRelLog.setUpdatorId(getUserId());
        dppEtlTaskNodeRelLog.setUpdateBy(getNickName());
        dppEtlTaskNodeRelLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskNodeRelLogService.updateDppEtlTaskNodeRelLog(dppEtlTaskNodeRelLog));
    }

    @Operation(summary = "删除数据集成任务节点关系-日志")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRelLog:remove')")
    @Log(title = "数据集成任务节点关系-日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlTaskNodeRelLogService.removeDppEtlTaskNodeRelLog(Arrays.asList(ids)));
    }

}
