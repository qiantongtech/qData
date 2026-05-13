package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据信息 Response VO 对象 MC_TABLE
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "元数据信息 Response VO")
@Data
public class McTableRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "采集任务id")
    @Schema(description = "采集任务id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Excel(name = "库id")
    @Schema(description = "库id", example = "")
    private Long dbId;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id;冗余字段", example = "")
    private Long datasourceId;

    @Excel(name = "版本")
    @Schema(description = "版本", example = "")
    private Integer version;

    @Excel(name = "表名称", readConverterExp = "表=英文名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

    @Excel(name = "表注释/表描述", readConverterExp = "表=中文名称")
    @Schema(description = "表注释/表描述", example = "")
    private String tableComment;

    @Excel(name = "安全等级id")
    @Schema(description = "安全等级id", example = "")
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

    @Excel(name = "审核状态")
    @Schema(description = "审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    private String auditStatus;

    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间", example = "")
    private Date auditTime;

    @Excel(name = "状态")
    @Schema(description = "状态;0：未发布，1：已发布", example = "")
    private String status;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
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

    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Schema(description = "业务责任人姓名", example = "")
    private String businessLeaderName;

    @Schema(description = "技术责任人姓名", example = "")
    private String techLeaderName;

    @Schema(description = "安全等级名称", example = "")
    private String safetyLevelName;


    private McDbRespVO dbRespVO;

    @TableField(exist = false)
    private Long columnCount;

    /** cron表达式 */
    @TableField(exist = false)
    private String cronExpression;

    /**
     * 是否在门户展示：0-不展示，1-展示
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

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
    /**
     * 负责部门
     */
    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;

    /**
     * 主键字段
     */
    @Schema(description = "主键字段", example = "")
    private String primaryKey;

    /**
     * 表创建时间
     */
    @Excel(name = "表创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "表创建时间", example = "")
    private Date tbCreateTime;

    /**
     * 数据更新时间
     */
    @Excel(name = "数据更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "数据更新时间", example = "")
    private Date dataUpdateTime;

    /**
     * 是否已经关联资产
     */
    @Schema(description = "是否已经关联资产", example = "")
    private Boolean dssetFlag;

    /**
     * 创建人电话
     */
    private String createPhoneNumber;

    /**
     * 更新人电话
     */
    private String updatePhoneNumber;

    /**
     * 数据源名称
     */
    @TableField(exist = false)
    private String datasourceName;


    /**
     * 数据源类型
     */
    @TableField(exist = false)
    private String datasourceType;
}
