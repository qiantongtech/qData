package tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel;

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
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.convert.sensitiveLevel.DaSensitiveLevelConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.module.da.service.sensitiveLevel.IDaSensitiveLevelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 敏感等级Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "敏感等级")
@RestController
@RequestMapping("/da/sensitiveLevel")
@Validated
public class DaSensitiveLevelController extends BaseController {
    @Resource
    private IDaSensitiveLevelService daSensitiveLevelService;

    @Operation(summary = "查询敏感等级列表")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaSensitiveLevelRespVO>> list(DaSensitiveLevelPageReqVO daSensitiveLevel) {
        PageResult<DaSensitiveLevelDO> page = daSensitiveLevelService.getDaSensitiveLevelPage(daSensitiveLevel);
        return CommonResult.success(BeanUtils.toBean(page, DaSensitiveLevelRespVO.class));
    }

    @Operation(summary = "导出敏感等级列表")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:export')")
    @Log(title = "敏感等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaSensitiveLevelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaSensitiveLevelDO> list = (List<DaSensitiveLevelDO>) daSensitiveLevelService.getDaSensitiveLevelPage(exportReqVO).getRows();
        ExcelUtil<DaSensitiveLevelRespVO> util = new ExcelUtil<>(DaSensitiveLevelRespVO.class);
        util.exportExcel(response, DaSensitiveLevelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入敏感等级列表")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:import')")
    @Log(title = "敏感等级", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaSensitiveLevelRespVO> util = new ExcelUtil<>(DaSensitiveLevelRespVO.class);
        List<DaSensitiveLevelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daSensitiveLevelService.importDaSensitiveLevel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取敏感等级详细信息")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaSensitiveLevelRespVO> getInfo(@PathVariable("id") Long id) {
        DaSensitiveLevelDO daSensitiveLevelDO = daSensitiveLevelService.getDaSensitiveLevelById(id);
        return CommonResult.success(BeanUtils.toBean(daSensitiveLevelDO, DaSensitiveLevelRespVO.class));
    }

    @Operation(summary = "新增敏感等级")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:add')")
    @Log(title = "敏感等级", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaSensitiveLevelSaveReqVO daSensitiveLevel) {
        daSensitiveLevel.setCreatorId(getUserId());
        daSensitiveLevel.setCreateBy(getNickName());
        daSensitiveLevel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daSensitiveLevelService.createDaSensitiveLevel(daSensitiveLevel));
    }

    @Operation(summary = "修改敏感等级")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:edit')")
    @Log(title = "敏感等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaSensitiveLevelSaveReqVO daSensitiveLevel) {
        daSensitiveLevel.setUpdatorId(getUserId());
        daSensitiveLevel.setUpdateBy(getNickName());
        daSensitiveLevel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daSensitiveLevelService.updateDaSensitiveLevel(daSensitiveLevel));
    }

    @Operation(summary = "修改敏感等级状态")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:edit')")
    @Log(title = "敏感等级", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus/{id}/{status}")
    public AjaxResult updateStatus(@PathVariable Long id, @PathVariable Long status) {
        if (!daSensitiveLevelService.updateStatus(id,status)){
            return AjaxResult.error("已被使用，不允许下线！");
        }
        return AjaxResult.success("修改成功");
    }

    @Operation(summary = "删除敏感等级")
    @PreAuthorize("@ss.hasPermi('da:sensitiveLevel:remove')")
    @Log(title = "敏感等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daSensitiveLevelService.removeDaSensitiveLevel(Arrays.asList(ids)));
    }

}
