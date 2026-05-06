package tech.qiantong.qdata.module.mc.api.tableLog.dto;

import lombok.Data;

/**
 * 元数据信息 - 日志 DTO 对象 MC_TABLE_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Data
public class McTableLogRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用 */
    private String dataType;

    /** 采集任务 id;预留字段，暂时不用 */
    private Long taskId;

    /** 表 id */
    private Long tableId;

     /** 版本 */
    private String version;

    /** 库 id */
    private Long dbId;

    /** 数据源 id;冗余字段 */
    private Long datasourceId;

    /** 表名称（表英文名称） */
    private String tableName;

    /** 表注释/表描述（表中文名称） */
    private String tableComment;

    /** 安全等级 id */
    private Long safetyLevelId;

    /** 数据库名 */
    private String dbName;

    /** 模式名;可空 */
    private String schemaName;

    /** 存储类型 */
    private String storageType;

    /** 存储大小 */
    private Integer storageSize;

    /** 业务责任人 */
    private Long businessLeader;

    /** 业务责任人电话 */
    private String businessLeaderPhone;

    /** 技术责任人 */
    private Long techLeader;

    /** 技术责任人电话 */
    private String techLeaderPhone;

    /** 是否主表;0：否，1：是 */
    private String masterFlag;

    /** 是否临时表;0：否，1：是 */
    private String tempFlag;

    /** 数据质量 */
    private Integer dataQuality;

    /** 变更类型 */
    private String updateType;

    /** 变更说明 */
    private String updateMsg;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    private Boolean delFlag;

    /** 描述 */
    private String description;


}
