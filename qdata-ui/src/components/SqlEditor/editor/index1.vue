<template>
  <div ref="monacoDom" class="json-editor" @editor-query="handleQuery"></div>
</template>
<script setup>
import beautify from "js-beautify";
import * as monaco from "monaco-editor";
// 注册快捷键-右键菜单
import { registerEditorKeyBindingAndAction } from "@/utils/functionCodemirror";
// flinksql语法
import { FlinkSQLLanguage } from "../languages/flinksql/index";
import { LogLanguage } from "../languages/javalog/index";

const props = defineProps({
  // 绑定值
  modelValue: {
    type: String,
    default: `
-- 查询已激活用户(MySQL)
SELECT
    id,
    username,
    email,
    status,
    created_at
FROM
    users
WHERE
    status = 1
ORDER BY
    created_at DESC
LIMIT 10;
    `.trim(),
  },
  // 只读
  readOnly: {
    type: Boolean,
    default: false,
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
    default: "flinksql",
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
    default: "on",
  },
});
const emits = defineEmits(["update:modelValue", "change", "ready", "query"]);
const monacoDom = ref(null);
let monacoInstance = null;
function handleQuery(sql) {
  console.log("触发查询，SQL内容:",);
  emits("query", sql.detail.value);
}
watch(
  () => props.modelValue,
  (newValue) => {
    const value = monacoInstance?.getValue();
    if (newValue !== value) {
      monacoInstance?.setValue(props.modelValue);
    }
  }
);
/**
 * 获取编辑器内容（优先选中，没有则取全文）
 */
function getEditorSelectedOrAll() {
  if (!monacoInstance) return "";
  const selection = monacoInstance.getSelection();
  if (selection && !selection.isEmpty()) {
    return monacoInstance.getModel().getValueInRange(selection);
  }
  return monacoInstance.getValue();
}
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
  // autoIndent: "none", //  auto indent
  // fontSize: 14, //  font size
  // automaticLayout: true, //  auto layout
  // scrollBeyondLastLine: false, //is scroll beyond the last line
  // autoDetectHighContrast: true, // auto detect high contrast
  // ---------------------
  formatOnPaste: true, // 粘贴时格式化
  mouseWheelZoom: true, // 鼠标滚轮缩放
  screenReaderAnnounceInlineSuggestion: true, // 屏幕阅读器提示
  automaticLayout: true, // 自动布局，编辑器自适应大小
  theme: "vs", // 官方自带三种主题vs, hc-black, or vs-dark
  minimap: {
    enabled: true, // 是否开启右侧代码小窗
  },
  codeLens: true, // 代码镜头
  colorDecorators: true, // 颜色装饰器
  parameterHints: {
    enabled: true,
  },
  selectOnLineNumbers: true, //显示行号
  quickSuggestionsDelay: 100, //代码提示延时
  autoIndent: true, //自动布局
  wrappingStrategy: "advanced",
  scrollBeyondLastLine: false,
  autoDetectHighContrast: true, // auto detect high contrast
  overviewRulerLanes: 0,
  scrollbar: {
    alwaysConsumeMouseWheel: false,
    useShadows: false,
    vertical: "visible",
    horizontal: "visible",
    verticalScrollbarSize: 8,
    horizontalScrollbarSize: 8,
    arrowSize: 30,
  },
  hover: {
    enabled: true,
    above: false,
  },
  renderLineHighlight: "none",
  fontSize: 14,
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
    tabCompletion: "on", // tab 补全
    cursorSmoothCaretAnimation: false, // 光标动画
    screenReaderAnnounceInlineSuggestion: true, // 屏幕阅读器提示
    formatOnPaste: true, // 粘贴时格式化
    mouseWheelZoom: true, // 鼠标滚轮缩放
    autoClosingBrackets: "always", // 自动闭合括号
    autoClosingOvertype: "always", // 用于在右引号或括号上键入的选项
    autoClosingQuotes: "always", // 自动闭合引号
    showUnused: true, // 显示未使用的代码
    unfoldOnClickAfterEndOfLine: true, // 控制在折叠线之后单击空内容是否会展开该线
    showFoldingControls: "always", // 代码折叠控件 'always' | 'mouseover' | 'never'
    automaticLayout: true, // 自动布局
    glyphMargin: true, // 字形边缘
    formatOnType: true, // 代码格式化
    // columnSelection: true, // 列选择
    wrappingIndent: props.language === "yaml" || props.language === "yml" || props.language === "json" ? "indent" : "none",
    inlineSuggest: {
      enabled: true,
      showToolbar: "always",
      keepOnBlur: false,
      allowQuickSuggestions: true,
      showOnAllSymbols: true,
    },
    // inlineSuggestionVisible: true,
    quickSuggestions: props.enableSuggestions,
    guides: {
      bracketPairs: true,
    },
    bracketPairColorization: {
      enabled: true,
      independentColorPoolPerBracketType: true,
    },
    foldingRanges: true,
    inlineCompletionsAccessibilityVerbose: true,
    smartSelect: {
      selectLeadingAndTrailingWhitespace: true,
      selectSubwords: true,
    },
    suggest: {
      quickSuggestions: props.enableSuggestions,
      showStatusBar: true,
      preview: props.enableSuggestionPreview,
      previewMode: "subword",
      showInlineDetails: true,
      showMethods: true,
      showFunctions: true,
      showConstructors: true,
      showFields: true,
      showEvents: true,
      showOperators: true,
      showClasses: true,
      showModules: true,
      showStructs: true,
      showInterfaces: true,
      showProperties: true,
      showUnits: true,
      showValues: true,
      showConstants: true,
      showEnums: true,
      showEnumMembers: true,
      showKeywords: true,
      showWords: true,
      showFolders: true,
      showReferences: true,
      showSnippets: true,
    },
    scrollbar: {
      useShadows: false,
      vertical: "visible",
      horizontal: "visible",
      verticalScrollbarSize: 8,
      horizontalScrollbarSize: 8,
      arrowSize: 30,
    },
    wordWrap: props.autoWrap,
    autoDetectHighContrast: true,
    lineNumbers: props.lineNumbers,
    readOnly: props.readOnly,
    value:`
-- ============================================
-- MySQL 示例：查询最近 30 天内激活用户
-- ============================================
SELECT
    id,
    username,
    email,
    created_at
FROM
    users
WHERE
    status = 1
    AND created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
ORDER BY
    created_at DESC;

-- ============================================
-- 达梦 DM8 示例：查询最近 30 天内激活用户
-- ============================================
SELECT
    ID,
    USERNAME,
    EMAIL,
    CREATED_AT
FROM
    USERS
WHERE
    STATUS = 1
    AND CREATED_AT >= ADD_DAYS(TRUNC(SYSDATE), -30)
ORDER BY
    CREATED_AT DESC;
    `.trim(),
    language: props.language,
    ...props.config,
  });
  // 注册快捷键及右键菜单
  registerEditorKeyBindingAndAction(monacoInstance);
  // 注册自定义语言 monacoLanguages, monacoEditor, registerCompletion: 代码提示
  FlinkSQLLanguage(monaco.languages, monaco.editor, true);
  LogLanguage(monaco.languages);
  // 注册主题色
  convertCodeEditTheme(monaco.editor);
  monaco.editor.setTheme("light");

  monacoInstance.onDidChangeModelContent(() => {
    emits("update:modelValue", monacoInstance?.getValue());
    emits("change", monacoInstance?.getValue());
  });
  emits("ready", monacoInstance, monaco);
  //   代码提示
  //   editorDidMountChange(monacoInstance, monaco);
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
          "editor.background": "#FCFCFC", // 编辑器背景色
          "editorLineNumber.foreground": "#999999", // 行号颜色
          "editorCursor.foreground": "#333333", // 光标颜色
          "editor.selectionBackground": "#D6EBFF", // 选中背景
          "editorLineNumber.activeForeground": "#000000", // 当前行号颜色
          "editorIndentGuide.background": "#E0E0E0", // 缩进参考线
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
function reloadCompilation(monacoIns, segmentedWords) {
  provider.dispose();
  provider = monacoIns.languages.registerCompletionItemProvider(props.language, {
    provideCompletionItems: (model, position, context) => {
      const allSuggestions = memoizedBuildAllSuggestionsCallback(model, position);

      // editor context
      const wordSuggestions = segmentedWords.map((word) => ({
        key: word,
        label: {
          label: word,
          detail: "",
          description: "",
        },
        kind: monaco.languages.CompletionItemKind.Text,
        insertText: word,
      }));
      let completionList = buildAllSuggestionsToEditor(model, position, wordSuggestions);
      const suggestions = allSuggestions.then((res) => {
        return {
          // eslint-disable-next-line no-unsafe-optional-chaining
          suggestions: [...(res?.suggestions ?? []), ...completionList?.suggestions],
        };
      });

      // 获取当前光标行的文本
      const lineText = model.getLineContent(position.lineNumber) ?? "";
      context.triggerKind = monacoIns.languages.CompletionTriggerKind.TriggerCharacter;
      // 设置以当前光标行的文本为触发字符
      context.triggerCharacter = lineText;
      return suggestions;
    },
    resolveCompletionItem: (item) => {
      return {
        ...item,
        detail: item.detail,
      };
    },
  });
}

