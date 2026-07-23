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

package tech.qiantong.qdata.module.da.dal.dataobject.datasource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Datasource DO - DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Data
@TableName(value = "DA_DATASOURCE")
// Used for auto-increment primary keys in Oracle, PostgreSQL, Kingbase, DB2, H2 databases. Not needed for MySQL and similar databases.
// @KeySequence("DA_DATASOURCE_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class DaDatasourceDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Datasource Name */
    private String datasourceName;

    /** Datasource Type */
    private String datasourceType;

    /** Datasource Configuration (JSON string) */
    private String datasourceConfig;

    /** Project List */
    @TableField(exist = false)
    private List<DaDatasourceProjectRelDO> projectList;

    /** Project Name */
    @TableField(exist = false)
    private String projectName;

    /** Whether assigned to DPP by admin */
    @TableField(exist = false)
    private Boolean isAdminAddTo;

    /** IP */
    private String ip;

    /** Port */
    private Long port;

    /** Database Table Count (reserved) */
    private Long listCount;

    /** Sync Record Count (reserved) */
    private Long syncCount;

    /** Sync Data Size (reserved) */
    private Long dataSize;

    /** Description */
    private String description;

    /** Valid Flag */
    private Boolean validFlag;

    @TableLogic
    private Boolean delFlag;


    @JSONField(serialize = false)
    public String toJsonString() {
        // Default Fastjson: ignore null fields, auto field order
        return JSON.toJSONString(this);
    }

    @JSONField(serialize = false)
    public DbQueryProperty simplify() {
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                this.getDatasourceType(),
                this.getIp(),
                this.getPort(),
                this.getDatasourceConfig()
        );
        return dbQueryProperty;
    }

}
