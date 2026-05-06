package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

/**
 * 采集任务实例-日志 Request VO 对象 MC_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-12-16
 */
@Schema(description = "采集任务实例-日志 Request VO")
@Data
public class McTaskInstanceLogPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;


    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "日志内容", example = "")
    private String logContent;




}
