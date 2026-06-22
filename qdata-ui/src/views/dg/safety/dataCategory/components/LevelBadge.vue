<template>
  <div class="level-badge" :style="{ backgroundColor: badgeColor }">
    <img src="@/assets/images/dg/safety/dataLevel/anquan.svg" alt="" />
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
