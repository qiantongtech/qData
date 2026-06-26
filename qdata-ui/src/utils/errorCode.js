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
