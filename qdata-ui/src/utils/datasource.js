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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

// 数据源配置表 (图标 + 标签类型)
const DATASOURCE_CONFIG = {
  DM: { icon: "datasource/dm.png" },
  DM8: { icon: "datasource/dm.png" },
  ORACLE: { icon: "datasource/oracle.png" },
  ORACLE11: { icon: "datasource/oracle.png" },
  MYSQL: { icon: "datasource/mysql.png" },
  HIVE: { icon: "datasource/hive.png", tagType: "warning" },
  SQLERVER: { icon: "datasource/sql-server.png", tagType: "warning" },
  SQLSERVER: { icon: "datasource/sql-server.png", tagType: "warning" },
  KAFKA: { icon: "datasource/kafka.png" },
  HDFS: { icon: "datasource/hdfs.png" },
  SHELL: { icon: "datasource/shell.png" },
  KINGBASE: { icon: "datasource/kingbase.png", tagType: "info" },
  KINGBASE8: { icon: "datasource/kingbase.png", tagType: "info" },
  POSTGRESQL: { icon: "datasource/postgresql.svg", tagType: "info" },
  SQL_SERVER: { icon: "datasource/sql-server.svg" },
  SQL_SERVER2008: { icon: "datasource/sql-server.svg" },
  DORIS: { icon: "datasource/doris.svg" },
  DB2: { icon: "datasource/db2.svg", tagType: "info" },
  OSCAR: { icon: "datasource/oscar.svg", tagType: "info" },
  CLICKHOUSE: { icon: "datasource/clickhouse.svg" },
  FLINK: { icon: "flink.svg", tagType: "success" },
  SPARK: { icon: "spark.svg" },
  SPARKSQL: { icon: "spark.svg" },
  FLINKBATCH: { icon: "flink.svg", tagType: "success" },
  FLINKSTREAM: { icon: "flink.svg", tagType: "success" },
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
