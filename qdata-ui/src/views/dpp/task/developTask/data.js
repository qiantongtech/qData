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

export const treeData = [
  {
    id: 2,
    label: "DM8",
    value: "DM",
    type: "DM",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 3,
    label: "Oracle",
    value: "Oracle",
    type: "ORACLE",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/oracle.png', import.meta.url).href,
  },
  {
    id: 4,
    label: "MySQL",
    value: "MySql",
    type: "MySql",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 5,
    label: "Kingbase",
    value: "Kingbase",
    type: "KINGBASE",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    elTagType: "info", // 紫色
  },
];
