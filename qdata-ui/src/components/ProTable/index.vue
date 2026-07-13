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
  <!-- Universal table component: supports column slots, dictionary/time formatting, custom sorting and paging -->
  <el-table
    v-bind="tableBinding"
    v-loading="conf.loading"
    @sort-change="onSortChange"
    @selection-change="onSelectionChange"
    @row-dblclick="onRowDblclick"
    @cell-dblclick="onCellDblclickRaw"
  >
    <el-table-column
      v-if="ui.selection"
      type="selection"
      width="55"
      align="center"
      :selectable="rowSelectable"
    />
    <template v-for="col in renderColumns" :key="getColumnKey(col)">
      <el-table-column v-bind="columnProps(col)">
        <!-- Customize column headers, support label + tooltip combination display -->
        <template #header>
          <template
            v-if="
              col.headerConfig &&
              (col.headerConfig.label || col.headerConfig.tooltip)
            "
          >
            <div class="justify-center">
              <span style="margin-right: 2px">{{
                col.headerConfig.label || col.label
              }}</span>
              <el-tooltip
                v-if="col.headerConfig.tooltip"
                effect="light"
                :content="col.headerConfig.tooltip"
                placement="top"
              >
                <el-icon class="tip-icon">
                  <InfoFilled />
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <template v-else>
            <span>{{ col.label }}</span>
          </template>
        </template>
        <!-- Cell default rendering: slots first, dictionary/time second, plain text last -->
        <template #default="scope">
          <slot
            v-if="col.slot"
            :name="col.slot"
            :row="scope.row"
            :$index="scope.$index"
            :column="col"
          />
          <image-preview
            v-else-if="col.image"
            :src="getImageSrc(scope.row, col)"
            :width="col.imageWidth || 50"
            :height="col.imageHeight || 50"
          />
          <div v-else-if="col.iconGetter" class="justify">
            <img
              :src="col.iconGetter(scope.row)"
              alt=""
              :style="
                col.iconGetter(scope.row)
                  ? 'width: ' + (col.iconSize || 20) + 'px;margin-right: 5px;'
                  : ''
              "
            />
            <span @dblclick="onCellDblclick(scope.row, col, scope.$index)">{{
              displayCell(scope.row, col.prop)
            }}</span>
          </div>
          <dict-tag
            v-else-if="col.dictOptions"
            :options="col.dictOptions"
            :value="getDictValue(scope.row, col)"
          />
          <span v-else-if="col.time">{{
            parseTime(
              scope.row?.[col.prop],
              col.timeFormat || "{y}-{m}-{d} {h}:{i}"
            ) || "-"
          }}</span>
          <span v-else-if="col.cron">{{
            cronToZh(scope.row?.[col.prop] || "-")
          }}</span>
          <span v-else>{{ displayCell(scope.row, col.prop) }}</span>
        </template>
      </el-table-column>
    </template>
    <el-table-column
      v-if="ui.actions"
      :fixed="ui.actionsFixed"
      :width="ui.actionsWidth"
      align="center"
      label="操作"
      class-name="small-padding fixed-width"
    >
      <template #default="scope">
        <slot name="actions" :row="scope.row" :$index="scope.$index" />
      </template>
    </el-table-column>
    <template #empty>
      <slot name="empty">
        <div class="emptyBg">
          <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
          <p>{{td('common.noData')}}</p>
        </div>
      </slot>
    </template>
  </el-table>
  <div style="text-align: right">
    <Pagination
      v-if="showPagination"
      v-model:page="pageLocal"
      v-model:limit="limitLocal"
      :total="paginationTotal"
      @pagination="onPagination"
    />
  </div>
</template>

