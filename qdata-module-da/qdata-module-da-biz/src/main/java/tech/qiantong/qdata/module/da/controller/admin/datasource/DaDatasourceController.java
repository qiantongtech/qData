package tech.qiantong.qdata.module.da.controller.admin.datasource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceTableVO;
import tech.qiantong.qdata.module.da.convert.datasource.DaDatasourceConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;
import tech.qiantong.qdata.module.da.service.datasource.impl.DaDatasourceServiceImpl;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnReqDTO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 数据源Controller
 *
 * @author lhs
 * @date 2025-01-21
 */
@Tag(name = "数据源")
@RestController
@RequestMapping("/da/dataSource")
@Validated
@RequiredArgsConstructor
public class DaDatasourceController extends BaseController {
    private final IDaDatasourceService daDatasourceService;
    private final IDaAssetService daAssetService;

    @Operation(summary = "查询数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDatasourceRespVO>> list(DaDatasourcePageReqVO daDatasource) {
        PageResult<DaDatasourceDO> page = daDatasourceService.getDaDatasourcePage(daDatasource);
        return CommonResult.success(BeanUtils.toBean(page, DaDatasourceRespVO.class));
    }

    @Operation(summary = "数据集成中排除Kafka并且是当前项目的数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:list')")
    @GetMapping("/dppNoKafka/list")
    public AjaxResult dppNoKafkaList(DaDatasourcePageReqVO daDatasource) {
        List<DaDatasourceDO> page = daDatasourceService.getDaDatasourceDppNoKafka(daDatasource);
        return AjaxResult.success(BeanUtils.toBean(page, DaDatasourceRespVO.class));
    }

    @Operation(summary = "数据研发中的查询数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:list')")
    @GetMapping("/dpp/list")
    public CommonResult<PageResult<DaDatasourceRespVO>> dppList(DaDatasourcePageReqVO daDatasource) {
        PageResult<DaDatasourceDO> page = daDatasourceService.getDaDatasourceDppPage(daDatasource);
        return CommonResult.success(BeanUtils.toBean(page, DaDatasourceRespVO.class));
    }

    @Operation(summary = "查询项目列表，让研发模块添加的数据不可选中")
    @PreAuthorize("@ss.hasPermi('da:dataSource:list')")
    @GetMapping("/noDppAdd/list")
    public CommonResult<PageResult<AttProjectRespDTO>> noDppAddList(AttProjectReqDTO pageReqVO) {
        PageResult<AttProjectRespDTO> page = daDatasourceService.getNoDppAddList(pageReqVO);
        return CommonResult.success(page);
    }

    @Operation(summary = "查询数据资产的数据源连接信息")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @GetMapping("/getDataSourceByAsset")
    public AjaxResult getDataSourceByAsset(DaDatasourceRespVO daAsset) {
        List<DaDatasourceDO> daAssetDOS = daDatasourceService.getDataSourceByAsset(daAsset);
        return AjaxResult.success(daAssetDOS);
    }

    @Operation(summary = "查询数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:list')")
    @GetMapping("/getDaDatasourceList")
    public CommonResult<List<DaDatasourceRespVO>> getDaDatasourceList(DaDatasourcePageReqVO daDatasource) {
        List<DaDatasourceDO> page = daDatasourceService.getDaDatasourceList(daDatasource);
        return CommonResult.success(BeanUtils.toBean(page, DaDatasourceRespVO.class));
    }

