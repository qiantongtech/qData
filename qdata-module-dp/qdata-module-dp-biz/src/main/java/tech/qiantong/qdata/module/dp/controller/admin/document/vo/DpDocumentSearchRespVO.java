package tech.qiantong.qdata.module.dp.controller.admin.document.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <P>
 * 用途:标准检索响应VO
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-22 10:08
 **/
@Data
public class DpDocumentSearchRespVO implements Serializable {

    private static final long serialVersionUID = -4634002019134354679L;

    /**
     * 数据类型 1:标准，2:逻辑模型，3:数据元，4:代码表
     */
    private String dataType;

    /**
     * ID
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 标准名称
     */
    private String name;

    /**
     * 类目名称（分类名称）
     */
    private String catName;

    /**
     * 文件标准类型字段，;1-国家标准，2-行业标准，3-地方标准，4-团体标准 字典：dp_document_type
     */
    private String type;

    /**
     * 文件状态（标准状态），字典：	dp_document_status
     */
    private String status;

    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    /**
     * 实施日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date implementationDate;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;
}
