// Quality Rules Asynchronous Registration Center
// Purpose:
// - Uniformly maintain the mapping of "rule type key → subcomponent path"
// - Load subcomponents on demand through import.meta.glob and defineAsyncComponent to reduce the size of the first screen
// Extension steps:
// 1) Add subcomponent files in the components/rule directory
// 2) Add the mapping between key and componentPath in ruleRegistry
// 3) The rule type key needs to be consistent with the form.ruleType used by the backend/page
import { defineAsyncComponent } from "vue"
const modules = import.meta.glob("./**/*.vue")

export const ruleRegistry = {
  DECIMAL_PRECISION_VALIDATION: { label: '数值精度校验', componentPath: "./decimalScaleRule.vue" },
  CHARACTER_VALIDATION: { label: '字符串类型校验', componentPath: "./characterValidationRule.vue" },
  LENGTH_VALIDATION: { label: '字段长度范围校验', componentPath: "./lengthRangeRule.vue" },
  NUMERIC_RANGE_VALIDATION: { label: '数值字段范围校验', componentPath: "./numberRangeRule.vue" },
  ENUM_VALIDATION: { label: '枚举值校验', componentPath: "./enumRule.vue" },
  GROUP_FIELD_COMPLETENESS: { label: '字段组完整性校验', componentPath: "./combinerFieldUniqueRule.vue" },
  TIME_ORDER_VALIDATION: { label: '时间字段先后顺序校验', componentPath: "./timeOrderRule.vue" },
}

// Obtain rule meta-information: used to display basic descriptions such as label
export function getRuleConfig(key) {
  return ruleRegistry[key] || null
}

// Get the rule asynchronous component:
// - If the path does not appear in modules (the file does not exist or the path does not match), return null
// - Use defineAsyncComponent wrapper to achieve lazy loading
export function getRuleComponent(key) {
  const cfg = getRuleConfig(key)
  const path = cfg?.componentPath
  if (!path || !modules[path]) return null
  return defineAsyncComponent(() => modules[path]())
}
