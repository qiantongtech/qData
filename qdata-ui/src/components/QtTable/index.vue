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

<!--
    qt-table component
    Description: An Element Plus table wrapper with pagination, sorting, dictionaries, icons, links, and other features.
    Note: Do not modify this component without authorization. Contact wy first if there are any issues.
-->
<template>
  <div class="qt-table" v-loading="store.loading">
    <div :class="['qt-table--main', config.table?.class]">
      <el-table
          ref="elTableRef"
          :data="tableData"
          v-bind="config.table"
          :default-sort="defaultTableSort"
          @sort-change="handleSortChange"
          v-if="store.showTable"
      >
        <template v-for="(column, index) in props.columns" :key="index">
          <el-table-column
              v-bind="getElColumnData(column)"
              v-if="!column.hide"
              :align="column.align || 'center'"
          >
            <template #header>
              <div class="tip-wrap" v-if="column.tip">
                <div class="tip-label">
                  {{ column.label }}
                </div>
                <el-tooltip
                    v-bind="column.tip"
                    :effect="column.tip.effect || 'light'"
                    :placement="column.tip.placement || 'top'"
                >
                  <template #content v-if="column.tip.custom">
                    <div class="tip-content" v-html="column.tip.content"></div>
                  </template>
                  <slot :name="column.tip.slot || 'tip'">
                    <el-icon><InfoFilled /></el-icon>
                  </slot>
                </el-tooltip>
              </div>
            </template>
            <template #default="scope">
              <!-- Empty data processing -->
              <template
                  v-if="
                  column.prop &&
                  [undefined, null].includes(scope.row[column.prop])
                "
              >
                {{ getFormatValue(scope.row[column.prop]) }}
              </template>

              <!-- dictionary -->
              <dict-tag
                  :options="getDictOptions(column.dict)"
                  v-if="column.dict"
                  :value="scope.row[column.prop]"
              />

              <!-- label -->
              <el-tag
                  v-if="column.tag && scope.row[column.prop]"
                  v-bind="typeof column.tag === 'object' ? column.tag : {}"
                  :type="
                  (typeof column.tag === 'object' ? column.tag.type : '') ||
                  'primary'
                "
                  :class="typeof column.tag === 'object' ? column.tag.class : ''"
              >
                {{ scope.row[column.prop] }}
              </el-tag>

              <!-- link -->
              <el-link
                  v-bind="column.link"
                  :underline="column.link?.underline || 'never'"
                  :type="column.link?.type || 'primary'"
                  v-if="column.link"
                  @click="handleLinkClick(column, scope.row)"
              >
                {{ scope.row[column.prop] }}
              </el-link>

              <!-- icon -->
              <svg-icon
                  v-bind="column.svg"
                  v-if="column.svg"
                  :icon-class="scope.row[column.prop]"
              />

              <!-- Uniform processing time -->
              <template v-if="column.date">
                {{
                  parseTime(
                      scope.row[column.prop],
                      column.date === true ? "{y}-{m}-{d} {h}:{i}" : column.date
                  )
                }}
              </template>

              <!-- Multi-field display -->
              <div
                  v-if="column.list"
                  :class="['flex-column', column.listClass || 'fz14']"
                  :style="{ alignItems: column.align || 'center' }"
              >
                <template v-for="(item, i) in column.list" :key="i">
                  <span
                      :class="['text-ellipsis', item.class]"
                      :title="
                      item.title ? scope.row[item.title] : scope.row[item.prop]
                    "
                  >
                    {{ scope.row[item.prop] || "-" }}
                  </span>
                </template>
              </div>
              <!-- Information column (icon + title + label / description) -->
              <div v-if="column.info" class="qt-table-info">
                <div class="qt-table-info__main">
                  <img
                      v-if="column.info.icon"
                      :src="
                      typeof column.info.icon === 'function'
                        ? column.info.icon(scope.row)
                        : scope.row[column.info.icon]
                    "
                      v-show="
                      typeof column.info.icon === 'function'
                        ? column.info.icon(scope.row)
                        : scope.row[column.info.icon]
                    "
                      class="info-icon"
                  />
                  <el-link
                      v-if="column.info.title"
                      type="primary"
                      :underline="false"
                      class="info-title text-ellipsis"
                      :title="
                      typeof column.info.title === 'function'
                        ? column.info.title(scope.row)
                        : scope.row[column.info.title]
                    "
                      @click="column.info.click && column.info.click(scope.row)"
                  >
                    {{
                      (typeof column.info.title === "function"
                          ? column.info.title(scope.row)
                          : scope.row[column.info.title]) || "-"
                    }}
                  </el-link>
                  <el-tag
                      v-if="
                      column.info.tag &&
                      (typeof column.info.tag === 'function'
                        ? column.info.tag(scope.row)
                        : scope.row[column.info.tag])
                    "
                      type="primary"
                      size="small"
                      class="info-tag"
                  >
                    {{
                      typeof column.info.tag === "function"
                          ? column.info.tag(scope.row)
                          : scope.row[column.info.tag]
                    }}
                  </el-tag>
                </div>
                <div
                    v-if="column.info.desc"
                    class="info-desc text-ellipsis"
                    :title="
                    typeof column.info.desc === 'function'
                      ? column.info.desc(scope.row)
                      : scope.row[column.info.desc]
                  "
                >
                  {{
                    (typeof column.info.desc === "function"
                        ? column.info.desc(scope.row)
                        : scope.row[column.info.desc]) || "-"
                  }}
                </div>
              </div>

              <!-- Custom slot -->
              <slot
                  v-if="scope.$index > -1 && column.slot"
                  :name="column.slot"
                  v-bind="scope"
                  :column_data="column"
              />
            </template>
          </el-table-column>
        </template>

        <template #empty>
          <div class="emptyBg">
            <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
            <p>{{td('common.noData')}}</p>
          </div>
        </template>
      </el-table>
    </div>
    <div
        :class="['qt-table--pagination', config.pagination?.class]"
        v-if="!config.notPagination"
    >
      <el-pagination
          layout="total, sizes, prev, pager, next, jumper"
          :total="store.total"
          v-model:current-page="store.params.pageNum"
          v-model:page-size="store.params.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          v-bind="config.pagination"
          :background="config.pagination?.background || true"
          :pager-count="config.pagination?.pagerCount || store.pagerCount"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script setup name="QtTable">
