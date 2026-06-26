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

import moment from "moment";
import { i18n } from "@/plugins/vueI18n";

export function timeAgo(timeStr) {
  const date = new Date(timeStr);
  const now = new Date();
  const diff = (now - date) / 1000; // 秒

  if (diff < 60) {
    return moment(timeStr).format("HH:mm");
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}${i18n.global.t('common.time.minutesAgo')}`;
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}${i18n.global.t('common.time.hoursAgo')}`;
  } else if (diff < 2592000) {
    return `${Math.floor(diff / 86400)}${i18n.global.t('common.time.daysAgo')}`;
  } else if (diff < 31536000) {
    return `${Math.floor(diff / 2592000)}${i18n.global.t('common.time.monthsAgo')}`;
  } else {
    return `${Math.floor(diff / 31536000)}${i18n.global.t('common.time.yearsAgo')}`;
  }
}
