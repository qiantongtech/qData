package tech.qiantong.qdata.module.att.controller.admin.client;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

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
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;
import tech.qiantong.qdata.module.att.service.client.IAttClientApiRelService;

/**
 * 应用API服务关联Controller
 *
 * @author FXB
 * @date 2025-08-21
 */
@Tag(name = "应用API服务关联")
@RestController
@RequestMapping("/att/clientApiRel")
@Validated
public class AttClientApiRelController extends BaseController {
    @Resource
    private IAttClientApiRelService attClientApiRelService;

    @Operation(summary = "查询应用API服务关联列表")
    @GetMapping("/list")
    public CommonResult<PageResult<AttClientApiRelRespVO>> list(AttClientApiRelPageReqVO attClientApiRel) {
        PageResult<AttClientApiRelDO> page = attClientApiRelService.getAttClientApiRelPage(attClientApiRel);
        return CommonResult.success(BeanUtils.toBean(page, AttClientApiRelRespVO.class));
    }

    @Operation(summary = "新增应用API服务关联")
//    @PreAuthorize("@ss.hasPermi('att:clientApiRel:add')")
    @Log(title = "应用API服务关联", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttClientApiRelSaveReqVO attClientApiRel) {
        attClientApiRel.setCreatorId(getUserId());
        attClientApiRel.setCreateBy(getNickName());
        attClientApiRel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attClientApiRelService.createAttClientApiRel(attClientApiRel));
    }

    @Operation(summary = "修改应用API服务关联")
//    @PreAuthorize("@ss.hasPermi('att:clientApiRel:edit')")
    @Log(title = "应用API服务关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttClientApiRelSaveReqVO attClientApiRel) {
        attClientApiRel.setUpdatorId(getUserId());
        attClientApiRel.setUpdateBy(getNickName());
        attClientApiRel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attClientApiRelService.updateAttClientApiRel(attClientApiRel));
    }

    @Operation(summary = "删除应用API服务关联")
//    @PreAuthorize("@ss.hasPermi('att:clientApiRel:remove')")
    @Log(title = "应用API服务关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attClientApiRelService.removeAttClientApiRel(Arrays.asList(ids)));
    }

}
