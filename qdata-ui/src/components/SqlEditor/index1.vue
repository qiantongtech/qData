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
import "codemirror/mode/sql/sql.js";
// 代码提示功能
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/show-hint";
import "codemirror/addon/hint/sql-hint";
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
    default: "select * FROM log_table where id>${id}",
  },
});

// 定义响应式变量
const code = ref("");

// 计算属性动态设置 `readOnly`
const cmOptions = computed(() => ({
  mode: "text/x-sql", // 语言及语法模式
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
