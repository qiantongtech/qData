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

package tech.qiantong.qdata.ai.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - AI 聊天消息发送 Response VO")
@Data
public class ChatMessageSendRespVO {

    @Schema(description = "发送消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private Message send;

    @Schema(description = "接收消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private Message receive;

    @Schema(description = "消息")
    @Data
    public static class Message {

        @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private Long id;

        @Schema(description = "消息类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "role")
        private String type; // 参见 MessageType 枚举类

        @Schema(description = "回复类型;1:知识问答 2: 知识图表",example = "")
        private String replyType;

        @Schema(description = "聊天内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好，你好啊")
        private String content;

        @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
        private Date createTime;

        @Schema(description = "引用的文章id", example = "")
        private List<String> documentIdList;

        @Schema(description = "引用的文章名称", example = "")
        private List<String> documentNameList;
    }

}
