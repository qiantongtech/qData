package tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Sensitive Level DO entity DG_SENSITIVE_LEVEL
 *
 * @author Chaos
 * @date 2025-01-21
 */
@Data
@TableName(value = "DG_SENSITIVE_LEVEL")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DG_SENSITIVE_LEVEL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgSensitiveLevelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Sensitive Level
     */
    private String sensitiveLevel;

    /**
     * Sensitive Rule
     */
    private String sensitiveRule;

    /**
     * Start Character Position
     */
    private Long startCharLoc;

    /**
     * End Character Position
     */
    private Long endCharLoc;

    /**
     * Mask Character
     */
    private String maskCharacter;

    /**
     * Online/Offline Flag
     * 0: offline, 1: online
     */
    private String onlineFlag;

    /**
     * Description
     */
    private String description;

    /**
     * Valid Flag
     */
    private Boolean validFlag;

    /**
     * Delete Flag
     */
    @TableLogic
    private Boolean delFlag;

}
