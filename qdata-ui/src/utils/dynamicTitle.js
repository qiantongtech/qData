import store from '@/store'
import defaultSettings from '@/settings'
import useSettingsStore from '@/store/system/settings'

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
  const settingsStore = useSettingsStore();
  if (settingsStore.dynamicTitle) {
    // document.title = settingsStore.title + ' - ' + defaultSettings.title;
    document.title = settingsStore.title + ' - ' + '千数平台';
  } else {
    document.title = '千数平台';
  }
}
