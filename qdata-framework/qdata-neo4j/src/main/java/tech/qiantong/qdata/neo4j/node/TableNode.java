/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.neo4j.node;

import org.springframework.data.neo4j.core.schema.*;
import lombok.Builder;
import lombok.Data;
import tech.qiantong.qdata.neo4j.rel.TableToTaskRel;
import tech.qiantong.qdata.neo4j.rel.TaskToTableRel;

import java.util.List;
import java.util.Objects;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 11:32
 **/
@Node("Table")
@Data
@Builder
public class TableNode {
    @Id
    @GeneratedValue
    private Long id;

//    /**
//     * 节点编码
//     */
//    @Transient
//    private transient String nodeCode;

    /**
     * 表名
     */
    private String name;
    /**
     * 全限定表名 例如：sales_db.dwh.orders
     */
    private String tableName;
    /**
     * 数据源 id
     */
//    private Long datasourceId;
    /**
     * 数据源ip:port
     */
    private String datasourceHostPort;
    /**
     * 数据源 名称
     */
    private String datasourceName;
    /**
     * 数据源类型
     */
    private String datasourceType;
    /**
     * 数据库名，冗余字段可空
     */
    private String dbName;
    /**
     * 模式名，冗余字段可空
     */
    private String sid;


    /** orders -> task */
    @Relationship(type = "TABLE_TO_TASK", direction = Relationship.Direction.OUTGOING)
    private List<TableToTaskRel> tableToTaskRels;

    /** task -> orders */
    @Relationship(type = "TASK_TO_TABLE", direction = Relationship.Direction.INCOMING)
    private List<TaskToTableRel> taskToTableRels;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableNode tableNode = (TableNode) o;
        return Objects.equals(tableName, tableNode.tableName) && Objects.equals(datasourceHostPort, tableNode.datasourceHostPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, datasourceHostPort);
    }
}
