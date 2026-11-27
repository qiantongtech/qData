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
  <div class="level-badge" :style="{ backgroundColor: badgeColor }">
    <img src="../../../../../assets/images/dg/icon-anquan.svg" alt="" />
    <span>{{ badgeText }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  levelData: {
    type: Object,
    required: true
  }
});

const badgeText = computed(() => {
  const row = props.levelData;
  const s = row.sensitiveLevel ?? "";
  if (s !== "") return `L${s}`;
  return "L";
});

const badgeColor = computed(() => {
  const row = props.levelData;
  const colors = String(row.colors || "");
  
  if (colors === "1") return "#00b285"; // Green
  if (colors === "2") return "#1890ff"; // Blue
  if (colors === "3") return "#faad14"; // Orange/Yellow
  if (colors === "4") return "#f5222d"; // Red
  
  return "#faad14"; // Default Yellow
});
</script>

<style lang="scss" scoped>
.level-badge {
  display: flex;
  align-items: center;
  height: 20px;
  padding: 0 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  margin-right: 8px;
  flex-shrink: 0;

  img {
    width: 10px;
    height: 12px;
    margin-right: 4px;
  }

  span {
    line-height: 1;
  }
}
</style>
