package tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据信息 - 日志 Response VO 对象 MC_TABLE_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据信息 - 日志 Response VO")
@Data
public class McTableLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据类型")
    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
    private String dataType;

    @Excel(name = "采集任务 id")
    @Schema(description = "采集任务 id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Excel(name = "表 id")
    @Schema(description = "表 id", example = "")
    private Long tableId;

    @Excel(name = "版本")
    @Schema(description = "版本", example = "")
    private String version;

    @Excel(name = "库 id")
    @Schema(description = "库 id", example = "")
    private Long dbId;

    @Excel(name = "数据源 id")
    @Schema(description = "数据源 id;冗余字段", example = "")
    private Long datasourceId;

    @Excel(name = "表名称", readConverterExp = "表=英文名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "表注释/表描述", readConverterExp = "表=中文名称")
    @Schema(description = "表注释/表描述", example = "")
    private String tableComment;

    @Excel(name = "安全等级 id")
    @Schema(description = "安全等级 id", example = "")
    private Long safetyLevelId;

    @Excel(name = "数据库名")
    @Schema(description = "数据库名", example = "")
    private String dbName;

    @Excel(name = "模式名")
    @Schema(description = "模式名;可空", example = "")
    private String schemaName;

    @Excel(name = "存储类型")
    @Schema(description = "存储类型", example = "")
    private String storageType;

    @Excel(name = "存储大小")
    @Schema(description = "存储大小", example = "")
    private Integer storageSize;

    @Excel(name = "业务责任人")
    @Schema(description = "业务责任人", example = "")
    private Long businessLeader;

    @Excel(name = "业务责任人电话")
    @Schema(description = "业务责任人电话", example = "")
    private String businessLeaderPhone;

    @Excel(name = "技术责任人")
    @Schema(description = "技术责任人", example = "")
    private Long techLeader;

    @Excel(name = "技术责任人电话")
    @Schema(description = "技术责任人电话", example = "")
    private String techLeaderPhone;

    @Excel(name = "是否主表")
    @Schema(description = "是否主表;0：否，1：是", example = "")
    private String masterFlag;

    @Excel(name = "是否临时表")
    @Schema(description = "是否临时表;0：否，1：是", example = "")
    private String tempFlag;

    @Excel(name = "数据质量")
    @Schema(description = "数据质量", example = "")
    private Integer dataQuality;

    @Excel(name = "变更类型")
    @Schema(description = "变更类型", example = "")
    private String updateType;

    @Excel(name = "变更说明")
    @Schema(description = "变更说明", example = "")
    private String updateMsg;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人 id")
    @Schema(description = "创建人 id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人 id")
    @Schema(description = "更新人 id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

}
