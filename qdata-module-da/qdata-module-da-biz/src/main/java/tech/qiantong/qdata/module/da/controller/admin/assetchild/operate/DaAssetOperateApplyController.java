package tech.qiantong.qdata.module.da.controller.admin.assetchild.operate;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.operate.DaAssetOperateApplyConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;
import tech.qiantong.qdata.module.da.service.assetchild.operate.IDaAssetOperateApplyService;

/**
 * 数据资产操作申请Controller
 *
 * @author qdata
 * @date 2025-05-09
 */
@Tag(name = "数据资产操作申请")
@RestController
@RequestMapping("/da/assetOperateApply")
@Validated
public class DaAssetOperateApplyController extends BaseController {
    @Resource
    private IDaAssetOperateApplyService daAssetOperateApplyService;

    @Operation(summary = "查询数据资产操作申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetOperateApplyRespVO>> list(DaAssetOperateApplyPageReqVO daAssetOperateApply) {
        PageResult<DaAssetOperateApplyDO> page = daAssetOperateApplyService.getDaAssetOperateApplyPage(daAssetOperateApply);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetOperateApplyRespVO.class));
    }

    @Operation(summary = "导出数据资产操作申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:export')")
    @Log(title = "数据资产操作申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetOperateApplyPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetOperateApplyDO> list = (List<DaAssetOperateApplyDO>) daAssetOperateApplyService.getDaAssetOperateApplyPage(exportReqVO).getRows();
        ExcelUtil<DaAssetOperateApplyRespVO> util = new ExcelUtil<>(DaAssetOperateApplyRespVO.class);
        util.exportExcel(response, DaAssetOperateApplyConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产操作申请列表")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:import')")
    @Log(title = "数据资产操作申请", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetOperateApplyRespVO> util = new ExcelUtil<>(DaAssetOperateApplyRespVO.class);
        List<DaAssetOperateApplyRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetOperateApplyService.importDaAssetOperateApply(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产操作申请详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetOperateApplyRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetOperateApplyDO daAssetOperateApplyDO = daAssetOperateApplyService.getDaAssetOperateApplyById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetOperateApplyDO, DaAssetOperateApplyRespVO.class));
    }

    @Operation(summary = "新增数据资产操作申请")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:add')")
    @Log(title = "数据资产操作申请", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetOperateApplySaveReqVO daAssetOperateApply) {
        daAssetOperateApply.setCreatorId(getUserId());
        daAssetOperateApply.setCreateBy(getNickName());
        daAssetOperateApply.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetOperateApplyService.createDaAssetOperateApply(daAssetOperateApply));
    }

    @Operation(summary = "修改数据资产操作申请")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:edit')")
    @Log(title = "数据资产操作申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetOperateApplySaveReqVO daAssetOperateApply) {
        daAssetOperateApply.setUpdatorId(getUserId());
        daAssetOperateApply.setUpdateBy(getNickName());
        daAssetOperateApply.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetOperateApplyService.updateDaAssetOperateApply(daAssetOperateApply));
    }

    @Operation(summary = "删除数据资产操作申请")
    @PreAuthorize("@ss.hasPermi('da:assetOperateApply:remove')")
    @Log(title = "数据资产操作申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetOperateApplyService.removeDaAssetOperateApply(Arrays.asList(ids)));
    }

}
