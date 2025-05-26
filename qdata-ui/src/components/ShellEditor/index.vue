<template>
  <Codemirror
    v-model:value="code"
    :options="cmOptions"
    ref="editor"
    :readonly="readonly"
    :height="height"
    :width="width"
    @ready="onReady"
    @blur="onInput"
  />
</template>

<script setup>
import { ref, computed, nextTick } from "vue";
import Codemirror from "codemirror-editor-vue3";
// 引入css文件
import "codemirror/lib/codemirror.css";
// 引入主题
import "codemirror/theme/idea.css";
// 引入语言模式
import "codemirror/mode/shell/shell.js";
// 代码提示功能
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/anyword-hint";
import "codemirror/addon/display/placeholder.js";
// 定义 props
const props = defineProps({
  readonly: {
    type: Boolean,
    default: false, // 默认为false，表示可编辑
  },
  width: {
    type: String,
    default: "100%", // 默认宽度为100%
  },
  height: {
    type: String,
    default: "300px",
  },
  placeholder: {
    type: String,
    default: "",
  },
});

// 定义响应式变量
const code = ref("");

// 计算属性动态设置 `readOnly`
const cmOptions = computed(() => ({
  mode: "shell", // 语言及语法模式
  theme: "default", // 主题
  lineNumbers: true, // 显示行号
  lineWrapping: true, // 软换行
  tabSize: 4, // tab宽度
  readOnly: props.readonly ? "nocursor" : false, // 只读模式
  placeholder: props.placeholder,
  hintOptions: {
    zindex: 9999, // 确保足够高
    completeSingle: false, // 避免自动填充
    tables: {
      BPSuv: ["DocEntry", "Subject", "DocStatus", "Remarks"],
      BPSuvA: ["DocEntry", "LineNum", "Question", "QstType"],
      BPSuvB: ["DocEntry", "LineNum", "UserID", "UserName"],
    },
  },
}));
const emit = defineEmits();
const onInput = () => {
  code.value;
  emit("changeTextarea", code.value); // 手动更新父组件的值
};
// 初始化时绑定事件
const onReady = (editor) => {
  editor.on("inputRead", (cm, location) => {
    if (/[a-zA-Z]/.test(location.text[0])) {
      cm.showHint();
    }
  });
  nextTick(() => {
    editor.refresh(); // 确保弹窗打开后，CodeMirror 正确刷新大小
  });
};
const clear = () => {
  code.value = "";
  emit("changeTextarea", ""); // 同步通知父组件
};
defineExpose({ clear });
</script>
<style>
.CodeMirror-hints {
  z-index: 9999 !important; /* 确保足够高，避免被遮挡 */
  position: absolute !important;
}
</style>
