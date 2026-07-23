package tech.qiantong.qdata.module.dg.dal.dataobject.standard;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * Data Element Category Management DO entity DG_DATA_ELEM_CAT
 *
 * @author qdata
 * @date 2025-01-20
 */
@Data
@TableName(value = "DG_DATA_ELEM_CAT")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Can be omitted for MySQL and similar databases.
// @KeySequence("DG_DATA_ELEM_CAT_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DgDataElemCatDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Category Name
     */
    private String name;

    /**
     * Parent ID
     */
    private Long parentId;

    /**
     * Category Sort Order
     */
    private Long sortOrder;

    /**
     * Description
     */
    private String description;

    /**
     * Level Code
     */
    private String code;

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
