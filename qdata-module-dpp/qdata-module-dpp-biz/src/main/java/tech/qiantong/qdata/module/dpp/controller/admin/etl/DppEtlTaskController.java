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
import javax.validation.Valid;
import java.util.Arrays;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskService;

/**
 * Data Integration Task Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "Data Integration Task")
@RestController
@RequestMapping("/dpp/etlTask")
@Validated
public class DppEtlTaskController extends BaseController {
    @Resource
    private IDppEtlTaskService dppEtlTaskService;

    @Operation(summary = "查询数据集成任务列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlTaskRespVO>> list(DppEtlTaskPageReqVO dppEtlTask) {
        if (StringUtils.isBlank(dppEtlTask.getType())) {
            dppEtlTask.setType("1");// Default offline data integration
        }
        PageResult<DppEtlTaskDO> page = dppEtlTaskService.getDppEtlTaskPage(dppEtlTask);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlTaskRespVO.class));
    }

    @Operation(summary = "查询数据集成任务列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:list')")
    @GetMapping("/getDppEtlTaskPage")
    public CommonResult<PageResult<DppEtlTaskRespVO>> getDppEtlTaskPageList(DppEtlTaskPageReqVO dppEtlTask) {
        if (StringUtils.isBlank(dppEtlTask.getType())) {
            dppEtlTask.setType("1");// Default offline data integration
        }
        return CommonResult.success(dppEtlTaskService.getDppEtlTaskPageList(dppEtlTask));
    }

    @Operation(summary = "Get data integration task statistics")
    @GetMapping("/statistics")
    public CommonResult<DppEtlTaskStatisticsRespVO> statistics(
            @RequestParam Long projectId,
            @RequestParam String projectCode,
            @RequestParam(defaultValue = "1") String taskType) {
        return CommonResult.success(
                dppEtlTaskService.getStatistics(projectId, projectCode, taskType));
    }


    @Operation(summary = "删除数据集成任务")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:remove')")
    @Log(title = "log.op.title.dpp.etl", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlTaskService.removeDppEtlTask(Arrays.asList(ids)));
    }

    /**
     * 2025-06-18 revision, this is the historical version
     *
     * @param dppEtlNewNodeSaveReqVO
     * @return
     */
    @Operation(summary = "新增节点")
    @PostMapping("/createProcessDefinitionEx")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult createProcessDefinition(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (StringUtils.isBlank(dppEtlNewNodeSaveReqVO.getType())) {
            dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        }
        DppEtlTaskSaveReqVO result = dppEtlTaskService.createProcessDefinition(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "上下线")
    @PostMapping("/updateReleaseTask")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult releaseTask(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        Map<String, Object> result = dppEtlTaskService.updateReleaseTask(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }


    @Operation(summary = "上下线-任务")
    @PostMapping("/updateReleaseJobTask")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateReleaseJobTask(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        Map<String, Object> result = dppEtlTaskService.updateReleaseJobTask(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "上下线-调度")
    @PostMapping("/updateReleaseSchedule")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateReleaseSchedule(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        Map<String, Object> result = dppEtlTaskService.updateReleaseSchedule(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "修改调度cron表达式")
    @PostMapping("/releaseTaskCrontab")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult releaseTaskCrontab(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        Map<String, Object> result = dppEtlTaskService.releaseTaskCrontab(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "新增数据集成节点-获取唯一健")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:add')")
    @GetMapping("/getNodeUniqueKey")
    public CommonResult<Long> getNodeUniqueKey(DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        return CommonResult.success(dppEtlTaskService.getNodeUniqueKey(dppEtlNewNodeSaveReqVO));
    }

    @Operation(summary = "获取数据集成任务详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlTaskRespVO> getInfo(@PathVariable("id") Long id) {
        return CommonResult.success(dppEtlTaskService.getDppEtlTaskById(id));
    }


    @Operation(summary = "获取数据集成任务详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:query')")
    @GetMapping(value = "/updateQuery/{id}")
    public CommonResult<DppEtlTaskUpdateQueryRespVO> getuUpdateQueryInfo(@PathVariable("id") Long id) {
        return CommonResult.success(dppEtlTaskService.getuUpdateQueryInfo(id));
    }


    @Operation(summary = "修改节点")
    @PostMapping("/updateProcessDefinition")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateProcessDefinition(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        DppEtlTaskSaveReqVO result = dppEtlTaskService.updateProcessDefinition(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "查询数据集成任务列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:list')")
    @GetMapping("/getDppEtlTaskListTree")
    public CommonResult<List<DppEtlTaskTreeRespVO>> getDppEtlTaskListTree(DppEtlTaskPageReqVO dppEtlTask) {
        return CommonResult.success(dppEtlTaskService.getDppEtlTaskListTree(dppEtlTask));
    }


    @Operation(summary = "查询数据集成任务列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:list')")
    @GetMapping("/getSubTaskStatusList")
    public CommonResult<List<DppEtlTaskRespVO>> getSubTaskStatusList(DppEtlTaskPageReqVO dppEtlTask) {
        return CommonResult.success(dppEtlTaskService.getSubTaskStatusList(dppEtlTask));
    }


    @Log(title = "log.op.title.dpp.task.trigger", businessType = BusinessType.UPDATE)
    @PutMapping("/startDppEtlTask/{id}")
    public AjaxResult startDppEtlTask(@PathVariable("id") Long id) {
        return dppEtlTaskService.startDppEtlTask(id);
    }


    @Operation(summary = "新增数据汇聚任务")
    @PostMapping("/createEtlTask")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult createEtlTask(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        DppEtlTaskSaveReqVO result = dppEtlTaskService.createEtlTask(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "修改数据汇聚任务")
    @PostMapping("/updateEtlTask")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult updateEtlTask(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        DppEtlTaskSaveReqVO result = dppEtlTaskService.updateEtlTask(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "新增数据汇聚任务")
    @PostMapping("/createEtlTaskFront")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult createEtlTaskFront(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (StringUtils.isBlank(dppEtlNewNodeSaveReqVO.getType())) {
            dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        }
        DppEtlNewNodeSaveReqVO result = dppEtlTaskService.createEtlTaskFront(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "新增数据汇聚任务")
    @PostMapping("/createEtlTaskFrontPostposition")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult createEtlTaskFrontPostposition(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (StringUtils.isBlank(dppEtlNewNodeSaveReqVO.getType())) {
            dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        }
        DppEtlTaskSaveReqVO result = dppEtlTaskService.createEtlTaskFrontPostposition(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "复制数据汇聚任务")
    @PostMapping("/copyCreateEtl")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResult copyCreateEtl(@Valid @RequestBody DppEtlNewNodeSaveReqVO dppEtlNewNodeSaveReqVO) {
        if (StringUtils.isBlank(dppEtlNewNodeSaveReqVO.getType())) {
            dppEtlNewNodeSaveReqVO.setType("1");// Default offline data integration
        }
        DppEtlTaskSaveReqVO result = dppEtlTaskService.copyCreateEtl(dppEtlNewNodeSaveReqVO);
        return CommonResult.success(result);
    }

    @Operation(summary = "获取数据集成任务详细信息--前置草稿任务")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTask:query')")
    @GetMapping(value = "/updateQueryFront/{id}")
    public CommonResult<DppEtlTaskUpdateQueryRespVO> getupdateQueryFront(@PathVariable("id") Long id) {
        return CommonResult.success(dppEtlTaskService.getupdateQueryFront(id));
    }

}
