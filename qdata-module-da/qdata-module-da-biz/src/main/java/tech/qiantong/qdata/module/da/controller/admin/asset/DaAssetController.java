package tech.qiantong.qdata.module.da.controller.admin.asset;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
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
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.convert.asset.DaAssetConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 数据资产Controller
 *
 * @author lhs
 * @date 2025-01-21
 */
@Tag(name = "数据资产")
@RestController
@RequestMapping("/da/asset")
@Validated
public class DaAssetController extends BaseController {
    @Resource
    private IDaAssetService daAssetService;


    @Operation(summary = "查询数据资产列表")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetRespVO>> list(DaAssetPageReqVO daAsset) {
        PageResult<DaAssetDO> page = daAssetService.getDaAssetPage(daAsset, "1");
        return CommonResult.success(BeanUtils.toBean(page, DaAssetRespVO.class));
    }

    @Operation(summary = "查询数据研发下的数据资产列表")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/dpp/list")
    public CommonResult<PageResult<DaAssetRespVO>> dppList(DaAssetPageReqVO daAsset) {
        PageResult<DaAssetDO> page = daAssetService.getDppAssetPage(daAsset);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetRespVO.class));
    }

    @Operation(summary = "查询数据研发下的数据资产全部不分页列表")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/dpp/noPage/list")
    public AjaxResult dppNoPageList(DaAssetPageReqVO daAsset) {
        List<DaAssetDO> daAssetDOList = daAssetService.getDppAssetNoPageList(daAsset);
        return AjaxResult.success(daAssetDOList);
    }

    @Operation(summary = "查询资产表")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/getTablesByDataSourceId")
    public AjaxResult getTablesByDataSourceId(DaAssetPageReqVO daAsset) {
        List<DaAssetDO> tablesByDataSourceId = daAssetService.getTablesByDataSourceId(daAsset);
        return AjaxResult.success(tablesByDataSourceId);
    }


    @Operation(summary = "查询资产表")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/getDaAssetRespList")
    public CommonResult<List<DaAssetRespVO>> getDaAssetRespList(DaAssetPageReqVO daAsset) {
        List<DaAssetDO> tablesByDataSourceId = daAssetService.getDaAssetList(daAsset);
        return CommonResult.success(BeanUtils.toBean(tablesByDataSourceId, DaAssetRespVO.class));
    }


    @Operation(summary = "导出数据资产列表")
    @PreAuthorize("@ss.hasPermi('da:asset:export')")
    @Log(title = "数据资产", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetDO> list = (List<DaAssetDO>) daAssetService.getDaAssetPage(exportReqVO, "1").getRows();
        ExcelUtil<DaAssetRespVO> util = new ExcelUtil<>(DaAssetRespVO.class);
        util.exportExcel(response, DaAssetConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产列表")
    @PreAuthorize("@ss.hasPermi('da:asset:import')")
    @Log(title = "数据资产", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetRespVO> util = new ExcelUtil<>(DaAssetRespVO.class);
        List<DaAssetRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetService.importDaAsset(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产详细信息")
    @PreAuthorize("@ss.hasPermi('da:asset:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetRespVO> getInfo(@PathVariable("id") Long id) {
        return CommonResult.success(daAssetService.getDaAssetById(id));
    }

    @Operation(summary = "获取数据资产详细信息预览数据")
    @PreAuthorize("@ss.hasPermi('da:asset:query')")
    @PostMapping(value = "/preview")
    public AjaxResult getPreview(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isEmpty(jsonObject.getStr("id"))) {
            return error("请携带资产id");
        }
        Map<String, Object> columnData = daAssetService.getColumnData(jsonObject);
        if (columnData == null) {
            return error("数据库中未获取到该表数据，请确认表是否存在!");
        }
        List<Map<String, Object>> dataMaskingList = daAssetService.dataMasking(Long.valueOf(jsonObject.getStr("id")), (List<Map<String, Object>>) columnData.get("tableData"));
        if (dataMaskingList == null) {
            return error("请检查资产字段与数据表字段是否一致");
        }
        columnData.put("tableData", dataMaskingList);
        return success(columnData);
    }

    @Operation(summary = "新增数据资产")
    @PreAuthorize("@ss.hasPermi('da:asset:add')")
    @Log(title = "数据资产", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetSaveReqVO daAsset) {
        daAsset.setCreatorId(getUserId());
        daAsset.setCreateBy(getNickName());
        daAsset.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetService.createDaAssetNew(daAsset));
    }

    @Operation(summary = "修改数据资产")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @Log(title = "数据资产", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetSaveReqVO daAsset) {
        daAsset.setUpdatorId(getUserId());
        daAsset.setUpdateBy(getNickName());
        daAsset.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetService.updateDaAssetNew(daAsset));
    }

//    @Operation(summary = "删除数据资产")
//    @PreAuthorize("@ss.hasPermi('da:asset:remove')")
//    @Log(title = "数据资产", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
//        return CommonResult.toAjax(daAssetService.removeDaAsset(Arrays.asList(ids)));
//    }


    @Operation(summary = "删除数据资产")
    @PreAuthorize("@ss.hasPermi('da:asset:remove')")
    @Log(title = "数据资产", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ID}")
    public CommonResult<Integer> remove(@PathVariable Long ID) {
        return CommonResult.toAjax(daAssetService.removeDaAsset(ID));
    }

    @Log(title = "触发一次定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/startDaDiscoveryTask")
    public AjaxResult startDaDiscoveryTask(@Valid @RequestBody DaAssetSaveReqVO daAsset) {
        return daAssetService.startDaAssetDatasourceTask(daAsset.getId());
    }

    @GetMapping("/listRelRule")
    public CommonResult<List<DaAssetColumnRelRuleVO>> listRelRule(@RequestParam Long id, @RequestParam String type) {
        List<DaAssetColumnRelRuleVO> vos = daAssetService.listRelRule(id, type);
        return CommonResult.success(vos);
    }

    @GetMapping("/listRelRule/v2")
    public CommonResult<List<DaAssetColumnRelRuleVO>> listRelRule(@RequestParam Long datasourceId,
                                                                  @RequestParam String tableName,
                                                                  @RequestParam String type) {
        List<DaAssetColumnRelRuleVO> vos = daAssetService.listRelRule(datasourceId, tableName, type);
        return CommonResult.success(vos);
    }
}
