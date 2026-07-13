package tech.qiantong.qdata.module.mc.api.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Collection task DTO object MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** Task name */
    private String name;

    /** Data connection id */
    private Long datasourceId;

    /** Database type */
    private String dbType;

    /** Responsible person */
    private Long leader;

    /** Responsible person’s phone number */
    private String leaderPhone;

    /** Collection mode */
    private String collectionMode;

    /** Collection range */
    private String collectionScope;

    /** Task status */
    private String status;

    /** Is it valid */
    private Boolean validFlag;

    /** Delete flag */
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Collection task type: 1-Collection, 2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * Collection blacklist
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


}
