package tech.qiantong.qdata.module.dpp.controller.admin.qa;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.qa.DppQualityTaskConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskService;

/**
 * 数据质量任务Controller
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Tag(name = "数据质量任务")
@RestController
@RequestMapping("/dpp/qualityTask")
@Validated
public class DppQualityTaskController extends BaseController {
    @Resource
    private IDppQualityTaskService dppQualityTaskService;

    @Operation(summary = "查询数据质量任务列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityTaskRespVO>> list(DppQualityTaskPageReqVO dppQualityTask) {
        PageResult<DppQualityTaskDO> page = dppQualityTaskService.getDppQualityTaskPage(dppQualityTask);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityTaskRespVO.class));
    }

    @Operation(summary = "导出数据质量任务列表")
    @Log(title = "数据质量任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityTaskPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityTaskDO> list = (List<DppQualityTaskDO>) dppQualityTaskService.getDppQualityTaskPage(exportReqVO).getRows();
        ExcelUtil<DppQualityTaskRespVO> util = new ExcelUtil<>(DppQualityTaskRespVO.class);
        util.exportExcel(response, DppQualityTaskConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量任务列表")
    @Log(title = "数据质量任务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityTaskRespVO> util = new ExcelUtil<>(DppQualityTaskRespVO.class);
        List<DppQualityTaskRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityTaskService.importDppQualityTask(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量任务详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityTaskRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityTaskRespVO dppQualityTaskDO = dppQualityTaskService.getDppQualityTaskById(id);
        return CommonResult.success(dppQualityTaskDO);
    }

    @Operation(summary = "获取数据质量任务详细信息")
    @GetMapping( "/getQualityTaskAsset")
    public CommonResult<DppQualityTaskRespVO> getQualityTaskAsset(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO) {
        DppQualityTaskRespVO dppQualityTaskDO = dppQualityTaskService.getQualityTaskAsset(dppQualityTaskAssetReqVO);
        return CommonResult.success(dppQualityTaskDO);
    }

    @Operation(summary = "新增数据质量任务")
    @Log(title = "数据质量任务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityTaskSaveReqVO dppQualityTask) {
        dppQualityTask.setCreatorId(getUserId());
        dppQualityTask.setCreateBy(getNickName());
        dppQualityTask.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskService.createDppQualityTask(dppQualityTask));
    }

    @Operation(summary = "修改数据质量任务")
    @Log(title = "数据质量任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityTaskSaveReqVO dppQualityTask) {
        dppQualityTask.setUpdatorId(getUserId());
        dppQualityTask.setUpdateBy(getNickName());
        dppQualityTask.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskService.updateDppQualityTask(dppQualityTask));
    }

    @Operation(summary = "删除数据质量任务")
    @Log(title = "数据质量任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityTaskService.removeDppQualityTask(Arrays.asList(ids)));
    }


    @Operation(summary = "修改数据质量任务")
    @PostMapping("/updateDppQualityTaskStatus")
    public AjaxResult updateDaDiscoveryTaskStatus(@RequestBody DppQualityTaskSaveReqVO daDiscoveryTask)
    {
        boolean result = dppQualityTaskService.updateDppQualityTaskStatus(daDiscoveryTask);
        return result ? success() : error("任务不存在或已过期！");
    }

    @Log(title = "触发一次定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/startDppQualityTask/{id}")
    public AjaxResult startDaDiscoveryTask(@PathVariable("id") Long id)
    {
        return dppQualityTaskService.startDppQualityTask(id);
    }


    @Log(title = "数据质量任务状态修改", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDaDiscoveryTaskCronExpression")
    public AjaxResult updateDaDiscoveryTaskCronExpression(@RequestBody DppQualityTaskSaveReqVO daDiscoveryTask)
    {
        boolean result = dppQualityTaskService.updateDaDiscoveryTaskCronExpression(daDiscoveryTask);
        return result ? success() : error("任务不存在或已过期！");
    }

}
