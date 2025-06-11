package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑模型属性信息 Response VO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型属性信息 Response VO")
@Data
public class DpModelColumnRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "逻辑模型表ID")
    @Schema(description = "逻辑模型表ID", example = "")
    private Long modelId;

    @Excel(name = "英文名称")
    @Schema(description = "英文名称", example = "")
    private String engName;

    @Excel(name = "中文名称")
    @Schema(description = "中文名称", example = "")
    private String cnName;

    @Excel(name = "数据类型")
    @Schema(description = "数据类型", example = "")
    private String columnType;

    @Excel(name = "属性长度")
    @Schema(description = "属性长度", example = "")
    private Long columnLength;

    @Excel(name = "小数长度")
    @Schema(description = "小数长度", example = "")
    private Long columnScale;

    @Excel(name = "默认值")
    @Schema(description = "默认值", example = "")
    private String defaultValue;

    @Excel(name = "是否主键")
    @Schema(description = "是否主键", example = "")
    private String pkFlag;

    @Excel(name = "是否必填")
    @Schema(description = "是否必填", example = "")
    private String nullableFlag;

    @Excel(name = "排序")
    @Schema(description = "排序", example = "")
    private Long sortOrder;

    @Excel(name = "权威部门")
    @Schema(description = "权威部门", example = "")
    private Long authorityDept;

    @Excel(name = "数据元id")
    @Schema(description = "数据元id", example = "")
    private Long dataElemId;

    @Excel(name = "数据元名称")
    @Schema(description = "数据元名称", example = "")
    private String dataElemName;

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
