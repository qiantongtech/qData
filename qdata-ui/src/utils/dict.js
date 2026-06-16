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
 */

import useDictStore from '@/store/system/dict'
import { getDicts } from '@/api/system/system/dict/data'
import { i18n } from '@/plugins/vueI18n'

function translateLabel(dictType, value, fallback) {
  const key = `dict.${dictType}.${value}`
  const translated = i18n.global.t(key)
  return translated !== key ? translated : fallback
}

function translateRemark(dictType, value, fallback) {
  const key = `dict.${dictType}.remark.${value}`
  const translated = i18n.global.t(key)
  return translated !== key ? translated : fallback
}

function mapAndTranslate(dictType, items) {
  return items.map(p => ({
    ...p,
    label: translateLabel(dictType, p.value, p.label),
    remark: translateRemark(dictType, p.value, p.label),
  }))
}

/**
 * 获取字典数据
 */
export function useDict(...args) {
  const res = ref({});
  return (() => {
    args.forEach((dictType, index) => {
      res.value[dictType] = [];
      const dicts = useDictStore().getDict(dictType);
      if (dicts) {
        res.value[dictType] = mapAndTranslate(dictType, dicts);
      } else {
        getDicts(dictType).then(resp => {
          const rawData = resp.data.map(p => ({ label: p.dictLabel, value: p.dictValue, elTagType: p.listClass, elTagClass: p.cssClass, remark: p.remark }))
          useDictStore().setDict(dictType, rawData);
          res.value[dictType] = mapAndTranslate(dictType, rawData);
        })
      }
    })
    return toRefs(res.value);
  })()
}
