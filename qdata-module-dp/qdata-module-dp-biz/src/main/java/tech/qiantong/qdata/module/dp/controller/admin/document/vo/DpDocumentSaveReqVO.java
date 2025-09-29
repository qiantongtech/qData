package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 标准信息登记 创建/修改 Request VO DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Schema(description = "标准信息登记 Response VO")
@Data
public class DpDocumentSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "编码", example = "")
    @Size(max = 256, message = "编码长度不能超过256个字符")
    private String code;

    @Schema(description = "名称", example = "")
    @Size(max = 256, message = "名称长度不能超过256个字符")
    private String name;

    @Schema(description = "类目code", example = "")
    @Size(max = 256, message = "类目code长度不能超过256个字符")
    private String catCode;
    private String catName;

    @Schema(description = "文件标准类型字段，", example = "")
    @Size(max = 256, message = "文件标准类型字段，长度不能超过256个字符")
    private String type;

    @Schema(description = "文件状态", example = "")
    @Size(max = 256, message = "文件状态长度不能超过256个字符")
    private String status;

    @Schema(description = "发布机构名称，例如“中国国家标准化管理委员会”", example = "")
    @Size(max = 256, message = "发布机构名称，例如“中国国家标准化管理委员会”长度不能超过256个字符")
    private String issuingAgency;

    @Schema(description = "版本号", example = "")
    @Size(max = 256, message = "版本号长度不能超过256个字符")
    private String version;

    @Schema(description = "发布日期", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseDate;

    @Schema(description = "实施日期", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementationDate;

    @Schema(description = "废止日期", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abolitionDate;

    @Schema(description = "文件url", example = "")
    @Size(max = 512, message = "文件url长度不能超过256个字符")
    private String fileUrl;
    private String fileName;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


    @Schema(description = "描述", example = "")
    @Size(max = 512, message = "描述长度不能超过512个字符")
    private String description;

}
