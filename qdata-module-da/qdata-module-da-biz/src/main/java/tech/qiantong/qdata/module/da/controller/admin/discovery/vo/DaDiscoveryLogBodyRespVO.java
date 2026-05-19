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

package tech.qiantong.qdata.module.da.controller.admin.discovery.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据发现节点实例-日志 Response VO 对象 DA_DISCOVERY_LOG_BODY
 *
 * @author qdata
 * @date 2025-10-15
 */
@Schema(description = "数据发现节点实例-日志 Response VO")
@Data
public class DaDiscoveryLogBodyRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "时间")
    @Schema(description = "时间; 日志入库的时间", example = "2025-10-15 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @Excel(name = "任务id")
    @Schema(description = "任务id", example = "123")
    private Long taskId;

    @Excel(name = "日志内容")
    @Schema(description = "日志内容", example = "任务执行完成")
    private String logContent;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效;0：无效，1：有效", example = "1")
    private Boolean validFlag;

    @Excel(name = "删除标志")
    @Schema(description = "删除标志;1：已删除，0：未删除", example = "0")
    private Boolean delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "1")
    private Long creatorId;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2025-10-15 10:05:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "1")
    private Long updaterId;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2025-10-15 10:10:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "定时任务日志")
    private String remark;
}
