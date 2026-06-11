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
-->

<template>
  <div></div>
</template>

<script setup>
defineProps({
  form: Object,
  falg: Boolean,
  inputFields: Array,
  columnList: Array,
});

// 占位组件校验，始终返回通过
const validate = async () => {
  return { valid: true, data: {} };
};

defineExpose({ validate });
</script>
