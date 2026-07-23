<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  QtTagGroup Component Notes:
  This component is used to display tag groups. It supports automatic
  collapsing and shows the full content in a tooltip.
  It is commonly used for data source lists, tag lists, and similar cases.

  Usage Examples:
  1. Basic usage (string array):
     <QtTagGroup :items="['tag1', 'tag2', 'tag3']" />

  2. Object array (auto-mapped fields):
     Field mapping: label/DATASOURCE_NAME/name, datasourceType/DATASOURCE_TYPE/type
     <QtTagGroup :items="[{ name: 'MySQL', type: 'MYSQL' }, { name: 'Oracle', type: 'ORACLE' }]" />

  3. Custom maximum display count:
     <QtTagGroup :items="items" :max-count="3" />

  4. Force data source style:
     <QtTagGroup :items="['MySQL', 'Oracle']" is-datasource />
-->

<template>
  <div
    class="qt-tag-group"
    :class="[{ 'is-block': type !== 'none' }]"
    v-if="items && items.length"
  >
    <el-tooltip
      v-if="overflow > 0"
      placement="top"
      effect="light"
      popper-class="qt-tag-group-popper"
    >
      <template #content>
        <div class="tooltip-content">
          <DictTag
            v-for="(item, idx) in items"
            :key="'full-' + idx"
            :options="[getItemProps(item)]"
            :value="getItemProps(item).value"
          />
        </div>
      </template>
      <div
        class="tag-container"
        :class="[size, { 'is-clamped': type !== 'none' }]"
        :style="containerStyle"
        ref="containerRef"
      >
        <DictTag
          v-for="(item, idx) in displayItems"
          :key="idx"
          :options="[item]"
          :value="item.value"
          :size="size"
          class="qt-tag-item"
        />
        <el-tag
          v-if="overflow > 0"
          :size="size"
          :type="isDatasourceGroup ? 'primary' : 'info'"
          class="more"
        >
          +{{ overflow }}
        </el-tag>
      </div>
    </el-tooltip>
    <div v-else class="tag-container" :class="[size]" ref="containerRef">
      <DictTag
        v-for="(item, idx) in displayItems"
        :key="idx"
        :options="[item]"
        :value="item.value"
        :size="size"
        class="qt-tag-item"
      />
    </div>
  </div>
  <span v-else class="empty-placeholder">-</span>
</template>

<script setup name="QtTagGroup">
import {
  computed,
  ref,
  onMounted,
  onBeforeUnmount,
  watch,
  nextTick,
} from "vue";

const props = defineProps({
  /** Tag data array (can be a string array or object array) */
  items: { type: Array, default: () => [] },
  /** Maximum display quantity (only takes effect when type is 'none') */
  maxCount: { type: Number, default: 2 },
  /**
   * Display type:
   * - 'none': use maxCount logic
   * - 'single': automatically display one line
   * - 'double': automatically display two lines
   */
  type: { type: String, default: "single" },
  /** Whether to force data source mode */
  isDatasource: { type: Boolean, default: false },
  /** Label size: large / default / small */
  size: { type: String, default: "default" },
});

const containerRef = ref(null);
const visibleCount = ref(props.items.length);

// Determine whether the entire group is in data source mode
const isDatasourceGroup = computed(() => {
  if (props.isDatasource) return true;
  // If any item in items contains a data source-specific field, it is considered a data source group
  return props.items.some(
    (item) =>
      typeof item === "object" && (item.datasourceType || item.DATASOURCE_TYPE)
  );
});

// Calculate the actual displayed items
const displayItems = computed(() => {
  const items =
    props.type === "none"
      ? props.items.slice(0, props.maxCount)
      : props.items.slice(0, visibleCount.value);

  return items.map((item) => getItemProps(item));
});

// Count the number of items not shown
const overflow = computed(() => {
  if (props.type === "none") {
    return Math.max(0, props.items.length - props.maxCount);
  }
  return Math.max(0, props.items.length - visibleCount.value);
});

// Container style, used to control the number of rows
const containerStyle = computed(() => {
  if (props.type === "none") return {};
  // Increase estimated row height to ensure small pixel differences don't cause clipping
  const rowHeight = props.size === "small" ? 32 : 44;
  const maxRows = props.type === "double" ? 2 : 1;
  return {
    maxHeight: `${rowHeight * maxRows}px`,
    overflow: "hidden",
  };
});

