package tech.qiantong.qdata.module.ds.controller.admin.api;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
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
import tech.qiantong.qdata.common.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.SqlParseVo;
import tech.qiantong.qdata.module.ds.convert.api.DsApiConvert;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.SqlParseDto;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * API服务Controller
 *
 * @author lhs
 * @date 2025-02-12
 */
@Tag(name = "API服务")
@RestController
@RequestMapping("/ds/api")
@Validated
public class DsApiController extends BaseController {
    @Resource
    private IDsApiService dsApiService;

    @Operation(summary = "查询API服务列表")
    @PreAuthorize("@ss.hasPermi('ds:api:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DsApiRespVO>> list(DsApiPageReqVO dsApi) {
        PageResult<DsApiDO> page = dsApiService.getDsApiPage(dsApi);
        return CommonResult.success(BeanUtils.toBean(page, DsApiRespVO.class));
    }

    @Operation(summary = "导出API服务列表")
    @PreAuthorize("@ss.hasPermi('ds:api:export')")
    @Log(title = "API服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DsApiPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DsApiDO> list = (List<DsApiDO>) dsApiService.getDsApiPage(exportReqVO).getRows();
        ExcelUtil<DsApiRespVO> util = new ExcelUtil<>(DsApiRespVO.class);
        util.exportExcel(response, DsApiConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入API服务列表")
    @PreAuthorize("@ss.hasPermi('ds:api:import')")
    @Log(title = "API服务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DsApiRespVO> util = new ExcelUtil<>(DsApiRespVO.class);
        List<DsApiRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dsApiService.importDsApi(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取API服务详细信息")
    @PreAuthorize("@ss.hasPermi('ds:api:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<DsApiRespVO> getInfo(@PathVariable("ID") Long ID) {
        DsApiDO dsApiDO = dsApiService.getDsApiById(ID);
        return CommonResult.success(BeanUtils.toBean(dsApiDO, DsApiRespVO.class));
    }

    @Operation(summary = "根据名称，版本号，路径进行判断是否重复")
    @PreAuthorize("@ss.hasPermi('ds:api:query')")
    @PostMapping(value = "/repeatFlag")
    public AjaxResult repeatFlag(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isBlank(jsonObject.getString("name"))) {
            return AjaxResult.error("请携带API名称");
        }
        if (StringUtils.isBlank(jsonObject.getString("apiVersion"))) {
            return AjaxResult.error("请携带API版本号");
        }
        if (StringUtils.isBlank(jsonObject.getString("apiUrl"))) {
            return AjaxResult.error("请携带API路径");
        }
        DsApiDO dsApiDO = dsApiService.repeatFlag(jsonObject);
        if (dsApiDO != null) {
            return AjaxResult.error("名称、版本号、路径以存在");
        }
        return AjaxResult.success(dsApiDO);
    }


    @Operation(summary = "删除API服务")
    @PreAuthorize("@ss.hasPermi('ds:api:remove')")
    @Log(title = "API服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public CommonResult<Integer> remove(@PathVariable(name = "id") Long[] id) {
        return CommonResult.toAjax(dsApiService.removeDsApi(Arrays.asList(id)));
    }

    /**
     * SQL解析
     *
     * @param sqlParseDto
     * @return
     */
    @Operation(summary = "SQL解析")
    @PreAuthorize("@ss.hasPermi('da:asset:list')")
    @PostMapping("/sqlParse")
    public AjaxResult sqlParse(@RequestBody @Validated SqlParseDto sqlParseDto) {
        /*try {
            sqlParseDto.setSqlText(AesEncryptUtil.desEncrypt(sqlParseDto.getSqlText()).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SqlParseVo sqlParseVo = dsApiService.sqlParse(sqlParseDto);
        return AjaxResult.success(sqlParseVo);
    }

    @Operation(summary = "接口调试")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @PostMapping("/serviceTesting")
    public AjaxResult serviceTesting(@RequestBody DsApiDO dataApi) {
        if (dataApi.getExecuteConfig() != null && StringUtils.isNotBlank(dataApi.getExecuteConfig().getSqlText())) {
            try {
                dataApi.getExecuteConfig().setSqlText(AesEncryptUtil.desEncrypt(dataApi.getExecuteConfig().getSqlText()).trim());
            } catch (Exception e) {
                logger.error("失败", e);
            }
        } else {
            if (dataApi.getExecuteConfig() != null) {
                dataApi.getExecuteConfig().setSqlText("");
            }
        }
        Object value = dsApiService.serviceTesting(dataApi);
        return AjaxResult.success(value);
    }


    @Operation(summary = "接口调试")
    @PreAuthorize("@ss.hasPermi('ds:api:query')")
    @PostMapping("/queryServiceForwarding")
    public void queryServiceForwarding(HttpServletResponse response, @Valid @RequestBody DsApiReqVO dsApiReqVO) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        dsApiService.queryServiceForwarding(response, dsApiReqVO);
    }


    /**
     * 添加
     *
     * @param dataApi
     * @return
     */
    @Operation(summary = "保存Api信息")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @PostMapping()
    public AjaxResult saveDataApi(@RequestBody DsApiDO dataApi) {
//        if (dataApi.getExecuteConfig() != null && StringUtils.isNotBlank(dataApi.getExecuteConfig().getSqlText())) {
//            try {
//                dataApi.getExecuteConfig().setSqlText(AesEncryptUtil.desEncrypt(dataApi.getExecuteConfig().getSqlText()).trim());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        dataApi.setCreateBy(getUsername());
        dataApi.setCreatorId(getUserId());
        dataApi.setCreateTime(DateUtil.date());
        return dsApiService.saveDataApi(dataApi);
    }


    /**
     * 修改
     *
     * @param dataApi
     * @return
     */
    @Operation(summary = "修改Api信息")
    @PutMapping
    public AjaxResult updateDataApi(@RequestBody DsApiDO dataApi) {
//        if (dataApi.getExecuteConfig() != null && StringUtils.isNotBlank(dataApi.getExecuteConfig().getSqlText())) {
//            try {
//                dataApi.getExecuteConfig().setSqlText(AesEncryptUtil.desEncrypt(dataApi.getExecuteConfig().getSqlText()).trim());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        dataApi.setUpdatorId(getUserId());
        dataApi.setUpdateBy(getUsername());
        dataApi.setUpdateTime(DateUtil.date());
        return dsApiService.updateDataApi(dataApi);
    }


    /**
     * 发布接口
     *
     * @param id
     * @return
     */

    @GetMapping(value = "/release/{id}")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    public AjaxResult releaseDataApi(@PathVariable String id) {
        dsApiService.releaseDataApi(id, getUserId(), getUsername());
        return AjaxResult.success();
    }

    /**
     * 注销接口
     *
     * @param id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @GetMapping(value = "/cancel/{id}")
    public AjaxResult cancelDataApi(@PathVariable String id) {
        dsApiService.cancelDataApi(id, getUserId(), getUsername());
        return AjaxResult.success();
    }

    @Operation(summary = "查询API下拉选服务列表")
    @GetMapping("/selectList")
    public CommonResult<List<DsApiRespVO>> selectList(String name) {
        IPage<DsApiDO> page = dsApiService.page(new Page(1, 20), Wrappers.lambdaQuery(DsApiDO.class)
                .like(StringUtils.isNotBlank(name),DsApiDO::getName, name)
                .select(DsApiDO::getId, DsApiDO::getName));
        return CommonResult.success(BeanUtils.toBean(page.getRecords(), DsApiRespVO.class));
    }


}
