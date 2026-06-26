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
