package tech.qiantong.qdata.module.dpp.controller.admin.etl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Arrays;

import cn.hutool.core.date.DateUtil;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
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
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.convert.etl.DppEtlTaskInstanceConvert;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;

/**
 * 数据集成任务实例Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成任务实例")
@RestController
@RequestMapping("/dpp/etlTaskInstance")
@Validated
public class DppEtlTaskInstanceController extends BaseController {
    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;

    @Operation(summary = "查询数据集成任务实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<DppEtlTaskInstanceRespVO>> list(DppEtlTaskInstancePageReqVO dppEtlTaskInstance) {
        if (StringUtils.isNotBlank(dppEtlTaskInstance.getTaskType())) {
            dppEtlTaskInstance.setTaskType("1");//默认离线数据集成
        }
        PageResult<DppEtlTaskInstanceDO> page = dppEtlTaskInstanceService.getDppEtlTaskInstancePage(dppEtlTaskInstance);
        return CommonResult.success(BeanUtils.toBean(page, DppEtlTaskInstanceRespVO.class));
    }

    @Operation(summary = "导出数据集成任务实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:export')")
    @Log(title = "数据集成任务实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlTaskInstancePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlTaskInstanceDO> list = (List<DppEtlTaskInstanceDO>) dppEtlTaskInstanceService.getDppEtlTaskInstancePage(exportReqVO).getRows();
        ExcelUtil<DppEtlTaskInstanceRespVO> util = new ExcelUtil<>(DppEtlTaskInstanceRespVO.class);
        util.exportExcel(response, DppEtlTaskInstanceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成任务实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:import')")
    @Log(title = "数据集成任务实例", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<DppEtlTaskInstanceRespVO> util = new ExcelUtil<>(DppEtlTaskInstanceRespVO.class);
        List<DppEtlTaskInstanceRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = dppEtlTaskInstanceService.importDppEtlTaskInstance(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取数据集成任务实例详细信息")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<DppEtlTaskInstanceRespVO> getInfo(@PathVariable("id") Long id) {
        DppEtlTaskInstanceDO dppEtlTaskInstanceDO = dppEtlTaskInstanceService.getDppEtlTaskInstanceById(id);
        return CommonResult.success(BeanUtils.toBean(dppEtlTaskInstanceDO, DppEtlTaskInstanceRespVO.class));
    }

    @Operation(summary = "新增数据集成任务实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:add')")
    @Log(title = "数据集成任务实例", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlTaskInstanceSaveReqVO dppEtlTaskInstance) {
        dppEtlTaskInstance.setCreatorId(getUserId());
        dppEtlTaskInstance.setCreateBy(getNickName());
        dppEtlTaskInstance.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskInstanceService.createDppEtlTaskInstance(dppEtlTaskInstance));
    }

    @Operation(summary = "修改数据集成任务实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:edit')")
    @Log(title = "数据集成任务实例", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlTaskInstanceSaveReqVO dppEtlTaskInstance) {
        dppEtlTaskInstance.setUpdatorId(getUserId());
        dppEtlTaskInstance.setUpdateBy(getNickName());
        dppEtlTaskInstance.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlTaskInstanceService.updateDppEtlTaskInstance(dppEtlTaskInstance));
    }

    @Operation(summary = "删除数据集成任务实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlTaskInstance:remove')")
    @Log(title = "数据集成任务实例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(dppEtlTaskInstanceService.removeDppEtlTaskInstance(Arrays.asList(ids)));
    }

    @Operation(summary = "获取数据集成任务实例列表")
    @GetMapping("/treeList")
    public CommonResult<PageResult<DppEtlTaskInstanceTreeListRespVO>> treeList(DppEtlTaskInstanceTreeListReqVO dppEtlTaskInstance) {
        return CommonResult.success(dppEtlTaskInstanceService.treeList(dppEtlTaskInstance));
    }

    @Operation(summary = "获取子任务列表")
    @GetMapping("/subNodeList")
    public CommonResult<List<DppEtlTaskInstanceTreeListRespVO>> subNodelist(@RequestParam Long taskInstanceId, @RequestParam Long nodeInstanceId) {
        return CommonResult.success(dppEtlTaskInstanceService.subNodelist(taskInstanceId, nodeInstanceId));
    }

    @Operation(summary = "获取正在运行的实例")
    @GetMapping("/getRunTaskInstance")
    public CommonResult<Long> getRunTaskInstance(@RequestParam Long taskId) {
        return CommonResult.success(dppEtlTaskInstanceService.getRunTaskInstance(taskId));
    }

    @Operation(summary = "通过实例id获取日志")
    @GetMapping("/getLogByTaskInstanceId")
    public CommonResult<DppEtlTaskInstanceLogStatusRespDTO> getLogByTaskInstanceId(@RequestParam Long taskInstanceId) {
        return CommonResult.success(dppEtlTaskInstanceService.getLogByTaskInstanceId(taskInstanceId));
    }

    @RequestMapping(value = "/downloadLog", method = RequestMethod.POST)
    @Operation(summary = "下载日志文件")
    public void downloadLog(HttpServletResponse response, Long taskInstanceId, String name) {
        try {
            // 获取文件路径
            DppEtlTaskInstanceLogStatusRespDTO dto = dppEtlTaskInstanceService.getLogByTaskInstanceId(taskInstanceId);
            // 如果文件存在
            // 设置响应的内容类型为文件下载
            response.setContentType("application/octet-stream");
            // 设置下载文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + name + ".log");

            // 创建文件输入流
            try (InputStream in = new ByteArrayInputStream(dto.getLog().getBytes("UTF-8"));
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                // 将文件内容写入输出流
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

    @Operation(summary = "根据任务实例id获取数据集成任务详细信息")
    @GetMapping(value = "/getTaskInfo/{id}")
    public CommonResult<DppEtlTaskUpdateQueryRespVO> getTaskInfo(@PathVariable("id") Long id) {
        return CommonResult.success(dppEtlTaskInstanceService.getTaskInfo(id));
    }

}
