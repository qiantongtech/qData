package tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据元代码 Response VO 对象 DP_DATA_ELEM_CODE
 *
 * @author qdata
 * @date 2025-01-21
 */
@Schema(description = "数据元代码 Response VO")
@Data
public class DpDataElemCodeRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "数据元id")
    @Schema(description = "数据元id", example = "")
    private String dataElemId;

    @Excel(name = "代码值")
    @Schema(description = "代码值", example = "")
    private String codeValue;

    @Excel(name = "代码名称")
    @Schema(description = "代码名称", example = "")
    private String codeName;

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
    @Schema(description = "创建时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

}
