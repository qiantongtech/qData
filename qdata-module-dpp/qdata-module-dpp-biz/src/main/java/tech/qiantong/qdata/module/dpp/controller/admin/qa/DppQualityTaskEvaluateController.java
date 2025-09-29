package tech.qiantong.qdata.module.dpp.controller.admin.qa;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
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
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.qa.DppQualityTaskEvaluateConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskEvaluateService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskService;

/**
 * 数据质量任务-评测规则Controller
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Tag(name = "数据质量任务-评测规则")
@RestController
@RequestMapping("/dpp/qualityTaskEvaluate")
@Validated
public class DppQualityTaskEvaluateController extends BaseController {
    @Resource
    private IDppQualityTaskEvaluateService dppQualityTaskEvaluateService;
    @Resource
    private IDppQualityTaskService dppQualityTaskService;

    @Operation(summary = "查询数据质量任务-评测规则列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityTaskEvaluateRespVO>> list(DppQualityTaskEvaluatePageReqVO dppQualityTaskEvaluate) {
        PageResult<DppQualityTaskEvaluateDO> page = dppQualityTaskEvaluateService.getDppQualityTaskEvaluatePage(dppQualityTaskEvaluate);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityTaskEvaluateRespVO.class));
    }

    @Operation(summary = "导出数据质量任务-评测规则列表")
    @Log(title = "数据质量任务-评测规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityTaskEvaluatePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityTaskEvaluateDO> list = (List<DppQualityTaskEvaluateDO>) dppQualityTaskEvaluateService.getDppQualityTaskEvaluatePage(exportReqVO).getRows();
        ExcelUtil<DppQualityTaskEvaluateRespVO> util = new ExcelUtil<>(DppQualityTaskEvaluateRespVO.class);
        util.exportExcel(response, DppQualityTaskEvaluateConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量任务-评测规则列表")
    @Log(title = "数据质量任务-评测规则", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityTaskEvaluateRespVO> util = new ExcelUtil<>(DppQualityTaskEvaluateRespVO.class);
        List<DppQualityTaskEvaluateRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityTaskEvaluateService.importDppQualityTaskEvaluate(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量任务-评测规则详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityTaskEvaluateRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO = dppQualityTaskEvaluateService.getDppQualityTaskEvaluateById(id);
        return CommonResult.success(BeanUtils.toBean(dppQualityTaskEvaluateDO, DppQualityTaskEvaluateRespVO.class));
    }

    @Operation(summary = "新增数据质量任务-评测规则")
    @Log(title = "数据质量任务-评测规则", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        dppQualityTaskEvaluate.setCreatorId(getUserId());
        dppQualityTaskEvaluate.setCreateBy(getNickName());
        dppQualityTaskEvaluate.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskEvaluateService.createDppQualityTaskEvaluate(dppQualityTaskEvaluate));
    }

    @Operation(summary = "修改数据质量任务-评测规则")
    @Log(title = "数据质量任务-评测规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        dppQualityTaskEvaluate.setUpdatorId(getUserId());
        dppQualityTaskEvaluate.setUpdateBy(getNickName());
        dppQualityTaskEvaluate.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityTaskEvaluateService.updateDppQualityTaskEvaluate(dppQualityTaskEvaluate));
    }

    @Operation(summary = "删除数据质量任务-评测规则")
    @Log(title = "数据质量任务-评测规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityTaskEvaluateService.removeDppQualityTaskEvaluate(Arrays.asList(ids)));
    }

    @Operation(summary = "删除数据质量任务-检验功能")
    @Log(title = "数据质量任务-检验功能", businessType = BusinessType.DELETE)
    @GetMapping("/verifyInterfaceValue")
    public CommonResult<String> verifyInterfaceValue(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {

        String meg = dppQualityTaskService.verifyInterfaceValue(dppQualityTaskEvaluate);
        return CommonResult.success(meg);
    }

    @Operation(summary = "删除数据质量任务-错误抽查功能")
    @PostMapping("/validationErrorDataSql")
    public AjaxResult validationErrorDataSql(@Valid @RequestBody DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        return AjaxResult.success(dppQualityTaskService.validationErrorDataSql(dppQualityTaskEvaluate));
    }

    @Operation(summary = "删除数据质量任务-成功抽查功能")
    @PostMapping("/validationValidDataSql")
    public AjaxResult validationValidDataSql(@Valid @RequestBody DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        return AjaxResult.success(dppQualityTaskService.validationValidDataSql(dppQualityTaskEvaluate));
    }
}
