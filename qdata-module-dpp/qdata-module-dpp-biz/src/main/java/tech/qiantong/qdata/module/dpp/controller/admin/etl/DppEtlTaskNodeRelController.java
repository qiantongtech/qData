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
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlTaskNodeRelConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskNodeRelService;

/**
 * 数据集成任务节点关系Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成任务节点关系")
@RestController
@RequestMapping("/dpp/etlTaskNodeRel")
@Validated
public class DppEtlTaskNodeRelController extends BaseController {
    @Resource
    private IDppEtlTaskNodeRelService dppEtlTaskNodeRelService;

    @Operation(summary = "查询数据集成任务节点关系列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlTaskNodeRelRespVO>> list(DppEtlTaskNodeRelPageReqVO dppEtlTaskNodeRel) {
        PageResult<DppEtlTaskNodeRelDO> page = dppEtlTaskNodeRelService.getDppEtlTaskNodeRelPage(dppEtlTaskNodeRel);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlTaskNodeRelRespVO.class));
    }

    @Operation(summary = "导出数据集成任务节点关系列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:export')")
    @Log(title = "数据集成任务节点关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlTaskNodeRelPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlTaskNodeRelDO> list = (List<DppEtlTaskNodeRelDO>) dppEtlTaskNodeRelService.getDppEtlTaskNodeRelPage(exportReqVO).getRows();
        ExcelUtil<DppEtlTaskNodeRelRespVO> util = new ExcelUtil<>(DppEtlTaskNodeRelRespVO.class);
        util.exportExcel(response, DppEtlTaskNodeRelConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成任务节点关系列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:import')")
    @Log(title = "数据集成任务节点关系", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlTaskNodeRelRespVO> util = new ExcelUtil<>(DppEtlTaskNodeRelRespVO.class);
        List<DppEtlTaskNodeRelRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlTaskNodeRelService.importDppEtlTaskNodeRel(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成任务节点关系详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlTaskNodeRelRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlTaskNodeRelDO dppEtlTaskNodeRelDO = dppEtlTaskNodeRelService.getDppEtlTaskNodeRelById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlTaskNodeRelDO, DppEtlTaskNodeRelRespVO.class));
    }

    @Operation(summary = "新增数据集成任务节点关系")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:add')")
    @Log(title = "数据集成任务节点关系", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlTaskNodeRelSaveReqVO dppEtlTaskNodeRel) {
        dppEtlTaskNodeRel.setCreatorId(getUserId());
        dppEtlTaskNodeRel.setCreateBy(getNickName());
        dppEtlTaskNodeRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskNodeRelService.createDppEtlTaskNodeRel(dppEtlTaskNodeRel));
    }

    @Operation(summary = "修改数据集成任务节点关系")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:edit')")
    @Log(title = "数据集成任务节点关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlTaskNodeRelSaveReqVO dppEtlTaskNodeRel) {
        dppEtlTaskNodeRel.setUpdatorId(getUserId());
        dppEtlTaskNodeRel.setUpdateBy(getNickName());
        dppEtlTaskNodeRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskNodeRelService.updateDppEtlTaskNodeRel(dppEtlTaskNodeRel));
    }

    @Operation(summary = "删除数据集成任务节点关系")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskNodeRel:remove')")
    @Log(title = "数据集成任务节点关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlTaskNodeRelService.removeDppEtlTaskNodeRel(Arrays.asList(ids)));
    }

}
