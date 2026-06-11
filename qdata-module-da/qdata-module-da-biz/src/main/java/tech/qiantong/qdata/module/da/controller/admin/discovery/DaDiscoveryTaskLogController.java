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
 */

package tech.qiantong.qdata.module.da.controller.admin.discovery;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qdata.common.core.domain.ReturnT;
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.LogResult;
import tech.qiantong.qdata.module.da.convert.discovery.DaDiscoveryTaskLogConvert;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskLogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * 数据发现任务日志Controller
 *
 * @author qdata
 * @date 2025-02-17
 */
@Tag(name = "数据发现任务日志")
@RestController
@RequestMapping("/da/discoveryTaskLog")
@Validated
public class DaDiscoveryTaskLogController extends BaseController {
    @Resource
    private IDaDiscoveryTaskLogService daDiscoveryTaskLogService;

    @Operation(summary = "查询数据发现任务日志列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DaDiscoveryTaskLogRespVO>> list(DaDiscoveryTaskLogPageReqVO daDiscoveryTaskLog) {
        PageResult<DaDiscoveryTaskLogDO> page = daDiscoveryTaskLogService.getDaDiscoveryTaskLogPage(daDiscoveryTaskLog);
        return CommonResult.success(BeanUtils.toBean(page, DaDiscoveryTaskLogRespVO.class));
    }

    @Operation(summary = "导出数据发现任务日志列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:export')")
    @Log(title = "数据发现任务日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DaDiscoveryTaskLogPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DaDiscoveryTaskLogDO> list = (List<DaDiscoveryTaskLogDO>) daDiscoveryTaskLogService
                .getDaDiscoveryTaskLogPage(exportReqVO).getRows();
        ExcelUtil<DaDiscoveryTaskLogRespVO> util = new ExcelUtil<>(DaDiscoveryTaskLogRespVO.class);
        util.exportExcel(response, DaDiscoveryTaskLogConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据发现任务日志列表")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:import')")
    @Log(title = "数据发现任务日志", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DaDiscoveryTaskLogRespVO> util = new ExcelUtil<>(DaDiscoveryTaskLogRespVO.class);
        List<DaDiscoveryTaskLogRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = daDiscoveryTaskLogService.importDaDiscoveryTaskLog(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据发现任务日志详细信息")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DaDiscoveryTaskLogRespVO> getInfo(@PathVariable("id") Long id) {
        DaDiscoveryTaskLogDO daDiscoveryTaskLogDO = daDiscoveryTaskLogService.getDaDiscoveryTaskLogById(id);
        return CommonResult.success(BeanUtils.toBean(daDiscoveryTaskLogDO, DaDiscoveryTaskLogRespVO.class));
    }

    @Operation(summary = "新增数据发现任务日志")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:add')")
    @Log(title = "数据发现任务日志", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DaDiscoveryTaskLogSaveReqVO daDiscoveryTaskLog) {
        daDiscoveryTaskLog.setCreatorId(getUserId());
        daDiscoveryTaskLog.setCreateBy(getNickName());
        daDiscoveryTaskLog.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTaskLogService.createDaDiscoveryTaskLog(daDiscoveryTaskLog));
    }

    @Operation(summary = "修改数据发现任务日志")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:edit')")
    @Log(title = "数据发现任务日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DaDiscoveryTaskLogSaveReqVO daDiscoveryTaskLog) {
        daDiscoveryTaskLog.setUpdatorId(getUserId());
        daDiscoveryTaskLog.setUpdateBy(getNickName());
        daDiscoveryTaskLog.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(daDiscoveryTaskLogService.updateDaDiscoveryTaskLog(daDiscoveryTaskLog));
    }

    @Operation(summary = "删除数据发现任务日志")
    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:remove')")
    @Log(title = "数据发现任务日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(daDiscoveryTaskLogService.removeDaDiscoveryTaskLog(Arrays.asList(ids)));
    }

    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:list')")
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

    @PreAuthorize("@ss.hasPermi('da:discoveryTaskLog:list')")
    @RequestMapping(value = "/downloadLog", method = RequestMethod.POST)
    @Operation(summary = "下载日志文件")
    public void downloadLog(HttpServletResponse response, String handleMsg) {
        // 添加日志审计功能
        try {
            // 获取文件路径
            File logFile = new File(handleMsg);

            // 如果文件存在
            if (logFile.exists()) {
                // 设置响应的内容类型为文件下载
                response.setContentType("application/octet-stream");
                // 设置下载文件名
                String fileName = logFile.getName();
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

                // 创建文件输入流
                try (InputStream in = new FileInputStream(logFile);
                        OutputStream out = response.getOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int length;
                    // 将文件内容写入输出流
                    while ((length = in.read(buffer)) != -1) {
                        out.write(buffer, 0, length);
                    }
                }
            } else {
                // 如果文件不存在，返回404或自定义错误
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("日志文件未找到");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("文件下载失败：" + e.getMessage());
            } catch (IOException ioException) {
                logger.error("写入错误信息失败", ioException);
            }
        }
    }


    @Operation(summary = "查看日志详情")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:query')")
    @GetMapping(value = "/log/{id}")
    public AjaxResult getLogInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(daDiscoveryTaskLogService.getLogInfo(id));
    }

}
