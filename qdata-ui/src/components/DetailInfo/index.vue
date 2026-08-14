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
  <!-- Universal detail page header component: title/header + rasterized information row -->
  <div class="pagecont-top pb15" v-show="show">
    <div class="infotop">
      <!-- title/header area -->
      <div class="infotop-title mb15" :class="headerClasses">
        <!-- Default title (displayed when no header is passed) -->
        <template v-if="!header">
          {{ title }}
        </template>
        <!-- Fixed header: numbered square + name + status dictionary + right button/slot -->
        <template v-else>
          <div class="task-item">
            <div class="task-id">
              {{
                stringify(getByPath(data, header.idKey ?? "id")) || placeholder
              }}
            </div>
            <div class="task-name">
              {{ stringify(getByPath(data, header.nameKey)) || "" }}
            </div>
            <dict-tag
              v-if="header.statusKey && header.statusOptions"
              :options="header.statusOptions"
              :value="getByPath(data, header.statusKey)"
            />
          </div>
          <div class="btn-style">
            <el-button
              type="primary"
              plain
              class="fh_btn"
              @mousedown="(e) => e.preventDefault()"
              @click="handleBack"
            >
              <svg-icon :iconClass="header.backIcon ?? 'fhs'"/>
              {{ header.backText ?? td('common.button.return') }}
            </el-button>
          </div>
          <slot name="header-right" />
        </template>
      </div>
      <!-- Information grid: 3 per row by default (span=8), can be covered by item -->
      <el-row :gutter="merged.gutter">
        <el-col
          v-for="(item, idx) in displayItems"
          :key="idx"
          :span="item.span ?? merged.defaultSpan"
          :class="item.className"
        >
          <div class="infotop-row border-top">
            <div class="infotop-row-lable">
              {{ item.label }}
              <!-- <el-tooltip
                    v-bind="item.tip"
                    :effect="item.tip.effect || 'light'"
                    :placement="item.tip.placement || 'top'"
                    v-if="item.tip && Object.keys(item.tip).length"
                >
                    <el-icon class="tip-icon" style="margin-left: 2px;"> <InfoFilled /> </el-icon>
                    <template #content v-if="item.tip.custom">
                        <div class="tip-content" v-html="item.tip.content"></div>
                    </template>
                </el-tooltip> -->
            </div>
            <div class="infotop-row-value">
              <slot
                v-if="item.slot"
                :name="item.slot"
                :item="item"
                :data="data"
              />
              <dict-tag
                v-else-if="item.dictOptions"
                :options="item.dictOptions"
                :value="resolveValue(item)"
              />
              <ImagePreview
                v-else-if="
                  item.type === 'image' &&
                  (resolveValue(item) || item.imageFallback)
                "
                :src="getImageSrc(item)"
                :width="item.width ?? 50"
                :height="item.height ?? 50"
              />
              <span v-else-if="item.type === 'image'">
                {{ merged.placeholder }}
              </span>
              <!-- Time display (example: {{ parseTime(dppEtlTaskDetail.createTime, "{y}-{m}-{d} {h}:{i}") }}) -->
              <span
                v-else-if="isTimeItem(item)"
                :class="item.ellipsisClass || 'ellipsis'"
                :title="formatTime(resolveValue(item))"
              >
                {{ formatTime(resolveValue(item)) || merged.placeholder }}
              </span>
              <!-- Format function display -->
              <span
                v-else-if="item.formatter"
                :class="item.ellipsisClass || 'ellipsis'"
                :title="stringify(resolveFormatted(item))"
              >
                {{ stringify(resolveFormatted(item)) || merged.placeholder }}
              </span>
              <!-- Ordinary text display (supports nested keys) -->
              <span
                v-else
                :class="item.ellipsisClass || 'ellipsis'"
                :title="stringify(resolveValue(item))"
              >
                {{ stringify(resolveValue(item)) || merged.placeholder }}
              </span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
  <!-- Usage example:
    <DetailInfo
      :title="data.name"
      :data="data"
      :items="[
        { label: 'number', key: 'id' },
        { label: 'status', key: 'status', dictOptions: sys_disable },
        { label: 'description', key: 'description', span: 12, ellipsisClass: 'ellipsis-2' },
        { label: 'remark', key: 'remark', span: 12, ellipsisClass: 'ellipsis' },
        { label: 'Creation Time', formatter: (d) => proxy.parseTime(d.createTime, '{y}-{m}-{d} {h}:{i}') },
        { label: 'API request address', formatter: (d) => `/services/${d.apiVersion}${d.apiUrl}` },
      ]"
      :defaultSpan="8"
      :gutter="2"
    />
  -->
</template>

