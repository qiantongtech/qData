package tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据资产-主题关联关系 DO 对象 DA_ASSET_THEME_REL
 *
 * @author qdata
 * @date 2025-04-14
 */
@Data
@TableName(value = "DA_ASSET_THEME_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_ASSET_THEME_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaAssetThemeRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 资产id */
    private Long assetId;

    /** 主题id */
    private Long themeId;

    @TableField(exist = false)
    private String themeName;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
