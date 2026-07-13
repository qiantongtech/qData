package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * Metadata field information Request VO object MC_COLUMN
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "元数据字段信息 Request VO")
@Data
public class McColumnPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "采集任务id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "库id", example = "")
    private Long dbId;

    @Schema(description = "表信息id", example = "")
    private Long tableId;

    @Schema(description = "数据源id;冗余字段", example = "")
    private Long datasourceId;

    @Schema(description = "版本", example = "")
    private Integer version;

    @Schema(description = "安全等级id", example = "")
    private Long safetyLevelId;

    @Schema(description = "数据元id", example = "")
    private Long dataElemId;

    @Schema(description = "字段名称", example = "")
    private String columnName;

    @Schema(description = "字段注释", example = "")
    private String columnComment;

    @Schema(description = "字段类型", example = "")
    private String columnType;

    @Schema(description = "数据长度", example = "")
    private Integer columnLength;

    @Schema(description = "数据精度", example = "")
    private Integer columnPrecision;

    @Schema(description = "数据小数位", example = "")
    private Integer columnScale;

    @Schema(description = "数据默认值", example = "")
    private String defaultValue;

    @Schema(description = "是否主键;0:否 1:是", example = "")
    private String pkFlag;

    @Schema(description = "是否外键;0:否 1:是", example = "")
    private String fkFlag;

    @Schema(description = "是否可空;0:否 1:是", example = "")
    private String nullableFlag;

    @Schema(description = "业务定义", example = "")
    private String businessDefinition;

    @Schema(description = "度量单位", example = "")
    private String measuringUnit;

    @Schema(description = "数据质量", example = "")
    private Integer dataQuality;

    @Schema(description = "审核状态;1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    private String auditStatus;

    @Schema(description = "审核时间", example = "")
    private Date auditTime;

    @Schema(description = "状态;0：未发布，1：已发布", example = "")
    private String status;

    @Schema(hidden = true)
    private String bizScopeMode;

    @Schema(hidden = true)
    private Boolean bizScopeIncludeUnassigned;

    @Schema(description = "业务责任人", example = "")
    private Long businessLeader;

    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;



    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;



}
