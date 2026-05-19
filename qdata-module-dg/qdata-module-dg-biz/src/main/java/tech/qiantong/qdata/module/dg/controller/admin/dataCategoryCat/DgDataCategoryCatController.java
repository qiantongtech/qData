/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat;

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
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.convert.dataCategoryCat.DgDataCategoryCatConvert;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import tech.qiantong.qdata.module.dg.service.dataCategoryCat.IDgDataCategoryCatService;

/**
 * 数据分类-类目Controller
 *
 * @author FXB
 * @date 2026-04-07
 */
@Tag(name = "数据分类-类目")
@RestController
@RequestMapping("/dg/dataCategoryCat")
@Validated
public class DgDataCategoryCatController extends BaseController {
    @Resource
    private IDgDataCategoryCatService dgDataCategoryCatService;

    @Operation(summary = "查询数据分类-类目列表")
    @GetMapping("/list")
    public CommonResult<List<DgDataCategoryCatRespVO>> list() {
        List<DgDataCategoryCatDO> dgDataCategoryCatDOList = dgDataCategoryCatService.getDgDataCategoryCatList();
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryCatDOList, DgDataCategoryCatRespVO.class));
    }

    @Operation(summary = "获取数据分类-类目详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DgDataCategoryCatRespVO> getInfo(@PathVariable("id") Long id) {
        DgDataCategoryCatDO dgDataCategoryCatDO = dgDataCategoryCatService.getDgDataCategoryCatById(id);
        return CommonResult.success(BeanUtils.toBean(dgDataCategoryCatDO, DgDataCategoryCatRespVO.class));
    }

    @Operation(summary = "新增数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:add')")
    @Log(title = "数据分类-类目", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DgDataCategoryCatSaveReqVO dgDataCategoryCat) {
        dgDataCategoryCat.setCreatorId(getUserId());
        dgDataCategoryCat.setCreateBy(getNickName());
        dgDataCategoryCat.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryCatService.createDgDataCategoryCat(dgDataCategoryCat));
    }

    @Operation(summary = "修改数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:edit')")
    @Log(title = "数据分类-类目", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DgDataCategoryCatSaveReqVO dgDataCategoryCat) {
        dgDataCategoryCat.setUpdatorId(getUserId());
        dgDataCategoryCat.setUpdateBy(getNickName());
        dgDataCategoryCat.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dgDataCategoryCatService.updateDgDataCategoryCat(dgDataCategoryCat));
    }

    @Operation(summary = "删除数据分类-类目")
//    @PreAuthorize("@ss.hasPermi('dg:dataCategoryCat:remove')")
    @Log(title = "数据分类-类目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dgDataCategoryCatService.removeDgDataCategoryCat(Arrays.asList(ids)));
    }

}
