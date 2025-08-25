package tech.qiantong.qdata.quality.dal.dataobject.datasource;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;


/**
 * 数据源 DO 对象 DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_DATASOURCE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_DATASOURCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDatasourceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据源名称 */
    private String datasourceName;

    /** 数据源类型 */
    private String datasourceType;

    /** 数据源配置(json字符串) */
    private String datasourceConfig;

    /** 项目名称 */
    @TableField(exist = false)
    private String projectName;

    /** 是否是管理员分配给数据研发 */
    @TableField(exist = false)
    private Boolean isAdminAddTo;

    /** IP */
    private String ip;

    /** 端口号 */
    private Long port;

    /** 数据库表数（预留） */
    private Long listCount;

    /** 同步记录数（预留） */
    private Long syncCount;

    /** 同步数据量大小（预留） */
    private Long dataSize;

    /** 描述 */
    private String description;

    /** 是否有效 */
    private Boolean validFlag;

    @TableLogic
    private Boolean delFlag;


}
