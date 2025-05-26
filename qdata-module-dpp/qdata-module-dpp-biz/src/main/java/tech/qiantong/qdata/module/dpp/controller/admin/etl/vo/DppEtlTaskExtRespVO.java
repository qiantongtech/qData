package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成任务-扩展数据 Response VO 对象 DPP_ETL_TASK_EXT
 *
 * @author qdata
 * @date 2025-04-16
 */
@Schema(description = "数据集成任务-扩展数据 Response VO")
@Data
public class DppEtlTaskExtRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据汇聚任务id")
    @Schema(description = "数据汇聚任务id", example = "")
    private Long taskId;

    @Excel(name = "数据汇聚节点id")
    @Schema(description = "数据汇聚节点id", example = "")
    private Long etlNodeId;

    @Excel(name = "数据汇聚节点名称")
    @Schema(description = "数据汇聚节点名称", example = "")
    private String etlNodeName;

    @Excel(name = "数据汇聚节点编码")
    @Schema(description = "数据汇聚节点编码", example = "")
    private String etlNodeCode;

    @Excel(name = "数据汇聚节点版本")
    @Schema(description = "数据汇聚节点版本", example = "")
    private Long etlNodeVersion;

    @Excel(name = "数据汇聚节点关系id")
    @Schema(description = "数据汇聚节点关系id", example = "")
    private Long etlRelationId;

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
    private String remark;

}
