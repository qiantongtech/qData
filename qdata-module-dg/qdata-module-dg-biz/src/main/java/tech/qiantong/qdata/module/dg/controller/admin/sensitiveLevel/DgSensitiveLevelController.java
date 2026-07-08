package tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel;

import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;
import tech.qiantong.qdata.module.dg.service.sensitiveLevel.IDgSensitiveLevelService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * 敏感等级Controller
 *
 * @author Chaos
 * @date 2025-01-21
 */
@Tag(name = "敏感等级")
@RestController
@RequestMapping("/dg/sensitiveLevel")
@Validated
public class DgSensitiveLevelController extends BaseController {
    @Resource
    private IDgSensitiveLevelService service;

    @Operation(summary = "查询敏感等级列表")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DgSensitiveLevelRespVO>> list(DgSensitiveLevelPageReqVO dgSensitiveLevel) {
        PageResult<DgSensitiveLevelDO> page = service.getDgSensitiveLevelPage(dgSensitiveLevel);
        return CommonResult.success(BeanUtils.toBean(page, DgSensitiveLevelRespVO.class));
    }

    @Operation(summary = "获取敏感等级详细信息")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DgSensitiveLevelRespVO> getInfo(@PathVariable("id") Long id) {
        DgSensitiveLevelDO dgSensitiveLevelDO = service.getDgSensitiveLevelById(id);
        return CommonResult.success(BeanUtils.toBean(dgSensitiveLevelDO, DgSensitiveLevelRespVO.class));
    }

    @Operation(summary = "新增敏感等级")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:add')")
    @Log(title = "log.op.title.dg.sensitive.level", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgSensitiveLevelSaveReqVO dgSensitiveLevel) {
        dgSensitiveLevel.setCreatorId(getUserId());
        dgSensitiveLevel.setCreateBy(getNickName());
        dgSensitiveLevel.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(service.createDgSensitiveLevel(dgSensitiveLevel));
    }

    @Operation(summary = "修改敏感等级")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:edit')")
    @Log(title = "log.op.title.dg.sensitive.level", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgSensitiveLevelSaveReqVO dgSensitiveLevel) {
        dgSensitiveLevel.setUpdatorId(getUserId());
        dgSensitiveLevel.setUpdateBy(getNickName());
        dgSensitiveLevel.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(service.updateDgSensitiveLevel(dgSensitiveLevel));
    }

    @Operation(summary = "修改敏感等级状态")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:edit')")
    @Log(title = "log.op.title.dg.sensitive.level", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus/{id}/{status}")
    public AjaxResult updateStatus(@PathVariable Long id, @PathVariable Long status) {
        if (!service.updateStatus(id, status)) {
            return AjaxResult.error("已被使用，不允许下线！");
        }
        return AjaxResult.success("修改成功");
    }

    @Operation(summary = "删除敏感等级")
    @PreAuthorize("@ss.hasPermi('dg:sensitiveLevel:remove')")
    @Log(title = "log.op.title.dg.sensitive.level", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(service.removeDgSensitiveLevel(Arrays.asList(ids)));
    }

}
