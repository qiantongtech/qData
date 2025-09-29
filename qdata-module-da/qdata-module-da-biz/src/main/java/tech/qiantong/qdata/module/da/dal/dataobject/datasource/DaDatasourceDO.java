package tech.qiantong.qdata.module.da.dal.dataobject.datasource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
@Slf4j
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

    /** 项目集合 */
    @TableField(exist = false)
    private List<DaDatasourceProjectRelDO> projectList;

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


    @JSONField(serialize = false)
    public String toJsonString() {
        // 默认 Fastjson：忽略 null 字段，字段顺序自动
        return JSON.toJSONString(this);
    }

    @JSONField(serialize = false)
    public DbQueryProperty simplify() {
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                this.getDatasourceType(),
                this.getIp(),
                this.getPort(),
                this.getDatasourceConfig()
        );
        return dbQueryProperty;
    }

}
