package tech.qiantong.qdata.module.mc.controller.admin.metadata;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.ToggleStatusVO;
import tech.qiantong.qdata.module.mc.convert.metadata.McTableConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;
import tech.qiantong.qdata.module.mc.service.metadata.IMcTableService;
import tech.qiantong.qdata.security.annotation.BizDataScope;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 元数据信息Controller
 *
 * @author qdata
 * @date 2026-02-11
 */
@Tag(name = "元数据信息")
@RestController
@RequestMapping("/mc/table")
@Validated
public class McTableController extends BaseController {
    @Resource
    private IMcTableService mcTableService;

    @Operation(summary = "查询元数据信息列表")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:list')")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/list")
    public CommonResult<PageResult<McTableRespVO>> list(McTablePageReqVO mcTable) {
        PageResult<McTableDO> page = mcTableService.getMcTablePage(mcTable);
        return CommonResult.success(BeanUtils.toBean(page, McTableRespVO.class));
    }

    @Operation(summary = "查询元数据信息列表")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:list')")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/getMcTablePageAsset")
    public CommonResult<PageResult<McTableRespVO>> getMcTablePageAsset(McTablePageReqVO mcTable) {
        return CommonResult.success(mcTableService.getMcTablePageAsset(mcTable));
    }

    @Operation(summary = "查询元数据信息列表")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:list')")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/getMcTableListAsset")
    public CommonResult<List<McTableRespVO>> getMcTableListAsset(McTablePageReqVO mcTable) {
        return CommonResult.success(mcTableService.getMcTableListAsset(mcTable));
    }

    @Operation(summary = "导出元数据信息列表")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:export')")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @Log(title = "log.op.title.mc.table", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McTablePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McTableDO> list = (List<McTableDO>) mcTableService.getMcTablePage(exportReqVO).getRows();
        ExcelUtil<McTableRespVO> util = new ExcelUtil<>(McTableRespVO.class);
        util.exportExcel(response, McTableConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取元数据信息详细信息")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<McTableRespVO> getInfo(@PathVariable("id") Long id) {
        McTableRespVO respVO = mcTableService.getMcTableById(id);
        return CommonResult.success(respVO);
    }

    @Operation(summary = "新增元数据信息")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:add')")
    @Log(title = "log.op.title.mc.table", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McTableSaveReqVO mcTable) {
        mcTable.setCreatorId(getUserId());
        mcTable.setCreateBy(getNickName());
        mcTable.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableService.createMcTable(mcTable));
    }

    @Operation(summary = "修改元数据信息")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:edit')")
    @Log(title = "log.op.title.mc.table", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McTableSaveReqVO mcTable) {
        mcTable.setUpdatorId(getUserId());
        mcTable.setUpdateBy(getNickName());
        mcTable.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcTableService.updateMcTable(mcTable));
    }

    @Operation(summary = "暂存表元数据")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:add')")
    @Log(title = "log.op.title.mc.table.draft", businessType = BusinessType.INSERT)
    @PostMapping("draft")
    public CommonResult<Long> draft(@RequestBody McTableSaveReqVO saveReqVO) {
        return CommonResult.toAjax(mcTableService.saveDraft(saveReqVO));
    }



    @Operation(summary = "删除元数据信息")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:remove')")
    @Log(title = "log.op.title.mc.table", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcTableService.removeMcTable(Arrays.asList(ids)));
    }

    @Operation(summary = "批量删除检查表元数据")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:remove')")
    @GetMapping("/batchDeleteCheck/{ids}")
    public CommonResult<BatchDeleteCheck<Long>> batchDeleteCheck(@PathVariable Long[] ids) {
        BatchDeleteCheck<Long> result = mcTableService.batchDeleteCheck(Arrays.asList(ids));
        return CommonResult.success(result);
    }

    @Operation(summary = "停启用表元数据")
    @PreAuthorize("@ss.hasPermi('mc:metadata:table:edit')")
    @Log(title = "log.op.title.mc.table.toggle", businessType = BusinessType.UPDATE)
    @PostMapping("/toggle")
    public CommonResult<Integer> toggle(@Valid @RequestBody ToggleStatusVO param) {
        return CommonResult.toAjax(mcTableService.toggle(param.getId(), param.getStatus()));
    }
}
