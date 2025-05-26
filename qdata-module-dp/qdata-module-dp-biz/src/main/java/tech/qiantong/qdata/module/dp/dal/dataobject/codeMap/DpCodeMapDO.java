package tech.qiantong.qdata.module.dp.dal.dataobject.codeMap;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据元代码映射 DO 对象 DP_CODE_MAP
 *
 * @author qdata
 * @date 2025-01-21
 */
@Data
@TableName(value = "DP_CODE_MAP")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DP_CODE_MAP_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DpCodeMapDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据元id */
    private String dataElemId;

    /** 原始值 */
    private String originalValue;

    /** 代码名 */
    private String codeName;

    /** 代码值 */
    private String codeValue;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
