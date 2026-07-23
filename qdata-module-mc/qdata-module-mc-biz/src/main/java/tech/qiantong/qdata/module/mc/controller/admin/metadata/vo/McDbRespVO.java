package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Response VO object MC_DB
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "数据库 Response VO")
@Data
public class McDbRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "采集任务id")
    @Schema(description = "采集任务id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Excel(name = "来源系统ID")
    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Excel(name = "来源系统名称")
    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Excel(name = "版本")
    @Schema(description = "版本", example = "")
    private Integer version;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Excel(name = "IP")
    @Schema(description = "IP", example = "")
    private String ip;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Integer port;

    @Excel(name = "数据源配置(json字符串)")
    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Excel(name = "数据库类型")
    @Schema(description = "数据库类型", example = "")
    private String dbType;

    @Excel(name = "数据库名称")
    @Schema(description = "数据库名称", example = "")
    private String dbName;

    @Excel(name = "模式名")
    @Schema(description = "模式名;可空", example = "")
    private String schemaName;

    @Excel(name = "安全等级id")
    @Schema(description = "安全等级id", example = "")
    private Long safetyLevelId;

    @Excel(name = "所属分层")
    @Schema(description = "所属分层;1:ODS 2:DWD 3:DWS  4:ADS 5:外部系统）", example = "")
    private String belongingLayer;

    @Excel(name = "所属系统")
    @Schema(description = "所属系统", example = "")
    private String belongingSystem;

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

    @Excel(name = "存储大小")
    @Schema(description = "存储大小", example = "")
    private Integer storageSize;

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

    @Excel(name = "数据源详情")
    @Schema(description = "数据源详情", example = "")
    private DaDatasourceRespDTO datasource;

    @Schema(description = "表数量", example = "")
    private Integer tableCount;

    @Schema(description = "字段数量", example = "")
    private Integer columnCount;

    @Schema(description = "业务责任人姓名", example = "")
    private String businessLeaderName;

    @Schema(description = "技术责任人姓名", example = "")
    private String techLeaderName;

    @Schema(description = "安全等级名称", example = "")
    private String safetyLevelName;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    /**
     * Responsible department
     */
    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;
    @Schema(description = "负责部门名称", example = "")
    private String responsibleDeptName;
    /**
     * Number of data rows
     */
    @Schema(description = "数据行数", example = "")
    private Long dataRowCount;
}
