package tech.qiantong.qdata.module.dpp.dal.dataobject.qa;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据质量任务 DO 对象 DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Data
@TableName(value = "DPP_QUALITY_TASK")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DPP_QUALITY_TASK_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DppQualityTaskDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务名称 */
    private String taskName;

    /** 类目编码 */
    private String catCode;

    /** 联系人 */
    private String contact;

    /** 联系人ID */
    private String contactId;

    /** 联系电话 */
    private String contactNumber;

    /** 任务状态 0:下线，1:上线*/
    private String status;

    /** 任务描述 */
    private String description;

    /** 任务优先级 */
    private String priority;

    /** Worker分组 */
    private String workerGroup;

    /** 失败重试次数 */
    private Long retryTimes;

    /** 失败重试间隔(秒) */
    private Long retryInterval;

    /** 延时执行时间(秒) */
    private Long delayTime;

    /** 执行策略 */
    private String strategy;

    /** 调度周期 */
    private String cycle;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private Integer taskObjNum;

    @TableField(exist = false)
    private Integer taskEvaluateNum;

    @TableField(exist = false)
    private String catName;

   /*定时任务调度表id*/
    private Long systemJobId;

    /** 节点id */
    private Long nodeId;

    /** 节点编码 */
    private String nodeCode;

    /** 任务id */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;


    @Schema(description = "是否是资产质量任务;0：否。1是")
    private String assetFlag;

    @Schema(description = "资产id")
    private Long assetId;
}
