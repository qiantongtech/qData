package tech.qiantong.qdata.module.dpp.controller.admin.etl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceService;

import javax.annotation.Resource;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:39
 **/
@Tag(name = "调度执行")
@RestController
@RequestMapping("/dpp/dppEtlExecutors")
public class DppEtlExecutorController {

    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;

    @Operation(summary = "执行命令")
    @PostMapping("/execute/{taskInstanceId}/{executeType}")
    public AjaxResult execute(@PathVariable("taskInstanceId") Long taskInstanceId, @PathVariable("executeType") ExecuteType executeType) {
        return dppEtlTaskInstanceService.execute(taskInstanceId, executeType);
    }

}
