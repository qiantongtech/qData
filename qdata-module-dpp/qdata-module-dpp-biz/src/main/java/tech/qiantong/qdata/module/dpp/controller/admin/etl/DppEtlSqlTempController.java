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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlSqlTempConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSqlTempService;

/**
 * 数据集成SQL模版Controller
 *
 * @author FXB
 * @date 2025-06-25
 */
@Tag(name = "数据集成SQL模版")
@RestController
@RequestMapping("/dpp/etlSqlTemp")
@Validated
public class DppEtlSqlTempController extends BaseController {
    @Resource
    private IDppEtlSqlTempService dppEtlSqlTempService;

    @Operation(summary = "查询数据集成SQL模版列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlSqlTempRespVO>> list(DppEtlSqlTempPageReqVO dppEtlSqlTemp) {
        PageResult<DppEtlSqlTempDO> page = dppEtlSqlTempService.getDppEtlSqlTempPage(dppEtlSqlTemp);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlSqlTempRespVO.class));
    }


    @Operation(summary = "新增数据集成SQL模版")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlSqlTempSaveReqVO dppEtlSqlTemp) {
        dppEtlSqlTemp.setCreatorId(getUserId());
        dppEtlSqlTemp.setCreateBy(getNickName());
        dppEtlSqlTemp.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSqlTempService.createDppEtlSqlTemp(dppEtlSqlTemp));
    }

    @Operation(summary = "修改数据集成SQL模版")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlSqlTempSaveReqVO dppEtlSqlTemp) {
        dppEtlSqlTemp.setUpdatorId(getUserId());
        dppEtlSqlTemp.setUpdateBy(getNickName());
        dppEtlSqlTemp.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlSqlTempService.updateDppEtlSqlTemp(dppEtlSqlTemp));
    }

    @Operation(summary = "删除数据集成SQL模版")
    @Log(title = "数据集成SQL模版", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlSqlTempService.removeDppEtlSqlTemp(Arrays.asList(ids)));
    }

}
