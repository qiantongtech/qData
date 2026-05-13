package tech.qiantong.qdata.module.mc.controller.admin.task;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;
import tech.qiantong.qdata.module.mc.service.task.IMcTaskService;

import javax.annotation.Resource;

/**
 * 采集任务执行器Controller
 * 用于DolphinScheduler回调执行采集任务
 *
 * @author qdata
 * @date 2026-05-11
 */
@Tag(name = "采集任务执行器")
@RestController
@RequestMapping("/mc/taskExecutor")
@Validated
public class McTaskExecutorController extends BaseController {

    @Resource
    private IMcTaskService mcTaskService;

    /**
     * DolphinScheduler回调执行采集任务
     *
     * @param id 任务ID
     * @return 执行结果
     */
    @PutMapping("/runExecuteTask/{id}")
    public CommonResult<String> runExecuteTask(@PathVariable("id") Long id) {
        try {
            mcTaskService.runDaDiscoveryTask(id);
            return CommonResult.success("任务id:" + id + "执行成功");
        } catch (NumberFormatException e) {
            return CommonResult.error( GlobalErrorCodeConstants.ERROR.getCode(),"任务ID格式错误：" + id);
        } catch (Exception e) {
            return CommonResult.error( GlobalErrorCodeConstants.ERROR.getCode(),"任务执行失败：" + e.getMessage());
        }
    }
}
