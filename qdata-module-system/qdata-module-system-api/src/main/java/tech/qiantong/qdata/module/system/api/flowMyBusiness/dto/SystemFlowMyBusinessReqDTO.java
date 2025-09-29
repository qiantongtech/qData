package tech.qiantong.qdata.module.system.api.flowMyBusiness.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 审批记录 DTO 对象 SYSTEM_FLOW_MY_BUSINESS
 *
 * @author qdata
 * @date 2025-04-25
 */
@Data
public class SystemFlowMyBusinessReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 流程定义key */
    private String processDefinitionKey;

    /** 流程定义id */
    private String processDefinitionId;

    /** 流程业务实例id */
    private String processInstanceId;

    /** 流程业务简要描述 */
    private String title;

    /** 业务表id */
    private Long dataId;

    /** 流程类型，0：服务资源，1：数据维护 */
    private String type;

    /** 业务类名，用来获取spring容器里的服务对象 */
    private String serviceImplName;

    /** 申请人 */
    private String proposer;

    /** 流程状态，0：审批中，1：审批中，2：审批通过，3：审批拒绝，4：审批撤回，5：审批异常 */
    private String actStatus;

    /** 当前节点在实例中的id */
    private String taskId;

    /** 当前节点名称 */
    private String taskName;

    /** 当前节点在流程定义中的id */
    private String taskNameId;

    /** 当前节点可以处理的用户名，String[]的json串 */
    private String todoUsers;

    /** 处理过的人，String[]的json串 */
    private String doneUsers;

    /** 当前节点优先级 */
    private String prority;

    /** 流程变量，是一个JsonObj类型 */
    private String flowValues;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    private Long userId;

    private List<String> processInstanceIdList;

    private List<String> processInstanceBizCodeList;

    private String apiName;

    /**
     * 页码
     */
    private Integer pageCopyNum;
    /**
     * 每页的数量
     */
    private Integer pageCopySize;

    private Long creatorId;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    // 创建时间自动填充
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
