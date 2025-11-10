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

package tech.qiantong.qdata.module.da.controller.admin.assetchild.video;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import cn.hutool.core.date.DateUtil;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.convert.assetchild.video.DaAssetVideoConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;
import tech.qiantong.qdata.module.da.service.assetchild.video.IDaAssetVideoService;

/**
 * 数据资产-视频数据Controller
 *
 * @author qdata
 * @date 2025-04-14
 */
@Tag(name = "数据资产-视频数据")
@RestController
@RequestMapping("/da/assetVideo")
@Validated
public class DaAssetVideoController extends BaseController {
    @Resource
    private IDaAssetVideoService daAssetVideoService;

    @Operation(summary = "查询数据资产-视频数据列表")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaAssetVideoRespVO>> list(DaAssetVideoPageReqVO daAssetVideo) {
        PageResult<DaAssetVideoDO> page = daAssetVideoService.getDaAssetVideoPage(daAssetVideo);
        return CommonResult.success(BeanUtils.toBean(page, DaAssetVideoRespVO.class));
    }

    @Operation(summary = "导出数据资产-视频数据列表")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:export')")
    @Log(title = "数据资产-视频数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaAssetVideoPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaAssetVideoDO> list = (List<DaAssetVideoDO>) daAssetVideoService.getDaAssetVideoPage(exportReqVO).getRows();
        ExcelUtil<DaAssetVideoRespVO> util = new ExcelUtil<>(DaAssetVideoRespVO.class);
        util.exportExcel(response, DaAssetVideoConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据资产-视频数据列表")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:import')")
    @Log(title = "数据资产-视频数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaAssetVideoRespVO> util = new ExcelUtil<>(DaAssetVideoRespVO.class);
        List<DaAssetVideoRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daAssetVideoService.importDaAssetVideo(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据资产-视频数据详细信息")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaAssetVideoRespVO> getInfo(@PathVariable("id") Long id) {
        DaAssetVideoDO daAssetVideoDO = daAssetVideoService.getDaAssetVideoById(id);
        return CommonResult.success(BeanUtils.toBean(daAssetVideoDO, DaAssetVideoRespVO.class));
    }

    @Operation(summary = "新增数据资产-视频数据")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:add')")
    @Log(title = "数据资产-视频数据", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaAssetVideoSaveReqVO daAssetVideo) {
        daAssetVideo.setCreatorId(getUserId());
        daAssetVideo.setCreateBy(getNickName());
        daAssetVideo.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetVideoService.createDaAssetVideo(daAssetVideo));
    }

    @Operation(summary = "修改数据资产-视频数据")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:edit')")
    @Log(title = "数据资产-视频数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaAssetVideoSaveReqVO daAssetVideo) {
        daAssetVideo.setUpdatorId(getUserId());
        daAssetVideo.setUpdateBy(getNickName());
        daAssetVideo.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daAssetVideoService.updateDaAssetVideo(daAssetVideo));
    }

    @Operation(summary = "删除数据资产-视频数据")
    @PreAuthorize("@ss.hasPermi('da:assetVideo:remove')")
    @Log(title = "数据资产-视频数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daAssetVideoService.removeDaAssetVideo(Arrays.asList(ids)));
    }



    @Operation(summary = "删除数据资产-视频")
    @PreAuthorize("@ss.hasPermi('da:asset:edit')")
    @PostMapping("/queryServiceForwarding")
    public void queryServiceForwarding(HttpServletResponse response,@Valid @RequestBody DaAssetVideoReqVO daAssetVideoReqVO) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        daAssetVideoService.queryServiceForwarding(response,daAssetVideoReqVO);
    }

}
