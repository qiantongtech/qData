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

import useLocaleStore from '@/store/system/locale'

const modules = import.meta.glob(
  '@/assets/images/locales/**/*.{png,jpg,jpeg,svg,webp,gif}',
  { eager: true, import: 'default' }
)

const findBySuffix = (suffix) => {
  for (const [key, url] of Object.entries(modules)) {
    if (key.endsWith(suffix)) return url
  }
  return ''
}

export function useLocaleImage() {
  const localeStore = useLocaleStore()

  const getImage = (path) => {
    const lang = localeStore.getCurrentLocale.lang
    return findBySuffix(`/${lang}/${path}`) || findBySuffix(`/zh-CN/${path}`) || ''
  }

  return { getImage }
}
