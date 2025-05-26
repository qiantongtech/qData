package tech.qiantong.qdata.module.ds.api.apply.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * API服务-申请 DTO 对象 DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Data
public class DsApiApplyRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    private List<Long> idList;

    /** API id */
    private String apiId;

    /** 申请人 */
    private String applyBy;

    /** 申请人手机号码 */
    private String applyByPhone;

    /** 申请人部门 */
    private String applyByDeptName;

    /** 申请人部门集合 */
    private List<Long> applyByDeptIdList;

    /** 申请时间 */
    private Date applyTime;

    /** 申请理由 */
    private String applyReason;

    /** 审批人 */
    private String approverBy;

    /** 审批时间 */
    private Date approvalTime;

    /** 审批理由 */
    private String approvalReason;

    /** 有效期类型 */
    private String validType;

    /** 时效开始时间 */
    private Date validStartTime;

    /** 时效结束时间 */
    private Date validEndTime;

    /** 流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    private String apiName;

    private String apiUrl;

    /** 流程业务实例id */
    private String processInstanceId;


    private Long creatorId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** 区域id */
    private String rpAreaDictId;

    /** 区域id */
    private List<Long> rpAreaDictIdList;

    /** 区域id */
    private String rpAreaDictName;

    private Object dsApi;
}
