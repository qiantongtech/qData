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
import "cronstrue/locales/en";
import "cronstrue/locales/ja";
import { i18n } from '@/plugins/vueI18n'

/**
 * 获取翻译值
 * @param {string} key - 翻译键
 * @param {string} fallback - 默认值
 * @returns {string} - 翻译后的字符串
 */
function getTranslation(key, fallback) {
  if (i18n && i18n.global && i18n.global.t) {
    try {
      return i18n.global.t(key);
    } catch (e) {
      console.warn(`Translation key not found: ${key}`);
      return fallback;
    }
  }
  return fallback;
}

/**
 * 将 Cron 表达式转换为对应语言的描述
 * @param {string} cron - Cron 表达式
 * @returns {string} - 转换后的描述
 */
export function cronToZh(cron) {
  if (!cron) return "";
  try {
    return (
      cronstrue.toString(cron, { locale: i18n.global.locale, use24HourTimeFormat: true }) +
      " " + getTranslation('common.crontab.cronUtils.execute', '执行')
    );
  } catch (error) {
    return getTranslation('common.crontab.cronUtils.invalidExpression', '无效的 Cron 表达式');
  }
}
