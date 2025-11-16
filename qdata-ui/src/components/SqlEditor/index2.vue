<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

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
