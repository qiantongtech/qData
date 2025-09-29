package tech.qiantong.qdata.module.att.controller.admin.cat;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttModelCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttModelCatService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 逻辑模型类目管理Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "逻辑模型类目管理")
@RestController
@RequestMapping("/att/modelCat")
@Validated
public class AttModelCatController extends BaseController {
    @Resource
    private IAttModelCatService attModelCatService;

    @Operation(summary = "查询逻辑模型类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:modelCat:list')")
    @GetMapping("/list")
    public CommonResult<List<AttModelCatRespVO>> list(AttModelCatPageReqVO attModelCat) {
        List<AttModelCatDO> attModelCatDOList = attModelCatService.getAttModelCatList(attModelCat);
        return CommonResult.success(BeanUtils.toBean(attModelCatDOList, AttModelCatRespVO.class));
    }

    @Operation(summary = "导出逻辑模型类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:modelCat:export')")
    @Log(title = "逻辑模型类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttModelCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttModelCatDO> list = (List<AttModelCatDO>) attModelCatService.getAttModelCatPage(exportReqVO).getRows();
        ExcelUtil<AttModelCatRespVO> util = new ExcelUtil<>(AttModelCatRespVO.class);
        util.exportExcel(response, AttModelCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入逻辑模型类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:modelCat:import')")
    @Log(title = "逻辑模型类目管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttModelCatRespVO> util = new ExcelUtil<>(AttModelCatRespVO.class);
        List<AttModelCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attModelCatService.importAttModelCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取逻辑模型类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:modelCat:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttModelCatRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttModelCatDO attModelCatDO = attModelCatService.getAttModelCatById(ID);
        return CommonResult.success(BeanUtils.toBean(attModelCatDO, AttModelCatRespVO.class));
    }

    @Operation(summary = "新增逻辑模型类目管理")
    @PreAuthorize("@ss.hasPermi('att:modelCat:add')")
    @Log(title = "逻辑模型类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttModelCatSaveReqVO attModelCat) {
        attModelCat.setCreatorId(getUserId());
        attModelCat.setCreateBy(getNickName());
        attModelCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attModelCatService.createAttModelCat(attModelCat));
    }

    @Operation(summary = "修改逻辑模型类目管理")
    @PreAuthorize("@ss.hasPermi('att:modelCat:edit')")
    @Log(title = "逻辑模型类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttModelCatSaveReqVO attModelCat) {
        attModelCat.setUpdatorId(getUserId());
        attModelCat.setUpdateBy(getNickName());
        attModelCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attModelCatService.updateAttModelCat(attModelCat));
    }

    //    @Operation(summary = "删除逻辑模型类目管理")
//    @PreAuthorize("@ss.hasPermi('att:modelCat:remove')")
//    @Log(title = "逻辑模型类目管理", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{IDs}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(attModelCatService.removeAttModelCat(Arrays.asList(ids)));
//    }
    //删除
    @Operation(summary = "删除逻辑模型类目管理")
    @PreAuthorize("@ss.hasPermi('att:modelCat:remove')")
    @Log(title = "逻辑模型类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ID}")
    public CommonResult<Integer> remove(@PathVariable Long ID) {
        return CommonResult.toAjax(attModelCatService.removeAttModelCat(ID));
    }

}
