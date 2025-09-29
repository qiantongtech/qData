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
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.model.DpModelConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.module.dp.service.model.IDpModelService;

/**
 * 逻辑模型Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "逻辑模型")
@RestController
@RequestMapping("/dp/model")
@Validated
public class DpModelController extends BaseController {
    @Resource
    private IDpModelService dpModelService;

    @Operation(summary = "查询逻辑模型列表")
    @PreAuthorize("@ss.hasPermi('dp:model:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpModelRespVO>> list(DpModelPageReqVO dpModel) {
        PageResult<DpModelDO> page = dpModelService.getDpModelPage(dpModel);
        return CommonResult.success(BeanUtils.toBean(page, DpModelRespVO.class));
    }

    @Operation(summary = "导出逻辑模型列表")
    @PreAuthorize("@ss.hasPermi('dp:model:export')")
    @Log(title = "逻辑模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpModelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpModelDO> list = (List<DpModelDO>) dpModelService.getDpModelPage(exportReqVO).getRows();
        ExcelUtil<DpModelRespVO> util = new ExcelUtil<>(DpModelRespVO.class);
        util.exportExcel(response, DpModelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入逻辑模型列表")
    @PreAuthorize("@ss.hasPermi('dp:model:import')")
    @Log(title = "逻辑模型", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpModelRespVO> util = new ExcelUtil<>(DpModelRespVO.class);
        List<DpModelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpModelService.importDpModel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取逻辑模型详细信息")
    @PreAuthorize("@ss.hasPermi('dp:model:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<DpModelRespVO> getInfo(@PathVariable("ID") Long ID) {
        DpModelDO dpModelDO = dpModelService.getDpModelById(ID);
        return CommonResult.success(BeanUtils.toBean(dpModelDO, DpModelRespVO.class));
    }

    @Operation(summary = "新增逻辑模型")
    @PreAuthorize("@ss.hasPermi('dp:model:add')")
    @Log(title = "逻辑模型", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpModelSaveReqVO dpModel) {
        dpModel.setCreatorId(getUserId());
        dpModel.setCreateBy(getNickName());
        dpModel.setCreateTime(DateUtil.date());
        return CommonResult.success(dpModelService.createDpModel(dpModel));
    }

    @Operation(summary = "修改逻辑模型")
    @PreAuthorize("@ss.hasPermi('dp:model:edit')")
    @Log(title = "逻辑模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpModelSaveReqVO dpModel) {
        dpModel.setUpdatorId(getUserId());
        dpModel.setUpdateBy(getNickName());
        dpModel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpModelService.updateDpModel(dpModel));
    }

    @Operation(summary = "删除逻辑模型")
    @PreAuthorize("@ss.hasPermi('dp:model:remove')")
    @Log(title = "逻辑模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpModelService.removeDpModel(Arrays.asList(ids)));
    }

    @Operation(summary = "删除逻辑模型连带字段一起删除")
    @PreAuthorize("@ss.hasPermi('dp:model:remove')")
    @Log(title = "逻辑模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/columnAll/{ids}")
    public CommonResult<Integer> removeAndColumnAll(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpModelService.removeDpModelAndColumnAll(Arrays.asList(ids)));
    }

    @Operation(summary = "更改状态")
    @PreAuthorize("@ss.hasPermi('dp:model:edit')")
    @Log(title = "更改数据元状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResult<Boolean> updateStatus(@PathVariable Long id,@PathVariable Long status) {
        return CommonResult.toAjax(dpModelService.updateStatus(id,status));
    }
}
