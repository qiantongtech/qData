package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 元数据信息 创建/修改 Request VO MC_TABLE
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "元数据信息 Response VO")
@Data
public class McTableSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "采集任务id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "库id", example = "")
    private Long dbId;

    @Schema(description = "数据源id;冗余字段", example = "")
    private Long datasourceId;

    @Schema(description = "版本", example = "")
    private Integer version;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表注释/表描述", example = "")
    @Size(max = 256, message = "表注释/表描述长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "安全等级id", example = "")
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

    @Schema(description = "审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    @Size(max = 256, message = "审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常长度不能超过256个字符")
    private String auditStatus;

    @Schema(description = "审核时间", example = "")
    private Date auditTime;

    @Schema(description = "状态;0：未发布，1：已发布", example = "")
    @Size(max = 256, message = "状态;0：未发布，1：已发布长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 512, message = "备注长度不能超过256个字符")
    private String remark;

    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过256个字符")
    private String description;

    /**
     * 是否在门户展示：0-不展示，1-展示
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;


    //表格变更内容
    @TableField(exist = false)
    private String updateMsg;

    //表格变更类型 1-注释变更 2-字段变更
    @TableField(exist = false)
    private String updateType;

    @TableField(exist = false)
    private Long columnCount;

    /** 表的行数 */
    @Schema(description = "行数", example = "")
    private Long rowCount;

    /** 表的索引信息 */
    @Schema(description = "索引", example = "")
    private String tbIndex;

    /** 表的分区字段信息 */
    @Schema(description = "分区字段", example = "")
    private String partitionKey;
    /**
     * 存储引擎
     */
    @Schema(description = "存储引擎", example = "")
    private String storageEngine;

    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;

    /**
     * 主键字段
     */
    @Schema(description = "主键字段", example = "")
    private Long primaryKey;

    /**
     * 表创建时间
     */
    @Schema(description = "表创建时间", example = "")
    private Date tbCreateTime;

    /**
     * 数据更新时间
     */
    @Schema(description = "数据更新时间", example = "")
    private Date dataUpdateTime;
}
