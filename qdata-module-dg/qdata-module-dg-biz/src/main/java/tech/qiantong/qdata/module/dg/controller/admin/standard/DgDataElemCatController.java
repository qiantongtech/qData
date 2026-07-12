package tech.qiantong.qdata.module.dg.controller.admin.standard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;
import tech.qiantong.qdata.module.dg.service.standard.IDgDataElemCatService;


import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Data Element Category Management Controller
 *
 * @author qdata
 * @date 2025-01-20
 */
@Tag(name = "Data Element Category Management")
@RestController
@RequestMapping("/dg/dataElemCat")
@Validated
public class DgDataElemCatController extends BaseController {
    @Resource
    private IDgDataElemCatService service;

    @Operation(summary = "Query element category list")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:list')")
    @GetMapping("/list")
    public CommonResult<List<DgDataElemCatRespVO>> list(DgDataElemCatPageReqVO dgDataElemCat) {
        List<DgDataElemCatDO> dgDataElemCatList = service.getDgDataElemCatList(dgDataElemCat);
        return CommonResult.success(BeanUtils.toBean(dgDataElemCatList, DgDataElemCatRespVO.class));
    }

    @Operation(summary = "Get element category details")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataElemCatRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataElemCatDO dgDataElemCatDO = service.getDgDataElemCatById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataElemCatDO, DgDataElemCatRespVO.class));
    }

    @Operation(summary = "Create element category")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:add')")
    @Log(title = "log.op.title.dg.data.elem.cat", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataElemCatSaveReqVO dgDataElemCat) {
        return CommonResult.toAjax(service.createDgDataElemCat(dgDataElemCat));
    }

    @Operation(summary = "Update element category")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:edit')")
    @Log(title = "log.op.title.dg.data.elem.cat", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataElemCatSaveReqVO dgDataElemCat) {
        return CommonResult.toAjax(service.updateDgDataElemCat(dgDataElemCat));
    }

    @Operation(summary = "Delete element category")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:remove')")
    @Log(title = "log.op.title.dg.data.elem.cat", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(service.removeDgDataElemCat(Arrays.asList(ids)));
    }

    /**
     * Batch delete check, query the number of deletable and non-deletable items
     */
    @Operation(summary = "Batch delete check element category")
    @PreAuthorize("@ss.hasPermi('dg:dataElemCat:remove')")
    @GetMapping("/batchDeleteCheck/{ids}")
    public CommonResult<BatchDeleteCheck<Long>> batchDeleteCheck(@PathVariable Long[] ids) {
        BatchDeleteCheck<Long> result = service.batchDeleteCheck(Arrays.asList(ids));
        return CommonResult.success(result);
    }

}
