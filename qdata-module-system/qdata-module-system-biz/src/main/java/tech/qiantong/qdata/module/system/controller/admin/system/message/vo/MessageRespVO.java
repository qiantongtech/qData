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

package tech.qiantong.qdata.module.system.controller.admin.system.message.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import tech.qiantong.qdata.common.annotation.Excel;

import java.util.Date;

/**
 * 消息 Response VO 对象 message
 *
 * @author qdata
 * @date 2024-10-31
 */
@Schema(description = "消息 Response VO")
@Data
public class MessageRespVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Excel(name = "发送人")
    @Schema(description = "发送人", example = "")
    private Long senderId;

    @Excel(name = "接收人")
    @Schema(description = "接收人", example = "")
    private Long receiverId;

    @Excel(name = "消息标题")
    @Schema(description = "消息标题", example = "")
    private String title;

    @Excel(name = "消息模板内容")
    @Schema(description = "消息模板内容", example = "")
    private String content;

    @Excel(name = "消息类别")
    @Schema(description = "消息类别", example = "")
    private Integer category;

    @Excel(name = "消息等级")
    @Schema(description = "消息等级", example = "")
    private Integer msgLevel;

    @Excel(name = "消息模块")
    @Schema(description = "消息模块", example = "")
    private Integer module;

    @Excel(name = "实体类型")
    @Schema(description = "实体类型", example = "")
    private Integer entityType;

    @Excel(name = "实体id")
    @Schema(description = "实体id", example = "")
    private Long entityId;

    @Excel(name = "消息链接")
    @Schema(description = "消息链接", example = "")
    private String entityUrl;

    @Excel(name = "是否已读")
    @Schema(description = "是否已读", example = "")
    private Integer hasRead;

    @Excel(name = "是否撤回")
    @Schema(description = "是否撤回", example = "")
    private Integer hasRetraction;

    @Excel(name = "是否有效")
    @Schema(description = "是否有效", example = "")
    private Boolean validFlag;

    @Schema(description = "删除标识")
    private Integer delFlag;

    @Excel(name = "创建人")
    @Schema(description = "创建人", example = "")
    private String createBy;

    @Excel(name = "创建人id")
    @Schema(description = "创建人id", example = "")
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人", example = "")
    private String updateBy;

    @Excel(name = "更新人id")
    @Schema(description = "更新人id", example = "")
    private Long updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @Schema(description = "更新时间", example = "")
    private Date updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注", example = "")
    private String remark;

}
