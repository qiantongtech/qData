package tech.qiantong.qdata.module.mc.controller.admin.task;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.*;
import tech.qiantong.qdata.module.mc.convert.task.McTaskConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;
import tech.qiantong.qdata.security.annotation.BizDataScope;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 采集任务Controller
 *
 * @author qdata
 * @date 2025-12-16
 */
@Tag(name = "采集任务")
@RestController
@RequestMapping("/mc/task")
@Validated
public class McTaskController extends BaseController {
    @Resource
    private IMcTaskService mcTaskService;

    @Operation(summary = "查询采集任务列表")
    @BizDataScope(code = "mc_task_list", userField = "leader", deptField = "responsibleDept")
    @GetMapping("/list")
    public CommonResult<PageResult<McTaskRespVO>> list(McTaskPageReqVO mcTask) {
        PageResult<McTaskDO> page = mcTaskService.getMcTaskPage(mcTask);
        return CommonResult.success(BeanUtils.toBean(page, McTaskRespVO.class));
    }

    @Operation(summary = "导出采集任务列表")
    @BizDataScope(code = "mc_task_list", userField = "leader", deptField = "responsibleDept")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTaskDO> list = (List<McTaskDO>) mcTaskService.getMcTaskPage(exportReqVO).getRows();
        ExcelUtil<McTaskRespVO> util = new ExcelUtil<>(McTaskRespVO.class);
        util.exportExcel(response, McTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入采集任务列表")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<McTaskRespVO> util = new ExcelUtil<>(McTaskRespVO.class);
        List<McTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = mcTaskService.importMcTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取采集任务详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTaskRespVO> getInfo(@PathVariable("id") Long id) {
        return CommonResult.success(mcTaskService.getMcTaskByIdNew(id));
    }

    @Operation(summary = "新增采集任务")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTaskSaveReqVO mcTask) {
        mcTask.setCreatorId(getUserId());
        mcTask.setCreateBy(getNickName());
        mcTask.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskService.createMcTask(mcTask));
    }

    @Operation(summary = "修改采集任务")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTaskSaveReqVO mcTask) {
        mcTask.setUpdatorId(getUserId());
        mcTask.setUpdateBy(getNickName());
        mcTask.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskService.updateMcTask(mcTask));
    }

    @Operation(summary = "删除采集任务")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTaskService.removeMcTask(Arrays.asList(ids)));
    }

    @Operation(summary = "删除采集任务")
    @Log(title = "log.op.title.mc.task", businessType = BusinessType.DELETE)
    @GetMapping("/batchDeleteCheck/{ids}")
    public CommonResult<BatchDeleteCheck<Long>> batchDeleteCheck(@PathVariable Long[] ids) {
        BatchDeleteCheck<Long> result = mcTaskService.batchDeleteCheck(Arrays.asList(ids));
        return CommonResult.success(result);
    }

    @Operation(summary = "获取采集范围信息")
    @GetMapping(value = "/getRealtimeMcTaskScopeList/{id}")
    public CommonResult<List<McTaskScopeRespVO>> getRealtimeMcTaskScopeList(@PathVariable("id") Long id) {
        return CommonResult.success(BeanUtils.toBean(mcTaskService.getRealtimeMcTaskScopeList(id), McTaskScopeRespVO.class));
    }



    @Operation(summary = "上下线-任务")
    @PostMapping("/updateReleaseJobTask")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateReleaseJobTask(@Valid @RequestBody McTaskSaveReqVO mcTask) {
        Map<String, Object> result = mcTaskService.updateReleaseJobTask(mcTask);
        return CommonResult.success(result);
    }

    @Operation(summary = "上下线-调度")
    @PostMapping("/updateReleaseSchedule")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateReleaseSchedule(@Valid @RequestBody McTaskSaveReqVO mcTask) {
        Map<String, Object> result = mcTaskService.updateReleaseSchedule(mcTask);
        return CommonResult.success(result);
    }

    @Operation(summary = "上下线-调度")
    @PostMapping("/runJobOnce")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult runJobOnce(@Valid @RequestBody McTaskSaveReqVO mcTask) {
        mcTask.setCreatorId(super.getUserId());
        mcTask.setCreateBy(super.getNickName());
        Map<String, Object> result = mcTaskService.runJobOnce(mcTask);
        return CommonResult.success(result);
    }

    @Operation(summary = "获取来源系统树形结构")
    @GetMapping("/sourceSystemTree")
    public CommonResult<List<McTaskSourceTreeRespVO>> getSourceSystemTree() {
        List<McTaskSourceTreeRespVO> treeList = mcTaskService.getSourceSystemTree();
        return CommonResult.success(treeList);
    }

    @GetMapping("/test")
    public void test() {
        System.out.println("33333333333333");
        mcTaskService.runDaDiscoveryTask(3L);
    }
}
