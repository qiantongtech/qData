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
