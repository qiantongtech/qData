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

import { Histogram, Opportunity } from "@element-plus/icons-vue";
import { i18n } from "@/plugins/vueI18n";

export const CHAT_TYPES = [
  {
    value: "chart",
    labelKey: 'ai.chat.smartChart',
    label: () => i18n.global.t('ai.chat.smartChart'),
    icon: Histogram,
    disabled: false,
  },
  {
    value: "smart",
    labelKey: 'ai.chat.smartQA',
    label: () => i18n.global.t('ai.chat.smartQA'),
    icon: Opportunity,
    disabled: true,
  },
];
