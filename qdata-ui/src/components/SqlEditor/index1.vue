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
// Import css files
import "codemirror/lib/codemirror.css";
// Introduce the topic
import "codemirror/theme/idea.css";
// Introduce language mode
import "codemirror/mode/sql/sql.js";
// Code prompt function
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";
import "codemirror/addon/display/placeholder.js";
// Define props
const props = defineProps({
  readonly: {
    type: Boolean,
    default: false, // The default is false, indicating editable
  },
  width: {
    type: String,
    default: "100%", // Default width is 100%
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

// Define reactive variables
const code = ref("");

// Calculated property dynamic setting `readOnly`
const cmOptions = computed(() => ({
  mode: "text/x-sql", // Language and Grammar Patterns
  theme: "default", // Topic
  lineNumbers: true, // Show line number
  lineWrapping: true, // soft line break
  tabSize: 4, // tab width
  readOnly: props.readonly ? "nocursor" : false, // read-only mode
  placeholder: props.placeholder,
  hintOptions: {
    zindex: 9999, // Make sure it's high enough
    completeSingle: false, // Avoid autofill
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
  emit("changeTextarea", code.value); // Manually update the value of the parent component
};
// Bind events during initialization
const onReady = (editor) => {
  editor.on("inputRead", (cm, location) => {
    if (/[a-zA-Z]/.test(location.text[0])) {
      cm.showHint();
    }
  });
  nextTick(() => {
    editor.refresh(); // Make sure that CodeMirror refreshes the size correctly after the pop-up window is opened.
  });
};
const clear = () => {
  code.value = "";
  emit("changeTextarea", ""); // Notify parent component synchronously
};
defineExpose({ clear });
</script>
<style>
.CodeMirror-hints {
  z-index: 9999 !important; /* Make sure it's high enough to avoid being obscured */
  position: absolute !important;
}
</style>
