package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * 逻辑模型 DTO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long ID;

    /** 模型编码 */
    private String modelName;

    /** 模型名称 */
    private String modelComment;

    /** 类目编码 */
    private String catCode;

    /** 状态 */
    private String status;

    /** 创建方式 */
    private String createType;

    /** 数据源id */
    private Long datasourceId;

    private Long documentId;

    /** 联系人 */
    private String contact;

    /** 联系电话 */
    private String contactNumber;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
