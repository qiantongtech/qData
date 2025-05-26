package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * 逻辑模型属性信息 DTO 对象 DP_MODEL_COLUMN
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelColumnRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 逻辑模型表ID */
    private Long modelId;

    /** 英文名称 */
    private String engName;

    /** 中文名称 */
    private String cnName;

    /** 数据类型 */
    private String columnType;

    /** 属性长度 */
    private Long columnLength;

    /** 小数长度 */
    private Long columnScale;

    /** 默认值 */
    private String defaultValue;

    /** 是否主键 */
    private String pkFlag;

    /** 是否必填 */
    private String nullableFlag;

    /** 排序 */
    private Long sortOrder;

    /** 权威部门 */
    private String authorityDept;

    /** 数据元id */
    private Long dataElemId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
