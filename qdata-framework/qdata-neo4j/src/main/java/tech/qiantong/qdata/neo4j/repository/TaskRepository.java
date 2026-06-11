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
 */

package tech.qiantong.qdata.neo4j.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.qiantong.qdata.neo4j.node.TaskNode;
import tech.qiantong.qdata.neo4j.rel.TaskToTableRel;

import java.util.List;
import java.util.Optional;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 14:11
 **/
@Repository
public interface TaskRepository extends Neo4jRepository<TaskNode, Long> {

    TaskNode findByTaskId(Long taskId);

    @Query("MATCH ()-[r {taskId:$taskId}]->() DELETE r")
    void deleteRelByTaskId(@Param("taskId") Long taskId);

    @Query("MATCH (ta:Task) DETACH DELETE ta")
    void deleteTask();

    @Query("MATCH (t:Table) DETACH DELETE t")
    void deleteTable();
}
