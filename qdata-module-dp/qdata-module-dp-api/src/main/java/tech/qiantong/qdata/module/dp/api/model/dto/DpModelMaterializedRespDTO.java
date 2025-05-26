package tech.qiantong.qdata.module.dp.api.model.dto;

import lombok.Data;

/**
 * 物化模型记录 DTO 对象 DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
public class DpModelMaterializedRespDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 模型编码 */
    private String modelName;

    /** 模型名称 */
    private String modelAlias;

    /** 模型表id */
    private Long modelId;

    /** 状态 */
    private String status;

    /** 执行日志信息 */
    private String message;

    /** 执行sql备份 */
    private String sqlCommand;

    /** 数据源id */
    private String datasourceId;

    /** 数据源类型 */
    private String datasourceType;

    /** 数据源名称 */
    private String datasourceName;

    /** 资产表id */
    private String assetId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    private Boolean delFlag;


}
