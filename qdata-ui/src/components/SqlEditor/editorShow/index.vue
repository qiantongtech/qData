<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div ref="monacoDom" class="read-json-editor monaco-editor-container"></div>
</template>
<script setup>
import beautify from "js-beautify";
import * as monaco from "monaco-editor";
// flinksql syntax
import { FlinkSQLLanguage } from "../languages/flinksql/index";
import { LogLanguage } from "../languages/javalog/index";

const props = defineProps({
  // Binding value
  modelValue: {
    type: String,
    default: "",
  },
  // read only
  readOnly: {
    type: Boolean,
    default: true,
  },
  // Configuration
  config: {
    type: Object,
    default() {
      return {};
    },
  },
  // Language
  language: {
    type: String,
    default: "sql",
  },
  // Enable suggestions
  enableSuggestions: {
    type: Boolean,
    default: true,
  },
  // Enable suggestion preview
  enableSuggestionPreview: {
    type: Boolean,
    default: true,
  },
  autoWrap: {
    type: String,
    default: "on",
  },
  lineNumbers: {
    type: String,
    default: "off",
  },
  enableAutoScroll: {
    type: Boolean,
    default: false,
  },
  enableMiniMap: {
    type: Boolean,
    default: false,
  },
});
const emits = defineEmits(["update:modelValue", "change", "ready"]);
const monacoDom = ref(null);
let monacoInstance = null;

