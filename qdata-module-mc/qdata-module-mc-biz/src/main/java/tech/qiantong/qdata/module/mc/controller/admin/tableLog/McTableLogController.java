package tech.qiantong.qdata.module.mc.controller.admin.tableLog;

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
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.tableLog.McTableLogConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;
import tech.qiantong.qdata.module.mc.service.tableLog.IMcTableLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Metadata information - Log Controller
 *
 * @author qdata
 * @date 2026-03-10
 */
@Tag(name = "元数据信息 - 日志")
@RestController
@RequestMapping("/mc/mcTableLog")
@Validated
public class McTableLogController extends BaseController {
    @Resource
    private IMcTableLogService mcTableLogService;

    @Operation(summary = "查询元数据信息 - 日志列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McTableLogRespVO>> list(McTableLogPageReqVO mcTableLog) {
        PageResult<McTableLogDO> page = mcTableLogService.getMcTableLogPage(mcTableLog);
        return CommonResult.success(BeanUtils.toBean(page, McTableLogRespVO.class));
    }

    @Operation(summary = "导出元数据信息 - 日志列表")
    @Log(title = "log.op.title.mc.table.log", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTableLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTableLogDO> list = (List<McTableLogDO>) mcTableLogService.getMcTableLogPage(exportReqVO).getRows();
        ExcelUtil<McTableLogRespVO> util = new ExcelUtil<>(McTableLogRespVO.class);
        util.exportExcel(response, McTableLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取元数据信息 - 日志详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTableLogRespVO> getInfo(@PathVariable("id") Long id) {
        McTableLogDO mcTableLogDO = mcTableLogService.getMcTableLogById(id);
        return CommonResult.success(BeanUtils.toBean(mcTableLogDO, McTableLogRespVO.class));
    }

    @Operation(summary = "新增元数据信息 - 日志")
    @Log(title = "log.op.title.mc.table.log", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTableLogSaveReqVO mcTableLog) {
        mcTableLog.setCreatorId(getUserId());
        mcTableLog.setCreateBy(getNickName());
        mcTableLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableLogService.createMcTableLog(mcTableLog));
    }

    @Operation(summary = "修改元数据信息 - 日志")
    @Log(title = "log.op.title.mc.table.log", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTableLogSaveReqVO mcTableLog) {
        mcTableLog.setUpdatorId(getUserId());
        mcTableLog.setUpdateBy(getNickName());
        mcTableLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableLogService.updateMcTableLog(mcTableLog));
    }

    @Operation(summary = "删除元数据信息 - 日志")
    @Log(title = "log.op.title.mc.table.log", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTableLogService.removeMcTableLog(Arrays.asList(ids)));
    }

}