import { reactive, computed, nextTick } from "vue";
import { useRouter } from "vue-router";
import SvgIcon from "@/components/SvgIcon/index.vue";
import { scrollTo } from "@/utils/scroll-to";
import useDefaultLang from "@/composables/useDefaultLang";

defineOptions({
  inheritAttrs: false,
});

/**
 * props
 * @param {columns} table items
 * @param {Function} Method to obtain table data Parameters: params Need to return a Promise
 * @param {Object} config.table table configuration, please see el-table for details
 * @param {Object} config.pagination paging configuration. Please see el-pagination for details.
 * @param {Bollean} config.pagination.notAutoScroll The function specified after turning off paging
 * @param {Bollean} config.initResquest whether to initialize the request
 * @param {Boolean} config.notPagination does not use paging
 * @param {Boolean} config.notPaginationParams does not use the default pagination parameters
 * @param {Boolean} config.autoPagination front-end paging
 * @param {Object|Boolean} config.sort The key required for backend sorting. If true, use the default value.
 * @param {Object|Boolean} config.sort.prop The key of the sort field defaults to: orderByColumn
 * @param {Object|Boolean} config.sort.order The key of the sorting method defaults to: isAsc
 * @param {Array} column.dict dictionary data
 * @param {Object} column.svg svg icon data
 * @param {String} column.svg.color icon color
 * @param {String} column.svg.className icon class name
 * @param {Object} column.link jump parameter The specific parameters integrate el-link+router.push
 * @param {Object} column.slot Custom slot
 * @param {Object} column.date time parameter will automatically format the time and also supports custom formatting
 * @param {events} event callback
 * @param {Function} events.onLinkClick triggered when link is clicked
 * @param {Function} events.onPageSizeChange Pagination - number of items per page
 * @param {Function} events.onPageCurrentChange Pagination - current page
 * @param {Function} events.onSortChange triggered when sorting
 * @param {Function} events.formatParams The last params processed before the request must have a return value
 * @param {Function} events.formatData processing data must have a return value
 */
