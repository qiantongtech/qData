package tech.qiantong.qdata.module.da.controller.admin.datasource;

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
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.convert.datasource.DaDatasourceProjectRelConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceProjectRelService;

/**
 * 数据源与项目关联关系Controller
 *
 * @author qdata
 * @date 2025-03-13
 */
@Tag(name = "数据源与项目关联关系")
@RestController
@RequestMapping("/da/dataSourceProjectRel")
@Validated
public class DaDatasourceProjectRelController extends BaseController {
    @Resource
    private IDaDatasourceProjectRelService daDatasourceProjectRelService;

    @Operation(summary = "查询数据源与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDatasourceProjectRelRespVO>> list(DaDatasourceProjectRelPageReqVO daDatasourceProjectRel) {
        PageResult<DaDatasourceProjectRelDO> page = daDatasourceProjectRelService.getDaDatasourceProjectRelPage(daDatasourceProjectRel);
        return CommonResult.success(BeanUtils.toBean(page, DaDatasourceProjectRelRespVO.class));
    }

    @Operation(summary = "导出数据源与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::export')")
    @Log(title = "数据源与项目关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDatasourceProjectRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDatasourceProjectRelDO> list = (List<DaDatasourceProjectRelDO>) daDatasourceProjectRelService.getDaDatasourceProjectRelPage(exportReqVO).getRows();
        ExcelUtil<DaDatasourceProjectRelRespVO> util = new ExcelUtil<>(DaDatasourceProjectRelRespVO.class);
        util.exportExcel(response, DaDatasourceProjectRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据源与项目关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::import')")
    @Log(title = "数据源与项目关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDatasourceProjectRelRespVO> util = new ExcelUtil<>(DaDatasourceProjectRelRespVO.class);
        List<DaDatasourceProjectRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDatasourceProjectRelService.importDaDatasourceProjectRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据源与项目关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDatasourceProjectRelRespVO> getInfo(@PathVariable("id") Long id) {
        DaDatasourceProjectRelDO daDatasourceProjectRelDO = daDatasourceProjectRelService.getDaDatasourceProjectRelById(id);
        return CommonResult.success(BeanUtils.toBean(daDatasourceProjectRelDO, DaDatasourceProjectRelRespVO.class));
    }

    @Operation(summary = "新增数据源与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::add')")
    @Log(title = "数据源与项目关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDatasourceProjectRelSaveReqVO daDatasourceProjectRel) {
        daDatasourceProjectRel.setCreatorId(getUserId());
        daDatasourceProjectRel.setCreateBy(getNickName());
        daDatasourceProjectRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceProjectRelService.createDaDatasourceProjectRel(daDatasourceProjectRel));
    }

    @Operation(summary = "修改数据源与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::edit')")
    @Log(title = "数据源与项目关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDatasourceProjectRelSaveReqVO daDatasourceProjectRel) {
        daDatasourceProjectRel.setUpdatorId(getUserId());
        daDatasourceProjectRel.setUpdateBy(getNickName());
        daDatasourceProjectRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceProjectRelService.updateDaDatasourceProjectRel(daDatasourceProjectRel));
    }

    @Operation(summary = "删除数据源与项目关联关系")
    @PreAuthorize("@ss.hasPermi('da:dataSourceProjectRel::remove')")
    @Log(title = "数据源与项目关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDatasourceProjectRelService.removeDaDatasourceProjectRel(Arrays.asList(ids)));
    }

}
