package tech.qiantong.qdata.quality.controller.qa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据质量任务-稽查对象 Response VO 对象 DPP_QUALITY_TASK_OBJ
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Schema(description = "数据质量任务-稽查对象 Response VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DppQualityTaskObjRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据质量任务ID")
    @Schema(description = "数据质量任务ID", example = "")
    private Long taskId;

    @Excel(name = "稽查对象名称")
    @Schema(description = "稽查对象名称", example = "")
    private String name;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Excel(name = "表名称")
    @Schema(description = "表名称", example = "")
    private String tableName;

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


    // 规则对象
    private DppQualityTaskEvaluateRespVO dppQualityTaskEvaluateSaveReqVO;

}
