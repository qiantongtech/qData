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

package tech.qiantong.qdata.neo4j.rel;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import tech.qiantong.qdata.neo4j.node.TaskNode;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-08-27 14:02
 **/
@RelationshipProperties
@Data
@Builder
public class TableToTaskRel {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 全限定表名 例如：sales_db.dwh.orders
     */
    private String tableName;
//    /**
//     * 数据源 id
//     */
//    private Long datasourceId;

    /**
     * 数据源ip:port
     */
    private String datasourceHostPort;

    @TargetNode
    private TaskNode task;
}
