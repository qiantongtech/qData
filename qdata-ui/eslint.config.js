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
     * "off" or 0 ==> Turn off the rule
     * "warn" or 1 ==> turns on the rule as a warning (does not affect code execution)
     * "error" or 2 ==> rule as an error (the code cannot be executed, the interface reports an error)
     */
    rules: {
      //  eslint (https:// eslint.nodejs.cn/docs/latest/rules)
      //  requires use of let or const instead of var
      "no-var": "error",
      //  Multiple blank lines are not allowed
      "no-multiple-empty-lines": ["error", { max: 1 }],
      //  Variables declared using the let keyword but never reallocated after the initial assignment require const
      "prefer-const": "off",
      //  It is forbidden to use functions/classes/variables before they are defined
      "no-use-before-define": "off",

      //  vue (https:// eslint.vuejs.org/rules)
      //  Prevents variables <template> used by <script setup> from being marked as unused. This rule is only effective when the no-unused-vars rule is enabled.
      "vue/script-setup-uses-vars": "warn",
      //  Enforce v-slot directive style
      "vue/v-slot-style": "error",
      //  Not allowed to change component props
      "vue/no-mutating-props": "error",
      //  Force specific case for custom event names
      "vue/custom-event-name-casing": "error",
      //  Require or disallow newline before closing bracket of label
      "vue/html-closing-bracket-newline": "error",
      //  Enforce property naming style for custom components in templates: my-prop="prop"
      "vue/attribute-hyphenation": "off",
      //  vue api usage order, enforce attribute order
      "vue/attributes-order": "off",
      //  Use of v-html is prohibited
      "vue/no-v-html": "off",
      //  This rule requires that a default value must be provided for each prop that is required
      "vue/require-default-prop": "error",
      //  Require component names to always be "-" linked words
      "vue/multi-word-component-names": "off",
      //  Disable destructuring of props passed to setup
      "vue/no-setup-props-destructure": "off",

      //  vue
      // Turn off operator wrapping rules.
      "vue/operator-linebreak": "off",
      // Turns off the new line requirement for single-line HTML element content.
      "vue/singleline-html-element-content-newline": "off",
      // Turn off rules for the v-model parameter.
      "vue/no-v-model-argument": "off",
      // Turn off prop type requirement rules.
      "vue/require-prop-types": "off",
      // Close HTML self-closing rules.
      "vue/html-self-closing": "off",
      // Turn off attribute quoting rules.
      "vue/quote-props": "off",
      // Turn off irregular whitespace checking.
      "vue/no-irregular-whitespace": "off",
      // Turn off prop name case rules.
      "vue/prop-name-casing": "off",
      // Turn off HTML indentation rules.
      "vue/html-indent": "off",
      // Turn off reserved component name checking.
      "vue/no-reserved-component-names": "off",

      //  eslint
      // Turn off JSX Turn off tag position rules.
      "style/jsx-closing-tag-location": "off",
      // Turn off collation of import statements.
      "import/order": "off",
      // Turn off preference rules for process global variables.
      "node/prefer-global/process": "off",
      // Turn off unused import variable rules.
      "unused-imports/no-unused-vars": "off",
      // Turn off the semicolon rule at the end of a statement.
      "style/semi": "off",
      // Turn off indentation rules.
      "style/indent": "off",
      // Turn off attribute quoting rules.
      "style/quote-props": "off",
      // Turn off brace style rules.
      "style/brace-style": "off",
      // Turn off bracketing rules for arrow function arguments.
      "style/arrow-parens": "off",
      // Turn off indentation rules for binary operators.
      "style/indent-binary-ops": "off",
      // Turn off operator wrapping rules.
      "style/operator-linebreak": "off",
      // Turn off member separator style rules.
      "style/member-delimiter-style": "off",
      // Turn off checking for undefined variables.
      "no-undef": "off",
      // Turn off the rule prohibiting direct use of the new keyword.
      "no-new": "off",
      // Reassignment of function parameters is prohibited.
      "no-param-reassign": "error",
      // Turn off the rule prohibiting the use of console.
      "no-console": "off",
      // Turn off the irregular whitespace checking rule.
      "no-irregular-whitespace": "off",
      // Turn off the unicorn plugin's numeric literal case rules.
      "unicorn/number-literal-case": "off",
      // Turn off TypeScript's ban on @ts- annotations.
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
