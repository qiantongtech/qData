package tech.qiantong.qdata.module.mc.controller.admin.columnLog;

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
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.columnLog.McColumnLogConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;
import tech.qiantong.qdata.module.mc.service.columnLog.IMcColumnLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Metadata field information - Log Controller
 *
 * @author qdata
 * @date 2026-03-10
 */
@Tag(name = "元数据字段信息 - 日志")
@RestController
@RequestMapping("/mc/mcColumnLog")
@Validated
public class McColumnLogController extends BaseController {
    @Resource
    private IMcColumnLogService mcColumnLogService;

    @Operation(summary = "查询元数据字段信息 - 日志列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McColumnLogRespVO>> list(McColumnLogPageReqVO mcColumnLog) {
        PageResult<McColumnLogDO> page = mcColumnLogService.getMcColumnLogPage(mcColumnLog);
        return CommonResult.success(BeanUtils.toBean(page, McColumnLogRespVO.class));
    }

    @Operation(summary = "导出元数据字段信息 - 日志列表")
    @Log(title = "log.op.title.mc.column.log", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McColumnLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McColumnLogDO> list = (List<McColumnLogDO>) mcColumnLogService.getMcColumnLogPage(exportReqVO).getRows();
        ExcelUtil<McColumnLogRespVO> util = new ExcelUtil<>(McColumnLogRespVO.class);
        util.exportExcel(response, McColumnLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取元数据字段信息 - 日志详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McColumnLogRespVO> getInfo(@PathVariable("id") Long id) {
        McColumnLogDO mcColumnLogDO = mcColumnLogService.getMcColumnLogById(id);
        return CommonResult.success(BeanUtils.toBean(mcColumnLogDO, McColumnLogRespVO.class));
    }

    @Operation(summary = "新增元数据字段信息 - 日志")
    @Log(title = "log.op.title.mc.column.log", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McColumnLogSaveReqVO mcColumnLog) {
        mcColumnLog.setCreatorId(getUserId());
        mcColumnLog.setCreateBy(getNickName());
        mcColumnLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcColumnLogService.createMcColumnLog(mcColumnLog));
    }

    @Operation(summary = "修改元数据字段信息 - 日志")
    @Log(title = "log.op.title.mc.column.log", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McColumnLogSaveReqVO mcColumnLog) {
        mcColumnLog.setUpdatorId(getUserId());
        mcColumnLog.setUpdateBy(getNickName());
        mcColumnLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcColumnLogService.updateMcColumnLog(mcColumnLog));
    }

    @Operation(summary = "删除元数据字段信息 - 日志")
    @Log(title = "log.op.title.mc.column.log", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcColumnLogService.removeMcColumnLog(Arrays.asList(ids)));
    }

}
