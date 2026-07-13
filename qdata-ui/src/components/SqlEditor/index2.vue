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
  <Codemirror v-model:value="code" :options="cmOptions" ref="editor" :readonly="readonly" :height="height"
    :width="width" @ready="onReady" @blur="onInput" />
</template>

<script setup>
import { ref, computed, nextTick, watch } from "vue";
import Codemirror from "codemirror-editor-vue3";

// Introducing core styles and plug-ins
import "codemirror/lib/codemirror.css";
import "codemirror/theme/idea.css";
import "codemirror/mode/sql/sql.js";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";
import "codemirror/addon/display/placeholder.js";

// receive props
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

// launch event
const emit = defineEmits(["update:modelValue", "changeTextarea"]);

// local bind variable
const code = ref(props.modelValue);

// Monitor changes in the v-model value passed in from the outside and update the internal code
watch(
  () => props.modelValue,
  (val) => {
    if (val !== code.value) {
      code.value = val;
    }
  }
);

// Monitor changes in internal code and update v-model synchronously
watch(code, (val) => {
  emit("update:modelValue", val);
});

// Handling when code changes
const onInput = () => {
  emit("changeTextarea", code.value);
};

// Codemirror configuration items
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

// Initialization prompt
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

// Provide methods for parent components to call
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
