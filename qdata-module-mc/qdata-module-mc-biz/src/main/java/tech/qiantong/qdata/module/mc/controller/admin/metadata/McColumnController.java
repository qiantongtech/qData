package tech.qiantong.qdata.module.mc.controller.admin.metadata;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.ToggleStatusVO;
import tech.qiantong.qdata.module.mc.convert.metadata.McColumnConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;
import tech.qiantong.qdata.module.mc.service.metadata.IMcColumnService;
import tech.qiantong.qdata.security.annotation.BizDataScope;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * Metadata field informationController
 *
 * @author qdata
 * @date 2026-02-11
 */
@Tag(name = "元数据字段信息")
@RestController
@RequestMapping("/mc/column")
@Validated
public class McColumnController extends BaseController {
    @Resource
    private IMcColumnService mcColumnService;

    @Operation(summary = "查询元数据字段信息列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/list")
    public CommonResult<PageResult<McColumnRespVO>> list(McColumnPageReqVO mcColumn) {
        PageResult<McColumnDO> page = mcColumnService.getMcColumnPage(mcColumn);
        return CommonResult.success(BeanUtils.toBean(page, McColumnRespVO.class));
    }

    @Operation(summary = "查询字段元数据列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/getMdColumnList")
    public CommonResult<List<McColumnRespVO>> getMdColumnList(McColumnPageReqVO mdColumn) {
        List<McColumnDO> page = mcColumnService.getMdColumnList(mdColumn);
        return CommonResult.success(BeanUtils.toBean(page, McColumnRespVO.class));
    }

    @Operation(summary = "导出元数据字段信息列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @Log(title = "log.op.title.mc.column", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McColumnPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McColumnDO> list = (List<McColumnDO>) mcColumnService.getMcColumnPage(exportReqVO).getRows();
        ExcelUtil<McColumnRespVO> util = new ExcelUtil<>(McColumnRespVO.class);
        util.exportExcel(response, McColumnConvert.INSTANCE.convertToRespVOList(list), "Application Management Data");
    }

    @Operation(summary = "获取元数据字段信息详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McColumnRespVO> getInfo(@PathVariable("id") Long id) {
        McColumnRespVO respVO = mcColumnService.getMcColumnById(id);
        return CommonResult.success(respVO);
    }

    @Operation(summary = "新增元数据字段信息")
    @Log(title = "log.op.title.mc.column", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Integer> add(@Valid @RequestBody @NotEmpty List<@Valid McColumnSaveReqVO> mdColumn) {
        for (McColumnSaveReqVO saveReqVO : mdColumn) {
            saveReqVO.validate();
            saveReqVO.setCreatorId(getUserId());
            saveReqVO.setCreateBy(getNickName());
            saveReqVO.setCreateTime(DateUtil.date());
        }
        return CommonResult.toAjax(mcColumnService.createMdColumn(mdColumn));
    }

    @Operation(summary = "暂存字段元数据信息")
    @Log(title = "log.op.title.mc.column.draft", businessType = BusinessType.INSERT)
    @PostMapping("draft")
    public CommonResult<Integer> draft(@RequestBody @NotEmpty List<McColumnSaveReqVO> saveReqVO) {
        return CommonResult.toAjax(mcColumnService.saveDraft(saveReqVO));
    }


    @Operation(summary = "修改元数据字段信息")
    @Log(title = "log.op.title.mc.column", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McColumnSaveReqVO mcColumn) {
        mcColumn.setUpdatorId(getUserId());
        mcColumn.setUpdateBy(getNickName());
        mcColumn.setUpdateTime(DateUtil.date());
        mcColumn.validate();
        return CommonResult.toAjax(mcColumnService.updateMcColumn(mcColumn));
    }

    @Operation(summary = "停启用字段元数据")
    @Log(title = "log.op.title.mc.column.toggle", businessType = BusinessType.UPDATE)
    @PostMapping("toggle")
    public CommonResult<Integer> toggle(@Valid @RequestBody ToggleStatusVO param) {
        return CommonResult.toAjax(mcColumnService.toggle(param.getId(), param.getStatus()));
    }


    @Operation(summary = "删除元数据字段信息")
    @Log(title = "log.op.title.mc.column", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcColumnService.removeMcColumn(Arrays.asList(ids)));
    }

    @Operation(summary = "批量删除检查字段元数据")
    @GetMapping("/batchDeleteCheck/{ids}")
    public CommonResult<BatchDeleteCheck<Long>> batchDeleteCheck(@PathVariable Long[] ids) {
        BatchDeleteCheck<Long> result = mcColumnService.batchDeleteCheck(Arrays.asList(ids));
        return CommonResult.success(result);
    }

}
