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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.audit.DaAssetAuditRuleConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditRuleService;

/**
 * 数据资产质量结果记录Controller
 *
 * @author qdata
 * @date 2025-05-09
 */
@Tag(name = "数据资产质量结果记录")
@RestController
@RequestMapping("/da/assetAuditRule")
@Validated
public class DaAssetAuditRuleController extends BaseController {
    @Resource
    private IDaAssetAuditRuleService daAssetAuditRuleService;

    @Operation(summary = "查询数据资产质量结果记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetAuditRuleRespVO>> list(DaAssetAuditRulePageReqVO daAssetAuditRule) {
        PageResult<DaAssetAuditRuleDO> page = daAssetAuditRuleService.getDaAssetAuditRulePage(daAssetAuditRule);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetAuditRuleRespVO.class));
    }

    @Operation(summary = "导出数据资产质量结果记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:export')")
    @Log(title = "数据资产质量结果记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetAuditRulePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetAuditRuleDO> list = (List<DaAssetAuditRuleDO>) daAssetAuditRuleService.getDaAssetAuditRulePage(exportReqVO).getRows();
        ExcelUtil<DaAssetAuditRuleRespVO> util = new ExcelUtil<>(DaAssetAuditRuleRespVO.class);
        util.exportExcel(response, DaAssetAuditRuleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产质量结果记录列表")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:import')")
    @Log(title = "数据资产质量结果记录", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetAuditRuleRespVO> util = new ExcelUtil<>(DaAssetAuditRuleRespVO.class);
        List<DaAssetAuditRuleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetAuditRuleService.importDaAssetAuditRule(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产质量结果记录详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetAuditRuleRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetAuditRuleDO daAssetAuditRuleDO = daAssetAuditRuleService.getDaAssetAuditRuleById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetAuditRuleDO, DaAssetAuditRuleRespVO.class));
    }

    @Operation(summary = "新增数据资产质量结果记录")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:add')")
    @Log(title = "数据资产质量结果记录", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetAuditRuleSaveReqVO daAssetAuditRule) {
        daAssetAuditRule.setCreatorId(getUserId());
        daAssetAuditRule.setCreateBy(getNickName());
        daAssetAuditRule.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditRuleService.createDaAssetAuditRule(daAssetAuditRule));
    }

    @Operation(summary = "修改数据资产质量结果记录")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:edit')")
    @Log(title = "数据资产质量结果记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetAuditRuleSaveReqVO daAssetAuditRule) {
        daAssetAuditRule.setUpdatorId(getUserId());
        daAssetAuditRule.setUpdateBy(getNickName());
        daAssetAuditRule.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetAuditRuleService.updateDaAssetAuditRule(daAssetAuditRule));
    }

    @Operation(summary = "删除数据资产质量结果记录")
    @PreAuthorize("@ss.hasPermi('da:assetAuditRule:remove')")
    @Log(title = "数据资产质量结果记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetAuditRuleService.removeDaAssetAuditRule(Arrays.asList(ids)));
    }

}
