/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dpp.controller.admin.etl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import cn.hutool.core.date.DateUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
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
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlNodeInstanceConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceService;
import tech.qiantong.qdata.module.dpp.utils.TaskConverter;
import tech.qiantong.qdata.redis.service.IRedisService;

/**
 * Data Integration Node Instance Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "Data Integration Node Instance")
@RestController
@RequestMapping("/dpp/etlNodeInstance")
@Validated
public class DppEtlNodeInstanceController extends BaseController {
    @Resource
    private IDppEtlNodeInstanceService dppEtlNodeInstanceService;

    @Resource
    private IRedisService redisService;

    @Resource
    private IDppEtlNodeInstanceLogService dppEtlNodeInstanceLogService;

    @Operation(summary = "查询数据集成节点实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlNodeInstanceRespVO>> list(DppEtlNodeInstancePageReqVO dppEtlNodeInstance) {
        PageResult<DppEtlNodeInstanceDO> page = dppEtlNodeInstanceService.getDppEtlNodeInstancePage(dppEtlNodeInstance);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlNodeInstanceRespVO.class));
    }

    @Operation(summary = "导出数据集成节点实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:export')")
    @Log(title = "log.op.title.dpp.node.instance", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlNodeInstancePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlNodeInstanceDO> list = (List<DppEtlNodeInstanceDO>) dppEtlNodeInstanceService.getDppEtlNodeInstancePage(exportReqVO).getRows();
        ExcelUtil<DppEtlNodeInstanceRespVO> util = new ExcelUtil<>(DppEtlNodeInstanceRespVO.class);
        util.exportExcel(response, DppEtlNodeInstanceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成节点实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:import')")
    @Log(title = "log.op.title.dpp.node.instance", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlNodeInstanceRespVO> util = new ExcelUtil<>(DppEtlNodeInstanceRespVO.class);
        List<DppEtlNodeInstanceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlNodeInstanceService.importDppEtlNodeInstance(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成节点实例详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlNodeInstanceRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlNodeInstanceDO dppEtlNodeInstanceDO = dppEtlNodeInstanceService.getDppEtlNodeInstanceById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlNodeInstanceDO, DppEtlNodeInstanceRespVO.class));
    }

    @Operation(summary = "新增数据集成节点实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:add')")
    @Log(title = "log.op.title.dpp.node.instance", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlNodeInstanceSaveReqVO dppEtlNodeInstance) {
        dppEtlNodeInstance.setCreatorId(getUserId());
        dppEtlNodeInstance.setCreateBy(getNickName());
        dppEtlNodeInstance.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeInstanceService.createDppEtlNodeInstance(dppEtlNodeInstance));
    }

    @Operation(summary = "修改数据集成节点实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:edit')")
    @Log(title = "log.op.title.dpp.node.instance", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlNodeInstanceSaveReqVO dppEtlNodeInstance) {
        dppEtlNodeInstance.setUpdatorId(getUserId());
        dppEtlNodeInstance.setUpdateBy(getNickName());
        dppEtlNodeInstance.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeInstanceService.updateDppEtlNodeInstance(dppEtlNodeInstance));
    }

    @Operation(summary = "删除数据集成节点实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:remove')")
    @Log(title = "log.op.title.dpp.node.instance", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlNodeInstanceService.removeDppEtlNodeInstance(Arrays.asList(ids)));
    }

    @Operation(summary = "查看日志详情")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:query')")
    @GetMapping(value = "/log/{id}")
    public AjaxResult getLogInfo(@PathVariable("id") Long id) {
        DppEtlNodeInstanceDO dppEtlNodeInstanceDO = dppEtlNodeInstanceService.getDppEtlNodeInstanceById(id);
        String content = "";
        String taskInstanceLogKey = TaskConverter.TASK_INSTANCE_LOG_KEY+ dppEtlNodeInstanceDO.getId();
        if (redisService.hasKey(taskInstanceLogKey)) {
            content += redisService.get(taskInstanceLogKey) + "\n";
        } else {
            // Get log from table
            String logContent = dppEtlNodeInstanceLogService.getLog(dppEtlNodeInstanceDO.getId());
            if (logContent != null) {
                content += logContent + "\n";
            }
        }
        return AjaxResult.success(content);
    }

    @RequestMapping(value = "/downloadLog", method = RequestMethod.POST)
    @Operation(summary = "下载日志文件")
    public void downloadLog(HttpServletResponse response, Long nodeInstanceId,String name) {
        try {
            // Get log
            String log = dppEtlNodeInstanceService.getLogByNodeInstanceId(nodeInstanceId);
            // If file exists
            // Set response content type to file download
            response.setContentType("application/octet-stream");
            // Set download filename
            response.setHeader("Content-Disposition", "attachment;filename=" + name + ".log");

            // Create file input stream
            try (InputStream in = new ByteArrayInputStream(log.getBytes("UTF-8"));
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                // Write file content to output stream
                while ((length = in.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
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

}