/**
 *  editorDidMount
 * @param {editor.IStandaloneCodeEditor} editor
 * @param monacoIns
 */
const editorInstance = ref(null);
let provider = {
  dispose: () => { },
};
const editorDidMountChange = (editor, monacoIns) => {
  editorInstance.current = editor;
  monacoInstance.current = monacoIns;

  let timeoutId = null;
  editor.onDidChangeModelContent(() => {
    if (timeoutId !== null) {
      return;
    }

    timeoutId = setTimeout(() => {
      timeoutId = null;
    }, 3000);

    const model = editor.getModel();
    if (model) {
      const segmenter = new Intl.Segmenter("en", { granularity: "word" });
      const segments = segmenter.segment(model.getValue());
      const segmentedWords = [];
      for (const segment of segments) {
        const trimmedSegment = segment.segment.trim().replace(/\n/g, "");
        if (trimmedSegment.length > 1) {
          segmentedWords.push(segment.segment);
        }
      }
      const uniqueSegmentedWords = Array.from(new Set(segmentedWords));
      reloadCompilation(monacoIns, uniqueSegmentedWords);
    }
  });

  if (props.enableSuggestions) {
    reloadCompilation(monacoInstance.current, []);
  }
  editor.layout();
  editor.focus();
};
defineExpose({
  format,
  focus,
  getEditorSelectedOrAll
});
</script>
<style lang="scss">
.json-editor {
  width: 100%;
  height: calc(100%);
  padding: 10px 0;
  background: #FCFCFC; // 浅色背景;
}

:deep(.monaco-scrollable-element > .scrollbar > .slider) {
  background: var(--el-color-primary) !important;
}
</style>
