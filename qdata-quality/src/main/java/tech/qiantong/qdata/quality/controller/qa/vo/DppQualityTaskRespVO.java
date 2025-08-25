package tech.qiantong.qdata.quality.controller.qa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据质量任务 Response VO 对象 DPP_QUALITY_TASK
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务 Response VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DppQualityTaskRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "任务名称")
    @Schema(description = "任务名称", example = "")
    private String taskName;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;

    @Excel(name = "联系人")
    @Schema(description = "联系人", example = "")
    private String contact;

    @Excel(name = "联系人ID")
    @Schema(description = "联系人ID", example = "")
    private String contactId;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "任务状态")
    @Schema(description = "任务状态", example = "")
    private String status;

    @Excel(name = "任务描述")
    @Schema(description = "任务描述", example = "")
    private String description;

    @Excel(name = "任务优先级")
    @Schema(description = "任务优先级", example = "")
    private String priority;

    @Excel(name = "Worker分组")
    @Schema(description = "Worker分组", example = "")
    private String workerGroup;

    @Excel(name = "失败重试次数")
    @Schema(description = "失败重试次数", example = "")
    private Long retryTimes;

    @Excel(name = "失败重试间隔(秒)")
    @Schema(description = "失败重试间隔(秒)", example = "")
    private Long retryInterval;

    @Excel(name = "延时执行时间(秒)")
    @Schema(description = "延时执行时间(秒)", example = "")
    private Long delayTime;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志", example = "")
    private Boolean delFlag;

    @Excel(name = "执行策略")
    @Schema(description = "执行策略", example = "")
    private String strategy;

    @Excel(name = "调度周期")
    @Schema(description = "调度周期", example = "")
    private String cycle;

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

    @Schema(description = "是否是资产质量任务;0：否。1是")
    private String assetFlag;

    @Schema(description = "资产id")
    private Long assetId;


    // 数据质量任务-稽查对象
    private List<DppQualityTaskObjRespVO> dppQualityTaskObjSaveReqVO;

    // 规则对象
    private List<DppQualityTaskEvaluateRespVO> dppQualityTaskEvaluateRespVOS;
    @Excel(name = "稽查对象数")
    @Schema(description = "稽查对象数", example = "")
    private Integer taskObjNum;

    @Excel(name = "稽查规则数")
    @Schema(description = "稽查规则数", example = "")
    private Integer taskEvaluateNum;

    @Excel(name = "最后执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后执行时间", example = "")
    private Date lastExecuteTime;

}