watch(
  () => props.modelValue,
  (newValue) => {
    const value = monacoInstance?.getValue();
    if (newValue !== value) {
      monacoInstance?.setValue(props.modelValue);
    }
  }
);
watch(
  () => props.readOnly,
  (readOnly) => {
    monacoInstance?.updateOptions({
      readOnly,
    });
  }
);
const defaultConfig = {
  renderSideBySide: false, //  side by side
  autoIndent: "none", //  auto indent
  fontSize: 14, //  font size
  automaticLayout: true, //  auto layout
  scrollBeyondLastLine: false, //is scroll beyond the last line
  autoDetectHighContrast: true, // auto detect high contrast
};
onMounted(() => {
  monaco.languages.json.jsonDefaults.setDiagnosticsOptions({
    allowComments: true,
    validate: true,
    trailingCommas: "ignore",
    schemaValidation: "warning",
  });
  monaco.languages.json.jsonDefaults.setModeConfiguration({
    completionItems: false,
    tokens: true,
    colors: true,
    foldingRanges: true,
    diagnostics: true,
  });
  monacoInstance = monaco.editor.create(monacoDom.value, {
    ...defaultConfig,
    // scrollBeyondLastLine: props.enableAutoScroll,//Invalid
    readOnly: true,
    glyphMargin: false,
    wordWrap: props.autoWrap,
    autoDetectHighContrast: true,
    selectOnLineNumbers: true,
    fixedOverflowWidgets: true,
    autoClosingDelete: "always",
    lineNumbers: props.lineNumbers,
    minimap: {
      enabled: false, // Whether to open the small code window on the right
    },
    // console
    scrollbar: {
      // Subtle shadows to the left & top. Defaults to true.
      useShadows: false,

      // Render vertical arrows. Defaults to false.
      // verticalHasArrows: true,
      // Render horizontal arrows. Defaults to false.
      // horizontalHasArrows: true,

      // Render vertical scrollbar.
      // Accepted values: 'auto', 'visible', 'hidden'.
      // Defaults to 'auto'
      vertical: "visible",
      // Render horizontal scrollbar.
      // Accepted values: 'auto', 'visible', 'hidden'.
      // Defaults to 'auto'
      horizontal: "visible",
      verticalScrollbarSize: 8,
      horizontalScrollbarSize: 8,
      arrowSize: 30,
    },
    value: props.modelValue,
    language: props.language,
    ...props.config,
  });
  // Register javalog language
  FlinkSQLLanguage(monaco.languages, monaco.editor, true);
  LogLanguage(monaco.languages);
  // Register theme color
  convertCodeEditTheme(monaco.editor);
  monaco.editor.setTheme("light");

  monacoInstance.onDidChangeModelContent(() => {
    emits("update:modelValue", monacoInstance?.getValue());
    emits("change", monacoInstance?.getValue());
    if (props.enableAutoScroll) {
      const lineCount = monacoInstance.getModel()?.getLineCount();
      if (lineCount > 20) {
        monacoInstance.revealLine(lineCount);
      } else {
        monacoInstance.revealLine(1);
      }
    }
  });
  emits("ready", monacoInstance, monaco);
});
onActivated(() => {
  monacoInstance?.focus();
});
onBeforeUnmount(() => {
  monacoInstance?.dispose();
});
const format = () => {
  const formatStr = beautify(props.modelValue, { indent_size: 4 });
  monacoInstance?.setValue(formatStr);
};
const focus = () => {
  monacoInstance?.focus();
};
const CODE_EDIT_THEME = {
  LIGHT: "light",
  DARK: "vs-dark",
};
function convertCodeEditTheme(editorInstance) {
  if (!editorInstance) {
    // eslint-disable-next-line no-param-reassign
    editorInstance = editor;
  }
  if (editorInstance === undefined) {
    return CODE_EDIT_THEME.LIGHT;
  } else {
    /**
     * Define bright colors, cover vs themes, and add expansion rules
     */
    editorInstance?.defineTheme?.(CODE_EDIT_THEME.LIGHT, {
      base: "vs", // Specify the base theme, optional values: 'vs', 'vs-dark', 'hc-black', base theme
      inherit: true, // Whether to inherit theme configuration
      rules: [
        // Note that the default is not modified because it inherits the parent theme and only adds your own defined ones. Otherwise, the default ones will be overwritten, resulting in inconsistent editor styles.
        { token: "custom-info", foreground: "#808080" },
        { token: "custom-thread", foreground: "#9fa19f" },
        { token: "custom-class", foreground: "#1060d9" },
        { token: "custom-error", foreground: "#ff0000", fontStyle: "bold" },
        { token: "custom-warning", foreground: "#FFA500", fontStyle: "bold" },
        { token: "custom-date", foreground: "#008800" },
        { token: "custom-process", foreground: "#07f313" },
      ],
      colors: {
        "editor.background": "#fcfcfc",
        "minimap.selectionHighlight": "#FFFFFF",
      },
      encodedTokensColors: [],
    });

    /**
     * Define dark color, cover vs-dark theme, add extension rules
     */
    editorInstance?.defineTheme?.(CODE_EDIT_THEME.DARK, {
      base: "vs-dark", // Specify the base theme, optional values: 'vs', 'vs-dark', 'hc-black', base theme
      inherit: true, // Whether to inherit theme configuration
      rules: [
        // Note that the default is not modified because it inherits the parent theme and only adds your own defined ones. Otherwise, the default ones will be overwritten, resulting in inconsistent editor styles.
        { token: "custom-info", foreground: "#008800" },
        { token: "custom-thread", foreground: "#9fa19f" },
        { token: "custom-class", foreground: "#1060d9" },
        { token: "custom-error", foreground: "#ff0000", fontStyle: "bold" },
        { token: "custom-warning", foreground: "#FFA500", fontStyle: "bold" },
        { token: "custom-date", foreground: "#008800" },
        { token: "custom-process", foreground: "#07f313" },
      ],
      colors: {},
      encodedTokensColors: [],
    });
  }

  // const theme = getLocalTheme();
  // switch (theme) {
  //   case THEME.dark:
  //     return CODE_EDIT_THEME.DARK;
  //   case THEME.light:
  //     return CODE_EDIT_THEME.LIGHT;
  //   default:
  //     return CODE_EDIT_THEME.LIGHT;
  // }
  return CODE_EDIT_THEME.LIGHT;
}
defineExpose({
  format,
  focus,
});
</script>
<style lang="scss" scoped>
.read-json-editor {
  width: 100%;
  height: 100%;
  background-color: #FCFCFC;

  :deep(.monaco-scrollable-element > .scrollbar > .slider) {
    background: var(--el-color-primary) !important;
  }

  :deep(.el-tree) {
    background-color: #FCFCFC;
  }

}
</style>
