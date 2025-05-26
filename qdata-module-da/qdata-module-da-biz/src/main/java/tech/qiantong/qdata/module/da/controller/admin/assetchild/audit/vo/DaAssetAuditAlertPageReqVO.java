package tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 数据资产-质量预警 Request VO 对象 DA_ASSET_AUDIT_ALERT
 *
 * @author qdata
 * @date 2025-05-09
 */
@Schema(description = "数据资产-质量预警 Request VO")
@Data
public class DaAssetAuditAlertPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "资产ID", example = "")
    private Long assetId;

    @Schema(description = "稽查批次号", example = "")
    private String batchNo;

    @Schema(description = "稽查时间", example = "")
    private Date auditTime;

    @Schema(description = "预警时间", example = "")
    private Date alertTime;

    @Schema(description = "预警信息", example = "")
    private String alertMessage;

    @Schema(description = "预警通道JSON", example = "")
    private String alertChannels;

    @Schema(description = "预警通道结果", example = "")
    private String alertChannelResult;




}
