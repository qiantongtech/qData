<template>
  <el-tooltip
    v-if="overflow > 0"
    placement="top"
    effect="light"
    popper-class="tagclamp-popper"
  >
    <template #content>
      <div class="tooltip-content">
        <el-tag
          v-for="(tag, idx) in allTags"
          :key="'full-' + idx"
          class="tag-full"
        >
          {{ tag }}
        </el-tag>
      </div>
    </template>
    <div class="tag-clamp">
      <div class="tag-line" v-for="(tag, idx) in visibleTags" :key="idx">
        <el-tag class="tag-original">{{ tag }}</el-tag>
        <el-tag
          v-if="idx === visibleTags.length - 1 && overflow > 0"
          class="more"
        >
          +{{ overflow }}
        </el-tag>
      </div>
    </div>
  </el-tooltip>
  <div v-else class="tag-clamp">
    <div class="tag-line" v-for="(tag, idx) in visibleTags" :key="idx">
      <el-tag class="tag-original">{{ tag }}</el-tag>
    </div>
  </div>
  <span v-if="visibleTags.length === 0" class="empty-placeholder">-</span>
</template>

<script setup>
const props = defineProps({
  tags: { type: Array, default: () => [] },
  maxLines: { type: Number, default: 2 },
});

const allTags = computed(() => (Array.isArray(props.tags) ? props.tags : []));
const visibleTags = computed(() => {
  if (!Array.isArray(props.tags)) return [];
  return props.tags.slice(0, props.maxLines);
});
const overflow = computed(() => {
  if (!Array.isArray(props.tags)) return 0;
  return Math.max(0, props.tags.length - visibleTags.value.length);
});
</script>

<style scoped>
.tag-clamp {
  display: flex;
  flex-direction: column;
}
.tag-line {
  display: flex;
  align-items: center;
}
.tag-line:not(:last-child) {
  margin-bottom: 5px;
}
.tag-original {
  margin-right: 5px;
}
.more {
  margin-right: 5px;
}
</style>
<style>
.tagclamp-popper .tooltip-content {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  max-width: 480px;
}
.tagclamp-popper {
  background-color: #ffffff !important;
  color: #303133 !important;
  border: 1px solid #ebeef5 !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1) !important;
}
.tagclamp-popper .el-tooltip__content {
  background-color: #ffffff !important;
  color: #303133 !important;
  border: none !important;
}
.tagclamp-popper .tag-full {
  margin-right: 0;
}
</style>
