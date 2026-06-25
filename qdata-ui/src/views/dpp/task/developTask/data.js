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
    elTagType: "info", // 紫色
  },
];
