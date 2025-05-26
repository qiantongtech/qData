package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据元数据资产关联信息 Response VO 对象 DP_DATA_ELEM_ASSET_REL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元数据资产关联信息 Response VO")
@Data
public class DpDataElemAssetRelRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据元类型")
    @Schema(description = "数据元类型", example = "")
    private String dataElemType;

    @Excel(name = "数据元id")
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Excel(name = "资产id(数据表id)")
    @Schema(description = "资产id(数据表id)", example = "")
    private String assetId;

    @Excel(name = "数据表")
    @Schema(description = "数据表", example = "")
    private String tableName;

    @Excel(name = "关联字段id")
    @Schema(description = "关联字段id", example = "")
    private String columnId;

    @Excel(name = "关联字段")
    @Schema(description = "关联字段", example = "")
    private String columnName;

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

    /** 资产名称 */
    @TableField(exist = false)
    private String assetName;

    /** 表描述 */
    @TableField(exist = false)
    private String tableComment;

    /** 资产描述 */
    @TableField(exist = false)
    private String description;
}
