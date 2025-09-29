package tech.qiantong.qdata.module.att.controller.admin.client;

import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.convert.client.AttClientConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;
import tech.qiantong.qdata.module.att.service.client.IAttClientService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 应用管理Controller
 *
 * @author qdata
 * @date 2025-02-18
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/att/client")
@Validated
public class AttClientController extends BaseController {
    @Resource
    private IAttClientService attClientService;

    @Operation(summary = "查询应用管理列表")
    @PreAuthorize("@ss.hasPermi('att:client:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttClientRespVO>> list(AttClientPageReqVO attClient) {
        PageResult<AttClientDO> page = attClientService.getAttClientPage(attClient);
        return CommonResult.success(BeanUtils.toBean(page, AttClientRespVO.class));
    }

    @Operation(summary = "导出应用管理列表")
    @PreAuthorize("@ss.hasPermi('att:client:export')")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttClientPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttClientDO> list = (List<AttClientDO>) attClientService.getAttClientPage(exportReqVO).getRows();
        ExcelUtil<AttClientRespVO> util = new ExcelUtil<>(AttClientRespVO.class);
        util.exportExcel(response, AttClientConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入应用管理列表")
    @PreAuthorize("@ss.hasPermi('att:client:import')")
    @Log(title = "应用管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttClientRespVO> util = new ExcelUtil<>(AttClientRespVO.class);
        List<AttClientRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attClientService.importAttClient(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取应用管理详细信息")
    @PreAuthorize("@ss.hasPermi('att:client:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttClientRespVO> getInfo(@PathVariable("id") Long id) {
        AttClientDO attClientDO = attClientService.getAttClientById(id);
        return CommonResult.success(BeanUtils.toBean(attClientDO, AttClientRespVO.class));
    }

    @Operation(summary = "新增应用管理")
    @PreAuthorize("@ss.hasPermi('att:client:add')")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttClientSaveReqVO attClient) {
        return CommonResult.toAjax(attClientService.createAttClient(attClient));
    }

    @Operation(summary = "修改应用管理")
    @PreAuthorize("@ss.hasPermi('att:client:edit')")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttClientSaveReqVO attClient) {
        return CommonResult.toAjax(attClientService.updateAttClient(attClient));
    }

    @Operation(summary = "删除应用管理")
    @PreAuthorize("@ss.hasPermi('att:client:remove')")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attClientService.removeAttClient(Arrays.asList(ids)));
    }

    @Operation(summary = "重置应用秘钥")
    @PreAuthorize("@ss.hasPermi('att:client:edit')")
    @Log(title = "重置应用秘钥", businessType = BusinessType.UPDATE)
    @PostMapping("/reset/secret")
    public CommonResult<String> resetSecret(Long id) {
        AttClientDO client = attClientService.getAttClientById(id);
        client.setSecret(IdUtil.fastSimpleUUID());
        attClientService.updateById(client);
        return CommonResult.success(client.getSecret());
    }
}
