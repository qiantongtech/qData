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

import { i18n } from '@/plugins/vueI18n';

export const treeData = [
  {
    id: 2,
    label: i18n.global.t('dpp.developTask.dm8'),
    value: "DM",
    type: "DM",
    falg: true,
    children: [],
    icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
  },
  {
    id: 3,
    label: i18n.global.t('dpp.developTask.oracle'),
    value: "Oracle",
    type: "ORACLE",
    falg: true,
    children: [],
    icon: new URL('@/assets/images/common/dpp/img-oracle-one.png', import.meta.url).href,
  },
  {
    id: 4,
    label: i18n.global.t('dpp.developTask.mysql'),
    value: "MySql",
    type: "MySql",
    falg: true,
    children: [],
    icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
  },
  {
    id: 5,
    label: i18n.global.t('dpp.developTask.kingbase'),
    value: "Kingbase",
    type: "KINGBASE",
    falg: true,
    children: [],
    icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    elTagType: "info", // purple
  },
];
