package tech.qiantong.qdata.module.dpp.controller.admin.etl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppQualityLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;

/**
 * 数据质量日志Controller
 *
 * @author qdata
 * @date 2025-07-19
 */
@Tag(name = "数据质量日志")
@RestController
@RequestMapping("/dpp/qualityLog")
@Validated
public class DppQualityLogController extends BaseController {
    @Resource
    private IDppQualityLogService dppQualityLogService;

    @Operation(summary = "查询数据质量日志列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityLogRespVO>> list(DppQualityLogPageReqVO dppQualityLog) {
        PageResult<DppQualityLogDO> page = dppQualityLogService.getDppQualityLogPage(dppQualityLog);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityLogRespVO.class));
    }

    @Operation(summary = "导出数据质量日志列表")
    @Log(title = "数据质量日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityLogDO> list = (List<DppQualityLogDO>) dppQualityLogService.getDppQualityLogPage(exportReqVO).getRows();
        ExcelUtil<DppQualityLogRespVO> util = new ExcelUtil<>(DppQualityLogRespVO.class);
        util.exportExcel(response, DppQualityLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量日志列表")
    @Log(title = "数据质量日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityLogRespVO> util = new ExcelUtil<>(DppQualityLogRespVO.class);
        List<DppQualityLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityLogService.importDppQualityLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量日志详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityLogDO dppQualityLogDO = dppQualityLogService.getDppQualityLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppQualityLogDO, DppQualityLogRespVO.class));
    }

    @Operation(summary = "新增数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityLogSaveReqVO dppQualityLog) {
        dppQualityLog.setCreatorId(getUserId());
        dppQualityLog.setCreateBy(getNickName());
        dppQualityLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityLogService.createDppQualityLog(dppQualityLog));
    }

    @Operation(summary = "修改数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityLogSaveReqVO dppQualityLog) {
        dppQualityLog.setUpdatorId(getUserId());
        dppQualityLog.setUpdateBy(getNickName());
        dppQualityLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityLogService.updateDppQualityLog(dppQualityLog));
    }

    @Operation(summary = "删除数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityLogService.removeDppQualityLog(Arrays.asList(ids)));
    }

}
