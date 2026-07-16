package tech.qiantong.qdata.module.mc.controller.admin.metadata;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.*;
import tech.qiantong.qdata.module.mc.convert.metadata.McDbConvert;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;
import tech.qiantong.qdata.security.annotation.BizDataScope;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * DatabaseController
 *
 * @author qdata
 * @date 2026-02-11
 */
@Tag(name = "数据库")
@RestController
@RequestMapping("/mc/db")
@Validated
public class McDbController extends BaseController {
    @Resource
    private IMcDbService mcDbService;

    @Operation(summary = "查询数据库列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/list")
    public CommonResult<PageResult<McDbRespVO>> list(McDbPageReqVO mcDb) {
        PageResult<McDbDO> page = mcDbService.getMcDbPage(mcDb);
        return CommonResult.success(BeanUtils.toBean(page, McDbRespVO.class));
    }

    @Operation(summary = "查询库元数据列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @GetMapping("/getMcDbList")
    public CommonResult<List<McDbRespVO>> getMcDbList(McDbPageReqVO McDb) {
        List<McDbDO> page = mcDbService.getMcDbList(McDb);
        return CommonResult.success(BeanUtils.toBean(page, McDbRespVO.class));
    }


    @Operation(summary = "导出数据库列表")
    @BizDataScope(code = "mc_metadata_list", userField = "businessLeader", deptField = "responsibleDept")
    @Log(title = "log.op.title.mc.db", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, McDbPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<McDbDO> list = (List<McDbDO>) mcDbService.getMcDbPage(exportReqVO).getRows();
        ExcelUtil<McDbRespVO> util = new ExcelUtil<>(McDbRespVO.class);
        util.exportExcel(response, McDbConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "获取数据库详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<McDbRespVO> getInfo(@PathVariable("id") Long id) {
        McDbRespVO respVO = mcDbService.getMcDbById(id);
        return CommonResult.success(respVO);
    }

    @Operation(summary = "新增数据库")
    @Log(title = "log.op.title.mc.db", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody McDbSaveReqVO mcDb) {
        mcDb.setCreatorId(getUserId());
        mcDb.setCreateBy(getNickName());
        mcDb.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(mcDbService.createMcDb(mcDb));
    }

    @Operation(summary = "修改数据库")
    @Log(title = "log.op.title.mc.db", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody McDbSaveReqVO mcDb) {
        mcDb.setUpdatorId(getUserId());
        mcDb.setUpdateBy(getNickName());
        mcDb.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(mcDbService.updateMcDb(mcDb));
    }

    @Operation(summary = "停启用库元数据")
    @Log(title = "log.op.title.mc.db.toggle", businessType = BusinessType.UPDATE)
    @PostMapping("toggle")
    public CommonResult<Integer> toggle(@Valid @RequestBody ToggleStatusVO param) {
        return CommonResult.toAjax(mcDbService.toggle(param.getId(), param.getStatus()));
    }


    @Operation(summary = "停启用库元数据")
    @Log(title = "log.op.title.mc.db.toggle", businessType = BusinessType.UPDATE)
    @PostMapping("/editPortalVisible")
    public CommonResult<Integer> editPortalVisible(@Valid @RequestBody McDbSaveReqVO param) {
        return CommonResult.toAjax(mcDbService.editPortalVisible(param.getId(), param.getPortalVisible()));
    }




    @Operation(summary = "删除数据库")
    @Log(title = "log.op.title.mc.db", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(mcDbService.removeMcDb(Arrays.asList(ids)));
    }


    @Operation(summary = "批量删除检查库元数据")
    @GetMapping("/batchDeleteCheck/{ids}")
    public CommonResult<BatchDeleteCheck<Long>> batchDeleteCheck(@PathVariable Long[] ids) {
        BatchDeleteCheck<Long> result = mcDbService.batchDeleteCheck(Arrays.asList(ids));
        return CommonResult.success(result);
    }


    @Operation(summary = "查询库元数据列表")
    @GetMapping("/selectMetaSearchPage")
    public CommonResult<PageResult<McMetaSearchRespDTO>> selectMetaSearchPage(McMetaSearchRespDTO mdMetaSearchRespDTO) {
        PageResult<McMetaSearchRespDTO> page = mcDbService.selectMetaSearchPage(mdMetaSearchRespDTO);
        return CommonResult.success(page);
    }


}
