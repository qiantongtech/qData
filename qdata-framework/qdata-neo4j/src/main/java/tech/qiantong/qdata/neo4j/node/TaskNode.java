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

package tech.qiantong.qdata.neo4j.node;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import tech.qiantong.qdata.neo4j.rel.TableToTaskRel;
import tech.qiantong.qdata.neo4j.rel.TaskToTableRel;

import java.util.Date;
import java.util.List;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 11:32
 **/
@Node("Task")
@Data
@Builder
public class TaskNode {
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务类型;1：离线任务(数据集成) 2：实时任务 3：数据开发任务 4：作业任务
     */
    private String type;

    /**
     * 执行 SPARK或FLINK
     */
    private String taskType;

    /** task -> table */
    @Relationship(type = "TASK_TO_TABLE", direction = Relationship.Direction.OUTGOING)
    private List<TaskToTableRel> taskToTableRels;

    /** table -> task */
    @Relationship(type = "TABLE_TO_TASK", direction = Relationship.Direction.INCOMING)
    private List<TableToTaskRel> tableToTaskRels;

    /**
     * 下方为扩展字段，不会在neo4j中存储
     */

    /**
     * 上一次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskTime;

    /**
     * 上一次执行状态
     */
    private String taskStatus;

}
