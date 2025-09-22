import { computed } from 'vue'

// 可配置的时间段与问候语
const GREETING_CONFIG = [
  { hour: 6,  greeting: '夜深了',  message: '早点休息，照顾好自己。' },
  { hour: 9,  greeting: '早上好',  message: '晨光不负赶路人！' },
  { hour: 12, greeting: '上午好',  message: '祝你开心每一天！' },
  { hour: 14, greeting: '中午好',  message: '记得按时吃饭哦～' },
  { hour: 18, greeting: '下午好',  message: '保持专注，继续加油！' },
  { hour: 22, greeting: '晚上好',  message: '放松心情，享受夜晚。' }
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