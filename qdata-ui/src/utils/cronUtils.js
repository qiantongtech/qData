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
