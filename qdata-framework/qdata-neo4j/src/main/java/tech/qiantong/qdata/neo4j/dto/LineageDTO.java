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

package tech.qiantong.qdata.neo4j.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import tech.qiantong.qdata.neo4j.node.TableNode;
import tech.qiantong.qdata.neo4j.node.TaskNode;
import tech.qiantong.qdata.neo4j.rel.TableToTaskRel;
import tech.qiantong.qdata.neo4j.rel.TaskToTableRel;

import java.util.List;
import java.util.Map;

@Data
public class LineageDTO {

    /**
     * 当前表节点
     */
    private TableNode currentTable;

    /**
     * 所有Task节点
     */
    private List<TaskNode> tasks;
    /**
     * 所有Table节点
     */
    private List<TableNode> tables;

    /**
     * 节点关系
     */
    private List<JSONObject> rels;
}
