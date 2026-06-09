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
  const { t } = useI18n()
  const now = new Date()
  const currentHour = now.getHours()

  const matched = computed(() => {
    for (const config of GREETING_CONFIG) {
      if (currentHour < config.hour) {
        return config
      }
    }
    return GREETING_CONFIG[GREETING_CONFIG.length - 1] // 默认
  })

  return {
    timeGreeting: matched,
    greeting: t(`login.greeting.${matched.value.key}`),
    message: t(`login.greeting.${matched.value.msgKey}`)
  }
}
