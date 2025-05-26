package tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 数据资产字段 Response VO 对象 DA_ASSET_COLUMN
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据资产字段 Response VO")
@Data
public class DaAssetColumnRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "资产id")
    @Schema(description = "资产id", example = "")
    private String assetId;

    @Excel(name = "字段名称/英文名称")
    @Schema(description = "字段名称/英文名称", example = "")
    private String columnName;

    @Excel(name = "字段注释/中文名称")
    @Schema(description = "字段注释/中文名称", example = "")
    private String columnComment;

    @Excel(name = "数据类型")
    @Schema(description = "数据类型", example = "")
    private String columnType;

    @Excel(name = "长度")
    @Schema(description = "长度", example = "")
    private Long columnLength;

    @Excel(name = "小数位")
    @Schema(description = "小数位", example = "")
    private Long columnScale;

    @Excel(name = "是否必填")
    @Schema(description = "是否必填", example = "")
    private String nullableFlag;

    @Excel(name = "是否主键")
    @Schema(description = "是否主键", example = "")
    private String pkFlag;

    @Excel(name = "默认值")
    @Schema(description = "默认值", example = "")
    private String defaultValue;

    @Excel(name = "是否代码")
    @Schema(description = "是否代码", example = "")
    private String dataElemCodeFlag;

    @Excel(name = "代码id")
    @Schema(description = "代码id", example = "")
    private String dataElemCodeId;

    @Excel(name = "代码表名称")
    @Schema(description = "代码表名称", example = "")
    @TableField(exist = false)
    private String dataElemCodeName;


    @Excel(name = "敏感等级id")
    @Schema(description = "敏感等级id", example = "")
    private String sensitiveLevelId;

    @Excel(name = "敏感等级名称")
    @Schema(description = "敏感等级名称", example = "")
    @TableField(exist = false)
    private String sensitiveLevelName;


    @Excel(name = "关联数据元")
    @Schema(description = "关联数据元", example = "")
    private String relDataElmeFlag;

    @Excel(name = "关联数据元名称，多个逗号隔开")
    @Schema(description = "关联数据元名称，多个逗号隔开", example = "")
    @TableField(exist = false)
    private String relDataElmeName;


    @Excel(name = "关联清洗规则")
    @Schema(description = "关联清洗规则", example = "")
    private String relCleanFlag;

    @Excel(name = "关联稽查规则")
    @Schema(description = "关联稽查规则", example = "")
    private String relAuditFlag;

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
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @TableField(exist = false)
    private Set<Long> elementId;


}
