package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据字段信息 Response VO 对象 MC_COLUMN
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "元数据字段信息 Response VO")
@Data
public class McColumnRespVO implements Serializable {

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

    @Excel(name = "表信息id")
    @Schema(description = "表信息id", example = "")
    private Long tableId;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id;冗余字段", example = "")
    private Long datasourceId;

    @Excel(name = "版本")
    @Schema(description = "版本", example = "")
    private Integer version;

    @Excel(name = "安全等级id")
    @Schema(description = "安全等级id", example = "")
    private Long safetyLevelId;

    @Excel(name = "数据元id")
    @Schema(description = "数据元id", example = "")
    private Long dataElemId;

    @Excel(name = "字段名称")
    @Schema(description = "字段名称", example = "")
    private String columnName;

    @Excel(name = "字段注释")
    @Schema(description = "字段注释", example = "")
    private String columnComment;

    @Excel(name = "字段类型")
    @Schema(description = "字段类型", example = "")
    private String columnType;

    @Excel(name = "数据长度")
    @Schema(description = "数据长度", example = "")
    private Integer columnLength;

    @Excel(name = "数据精度")
    @Schema(description = "数据精度", example = "")
    private Integer columnPrecision;

    @Excel(name = "数据小数位")
    @Schema(description = "数据小数位", example = "")
    private Integer columnScale;

    @Excel(name = "数据默认值")
    @Schema(description = "数据默认值", example = "")
    private String defaultValue;

    @Excel(name = "是否主键")
    @Schema(description = "是否主键;0:否 1:是", example = "")
    private String pkFlag;

    @Excel(name = "是否外键")
    @Schema(description = "是否外键;0:否 1:是", example = "")
    private String fkFlag;

    @Excel(name = "是否可空")
    @Schema(description = "是否可空;0:否 1:是", example = "")
    private String nullableFlag;

    @Excel(name = "业务定义")
    @Schema(description = "业务定义", example = "")
    private String businessDefinition;

    @Excel(name = "度量单位")
    @Schema(description = "度量单位", example = "")
    private String measuringUnit;

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

    private McTableRespVO tableRespVO;

    @Schema(description = "来源系统ID", example = "")
    @TableField(exist = false)
    private Long sourceSystemId;

    @TableField(exist = false)
    private String sourceSystemName;

    @Schema(description = "数据元名称", example = "")
    private String dataElemName;

    @Schema(description = "数据库名", example = "")
    private String dbName;

    @Schema(description = "表名称", example = "")
    private String tableName;

    @Schema(description = "安全等级名称", example = "")
    private String safetyLevelName;

    /**
     * 是否在门户展示：0-不展示，1-展示
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    @Schema(description = "是否自增", example = "")
    private String autoIncrementFlag;

    @Schema(description = "是否分区字段", example = "")
    private String partitionFlag;
    /**
     * 负责部门
     */
    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;

    /** 责任人 */
    @Schema(description = "责任人", example = "")
    private Long businessLeader;

    /**
     * 字段规范
     */
    @Schema(description = "字段规范", example = "")
    private String columnStandard;

    /**
     * 取值逻辑
     */
    @Schema(description = "取值逻辑", example = "")
    private String valueRule;

    /**
     * 是否唯一
     */
    @Schema(description = "是否唯一", example = "")
    private Long uniqueFlag;
}
