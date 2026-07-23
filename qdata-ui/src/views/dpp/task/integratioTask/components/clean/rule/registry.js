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

import { defineAsyncComponent } from "vue"
const modules = import.meta.glob("./**/*.vue")

/**
 * Cleaning rule registration list: only maintain meta information and component paths
 * Component resolution is loaded on demand through import.meta.glob + defineAsyncComponent
 */
export const ruleRegistry = {
  "001": { label: "components.cleanRule.numberBoundary", componentPath: "./numberBoundaryRule.vue" },
  "008": { label: "components.cleanRule.decimalFormatter", componentPath: "./decimalFormatterRule.vue" },
  "010": { label: "components.cleanRule.affixEditor", componentPath: "./affixEditorRule.vue" },
  "024": { label: "components.cleanRule.enumMap", componentPath: "./enumMapRule/index.vue" },
  "029": { label: "components.cleanRule.combinerFieldUnique", componentPath: "./combinerFieldUniqueRule.vue" },
  "011": { label: "components.cleanRule.regexReplace", componentPath: "./regexReplaceRule.vue" },
  "039": { label: "components.cleanRule.cleanExpiredData", componentPath: "./cleanExpiredDataRule.vue" },
  "012": { label: "components.cleanRule.longFieldTruncator", componentPath: "./longFieldTruncator.vue" },
  "019": { label: "components.cleanRule.emptyRule", componentPath: "./emptyRule.vue" },
  "022": { label: "components.cleanRule.fieldToLower", componentPath: "./emptyRule.vue" },
  "007": { label: "components.cleanRule.dateFormatter", componentPath: "./dateFormatter.vue" },
  "009": { label: "components.cleanRule.trimSpace", componentPath: "./trimSpaceRule.vue" },
  EMPTY: { label: "components.cleanRule.placeholder", componentPath: "./emptyRule.vue" },
}

/**
 * Get rule meta information
 * @param {string} key rule code (ruleCode)
 * @returns {object|null}
 */
export function getRuleConfig(key) {
  return ruleRegistry[key] || null
}

/**
 * Get the rule asynchronous component
 * @param {string} key rule code (ruleCode)
 * @returns {import('vue').DefineComponent|null}
 */
export function getRuleComponent(key) {
  const cfg = getRuleConfig(key)
  const path = cfg?.componentPath
  if (!path || !modules[path]) return null
  return defineAsyncComponent(() => modules[path]())
}
