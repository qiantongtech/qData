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

// Data source configuration table (icon + label type)
const DATASOURCE_CONFIG = {
  DM: { icon: "datasource/img-dm.png" },
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
 * Get data source icon
 * @param {string} type data source type
 * @returns {string} Icon URL
 */
export const getDatasourceIcon = (type) => {
  if (!type) return "";
  const key = type.toUpperCase();
  const config = DATASOURCE_CONFIG[key];
  if (!config || !config.icon) return "";

  // Use relative paths to ensure Vite can correctly resolve dynamic URLs
  // src/utils/datasource.js -> src/assets/images/common/
  return new URL(`../assets/images/common/${config.icon}`, import.meta.url).href;
};

/**
 * Get data source label type
 * @param {string} type data source type
 * @returns {string} tag type (info, warning, success, etc.)
 */
export const getDatasourceTagType = (type) => {
  if (!type) return "success"; // Returns success by default
  const key = type.toUpperCase();
  const config = DATASOURCE_CONFIG[key];
  return (config && config.tagType) || "success";
};
