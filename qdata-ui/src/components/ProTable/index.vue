<!--
  Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
   *
  Software Name: qData Data Middle Platform (Commercial Edition)
  Software Copyright Registration No. 16069171
   *
  [RIGHTS AND LICENSE STATEMENT]
  This file contains non-public commercial source code of which Jiangsu Qiantong
  Technology Co., Ltd. lawfully possesses complete intellectual property rights.
   *
  Access and use are limited to entities or individuals who have signed a valid
  commercial license agreement, within the scope stipulated in the agreement.
  The "accessibility" of this source code is premised on lawful authorization
  and does not constitute any form of transfer of intellectual property rights
  or implied licensing.
   *
  [PROHIBITIONS]
  Unless explicitly agreed in the license agreement, the following acts in any
  form are strictly prohibited:
  1. Copying, disseminating, disclosing, selling, renting, or redistributing
  this source code;
  2. Providing the software's functionality to third parties via SaaS, PaaS,
  cloud hosting, or other means;
  3. Using this software or its derivative versions to develop products that
  compete with the Right Holder;
  4. Providing or displaying this source code or related technical information
  to unauthorized third parties;
  5. Tampering with, circumventing, or destroying copyright notices, license
  verifications, or other technical protection measures.
   *
  [LEGAL LIABILITY]
  Any unauthorized use constitutes an infringement of trade secrets and
  intellectual property rights.
   *
  The Right Holder will strictly pursue liability for breach of contract and
  infringement in accordance with the commercial agreement and laws such as
  the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
  Competition Law".
   *
  ============================================================================
   *
  Copyright (c) 2026 江苏千桐科技有限公司
   *
  软件名称：qData 数据中台（商业版） | 软著登字第16069171号
   *
  【权利与授权声明】
  本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
  仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
  源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
   *
  【禁止事项】
  除授权合同明确约定外，严禁任何形式的：
  1. 复制、传播、披露、出售、出租或再分发本源代码；
  2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
  3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
  4. 向未授权第三方提供或展示本源代码或相关技术信息；
  5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
   *
  【法律责任】
  任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
  权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
  等法律法规，严厉追究违约与侵权责任。
-->

