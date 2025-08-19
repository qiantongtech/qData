package tech.qiantong.qdata.module.dpp.controller.admin.qa.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据质量任务 创建/修改 Request VO DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务 Response VO")
@Data
public class DppQualityTaskSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务名称", example = "")
    @Size(max = 256, message = "任务名称长度不能超过256个字符")
    private String taskName;

    @Schema(description = "类目编码", example = "")
    @Size(max = 256, message = "类目编码长度不能超过256个字符")
    private String catCode;

    @Schema(description = "联系人", example = "")
    @Size(max = 256, message = "联系人长度不能超过256个字符")
    private String contact;

    @Schema(description = "联系人ID", example = "")
    @Size(max = 256, message = "联系人ID长度不能超过256个字符")
    private String contactId;

    @Schema(description = "联系电话", example = "")
    @Size(max = 256, message = "联系电话长度不能超过256个字符")
    private String contactNumber;

    @Schema(description = "任务状态", example = "")
    @Size(max = 256, message = "任务状态长度不能超过256个字符")
    private String status;

    @Schema(description = "任务描述", example = "")
    @Size(max = 256, message = "任务描述长度不能超过256个字符")
    private String description;

    @Schema(description = "任务优先级", example = "")
    @Size(max = 256, message = "任务优先级长度不能超过256个字符")
    private String priority;

    @Schema(description = "Worker分组", example = "")
    @Size(max = 256, message = "Worker分组长度不能超过256个字符")
    private String workerGroup;

    @Schema(description = "失败重试次数", example = "")
    private Long retryTimes;

    @Schema(description = "失败重试间隔(秒)", example = "")
    private Long retryInterval;

    @Schema(description = "延时执行时间(秒)", example = "")
    private Long delayTime;

    @Schema(description = "执行策略", example = "")
    @Size(max = 256, message = "执行策略长度不能超过256个字符")
    private String strategy;

    @Schema(description = "调度周期", example = "")
    @Size(max = 256, message = "调度周期长度不能超过256个字符")
    private String cycle;


    // 数据质量任务-稽查对象
    private List<DppQualityTaskObjSaveReqVO> dppQualityTaskObjSaveReqVO;

    // 规则对象
    private List<DppQualityTaskEvaluateSaveReqVO> dppQualityTaskEvaluateSaveReqVO;



    /** 节点id */
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    /** 节点编码 */
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    /** 任务id */
    @Schema(description = "任务id", example = "")
    private Long taskId;

    /** 任务编码 */
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Schema(description = "定时任务调度表id", example = "")
    private Long systemJobId;







}
