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

import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus'
import { i18n } from '@/plugins/vueI18n'
let loadingInstance;

export default {
  // 消息提示
  msg(content) {
    ElMessage.info(content)
  },
  // 错误消息
  msgError(content) {
    ElMessage.error(content)
  },
  // 成功消息
  msgSuccess(content) {
    ElMessage.success(content)
  },
  // 警告消息
  msgWarning(content) {
    ElMessage.warning(content)
  },
  // 弹出提示
  alert(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'))
  },
  // 错误提示
  alertError(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'error' })
  },
  // 成功提示
  alertSuccess(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'success' })
  },
  // 警告提示
  alertWarning(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'warning' })
  },
  // 通知提示
  notify(content) {
    ElNotification.info(content)
  },
  // 错误通知
  notifyError(content) {
    ElNotification.error(content);
  },
  // 成功通知
  notifySuccess(content) {
    ElNotification.success(content)
  },
  // 警告通知
  notifyWarning(content) {
    ElNotification.warning(content)
  },
  // 确认窗体
  confirm(content) {
    return ElMessageBox.confirm(content, i18n.global.t('common.message.systemPrompt'), {
      confirmButtonText: i18n.global.t('common.button.confirm'),
      cancelButtonText: i18n.global.t('common.button.cancel'),
      type: "warning",
    })
  },
  // 提交内容
  prompt(content) {
    return ElMessageBox.prompt(content, i18n.global.t('common.message.systemPrompt'), {
      confirmButtonText: i18n.global.t('common.button.confirm'),
      cancelButtonText: i18n.global.t('common.button.cancel'),
      type: "warning",
    })
  },
  // 打开遮罩层
  loading(content) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: "rgba(0, 0, 0, 0.7)",
    })
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.close();
  }
}
