package tech.qiantong.qdata.module.mc.dal.dataobject.metadata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;

import java.util.Date;

/**
 * Database DO object MC_DB
 *
 * @author qdata
 * @date 2026-02-11
 */
@Data
@TableName(value = "MC_DB")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_DB_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McDbDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Collection task id; reserved field, not used temporarily */
    private Long taskId;

    /** Source system ID */
    private Long sourceSystemId;

    /** Source system name */
    private String sourceSystemName;

    /** version */
    private Integer version;

    /** data source id */
    private Long datasourceId;

    /** IP */
    private String ip;

    /** Port number */
    private Integer port;

    /** Data source configuration (json string) */
    private String datasourceConfig;

    /** Database type */
    private String dbType;

    /** Database name */
    private String dbName;

    /** Pattern name; can be empty */
    private String schemaName;

    /** Security level id */
    private Long safetyLevelId;

    /** The layer to which it belongs;1:ODS 2:DWD 3:DWS 4:ADS 5:External system) */
    private String belongingLayer;

    /** Belonging system */
    private String belongingSystem;

    /** Business Responsible Person */
    private Long businessLeader;

    /** Phone number of business person in charge */
    private String businessLeaderPhone;

    /** Technical Responsible Person */
    private Long techLeader;

    /** Telephone number of technical person in charge */
    private String techLeaderPhone;

    /** Storage size */
    private Integer storageSize;

    /** Data quality */
    private Integer dataQuality;

    /** Review status; 1: Approval in progress, 2: Approval passed, 3: Approval rejected, 4: Approval withdrawn, 5: Approval abnormal */
    private String auditStatus;

    /** Review time */
    private Date auditTime;

    /** Status; 0: not released, 1: released */
    private String status;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    @TableField(exist = false)
    private DaDatasourceRespDTO datasource;

    @TableField(exist = false)
    private Integer tableCount;

    /**
     * Number of fields
     */
    @TableField(exist = false)
    private Integer columnCount;

    /**
     * Name of person responsible for business
     */
    @TableField(exist = false)
    private String businessLeaderName;

    /**
     * Name of technical person in charge
     */
    @TableField(exist = false)
    private String techLeaderName;

    /**
     * Security level name
     */
    @TableField(exist = false)
    private String safetyLevelName;

    /**
     * Responsible department
     */
    private Long responsibleDept;

    /**
     * Responsible department name
     */
    @TableField(exist = false)
    private String responsibleDeptName;

    /**
     * Number of data rows
     */
    private Long dataRowCount;

}
