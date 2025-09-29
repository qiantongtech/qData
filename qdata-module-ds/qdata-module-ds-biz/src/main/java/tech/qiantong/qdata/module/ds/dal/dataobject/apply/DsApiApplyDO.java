package tech.qiantong.qdata.module.ds.dal.dataobject.apply;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * API服务-申请 DO 对象 DS_API_APPLY
 *
 * @author qdata
 * @date 2025-04-22
 */
@Data
@TableName(value = "DS_API_APPLY")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DS_API_APPLY_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DsApiApplyDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** API id */
    private String apiId;

    /** 申请人 */
    private String applyBy;

    /** 申请人手机号码 */
    @TableField(exist = false)
    private String applyByPhone;

    /** 申请人部门 */
    @TableField(exist = false)
    private String applyByDeptName;

    /** 申请人部门集合 */
    @TableField(exist = false)
    private List<String> applyByDeptIdList;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 申请理由 */
    private String applyReason;

    /** 审批人 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String approverBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;

    /** 审批理由 */
    private String approvalReason;

    /** 有效期类型 */
    private String validType;

    /** 时效开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validStartTime;

    /** 时效结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validEndTime;

    /** 流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


    @TableField(exist = false)
    private String apiName;
//    @TableField(exist = false)
//    private String apiId;
    @TableField(exist = false)
    private String apiUrl;

    /** 流程业务实例id */
    @TableField(exist = false)
    private String processInstanceId;

    /** 区域id */
    private String rpAreaDictId;

    /** 区域名称 */
    @TableField(exist = false)
    private String rpAreaDictName;
}
