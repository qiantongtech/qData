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
// AI chat chat
export const ChatMessageApi = {
    // Message list
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

    // Export detailed list
    exportDetailData: (params) => {
        return request({
            url: `/chat/message/exportDetailData`,
            method: 'get',
            params,
            responseType: 'blob'
        });
    },

    // Send Stream message
    // Why not use axios? Because it does not support SSE calls
    sendChatMessageStream: async (
        conversationId,
        content,
        ctrl,
        contextFlag,
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
                modelId,
                contextFlag,
                replyType,
                content
            }),
            onmessage: onMessage,
            onerror: onError,
            onclose: onClose,
            signal: ctrl.signal
        });
    },
    // Compliance check
    ruleWriting: async (
        writingId,
        writingTitle,
        writingArticle,
        ruleIds,
        ruleNames,
        customRule,
        ctrl,
        onMessage,
        onError,
        onClose
    ) => {
        const token = getToken();
        return fetchEventSource(
            `${import.meta.env.VITE_APP_BASE_AI}/app/complianceCheck/checkStream`,
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                openWhenHidden: true,
                body: JSON.stringify({
                    writingId: writingId,
                    writingTitle: writingTitle,
                    writingArticle: writingArticle,
                    ruleIds: ruleIds,
                    ruleNames: ruleNames,
                    customRule: customRule
                }),
                onmessage: onMessage,
                onerror: onError,
                onclose: onClose,
                signal: ctrl.signal
            }
        );
    },
    // Delete message
    deleteChatMessage: (id) => {
        return request({
            url: `/chat/message/` + id,
            method: 'delete'
        });
    },

    // Delete messages from a specified conversation
    deleteByConversationId: (conversationId) => {
        return request({
            url: `/chat/message/deleteByConversationId?conversationId=${conversationId}`,
            method: 'delete'
        });
    }
};
