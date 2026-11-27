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

  Business License: https://docs.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <div :class="['app-container', { 'is-tab': type === 'tab' }]">
    <div class="developing-wrapper">
      <img
        src="@/assets/images/system/error/illust-building.png"
        :alt="td('sys.developing.underDevelopment')"
        class="developing-img"
      />
    </div>
  </div>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();

defineProps({
  type: {
    type: String,
    default: "page",
  },
});
</script>

<style scoped>
.developing-wrapper {
  width: 100%;
  height: calc(100vh - 125px);
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  overflow: hidden;
}

.developing-img {
  width: 100%;
  max-width: 500px;
  height: auto;
  object-fit: contain;
  display: block;
  margin-top: -46px;
}

.is-tab.app-container {
  padding: 0;
}

.is-tab .developing-wrapper {
  height: 500px;
}

.is-tab .developing-img {
  max-width: 400px;
  margin-top: 0;
}
</style>
