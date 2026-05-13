package tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据数据库与信息及字段信息关系-日志 Response VO 对象 MC_TABLE_COLUMN_REL_LOG
 *
 * @author qdata
 * @date 2026-03-10
 */
@Schema(description = "元数据数据库与信息及字段信息关系-日志 Response VO")
@Data
public class McTableColumnRelLogRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据类型")
    @Schema(description = "数据类型;数据类型 1：预发布 2：采集，预留字段，暂时不用", example = "")
    private String dataType;

    @Excel(name = "采集任务 id")
    @Schema(description = "采集任务 id;预留字段，暂时不用", example = "")
    private Long taskId;

    @Excel(name = "库 id")
    @Schema(description = "库 id", example = "")
    private Long dbId;

    @Excel(name = "库版本")
    @Schema(description = "库版本", example = "")
    private Integer dbVersion;

    @Excel(name = "表 id")
    @Schema(description = "表 id", example = "")
    private Long tableId;

    @Excel(name = "表版本")
    @Schema(description = "表版本", example = "")
    private Integer tableVersion;

    @Excel(name = "字段 id")
    @Schema(description = "字段 id", example = "")
    private String columnId;

    @Excel(name = "字段版本")
    @Schema(description = "字段版本", example = "")
    private Integer columnVersion;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人 id")
    @Schema(description = "创建人 id", example = "")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人 id")
    @Schema(description = "更新人 id", example = "")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;

}
