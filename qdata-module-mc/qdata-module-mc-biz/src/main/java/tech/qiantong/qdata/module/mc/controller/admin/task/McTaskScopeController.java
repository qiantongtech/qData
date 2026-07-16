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
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.convert.task.McTaskScopeConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskScopeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Collection rangeController
 *
 * @author qdata
 * @date 2025-12-16
 */
@Tag(name = "采集范围")
@RestController
@RequestMapping("/mc/taskScope")
@Validated
public class McTaskScopeController extends BaseController {
    @Resource
    private IMcTaskScopeService mcTaskScopeService;

    @Operation(summary = "查询采集范围列表")
    @GetMapping("/list")
    public CommonResult<PageResult<McTaskScopeRespVO>> list(McTaskScopePageReqVO mcTaskScope) {
        PageResult<McTaskScopeDO> page = mcTaskScopeService.getMcTaskScopePage(mcTaskScope);
        return CommonResult.success(BeanUtils.toBean(page, McTaskScopeRespVO.class));
    }

    @Operation(summary = "导出采集范围列表")
    @Log(title = "log.op.title.mc.task.scope", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTaskScopePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTaskScopeDO> list = (List<McTaskScopeDO>) mcTaskScopeService.getMcTaskScopePage(exportReqVO).getRows();
        ExcelUtil<McTaskScopeRespVO> util = new ExcelUtil<>(McTaskScopeRespVO.class);
        util.exportExcel(response, McTaskScopeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入采集范围列表")
    @Log(title = "log.op.title.mc.task.scope", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<McTaskScopeRespVO> util = new ExcelUtil<>(McTaskScopeRespVO.class);
        List<McTaskScopeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = mcTaskScopeService.importMcTaskScope(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取采集范围详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McTaskScopeRespVO> getInfo(@PathVariable("id") Long id) {
        McTaskScopeDO mcTaskScopeDO = mcTaskScopeService.getMcTaskScopeById(id);
        return CommonResult.success(BeanUtils.toBean(mcTaskScopeDO, McTaskScopeRespVO.class));
    }

    @Operation(summary = "新增采集范围")
    @Log(title = "log.op.title.mc.task.scope", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTaskScopeSaveReqVO mcTaskScope) {
        mcTaskScope.setCreatorId(getUserId());
        mcTaskScope.setCreateBy(getNickName());
        mcTaskScope.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskScopeService.createMcTaskScope(mcTaskScope));
    }

    @Operation(summary = "修改采集范围")
    @Log(title = "log.op.title.mc.task.scope", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTaskScopeSaveReqVO mcTaskScope) {
        mcTaskScope.setUpdatorId(getUserId());
        mcTaskScope.setUpdateBy(getNickName());
        mcTaskScope.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTaskScopeService.updateMcTaskScope(mcTaskScope));
    }

    @Operation(summary = "删除采集范围")
    @Log(title = "log.op.title.mc.task.scope", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTaskScopeService.removeMcTaskScope(Arrays.asList(ids)));
    }

}
