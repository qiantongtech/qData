package tech.qiantong.qdata.module.mc.controller.admin.task;

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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.task.McTaskInstanceLogConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskInstanceLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 采集任务实例-日志Controller
 *
 * @author qdata
 * @date 2025-12-16
 */
@Tag(name = "采集任务实例-日志")
@RestController
@RequestMapping("/mc/taskInstanceLog")
@Validated
public class McTaskInstanceLogController extends BaseController {
    @Resource
    private IMcTaskInstanceLogService mcTaskInstanceLogService;

    @Operation(summary = "查询采集任务实例-日志列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McTaskInstanceLogRespVO>> list(McTaskInstanceLogPageReqVO mcTaskInstanceLog) {
        PageResult<McTaskInstanceLogDO> page = mcTaskInstanceLogService.getMcTaskInstanceLogPage(mcTaskInstanceLog);
        return CommonResult.success(BeanUtils.toBean(page, McTaskInstanceLogRespVO.class));
    }

    @Operation(summary = "导出采集任务实例-日志列表")
    @Log(title = "采集任务实例-日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTaskInstanceLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTaskInstanceLogDO> list = (List<McTaskInstanceLogDO>) mcTaskInstanceLogService.getMcTaskInstanceLogPage(exportReqVO).getRows();
        ExcelUtil<McTaskInstanceLogRespVO> util = new ExcelUtil<>(McTaskInstanceLogRespVO.class);
        util.exportExcel(response, McTaskInstanceLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取采集任务实例-日志详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTaskInstanceLogRespVO> getInfo(@PathVariable("id") Long id) {
        McTaskInstanceLogDO mcTaskInstanceLogDO = mcTaskInstanceLogService.getMcTaskInstanceLogById(id);
        return CommonResult.success(BeanUtils.toBean(mcTaskInstanceLogDO, McTaskInstanceLogRespVO.class));
    }

    @Operation(summary = "新增采集任务实例-日志")
    @Log(title = "采集任务实例-日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTaskInstanceLogSaveReqVO mcTaskInstanceLog) {
        mcTaskInstanceLog.setCreatorId(getUserId());
        mcTaskInstanceLog.setCreateBy(getNickName());
        mcTaskInstanceLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskInstanceLogService.createMcTaskInstanceLog(mcTaskInstanceLog));
    }

    @Operation(summary = "修改采集任务实例-日志")
    @Log(title = "采集任务实例-日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTaskInstanceLogSaveReqVO mcTaskInstanceLog) {
        mcTaskInstanceLog.setUpdatorId(getUserId());
        mcTaskInstanceLog.setUpdateBy(getNickName());
        mcTaskInstanceLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskInstanceLogService.updateMcTaskInstanceLog(mcTaskInstanceLog));
    }

    @Operation(summary = "删除采集任务实例-日志")
    @Log(title = "采集任务实例-日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskInstanceIds}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTaskInstanceLogService.removeMcTaskInstanceLog(Arrays.asList(ids)));
    }

}