<template>
  <!-- 通用表格组件：支持列插槽、字典/时间格式化、自定义排序与分页 -->
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
        <!-- 自定义列头，支持 label + tooltip 组合展示 -->
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
        <!-- 单元格默认渲染：优先插槽，其次字典/时间，最后纯文本 -->
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
          <img src="@/assets/system/images/no_data/noData.png" alt="" />
          <p>暂无记录</p>
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
const noDataImg = new URL(
  "@/assets/images/common/noDataImg.png",
  import.meta.url
).href;
/**
 * ProTable（通用表格）——帮助页面“怎么做、做什么、注意什么”
 *
 * 一步一步怎么用（推荐）：
 * 1) 引入组件：import ProTable from '@/components/ProTable/index.vue'
 * 2) 定义列：const columns = [{ prop: 'name', label: '名称' }, { prop: 'status', label: '状态', dictOptions: [...] }]
 * 3) 准备数据与分页状态：list、loading、total、query({ pageNum, pageSize })
 * 4) 组装唯一入口 `config`：{ columns, data: list, loading, table, selection, actions, pagination }
 * 5) 模板中使用：<ProTable :config="tableConfig" @sort-change @pagination @update:selected> ...插槽 ...</ProTable>
 * 6) 可选：定义列插槽（如 `slot:'name'`）、操作列（`#actions`）、选择禁用（`selectionSelectable`）与排序（`sortKey`）
 *
 * 关键字段解释（最常用）：
 * - columns[].prop/label：数据字段名与列头文案（必填）
 * - columns[].dictOptions：字典渲染（根据 value 显示 label）
 * - columns[].time/timeFormat：时间渲染；默认格式 '{y}-{m}-{d} {h}:{i}'
 * - columns[].slot：自定义列渲染（模板里写 `#<slot>`）
 * - columns[].sort / sortable:'custom'：开启自定义排序；配合 sortKey 指定后端排序字段
 * - selectionSelectable：控制哪些行可选；支持三种：函数、字符串字段名、对象 { field, disabledValues }
 * - table.defaultSort：用 'asc' | 'desc'（内部自动映射为 ElementPlus 的 ascending/descending）
 * - pagination：{ total, page, limit }；当 total>0 自动显示分页
 *
 * 常见场景示例（摘抄）：
 * - 字典：{ prop:'status', label:'状态', dictOptions:[{value:'1',label:'启用'},{value:'0',label:'停用'}] }
 * - 时间：{ prop:'createTime', label:'创建时间', time:true, timeFormat:'{y}-{m}-{d} {h}:{i}' }
 * - 图片：{ prop:'icon', label:'图标', image:true, imageWidth:50, imageHeight:50, imageFallback:占位图 }
 * - 图标+文本：{ prop:'name', label:'名称', iconGetter:(row)=>url, iconSize:20 }
 * - 插槽：{ prop:'name', label:'名称', slot:'name' }，模板写 <template #name="{ row }">...</template>
 * - 操作列：在组件内统一开启 `actions:true`，模板写 <template #actions="{ row }"><el-button>...</el-button></template>
 * - 禁用选择：selectionSelectable: 'disabled' 或 { field:'status', disabledValues:['1'] } 或 (row)=>boolean
 * - 自定义排序：{ prop:'createTime', label:'创建时间', sort:true, sortKey:'create_time' }
 *
 * 事件（只需接收并回填/透传）：
 * - sort-change(e)：e.order 为 'asc'|'desc'；根据 e.column/e.prop 触发后端排序
 * - selection-change(rows)/update:selected(rows)：选中行集合
 * - pagination({ page, limit })：更新查询参数并刷新列表
 * - row-dblclick(row, column, event) / cell-dblclick({ row, column, cell, event })
 *
 * 最佳实践与注意：
 * - 统一只用 `config` 传参，减少多入口维护成本
 * - 列显隐用 `visible:false` 控制；不删除列定义，方便复用
 * - 长文本默认已开启溢出提示（showOverflowTooltip），如需关闭设为 false
 * - 大列表务必设置稳定的 `rowKey`（如 id）以优化渲染与选择
 * - 后端排序时使用 `sortKey`，并在 `sort-change` 里把 `order/prop` 透传给请求
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

// 统一入口：仅从 props.config 读取配置
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

// 选择列：判断某行是否允许选择
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

// 将 'asc' | 'desc' 映射为 ElementPlus 需要的 'ascending' | 'descending'
function mapOrderToEl(order) {
  if (order === "asc") return "ascending";
  if (order === "desc") return "descending";
  return order;
}

// 组装传给 el-table 的绑定参数
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

// 过滤不可见列
const renderColumns = computed(() => {
  return (conf.value.columns || []).filter((c) => c.visible !== false);
});

// 文本转 kebab：用于自动生成稳定列键
function toKebab(str) {
  if (!str) return str;
  return String(str)
    .replace(/([a-z0-9])([A-Z])/g, "$1-$2")
    .toLowerCase();
}

// 列唯一键：不强制要求传入 key，自动生成稳定键
// 优先使用 columnKey/sortKey，其次用 prop 的 kebab 形式，最后回退到 label
function getColumnKey(col) {
  return col?.columnKey || col?.sortKey || toKebab(col?.prop) || col?.label;
}

// 将列配置映射为 el-table-column 的 props
function columnProps(col) {
  // 开启自定义排序：sort/time 为真或显式 sortable="custom"
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

// 默认文本渲染：空值展示为 "-"
function displayCell(row, prop) {
  const val = prop ? row?.[prop] : undefined;
  return val ?? "-";
}

// 选择变更：同步 v-model:selected 与事件
function onSelectionChange(rows) {
  emit("update:selected", rows);
  emit("selection-change", rows);
}

// 排序事件标准化：order 统一输出为 'asc' | 'desc'
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

// 分页配置：仅从 config.pagination 读取
const paginationConfig = computed(() => {
  return conf.value.pagination || null;
});
// 当 total > 0 时显示分页组件
const showPagination = computed(() => {
  const cfg = paginationConfig.value;
  const total = cfg && typeof cfg.total !== "undefined" ? Number(cfg.total) : 0;
  return !!cfg && total > 0;
});
// 本地分页状态
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
// 分页事件透传
function onPagination(e) {
  emit("pagination", e);
}
// 列值获取：优先使用自定义 valueGetter
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
