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

import { i18n } from "@/plugins/vueI18n";

const errorCode = {}

const errorCodeMap = {
  '401': 'common.error.code401',
  '403': 'common.error.code403',
  '404': 'common.error.code404',
  'default': 'common.error.default'
}

Object.entries(errorCodeMap).forEach(([code, key]) => {
  Object.defineProperty(errorCode, code, {
    enumerable: true,
    get: () => i18n.global.t(key)
  })
})

export default errorCode
