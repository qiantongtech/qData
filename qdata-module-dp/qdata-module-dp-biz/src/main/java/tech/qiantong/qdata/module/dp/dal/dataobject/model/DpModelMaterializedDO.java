package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 物化模型记录 DO 对象 DP_MODEL_MATERIALIZED
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL_MATERIALIZED")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_MODEL_MATERIALIZED_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelMaterializedDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 模型编码 */
    private String modelName;

    /** 模型名称 */
    private String modelAlias;

    /** 模型表id */
    private Long modelId;

    /** 状态
     * 1未创建，2创建中，3成功，4失败，5已存在。
     *
     * */
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
    private Long assetId;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    /** 字段数量 */
    private Long fieldCount;
}
