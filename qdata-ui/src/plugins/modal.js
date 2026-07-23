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

import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus'
import { i18n } from '@/plugins/vueI18n'
let loadingInstance;

export default {
  // Message prompt
  msg(content) {
    ElMessage.info(content)
  },
  // error message
  msgError(content) {
    ElMessage.error(content)
  },
  // success message
  msgSuccess(content) {
    ElMessage.success(content)
  },
  // warning message
  msgWarning(content) {
    ElMessage.warning(content)
  },
  // Pop up prompt
  alert(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'))
  },
  // Error message
  alertError(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'error' })
  },
  // Tips for success
  alertSuccess(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'success' })
  },
  // Warning
  alertWarning(content) {
    ElMessageBox.alert(content, i18n.global.t('common.message.systemPrompt'), { type: 'warning' })
  },
  // Notification tips
  notify(content) {
    ElNotification.info(content)
  },
  // Error notification
  notifyError(content) {
    ElNotification.error(content);
  },
  // Success notification
  notifySuccess(content) {
    ElNotification.success(content)
  },
  // warning notification
  notifyWarning(content) {
    ElNotification.warning(content)
  },
  // Confirmation form
  confirm(content) {
    return ElMessageBox.confirm(content, i18n.global.t('common.message.systemPrompt'), {
      confirmButtonText: i18n.global.t('common.button.confirm'),
      cancelButtonText: i18n.global.t('common.button.cancel'),
      type: "warning",
    })
  },
  // Submit content
  prompt(content) {
    return ElMessageBox.prompt(content, i18n.global.t('common.message.systemPrompt'), {
      confirmButtonText: i18n.global.t('common.button.confirm'),
      cancelButtonText: i18n.global.t('common.button.cancel'),
      type: "warning",
    })
  },
  // Open mask layer
  loading(content) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: "rgba(0, 0, 0, 0.7)",
    })
  },
  // Turn off mask layer
  closeLoading() {
    loadingInstance.close();
  }
}
