package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线单数据 Response VO 对象 DPP_ONL_DESFORM_DATA
 *
 * @author qdata
 * @date 2025-04-09
 */
@Schema(description = "在线单数据 Response VO")
@Data
public class DppOnlDesformDataRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "ID", example = "")
    private Long id;

    @Excel(name = "表单编码")
    @Schema(description = "表单编码", example = "")
    private String desformCode;

    @Excel(name = "表单名称")
    @Schema(description = "表单名称", example = "")
    private String desformName;

    @Excel(name = "表单ID")
    @Schema(description = "表单ID", example = "")
    private String desformId;

    @Excel(name = "表单数据")
    @Schema(description = "表单数据", example = "")
    private String desformData;

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
