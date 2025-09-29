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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.audit.DaAssetAuditScheduleConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditScheduleService;

/**
 * 资产稽查调度Controller
 *
 * @author qdata
 * @date 2025-05-09
 */
@Tag(name = "资产稽查调度")
@RestController
@RequestMapping("/da/assetAuditSchedule")
@Validated
public class DaAssetAuditScheduleController extends BaseController {
    @Resource
    private IDaAssetAuditScheduleService daAssetAuditScheduleService;

    @Operation(summary = "查询资产稽查调度列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetAuditScheduleRespVO>> list(DaAssetAuditSchedulePageReqVO daAssetAuditSchedule) {
        PageResult<DaAssetAuditScheduleDO> page = daAssetAuditScheduleService.getDaAssetAuditSchedulePage(daAssetAuditSchedule);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetAuditScheduleRespVO.class));
    }

    @Operation(summary = "导出资产稽查调度列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:export')")
    @Log(title = "资产稽查调度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetAuditSchedulePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetAuditScheduleDO> list = (List<DaAssetAuditScheduleDO>) daAssetAuditScheduleService.getDaAssetAuditSchedulePage(exportReqVO).getRows();
        ExcelUtil<DaAssetAuditScheduleRespVO> util = new ExcelUtil<>(DaAssetAuditScheduleRespVO.class);
        util.exportExcel(response, DaAssetAuditScheduleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入资产稽查调度列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:import')")
    @Log(title = "资产稽查调度", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetAuditScheduleRespVO> util = new ExcelUtil<>(DaAssetAuditScheduleRespVO.class);
        List<DaAssetAuditScheduleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetAuditScheduleService.importDaAssetAuditSchedule(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取资产稽查调度详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetAuditScheduleRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetAuditScheduleDO daAssetAuditScheduleDO = daAssetAuditScheduleService.getDaAssetAuditScheduleById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetAuditScheduleDO, DaAssetAuditScheduleRespVO.class));
    }

    @Operation(summary = "新增资产稽查调度")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:add')")
    @Log(title = "资产稽查调度", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetAuditScheduleSaveReqVO daAssetAuditSchedule) {
        daAssetAuditSchedule.setCreatorId(getUserId());
        daAssetAuditSchedule.setCreateBy(getNickName());
        daAssetAuditSchedule.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditScheduleService.createDaAssetAuditSchedule(daAssetAuditSchedule));
    }

    @Operation(summary = "修改资产稽查调度")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:edit')")
    @Log(title = "资产稽查调度", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetAuditScheduleSaveReqVO daAssetAuditSchedule) {
        daAssetAuditSchedule.setUpdatorId(getUserId());
        daAssetAuditSchedule.setUpdateBy(getNickName());
        daAssetAuditSchedule.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditScheduleService.updateDaAssetAuditSchedule(daAssetAuditSchedule));
    }

    @Operation(summary = "删除资产稽查调度")
    @PreAuthorize("@ss.hasPermi('da:assetAuditSchedule:remove')")
    @Log(title = "资产稽查调度", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetAuditScheduleService.removeDaAssetAuditSchedule(Arrays.asList(ids)));
    }

}
