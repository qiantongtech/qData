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
