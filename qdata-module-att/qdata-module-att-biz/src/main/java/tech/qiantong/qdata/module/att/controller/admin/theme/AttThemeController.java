package tech.qiantong.qdata.module.att.controller.admin.theme;

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
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.convert.theme.AttThemeConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;
import tech.qiantong.qdata.module.att.service.theme.IAttThemeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 主题Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "主题")
@RestController
@RequestMapping("/att/theme")
@Validated
public class AttThemeController extends BaseController {
    @Resource
    private IAttThemeService attThemeService;

    @Operation(summary = "查询主题列表")
    @PreAuthorize("@ss.hasPermi('att:theme:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttThemeRespVO>> list(AttThemePageReqVO attTheme) {
        PageResult<AttThemeDO> page = attThemeService.getAttThemePage(attTheme);
        return CommonResult.success(BeanUtils.toBean(page, AttThemeRespVO.class));
    }
    @Operation(summary = "查询主题列表")
    @GetMapping("/getAttThemeListByReqVO")
    public CommonResult<List<AttThemeRespVO>> getAttThemeListByReqVO(AttThemePageReqVO attTheme) {
        List<AttThemeDO> list = attThemeService.getAttThemeListByReqVO(attTheme);
        return CommonResult.success(BeanUtils.toBean(list, AttThemeRespVO.class));
    }

    @Operation(summary = "导出主题列表")
    @PreAuthorize("@ss.hasPermi('att:theme:export')")
    @Log(title = "主题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttThemePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttThemeDO> list = (List<AttThemeDO>) attThemeService.getAttThemePage(exportReqVO).getRows();
        ExcelUtil<AttThemeRespVO> util = new ExcelUtil<>(AttThemeRespVO.class);
        util.exportExcel(response, AttThemeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入主题列表")
    @PreAuthorize("@ss.hasPermi('att:theme:import')")
    @Log(title = "主题", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttThemeRespVO> util = new ExcelUtil<>(AttThemeRespVO.class);
        List<AttThemeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attThemeService.importAttTheme(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取主题详细信息")
    @PreAuthorize("@ss.hasPermi('att:theme:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttThemeRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttThemeDO attThemeDO = attThemeService.getAttThemeById(ID);
        return CommonResult.success(BeanUtils.toBean(attThemeDO, AttThemeRespVO.class));
    }

    @Operation(summary = "新增主题")
    @PreAuthorize("@ss.hasPermi('att:theme:add')")
    @Log(title = "主题", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttThemeSaveReqVO attTheme) {
        attTheme.setCreatorId(getUserId());
        attTheme.setCreateBy(getNickName());
        attTheme.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attThemeService.createAttTheme(attTheme));
    }

    @Operation(summary = "修改主题")
    @PreAuthorize("@ss.hasPermi('att:theme:edit')")
    @Log(title = "主题", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttThemeSaveReqVO attTheme) {
        attTheme.setUpdatorId(getUserId());
        attTheme.setUpdateBy(getNickName());
        attTheme.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attThemeService.updateAttTheme(attTheme));
    }

    @Operation(summary = "删除主题")
    @PreAuthorize("@ss.hasPermi('att:theme:remove')")
    @Log(title = "主题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attThemeService.removeAttTheme(Arrays.asList(ids)));
    }

}
