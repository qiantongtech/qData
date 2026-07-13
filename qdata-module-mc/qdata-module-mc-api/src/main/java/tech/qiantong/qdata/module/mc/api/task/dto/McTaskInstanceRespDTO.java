package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * Collection task instance DTO object MC_TASK_INSTANCE
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskInstanceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** Collection task id */
    private Long taskId;

    /** Collection mode */
    private String collectionMode;

    /** Collection range */
    private String collectionScope;

    /** Total number of collection tables */
    private Long totalCount;

    /** Number of successful collection tables */
    private Long successCount;

    /** Number of failed collection tables */
    private Long failCount;

    /** Reason for failure */
    private String failCause;

    /** New quantity */
    private Long addCount;

    /** Delete quantity */
    private Long delCount;

    /** Change quantity */
    private Long updateCount;

    /** Start time */
    private Date startTime;

    /** End time */
    private Date endTime;

    /** Time consuming */
    private Long duration;

    /** status */
    private String status;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;

    /** Description */
    private String description;


}
