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

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
  <!-- Universal description information component: based on Element Plus el-descriptions, supports dictionary, time, label, formatting -->
  <div class="descriptions-info">
    <el-descriptions :title="title" :column="column" :border="border">
      <el-descriptions-item
        v-for="(item, index) in normalizedItems"
        :key="index"
        :label-class-name="labelClass"
        :class-name="contentClass"
        :span="item.span ?? 1"
      >
        <template #label>
          <div class="cell-item">
            {{ item.label }}
            <!-- <el-tooltip
                v-bind="item.tip"
                :effect="item.tip.effect || 'light'"
                :placement="item.tip.placement || 'top'"
                v-if="item.tip && Object.keys(item.tip).length"
            >
                <el-icon class="tip-icon" color="#888"> <InfoFilled /> </el-icon>
                <template #content v-if="item.tip.custom">
                    <div class="tip-content" v-html="item.tip.content"></div>
                </template>
            </el-tooltip> -->
          </div>
        </template>

        <!-- Custom slot -->
        <slot v-if="item.slot" :name="item.slot" :item="item" :data="data" />

        <!-- Tag type -->
        <span v-else-if="item.type === 'tags'">
          <template
            v-if="
              Array.isArray(resolveValue(item)) && resolveValue(item).length
            "
          >
            <el-tag
              v-for="tag in resolveValue(item)"
              :key="String(tag)"
              class="mr10"
            >
              {{ String(tag) }}
            </el-tag>
          </template>
          <template v-else>{{ placeholder }}</template>
        </span>

        <!-- dictionary type -->
        <span v-else-if="item.dictOptions">
          <dict-tag
            v-if="
              resolveValue(item) !== null &&
              resolveValue(item) !== undefined &&
              resolveValue(item) !== ''
            "
            :options="item.dictOptions"
            :value="resolveValue(item)"
          />
          <template v-else>{{ placeholder }}</template>
        </span>

        <!-- time type -->
        <span v-else-if="isTimeItem(item)">
          {{ formatTime(resolveValue(item)) || placeholder }}
        </span>

        <!-- Custom formatting -->
        <span v-else-if="item.formatter">
          {{ stringify(resolveFormatted(item)) || placeholder }}
        </span>

        <!-- normal text -->
        <span v-else>
          {{ stringify(resolveValue(item)) || placeholder }}
        </span>
      </el-descriptions-item>
    </el-descriptions>
  </div>
  <!-- Usage example:
    <DescriptionsInfo
      title="Basic information"
      :data="detail"
      :items="[
        { label: 'file name', key: 'fileName' },
        { label: 'status', key: 'status', dictOptions: dp_document_status },
        { label: 'label', key: 'tags', type: 'tags' },
        { label: 'Creation Time', key: 'createTime', type: 'time' },
        { label: 'Creator', key: 'createBy' },
        { label: 'updateTime', key: 'updateTime', type: 'time' },
        { label: 'remark', key: 'remark', span: 2 },
        { label: 'CreateType', key: 'createType', formatter: (v) => v === 1 ? 'Virtual asset creation' : 'Complete asset creation' },
      ]"
    />
  -->
</template>

<script setup name="DescriptionsInfo">
/**
 * Design description:
 * - Universal global component that uniformly encapsulates the common display capabilities of el-descriptions;
 * - Supports four types of display: tags tags, dictionary dictOptions, time type='time', custom formatter, and ordinary text;
 * - items supports span, className, key (supports nested a.b.c), value (direct value passing), dictOptions, formatter;
 * - Control empty value display placeholder through props.placeholder (default "-");
 * - Control the time format through props.timeFormat (default "{y}-{m}-{d} {h}:{i}");
 */

const { proxy } = getCurrentInstance();

const props = defineProps({
  // Title (consistent with el-descriptions)
  title: { type: String, default: "" },
  // data source object
  data: { type: Object, default: () => ({}) },
  // Configuration items
  items: { type: Array, default: () => [] },
  // Number of columns
  column: { type: Number, default: 2 },
  // Whether to display borders
  border: { type: Boolean, default: true },
  // Tag class name
  labelClass: { type: String, default: "base-label" },
  // Content class name
  contentClass: { type: String, default: "base-content" },
  // Null value placeholder
  placeholder: { type: String, default: "-" },
  // Default time format
  timeFormat: { type: String, default: "{y}-{m}-{d} {h}:{i}" },
});

const normalizedItems = computed(() => {
  return Array.isArray(props.items) ? props.items : [];
});

function getByPath(obj, path) {
  if (!obj || !path) return undefined;
  if (typeof path !== "string") return obj[path];
  return path.split(".").reduce((acc, k) => (acc ? acc[k] : undefined), obj);
}

function resolveValue(item) {
  if (typeof item?.value !== "undefined") return item.value;
  if (item?.key) return getByPath(props.data, item.key);
  return undefined;
}

function isTimeItem(item) {
  return (
    item?.type === "time" ||
    (typeof item?.key === "string" && /(time|date)$/i.test(item.key))
  );
}

function formatTime(val) {
  return val === null || val === undefined
    ? ""
    : proxy.parseTime?.(val, props.timeFormat) ?? val;
}

function resolveFormatted(item) {
  try {
    const val = resolveValue(item);
    if (item.formatter) {
      return item.formatter(val, props.data, proxy);
    }
    return val;
  } catch {
    return resolveValue(item);
  }
}

function stringify(val) {
  if (val === null || val === undefined) return "";
  if (typeof val === "object") {
    try {
      return JSON.stringify(val);
    } catch {
      return "";
    }
  }
  return String(val);
}
</script>

<style lang="scss" scoped>
.mr10 {
  margin-right: 10px;
}
:deep(.base-label) {
  width: 200px;
  .cell-item {
    font-weight: 500;
    display: flex;
    align-items: center;
    gap: 2px;
  }
}
</style>
