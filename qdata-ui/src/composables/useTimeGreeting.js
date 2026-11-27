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

import { computed } from 'vue'
import { useI18n } from 'vue-i18n'

const GREETING_CONFIG = [
  { hour: 6, key: 'greetingLateNight', msgKey: 'greetingLateNightMsg' },
  { hour: 9, key: 'greetingMorning', msgKey: 'greetingMorningMsg' },
  { hour: 12, key: 'greetingForenoon', msgKey: 'greetingForenoonMsg' },
  { hour: 14, key: 'greetingNoon', msgKey: 'greetingNoonMsg' },
  { hour: 18, key: 'greetingAfternoon', msgKey: 'greetingAfternoonMsg' },
  { hour: 22, key: 'greetingEvening', msgKey: 'greetingEveningMsg' }
]

export function useTimeGreeting() {
  const { t } = useI18n();
  const now = new Date()
  const currentHour = now.getHours()

  const matched = computed(() => {
    for (const config of GREETING_CONFIG) {
      if (currentHour < config.hour) {
        return config
      }
    }
    return GREETING_CONFIG[GREETING_CONFIG.length - 1] // Default
  })

  return {
    timeGreeting: matched,
    greeting: t(`login.greeting.${matched.value.key}`),
    message: t(`login.greeting.${matched.value.msgKey}`)
  }
}
