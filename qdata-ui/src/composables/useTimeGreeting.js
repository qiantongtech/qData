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

import { computed } from 'vue'

// 可配置的时间段与问候语
const GREETING_CONFIG = [
  { hour: 6, greeting: 'night', message: 'nightMessage' },
  { hour: 9, greeting: 'morning', message: 'morningMessage' },
  { hour: 12, greeting: 'forenoon', message: 'forenoonMessage' },
  { hour: 14, greeting: 'noon', message: 'noonMessage' },
  { hour: 18, greeting: 'afternoon', message: 'afternoonMessage' },
  { hour: 22, greeting: 'evening', message: 'eveningMessage' }
]

export function useTimeGreeting() {
  const now = new Date()
  const currentHour = now.getHours()

  const timeGreeting = computed(() => {
    for (let config of GREETING_CONFIG) {
      if (currentHour < config.hour) {
        return config
      }
    }
    return GREETING_CONFIG[GREETING_CONFIG.length - 1] // 默认
  })

  return {
    timeGreeting: timeGreeting,
    // 拆解使用
    greeting: timeGreeting.value.greeting,
    message: timeGreeting.value.message
  }
}
