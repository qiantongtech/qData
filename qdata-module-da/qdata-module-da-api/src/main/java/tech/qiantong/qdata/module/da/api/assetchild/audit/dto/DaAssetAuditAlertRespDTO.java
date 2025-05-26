package tech.qiantong.qdata.module.da.api.assetchild.audit.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据资产-质量预警 DTO 对象 DA_ASSET_AUDIT_ALERT
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
public class DaAssetAuditAlertRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 稽查批次号 */
    private String batchNo;

    /** 稽查时间 */
    private Date auditTime;

    /** 预警时间 */
    private Date alertTime;

    /** 预警信息 */
    private String alertMessage;

    /** 预警通道JSON */
    private String alertChannels;

    /** 预警通道结果 */
    private String alertChannelResult;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
