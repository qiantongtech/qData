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

package tech.qiantong.qdata.neo4j.service;

import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Relationship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.neo4j.config.Neo4jProperties;
import tech.qiantong.qdata.neo4j.dto.LineageDTO;
import tech.qiantong.qdata.neo4j.node.TableNode;
import tech.qiantong.qdata.neo4j.node.TaskNode;
import tech.qiantong.qdata.neo4j.rel.TableToTaskRel;
import tech.qiantong.qdata.neo4j.rel.TaskToTableRel;
import tech.qiantong.qdata.neo4j.repository.TableRepository;
import tech.qiantong.qdata.neo4j.repository.TaskRepository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 13:46
 **/
@Service
public class LineageDataService {

    @Resource
    private TableRepository tableRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private Neo4jProperties neo4jProperties;


    @Transactional("neo4jTransactionManager")
    public LineageDTO lineage(String datasourceHostPort, String tableName) {
        LineageDTO lineageDto = tableRepository.findLineage(datasourceHostPort, tableName)
                .orElse(null);
        if (lineageDto == null) {
            return new LineageDTO();
        }

        //Query relational data
        getRels(lineageDto, datasourceHostPort, tableName);
        return lineageDto;
    }

    /**
     * Query relational data
     *
     * @param lineageDto
     * @param datasourceHostPort
     * @param tableName
     */
    void getRels(LineageDTO lineageDto, String datasourceHostPort, String tableName) {
        // Create driver
        Driver driver = GraphDatabase.driver(neo4jProperties.getUri(),
                AuthTokens.basic(neo4jProperties.getUsername(), neo4jProperties.getPassword()));

        // Open an automatically closed session
        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            // 3. Execute Cypher and return a record
            Result result = session.run(
                    "MATCH (currentTable:Table {datasourceHostPort: $datasourceHostPort,tableName: $tableName})   " +
                            "OPTIONAL MATCH (currentTable)<-[r1:TASK_TO_TABLE]-(sourceTask:Task)   " +
                            "OPTIONAL MATCH (sourceTask)<-[r2:TABLE_TO_TASK]-(sourceTable:Table)   " +
                            "OPTIONAL MATCH (currentTable)-[r3:TABLE_TO_TASK]->(targetTask:Task)   " +
                            "OPTIONAL MATCH (targetTask)-[r4:TASK_TO_TABLE]->(targetTable:Table)   " +
                            "RETURN     " +
                            "collect(DISTINCT sourceTask) + collect(DISTINCT targetTask) AS tasks,   " +
                            "currentTable + collect(DISTINCT sourceTable) + collect(DISTINCT targetTable)  AS tables,   " +
                            "collect(DISTINCT r1) + collect(DISTINCT r2) +collect(DISTINCT r3) + collect(DISTINCT r4) AS  rels ",
                    Values.parameters("datasourceHostPort", datasourceHostPort, "tableName", tableName)
            );
            if (result.hasNext()) {
                Record row = result.single();
                List<Relationship> rels = row.get("rels").asList(v -> v.asRelationship());
                List<JSONObject> relsList = new ArrayList<>();
                for (Relationship rel : rels) {
                    JSONObject relObj = new JSONObject();
                    relObj.put("startNodeId", rel.startNodeId());
                    relObj.put("endNodeId", rel.endNodeId());
                    relObj.put("type", rel.type());
                    JSONObject properties = new JSONObject();
                    properties.put("taskId", rel.get("taskId").asLong());
                    properties.put("taskCode", rel.get("taskCode").asString());
                    properties.put("datasourceHostPort", rel.get("datasourceHostPort").asString());
                    properties.put("tableName", rel.get("tableName").asString());
                    relObj.put("properties", properties);
                    relsList.add(relObj);
                }
                lineageDto.setRels(relsList);
            }
        } finally {
            if (driver != null) {
                driver.close();
            }
        }
    }

    /**
     * Delete task
     *
     * @param taskId
     */
    @Transactional("neo4jTransactionManager")
    public void deleteTask(Long taskId) {
        TaskNode oldTaskNode = taskRepository.findByTaskId(taskId);
        if (oldTaskNode != null) {
            taskRepository.delete(oldTaskNode);
        }
    }

    /**
     * Save
     *
     * @param readerTableNodeList
     * @param writerTableNodeList
     * @param taskNode
     */
    @Transactional("neo4jTransactionManager")
    public void save(List<TableNode> readerTableNodeList, List<TableNode> writerTableNodeList, TaskNode taskNode) {
        TaskNode oldTaskNode = taskRepository.findByTaskId(taskNode.getTaskId());
        if (oldTaskNode != null) {
            taskRepository.delete(oldTaskNode);
        }

        for (TableNode writerTableNode : writerTableNodeList) {
            TableNode oldWriterTableNode = tableRepository.findByTableNameAndDatasourceHostPort(writerTableNode.getTableName(), writerTableNode.getDatasourceHostPort())
                    .orElse(null);
            if (oldWriterTableNode != null) {
                writerTableNode.setId(oldWriterTableNode.getId());
                writerTableNode.setTaskToTableRels(oldWriterTableNode.getTaskToTableRels());
                writerTableNode.setTableToTaskRels(oldWriterTableNode.getTableToTaskRels());
            }
            tableRepository.save(writerTableNode);
        }


        for (TableNode readerTableNode : readerTableNodeList) {
            TableNode oldReaderTableNode = tableRepository.findByTableNameAndDatasourceHostPort(readerTableNode.getTableName(), readerTableNode.getDatasourceHostPort())
                    .orElse(null);
            if (oldReaderTableNode != null) {
                readerTableNode.setId(oldReaderTableNode.getId());
                readerTableNode.setTableToTaskRels(Stream.concat(readerTableNode.getTableToTaskRels().stream(), oldReaderTableNode.getTableToTaskRels().stream())
                        .distinct()
                        .collect(Collectors.toList()));
            }
            tableRepository.save(readerTableNode);
        }
        taskRepository.save(taskNode);
    }

    /**
     * Save node information
     *
     * @param tableNodeList
     */
    @Transactional("neo4jTransactionManager")
    public void saveTable(List<TableNode> tableNodeList) {
        for (TableNode writerTableNode : tableNodeList) {
            TableNode oldWriterTableNode = tableRepository.findByTableNameAndDatasourceHostPort(writerTableNode.getTableName(), writerTableNode.getDatasourceHostPort())
                    .orElse(null);
            if (oldWriterTableNode != null) {
                writerTableNode.setId(oldWriterTableNode.getId());
                writerTableNode.setTaskToTableRels(oldWriterTableNode.getTaskToTableRels());
                writerTableNode.setTableToTaskRels(oldWriterTableNode.getTableToTaskRels());
            }
            tableRepository.save(writerTableNode);
        }
    }

    @Transactional
    public void save() {
        // 1. Source table
        TableNode orders = tableRepository.save(
                TableNode.builder()
                        .name("user")
                        .tableName("sales_db.dwh.user")
//                        .datasourceId(1L)
                        .datasourceType("MySql")
                        .dbName("sales_db")
                        .sid("dwh")
                        .build());

        // 2. Task 1
        TaskNode task1 = taskRepository.save(
                TaskNode.builder()
                .name("Task Name")
                        .taskId(3L)
                        .taskCode("4000000000")
                        .build());

        // 3. Target table 1
        TableNode toOrders = tableRepository.save(
                TableNode.builder()
                        .name("to_user")
                        .tableName("sales_db.dwh.to_user")
//                        .datasourceId(1L)
                        .datasourceType("MySql")
                        .dbName("sales_db")
                        .sid("dwh")
                        .build());
        /* 6. Building relationships (core) */

        // orders -> task1
        orders.setTableToTaskRels(Arrays.asList(TableToTaskRel.builder()
                .taskId(3L)
                .taskCode("4000000000")
//                .datasourceId(1L)
                .tableName("sales_db.dwh.user")
                .task(task1).build()));
        tableRepository.save(orders);

        // task1 -> toOrders
        task1.setTaskToTableRels(Arrays.asList(TaskToTableRel.builder()
                .taskId(3L)
                .taskCode("4000000000")
//                .datasourceId(1L)
                .tableName("sales_db.dwh.to_user")
                .table(toOrders).build()));
        taskRepository.save(task1);
    }

    @Transactional("neo4jTransactionManager")
    public void deletdAll() {
        taskRepository.deleteTask();
        taskRepository.deleteTable();
    }

