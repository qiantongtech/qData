package tech.qiantong.qdata.module.ds.controller.admin.apply.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * API服务-申请 Response VO 对象 DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Schema(description = "API服务-申请 Response VO")
@Data
public class DsApiApplyRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "API id")
    @Schema(description = "API id", example = "")
    private String apiId;

    @Excel(name = "申请人")
    @Schema(description = "申请人", example = "")
    private String applyBy;

    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "申请时间", example = "")
    private Date applyTime;

    @Excel(name = "申请理由")
    @Schema(description = "申请理由", example = "")
    private String applyReason;

    @Excel(name = "审批人")
    @Schema(description = "审批人", example = "")
    private String approverBy;

    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审批时间", example = "")
    private Date approvalTime;

    @Excel(name = "审批理由")
    @Schema(description = "审批理由", example = "")
    private String approvalReason;

    @Excel(name = "有效期类型")
    @Schema(description = "有效期类型", example = "")
    private String validType;

    @Excel(name = "时效开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "时效开始时间", example = "")
    private Date validStartTime;

    @Excel(name = "时效结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "时效结束时间", example = "")
    private Date validEndTime;

    @Excel(name = "流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常")
    @Schema(description = "流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常", example = "")
    private String status;

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
    private String REMARK;


    private String apiName;
    //    @TableField(exist = false)
//    private String apiId;
    private String apiUrl;
    /** 区域id */
    private String rpAreaDictId;
}
