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

const td = (key, def) => {
    if (!i18n.global.te(key)) return def;
    return i18n.global.t(key);
};

export const treeData =
  [
    {
      id: 2,
      label: "DM",
      value: "DM",
      type: "DM",
      falg: true,
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
    {
      id: 3,
      label: "Oracle",
      value: "Oracle",
      type: "ORACLE",
      falg: true,
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-oracle-one.png', import.meta.url).href,
    },
    {
      id: 4,
      label: "MySQL",
      value: "MYSQL",
      type: "MYSQL",
      falg: true,
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
    {
      id: 5,
      label: "Kingbase",
      value: "Kingbase",
      type: "KINGBASE",
      falg: true,
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
    // 禁用的节点
    {
      id: 6,
      label: "Sqlerver",
      value: "Sqlerver",
      type: "SQLSERVER",
      children: [],
      disabled: true // 禁用该节点
    },
    {
      id: 7,
      label: "PostgreSql",
      value: "PostgreSql",
      type: "POSTGRESQL",
      children: [],
      disabled: true // 禁用该节点
    },
    {
      id: 8,
      label: "Hive",
      value: "Hive",
      type: "HIVE",
      children: [],
      disabled: false // 禁用该节点
    },
    {
      id: 9,
      label: "SparkSql",
      value: "SparkSql",
      type: "SPARKSQL",
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
    {
      id: 10,
      label: td('da.qualityTask.flinkBatch'),
      value: "FlinkBatch",
      type: "FLINKBATCH",
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
    {
      id: 11,
      label: td('da.qualityTask.flinkStream'),
      value: "FlinkStream",
      type: "FLINKSTREAM",
      children: [],
      icon: new URL('@/assets/images/common/dpp/img-sr.png', import.meta.url).href,
    },
  ];
