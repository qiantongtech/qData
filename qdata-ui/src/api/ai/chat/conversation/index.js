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

// AI 聊天对话 API
export const ChatConversationApi = {
  // 获得【我的】聊天对话
  getChatConversationMy: (id) => {
    return request({
      url: `/chat/conversation/` + id,
      method: 'get',
    });
  },

  // 新增【我的】聊天对话
  createChatConversationMy: (data) => {
    return request({
      url: `/chat/conversation`,
      method: 'post',
      data: data
    });
  },

  // 更新【我的】聊天对话
  updateChatConversationMy: (data) => {
    return request({
      url: `/chat/conversation`,
      method: 'put',
      data: data
    });
  },

  // 删除【我的】聊天对话
  deleteChatConversationMy: (id) => {
    return request({
      url: `/chat/conversation/` + id,
      method: 'delete',
    });
  },

  // 获得【我的】聊天对话列表
  getChatConversationMyList: () => {
    return request({
      url: `/chat/conversation/myList`,
      method: 'get'
    });
  },

  // 设置关联关系
  setAssociations: (data) => {
    return request({
      url: `/chat/conversation/setAssociations`,
      method: 'post',
      data: data
    });
  },

}
