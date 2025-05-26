package tech.qiantong.qdata.module.da.dal.dataobject.datasource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据源与项目关联关系 DO 对象 DA_DATASOURCE_PROJECT_REL
 *
 * @author qdata
 * @date 2025-03-13
 */
@Data
@TableName(value = "DA_DATASOURCE_PROJECT_REL")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_DATASOURCE_PROJECT_REL_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDatasourceProjectRelDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目id */
    private Long projectId;

    /** 项目名称 */
    @TableField(exist = false)
    private String projectName;

    /** 项目编码 */
    private String projectCode;

    /** 数据源id */
    private Long datasourceId;

    /** 数据源名称 */
    @TableField(exist = false)
    private String datasourceName;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    /** 是否分配到数据研发 */
    private Boolean dppAssigned;
}
