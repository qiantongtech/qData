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
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeSaveReqVO;
import tech.qiantong.qdata.module.dp.convert.dataElem.DpDataElemCodeConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemCodeService;

/**
 * 数据元代码Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "数据元代码")
@RestController
@RequestMapping("/dp/dataElemCode")
@Validated
public class DpDataElemCodeController extends BaseController {
    @Resource
    private IDpDataElemCodeService dpDataElemCodeService;

    @Operation(summary = "查询数据元代码列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DpDataElemCodeRespVO>> list(DpDataElemCodePageReqVO dpDataElemCode) {
        PageResult<DpDataElemCodeDO> page = dpDataElemCodeService.getDpDataElemCodePage(dpDataElemCode);
        return CommonResult.success(BeanUtils.toBean(page, DpDataElemCodeRespVO.class));
    }

    @Operation(summary = "导出数据元代码列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:export')")
    @Log(title = "数据元代码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpDataElemCodePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpDataElemCodeDO> list = (List<DpDataElemCodeDO>) dpDataElemCodeService.getDpDataElemCodePage(exportReqVO).getRows();
        ExcelUtil<DpDataElemCodeRespVO> util = new ExcelUtil<>(DpDataElemCodeRespVO.class);
        util.exportExcel(response, DpDataElemCodeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据元代码列表")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:import')")
    @Log(title = "数据元代码", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpDataElemCodeRespVO> util = new ExcelUtil<>(DpDataElemCodeRespVO.class);
        List<DpDataElemCodeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpDataElemCodeService.importDpDataElemCode(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据元代码详细信息")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DpDataElemCodeRespVO> getInfo(@PathVariable("id") Long id) {
        DpDataElemCodeDO dpDataElemCodeDO = dpDataElemCodeService.getDpDataElemCodeById(id);
        return CommonResult.success(BeanUtils.toBean(dpDataElemCodeDO, DpDataElemCodeRespVO.class));
    }

    @Operation(summary = "新增数据元代码")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:add')")
    @Log(title = "数据元代码", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpDataElemCodeSaveReqVO dpDataElemCode) {
        dpDataElemCode.setCreatorId(getUserId());
        dpDataElemCode.setCreateBy(getNickName());
        dpDataElemCode.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpDataElemCodeService.createDpDataElemCode(dpDataElemCode));
    }

    @Operation(summary = "修改数据元代码")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:edit')")
    @Log(title = "数据元代码", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpDataElemCodeSaveReqVO dpDataElemCode) {
        dpDataElemCode.setUpdatorId(getUserId());
        dpDataElemCode.setUpdateBy(getNickName());
        dpDataElemCode.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpDataElemCodeService.updateDpDataElemCode(dpDataElemCode));
    }

    @Operation(summary = "删除数据元代码")
    @PreAuthorize("@ss.hasPermi('dp:dataElemCode:remove')")
    @Log(title = "数据元代码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dpDataElemCodeService.removeDpDataElemCode(Arrays.asList(ids)));
    }

    @Operation(summary = "校验源代码值")
    @GetMapping("/validateCodeValue")
    public CommonResult<Integer> validateCodeValue(@RequestParam String dataElemId, @RequestParam String codeValue
            , @RequestParam(required = false) String id) {
        return CommonResult.success(dpDataElemCodeService.validateCodeValue(dataElemId, codeValue, id));
    }

}
