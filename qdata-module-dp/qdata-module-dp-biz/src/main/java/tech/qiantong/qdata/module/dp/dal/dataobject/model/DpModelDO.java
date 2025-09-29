package tech.qiantong.qdata.module.dp.dal.dataobject.model;

import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 逻辑模型 DO 对象 DP_MODEL
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_MODEL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_MODEL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpModelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 模型编码 */
    private String modelName;

    /** 模型名称 */
    private String modelComment;

    /** 类目编码 */
    private String catCode;

    @TableField(exist = false)
    private String catName;

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
    @TableLogic
    private Boolean delFlag;

    @TableField(exist = false)
    private long columnCount;

    /** 数据源名称 */
    @TableField(exist = false)
    private String datasourceName;

    /** 数据源类型 */
    @TableField(exist = false)
    private String datasourceType;

    /** 数据源配置(json字符串) */
    @TableField(exist = false)
    private String datasourceConfig;

    /** IP */
    @TableField(exist = false)
    private String ip;

    /** 端口号 */
    @TableField(exist = false)
    private Long port;

    /** 名称 */
    @TableField(exist = false)
    private String documentName;

    /** 名称 */
    @TableField(exist = false)
    private String documentCode;

    /** 文件标准类型字段， */
    @TableField(exist = false)
    private String documentType;
}
