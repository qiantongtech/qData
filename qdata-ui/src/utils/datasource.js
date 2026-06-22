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

// 数据源配置表 (图标 + 标签类型)
const DATASOURCE_CONFIG = {
  DM: { icon: "datasource/dimg-m.png" },
  DM8: { icon: "datasource/img-dm.png" },
  ORACLE: { icon: "datasource/img-oracle.png" },
  ORACLE11: { icon: "datasource/img-oracle.png" },
  MYSQL: { icon: "datasource/img-mysql.png" },
  HIVE: { icon: "datasource/img-hive.png", tagType: "warning" },
  SQLERVER: { icon: "datasource/img-sql-server.png", tagType: "warning" },
  SQLSERVER: { icon: "datasource/img-sql-server.png", tagType: "warning" },
  KAFKA: { icon: "datasource/img-kafka.png" },
  HDFS: { icon: "datasource/img-hdfs.png" },
  SHELL: { icon: "datasource/img-shell.png" },
  KINGBASE: { icon: "datasource/img-kingbase.png", tagType: "info" },
  KINGBASE8: { icon: "datasource/img-kingbase.png", tagType: "info" },
  POSTGRESQL: { icon: "datasource/icon-postgresql.svg", tagType: "info" },
  SQL_SERVER: { icon: "datasource/icon-sql-server.svg" },
  SQL_SERVER2008: { icon: "datasource/icon-sql-server.svg" },
  DORIS: { icon: "datasource/icon-doris.svg" },
  DB2: { icon: "datasource/icon-db2.svg", tagType: "info" },
  OSCAR: { icon: "datasource/icon-oscar.svg", tagType: "info" },
  CLICKHOUSE: { icon: "datasource/icon-clickhouse.svg" },
  FLINK: { icon: "icon-flink.svg", tagType: "success" },
  SPARK: { icon: "icon-spark.svg" },
  SPARKSQL: { icon: "icon-spark.svg" },
  FLINKBATCH: { icon: "icon-flink.svg", tagType: "success" },
  FLINKSTREAM: { icon: "icon-flink.svg", tagType: "success" },
};

/**
 * 获取数据源图标
 * @param {string} type 数据源类型
 * @returns {string} 图标URL
 */
export const getDatasourceIcon = (type) => {
  if (!type) return "";
  const key = type.toUpperCase();
  const config = DATASOURCE_CONFIG[key];
  if (!config || !config.icon) return "";

  // 使用相对路径以确保 Vite 能正确解析动态 URL
  // src/utils/datasource.js -> src/assets/images/common/
  return new URL(`../assets/images/common/${config.icon}`, import.meta.url).href;
};

/**
 * 获取数据源标签类型
 * @param {string} type 数据源类型
 * @returns {string} 标签类型 (info, warning, success, etc.)
 */
export const getDatasourceTagType = (type) => {
  if (!type) return "success"; // 默认返回 success
  const key = type.toUpperCase();
  const config = DATASOURCE_CONFIG[key];
  return (config && config.tagType) || "success";
};
