package tech.qiantong.qdata.module.da.api.daAssetApply.dto;

import lombok.Data;

/**
 * 数据资产申请 DTO 对象 DA_ASSET_APPLY
 *
 * @author shu
 * @date 2025-03-19
 */
@Data
public class DaAssetApplyReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 项目id */
    private Long projectId;

    /** 项目编码 */
    private String projectCode;

    /** 申请理由 */
    private String applyReason;

    /** 审批理由 */
    private String approvalReason;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
