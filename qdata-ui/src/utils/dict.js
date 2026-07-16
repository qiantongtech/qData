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
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
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
 * Get dictionary data
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
