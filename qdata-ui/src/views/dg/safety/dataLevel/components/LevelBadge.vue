<template>
  <div class="level-badge" :style="{ backgroundColor: badgeColor }">
    <img src="@/assets/images/dg/safety/dataLevel/anquan.svg" alt="" />
    <span>{{ badgeText }}</span>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  levelData: {
    type: Object,
    required: true,
  },
});

const badgeText = computed(() => {
  return props.levelData ?? "";
});

// 根据 badgeText 内容决定背景色
const badgeColor = computed(() => {
  switch (props.levelData) {
    case "L1":
      return "#00b285"; // Green
    case "L2":
      return "#1890ff"; // Blue
    case "L3":
      return "#faad14"; // Orange
    case "L4":
      return "#f5222d"; // Red
    default:
      return "#faad14";
  }
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