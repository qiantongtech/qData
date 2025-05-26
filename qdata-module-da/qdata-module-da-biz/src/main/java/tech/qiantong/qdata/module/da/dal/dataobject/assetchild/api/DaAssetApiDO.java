package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据资产-外部API DO 对象 DA_ASSET_API
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_API")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_API_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetApiDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产id */
    private Long assetId;

    /** API路径 */
    private String url;

    @Schema(description = "开发者", example = "")
    private String developerName;

    @Schema(description = "应用名称", example = "")
    private String appName;

    /** 请求方式 */
    private String httpMethod;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
