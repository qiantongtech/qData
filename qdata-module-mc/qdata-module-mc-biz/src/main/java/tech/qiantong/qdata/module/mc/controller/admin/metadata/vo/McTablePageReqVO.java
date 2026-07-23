package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;
import java.util.List;

/**
 * Metadata information Request VO object MC_TABLE
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "元数据信息 Request VO")
@Data
public class McTablePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
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
    private String tableName;

    @Schema(description = "表注释/表描述", example = "")
    private String tableComment;

    @Schema(description = "安全等级id", example = "")
    private Long safetyLevelId;

    @Schema(description = "数据库名", example = "")
    private String dbName;

    @Schema(description = "模式名;可空", example = "")
    private String schemaName;

    @Schema(description = "存储类型", example = "")
    private String storageType;

    @Schema(description = "存储大小", example = "")
    private Integer storageSize;

    @Schema(description = "业务责任人", example = "")
    private Long businessLeader;

    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;

    @Schema(description = "业务责任人电话", example = "")
    private String businessLeaderPhone;

    @Schema(description = "技术责任人", example = "")
    private Long techLeader;

    @Schema(description = "技术责任人电话", example = "")
    private String techLeaderPhone;

    @Schema(description = "是否主表;0：否，1：是", example = "")
    private String masterFlag;

    @Schema(description = "是否临时表;0：否，1：是", example = "")
    private String tempFlag;

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


    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "来源系统id", example = "")
    private String sourceSystemId;

    /**
     * Library collection filter
     */
    private List<Long> dbIdList;


    /**
     * Fuzzy query for names or comments
     */
    private String keyWord;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;


    /**
     * Data not displayed, multiple comma splicing
     */
    @Schema(description = "不展示的数据，多个一逗号拼接", example = "0")
    private String hideTableIds;

}
