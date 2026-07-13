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

package tech.qiantong.qdata.quality.dal.dataobject.datasource;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;


/**
 * Data source DO object DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_DATASOURCE")
// Primary key auto-increment for Oracle, PostgreSQL, Kingbase, DB2, H2 databases. If it is a database such as MySQL, you do not need to write it.
// @KeySequence("DA_DATASOURCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDatasourceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Data source name */
    private String datasourceName;

    /** Data source type */
    private String datasourceType;

    /** Data source configuration (json string) */
    private String datasourceConfig;

    /** Project name */
    @TableField(exist = false)
    private String projectName;

    /** Whether the administrator is assigned to data research and development */
    @TableField(exist = false)
    private Boolean isAdminAddTo;

    /** IP */
    private String ip;

    /** Port number */
    private Long port;

    /** Number of database tables (reserved) */
    private Long listCount;

    /** Number of synchronized records (reserved) */
    private Long syncCount;

    /** Synchronization data size (reserved) */
    private Long dataSize;

    /** Description */
    private String description;

    /** Is it valid */
    private Boolean validFlag;

    @TableLogic
    private Boolean delFlag;


}
