package tech.qiantong.qdata.module.att.controller.admin.cat;

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
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.convert.cat.AttTaskCatConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.module.att.service.cat.IAttTaskCatService;

/**
 * 数据集成任务类目管理Controller
 *
 * @author qdata
 * @date 2025-03-11
 */
@Tag(name = "数据集成任务类目管理")
@RestController
@RequestMapping("/att/taskCat")
@Validated
public class AttTaskCatController extends BaseController {
    @Resource
    private IAttTaskCatService attTaskCatService;

    @Operation(summary = "查询数据集成任务类目管理列表")
    @GetMapping("/list")
    public CommonResult<List<AttTaskCatRespVO>> list(AttTaskCatPageReqVO attTaskCat) {
        List<AttTaskCatDO> attTaskCatDOList = attTaskCatService.getAttTaskCatList(attTaskCat);
        return CommonResult.success(BeanUtils.toBean(attTaskCatDOList, AttTaskCatRespVO.class));
    }

    @Operation(summary = "导出数据集成任务类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:taskCat:export')")
    @Log(title = "数据集成任务类目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttTaskCatPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttTaskCatDO> list = (List<AttTaskCatDO>) attTaskCatService.getAttTaskCatPage(exportReqVO).getRows();
        ExcelUtil<AttTaskCatRespVO> util = new ExcelUtil<>(AttTaskCatRespVO.class);
        util.exportExcel(response, AttTaskCatConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成任务类目管理列表")
    @PreAuthorize("@ss.hasPermi('att:taskCat:import')")
    @Log(title = "数据集成任务类目管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttTaskCatRespVO> util = new ExcelUtil<>(AttTaskCatRespVO.class);
        List<AttTaskCatRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attTaskCatService.importAttTaskCat(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成任务类目管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:taskCat:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttTaskCatRespVO> getInfo(@PathVariable("id") Long id) {
        AttTaskCatDO attTaskCatDO = attTaskCatService.getAttTaskCatById(id);
        return CommonResult.success(BeanUtils.toBean(attTaskCatDO, AttTaskCatRespVO.class));
    }

    @Operation(summary = "新增数据集成任务类目管理")
    @PreAuthorize("@ss.hasPermi('att:taskCat:add')")
    @Log(title = "数据集成任务类目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttTaskCatSaveReqVO attTaskCat) {
        attTaskCat.setCreatorId(getUserId());
        attTaskCat.setCreateBy(getNickName());
        attTaskCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attTaskCatService.createAttTaskCat(attTaskCat));
    }

    @Operation(summary = "修改数据集成任务类目管理")
    @PreAuthorize("@ss.hasPermi('att:taskCat:edit')")
    @Log(title = "数据集成任务类目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttTaskCatSaveReqVO attTaskCat) {
        attTaskCat.setUpdatorId(getUserId());
        attTaskCat.setUpdateBy(getNickName());
        attTaskCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attTaskCatService.updateAttTaskCat(attTaskCat));
    }

    @Operation(summary = "删除数据集成任务类目管理")
    @PreAuthorize("@ss.hasPermi('att:taskCat:remove')")
    @Log(title = "数据集成任务类目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attTaskCatService.removeAttTaskCat(Arrays.asList(ids)));
    }

}
