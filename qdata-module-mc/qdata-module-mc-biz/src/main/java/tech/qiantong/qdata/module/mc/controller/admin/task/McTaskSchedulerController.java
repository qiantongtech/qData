package tech.qiantong.qdata.module.mc.controller.admin.task;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.task.McTaskSchedulerConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskSchedulerService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据集成调度信息Controller
 *
 * @author qdata
 * @date 2025-12-16
 */
@Tag(name = "数据集成调度信息")
@RestController
@RequestMapping("/mc/taskScheduler")
@Validated
public class McTaskSchedulerController extends BaseController {
    @Resource
    private IMcTaskSchedulerService mcTaskSchedulerService;

    @Operation(summary = "查询数据集成调度信息列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McTaskSchedulerRespVO>> list(McTaskSchedulerPageReqVO mcTaskScheduler) {
        PageResult<McTaskSchedulerDO> page = mcTaskSchedulerService.getMcTaskSchedulerPage(mcTaskScheduler);
        return CommonResult.success(BeanUtils.toBean(page, McTaskSchedulerRespVO.class));
    }

    @Operation(summary = "导出数据集成调度信息列表")
    @Log(title = "log.op.title.mc.task.scheduler", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTaskSchedulerPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTaskSchedulerDO> list = (List<McTaskSchedulerDO>) mcTaskSchedulerService.getMcTaskSchedulerPage(exportReqVO).getRows();
        ExcelUtil<McTaskSchedulerRespVO> util = new ExcelUtil<>(McTaskSchedulerRespVO.class);
        util.exportExcel(response, McTaskSchedulerConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成调度信息列表")
    @Log(title = "log.op.title.mc.task.scheduler", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<McTaskSchedulerRespVO> util = new ExcelUtil<>(McTaskSchedulerRespVO.class);
        List<McTaskSchedulerRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = mcTaskSchedulerService.importMcTaskScheduler(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成调度信息详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTaskSchedulerRespVO> getInfo(@PathVariable("id") Long id) {
        McTaskSchedulerDO mcTaskSchedulerDO = mcTaskSchedulerService.getMcTaskSchedulerById(id);
        return CommonResult.success(BeanUtils.toBean(mcTaskSchedulerDO, McTaskSchedulerRespVO.class));
    }

    @Operation(summary = "新增数据集成调度信息")
    @Log(title = "log.op.title.mc.task.scheduler", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTaskSchedulerSaveReqVO mcTaskScheduler) {
        mcTaskScheduler.setCreatorId(getUserId());
        mcTaskScheduler.setCreateBy(getNickName());
        mcTaskScheduler.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskSchedulerService.createMcTaskScheduler(mcTaskScheduler));
    }

    @Operation(summary = "修改数据集成调度信息")
    @Log(title = "log.op.title.mc.task.scheduler", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTaskSchedulerSaveReqVO mcTaskScheduler) {
        mcTaskScheduler.setUpdatorId(getUserId());
        mcTaskScheduler.setUpdateBy(getNickName());
        mcTaskScheduler.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskSchedulerService.updateMcTaskScheduler(mcTaskScheduler));
    }

    @Operation(summary = "删除数据集成调度信息")
    @Log(title = "log.op.title.mc.task.scheduler", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTaskSchedulerService.removeMcTaskScheduler(Arrays.asList(ids)));
    }

}
