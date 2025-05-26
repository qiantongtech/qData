package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产稽查调度 Response VO 对象 DA_ASSET_AUDIT_SCHEDULE
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "资产稽查调度 Response VO")
@Data
public class DaAssetAuditScheduleRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产ID")
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Excel(name = "是否开启稽查调度", readConverterExp = "0=否1是")
    @Schema(description = "是否开启稽查调度", example = "")
    private String scheduleFlag;

    @Excel(name = "cron执行表达式")
    @Schema(description = "cron执行表达式", example = "")
    private String cronExpression;

    @Excel(name = "节点id")
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Excel(name = "节点编码")
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Excel(name = "任务编码")
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Excel(name = "定时任务调度表id")
    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
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

}
