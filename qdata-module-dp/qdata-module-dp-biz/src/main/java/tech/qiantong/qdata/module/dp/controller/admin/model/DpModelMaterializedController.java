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
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpMaterializedMethodReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.model.DpModelMaterializedConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;
import tech.qiantong.qdata.module.dp.service.model.IDpModelMaterializedService;

/**
 * 物化模型记录Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "物化模型记录")
@RestController
@RequestMapping("/dp/modelMaterialized")
@Validated
public class DpModelMaterializedController extends BaseController {
    @Resource
    private IDpModelMaterializedService dpModelMaterializedService;

    @Operation(summary = "查询物化模型记录列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpModelMaterializedRespVO>> list(DpModelMaterializedPageReqVO dpModelMaterialized) {
        PageResult<DpModelMaterializedDO> page = dpModelMaterializedService.getDpModelMaterializedPage(dpModelMaterialized);
        return CommonResult.success(BeanUtils.toBean(page, DpModelMaterializedRespVO.class));
    }

    @Operation(summary = "导出物化模型记录列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:export')")
    @Log(title = "物化模型记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpModelMaterializedPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpModelMaterializedDO> list = (List<DpModelMaterializedDO>) dpModelMaterializedService.getDpModelMaterializedPage(exportReqVO).getRows();
        ExcelUtil<DpModelMaterializedRespVO> util = new ExcelUtil<>(DpModelMaterializedRespVO.class);
        util.exportExcel(response, DpModelMaterializedConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入物化模型记录列表")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:import')")
    @Log(title = "物化模型记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpModelMaterializedRespVO> util = new ExcelUtil<>(DpModelMaterializedRespVO.class);
        List<DpModelMaterializedRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpModelMaterializedService.importDpModelMaterialized(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取物化模型记录详细信息")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpModelMaterializedRespVO> getInfo(@PathVariable("id") Long id) {
        DpModelMaterializedDO dpModelMaterializedDO = dpModelMaterializedService.getDpModelMaterializedById(id);
        return CommonResult.success(BeanUtils.toBean(dpModelMaterializedDO, DpModelMaterializedRespVO.class));
    }

    @Operation(summary = "新增物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:add')")
    @Log(title = "物化模型记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpModelMaterializedSaveReqVO dpModelMaterialized) {
        dpModelMaterialized.setCreatorId(getUserId());
        dpModelMaterialized.setCreateBy(getNickName());
        dpModelMaterialized.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelMaterializedService.createDpModelMaterialized(dpModelMaterialized));
    }

    @Operation(summary = "新增物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:add')")
    @Log(title = "物化模型记录", businessType = BusinessType.INSERT)
    @PostMapping("/createMaterializedTable")
    public CommonResult<Long> createMaterializedTable(@Valid @RequestBody DpMaterializedMethodReqVO dpModelMaterialized) {
        dpModelMaterialized.setCreatorId(getUserId());
        dpModelMaterialized.setCreateBy(getNickName());
        dpModelMaterialized.setCreateTime(DateUtil.date());
        return CommonResult.success(dpModelMaterializedService.createMaterializedTable(dpModelMaterialized));
    }

    @Operation(summary = "修改物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:edit')")
    @Log(title = "物化模型记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpModelMaterializedSaveReqVO dpModelMaterialized) {
        dpModelMaterialized.setUpdatorId(getUserId());
        dpModelMaterialized.setUpdateBy(getNickName());
        dpModelMaterialized.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelMaterializedService.updateDpModelMaterialized(dpModelMaterialized));
    }

    @Operation(summary = "删除物化模型记录")
//    @PreAuthorize("@ss.hasPermi('dp:modelMaterialized:remove')")
    @Log(title = "物化模型记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpModelMaterializedService.removeDpModelMaterialized(Arrays.asList(ids)));
    }

}
