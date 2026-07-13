package tech.qiantong.qdata.module.mc.dal.dataobject.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Business domain DO object MC_DOMAIN
 *
 * @author qdata
 * @date 2026-02-12
 */
@Data
@TableName(value = "MC_DOMAIN")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("MC_DOMAIN_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class McDomainDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Business name */
    private String name;

    /** Associated superior ID */
    private Long parentId;

    /** Category sorting */
    private Integer sortOrder;

    /** Hierarchical encoding */
    private String code;

    /** Whether it is valid; 0: invalid, 1: valid */
    private Boolean validFlag;

    /** Deletion flag; 1: deleted, 0: not deleted */
    @TableLogic
    private Boolean delFlag;

    /** Description */
    private String description;


}
