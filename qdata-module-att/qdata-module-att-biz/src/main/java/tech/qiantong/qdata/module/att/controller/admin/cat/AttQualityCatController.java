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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttQualityCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttQualityCatService;

/**
 * 数据质量类目Controller
 *
 * @author qdata
 * @date 2025-07-19
 */
@Tag(name = "数据质量类目")
@RestController
@RequestMapping("/att/qualityCat")
@Validated
public class AttQualityCatController extends BaseController {
    @Resource
    private IAttQualityCatService attQualityCatService;

    @Operation(summary = "查询数据质量类目列表")
    @GetMapping("/list")
    public CommonResult<List<AttQualityCatRespVO>> list(AttQualityCatPageReqVO attQualityCat) {
        List<AttQualityCatDO> page = attQualityCatService.getAttQualityCatList(attQualityCat);
        return CommonResult.success(BeanUtils.toBean(page, AttQualityCatRespVO.class));
    }

    @Operation(summary = "导出数据质量类目列表")
    @Log(title = "数据质量类目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttQualityCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttQualityCatDO> list = (List<AttQualityCatDO>) attQualityCatService.getAttQualityCatPage(exportReqVO).getRows();
        ExcelUtil<AttQualityCatRespVO> util = new ExcelUtil<>(AttQualityCatRespVO.class);
        util.exportExcel(response, AttQualityCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量类目列表")
    @PreAuthorize("@ss.hasPermi('att:qualityCat:import')")
    @Log(title = "数据质量类目", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttQualityCatRespVO> util = new ExcelUtil<>(AttQualityCatRespVO.class);
        List<AttQualityCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attQualityCatService.importAttQualityCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量类目详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<AttQualityCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttQualityCatDO attQualityCatDO = attQualityCatService.getAttQualityCatById(id);
        return CommonResult.success(BeanUtils.toBean(attQualityCatDO, AttQualityCatRespVO.class));
    }

    @Operation(summary = "新增数据质量类目")
    @PreAuthorize("@ss.hasPermi('att:qualityCat:add')")
    @Log(title = "数据质量类目", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttQualityCatSaveReqVO attQualityCat) {
        attQualityCat.setCreatorId(getUserId());
        attQualityCat.setCreateBy(getNickName());
        attQualityCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attQualityCatService.createAttQualityCat(attQualityCat));
    }

    @Operation(summary = "修改数据质量类目")
    @PreAuthorize("@ss.hasPermi('att:qualityCat:edit')")
    @Log(title = "数据质量类目", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttQualityCatSaveReqVO attQualityCat) {
        attQualityCat.setUpdatorId(getUserId());
        attQualityCat.setUpdateBy(getNickName());
        attQualityCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attQualityCatService.updateAttQualityCat(attQualityCat));
    }

    @Operation(summary = "删除数据质量类目")
    @PreAuthorize("@ss.hasPermi('att:qualityCat:remove')")
    @Log(title = "数据质量类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attQualityCatService.removeAttQualityCat(Arrays.asList(ids)));
    }

}
