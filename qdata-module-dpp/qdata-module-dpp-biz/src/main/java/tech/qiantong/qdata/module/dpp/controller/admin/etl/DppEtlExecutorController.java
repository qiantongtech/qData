package tech.qiantong.qdata.module.dpp.controller.admin.etl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlExecutorService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;
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
@RequestMapping("/dpp/etlExecutors")
public class DppEtlExecutorController {

    @Resource
    private IDppEtlTaskInstanceService dppEtlTaskInstanceService;

    @Operation(summary = "执行命令")
    @PostMapping("/execute/{taskInstanceId}/{executeType}")
    public AjaxResult execute(@PathVariable("taskInstanceId") Long taskInstanceId, @PathVariable("executeType") ExecuteType executeType) {
        return dppEtlTaskInstanceService.execute(taskInstanceId, executeType);
    }

}
