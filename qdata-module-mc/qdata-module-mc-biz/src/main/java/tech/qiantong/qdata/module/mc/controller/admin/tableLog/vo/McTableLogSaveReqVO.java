package tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;

/**
 * Metadata Information - Log Create/Modify Request VO MC_TABLE_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据信息 - 日志 Response VO")
@Data
public class McTableLogSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
    @Size(max = 256, message = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用长度不能超过256个字符")
    private String dataType;

    @Schema(description = "采集任务 id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "表 id", example = "")
    private Long tableId;

    @Schema(description = "版本", example = "")
    private String version;

    @Schema(description = "库 id", example = "")
    private Long dbId;

    @Schema(description = "数据源 id;冗余字段", example = "")
    private Long datasourceId;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表注释/表描述", example = "")
    @Size(max = 256, message = "表注释/表描述长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "安全等级 id", example = "")
    private Long safetyLevelId;

    @Schema(description = "数据库名", example = "")
    @Size(max = 256, message = "数据库名长度不能超过256个字符")
    private String dbName;

    @Schema(description = "模式名;可空", example = "")
    @Size(max = 256, message = "模式名;可空长度不能超过256个字符")
    private String schemaName;

    @Schema(description = "存储类型", example = "")
    @Size(max = 256, message = "存储类型长度不能超过256个字符")
    private String storageType;

    @Schema(description = "存储大小", example = "")
    private Integer storageSize;

    @Schema(description = "业务责任人", example = "")
    private Long businessLeader;

    @Schema(description = "业务责任人电话", example = "")
    @Size(max = 256, message = "业务责任人电话长度不能超过256个字符")
    private String businessLeaderPhone;

    @Schema(description = "技术责任人", example = "")
    private Long techLeader;

    @Schema(description = "技术责任人电话", example = "")
    @Size(max = 256, message = "技术责任人电话长度不能超过256个字符")
    private String techLeaderPhone;

    @Schema(description = "是否主表;0：否，1：是", example = "")
    @Size(max = 256, message = "是否主表;0：否，1：是长度不能超过256个字符")
    private String masterFlag;

    @Schema(description = "是否临时表;0：否，1：是", example = "")
    @Size(max = 256, message = "是否临时表;0：否，1：是长度不能超过256个字符")
    private String tempFlag;

    @Schema(description = "数据质量", example = "")
    private Integer dataQuality;

    @Schema(description = "变更类型", example = "")
    @Size(max = 256, message = "变更类型长度不能超过256个字符")
    private String updateType;

    @Schema(description = "变更说明", example = "")
    @Size(max = 256, message = "变更说明长度不能超过256个字符")
    private String updateMsg;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过256个字符")
    private String description;


}
