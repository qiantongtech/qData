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
  <div ref="monacoDom" class="json-editors"></div>
</template>
<script setup>
import beautify from "js-beautify";
import * as monaco from "monaco-editor";
// Register shortcut keys-right-click menu
import { registerEditorKeyBindingAndAction } from "@/utils/function";
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
    default: false,
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
    default: "flinksql",
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
    default: "on",
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
  // autoIndent: "none", //  auto indent
  // fontSize: 14, //  font size
  // automaticLayout: true, //  auto layout
  // scrollBeyondLastLine: false, //is scroll beyond the last line
  // autoDetectHighContrast: true, // auto detect high contrast
  // ---------------------
  formatOnPaste: true, // Format when pasting
  mouseWheelZoom: true, // Mouse wheel zoom
  screenReaderAnnounceInlineSuggestion: true, // Screen reader tips
  automaticLayout: true, // Automatic layout, editor adaptive size
  theme: "vs", // There are three official themes: vs, hc-black, or vs-dark
  minimap: {
    enabled: false, // Whether to open the small code window on the right
  },
  codeLens: true, // code lens
  colorDecorators: true, // color decorator
  parameterHints: {
    enabled: true,
  },
  selectOnLineNumbers: true, //Show line number
  quickSuggestionsDelay: 100, //Code prompt delay
  autoIndent: true, //autolayout
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
    tabCompletion: "on", // tab completion
    cursorSmoothCaretAnimation: false, // Cursor animation
    screenReaderAnnounceInlineSuggestion: true, // Screen reader tips
    formatOnPaste: true, // Format when pasting
    mouseWheelZoom: true, // Mouse wheel zoom
    autoClosingBrackets: "always", // Automatic closing brackets
    autoClosingOvertype: "always", // Options for typing around closing quotes or brackets
    autoClosingQuotes: "always", // Automatic closing quotes
    showUnused: true, // Show unused code
    unfoldOnClickAfterEndOfLine: true, // Controls whether clicking empty content after a collapsed line expands the line
    showFoldingControls: "always", // Code folding control 'always' | 'mouseover' | 'never'
    automaticLayout: true, // autolayout
    glyphMargin: true, // glyph edge
    formatOnType: true, // Code formatting
    // columnSelection: true, // column selection
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
    value: props.modelValue || `
-- 在这里输入内容
`.trim(),
    language: props.language,
    ...props.config,
  });
  // Register shortcut keys and right-click menu
  registerEditorKeyBindingAndAction(monacoInstance);
  // Register custom languages monacoLanguages, monacoEditor, registerCompletion: code tips
  FlinkSQLLanguage(monaco.languages, monaco.editor, true);
  LogLanguage(monaco.languages);
  // Register theme color
  convertCodeEditTheme(monaco.editor);
  monaco.editor.setTheme("light");

  monacoInstance.onDidChangeModelContent(() => {
    emits("update:modelValue", monacoInstance?.getValue());
    emits("change", monacoInstance?.getValue());
  });
  emits("ready", monacoInstance, monaco);
  //   Code tips
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
        "editor.background": "#FFFFFF",
        "minimap.background": "#FFFFFF",
        "minimap.selectionHighlight": "#FFFFFF",
        "minimap.errorHighlight": "#FFFFFF",
        "minimap.warningHighlight": "#FFFFFF",
        "minimapSlider.background": "#FFFFFF",
        "minimapSlider.hoverBackground": "#FFFFFF",
        "minimapSlider.activeBackground": "#FFFFFF",
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

      // Get the text of the current cursor line
      const lineText = model.getLineContent(position.lineNumber) ?? "";
      context.triggerKind = monacoIns.languages.CompletionTriggerKind.TriggerCharacter;
      // Set the text of the current cursor line as the trigger character
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
});
</script>
<style lang="scss" scoped>
.json-editors {
  width: 100%;
  height: 100%;

  :deep(.monaco-editor-background) {
    background: #FFFFFF !important;
  }

  :deep(.monaco-editor .margin) {
    background: #FFFFFF !important;
  }

  // :deep(.monaco-editor .minimap) {
  //   background-color: #ffffff !important;
  // }

}
</style>
