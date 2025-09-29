package tech.qiantong.qdata.module.dp.api.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 标准信息登记 DTO 对象 DP_DOCUMENT
 *
 * @author qdata
 * @date 2025-08-21
 */
@Data
public class DpDocumentReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 类目code */
    private String catCode;
    private String catName;

    /** 文件标准类型字段， */
    private String type;

    /** 文件状态 */
    private String status;

    /** 发布机构名称，例如“中国国家标准化管理委员会” */
    private String issuingAgency;

    /** 版本号 */
    private String version;

    /** 发布日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date releaseDate;

    /** 实施日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date implementationDate;

    /** 废止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abolitionDate;

    /** 文件url */

    private String fileUrl;
    private String fileName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;

    /** 描述 */
    private String description;



}
