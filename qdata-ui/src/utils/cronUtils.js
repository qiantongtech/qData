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
  const localeMap = {
    'zh-CN': 'zh_CN',
    'ja-JP': 'ja',
    'en-US': 'en',
  }

  try {
    return (
      cronstrue.toString(cron, { locale: localeMap[i18n.global.locale.value], use24HourTimeFormat: true }) +
      " " + getTranslation('common.crontab.cronUtils.execute', '执行')
    );
  } catch (error) {
    return getTranslation('common.crontab.cronUtils.invalidExpression', '无效的 Cron 表达式');
  }
}
