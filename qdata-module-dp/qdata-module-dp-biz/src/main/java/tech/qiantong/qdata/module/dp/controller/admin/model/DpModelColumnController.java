package tech.qiantong.qdata.module.dp.controller.admin.model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.R;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.model.DpModelColumnConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.module.dp.service.model.IDpModelColumnService;

/**
 * 逻辑模型属性信息Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "逻辑模型属性信息")
@RestController
@RequestMapping("/dp/modelColumn")
@Validated
public class DpModelColumnController extends BaseController {
    @Resource
    private IDpModelColumnService dpModelColumnService;

    @Operation(summary = "查询逻辑模型属性信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpModelColumnRespVO>> list(DpModelColumnPageReqVO dpModelColumn) {
        PageResult<DpModelColumnDO> page = dpModelColumnService.getDpModelColumnPage(dpModelColumn);
        return CommonResult.success(BeanUtils.toBean(page, DpModelColumnRespVO.class));
    }

    @Operation(summary = "查询逻辑模型属性信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:list')")
    @GetMapping("/getDpModelColumnList")
    public AjaxResult getDpModelColumnList(DpModelColumnSaveReqVO dpModelColumn) {
        List<DpModelColumnDO> dpModelColumnList = dpModelColumnService.getDpModelColumnList(dpModelColumn);
        return AjaxResult.success(dpModelColumnList);
    }

    @Operation(summary = "导出逻辑模型属性信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:export')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpModelColumnPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpModelColumnDO> list = (List<DpModelColumnDO>) dpModelColumnService.getDpModelColumnPage(exportReqVO).getRows();
        ExcelUtil<DpModelColumnRespVO> util = new ExcelUtil<>(DpModelColumnRespVO.class);
        util.exportExcel(response, DpModelColumnConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入逻辑模型属性信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:import')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpModelColumnRespVO> util = new ExcelUtil<>(DpModelColumnRespVO.class);
        List<DpModelColumnRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpModelColumnService.importDpModelColumn(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取逻辑模型属性信息详细信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpModelColumnRespVO> getInfo(@PathVariable("id") Long id) {
        DpModelColumnDO dpModelColumnDO = dpModelColumnService.getDpModelColumnById(id);
        return CommonResult.success(BeanUtils.toBean(dpModelColumnDO, DpModelColumnRespVO.class));
    }

    @Operation(summary = "新增逻辑模型属性信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:add')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpModelColumnSaveReqVO dpModelColumn) {
        dpModelColumn.setCreatorId(getUserId());
        dpModelColumn.setCreateBy(getNickName());
        dpModelColumn.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelColumnService.createDpModelColumn(dpModelColumn));
    }

    @Operation(summary = "修改逻辑模型属性信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:edit')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpModelColumnSaveReqVO dpModelColumn) {
        dpModelColumn.setUpdatorId(getUserId());
        dpModelColumn.setUpdateBy(getNickName());
        dpModelColumn.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelColumnService.updateDpModelColumn(dpModelColumn));
    }

    @Operation(summary = "批量新增逻辑模型属性信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:add')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addList")
    public CommonResult<Boolean> addList(@Valid @RequestBody List<DpModelColumnSaveReqVO> dpModelColumnList) {
        for (DpModelColumnSaveReqVO dpModelColumnSaveReqVO : dpModelColumnList) {
            dpModelColumnSaveReqVO.setCreatorId(getUserId());
            dpModelColumnSaveReqVO.setCreateBy(getNickName());
            dpModelColumnSaveReqVO.setCreateTime(DateUtil.date());
        }
        return CommonResult.toAjax(dpModelColumnService.createDpModelColumnList(dpModelColumnList));
    }

    @Operation(summary = "批量修改逻辑模型属性信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:edit')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/editList")
    public CommonResult<Boolean> editList(@Valid @RequestBody List<DpModelColumnSaveReqVO> dpModelColumnList) {
        for (DpModelColumnSaveReqVO dpModelColumnSaveReqVO : dpModelColumnList) {
            dpModelColumnSaveReqVO.setCreatorId(getUserId());
            dpModelColumnSaveReqVO.setCreateBy(getNickName());
            dpModelColumnSaveReqVO.setCreateTime(DateUtil.date());
        }
        return CommonResult.toAjax(dpModelColumnService.updateDpModelColumnList(dpModelColumnList));
    }

    @Operation(summary = "删除逻辑模型属性信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelColumn:remove')")
    @Log(title = "逻辑模型属性信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpModelColumnService.removeDpModelColumn(Arrays.asList(ids)));
    }

}