/**
 * Calculate visible quantity under auto-collapse
 */
const calculateVisibleCount = () => {
  if (props.type === "none" || !containerRef.value) return;

  // 1. Display all items first for measurement (measure offsetTop in hidden state)
  visibleCount.value = props.items.length;

  nextTick(() => {
    if (!containerRef.value) return;
    const container = containerRef.value;
    const tags = container.querySelectorAll(".qt-tag-item");
    if (tags.length === 0 || tags[0].offsetHeight === 0) return;

    const firstTop = tags[0].offsetTop;
    const maxRows = props.type === "double" ? 2 : 1;

    // Dynamically obtain the height of the first label as the row height base
    const itemHeight = tags[0].offsetHeight;
    const gap = 6;
    const lineHeight = itemHeight + gap;
    // Threshold: first line top + (line height * number of lines) - buffer
    const threshold = firstTop + lineHeight * maxRows - 2;

    let count = 0;
    for (let i = 0; i < tags.length; i++) {
      // Check if bottom of label exceeds threshold
      if (tags[i].offsetTop + tags[i].offsetHeight <= threshold) {
        count++;
      } else {
        break;
      }
    }

    // If there is overflow, space needs to be left for the "+N" tag
    if (count < props.items.length) {
      // Reserve a spot for "+N", but keep at least one label
      visibleCount.value = Math.max(1, count - 1);
    } else {
      visibleCount.value = count;
    }
  });
};

let resizeObserver = null;

onMounted(() => {
  if (props.type !== "none") {
    calculateVisibleCount();
    resizeObserver = new ResizeObserver(() => {
      calculateVisibleCount();
    });
    if (containerRef.value) {
      resizeObserver.observe(containerRef.value);
    }
  }
});

onBeforeUnmount(() => {
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
});

watch(
  () => props.items,
  () => {
    calculateVisibleCount();
  },
  { deep: true }
);

watch(
  () => props.type,
  (newVal) => {
    if (newVal !== "none") {
      calculateVisibleCount();
      if (!resizeObserver && containerRef.value) {
        resizeObserver = new ResizeObserver(() => {
          calculateVisibleCount();
        });
        resizeObserver.observe(containerRef.value);
      }
    } else if (resizeObserver) {
      resizeObserver.disconnect();
      resizeObserver = null;
    }
  }
);

/**
 * Conversion item properties
 * Supports multiple field formats: DATASOURCE_NAME, label, name, DATASOURCE_TYPE, datasourceType, type, etc.
 */
const getItemProps = (item) => {
  if (typeof item === "string") {
    return { label: item, value: item, elTagType: "primary" };
  }
  const label = item.label || item.DATASOURCE_NAME || item.name || "";
  return {
    ...item,
    label,
    value: item.value || label,
    // If it is data source mode and no type is specified, primary is used by default (aligned with the blue label of DictTag)
    elTagType:
      item.elTagType ||
      item.tagType ||
      item.tag_type ||
      item.type ||
      (props.isDatasource || !!(item.DATASOURCE_TYPE || item.datasourceType)
        ? "primary"
        : ""),
    elTagClass: item.elTagClass || item.tagClass || item.class || "",
    datasourceType:
      item.datasourceType ||
      item.DATASOURCE_TYPE ||
      item.type ||
      item.dbType ||
      "",
  };
};
</script>

<style lang="scss" scoped>
.qt-tag-group {
  display: inline-block;
  vertical-align: middle;
  &.is-block {
    display: block;
    width: 100%;
  }
}

.tag-container {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  width: 100%;
}

.empty-placeholder {
  color: #999;
}

.more {
  cursor: pointer;
  margin-left: 2px;
}
</style>

<style lang="scss">
.qt-tag-group-popper {
  background-color: #ffffff !important;
  padding: 8px !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1) !important;
  border: 1px solid #ebeef5 !important;

  .el-popper__arrow::before {
    background-color: #ffffff !important;
    border: 1px solid #ebeef5 !important;
  }

  .tooltip-content {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    max-width: 400px;
  }
}
</style>
