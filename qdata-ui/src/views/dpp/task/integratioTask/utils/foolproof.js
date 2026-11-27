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
 * Business License: https://docs.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

const DANGEROUS_SQL_KEYWORDS = /\b(select|insert|update|delete|drop|truncate|alter|create|merge|grant|revoke|call|execute|exec)\b/i;

export function validateWhereCondition(value) {
  const condition = String(value || "").trim();
  if (!condition) return { valid: true };
  if (condition.includes(";") || condition.includes("--") || condition.includes("/*") || condition.includes("*/")) {
    return { valid: false, message: "Where 条件格式不正确，请仅填写过滤条件，不要填写完整 SQL 或分号。" };
  }
  if (DANGEROUS_SQL_KEYWORDS.test(condition)) {
    return { valid: false, message: "Where 条件包含危险 SQL 关键字，请仅填写过滤条件。" };
  }
  return { valid: true };
}

export function isDateColumnType(columnType) {
  return /DATE|TIME|TIMESTAMP|YEAR/i.test(String(columnType || ""));
}

export function isNumericColumnType(columnType) {
  return /INT|NUMBER|NUMERIC|DECIMAL|FLOAT|DOUBLE|REAL|LONG|SHORT/i.test(String(columnType || ""));
}

export function isTextColumnType(columnType) {
  return /CHAR|TEXT|STRING|VARCHAR|CLOB/i.test(String(columnType || ""));
}
