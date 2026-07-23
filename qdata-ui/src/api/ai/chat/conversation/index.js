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

import request from "@/utils/requestAi.js";

// AI Chat Conversation API
export const ChatConversationApi = {
  // Get [my] chat conversation
  getChatConversationMy: (id) => {
    return request({
      url: `/chat/conversation/` + id,
      method: 'get',
    });
  },

  // Add [my] chat conversation
  createChatConversationMy: (data) => {
    return request({
      url: `/chat/conversation`,
      method: 'post',
      data: data
    });
  },

  // Update [my] chat conversation
  updateChatConversationMy: (data) => {
    return request({
      url: `/chat/conversation`,
      method: 'put',
      data: data
    });
  },

  // Delete [my] chat conversation
  deleteChatConversationMy: (id) => {
    return request({
      url: `/chat/conversation/` + id,
      method: 'delete',
    });
  },

  // Get [my] chat conversation list
  getChatConversationMyList: () => {
    return request({
      url: `/chat/conversation/myList`,
      method: 'get'
    });
  },

  // Set up relationships
  setAssociations: (data) => {
    return request({
      url: `/chat/conversation/setAssociations`,
      method: 'post',
      data: data
    });
  },

}
