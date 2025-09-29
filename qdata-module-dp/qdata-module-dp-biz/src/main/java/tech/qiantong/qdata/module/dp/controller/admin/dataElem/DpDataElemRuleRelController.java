package tech.qiantong.qdata.module.dp.controller.admin.dataElem;

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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.dataElem.DpDataElemRuleRelConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemRuleRelService;

/**
 * 数据元数据规则关联信息Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "数据元数据规则关联信息")
@RestController
@RequestMapping("/dp/dataElemRuleRel")
@Validated
public class DpDataElemRuleRelController extends BaseController {
    @Resource
    private IDpDataElemRuleRelService dpDataElemRuleRelService;

    @Operation(summary = "查询数据元数据规则关联信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpDataElemRuleRelDO>> list(DpDataElemRuleRelPageReqVO dpDataElemRuleRel) {
        PageResult<DpDataElemRuleRelDO> page = dpDataElemRuleRelService.getDpDataElemRuleRelPage(dpDataElemRuleRel);
        return CommonResult.success(page);
    }

    @Operation(summary = "导出数据元数据规则关联信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:export')")
    @Log(title = "数据元数据规则关联信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpDataElemRuleRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpDataElemRuleRelDO> list = (List<DpDataElemRuleRelDO>) dpDataElemRuleRelService.getDpDataElemRuleRelPage(exportReqVO).getRows();
        ExcelUtil<DpDataElemRuleRelRespVO> util = new ExcelUtil<>(DpDataElemRuleRelRespVO.class);
        util.exportExcel(response, DpDataElemRuleRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据元数据规则关联信息列表")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:import')")
    @Log(title = "数据元数据规则关联信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpDataElemRuleRelRespVO> util = new ExcelUtil<>(DpDataElemRuleRelRespVO.class);
        List<DpDataElemRuleRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpDataElemRuleRelService.importDpDataElemRuleRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据元数据规则关联信息详细信息")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpDataElemRuleRelRespVO> getInfo(@PathVariable("id") Long id) {
        DpDataElemRuleRelDO dpDataElemRuleRelDO = dpDataElemRuleRelService.getDpDataElemRuleRelById(id);
        return CommonResult.success(BeanUtils.toBean(dpDataElemRuleRelDO, DpDataElemRuleRelRespVO.class));
    }

    @Operation(summary = "新增数据元数据规则关联信息")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:add')")
    @Log(title = "数据元数据规则关联信息", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpDataElemRuleRelSaveReqVO dpDataElemRuleRel) {
        dpDataElemRuleRel.setCreatorId(getUserId());
        dpDataElemRuleRel.setCreateBy(getNickName());
        dpDataElemRuleRel.setCreateTime(DateUtil.date());
        return CommonResult.success(dpDataElemRuleRelService.createDpDataElemRuleRel(dpDataElemRuleRel));
    }

    @Operation(summary = "修改数据元数据规则关联信息")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:edit')")
    @Log(title = "数据元数据规则关联信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpDataElemRuleRelSaveReqVO dpDataElemRuleRel) {
        dpDataElemRuleRel.setUpdatorId(getUserId());
        dpDataElemRuleRel.setUpdateBy(getNickName());
        dpDataElemRuleRel.setUpdateTime(DateUtil.date());
        return CommonResult.success(dpDataElemRuleRelService.updateDpDataElemRuleRel(dpDataElemRuleRel));
    }

    @Operation(summary = "删除数据元数据规则关联信息")
//    @PreAuthorize("@ss.hasPermi('dp:dataElemRuleRel:remove')")
    @Log(title = "数据元数据规则关联信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpDataElemRuleRelService.removeDpDataElemRuleRel(Arrays.asList(ids)));
    }

}
