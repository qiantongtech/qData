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

package tech.qiantong.qdata.module.da.controller.admin.datasource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据源 创建/修改 Request VO DA_DATASOURCE
 *
 * @author lhs
 * @date 2025-01-21
 */
@Schema(description = "数据源 Response VO")
@Data
public class DaDatasourceSaveReqVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "数据源名称", example = "")
    @Size(max = 256, message = "数据源名称长度不能超过256个字符")
    private String datasourceName;

    @Schema(description = "数据源类型", example = "")
    @Size(max = 256, message = "数据源类型长度不能超过256个字符")
    private String datasourceType;

    @Schema(description = "数据源配置(json字符串)", example = "")
    @Size(max = 256, message = "数据源配置(json字符串)长度不能超过256个字符")
    private String datasourceConfig;

    @Schema(description = "旧项目集合", example = "")
    private List<Long> projectListOld;

    @Schema(description = "项目集合", example = "")
    private List<DaDatasourceProjectRelDO> projectList;

    @Schema(description = "IP", example = "")
    @Size(max = 256, message = "IP长度不能超过256个字符")
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
    @Size(max = 256, message = "描述长度不能超过256个字符")
    private String description;

    @Schema(description = "备注", example = "")
    @Size(max = 256, message = "备注长度不能超过256个字符")
    private String remark;


}
