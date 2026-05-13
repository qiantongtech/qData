package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * 采集任务实例-日志 DTO 对象 MC_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskInstanceLogReqDTO {

    private static final long serialVersionUID = 1L;

    /** 任务实例id */
    private Long taskInstanceId;

    /** 时间 */
    private Date time;

    /** 任务id */
    private Long taskId;

    /** 日志内容 */
    private String logContent;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
