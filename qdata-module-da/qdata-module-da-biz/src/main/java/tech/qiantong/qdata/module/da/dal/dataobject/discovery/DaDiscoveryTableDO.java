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

package tech.qiantong.qdata.module.da.dal.dataobject.discovery;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

/**
 * 数据发现库信息 DO 对象 DA_DISCOVERY_TABLE
 *
 * @author qdata
 * @date 2025-02-11
 */
@Data
@TableName(value = "DA_DISCOVERY_TABLE")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DA_DISCOVERY_TABLE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDiscoveryTableDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务id */
    private Long taskId;

    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据量 */
    private Long dataCount;

    /** 字段量 */
    private Long fieldCount;

    /** 表结构标识 */
    private String changeFlag;

    /** 状态 */
    private String status;

    /** 是否忽略 */
    private String ignoreFlag;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


}