//    public static void main(String[] args) {
// // 1. Create driver
//        Driver driver = GraphDatabase.driver("bolt://110.42.38.62:40053",
//                AuthTokens.basic("neo4j", "InC3tmU4bijT4vkl"));
//
// // 2. Open the automatically closed session
//        try (Session session = driver.session(SessionConfig.forDatabase("neo4j"))) {
// // 3. Execute Cypher and return a record
//            Record row = session.run(
//                    "MATCH (currentTable:Table {tableName: $tableName})   " +
//                            "OPTIONAL MATCH (currentTable)<-[r1:TASK_TO_TABLE]-(sourceTask:Task)   " +
//                            "   " +
//                            "OPTIONAL MATCH (sourceTask)<-[r2:TABLE_TO_TASK]-(sourceTable:Table)   " +
//                            "   " +
//                            "OPTIONAL MATCH (currentTable)-[r3:TABLE_TO_TASK]->(targetTask:Task)   " +
//                            "OPTIONAL MATCH (targetTask)-[r4:TASK_TO_TABLE]->(targetTable:Table)   " +
//                            "RETURN     " +
//                            "collect(DISTINCT sourceTask) + collect(DISTINCT targetTask) AS tasks,   " +
//                            "currentTable + collect(DISTINCT sourceTable) + collect(DISTINCT targetTable)  AS tables,   " +
//                            "collect(DISTINCT r1) + collect(DISTINCT r2) +collect(DISTINCT r3) + collect(DISTINCT r4) AS  rels ",
//                    Values.parameters("tableName", "sales_db.dwh.to_orders")
// ).single(); // Explicitly take only one item to avoid cursors
//
//            List<Relationship> rels =
// row.get("rels").asList(v -> v.asRelationship()); // or Value::asRelationship
//
//            List<JSONObject> relsObj = new ArrayList<>();
//            for (Relationship rel : rels) {
//                JSONObject relObj = new JSONObject();
//                relObj.put("startNodeId", rel.startNodeId());
//                relObj.put("endNodeId", rel.endNodeId());
//                relObj.put("type", rel.type());
//                JSONObject properties = new JSONObject();
//                properties.put("taskId", rel.get("taskId").asLong());
//                properties.put("taskCode", rel.get("taskCode").asString());
//                properties.put("datasourceId", rel.get("datasourceId").asLong());
//                properties.put("tableName", rel.get("tableName").asString());
//                relObj.put("properties", properties);
//                relsObj.add(relObj);
//            }
//            System.out.println(JSONObject.toJSONString(relsObj));
//        }
//        driver.close();
//    }

}
