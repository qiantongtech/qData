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
     * Current table node
     */
    private TableNode currentTable;

    /**
     * All Task nodes
     */
    private List<TaskNode> tasks;
    /**
     * All Table nodes
     */
    private List<TableNode> tables;

    /**
     * Node relationship
     */
    private List<JSONObject> rels;
}
