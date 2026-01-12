// 质量规则异步注册中心
// 用途：
// - 统一维护“规则类型键 → 子组件路径”的映射
// - 通过 import.meta.glob 与 defineAsyncComponent 按需加载子组件，降低首屏体积
// 扩展步骤：
// 1) 在 components/rule 目录新增子组件文件
// 2) 在 ruleRegistry 中追加键与 componentPath 的映射
// 3) 规则类型键需与后端/页面使用的 form.ruleType 保持一致
import { defineAsyncComponent } from "vue"
const modules = import.meta.glob("./**/*.vue")

export const ruleRegistry = {
  DECIMAL_PRECISION_VALIDATION: { label: "数值精度校验", componentPath: "./decimalScaleRule.vue" },
  CHARACTER_VALIDATION: { label: "字符串类型校验", componentPath: "./characterValidationRule.vue" },
  LENGTH_VALIDATION: { label: "字段长度范围校验", componentPath: "./lengthRangeRule.vue" },
  NUMERIC_RANGE_VALIDATION: { label: "数值字段范围校验", componentPath: "./numberRangeRule.vue" },
  ENUM_VALIDATION: { label: "枚举值校验", componentPath: "./enumRule.vue" },
  GROUP_FIELD_COMPLETENESS: { label: "字段组完整性校验", componentPath: "./combinerFieldUniqueRule.vue" },
  TIME_ORDER_VALIDATION: { label: "时间字段先后顺序校验", componentPath: "./timeOrderRule.vue" },
}

// 获取规则元信息：用于展示 label 等基础描述
export function getRuleConfig(key) {
  return ruleRegistry[key] || null
}

// 获取规则异步组件：
// - 若路径未在 modules 中出现（文件不存在或路径不匹配）返回 null
// - 使用 defineAsyncComponent 包裹以实现懒加载
export function getRuleComponent(key) {
  const cfg = getRuleConfig(key)
  const path = cfg?.componentPath
  if (!path || !modules[path]) return null
  return defineAsyncComponent(() => modules[path]())
}
