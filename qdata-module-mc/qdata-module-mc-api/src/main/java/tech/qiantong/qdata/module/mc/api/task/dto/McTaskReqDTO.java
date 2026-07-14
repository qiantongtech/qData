package tech.qiantong.qdata.module.mc.api.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Handle task-related data and operations.
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Implementation details. */
    private Long sourceSystemId;

    /** Implementation details. */
    private String sourceSystemName;

    /** Handle task-related data and operations. */
    private String name;

    /** Implementation details. */
    private Long datasourceId;

    /** Handle database and data source configuration. */
    private String dbType;

    /** Implementation details. */
    private Long leader;

    /** Implementation details. */
    private String leaderPhone;

    /** Implementation details. */
    private String collectionMode;

    /** Implementation details. */
    private String collectionScope;

    /** Handle task-related data and operations. */
    private String status;

    /** Scheduler */
    private String scheduler;

    /** Whether the record is valid. */
    private Boolean validFlag;

    /** Delete the related record. */
    private Boolean delFlag;

    /** Implementation details. */
    private String description;

    /**
     * Handle task-related data and operations.
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Implementation details.
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


}
