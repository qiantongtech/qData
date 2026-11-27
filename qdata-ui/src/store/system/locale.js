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

export const getStoredLang = () => {
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
        window.dispatchEvent(new CustomEvent('LangChange'))
      }
    }
  }
})

export default useLocaleStore

export const useLocaleStoreWithOut = () => {
  return useLocaleStore()
}
