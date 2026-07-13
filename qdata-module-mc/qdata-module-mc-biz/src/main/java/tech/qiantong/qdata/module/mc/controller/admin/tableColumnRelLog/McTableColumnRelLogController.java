package tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.tableColumnRelLog.McTableColumnRelLogConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;
import tech.qiantong.qdata.module.mc.service.tableColumnRelLog.IMcTableColumnRelLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Metadata database and information and field information relationship-Log Controller
 *
 * @author qdata
 * @date 2026-03-10
 */
@Tag(name = "元数据数据库与信息及字段信息关系-日志")
@RestController
@RequestMapping("/mc/mcTableColumnRelLog")
@Validated
public class McTableColumnRelLogController extends BaseController {
    @Resource
    private IMcTableColumnRelLogService mcTableColumnRelLogService;

    @Operation(summary = "查询元数据数据库与信息及字段信息关系-日志列表")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<McTableColumnRelLogRespVO>> list(McTableColumnRelLogPageReqVO mcTableColumnRelLog) {
        PageResult<McTableColumnRelLogDO> page = mcTableColumnRelLogService.getMcTableColumnRelLogPage(mcTableColumnRelLog);
        return CommonResult.success(BeanUtils.toBean(page, McTableColumnRelLogRespVO.class));
    }

    @Operation(summary = "导出元数据数据库与信息及字段信息关系-日志列表")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:export')")
    @Log(title = "log.op.title.mc.table.column.rel.log", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTableColumnRelLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTableColumnRelLogDO> list = (List<McTableColumnRelLogDO>) mcTableColumnRelLogService.getMcTableColumnRelLogPage(exportReqVO).getRows();
        ExcelUtil<McTableColumnRelLogRespVO> util = new ExcelUtil<>(McTableColumnRelLogRespVO.class);
        util.exportExcel(response, McTableColumnRelLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取元数据数据库与信息及字段信息关系-日志详细信息")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<McTableColumnRelLogRespVO> getInfo(@PathVariable("id") Long id) {
        McTableColumnRelLogDO mcTableColumnRelLogDO = mcTableColumnRelLogService.getMcTableColumnRelLogById(id);
        return CommonResult.success(BeanUtils.toBean(mcTableColumnRelLogDO, McTableColumnRelLogRespVO.class));
    }

    @Operation(summary = "新增元数据数据库与信息及字段信息关系-日志")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:add')")
    @Log(title = "log.op.title.mc.table.column.rel.log", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTableColumnRelLogSaveReqVO mcTableColumnRelLog) {
        mcTableColumnRelLog.setCreatorId(getUserId());
        mcTableColumnRelLog.setCreateBy(getNickName());
        mcTableColumnRelLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableColumnRelLogService.createMcTableColumnRelLog(mcTableColumnRelLog));
    }

    @Operation(summary = "修改元数据数据库与信息及字段信息关系-日志")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:edit')")
    @Log(title = "log.op.title.mc.table.column.rel.log", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTableColumnRelLogSaveReqVO mcTableColumnRelLog) {
        mcTableColumnRelLog.setUpdatorId(getUserId());
        mcTableColumnRelLog.setUpdateBy(getNickName());
        mcTableColumnRelLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableColumnRelLogService.updateMcTableColumnRelLog(mcTableColumnRelLog));
    }

    @Operation(summary = "删除元数据数据库与信息及字段信息关系-日志")
//    @PreAuthorize("@ss.hasPermi('mc:tableColumnRelLog:TableColumnRelLog:remove')")
    @Log(title = "log.op.title.mc.table.column.rel.log", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTableColumnRelLogService.removeMcTableColumnRelLog(Arrays.asList(ids)));
    }

}
