package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;

/**
 * 标准信息登记 Request VO 对象 DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Schema(description = "标准信息登记 Request VO")
@Data
public class DpDocumentPageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;

    @Schema(description = "编码", example = "")
    private String code;

    @Schema(description = "名称", example = "")
    private String name;

    @Schema(description = "类目code", example = "")
    private String catCode;
    private String catName;

    @Schema(description = "文件标准类型字段，", example = "")
    private String type;

    @Schema(description = "文件状态", example = "")
    private String status;

    @Schema(description = "发布机构名称，例如“中国国家标准化管理委员会”", example = "")
    private String issuingAgency;

    @Schema(description = "版本号", example = "")
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
    private String fileUrl;
    private String fileName;

    private String keyWordParam;

    private String existStandardUrl;

    /** 描述 */
    private String description;



}
