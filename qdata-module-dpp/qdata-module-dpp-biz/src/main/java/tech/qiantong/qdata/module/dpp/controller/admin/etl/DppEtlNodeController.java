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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlNodeConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeService;

/**
 * 数据集成节点Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成节点")
@RestController
@RequestMapping("/dpp/etlNode")
@Validated
public class DppEtlNodeController extends BaseController {
    @Resource
    private IDppEtlNodeService dppEtlNodeService;

    @Operation(summary = "查询数据集成节点列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlNodeRespVO>> list(DppEtlNodePageReqVO dppEtlNode) {
        PageResult<DppEtlNodeDO> page = dppEtlNodeService.getDppEtlNodePage(dppEtlNode);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlNodeRespVO.class));
    }

    @Operation(summary = "导出数据集成节点列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:export')")
    @Log(title = "数据集成节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlNodePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlNodeDO> list = (List<DppEtlNodeDO>) dppEtlNodeService.getDppEtlNodePage(exportReqVO).getRows();
        ExcelUtil<DppEtlNodeRespVO> util = new ExcelUtil<>(DppEtlNodeRespVO.class);
        util.exportExcel(response, DppEtlNodeConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成节点列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:import')")
    @Log(title = "数据集成节点", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlNodeRespVO> util = new ExcelUtil<>(DppEtlNodeRespVO.class);
        List<DppEtlNodeRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlNodeService.importDppEtlNode(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成节点详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlNodeRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlNodeDO dppEtlNodeDO = dppEtlNodeService.getDppEtlNodeById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlNodeDO, DppEtlNodeRespVO.class));
    }

    @Operation(summary = "新增数据集成节点")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:add')")
    @Log(title = "数据集成节点", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlNodeSaveReqVO dppEtlNode) {
        dppEtlNode.setCreatorId(getUserId());
        dppEtlNode.setCreateBy(getNickName());
        dppEtlNode.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeService.createDppEtlNode(dppEtlNode));
    }

    @Operation(summary = "修改数据集成节点")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:edit')")
    @Log(title = "数据集成节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlNodeSaveReqVO dppEtlNode) {
        dppEtlNode.setUpdatorId(getUserId());
        dppEtlNode.setUpdateBy(getNickName());
        dppEtlNode.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeService.updateDppEtlNode(dppEtlNode));
    }

    @Operation(summary = "删除数据集成节点")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNode:remove')")
    @Log(title = "数据集成节点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlNodeService.removeDppEtlNode(Arrays.asList(ids)));
    }
}
