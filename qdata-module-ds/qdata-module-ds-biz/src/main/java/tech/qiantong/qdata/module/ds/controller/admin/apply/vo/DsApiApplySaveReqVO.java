package tech.qiantong.qdata.module.ds.controller.admin.apply.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * API服务-申请 创建/修改 Request VO DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Schema(description = "API服务-申请 Response VO")
@Data
public class DsApiApplySaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "API id", example = "")
    @Size(max = 256, message = "API id长度不能超过256个字符")
    private String apiId;

    @Schema(description = "申请人", example = "")
    @Size(max = 256, message = "申请人长度不能超过256个字符")
    private String applyBy;

    @Schema(description = "申请人部门id", example = "")
    private Long applyByDept;

    @Schema(description = "申请时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @Schema(description = "申请理由", example = "")
    @Size(max = 256, message = "申请理由长度不能超过256个字符")
    private String applyReason;

    @Schema(description = "审批人", example = "")
    @Size(max = 256, message = "审批人长度不能超过256个字符")
    private String approverBy;

    @Schema(description = "审批时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    @Schema(description = "审批理由", example = "")
    @Size(max = 256, message = "审批理由长度不能超过256个字符")
    private String approvalReason;

    @Schema(description = "有效期类型", example = "")
    @Size(max = 256, message = "有效期类型长度不能超过256个字符")
    private String validType;

    @Schema(description = "时效开始时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validStartTime;

    @Schema(description = "时效结束时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validEndTime;

    @Schema(description = "流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String REMARK;
    /** 区域id */
    private String rpAreaDictId;

}
