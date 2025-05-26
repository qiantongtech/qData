package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据资产-外部API-参数 DO 对象 DA_ASSET_API_PARAM
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_API_PARAM")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_API_PARAM_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetApiParamDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** API id */
    private Long apiId;

    /** 父级id */
    private Long parentId;

    /** 参数名称 */
    private String name;

    /** 参数类型 */
    private String type;

    /** 是否必填 */
    private String requestFlag;

    /** 字段类型 */
    private String columnType;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    @Schema(description = "数据默认值", example = "")
    private String defaultValue;
    @Schema(description = "示例值", example = "")
    private String exampleValue;
    @Schema(description = "描述", example = "")
    private String description;


}
