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

import useDefaultLang from "@/composables/useDefaultLang"
const { td } = useDefaultLang()


export const treeData =
  [
    {
      id: 2,
      label: "DM",
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
      value: "MYSQL",
      type: "MYSQL",
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
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
    {
      id: 10,
      label: td('da.qualityTask.flinkBatch'),
      value: "FlinkBatch",
      type: "FLINKBATCH",
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
    {
      id: 11,
      label: td('da.qualityTask.flinkStream'),
      value: "FlinkStream",
      type: "FLINKSTREAM",
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
  ];
