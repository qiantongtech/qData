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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.task.McTaskInstanceConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskInstanceService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Collection task instanceController
 *
 * @author qdata
 * @date 2025-12-16
 */
@Tag(name = "采集任务实例")
@RestController
@RequestMapping("/mc/taskInstance")
@Validated
public class McTaskInstanceController extends BaseController {
    @Resource
    private IMcTaskInstanceService mcTaskInstanceService;

    @Operation(summary = "查询采集任务实例列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McTaskInstanceRespVO>> list(McTaskInstancePageReqVO mcTaskInstance) {
        PageResult<McTaskInstanceDO> page = mcTaskInstanceService.getMcTaskInstancePage(mcTaskInstance);
        return CommonResult.success(BeanUtils.toBean(page, McTaskInstanceRespVO.class));
    }

    @Operation(summary = "导出采集任务实例列表")
    @Log(title = "log.op.title.mc.task.instance", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTaskInstancePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTaskInstanceDO> list = (List<McTaskInstanceDO>) mcTaskInstanceService.getMcTaskInstancePage(exportReqVO).getRows();
        ExcelUtil<McTaskInstanceRespVO> util = new ExcelUtil<>(McTaskInstanceRespVO.class);
        util.exportExcel(response, McTaskInstanceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入采集任务实例列表")
    @Log(title = "log.op.title.mc.task.instance", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<McTaskInstanceRespVO> util = new ExcelUtil<>(McTaskInstanceRespVO.class);
        List<McTaskInstanceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = mcTaskInstanceService.importMcTaskInstance(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取采集任务实例详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTaskInstanceRespVO> getInfo(@PathVariable("id") Long id) {
        McTaskInstanceDO mcTaskInstanceDO = mcTaskInstanceService.getMcTaskInstanceById(id);
        return CommonResult.success(BeanUtils.toBean(mcTaskInstanceDO, McTaskInstanceRespVO.class));
    }

    @Operation(summary = "新增采集任务实例")
    @Log(title = "log.op.title.mc.task.instance", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTaskInstanceSaveReqVO mcTaskInstance) {
        mcTaskInstance.setCreatorId(getUserId());
        mcTaskInstance.setCreateBy(getNickName());
        mcTaskInstance.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskInstanceService.createMcTaskInstance(mcTaskInstance));
    }

    @Operation(summary = "修改采集任务实例")
    @Log(title = "log.op.title.mc.task.instance", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTaskInstanceSaveReqVO mcTaskInstance) {
        mcTaskInstance.setUpdatorId(getUserId());
        mcTaskInstance.setUpdateBy(getNickName());
        mcTaskInstance.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskInstanceService.updateMcTaskInstance(mcTaskInstance));
    }

    @Operation(summary = "删除采集任务实例")
    @Log(title = "log.op.title.mc.task.instance", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTaskInstanceService.removeMcTaskInstance(Arrays.asList(ids)));
    }

}
