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

import request from '@/utils/requestAi.js';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { getToken } from '@/utils/auth';
// import { data } from 'vis-network';

// AI chat 聊天
export const ChatMessageApi = {
    // 消息列表
    getChatMessageListByConversationId: (conversationId) => {
        return request({
            url: `/chat/message/list-by-conversation-id?conversationId=${conversationId}`
        });
    },
    getSuggested: (messageId) => {
        return request({
            url: `/chat/message/getSuggested/${messageId}`,
            method: 'get',
            timeout: 30 * 1000
        });
    },

    // 导出明细列表
    exportDetailData: (params) => {
        return request({
            url: `/chat/message/exportDetailData`,
            method: 'get',
            params,
            responseType: 'blob'
        });
    },

    // 发送 Stream 消息
    // 为什么不用 axios 呢？因为它不支持 SSE 调用
    sendChatMessageStream: async (
        conversationId,
        content,
        ctrl,
        enableContext,
        datasourceId,
        factTableName,
        factTableComment,
        dimensionTable,
        replyType,
        modelId,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(`${import.meta.env.VITE_APP_BASE_AI}/chat/message/send-stream`, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            openWhenHidden: true,
            body: JSON.stringify({
                conversationId,
                content,
                useContext: enableContext,
                datasourceId,
                factTableName,
                factTableComment,
                dimensionTable,
                replyType,
                modelId
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },
    // 删除消息
    deleteChatMessage: (id) => {
        return request({
            url: `/chat/message/` + id,
            method: 'delete'
        });
    },

    // 删除指定对话的消息
    deleteByConversationId: (conversationId) => {
        return request({
            url: `/chat/message/deleteByConversationId?conversationId=${conversationId}`,
            method: 'delete'
        });
    }
};
// 查询模型管理详细
export function getModelLists() {
  return request({
    url: '/model/list' ,
    method: 'get'
  })
}
