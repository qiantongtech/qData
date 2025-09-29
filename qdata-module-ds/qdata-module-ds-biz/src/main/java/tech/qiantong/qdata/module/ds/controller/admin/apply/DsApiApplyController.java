package tech.qiantong.qdata.module.ds.controller.admin.apply;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplySaveReqVO;
import tech.qiantong.qdata.module.ds.convert.apply.DsApiApplyConvert;
import tech.qiantong.qdata.module.ds.dal.dataobject.apply.DsApiApplyDO;
import tech.qiantong.qdata.module.ds.service.apply.IDsApiApplyService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * API服务-申请Controller
 *
 * @author qdata
 * @date 2025-04-22
 */
@Tag(name = "API服务-申请")
@RestController
@RequestMapping("/da/apply")
@Validated
public class DsApiApplyController extends BaseController {
    @Resource
    private IDsApiApplyService dsApiApplyService;

    @Operation(summary = "查询API服务-申请列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DsApiApplyRespVO>> list(DsApiApplyPageReqVO dsApiApply) {
        PageResult<DsApiApplyDO> page = dsApiApplyService.getDsApiApplyPage(dsApiApply);
        return CommonResult.success(BeanUtils.toBean(page, DsApiApplyRespVO.class));
    }

    @Operation(summary = "导出API服务-申请列表")
//    @Log(title = "API服务-申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DsApiApplyPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DsApiApplyDO> list = (List<DsApiApplyDO>) dsApiApplyService.getDsApiApplyPage(exportReqVO).getRows();
        ExcelUtil<DsApiApplyRespVO> util = new ExcelUtil<>(DsApiApplyRespVO.class);
        util.exportExcel(response, DsApiApplyConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入API服务-申请列表")
//    @Log(title = "API服务-申请", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public CommonResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DsApiApplyRespVO> util = new ExcelUtil<>(DsApiApplyRespVO.class);
        List<DsApiApplyRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dsApiApplyService.importDsApiApply(importExcelList, updateSupport, operName);
        return CommonResult.success(message);
    }

    @Operation(summary = "获取API服务-申请详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DsApiApplyRespVO> getInfo(@PathVariable("id") Long id) {
        DsApiApplyDO dsApiApplyDO = dsApiApplyService.getDsApiApplyById(id);
        return CommonResult.success(BeanUtils.toBean(dsApiApplyDO, DsApiApplyRespVO.class));
    }

    @Operation(summary = "获取API服务-申请详细信息")
    @PostMapping(value = "/flyfowApiApply")
    public AjaxResult flyfowApiApply(@RequestBody DsApiApplySaveReqVO dsApiApply) {
        Map<String,Object> map = dsApiApplyService.getFlyfowApiApply(dsApiApply);
        return AjaxResult.success(map);
    }

    @Operation(summary = "新增API服务-申请")
//    @Log(title = "API服务-申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody DsApiApplySaveReqVO dsApiApply) {
        Map<String, Long> apiApplyMap = dsApiApplyService.createDsApiApply(dsApiApply);
        return AjaxResult.success(apiApplyMap);
    }

    @Operation(summary = "修改API服务-申请")
//    @Log(title = "API服务-申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DsApiApplySaveReqVO dsApiApply) {
        return CommonResult.toAjax(dsApiApplyService.updateDsApiApply(dsApiApply));
    }

    @Operation(summary = "删除API服务-申请")
//    @Log(title = "API服务-申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public CommonResult<Integer> remove(@PathVariable("id") Long id) {
        return CommonResult.toAjax(dsApiApplyService.removeDsApiApply(Arrays.asList(id)));
    }

}
