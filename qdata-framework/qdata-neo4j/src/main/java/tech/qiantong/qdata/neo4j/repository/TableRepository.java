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

package tech.qiantong.qdata.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.qiantong.qdata.neo4j.dto.LineageDTO;
import tech.qiantong.qdata.neo4j.node.TableNode;

import java.util.Optional;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 13:44
 **/
@Repository
public interface TableRepository extends Neo4jRepository<TableNode, Long> {
//    Optional<TableNode> findByTableNameAndDatasourceId(String tableName,Long datasourceId);

    Optional<TableNode> findByTableNameAndDatasourceHostPort(String tableName,String datasourceHostPort);

    @Query(value = "MATCH (currentTable:Table {datasourceHostPort: $datasourceHostPort,tableName: $tableName})  " +
            "  " +
            "OPTIONAL MATCH (currentTable)<-[r1:TASK_TO_TABLE]-(sourceTask:Task)  " +
            "OPTIONAL MATCH (sourceTask)<-[r2:TABLE_TO_TASK]-(sourceTable:Table)  " +
            "  " +
            "OPTIONAL MATCH (currentTable)-[r3:TABLE_TO_TASK]->(targetTask:Task)  " +
            "OPTIONAL MATCH (targetTask)-[r4:TASK_TO_TABLE]->(targetTable:Table)  " +
            "RETURN  " +
            "  currentTable,  " +
            "  collect(DISTINCT sourceTask) + collect(DISTINCT targetTask) AS tasks,  " +
            "  [currentTable] + collect(DISTINCT sourceTable) + collect(DISTINCT targetTable) AS tables")
    Optional<LineageDTO> findLineage(@Param("datasourceHostPort") String datasourceHostPort, @Param("tableName") String tableName);
}
