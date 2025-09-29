package tech.qiantong.qdata.module.da.controller.admin.assetchild.theme;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.theme.DaAssetThemeRelConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme.DaAssetThemeRelDO;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;

/**
 * 数据资产-主题关联关系Controller
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-主题关联关系")
@RestController
@RequestMapping("/da/assetThemeRel")
@Validated
public class DaAssetThemeRelController extends BaseController {
    @Resource
    private IDaAssetThemeRelService daAssetThemeRelService;

    @Operation(summary = "查询数据资产-主题关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetThemeRelRespVO>> list(DaAssetThemeRelPageReqVO daAssetThemeRel) {
        PageResult<DaAssetThemeRelDO> page = daAssetThemeRelService.getDaAssetThemeRelPage(daAssetThemeRel);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetThemeRelRespVO.class));
    }

    @Operation(summary = "导出数据资产-主题关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:export')")
    @Log(title = "数据资产-主题关联关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetThemeRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetThemeRelDO> list = (List<DaAssetThemeRelDO>) daAssetThemeRelService.getDaAssetThemeRelPage(exportReqVO).getRows();
        ExcelUtil<DaAssetThemeRelRespVO> util = new ExcelUtil<>(DaAssetThemeRelRespVO.class);
        util.exportExcel(response, DaAssetThemeRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-主题关联关系列表")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:import')")
    @Log(title = "数据资产-主题关联关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetThemeRelRespVO> util = new ExcelUtil<>(DaAssetThemeRelRespVO.class);
        List<DaAssetThemeRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetThemeRelService.importDaAssetThemeRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-主题关联关系详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetThemeRelRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetThemeRelDO daAssetThemeRelDO = daAssetThemeRelService.getDaAssetThemeRelById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetThemeRelDO, DaAssetThemeRelRespVO.class));
    }

    @Operation(summary = "新增数据资产-主题关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:add')")
    @Log(title = "数据资产-主题关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetThemeRelSaveReqVO daAssetThemeRel) {
        daAssetThemeRel.setCreatorId(getUserId());
        daAssetThemeRel.setCreateBy(getNickName());
        daAssetThemeRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetThemeRelService.createDaAssetThemeRel(daAssetThemeRel));
    }

    @Operation(summary = "修改数据资产-主题关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:edit')")
    @Log(title = "数据资产-主题关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetThemeRelSaveReqVO daAssetThemeRel) {
        daAssetThemeRel.setUpdatorId(getUserId());
        daAssetThemeRel.setUpdateBy(getNickName());
        daAssetThemeRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetThemeRelService.updateDaAssetThemeRel(daAssetThemeRel));
    }

    @Operation(summary = "删除数据资产-主题关联关系")
    @PreAuthorize("@ss.hasPermi('da:assetThemeRel:remove')")
    @Log(title = "数据资产-主题关联关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetThemeRelService.removeDaAssetThemeRel(Arrays.asList(ids)));
    }

}
