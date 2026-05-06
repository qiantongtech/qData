package tech.qiantong.qdata.module.mc.api.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 采集任务 DTO 对象 MC_TASK
 *
 * @author qdata
 * @date 2025-12-16
 */
@Data
public class McTaskReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 来源系统ID */
    private Long sourceSystemId;

    /** 来源系统名称 */
    private String sourceSystemName;

    /** 任务名称 */
    private String name;

    /** 数据连接id */
    private Long datasourceId;

    /** 数据库类型 */
    private String dbType;

    /** 责任人 */
    private Long leader;

    /** 责任人电话 */
    private String leaderPhone;

    /** 采集模式 */
    private String collectionMode;

    /** 采集范围 */
    private String collectionScope;

    /** 任务状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 描述 */
    private String description;

    /**
     * 采集任务类型：1-采集，2-DDL
     */
    @Schema(description = "采集任务类型：1-采集，2-DDL", example = "1")
    private String collectType;

    /**
     * 采集黑名单
     */
    @Schema(description = "采集黑名单", example = "")
    private String blacklist;


}
