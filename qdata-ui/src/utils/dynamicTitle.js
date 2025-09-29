import store from '@/store'
import defaultSettings from '@/settings'
import useSettingsStore from '@/store/system/settings'

/**
 * 动态修改标题
 */
const title = localStorage.getItem('icoTitle') || defaultSettings.title;
console.log(title,'sfsdfsdf');

export function useDynamicTitle() {
  const settingsStore = useSettingsStore();
  if (settingsStore.dynamicTitle) {
    document.title = settingsStore.title ? settingsStore.title + ' - ' + title : title;
  } else {
    document.title = title;
  }
}
