package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.audit.DaAssetAuditAlertConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditAlertService;

/**
 * 数据资产-质量预警Controller
 *
 * @author qdata
 * @date 2025-05-09
 */
@Tag(name = "数据资产-质量预警")
@RestController
@RequestMapping("/da/assetAuditAlert")
@Validated
public class DaAssetAuditAlertController extends BaseController {
    @Resource
    private IDaAssetAuditAlertService daAssetAuditAlertService;

    @Operation(summary = "查询数据资产-质量预警列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetAuditAlertRespVO>> list(DaAssetAuditAlertPageReqVO daAssetAuditAlert) {
        PageResult<DaAssetAuditAlertDO> page = daAssetAuditAlertService.getDaAssetAuditAlertPage(daAssetAuditAlert);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetAuditAlertRespVO.class));
    }

    @Operation(summary = "导出数据资产-质量预警列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:export')")
    @Log(title = "数据资产-质量预警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetAuditAlertPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetAuditAlertDO> list = (List<DaAssetAuditAlertDO>) daAssetAuditAlertService.getDaAssetAuditAlertPage(exportReqVO).getRows();
        ExcelUtil<DaAssetAuditAlertRespVO> util = new ExcelUtil<>(DaAssetAuditAlertRespVO.class);
        util.exportExcel(response, DaAssetAuditAlertConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-质量预警列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:import')")
    @Log(title = "数据资产-质量预警", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetAuditAlertRespVO> util = new ExcelUtil<>(DaAssetAuditAlertRespVO.class);
        List<DaAssetAuditAlertRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetAuditAlertService.importDaAssetAuditAlert(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-质量预警详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetAuditAlertRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetAuditAlertDO daAssetAuditAlertDO = daAssetAuditAlertService.getDaAssetAuditAlertById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetAuditAlertDO, DaAssetAuditAlertRespVO.class));
    }

    @Operation(summary = "新增数据资产-质量预警")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:add')")
    @Log(title = "数据资产-质量预警", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetAuditAlertSaveReqVO daAssetAuditAlert) {
        daAssetAuditAlert.setCreatorId(getUserId());
        daAssetAuditAlert.setCreateBy(getNickName());
        daAssetAuditAlert.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditAlertService.createDaAssetAuditAlert(daAssetAuditAlert));
    }

    @Operation(summary = "修改数据资产-质量预警")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:edit')")
    @Log(title = "数据资产-质量预警", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetAuditAlertSaveReqVO daAssetAuditAlert) {
        daAssetAuditAlert.setUpdatorId(getUserId());
        daAssetAuditAlert.setUpdateBy(getNickName());
        daAssetAuditAlert.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditAlertService.updateDaAssetAuditAlert(daAssetAuditAlert));
    }

    @Operation(summary = "删除数据资产-质量预警")
    @PreAuthorize("@ss.hasPermi('da:assetAuditAlert:remove')")
    @Log(title = "数据资产-质量预警", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetAuditAlertService.removeDaAssetAuditAlert(Arrays.asList(ids)));
    }

}
