package tech.qiantong.qdata.module.da.controller.admin.assetColumn;

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
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetColumn.DaAssetColumnConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;

/**
 * 数据资产字段Controller
 *
 * @author lhs
 * @date 2025-01-21
 */
@Tag(name = "数据资产字段")
@RestController
@RequestMapping("/da/assetColumn")
@Validated
public class DaAssetColumnController extends BaseController {
    @Resource
    private IDaAssetColumnService daAssetColumnService;

    @Operation(summary = "查询数据资产字段列表")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetColumnRespVO>> list(DaAssetColumnPageReqVO daAssetColumn) {
        PageResult<DaAssetColumnDO> page = daAssetColumnService.getDaAssetColumnPage(daAssetColumn);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetColumnRespVO.class));
    }


    @Operation(summary = "根据选择的资产表id信息查询字段信息")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:list')")
    @GetMapping("/getColumnByAssetId")
    public AjaxResult getColumnByAssetId(DaAssetColumnPageReqVO daAssetColumn) {
        return daAssetColumnService.getColumnByAssetId(daAssetColumn);
    }

    @Operation(summary = "导出数据资产字段列表")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:export')")
    @Log(title = "数据资产字段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetColumnPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetColumnDO> list = (List<DaAssetColumnDO>) daAssetColumnService.getDaAssetColumnPage(exportReqVO).getRows();
        ExcelUtil<DaAssetColumnRespVO> util = new ExcelUtil<>(DaAssetColumnRespVO.class);
        util.exportExcel(response, DaAssetColumnConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产字段列表")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:import')")
    @Log(title = "数据资产字段", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetColumnRespVO> util = new ExcelUtil<>(DaAssetColumnRespVO.class);
        List<DaAssetColumnRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetColumnService.importDaAssetColumn(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产字段详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetColumnRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetColumnDO daAssetColumnDO = daAssetColumnService.getDaAssetColumnById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetColumnDO, DaAssetColumnRespVO.class));
    }

    @Operation(summary = "新增数据资产字段")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:add')")
    @Log(title = "数据资产字段", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetColumnSaveReqVO daAssetColumn) {
        daAssetColumn.setCreatorId(getUserId());
        daAssetColumn.setCreateBy(getNickName());
        daAssetColumn.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetColumnService.createDaAssetColumn(daAssetColumn));
    }

    @Operation(summary = "修改数据资产字段")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:edit')")
    @Log(title = "数据资产字段", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetColumnSaveReqVO daAssetColumn) {
        daAssetColumn.setUpdatorId(getUserId());
        daAssetColumn.setUpdateBy(getNickName());
        daAssetColumn.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetColumnService.updateDaAssetColumn(daAssetColumn));
    }

    @Operation(summary = "删除数据资产字段")
    @PreAuthorize("@ss.hasPermi('da:assetColumn:remove')")
    @Log(title = "数据资产字段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetColumnService.removeDaAssetColumn(Arrays.asList(ids)));
    }

}
