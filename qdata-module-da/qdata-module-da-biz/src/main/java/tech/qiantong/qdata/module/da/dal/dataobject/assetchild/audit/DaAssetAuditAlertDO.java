package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据资产-质量预警 DO 对象 DA_ASSET_AUDIT_ALERT
 *
 * @author qdata
 * @date 2025-05-09
 */
@Data
@TableName(value = "DA_ASSET_AUDIT_ALERT")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_AUDIT_ALERT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetAuditAlertDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Boolean delFlag;


}
