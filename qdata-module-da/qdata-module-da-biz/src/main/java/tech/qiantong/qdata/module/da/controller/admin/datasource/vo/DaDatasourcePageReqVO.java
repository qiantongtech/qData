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

package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.List;

/**
 * Datasource Request VO DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据源 Request VO")
@Data
public class DaDatasourcePageReqVO extends PageParam {

    private static final long serialVersionUID = 1L;
        @Schema(description = "ID", example = "")
        private Long id;
    @Schema(description = "数据源名称", example = "")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    private String datasourceType;

    @Schema(description = "数据源配置(json字符串)", example = "")
    private String datasourceConfig;

    @Schema(description = "IP", example = "")
    private String ip;

    @Schema(description = "端口号", example = "")
    private Long port;

    @Schema(description = "数据库表数", example = "")
    private Long listCount;

    @Schema(description = "同步记录数", example = "")
    private Long syncCount;

    @Schema(description = "同步数据量大小", example = "")
    private Long dataSize;

    @Schema(description = "描述", example = "")
    private String description;

    @Schema(description = "项目编码", example = "")
    private String projectCode;

    @Schema(description = "数据源id集合", example = "")
    private List<Long> idList;

    /**
     * SQL Parser
     */
    @TableField(exist = false)
    private String sqlText;

}