    @Operation(summary = "导出数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:export')")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDatasourcePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDatasourceDO> list = (List<DaDatasourceDO>) daDatasourceService.getDaDatasourcePage(exportReqVO).getRows();
        ExcelUtil<DaDatasourceRespVO> util = new ExcelUtil<>(DaDatasourceRespVO.class);
        util.exportExcel(response, DaDatasourceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据源列表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:import')")
    @Log(title = "数据源", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDatasourceRespVO> util = new ExcelUtil<>(DaDatasourceRespVO.class);
        List<DaDatasourceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDatasourceService.importDaDatasource(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据源详细信息")
    @PreAuthorize("@ss.hasPermi('da:dataSource:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDatasourceRespVO> getInfo(@PathVariable("id") Long id) {
        DaDatasourceDO daDatasourceDO = daDatasourceService.getDaDatasourceById(id);
        return CommonResult.success(BeanUtils.toBean(daDatasourceDO, DaDatasourceRespVO.class));
    }

    @Operation(summary = "新增数据源")
    @PreAuthorize("@ss.hasPermi('da:dataSource:add')")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDatasourceSaveReqVO daDatasource) {
        daDatasource.setCreatorId(getUserId());
        daDatasource.setCreateBy(getNickName());
        daDatasource.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceService.createDaDatasource(daDatasource));
    }

    @Operation(summary = "修改数据源")
    @PreAuthorize("@ss.hasPermi('da:dataSource:edit')")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDatasourceSaveReqVO daDatasource) {
        daDatasource.setUpdatorId(getUserId());
        daDatasource.setUpdateBy(getNickName());
        daDatasource.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDatasourceService.updateDaDatasource(daDatasource));
    }

    @Operation(summary = "删除数据源")
    @PreAuthorize("@ss.hasPermi('da:dataSource:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDatasourceService.removeDaDatasource(Arrays.asList(ids)));
    }

    @Operation(summary = "修改数据源状态")
    @PreAuthorize("@ss.hasPermi('da:dataSource:edit')")
    @GetMapping(value = "/editDatasourceStatus/{id}/{status}")
    public AjaxResult editDatasourceStatus(@PathVariable Long id, @PathVariable Long status) {
        Boolean isOk = daDatasourceService.editDatasourceStatus(id, status);
        if (!isOk) {
            return AjaxResult.error("任务状态修改失败，请联系系统管理员");
        }
        return AjaxResult.success("修改成功");
    }


    @Operation(summary = "删除数据源带类型判断是数据资产还是数据研发")
    @PreAuthorize("@ss.hasPermi('da:dataSource:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}/{type}")
    public CommonResult<Integer> removeDppOrDa(@PathVariable("ids") Long[] ids, @PathVariable("type") Long type) {
        return CommonResult.toAjax(daDatasourceService.removeDaDatasourceDppOrDa(Arrays.asList(ids), type));
    }

    @Operation(summary = "测试连接")
    @PreAuthorize("@ss.hasPermi('da:dataSource:remove')")
    @Log(title = "测试连接", businessType = BusinessType.DELETE)
    @GetMapping("clientsTest/{id}")
    public AjaxResult clientsTest(@PathVariable("id") Long ids) {
        return daDatasourceService.clientsTest(ids);
    }


    @Operation(summary = "获取数据源里面的数据表")
    @PreAuthorize("@ss.hasPermi('da:dataSource:query')")
    @GetMapping(value = "/tableList/{id}")
    public AjaxResult getTableList(@PathVariable("id") Long id) {
        List<DbTable> tables = daDatasourceService.getDbTables(id);
        return success(tables);
    }


    @Operation(summary = "获取数据源里面的数据表的数据字段")
    @PreAuthorize("@ss.hasPermi('da:dataSource:query')")
    @PostMapping(value = "/columnsList")
    public AjaxResult getColumnsList(@RequestBody JSONObject jsonObject) {
        List<DpModelColumnReqDTO> columns = daDatasourceService.getColumnsList(jsonObject);
        return success(columns);
    }

    @Operation(summary = "SQL解析")
    @PreAuthorize("@ss.hasPermi('da:dataSource:query')")
    @PostMapping("/sqlParse")
    public AjaxResult sqlParse(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isEmpty(jsonObject.getStr("sourceId"))) {
            return AjaxResult.error("请携带数据源!");
        }
        if (StringUtils.isEmpty(jsonObject.getStr("sql"))) {
            return AjaxResult.error("请输入SQL语句!");
        }
       /* try {
            jsonObject.set("sql",AesEncryptUtil.desEncrypt(jsonObject.getStr("sql")).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        List<DbColumn> dbColumnList = daDatasourceService.sqlParse(jsonObject.getStr("sourceId"), jsonObject.getStr("sql"));
        List<DaAssetColumnDO> daAssetColumnDOList = DaDatasourceServiceImpl.convertDbColumns(dbColumnList);
        return AjaxResult.success(daAssetColumnDOList);
    }




    @Operation(summary = "获取数据源里面的数据表的数据字段")
    @PreAuthorize("@ss.hasPermi('da:dataSource:query')")
    @PostMapping(value = "/columnsAsAssetColumnList")
    public CommonResult<List<DaAssetColumnDO>> columnsAsAssetColumnList(@RequestBody @Valid DaDatasourceTableVO param) {
        List<DaAssetColumnDO> columns = daDatasourceService.columnsAsAssetColumnList(param.getId(), param.getTableName());
        if (param.getWithRule() == null) {
            return CommonResult.success(columns);
        }
        if (param.getWithRule() != 2 && param.getWithRule() != 1) {
            return CommonResult.success(columns);
        }
        List<DaAssetColumnRelRuleVO> assetColumnRelRuleVOS = daAssetService.listRelRule(param.getId(), param.getTableName(), param.getWithRule().toString());
        if (assetColumnRelRuleVOS.isEmpty()) {
            return CommonResult.success(columns);
        }
        columns.forEach(i -> i.setCleanRuleList(new ArrayList<>()));
        for (DaAssetColumnRelRuleVO vo : assetColumnRelRuleVOS) {
            for (DaAssetColumnDO column : columns) {
                if (column.getColumnName().equalsIgnoreCase(vo.getAssetColumn().getColumnName())) {
                    column.getCleanRuleList().add(vo.getElemRuleRel());
                    break;
                }
            }
        }
        return CommonResult.success(columns);
    }
    @Operation(summary = "")
    @PreAuthorize("@ss.hasPermi('da:dataQuery:list')")
    @GetMapping(value = "/executeSqlQuery")
    public AjaxResult executeSqlQuery(DaDatasourcePageReqVO daDatasource) {
        return success(daDatasourceService.executeSqlQuery(daDatasource));
    }


    @Operation(summary = "")
    @PreAuthorize("@ss.hasPermi('da:dataQuery:list')")
    @GetMapping(value = "/exportSqlQueryResult/export")
    public void exportSqlQueryResult(HttpServletResponse response, DaDatasourcePageReqVO daDatasource) {
        daDatasourceService.exportSqlQueryResult(response, daDatasource);
    }

}
