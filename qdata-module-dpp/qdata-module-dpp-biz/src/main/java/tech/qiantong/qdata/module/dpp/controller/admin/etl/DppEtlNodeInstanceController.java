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
 * 数据集成节点实例Controller
 *
 * @author qdata
 * @date 2025-02-13
 */
@Tag(name = "数据集成节点实例")
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
    @Log(title = "数据集成节点实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DppEtlNodeInstancePageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DppEtlNodeInstanceDO> list = (List<DppEtlNodeInstanceDO>) dppEtlNodeInstanceService.getDppEtlNodeInstancePage(exportReqVO).getRows();
        ExcelUtil<DppEtlNodeInstanceRespVO> util = new ExcelUtil<>(DppEtlNodeInstanceRespVO.class);
        util.exportExcel(response, DppEtlNodeInstanceConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入数据集成节点实例列表")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:import')")
    @Log(title = "数据集成节点实例", businessType = BusinessType.IMPORT)
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
    @Log(title = "数据集成节点实例", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody DppEtlNodeInstanceSaveReqVO dppEtlNodeInstance) {
        dppEtlNodeInstance.setCreatorId(getUserId());
        dppEtlNodeInstance.setCreateBy(getNickName());
        dppEtlNodeInstance.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeInstanceService.createDppEtlNodeInstance(dppEtlNodeInstance));
    }

    @Operation(summary = "修改数据集成节点实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:edit')")
    @Log(title = "数据集成节点实例", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody DppEtlNodeInstanceSaveReqVO dppEtlNodeInstance) {
        dppEtlNodeInstance.setUpdatorId(getUserId());
        dppEtlNodeInstance.setUpdateBy(getNickName());
        dppEtlNodeInstance.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(dppEtlNodeInstanceService.updateDppEtlNodeInstance(dppEtlNodeInstance));
    }

    @Operation(summary = "删除数据集成节点实例")
//    @PreAuthorize("@ss.hasPermi('dpp:etlNodeInstance:remove')")
    @Log(title = "数据集成节点实例", businessType = BusinessType.DELETE)
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
            //获取表中的日志
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
            // 获取日志
            String log = dppEtlNodeInstanceService.getLogByNodeInstanceId(nodeInstanceId);
            // 如果文件存在
            // 设置响应的内容类型为文件下载
            response.setContentType("application/octet-stream");
            // 设置下载文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + name + ".log");

            // 创建文件输入流
            try (InputStream in = new ByteArrayInputStream(log.getBytes("UTF-8"));
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

}