<script setup>
import { computed, ref, watch } from "vue";
import { parseTime } from "@/utils/anivia";
import { cronToZh } from "@/utils/cronUtils";
import Pagination from "@/views/flyflow/components/pagination.vue";
import useDefaultLang from "@/composables/useDefaultLang";
const { td } = useDefaultLang();
const noDataImg = new URL(
  "@/assets/images/common/empty-nodataimg.png",
  import.meta.url
).href;
/**
 * ProTable (General Table) - Help page "How to do, what to do, what to pay attention to"
 *
 * How to use it step by step (recommended):
 * 1) Introduce components: import ProTable from '@/components/ProTable/index.vue'
 * 2) Define columns: const columns = [{ prop: 'name', label: 'name' }, { prop: 'status', label: 'status', dictOptions: [...] }]
 * 3) Prepare data and paging status: list, loading, total, query({ pageNum, pageSize })
 * 4) Assemble the only entrance `config`: { columns, data: list, loading, table, selection, actions, pagination }
 * 5) Used in the template: <ProTable :config="tableConfig" @sort-change @pagination @update:selected> ...slot ...</ProTable>
 * 6) Optional: define column slots (such as `slot:'name'`), operation columns (`#actions`), selection disable (`selectionSelectable`) and sorting (`sortKey`)
 *
 * Explanation of key fields (most commonly used):
 * - columns[].prop/label: data field names and column header copy (required)
 * - columns[].dictOptions: dictionary rendering (display label according to value)
 * - columns[].time/timeFormat: time rendering; default format '{y}-{m}-{d} {h}:{i}'
 * - columns[].slot: Custom column rendering (write `#<slot>` in the template)
 * - columns[].sort / sortable:'custom': enable custom sorting; use sortKey to specify the backend sorting field
 * - selectionSelectable: controls which rows are selectable; supports three types: function, string field name, object { field, disabledValues }
 * - table.defaultSort: use 'asc' | 'desc' (automatically mapped internally to ascending/descending of ElementPlus)
 * - pagination: { total, page, limit }; when total>0, pagination is automatically displayed
 *
 * Examples of common scenarios (excerpts):
 * - Dictionary: { prop:'status', label:'status', dictOptions:[{value:'1',label:'enable'},{value:'0',label:'disable'}] }
 * - Time: { prop:'createTime', label:'Creation Time', time:true, timeFormat:'{y}-{m}-{d} {h}:{i}' }
 * - Image: { prop:'icon', label:'icon', image:true, imageWidth:50, imageHeight:50, imageFallback: placeholder image }
 * - Icon + text: { prop:'name', label:'name', iconGetter:(row)=>url, iconSize:20 }
 * - Slot: { prop:'name', label:'name', slot:'name' }, the template is written as <template #name="{ row }">...</template>
 * - Action column: Turn on `actions:true` uniformly in the component, and write the template as <template #actions="{ row }"><el-button>...</el-button></template>
 * - Disable selection: selectionSelectable: 'disabled' or { field:'status', disabledValues:['1'] } or (row)=>boolean
 * - Custom sorting: { prop:'createTime', label:'Creation Time', sort:true, sortKey:'create_time' }
 *
 * Events (just receive and backfill/passthrough):
 * - sort-change(e): e.order is 'asc'|'desc'; trigger backend sorting based on e.column/e.prop
 * - selection-change(rows)/update:selected(rows): selected row set
 * - pagination({ page, limit }): Update query parameters and refresh the list
 * - row-dblclick(row, column, event) / cell-dblclick({ row, column, cell, event })
 *
 * Best practices and notes:
 * - Unified and only use `config` to pass parameters, reducing multi-entry maintenance costs
 * - Use `visible:false` to control column display; do not delete column definitions, making it easier to reuse
 * - The overflow tip (showOverflowTooltip) is turned on by default for long text. If you need to turn it off, set it to false.
 * - For large lists, be sure to set a stable `rowKey` (such as id) to optimize rendering and selection
 * - Use `sortKey` when sorting on the backend, and transparently pass `order/prop` to the request in `sort-change`
 */
const props = defineProps({
  config: { type: Object, default: () => ({}) },
});

const emit = defineEmits([
  "update:selected",
  "selection-change",
  "sort-change",
  "pagination",
  "row-dblclick",
  "cell-dblclick",
]);

// Unified entrance: only read configuration from props.config
const cfgRef = computed(() => props.config || {});
const conf = computed(() => ({
  columns: cfgRef.value.columns || [],
  data: cfgRef.value.data || [],
  loading: cfgRef.value.loading || false,
  table: cfgRef.value.table || {},
  selection: cfgRef.value.selection || false,
  selectionSelectable: cfgRef.value.selectionSelectable,
  actions: cfgRef.value.actions || false,
  actionsWidth: cfgRef.value.actionsWidth ?? 240,
  actionsFixed: cfgRef.value.actionsFixed ?? "right",
  pagination: cfgRef.value.pagination || null,
}));
const ui = computed(() => ({
  selection: conf.value.selection,
  actions: conf.value.actions,
  actionsWidth: conf.value.actionsWidth,
  actionsFixed: conf.value.actionsFixed,
}));

// Select column: Determine whether a row allows selection
function rowSelectable(row, index) {
  const rule = conf.value.selectionSelectable;
  if (!rule) return true;
  if (typeof rule === "function") return !!rule(row, index);
  if (typeof rule === "string") return !Boolean(row?.[rule]);
  if (typeof rule === "object" && rule) {
    const { field, disabledValues } = rule;
    if (!field) return true;
    const v = row?.[field];
    if (Array.isArray(disabledValues)) {
      return !disabledValues.map(String).includes(String(v));
    }
    return !Boolean(v);
  }
  return true;
}