<script setup name="DetailInfo">
import { useRouter } from "vue-router";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
// Design description:
// - Universal detail page header display component, used uniformly by all modules in the system;
// - The default is 3 items per line (span=8), which can be adjusted to 12 (2 items per line) or 24 (exclusively one line) through items[i].span;
// - Supports four display methods: slot slot, dictionary component, formatter function, and ordinary text;
// - Ordinary text supports nested keys (such as 'a.b.c'), and displays placeholders when empty;
// - It is recommended to set span=12 for fields such as description and remarks, so that 2 fields can be displayed in one line;

const { proxy } = getCurrentInstance();

const props = defineProps({
  // title text
  title: { type: String, default: "" },
  // Whether to display
  show: { type: Boolean, default: true },
  // Header configuration (can display: number, name, status, return button)
  header: {
    type: Object,
    default: null, // { idKey?, nameKey, statusKey, statusOptions, backText?, backIcon?, className? }
  },
  // line spacing
  gutter: { type: Number, default: 2 },
  // Default span for each item (if not passed, 3 per line)
  defaultSpan: { type: Number, default: 8 },
  // Details data source object
  data: { type: Object, default: () => ({}) },
  // Configuration item list
  items: {
    type: Array,
    default: () => [],
  },
  // null placeholder
  placeholder: { type: String, default: "-" },
  // Time type default format
  timeFormat: { type: String, default: "{y}-{m}-{d} {h}:{i}" },
  // Display mode: 'default' uses fixed layout; 'free' uses passed items layout
  mode: { type: String, default: "default" },
  // Optional configuration object to facilitate one-time setting of parameters that are not frequently changed
  config: {
    type: Object,
    default: () => ({}), // { gutter, defaultSpan, placeholder, timeFormat, mode }
  },
});

const headerClass = computed(() => props.header?.className ?? "clearfixs");
const headerIdVal = computed(() =>
  stringify(getByPath(props.data, props.header?.idKey ?? "id"))
);
const headerClasses = computed(() => {
  const cls = headerClass.value;
  const isLong =
    typeof headerIdVal.value === "string" && headerIdVal.value.length > 12;
  return isLong ? [cls, "no-square-id"] : cls;
});

// Merge configuration: config first, then use separate props, and finally use default
const merged = computed(() => ({
  gutter: props.config.gutter ?? props.gutter,
  defaultSpan: props.config.defaultSpan ?? props.defaultSpan,
  placeholder: props.config.placeholder ?? props.placeholder,
  timeFormat: props.config.timeFormat ?? props.timeFormat,
  mode: props.config.mode ?? props.mode,
}));

// Parse nested keys: support 'a.b.c' access
function getByPath(obj, path) {
  if (!obj || !path) return undefined;
  if (typeof path !== "string") return obj[path];
  return path.split(".").reduce((acc, k) => (acc ? acc[k] : undefined), obj);
}

// Convert any value to a string (avoid [object Object])
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

// Ordinary/dictionary item value
function resolveValue(item) {
  // Key takes priority, then item.value is read directly, and finally the entire data
  if (item.key) return getByPath(props.data, item.key);
  if (typeof item.value !== "undefined") return item.value;
  return props.data;
}

// Determine whether it is a time item (type='time' or key ends with 'time')
function isTimeItem(item) {
  return (
    item?.type === "time" ||
    (typeof item?.key === "string" && /time$/i.test(item.key))
  );
}

// Time formatting (directly called in the template, simple and intuitive)
function formatTime(val) {
  return val === null || val === undefined
    ? ""
    : proxy.parseTime?.(val, merged.value.timeFormat) ?? val;
}

// formatter format value
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

function getImageSrc(item) {
  const val = resolveValue(item);
  return val || item.imageFallback || "";
}

const displayItems = computed(() => {
  const items = Array.isArray(props.items) ? props.items : [];
  if (merged.value.mode === "default") {
    const firstRow = items.slice(0, 3);
    const rows = [
      ...firstRow,
      {
        label: td('common.texts.description'),
        key: "description",
        span: 24,
        ellipsisClass: "ellipsis",
        className: "mt2 mb2",
      },
      { label: td('common.texts.createdBy'), key: "createBy", span: 8 },
      { label: td('common.texts.createdTime'), key: "createTime", type: "time", span: 8 },
      { label: td('common.texts.updatedTime'), key: "updateTime", type: "time", span: 8 },
      {
        label: td('common.texts.remark'),
        key: "remark",
        span: 24,
        ellipsisClass: "ellipsis",
        className: "mt2 mb2",
      },
    ];
    return rows;
  }
  return items;
});

const emit = defineEmits(["back"]);
const router = useRouter();

function handleBack() {
  emit("back");
  try {
    router.back();
  } catch {}
}
</script>
<style scoped>
.no-square-id .task-id {
  aspect-ratio: auto !important;
}
</style>
