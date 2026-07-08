package tech.qiantong.qdata.module.dg.controller.admin.standard;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemDO;
import tech.qiantong.qdata.module.dg.service.standard.IDgDataElemService;


import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 数据元Controller
 *
 * @author qdata
 * @date 2025-01-21
 */
@Tag(name = "数据元")
@RestController
@RequestMapping("/dg/dataElem")
@Validated
public class DgDataElemController extends BaseController {
    @Resource
    private IDgDataElemService service;

    @Operation(summary = "查询数据元列表")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgDataElemRespVO>> list(DgDataElemPageReqVO dgDataElem) {
        PageResult<DgDataElemDO> page = service.getDgDataElemPage(dgDataElem);
        return CommonResult.success(BeanUtils.toBean(page, DgDataElemRespVO.class));
    }

    @Operation(summary = "查询数据元列表")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:list')")
    @GetMapping("/getDgDataElemList")
    public CommonResult<List<DgDataElemRespVO>> getDgDataElemList(DgDataElemPageReqVO dgDataElem) {
        List<DgDataElemDO> list = service.getDgDataElemList(dgDataElem);
        return CommonResult.success(BeanUtils.toBean(list, DgDataElemRespVO.class));
    }

    @Operation(summary = "获取数据元详细信息")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataElemRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataElemDO dgDataElemDO = service.getDgDataElemById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataElemDO, DgDataElemRespVO.class));
    }

    @Operation(summary = "新增数据元")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:add')")
    @Log(title = "log.op.title.dg.data.elem", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataElemSaveReqVO dgDataElem) {
        dgDataElem.setCreatorId(getUserId());
        dgDataElem.setCreateBy(getNickName());
        dgDataElem.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(service.createDgDataElem(dgDataElem));
    }

    @Operation(summary = "修改数据元")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:edit')")
    @Log(title = "log.op.title.dg.data.elem", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataElemSaveReqVO dgDataElem) {
        dgDataElem.setUpdatorId(getUserId());
        dgDataElem.setUpdateBy(getNickName());
        dgDataElem.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(service.updateDgDataElem(dgDataElem));
    }

    @Operation(summary = "删除数据元")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:remove')")
    @Log(title = "log.op.title.dg.data.elem", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(service.removeDgDataElem(Arrays.asList(ids)));
    }

    @Operation(summary = "更改数据元状态")
    @PreAuthorize("@ss.hasPermi('dg:dataElem:edit')")
    @Log(title = "log.op.title.dg.data.elem.status", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus/{id}/{status}")
    public CommonResult<Boolean> updateStatus(@PathVariable Long id, @PathVariable Long status) {
        return CommonResult.toAjax(service.updateStatus(id, status));
    }


}
