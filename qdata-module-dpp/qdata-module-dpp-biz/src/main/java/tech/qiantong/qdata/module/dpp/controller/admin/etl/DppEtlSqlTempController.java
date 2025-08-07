package tech.qiantong.qdata.module.dpp.controller.admin.etl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlSqlTempConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSqlTempService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据集成SQL模版Controller
 *
 * @author FXB
 * @date 2025-06-25
 */
@Tag(name = "数据集成SQL模版")
@RestController
@RequestMapping("/dpp/dppEtlSqlTemp")
@Validated
public class DppEtlSqlTempController extends BaseController {
    @Resource
    private IDppEtlSqlTempService dppEtlSqlTempService;

    @Operation(summary = "查询数据集成SQL模版列表")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlSqlTempRespVO>> list(DppEtlSqlTempPageReqVO dppEtlSqlTemp) {
        PageResult<DppEtlSqlTempDO> page = dppEtlSqlTempService.getDppEtlSqlTempPage(dppEtlSqlTemp);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlSqlTempRespVO.class));
    }

    @Operation(summary = "导出数据集成SQL模版列表")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:export')")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlSqlTempPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlSqlTempDO> list = (List<DppEtlSqlTempDO>) dppEtlSqlTempService.getDppEtlSqlTempPage(exportReqVO).getRows();
        ExcelUtil<DppEtlSqlTempRespVO> util = new ExcelUtil<>(DppEtlSqlTempRespVO.class);
        util.exportExcel(response, DppEtlSqlTempConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成SQL模版列表")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:import')")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlSqlTempRespVO> util = new ExcelUtil<>(DppEtlSqlTempRespVO.class);
        List<DppEtlSqlTempRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlSqlTempService.importDppEtlSqlTemp(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成SQL模版详细信息")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlSqlTempRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlSqlTempDO dppEtlSqlTempDO = dppEtlSqlTempService.getDppEtlSqlTempById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlSqlTempDO, DppEtlSqlTempRespVO.class));
    }

    @Operation(summary = "新增数据集成SQL模版")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:add')")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlSqlTempSaveReqVO dppEtlSqlTemp) {
        dppEtlSqlTemp.setCreatorId(getUserId());
        dppEtlSqlTemp.setCreateBy(getNickName());
        dppEtlSqlTemp.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSqlTempService.createDppEtlSqlTemp(dppEtlSqlTemp));
    }

    @Operation(summary = "修改数据集成SQL模版")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:edit')")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlSqlTempSaveReqVO dppEtlSqlTemp) {
        dppEtlSqlTemp.setUpdatorId(getUserId());
        dppEtlSqlTemp.setUpdateBy(getNickName());
        dppEtlSqlTemp.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSqlTempService.updateDppEtlSqlTemp(dppEtlSqlTemp));
    }

    @Operation(summary = "删除数据集成SQL模版")
    @PreAuthorize("@ss.hasPermi('dpp:etl:temp:remove')")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlSqlTempService.removeDppEtlSqlTemp(Arrays.asList(ids)));
    }

}
