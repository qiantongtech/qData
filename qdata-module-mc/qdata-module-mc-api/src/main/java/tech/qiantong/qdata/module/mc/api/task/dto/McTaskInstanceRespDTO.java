package tech.qiantong.qdata.module.mc.api.task.dto;

import lombok.Data;

import java.util.Date;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskInstanceRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Implementation details. */
    private Long sourceSystemId;

    /** Implementation details. */
    private String sourceSystemName;

    /** Handle task-related data and operations. */
    private Long taskId;

    /** Implementation details. */
    private String collectionMode;

    /** Implementation details. */
    private String collectionScope;

    /** Implementation details. */
    private Long totalCount;

    /** Implementation details. */
    private Long successCount;

    /** Implementation details. */
    private Long failCount;

    /** Implementation details. */
    private String failCause;

    /** Implementation details. */
    private Long addCount;

    /** Implementation details. */
    private Long delCount;

    /** Implementation details. */
    private Long updateCount;

    /** Implementation details. */
    private Date startTime;

    /** Implementation details. */
    private Date endTime;

    /** Implementation details. */
    private Long duration;

    /** Implementation details. */
    private String status;

    /** Scheduling engine */
    private String taskScheduler;

    /** Handle Quartz scheduling operations. */
    private Long quartzId;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    private Boolean delFlag;

    /** Implementation details. */
    private String description;


}
