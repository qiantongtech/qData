package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;

import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;

/**
 * Metadata field information Create/modify Request VO MC_COLUMN
 *
 * @author qdata
 * @date 2026-02-11
 */
@Slf4j
@Schema(description = "元数据字段信息 Response VO")
@Data
public class McColumnSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
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
    @Size(max = 256, message = "字段名称长度不能超过256个字符")
    private String columnName;

    @Schema(description = "字段注释", example = "")
    @Size(max = 256, message = "字段注释长度不能超过256个字符")
    private String columnComment;

    @Schema(description = "字段类型", example = "")
    @Size(max = 256, message = "字段类型长度不能超过256个字符")
    private String columnType;

    @Schema(description = "数据长度", example = "")
    private Integer columnLength;

    @Schema(description = "数据精度", example = "")
    private Integer columnPrecision;

    @Schema(description = "数据小数位", example = "")
    private Integer columnScale;

    @Schema(description = "数据默认值", example = "")
    @Size(max = 256, message = "数据默认值长度不能超过256个字符")
    private String defaultValue;

    @Schema(description = "是否主键;0:否 1:是", example = "")
    @Size(max = 256, message = "是否主键;0:否 1:是长度不能超过256个字符")
    private String pkFlag;

    @Schema(description = "是否外键;0:否 1:是", example = "")
    @Size(max = 256, message = "是否外键;0:否 1:是长度不能超过256个字符")
    private String fkFlag;

    @Schema(description = "是否可空;0:否 1:是", example = "")
    @Size(max = 256, message = "是否可空;0:否 1:是长度不能超过256个字符")
    private String nullableFlag;

    @Schema(description = "业务定义", example = "")
    @Size(max = 256, message = "业务定义长度不能超过256个字符")
    private String businessDefinition;

    @Schema(description = "度量单位", example = "")
    @Size(max = 256, message = "度量单位长度不能超过256个字符")
    private String measuringUnit;

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
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

    //Field changes
    @TableField(exist = false)
    private String updateMsg;

    //Field change type - Numeric code represents different change types: 1-Field comment change, 2-Field type change, 3-Field length change, 4-Field precision change, 5-Field decimal place change, 6-Field default value change, 7-Primary key identification change, 8-Foreign key identification change, 9-Nullable identification change
    @TableField(exist = false)
    private String updateType;

    @TableField(exist = false)
    private Long mcTableLogId;

    @Schema(description = "是否自增", example = "")
    private String autoIncrementFlag;

    @Schema(description = "是否分区字段", example = "")
    private String partitionFlag;

    //Table partition field
    @TableField(exist = false)
    private String tbPartitionKey;

    /**
     * Field specification
     */
    @Schema(description = "字段规范", example = "")
    private String columnStandard;

    /**
     * Value logic
     */
    @Schema(description = "取值逻辑", example = "")
    private String valueRule;

    @Schema(description = "负责部门", example = "")
    private Long responsibleDept;

    /** Responsible person */
    @Schema(description = "责任人", example = "")
    private Long businessLeader;

    /**
     * Is it unique?
     */
    @Schema(description = "是否唯一", example = "")
    private Long uniqueFlag;

    public void validate() {
        if (StringUtils.isNotEmpty(defaultValue)) {
            try {
                if ("float".equalsIgnoreCase(columnType) || "double".equalsIgnoreCase(columnType)) {
                    Double.parseDouble(defaultValue);
                } else if ("tinyint".equalsIgnoreCase(columnType) || "integer".equalsIgnoreCase(columnType)) {
                    Integer.parseInt(defaultValue);
                } else if ("bigint".equalsIgnoreCase(columnType)) {
                    new BigInteger(defaultValue);
                }
            } catch (NumberFormatException e) {
                log.error("默认值非法", e);
                throw new ServiceException("字段" + columnName + "的默认值[" + defaultValue + "]是非法的" + columnType + "字段类型");
            }
        }
    }

}
