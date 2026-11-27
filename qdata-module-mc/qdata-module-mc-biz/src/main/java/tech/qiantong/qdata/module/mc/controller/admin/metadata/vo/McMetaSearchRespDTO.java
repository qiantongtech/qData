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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.mc.controller.admin.metadata.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.page.PageParam;

import java.util.Date;
import java.util.List;

/**
 * Metadata retrieval uniformly returns entities
 * For MC_DB / MC_TABLE / MC_COLUMN UNION ALL queries
 */
@Data
@Schema(description = "元数据检索结果")
public class McMetaSearchRespDTO extends PageParam {

    private static final long serialVersionUID = 1L;

    // ================== Basic logo ==================

    @Schema(description = "元数据ID")
    private Long id;

    @Schema(description = "元数据类型(DB-1 / TABLE-2 / COLUMN-3)")
    private String type;

    private Long datasourceId;

    private String dbType;

    private String name;

    private String status;


    private Date updateTime;

    private McDbRespVO mdDbDO;
    private McTableRespVO mdTableRespVO;
    private McColumnRespVO mdColumnDO;



    //Query parameters
    @TableField(exist = false)
    private String keyword;

    //Metadata type (DB-1/TABLE-2/COLUMN-3)
    @TableField(exist = false)
    private List<String> types;

    //Data source type
    @TableField(exist = false)
    private List<String> dbTypes;

    //Time
    @TableField(exist = false)
    private Date startTime;
    @TableField(exist = false)
    private Date endTime;

    /**
     * Whether to display on the portal: 0-not displayed, 1-displayed
     */
    @Schema(description = "是否在门户展示：0-不展示，1-展示", example = "0")
    private String portalVisible;

}
