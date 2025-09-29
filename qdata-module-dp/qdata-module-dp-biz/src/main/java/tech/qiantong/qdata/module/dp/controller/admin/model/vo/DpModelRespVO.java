package tech.qiantong.qdata.module.dp.controller.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import tech.qiantong.qdata.common.annotation.Excel;
import java.util.Date;
import java.io.Serializable;

/**
 * 逻辑模型 Response VO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "逻辑模型 Response VO")
@Data
public class DpModelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "模型编码")
    @Schema(description = "模型编码", example = "")
    private String modelName;

    @Excel(name = "模型名称")
    @Schema(description = "模型名称", example = "")
    private String modelComment;

    @Excel(name = "类目编码")
    @Schema(description = "类目编码", example = "")
    private String catCode;
    private String catName;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private String status;

    @Excel(name = "创建方式")
    @Schema(description = "创建方式", example = "")
    private String createType;

    @Excel(name = "数据源id")
    @Schema(description = "数据源id", example = "")
    private Long datasourceId;

    private Long documentId;

    @Excel(name = "联系人")
    @Schema(description = "联系人", example = "")
    private String contact;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

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

    @Excel(name = "数据源名称")
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Excel(name = "数据源类型")
    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Excel(name = "数据源配置(json字符串)")
    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Excel(name = "IP")
    @Schema(description = "IP", example = "")
    private String ip;

    @Excel(name = "端口号")
    @Schema(description = "端口号", example = "")
    private Long port;

    /** 名称 */
    @TableField(exist = false)
    private String documentName;

    /** 名称 */
    @TableField(exist = false)
    private String documentCode;

    /** 文件标准类型字段， */
    @TableField(exist = false)
    private String documentType;
}
