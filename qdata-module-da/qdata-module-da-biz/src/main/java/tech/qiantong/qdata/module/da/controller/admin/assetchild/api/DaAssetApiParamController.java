package tech.qiantong.qdata.module.da.controller.admin.assetchild.api;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.api.DaAssetApiParamConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;

/**
 * 数据资产-外部API-参数Controller
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-外部API-参数")
@RestController
@RequestMapping("/da/assetApiParam")
@Validated
public class DaAssetApiParamController extends BaseController {
    @Resource
    private IDaAssetApiParamService daAssetApiParamService;

    @Operation(summary = "查询数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetApiParamRespVO>> list(DaAssetApiParamPageReqVO daAssetApiParam) {
        PageResult<DaAssetApiParamDO> page = daAssetApiParamService.getDaAssetApiParamPage(daAssetApiParam);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "查询数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:list')")
    @GetMapping("/getDaAssetApiParamList")
    public CommonResult<List<DaAssetApiParamRespVO>> getDaAssetApiParamList(DaAssetApiParamPageReqVO daAssetApiParam) {
        return CommonResult.success(BeanUtils.toBean(daAssetApiParamService.getDaAssetApiParamList(daAssetApiParam.getApiId()), DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "导出数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:export')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetApiParamPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetApiParamDO> list = (List<DaAssetApiParamDO>) daAssetApiParamService.getDaAssetApiParamPage(exportReqVO).getRows();
        ExcelUtil<DaAssetApiParamRespVO> util = new ExcelUtil<>(DaAssetApiParamRespVO.class);
        util.exportExcel(response, DaAssetApiParamConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-外部API-参数列表")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:import')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetApiParamRespVO> util = new ExcelUtil<>(DaAssetApiParamRespVO.class);
        List<DaAssetApiParamRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetApiParamService.importDaAssetApiParam(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-外部API-参数详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetApiParamRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetApiParamDO daAssetApiParamDO = daAssetApiParamService.getDaAssetApiParamById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetApiParamDO, DaAssetApiParamRespVO.class));
    }

    @Operation(summary = "新增数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:add')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetApiParamSaveReqVO daAssetApiParam) {
        daAssetApiParam.setCreatorId(getUserId());
        daAssetApiParam.setCreateBy(getNickName());
        daAssetApiParam.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiParamService.createDaAssetApiParam(daAssetApiParam));
    }

    @Operation(summary = "修改数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:edit')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetApiParamSaveReqVO daAssetApiParam) {
        daAssetApiParam.setUpdatorId(getUserId());
        daAssetApiParam.setUpdateBy(getNickName());
        daAssetApiParam.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetApiParamService.updateDaAssetApiParam(daAssetApiParam));
    }

    @Operation(summary = "删除数据资产-外部API-参数")
    @PreAuthorize("@ss.hasPermi('da:assetApiParam:remove')")
    @Log(title = "数据资产-外部API-参数", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetApiParamService.removeDaAssetApiParam(Arrays.asList(ids)));
    }

}
