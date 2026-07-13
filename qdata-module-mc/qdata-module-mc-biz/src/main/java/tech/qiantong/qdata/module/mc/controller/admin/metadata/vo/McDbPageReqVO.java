package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;
import java.util.List;

/**
 * Database Request VO object MC_DB
 *
 * @author qdata
 * @date 2026-02-11
 */
@Schema(description = "数据库 Request VO")
@Data
public class McDbPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "采集任务id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Schema(description = "来源系统ID", example = "")
    private Long sourceSystemId;

    @Schema(description = "来源系统名称", example = "")
    private String sourceSystemName;

    @Schema(description = "版本", example = "")
    private Integer version;

    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Schema(description = "IP", example = "")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Integer port;

    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Schema(description = "数据库类型", example = "")
    private String dbType;

    @Schema(description = "数据库名称", example = "")
    private String dbName;

    @Schema(description = "模式名;可空", example = "")
    private String schemaName;

    @Schema(description = "安全等级id", example = "")
    private Long safetyLevelId;

    @Schema(description = "所属分层;1:ODS 2:DWD 3:DWS  4:ADS 5:外部系统）", example = "")
    private String belongingLayer;

    @Schema(description = "所属系统", example = "")
    private String belongingSystem;

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

    @Schema(description = "存储大小", example = "")
    private Integer storageSize;

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

    @Schema(description = "表数量", example = "")
    private Integer tableCount;

    @Schema(description = "字段数量", example = "")
    private Integer columnCount;

    @Schema(description = "业务责任人姓名", example = "")
    private String businessLeaderName;

    @Schema(description = "技术责任人姓名", example = "")
    private String techLeaderName;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    /**
     * Multiple selection of data sources, query library
     */
    private List<Long> datasourceIdList;


}
