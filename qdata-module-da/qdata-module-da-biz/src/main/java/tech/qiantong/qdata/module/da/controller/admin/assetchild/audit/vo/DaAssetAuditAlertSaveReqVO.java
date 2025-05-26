package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 数据资产-质量预警 创建/修改 Request VO DA_ASSET_AUDIT_ALERT
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产-质量预警 Response VO")
@Data
public class DaAssetAuditAlertSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "稽查批次号", example = "")
    @Size(max = 256, message = "稽查批次号长度不能超过256个字符")
    private String batchNo;

    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Schema(description = "预警时间", example = "")
    private Date alertTime;

    @Schema(description = "预警信息", example = "")
    @Size(max = 256, message = "预警信息长度不能超过256个字符")
    private String alertMessage;

    @Schema(description = "预警通道JSON", example = "")
    @Size(max = 256, message = "预警通道JSON长度不能超过256个字符")
    private String alertChannels;

    @Schema(description = "预警通道结果", example = "")
    @Size(max = 256, message = "预警通道结果长度不能超过256个字符")
    private String alertChannelResult;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
