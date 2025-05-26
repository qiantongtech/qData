// cronUtils.js

import cronstrue from "cronstrue";
import "cronstrue/locales/zh_CN";

/**
 * 将 Cron 表达式转换为中文描述
 * @param {string} cron - Cron 表达式
 * @returns {string} - 转换后的中文描述
 */
export function cronToZh(cron) {
  if (!cron) return "";

  try {
    return (
      cronstrue.toString(cron, { locale: "zh_CN", use24HourTimeFormat: true }) +
      " 执行"
    );
  } catch (error) {
    console.error("Cron 表达式解析错误:", error);
    return "无效的 Cron 表达式";
  }
}
