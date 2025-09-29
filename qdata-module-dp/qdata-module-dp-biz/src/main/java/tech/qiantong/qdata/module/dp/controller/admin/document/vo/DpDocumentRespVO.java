package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 标准信息登记 Response VO 对象 DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Schema(description = "标准信息登记 Response VO")
@Data
public class DpDocumentRespVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Excel(name = "ID")
    @Schema(description = "id", example = "")
    private Long id;

    @Excel(name = "编码")
    @Schema(description = "编码", example = "")
    private String code;

    @Excel(name = "名称")
    @Schema(description = "名称", example = "")
    private String name;

    @Excel(name = "类目code")
    @Schema(description = "类目code", example = "")
    private String catCode;
    @TableField(exist = false)
    private String catName;

    @Excel(name = "文件标准类型字段，")
    @Schema(description = "文件标准类型字段，", example = "")
    private String type;

    @Excel(name = "文件状态")
    @Schema(description = "文件状态", example = "")
    private String status;

    @Excel(name = "发布机构名称，例如“中国国家标准化管理委员会”")
    @Schema(description = "发布机构名称，例如“中国国家标准化管理委员会”", example = "")
    private String issuingAgency;

    @Excel(name = "版本号")
    @Schema(description = "版本号", example = "")
    private String version;

    @Excel(name = "发布日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布日期", example = "")
    private Date releaseDate;

    @Excel(name = "实施日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "实施日期", example = "")
    private Date implementationDate;

    @Excel(name = "废止日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "废止日期", example = "")
    private Date abolitionDate;

    @Excel(name = "文件url")
    @Schema(description = "文件url", example = "")
    private String fileUrl;
    private String fileName;

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

    @Excel(name = "描述")
    @Schema(description = "描述", example = "")
    private String description;
}
