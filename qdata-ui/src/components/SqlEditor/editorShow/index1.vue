<template>
  <div ref="monacoDom" class="read-json-editor monaco-editor-container"></div>
</template>
<script setup>
import beautify from "js-beautify";
import * as monaco from "monaco-editor";
// flinksql语法
import { FlinkSQLLanguage } from "../languages/flinksql/index";
import { LogLanguage } from "../languages/javalog/index";

const props = defineProps({
  // 绑定值
  modelValue: {
    type: String,
    default: "",
  },
  // 只读
  readOnly: {
    type: Boolean,
    default: true,
  },
  // 配置
  config: {
    type: Object,
    default() {
      return {};
    },
  },
  // 语言
  language: {
    type: String,
    default: "sql",
  },
  // 启用建议
  enableSuggestions: {
    type: Boolean,
    default: true,
  },
  // 启用建议预览
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
    // scrollBeyondLastLine: props.enableAutoScroll,//无效
    readOnly: true,
    glyphMargin: false,
    wordWrap: props.autoWrap,
    autoDetectHighContrast: true,
    selectOnLineNumbers: true,
    fixedOverflowWidgets: true,
    autoClosingDelete: "always",
    lineNumbers: props.lineNumbers,
    minimap: {
      enabled: false, // 是否开启右侧代码小窗
    },
    // 控制台
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
  // 注册javalog语言
  FlinkSQLLanguage(monaco.languages, monaco.editor, true);
  LogLanguage(monaco.languages);
  // 注册主题色
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
     * 定义亮色 覆盖vs主题,增加扩展规则
     */
    editorInstance?.defineTheme?.(CODE_EDIT_THEME.LIGHT, {
      base: "vs", // 指定基础主题 , 可选值: 'vs', 'vs-dark', 'hc-black' , base theme
      inherit: true, // 是否继承主题配置
      rules: [
        // 注意,默认的不做修改 因为上边继承了父主题, 只添加自己定义的 , 否则会覆盖默认的 , 导致编辑器样式不一致
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
     * 定义暗色 覆盖vs-dark主题,增加扩展规则
     */
    editorInstance?.defineTheme?.(CODE_EDIT_THEME.DARK, {
      base: "vs-dark", // 指定基础主题 , 可选值: 'vs', 'vs-dark', 'hc-black' , base theme
      inherit: true, // 是否继承主题配置
      rules: [
        // 注意,默认的不做修改 因为上边继承了父主题, 只添加自己定义的 , 否则会覆盖默认的 , 导致编辑器样式不一致
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
}
</style>
