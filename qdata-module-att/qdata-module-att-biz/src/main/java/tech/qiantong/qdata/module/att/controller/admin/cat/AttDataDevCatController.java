package tech.qiantong.qdata.module.att.controller.admin.cat;

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
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttDataDevCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttDataDevCatService;

/**
 * 数据开发类目管理Controller
 *
 * @author qdata
 * @date 2025-03-11
 */
@Tag(name = "数据开发类目管理")
@RestController
@RequestMapping("/att/dataDevCat")
@Validated
public class AttDataDevCatController extends BaseController {
    @Resource
    private IAttDataDevCatService attDataDevCatService;

    @Operation(summary = "查询数据开发类目管理列表")
    @GetMapping("/list")
    public CommonResult<List<AttDataDevCatRespVO>> list(AttDataDevCatPageReqVO attDataDevCat) {
        List<AttDataDevCatDO> attDataDevCatDOList = attDataDevCatService.getAttDataDevCatList(attDataDevCat);
        return CommonResult.success(BeanUtils.toBean(attDataDevCatDOList, AttDataDevCatRespVO.class));
    }

    @Operation(summary = "导出数据开发类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:export')")
    @Log(title = "数据开发类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttDataDevCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttDataDevCatDO> list = (List<AttDataDevCatDO>) attDataDevCatService.getAttDataDevCatPage(exportReqVO).getRows();
        ExcelUtil<AttDataDevCatRespVO> util = new ExcelUtil<>(AttDataDevCatRespVO.class);
        util.exportExcel(response, AttDataDevCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据开发类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:import')")
    @Log(title = "数据开发类目管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttDataDevCatRespVO> util = new ExcelUtil<>(AttDataDevCatRespVO.class);
        List<AttDataDevCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attDataDevCatService.importAttDataDevCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据开发类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttDataDevCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttDataDevCatDO attDataDevCatDO = attDataDevCatService.getAttDataDevCatById(id);
        return CommonResult.success(BeanUtils.toBean(attDataDevCatDO, AttDataDevCatRespVO.class));
    }

    @Operation(summary = "新增数据开发类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:add')")
    @Log(title = "数据开发类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttDataDevCatSaveReqVO attDataDevCat) {
        attDataDevCat.setCreatorId(getUserId());
        attDataDevCat.setCreateBy(getNickName());
        attDataDevCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attDataDevCatService.createAttDataDevCat(attDataDevCat));
    }

    @Operation(summary = "修改数据开发类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:edit')")
    @Log(title = "数据开发类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttDataDevCatSaveReqVO attDataDevCat) {
        attDataDevCat.setUpdatorId(getUserId());
        attDataDevCat.setUpdateBy(getNickName());
        attDataDevCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attDataDevCatService.updateAttDataDevCat(attDataDevCat));
    }

    @Operation(summary = "删除数据开发类目管理")
    @PreAuthorize("@ss.hasPermi('att:dataDevCat:remove')")
    @Log(title = "数据开发类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attDataDevCatService.removeAttDataDevCat(Arrays.asList(ids)));
    }

}
