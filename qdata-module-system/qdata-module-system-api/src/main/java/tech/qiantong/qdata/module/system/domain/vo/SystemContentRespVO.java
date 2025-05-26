package tech.qiantong.qdata.module.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统配置 Response VO 对象 system_content
 *
 * @author qdata
 * @date 2024-12-31
 */
@Schema(description = "系统配置 Response VO")
@Data
public class SystemContentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    @Schema(description = "ID")
    private Long id;

    @Excel(name = "系统名称")
    @Schema(description = "系统名称", example = "")
    private String sysName;

    @Excel(name = "loginLogo")
    @Schema(description = "loginLogo", example = "")
    private String loginLogo;

    @Excel(name = "logo")
    @Schema(description = "logo", example = "")
    private String logo;

    @Excel(name = "轮播图")
    @Schema(description = "轮播图", example = "")
    private String carouselImage;

    @Excel(name = "联系电话")
    @Schema(description = "联系电话", example = "")
    private String contactNumber;

    @Excel(name = "电子邮箱")
    @Schema(description = "电子邮箱", example = "")
    private String email;

    @Excel(name = "版权方")
    @Schema(description = "版权方", example = "")
    private String copyright;

    @Excel(name = "备案号")
    @Schema(description = "备案号", example = "")
    private String recordNumber;

    @Excel(name = "删除标记")
    @Schema(description = "删除标记", example = "")
    private Boolean delFlag;

    @Excel(name = "状态")
    @Schema(description = "状态", example = "")
    private Integer status;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "修改人")
    @Schema(description = "修改人", example = "")
    private String updateBy;

    @Excel(name = "修改人id")
    @Schema(description = "修改人id", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remarks;

}
