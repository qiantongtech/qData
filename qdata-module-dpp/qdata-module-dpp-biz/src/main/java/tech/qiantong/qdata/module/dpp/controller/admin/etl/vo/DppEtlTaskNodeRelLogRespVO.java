package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成任务节点关系-日志 Response VO 对象 DPP_ETL_TASK_NODE_REL_LOG
 *
 * @author qdata
 * @date 2025-02-13
 */
@Schema(description = "数据集成任务节点关系-日志 Response VO")
@Data
public class DppEtlTaskNodeRelLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "项目id")
    @Schema(description = "项目id", example = "")
    private Long projectId;

    @Excel(name = "项目编码")
    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Excel(name = "任务编码")
    @Schema(description = "任务编码", example = "")
    private String taskCode;

    @Excel(name = "任务版本")
    @Schema(description = "任务版本", example = "")
    private Long taskVersion;

    @Excel(name = "前节点id")
    @Schema(description = "前节点id", example = "")
    private Long preNodeId;

    @Excel(name = "前节点编码")
    @Schema(description = "前节点编码", example = "")
    private String preNodeCode;

    @Excel(name = "前节点版本")
    @Schema(description = "前节点版本", example = "")
    private Long preNodeVersion;

    @Excel(name = "后节点id")
    @Schema(description = "后节点id", example = "")
    private Long postNodeId;

    @Excel(name = "后节点编码")
    @Schema(description = "后节点编码", example = "")
    private String postNodeCode;

    @Excel(name = "后节点版本")
    @Schema(description = "后节点版本", example = "")
    private Long postNodeVersion;

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
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
