package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据集成节点实例-日志 Response VO 对象 DPP_ETL_NODE_INSTANCE_LOG
 *
 * @author qdata
 * @date 2025-08-05
 */
@Schema(description = "数据集成节点实例-日志 Response VO")
@Data
public class DppEtlNodeInstanceLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID节点实例id")
    @Schema(description = "ID节点实例id", example = "")
    private Long nodeInstanceId;

    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "时间", example = "")
    private Date tm;

    @Excel(name = "任务类型")
    @Schema(description = "任务类型", example = "")
    private String taskType;

    @Excel(name = "节点id")
    @Schema(description = "节点id", example = "")
    private Long nodeId;

    @Excel(name = "节点编码")
    @Schema(description = "节点编码", example = "")
    private String nodeCode;

    @Excel(name = "任务实例id")
    @Schema(description = "任务实例id", example = "")
    private Long taskInstanceId;

    @Excel(name = "日志内容")
    @Schema(description = "日志内容", example = "")
    private String logContent;

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
