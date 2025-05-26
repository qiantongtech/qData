import js from "@eslint/js";
import pluginVue from "eslint-plugin-vue";
import globals from "globals";

export default [
  js.configs.recommended,
  ...pluginVue.configs["flat/essential"],
  {
    name: "app/files-to-lint",
    files: ["**/*.{js,mjs,jsx,vue}"],
    languageOptions: {
      ecmaVersion: "latest",
      sourceType: "module",
      globals: globals.node,
    },
    /**
     * "off" 或 0    ==>  关闭规则
     * "warn" 或 1   ==>  打开的规则作为警告（不影响代码执行）
     * "error" 或 2  ==>  规则作为一个错误（代码不能执行，界面报错）
     */
    rules: {
      //  eslint (https:// eslint.nodejs.cn/docs/latest/rules)
      //  要求使用 let 或 const 而不是 var
      "no-var": "error",
      //  不允许多个空行
      "no-multiple-empty-lines": ["error", { max: 1 }],
      //  使用 let 关键字声明但在初始分配后从未重新分配的变量，要求使用 const
      "prefer-const": "off",
      //  禁止在 函数/类/变量 定义之前使用它们
      "no-use-before-define": "off",

      //  vue (https:// eslint.vuejs.org/rules)
      //  防止<script setup>使用的变量<template>被标记为未使用，此规则仅在启用该 no-unused-vars 规则时有效
      "vue/script-setup-uses-vars": "warn",
      //  强制执行 v-slot 指令样式
      "vue/v-slot-style": "error",
      //  不允许改变组件 prop
      "vue/no-mutating-props": "error",
      //  为自定义事件名称强制使用特定大小写
      "vue/custom-event-name-casing": "error",
      //  在标签的右括号之前要求或禁止换行
      "vue/html-closing-bracket-newline": "error",
      //  对模板中的自定义组件强制执行属性命名样式：my-prop="prop"
      "vue/attribute-hyphenation": "off",
      //  vue api使用顺序，强制执行属性顺序
      "vue/attributes-order": "off",
      //  禁止使用 v-html
      "vue/no-v-html": "off",
      //  此规则要求为每个 prop 为必填时，必须提供默认值
      "vue/require-default-prop": "error",
      //  要求组件名称始终为 “-” 链接的单词
      "vue/multi-word-component-names": "off",
      //  禁止解构 props 传递给 setup
      "vue/no-setup-props-destructure": "off",

      //  vue
      // 关闭操作符换行规则。
      "vue/operator-linebreak": "off",
      // 关闭单行 HTML 元素内容的新行要求。
      "vue/singleline-html-element-content-newline": "off",
      // 关闭对 v-model 参数的规则。
      "vue/no-v-model-argument": "off",
      // 关闭 prop 类型要求规则。
      "vue/require-prop-types": "off",
      // 关闭 HTML 自闭合规则。
      "vue/html-self-closing": "off",
      // 关闭属性引号规则。
      "vue/quote-props": "off",
      // 关闭不规则空白检查。
      "vue/no-irregular-whitespace": "off",
      // 关闭 prop 名称大小写规则。
      "vue/prop-name-casing": "off",
      // 关闭 HTML 缩进规则。
      "vue/html-indent": "off",
      // 关闭保留组件名称检查。
      "vue/no-reserved-component-names": "off",

      //  eslint
      // 关闭 JSX 关闭标签位置规则。
      "style/jsx-closing-tag-location": "off",
      // 关闭 import 语句的排序规则。
      "import/order": "off",
      // 关闭对 process 全局变量的偏好规则。
      "node/prefer-global/process": "off",
      // 关闭未使用的导入变量规则。
      "unused-imports/no-unused-vars": "off",
      // 关闭语句末尾分号规则。
      "style/semi": "off",
      // 关闭缩进规则。
      "style/indent": "off",
      // 关闭属性引号规则。
      "style/quote-props": "off",
      // 关闭大括号风格规则。
      "style/brace-style": "off",
      // 关闭箭头函数参数的括号规则。
      "style/arrow-parens": "off",
      // 关闭二元运算符的缩进规则。
      "style/indent-binary-ops": "off",
      // 关闭操作符换行规则。
      "style/operator-linebreak": "off",
      // 关闭成员分隔符风格规则。
      "style/member-delimiter-style": "off",
      // 关闭对未定义变量的检查。
      "no-undef": "off",
      // 关闭禁止直接使用 new 关键字的规则。
      "no-new": "off",
      // 禁止重新分配函数参数。
      "no-param-reassign": "error",
      // 关闭禁止使用 console 的规则。
      "no-console": "off",
      // 关闭不规则空白检查规则。
      "no-irregular-whitespace": "off",
      // 关闭 unicorn 插件的数字字面量大小写规则。
      "unicorn/number-literal-case": "off",
      // 关闭 TypeScript 对 @ts- 注释的禁止规则。
      "ts/ban-ts-comment": "off",
    },
  },
  {
    name: "app/files-to-ignore",
    ignores: [
      "**/dist/**",
      "**/build/*.js",
      "**/src/assets/**",
      "**/public/**",
    ],
  },
];
