package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源与项目关联关系 Response VO 对象 DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Schema(description = "数据源与项目关联关系 Response VO")
@Data
public class DaDatasourceProjectRelRespVO implements Serializable {

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

    @Excel(name = "是否分配到数据研发")
    @Schema(description = "是否分配到数据研发", example = "")
    private Boolean dppAssigned;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

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
