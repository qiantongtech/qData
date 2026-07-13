package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * Collection task instance-log DTO object MC_TASK_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskInstanceLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** Task instance id */
    private Long taskInstanceId;

    /** time */
    private Date time;

    /** task id */
    private Long taskId;

    /** Log content */
    private String logContent;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;


}
