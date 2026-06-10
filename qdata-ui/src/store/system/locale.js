import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import ja from 'element-plus/es/locale/lang/ja'

const LANG_KEY = 'qdata_lang'

const SUPPORTED_LANGS = ['zh-CN', 'en-US', 'ja-JP']

const elLocaleMap = {
  'zh-CN': zhCn,
  'en-US': en,
  'ja-JP': ja
}

const getStoredLang = () => {
  const raw = localStorage.getItem(LANG_KEY)
  return SUPPORTED_LANGS.includes(raw) ? raw : 'zh-CN'
}

const useLocaleStore = defineStore('locale', {
  state: () => {
    const lang = getStoredLang()
    return {
      currentLocale: {
        lang,
        elLocale: elLocaleMap[lang]
      },
      localeMap: [
        {
          lang: 'zh-CN',
          name: '简体中文'
        },
        {
          lang: 'en-US',
          name: 'English'
        },
        {
          lang: 'ja-JP',
          name: '日本語'
        }
      ]
    }
  },
  getters: {
    getCurrentLocale() {
      return this.currentLocale
    },
    getLocaleMap() {
      return this.localeMap
    }
  },
  actions: {
    setCurrentLocale(localeMap) {
      this.currentLocale.lang = localeMap?.lang
      this.currentLocale.elLocale = elLocaleMap[localeMap?.lang]
      if (localeMap?.lang) {
        localStorage.setItem(LANG_KEY, localeMap.lang)
      }
    }
  }
})

export default useLocaleStore

export const useLocaleStoreWithOut = () => {
  return useLocaleStore()
}
