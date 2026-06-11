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
<!--   <i-frame v-model:src="url"></i-frame>-->
</template>

<script setup>
import iFrame from '@/components/iFrame/index.vue'
const { proxy } = getCurrentInstance();

const url = ref(import.meta.env.VITE_APP_BASE_API + "/doc.html")

// 打开外链 URL 在新的标签页中
window.open(url.value, "_blank");

proxy.$tab.closePage();
</script>