const props = defineProps({
  config: {
    type: Object,
    default: () => {
      return {};
    },
  },
  columns: {
    type: Array,
    required: true,
  },
  func: {
    type: Function,
    required: true,
  },
  params: {
    type: Object,
    default: () => {
      return {};
    },
  },
  events: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const router = useRouter();

const elTableRef = ref(null);

const DEFAULT_PAGE_PARAMS = {
  pageNum: 1,
  pageSize: 10,
};

const store = reactive({
  loading: false,
  params: {},
  total: 0,
  data: [],
  rows: [],
  defaultSort: {},
  sort: {
    prop: "orderByColumn",
    order: "isAsc",
  },
  dict: {},
  showTable: true,
  pagerCount: document.body.clientWidth < 1300 ? 4 : 7,
});

const config = computed(() => {
  // Reserved for subsequent collection and merging of default configuration items...
  return props.config || {};
});

const defaultTableSort = computed(() => {
  const { table } = config.value;
  return table?.defaultSort || store.defaultSort;
});

// tabular data
const tableData = computed(() => {
  const { notPagination, autoPagination } = config.value;
  if (notPagination) return store.data;
  const { pageNum, pageSize } = store.params;
  if (autoPagination) {
    return store.data.slice((pageNum - 1) * pageSize, pageNum * pageSize);
  }
  return store.data;
});

// Get data
function getList() {
  store.loading = true;
  const { formatParams, formatData } = props.events;
  const { notPagination, autoPagination } = config.value;
  let params = Object.assign({}, store.params, props.params);
  params = formatParams ? formatParams(params) : params;
  props
      .func(params)
      .then((res) => {
        let data = Array.isArray(res.data) ? res.data : res.data.rows;
        if (!notPagination) {
          store.total = res.data.total;
        }
        if (autoPagination) {
          store.total = data.length;
        }
        store.total = store.total || 0;
        data = formatData ? formatData(data, params) : data;
        store.data = data;
        store.loading = false;
      })
      .catch(() => {
        store.loading = false;
      });
}

// Reset data
function resetQuery() {
  setupDefaultPageParams();
  getList();
}

// Pagination change-number of pages
function handleSizeChange(pageSize) {
  const { onPageSizeChange } = props.events;
  const { pageNum } = store.params;
  if (pageNum * pageSize > store.total) {
    store.params.pageNum = 1;
  }
  const { pagination } = config.value;
  onPageSizeChange && onPageSizeChange({ ...store.params });
  getList();
  if (pagination?.notAutoScroll) return;
  scrollTo(0, 800);
}

// Pagination change-current page
function handleCurrentChange() {
  const { onPageCurrentChange } = props.events;
  const { pagination } = config.value;
  onPageCurrentChange && onPageCurrentChange({ ...store.params });
  getList();
  if (pagination?.notAutoScroll) return;
  scrollTo(0, 800);
}

// Sort change
function handleSortChange({ column, order, prop }) {
  const { onSortChange } = props.events;
  const index = column.getColumnIndex();
  const data = props.columns[index];
  const sort = store.sort;
  store.params[sort.prop] = data.sortableKey || prop;
  store.params[sort.order] = order;
  onSortChange &&
  onSortChange({ ...store.params, ...props.params }, { ...sort });
  getList();
}

// Filter Column data
function getElColumnData(column) {
  const { hide, dict, link, ...otherData } = column;
  return otherData;
}

// link click event
function handleLinkClick(column, row) {
  const { onLinkClick } = props.events;
  const { type, path, name, external, ...other } = column.link;
  onLinkClick && onLinkClick(column, row);
  if (external) return external(row);
  const params = other.params ? other.params(row) : undefined;
  const query = other.query ? other.query(row) : undefined;
  router.push({
    name,
    path,
    params,
    query,
  });
}

// Get dictionary data
function getDictOptions(key) {
  if (store.dict[key]) return store.dict[key];
  const value = proxy.useDict(key)[key];
  store.dict[key] = value;
  return value;
}

// reload
function reload() {
  store.showTable = false;
  nextTick(() => {
    store.showTable = true;
  });
}

// Set paging parameters
function setupDefaultPageParams() {
  const { notPagination, pagination } = config.value;
  if (pagination?.params) {
    const { notPaginationParams } = config.value;
    const defaultParams = notPaginationParams ? { ...DEFAULT_PAGE_PARAMS } : {};
    const params = Object.assign(
        {},
        defaultParams,
        config.value.pagination.params
    );
    for (let key in params) {
      store.params[key] = params[key];
    }
    return;
  }

  if (notPagination) return;

  for (let key in DEFAULT_PAGE_PARAMS) {
    store.params[key] = DEFAULT_PAGE_PARAMS[key];
  }
}

// Initialize sorting parameters
setupDefaultPageParams();

// Initialize sorting parameters
if (config.value.sort) {
  let sort = config.value.sort;
  if (typeof sort == "boolean") {
    sort = { ...store.sort };
  }
  store.sort = { ...sort };
  let defaultSort = defaultTableSort.value;
  if (!Object.keys(defaultSort).length) {
    defaultSort = { prop: "createTime", order: "descending" };
    store.defaultSort = { ...defaultSort };
  }
  store.params[sort.prop] = defaultSort.prop;
  store.params[sort.order] = defaultSort.order;
}

if (config.value.initResquest || config.value.initResquest === undefined) {
  getList();
}

defineExpose({
  getList,
  resetQuery,
  reload,
  elTableRef,
});
</script>

<style lang="scss" scoped>
::v-deep(.el-table) {
  --el-table-header-bg-color: #f1f1f5;
  --el-table-header-text-color: #666;
}

.tip-wrap {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.is-center.el-table__cell {
  .tip-wrap {
    justify-content: center;
  }
}

.qt-table--pagination {
  padding: 20px 10px;
  display: flex;
  justify-content: flex-end;
}

.empty-wrap {
  img {
    width: 380px;
  }
  p {
    font-size: 14px;
    margin: 14px 0;
  }
}
</style>
