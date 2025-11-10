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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
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
import tech.qiantong.qdata.common.core.domain.ReturnT;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.LogResult;
import tech.qiantong.qdata.module.dpp.convert.etl.DppQualityLogConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;

/**
 * 数据质量日志Controller
 *
 * @author qdata
 * @date 2025-07-19
 */
@Tag(name = "数据质量日志")
@RestController
@RequestMapping("/dpp/qualityLog")
@Validated
public class DppQualityLogController extends BaseController {
    @Resource
    private IDppQualityLogService dppQualityLogService;

    @Operation(summary = "查询数据质量日志列表")
    @GetMapping("/list")
    public CommonResult<PageResult<DppQualityLogRespVO>> list(DppQualityLogPageReqVO dppQualityLog) {
        PageResult<DppQualityLogDO> page = dppQualityLogService.getDppQualityLogPage(dppQualityLog);
        return CommonResult.success(BeanUtils.toBean(page, DppQualityLogRespVO.class));
    }

    @Operation(summary = "导出数据质量日志列表")
    @Log(title = "数据质量日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppQualityLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppQualityLogDO> list = (List<DppQualityLogDO>) dppQualityLogService.getDppQualityLogPage(exportReqVO).getRows();
        ExcelUtil<DppQualityLogRespVO> util = new ExcelUtil<>(DppQualityLogRespVO.class);
        util.exportExcel(response, DppQualityLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据质量日志列表")
    @Log(title = "数据质量日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppQualityLogRespVO> util = new ExcelUtil<>(DppQualityLogRespVO.class);
        List<DppQualityLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppQualityLogService.importDppQualityLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据质量日志详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<DppQualityLogRespVO> getInfo(@PathVariable("id") Long id) {
        DppQualityLogDO dppQualityLogDO = dppQualityLogService.getDppQualityLogById(id);
        return CommonResult.success(BeanUtils.toBean(dppQualityLogDO, DppQualityLogRespVO.class));
    }

    @Operation(summary = "新增数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppQualityLogSaveReqVO dppQualityLog) {
        dppQualityLog.setCreatorId(getUserId());
        dppQualityLog.setCreateBy(getNickName());
        dppQualityLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityLogService.createDppQualityLog(dppQualityLog));
    }

    @Operation(summary = "修改数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppQualityLogSaveReqVO dppQualityLog) {
        dppQualityLog.setUpdatorId(getUserId());
        dppQualityLog.setUpdateBy(getNickName());
        dppQualityLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppQualityLogService.updateDppQualityLog(dppQualityLog));
    }

    @Operation(summary = "删除数据质量日志")
    @Log(title = "数据质量日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppQualityLogService.removeDppQualityLog(Arrays.asList(ids)));
    }


    @RequestMapping(value = "/logDetailCat", method = RequestMethod.GET)
    @Operation(summary = "运行日志详情")
    public ReturnT<LogResult> logDetailCat(String handleMsg) {
        // 添加日志审计功能
        try {
            InputStream in = new FileInputStream(handleMsg);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            String logContent = new String(bos.toByteArray(), "UTF-8");
            if (bos != null) {
                bos.close();
            }
            if (in != null) {
                in.close();
            }
            // @TODO 查看日志
            ReturnT<LogResult> returnT = new ReturnT<>(ReturnT.SUCCESS_CODE, "查询日志成功");
            LogResult logResult = new LogResult(0, 0, logContent, true);
            returnT.setContent(logResult);
            return returnT;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<>(ReturnT.FAIL_CODE, "暂未找到日志文件信息");
        }
    }

}
