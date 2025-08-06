<template>
  <Codemirror v-model:value="code" :options="cmOptions" ref="editor" :readonly="readonly" :height="height"
    :width="width" @ready="onReady" @blur="onInput" />
</template>

<script setup>
import { ref, computed, nextTick, watch } from "vue";
import Codemirror from "codemirror-editor-vue3";

// 引入核心样式与插件
import "codemirror/lib/codemirror.css";
import "codemirror/theme/idea.css";
import "codemirror/mode/sql/sql.js";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";
import "codemirror/addon/display/placeholder.js";

// 接收 props
const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  readonly: {
    type: Boolean,
    default: false,
  },
  width: {
    type: String,
    default: "100%",
  },
  height: {
    type: String,
    default: "300px",
  },
  placeholder: {
    type: String,
    default: "select * FROM log_table where id>${id}",
  },
});

// 发射事件
const emit = defineEmits(["update:modelValue", "changeTextarea"]);

// 本地绑定变量
const code = ref(props.modelValue);

// 监听外部传入的 v-model 值变化，更新内部 code
watch(
  () => props.modelValue,
  (val) => {
    if (val !== code.value) {
      code.value = val;
    }
  }
);

// 监听内部 code 的变化，同步更新 v-model
watch(code, (val) => {
  emit("update:modelValue", val);
});

// 代码变化时的处理
const onInput = () => {
  emit("changeTextarea", code.value);
};

// Codemirror 配置项
const cmOptions = computed(() => ({
  mode: "text/x-sql",
  theme: "default",
  lineNumbers: true,
  lineWrapping: true,
  tabSize: 4,
  readOnly: props.readonly ? "nocursor" : false,
  placeholder: props.placeholder,
  hintOptions: {
    zindex: 9999,
    completeSingle: false,
    tables: {
      BPSuv: ["DocEntry", "Subject", "DocStatus", "Remarks"],
      BPSuvA: ["DocEntry", "LineNum", "Question", "QstType"],
      BPSuvB: ["DocEntry", "LineNum", "UserID", "UserName"],
    },
  },
}));

// 初始化提示
const onReady = (editor) => {
  editor.on("inputRead", (cm, location) => {
    if (/[a-zA-Z]/.test(location.text[0])) {
      cm.showHint();
    }
  });
  nextTick(() => {
    editor.refresh();
  });
};

// 提供方法供父组件调用
const clear = () => {
  code.value = "";
  emit("changeTextarea", "");
};

defineExpose({ clear });
</script>

<style>
.CodeMirror-hints {
  z-index: 9999 !important;
  position: absolute !important;
}
</style>
