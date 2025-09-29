package tech.qiantong.qdata.module.da.controller.admin.daAssetApply;

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
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.convert.daAssetApply.DaAssetApplyConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.module.da.service.daAssetApply.IDaAssetApplyService;

/**
 * 数据资产申请Controller
 *
 * @author shu
 * @date 2025-03-19
 */
@Tag(name = "数据资产申请")
@RestController
@RequestMapping("/da/assetApply")
@Validated
public class DaAssetApplyController extends BaseController {
    @Resource
    private IDaAssetApplyService daAssetApplyService;

    @Operation(summary = "查询数据资产申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetApply:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetApplyRespVO>> list(DaAssetApplyPageReqVO daAssetApply) {
        daAssetApply.setOrderByColumn("status");
        PageResult<DaAssetApplyDO> page = daAssetApplyService.getDaAssetApplyPage(daAssetApply);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetApplyRespVO.class));
    }

    @Operation(summary = "导出数据资产申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetApply:export')")
    @Log(title = "数据资产申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetApplyPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetApplyDO> list = (List<DaAssetApplyDO>) daAssetApplyService.getDaAssetApplyPage(exportReqVO).getRows();
        ExcelUtil<DaAssetApplyRespVO> util = new ExcelUtil<>(DaAssetApplyRespVO.class);
        util.exportExcel(response, DaAssetApplyConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetApply:import')")
    @Log(title = "数据资产申请", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetApplyRespVO> util = new ExcelUtil<>(DaAssetApplyRespVO.class);
        List<DaAssetApplyRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetApplyService.importDaAssetApply(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产申请详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetApply:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetApplyRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetApplyDO daAssetApplyDO = daAssetApplyService.getDaAssetApplyById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetApplyDO, DaAssetApplyRespVO.class));
    }

    @Operation(summary = "新增数据资产申请")
    @PreAuthorize("@ss.hasAnyPermi('da:assetApply:add,da:asset:asset:add')")
    @Log(title = "数据资产申请", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetApplySaveReqVO daAssetApply) {
        daAssetApply.setCreatorId(getUserId());
        daAssetApply.setCreateBy(getNickName());
        daAssetApply.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApplyService.createDaAssetApply(daAssetApply));
    }

    @Operation(summary = "修改数据资产申请")
    @PreAuthorize("@ss.hasPermi('da:assetApply:edit')")
    @Log(title = "数据资产申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetApplySaveReqVO daAssetApply) {
        daAssetApply.setUpdatorId(getUserId());
        daAssetApply.setUpdateBy(getNickName());
        daAssetApply.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApplyService.updateDaAssetApply(daAssetApply));
    }

    @Operation(summary = "删除数据资产申请")
    @PreAuthorize("@ss.hasPermi('da:assetApply:remove')")
    @Log(title = "数据资产申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetApplyService.removeDaAssetApply(Arrays.asList(ids)));
    }

}