// Map 'asc' | 'desc' to 'ascending' | 'descending' required by ElementPlus
function mapOrderToEl(order) {
  if (order === "asc") return "ascending";
  if (order === "desc") return "descending";
  return order;
}

// Assemble the binding parameters passed to el-table
const tableBinding = computed(() => {
  const extra = conf.value.table || {};
  const ds = extra.defaultSort || {};
  const defaultSortMapped = ds?.order
    ? { ...ds, order: mapOrderToEl(ds.order) }
    : ds;
  return {
    stripe: extra.stripe ?? true,
    height: extra.height,
    data: conf.value.data,
    rowKey: extra.rowKey ?? "id",
    ...extra,
    defaultSort: defaultSortMapped,
  };
});

// Filter invisible columns
const renderColumns = computed(() => {
  return (conf.value.columns || []).filter((c) => c.visible !== false);
});

// Text to kebab: used to automatically generate stable column keys
function toKebab(str) {
  if (!str) return str;
  return String(str)
    .replace(/([a-z0-9])([A-Z])/g, "$1-$2")
    .toLowerCase();
}

// Column unique key: It is not mandatory to pass in the key, and a stable key is automatically generated
// Use columnKey/sortKey first, then use the kebab form of prop, and finally fall back to label
function getColumnKey(col) {
  return col?.columnKey || col?.sortKey || toKebab(col?.prop) || col?.label;
}

// Map column configuration to props of el-table-column
function columnProps(col) {
  // Turn on custom sorting: sort/time is true or explicit sortable="custom"
  const isSortable =
    col.sort === true || col.time === true || col.sortable === "custom";
  const columnKey = col.columnKey || col.sortKey || toKebab(col.prop);
  return {
    prop: col.prop,
    label: col.label,
    width: col.width,
    align: col.align || "left",
    sortable: isSortable ? "custom" : col.sortable,
    "column-key": columnKey,
    "show-overflow-tooltip": col.showOverflowTooltip ?? true,
  };
}

// Default text rendering: null values are displayed as "-"
function displayCell(row, prop) {
  const val = prop ? row?.[prop] : undefined;
  return val ?? "-";
}

// Select changes: Synchronize v-model:selected with events
function onSelectionChange(rows) {
  emit("update:selected", rows);
  emit("selection-change", rows);
}

// Sorting event standardization: order unified output is 'asc' | 'desc'
function onSortChange(e) {
  const order =
    e?.order === "ascending"
      ? "asc"
      : e?.order === "descending"
      ? "desc"
      : e?.order;
  emit("sort-change", { ...e, order });
}

function onRowDblclick(row, column, event) {
  emit("row-dblclick", row, column, event);
}

function onCellDblclickRaw(row, column, cell, event) {
  emit("cell-dblclick", { row, column, cell, event });
}

function onCellDblclick(row, col, index) {
  if (typeof col.onDblclick === "function") {
    col.onDblclick(row, index);
  }
}

// Pagination configuration: only read from config.pagination
const paginationConfig = computed(() => {
  return conf.value.pagination || null;
});
// Show pagination component when total > 0
const showPagination = computed(() => {
  const cfg = paginationConfig.value;
  const total = cfg && typeof cfg.total !== "undefined" ? Number(cfg.total) : 0;
  return !!cfg && total > 0;
});
// local paging status
const pageLocal = ref(1);
const limitLocal = ref(10);
watch(
  paginationConfig,
  (cfg) => {
    pageLocal.value = (cfg && cfg.page) || 1;
    limitLocal.value = (cfg && cfg.limit) || 10;
  },
  { immediate: true }
);
const paginationTotal = computed(() => {
  const cfg = paginationConfig.value;
  return cfg && typeof cfg.total !== "undefined" ? Number(cfg.total) : 0;
});
// Transparent transmission of pagination events
function onPagination(e) {
  emit("pagination", e);
}
// Column value acquisition: preferentially use custom valueGetter
function getDictValue(row, col) {
  if (typeof col.valueGetter === "function") {
    try {
      return col.valueGetter(row);
    } catch (e) {
      return row?.[col.prop];
    }
  }
  return row?.[col.prop];
}

function getImageSrc(row, col) {
  let src = null;
  if (typeof col.valueGetter === "function") {
    try {
      src = col.valueGetter(row);
    } catch (e) {
      src = row?.[col.prop];
    }
  } else {
    src = row?.[col.prop];
  }
  if (!src) return col.imageFallback || noDataImg;
  return src;
}
</script>

<style scoped>
</style>
