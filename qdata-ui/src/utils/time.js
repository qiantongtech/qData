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
