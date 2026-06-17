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
