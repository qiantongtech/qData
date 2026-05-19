<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.

  QtTagGroup 组件说明:
  用于展示标签组，支持自动折叠和 Tooltip 显示全部内容。
  常用于展示数据源列表、标签列表等。

  使用示例:
  1. 基础用法 (字符串数组):
     <QtTagGroup :items="['标签1', '标签2', '标签3']" />

  2. 对象数组 (自动映射字段):
     字段映射: label/DATASOURCE_NAME/name, datasourceType/DATASOURCE_TYPE/type
     <QtTagGroup :items="[{ name: 'MySQL', type: 'MYSQL' }, { name: 'Oracle', type: 'ORACLE' }]" />

  3. 自定义最大显示数量:
     <QtTagGroup :items="items" :max-count="3" />

  4. 强制数据源样式:
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
  /** 标签数据数组 (可以是字符串数组 or 对象数组) */
  items: { type: Array, default: () => [] },
  /** 最大显示数量 (仅在 type 为 'none' 时生效) */
  maxCount: { type: Number, default: 2 },
  /**
   * 展示类型:
   * - 'none': 使用 maxCount 逻辑
   * - 'single': 自动展示一行
   * - 'double': 自动展示两行
   */
  type: { type: String, default: "single" },
  /** 是否强制为数据源模式 */
  isDatasource: { type: Boolean, default: false },
  /** 标签尺寸: large / default / small */
  size: { type: String, default: "default" },
});

const containerRef = ref(null);
const visibleCount = ref(props.items.length);

// 判断整个组是否为数据源模式
const isDatasourceGroup = computed(() => {
  if (props.isDatasource) return true;
  // 如果 items 中有任何一项包含数据源特有字段，则视为数据源组
  return props.items.some(
    (item) =>
      typeof item === "object" && (item.datasourceType || item.DATASOURCE_TYPE)
  );
});

// 计算实际显示的项
const displayItems = computed(() => {
  const items =
    props.type === "none"
      ? props.items.slice(0, props.maxCount)
      : props.items.slice(0, visibleCount.value);

  return items.map((item) => getItemProps(item));
});

// 计算未显示的项数量
const overflow = computed(() => {
  if (props.type === "none") {
    return Math.max(0, props.items.length - props.maxCount);
  }
  return Math.max(0, props.items.length - visibleCount.value);
});

// 容器样式，用于控制行数
const containerStyle = computed(() => {
  if (props.type === "none") return {};
  // 增加估算行高，确保不会因为微小的像素差异导致裁剪
  const rowHeight = props.size === "small" ? 32 : 44;
  const maxRows = props.type === "double" ? 2 : 1;
  return {
    maxHeight: `${rowHeight * maxRows}px`,
    overflow: "hidden",
  };
});

/**
 * 计算自动折叠下的可见数量
 */
const calculateVisibleCount = () => {
  if (props.type === "none" || !containerRef.value) return;

  // 1. 先展示所有项以便测量（在隐藏状态下测量 offsetTop）
  visibleCount.value = props.items.length;

  nextTick(() => {
    if (!containerRef.value) return;
    const container = containerRef.value;
    const tags = container.querySelectorAll(".qt-tag-item");
    if (tags.length === 0 || tags[0].offsetHeight === 0) return;

    const firstTop = tags[0].offsetTop;
    const maxRows = props.type === "double" ? 2 : 1;

    // 动态获取第一个标签的高度作为行高基准
    const itemHeight = tags[0].offsetHeight;
    const gap = 6;
    const lineHeight = itemHeight + gap;
    // 阈值：第一行 top + (行高 * 行数) - 缓冲
    const threshold = firstTop + lineHeight * maxRows - 2;

    let count = 0;
    for (let i = 0; i < tags.length; i++) {
      // 检查标签的底部是否超出了阈值
      if (tags[i].offsetTop + tags[i].offsetHeight <= threshold) {
        count++;
      } else {
        break;
      }
    }

    // 如果有溢出，需要留出空间给 "+N" 标签
    if (count < props.items.length) {
      // 预留一个位置给 "+N"，但至少保留一个标签
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
 * 转换项属性
 * 支持多种字段格式: DATASOURCE_NAME, label, name, DATASOURCE_TYPE, datasourceType, type 等
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
    // 如果是数据源模式且没有指定类型，默认使用 primary (对齐 DictTag 的蓝色标签)
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
