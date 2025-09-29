package tech.qiantong.qdata.module.da.controller.admin.assetchild.gis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.gis.DaAssetGisConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;
import tech.qiantong.qdata.module.da.service.assetchild.gis.IDaAssetGisService;

/**
 * 数据资产-地理空间服务Controller
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-地理空间服务")
@RestController
@RequestMapping("/da/assetGis")
@Validated
public class DaAssetGisController extends BaseController {
    @Resource
    private IDaAssetGisService daAssetGisService;

    @Operation(summary = "查询数据资产-地理空间服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetGis:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetGisRespVO>> list(DaAssetGisPageReqVO daAssetGis) {
        PageResult<DaAssetGisDO> page = daAssetGisService.getDaAssetGisPage(daAssetGis);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetGisRespVO.class));
    }

    @Operation(summary = "导出数据资产-地理空间服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetGis:export')")
    @Log(title = "数据资产-地理空间服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetGisPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetGisDO> list = (List<DaAssetGisDO>) daAssetGisService.getDaAssetGisPage(exportReqVO).getRows();
        ExcelUtil<DaAssetGisRespVO> util = new ExcelUtil<>(DaAssetGisRespVO.class);
        util.exportExcel(response, DaAssetGisConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-地理空间服务列表")
    @PreAuthorize("@ss.hasPermi('da:assetGis:import')")
    @Log(title = "数据资产-地理空间服务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetGisRespVO> util = new ExcelUtil<>(DaAssetGisRespVO.class);
        List<DaAssetGisRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetGisService.importDaAssetGis(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-地理空间服务详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetGis:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetGisRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetGisDO daAssetGisDO = daAssetGisService.getDaAssetGisById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetGisDO, DaAssetGisRespVO.class));
    }

    @Operation(summary = "新增数据资产-地理空间服务")
    @PreAuthorize("@ss.hasPermi('da:assetGis:add')")
    @Log(title = "数据资产-地理空间服务", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetGisSaveReqVO daAssetGis) {
        daAssetGis.setCreatorId(getUserId());
        daAssetGis.setCreateBy(getNickName());
        daAssetGis.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetGisService.createDaAssetGis(daAssetGis));
    }

    @Operation(summary = "修改数据资产-地理空间服务")
    @PreAuthorize("@ss.hasPermi('da:assetGis:edit')")
    @Log(title = "数据资产-地理空间服务", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetGisSaveReqVO daAssetGis) {
        daAssetGis.setUpdatorId(getUserId());
        daAssetGis.setUpdateBy(getNickName());
        daAssetGis.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetGisService.updateDaAssetGis(daAssetGis));
    }

    @Operation(summary = "删除数据资产-地理空间服务")
    @PreAuthorize("@ss.hasPermi('da:assetGis:remove')")
    @Log(title = "数据资产-地理空间服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetGisService.removeDaAssetGis(Arrays.asList(ids)));
    }



    @Operation(summary = "删除数据资产-视频")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @PostMapping("/queryServiceForwarding")
    public void queryServiceForwarding(HttpServletResponse response,@Valid @RequestBody DaAssetGisReqVO daAssetGisReqVO) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        daAssetGisService.queryServiceForwarding(response,daAssetGisReqVO);
    }


}
