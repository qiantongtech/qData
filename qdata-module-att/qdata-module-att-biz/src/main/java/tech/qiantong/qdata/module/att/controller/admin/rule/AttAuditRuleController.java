package tech.qiantong.qdata.module.att.controller.admin.rule;

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
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.convert.rule.AttAuditRuleConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;
import tech.qiantong.qdata.module.att.service.rule.IAttAuditRuleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 稽查规则Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "稽查规则")
@RestController
@RequestMapping("/att/auditRule")
@Validated
public class AttAuditRuleController extends BaseController {
    @Resource
    private IAttAuditRuleService attAuditRuleService;

    @Operation(summary = "查询稽查规则列表")
    @PreAuthorize("@ss.hasPermi('att:auditRule:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttAuditRuleRespVO>> list(AttAuditRulePageReqVO attAuditRule) {
        PageResult<AttAuditRuleDO> page = attAuditRuleService.getAttAuditRulePage(attAuditRule);
        return CommonResult.success(BeanUtils.toBean(page, AttAuditRuleRespVO.class));
    }

    @Operation(summary = "导出稽查规则列表")
    @PreAuthorize("@ss.hasPermi('att:auditRule:export')")
    @Log(title = "稽查规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttAuditRulePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttAuditRuleDO> list = (List<AttAuditRuleDO>) attAuditRuleService.getAttAuditRulePage(exportReqVO)
                .getRows();
        ExcelUtil<AttAuditRuleRespVO> util = new ExcelUtil<>(AttAuditRuleRespVO.class);
        util.exportExcel(response, AttAuditRuleConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入稽查规则列表")
    @PreAuthorize("@ss.hasPermi('att:auditRule:import')")
    @Log(title = "稽查规则", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttAuditRuleRespVO> util = new ExcelUtil<>(AttAuditRuleRespVO.class);
        List<AttAuditRuleRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attAuditRuleService.importAttAuditRule(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取稽查规则详细信息")
    @PreAuthorize("@ss.hasPermi('att:auditRule:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttAuditRuleRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttAuditRuleDO attAuditRuleDO = attAuditRuleService.getAttAuditRuleById(ID);
        return CommonResult.success(BeanUtils.toBean(attAuditRuleDO, AttAuditRuleRespVO.class));
    }

    @Operation(summary = "新增稽查规则")
    @PreAuthorize("@ss.hasPermi('att:auditRule:add')")
    @Log(title = "稽查规则", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttAuditRuleSaveReqVO attAuditRule) {
        attAuditRule.setCreatorId(getUserId());
        attAuditRule.setCreateBy(getNickName());
        attAuditRule.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attAuditRuleService.createAttAuditRule(attAuditRule));
    }

    @Operation(summary = "修改稽查规则")
    @PreAuthorize("@ss.hasPermi('att:auditRule:edit')")
    @Log(title = "稽查规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttAuditRuleSaveReqVO attAuditRule) {
        attAuditRule.setUpdatorId(getUserId());
        attAuditRule.setUpdateBy(getNickName());
        attAuditRule.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attAuditRuleService.updateAttAuditRule(attAuditRule));
    }

    @Operation(summary = "删除稽查规则")
    @PreAuthorize("@ss.hasPermi('att:auditRule:remove')")
    @Log(title = "稽查规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attAuditRuleService.removeAttAuditRule(Arrays.asList(ids)));
    }

    @Operation(summary = "获取稽查规则树形结构")
    @GetMapping("/tree")
    public CommonResult<List<AttAuditRuleRespVO>> tree(@RequestParam Long dataElemId) {
        List<AttAuditRuleRespVO> tree = attAuditRuleService.getAttAuditRuleTree(dataElemId);
        return CommonResult.success(tree);
    }

}
