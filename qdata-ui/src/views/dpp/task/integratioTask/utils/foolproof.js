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
