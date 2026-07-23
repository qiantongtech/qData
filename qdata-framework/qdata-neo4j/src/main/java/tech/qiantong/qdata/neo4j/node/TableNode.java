/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Purpose:
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
// * Node encoding
//     */
//    @Transient
//    private transient String nodeCode;

    /**
     * Table name
     */
    private String name;
    /**
     * Fully qualified table name For example: sales_db.dwh.orders
     */
    private String tableName;
    /**
     * Data source id
     */
//    private Long datasourceId;
    /**
     * Data source ip:port
     */
    private String datasourceHostPort;
    /**
     * Data source name
     */
    private String datasourceName;
    /**
     * Data source type
     */
    private String datasourceType;
    /**
     * Database name, redundant fields can be empty
     */
    private String dbName;
    /**
     * Schema name, redundant fields can be empty
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
