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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据发现库信息 创建/修改 Request VO DA_DISCOVERY_TABLE
 *
 * @author qdata
 * @date 2025-02-11
 */
@Schema(description = "数据发现库信息 Response VO")
@Data
public class DaDiscoveryTableSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务id", example = "")
    private Long taskId;

    @Schema(description = "任务id集合", example = "")
    private List<Long> taskIdList;

    @Schema(description = "表名称", example = "")
    @Size(max = 256, message = "表名称长度不能超过256个字符")
    private String tableName;

    @Schema(description = "表描述", example = "")
    @Size(max = 256, message = "表描述长度不能超过256个字符")
    private String tableComment;

    @Schema(description = "数据量", example = "")
    private Long dataCount;

    @Schema(description = "字段量", example = "")
    private Long fieldCount;

    @Schema(description = "表结构标识", example = "")
    @Size(max = 256, message = "表结构标识长度不能超过256个字符")
    private String changeFlag;

    @Schema(description = "状态", example = "")
    @Size(max = 256, message = "状态长度不能超过256个字符")
    private String status;

    @Schema(description = "是否忽略", example = "")
    @Size(max = 256, message = "是否忽略长度不能超过256个字符")
    private String ignoreFlag;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


    @Schema(description = "类目编码", example = "")
    @TableField(exist = false)
    private String catCode;

    @Schema(description = "主题id", example = "")
    @TableField(exist = false)
    private String themeId;

    @Schema(description = "资产地图名称", example = "")
    @TableField(exist = false)
    private String assetName;
}
