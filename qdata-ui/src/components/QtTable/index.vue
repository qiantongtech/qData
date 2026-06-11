<!--
  Copyright © 2025 Qiantong Technology Co., Ltd.
  qData Data Middle Platform (Open Source Edition)
   *
  License:
  Released under the Apache License, Version 2.0.
  You may use, modify, and distribute this software for commercial purposes
  under the terms of the License.
   *
  Special Notice:
  All derivative versions are strictly prohibited from modifying or removing
  the default system logo and copyright information.
  For brand customization, please apply for brand customization authorization via official channels.
   *
  More information: https://qdata.qiantong.tech/business.html
   *
  ============================================================================
   *
  版权所有 © 2025 江苏千桐科技有限公司
  qData 数据中台（开源版）
   *
  许可协议：
  本项目基于 Apache License 2.0 开源协议发布，
  允许在遵守协议的前提下进行商用、修改和分发。
   *
  特别说明：
  所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
  如需定制品牌，请通过官方渠道申请品牌定制授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
-->

<!--
    qt-table 组件
    说明：基于element-plus的表格封装，集成了分页、排序、字典、图标、链接等功能
    注意：不要私自修改本组件中的代码 有问题先联系wy
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
              <!-- 空数据处理 -->
              <template
                  v-if="
                  column.prop &&
                  [undefined, null].includes(scope.row[column.prop])
                "
              >
                {{ getFormatValue(scope.row[column.prop]) }}
              </template>

              <!-- 字典 -->
              <dict-tag
                  :options="getDictOptions(column.dict)"
                  v-if="column.dict"
                  :value="scope.row[column.prop]"
              />

              <!-- 标签 -->
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

              <!-- 链接 -->
              <el-link
                  v-bind="column.link"
                  :underline="column.link?.underline || 'never'"
                  :type="column.link?.type || 'primary'"
                  v-if="column.link"
                  @click="handleLinkClick(column, scope.row)"
              >
                {{ scope.row[column.prop] }}
              </el-link>

              <!-- 图标 -->
              <svg-icon
                  v-bind="column.svg"
                  v-if="column.svg"
                  :icon-class="scope.row[column.prop]"
              />

              <!-- 统一处理时间 -->
              <template v-if="column.date">
                {{
                  parseTime(
                      scope.row[column.prop],
                      column.date === true ? "{y}-{m}-{d} {h}:{i}" : column.date
                  )
                }}
              </template>

              <!-- 多字段展示 -->
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
              <!-- 信息列 (图标 + 标题 + 标签 / 描述) -->
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

              <!-- 自定义slot -->
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
            <img src="@/assets/system/images/no_data/noData.png" alt="" />
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

defineOptions({
  inheritAttrs: false,
});

/**
 * props
 * @param {columns} 表格项
 * @param {Function} 获取表格数据的方法 参数:params 需返回一个Promise
 * @param {Object} config.table 表格配置 具体请查看el-table
 * @param {Object} config.pagination 分页配置 具体请查看 el-pagination
 * @param {Bollean} config.pagination.notAutoScroll 关闭分页后指定的功能
 * @param {Bollean} config.initResquest 是否初始化请求
 * @param {Boolean} config.notPagination 不使用分页
 * @param {Boolean} config.notPaginationParams 不使用默认的分页参数
 * @param {Boolean} config.autoPagination 前端分页
 * @param {Object|Boolean} config.sort 后端排序所需的key 如果为true则使用默认值
 * @param {Object|Boolean} config.sort.prop 排序字段的key 默认为：orderByColumn
 * @param {Object|Boolean} config.sort.order 排序方式的key 默认为：isAsc
 * @param {Array} column.dict 字典数据
 * @param {Object} column.svg svg图标数据
 * @param {String} column.svg.color 图标颜色
 * @param {String} column.svg.className 图标类名
 * @param {Object} column.link 跳转参数 具体参数整合了el-link+router.push
 * @param {Object} column.slot 自定义插槽
 * @param {Object} column.date 时间参数 会自动格式化时间 也支持自定义格式化
 * @param {events} 事件回调
 * @param {Function} events.onLinkClick 点击link时触发
 * @param {Function} events.onPageSizeChange 分页-每页条数
 * @param {Function} events.onPageCurrentChange 分页-当前页
 * @param {Function} events.onSortChange 排序时触发
 * @param {Function} events.formatParams 请求前最后处理params必须有返回值
 * @param {Function} events.formatData 处理data必须有返回值
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
  // 留作后续收集合并默认配置项处理...
  return props.config || {};
});

const defaultTableSort = computed(() => {
  const { table } = config.value;
  return table?.defaultSort || store.defaultSort;
});

// 表格数据
const tableData = computed(() => {
  const { notPagination, autoPagination } = config.value;
  if (notPagination) return store.data;
  const { pageNum, pageSize } = store.params;
  if (autoPagination) {
    return store.data.slice((pageNum - 1) * pageSize, pageNum * pageSize);
  }
  return store.data;
});

// 获取数据
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

// 重置数据
function resetQuery() {
  setupDefaultPageParams();
  getList();
}

// 分页change-页数
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

// 分页change-当前页
function handleCurrentChange() {
  const { onPageCurrentChange } = props.events;
  const { pagination } = config.value;
  onPageCurrentChange && onPageCurrentChange({ ...store.params });
  getList();
  if (pagination?.notAutoScroll) return;
  scrollTo(0, 800);
}

// 排序change
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

// 过滤Column数据
function getElColumnData(column) {
  const { hide, dict, link, ...otherData } = column;
  return otherData;
}

// link点击事件
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

// 获取字典数据
function getDictOptions(key) {
  if (store.dict[key]) return store.dict[key];
  const value = proxy.useDict(key)[key];
  store.dict[key] = value;
  return value;
}

// 重新加载
function reload() {
  store.showTable = false;
  nextTick(() => {
    store.showTable = true;
  });
}

// 设置分页参数
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

// 初始化排序参数
setupDefaultPageParams();

// 初始化排序参数
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
