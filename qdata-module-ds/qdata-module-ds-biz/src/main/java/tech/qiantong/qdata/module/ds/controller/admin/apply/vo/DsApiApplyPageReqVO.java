package tech.qiantong.qdata.module.ds.controller.admin.apply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;
import java.util.List;

/**
 * API服务-申请 Request VO 对象 DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Schema(description = "API服务-申请 Request VO")
@Data
public class DsApiApplyPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "API id", example = "")
    private String apiId;

    @Schema(description = "申请人", example = "")
    private String applyBy;

    @Schema(description = "申请时间", example = "")
    private Date applyTime;

    @Schema(description = "申请理由", example = "")
    private String applyReason;

    @Schema(description = "审批人", example = "")
    private String approverBy;

    @Schema(description = "审批时间", example = "")
    private Date approvalTime;

    @Schema(description = "审批理由", example = "")
    private String approvalReason;

    @Schema(description = "有效期类型", example = "")
    private String validType;

    @Schema(description = "时效开始时间", example = "")
    private Date validStartTime;

    @Schema(description = "时效结束时间", example = "")
    private Date validEndTime;

    @Schema(description = "流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    private String status;

    private String name;
    List<Long> idList;
    /** 区域id */
    private String rpAreaDictId;


}
