package tech.qiantong.qdata.module.da.controller.admin.assetchild.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import org.springframework.http.MediaType;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.api.DaAssetApiConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiService;

/**
 * 数据资产-外部APIController
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-外部API")
@RestController
@RequestMapping("/da/api")
@Validated
public class DaAssetApiController extends BaseController {
    @Resource
    private IDaAssetApiService daAssetApiService;

    @Operation(summary = "查询数据资产-外部API列表")
    @PreAuthorize("@ss.hasPermi('da:api:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetApiRespVO>> list(DaAssetApiPageReqVO daAssetApi) {
        PageResult<DaAssetApiDO> page = daAssetApiService.getDaAssetApiPage(daAssetApi);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetApiRespVO.class));
    }

    @Operation(summary = "导出数据资产-外部API列表")
    @PreAuthorize("@ss.hasPermi('da:api:export')")
    @Log(title = "数据资产-外部API", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetApiPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetApiDO> list = (List<DaAssetApiDO>) daAssetApiService.getDaAssetApiPage(exportReqVO).getRows();
        ExcelUtil<DaAssetApiRespVO> util = new ExcelUtil<>(DaAssetApiRespVO.class);
        util.exportExcel(response, DaAssetApiConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-外部API列表")
    @PreAuthorize("@ss.hasPermi('da:api:import')")
    @Log(title = "数据资产-外部API", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetApiRespVO> util = new ExcelUtil<>(DaAssetApiRespVO.class);
        List<DaAssetApiRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetApiService.importDaAssetApi(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-外部API详细信息")
    @PreAuthorize("@ss.hasPermi('da:api:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetApiRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetApiDO daAssetApiDO = daAssetApiService.getDaAssetApiById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetApiDO, DaAssetApiRespVO.class));
    }

    @Operation(summary = "新增数据资产-外部API")
    @PreAuthorize("@ss.hasPermi('da:api:add')")
    @Log(title = "数据资产-外部API", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetApiSaveReqVO daAssetApi) {
        daAssetApi.setCreatorId(getUserId());
        daAssetApi.setCreateBy(getNickName());
        daAssetApi.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiService.createDaAssetApi(daAssetApi));
    }

    @Operation(summary = "修改数据资产-外部API")
    @PreAuthorize("@ss.hasPermi('da:api:edit')")
    @Log(title = "数据资产-外部API", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetApiSaveReqVO daAssetApi) {
        daAssetApi.setUpdatorId(getUserId());
        daAssetApi.setUpdateBy(getNickName());
        daAssetApi.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiService.updateDaAssetApi(daAssetApi));
    }

    @Operation(summary = "删除数据资产-外部API")
    @PreAuthorize("@ss.hasPermi('da:api:remove')")
    @Log(title = "数据资产-外部API", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetApiService.removeDaAssetApi(Arrays.asList(ids)));
    }


    @Operation(summary = "删除数据资产-外部API")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @PostMapping("/queryServiceForwarding")
    public void queryServiceForwarding(HttpServletResponse response,@Valid @RequestBody DaAssetApiReqVO daAssetApi) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        daAssetApiService.queryServiceForwarding(response,daAssetApi);
    }

}
