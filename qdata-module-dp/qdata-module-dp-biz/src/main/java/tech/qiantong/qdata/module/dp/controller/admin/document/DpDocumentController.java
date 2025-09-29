package tech.qiantong.qdata.module.dp.controller.admin.document;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.convert.document.DpDocumentConvert;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 标准信息登记Controller
 *
 * @author qdata
 * @date 2025-08-21
 */
@Tag(name = "标准信息登记")
@RestController
@RequestMapping("/dp/document")
@Validated
public class DpDocumentController extends BaseController {
    @Resource
    private IDpDocumentService dpDocumentService;

    @Operation(summary = "查询标准信息登记列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DpDocumentRespVO>> list(DpDocumentPageReqVO dpDocument) {
        PageResult<DpDocumentDO> page = dpDocumentService.getDpDocumentPage(dpDocument);
        return CommonResult.success(BeanUtils.toBean(page, DpDocumentRespVO.class));
    }


    @Operation(summary = "查询标准信息登记列表")
    @GetMapping("/getDpDocumentList")
    public CommonResult<List<DpDocumentRespVO>> getDpDocumentList(DpDocumentPageReqVO dpDocument) {
        List<DpDocumentDO> list = dpDocumentService.getDpDocumentList(dpDocument);
        return CommonResult.success(BeanUtils.toBean(list, DpDocumentRespVO.class));
    }

    @Operation(summary = "导出标准信息登记列表")
    @Log(title = "标准信息登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DpDocumentPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DpDocumentDO> list = (List<DpDocumentDO>) dpDocumentService.getDpDocumentPage(exportReqVO).getRows();
        ExcelUtil<DpDocumentRespVO> util = new ExcelUtil<>(DpDocumentRespVO.class);
        util.exportExcel(response, DpDocumentConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入标准信息登记列表")
    @Log(title = "标准信息登记", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DpDocumentRespVO> util = new ExcelUtil<>(DpDocumentRespVO.class);
        List<DpDocumentRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dpDocumentService.importDpDocument(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取标准信息登记详细信息")
    @GetMapping(value = "/{ID}")
    public CommonResult<DpDocumentRespVO> getInfo(@PathVariable("ID") Long ID) {
        DpDocumentDO dpDocumentDO = dpDocumentService.getDpDocumentById(ID);
        return CommonResult.success(BeanUtils.toBean(dpDocumentDO, DpDocumentRespVO.class));
    }

    @Operation(summary = "新增标准信息登记")
    @Log(title = "标准信息登记", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DpDocumentSaveReqVO dpDocument) {
        dpDocument.setCreatorId(getUserId());
        dpDocument.setCreateBy(getNickName());
        dpDocument.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dpDocumentService.createDpDocument(dpDocument));
    }

    @Operation(summary = "修改标准信息登记")
    @Log(title = "标准信息登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DpDocumentSaveReqVO dpDocument) {
        dpDocument.setUpdatorId(getUserId());
        dpDocument.setUpdateBy(getNickName());
        dpDocument.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dpDocumentService.updateDpDocument(dpDocument));
    }
//
//    @Operation(summary = "删除标准信息登记")
//    @PreAuthorize("@ss.hasPermi('dp:document:document:remove')")
//    @Log(title = "标准信息登记", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{IDs}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(dpDocumentService.removeDpDocument(Arrays.asList(ids)));
//    }

    @Operation(summary = "删除标准信息登记")
    @Log(title = "标准信息登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public CommonResult<Integer> remove(@PathVariable Long id) {
        return CommonResult.toAjax(dpDocumentService.removeDpDocument(Arrays.asList(id)));
    }

    @Operation(summary = "查询标准检索列表")
    @GetMapping("/search")
    public CommonResult<PageResult<DpDocumentSearchRespVO>> search(DpDocumentSearchReqVO dpDocument) {
        return CommonResult.success(dpDocumentService.getDpDocumentSearchPage(dpDocument));
    }
}
