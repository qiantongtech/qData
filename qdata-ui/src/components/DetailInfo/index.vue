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

<template>
  <!-- 通用详情页头部组件：标题/头部 + 栅格化信息行 -->
  <div class="pagecont-top pb15" v-show="show">
    <div class="infotop">
      <!-- 标题/头部区域 -->
      <div class="infotop-title mb15" :class="headerClasses">
        <!-- 默认标题（未传 header 时展示） -->
        <template v-if="!header">
          {{ title || "-" }}
        </template>
        <!-- 固定头部：编号方块 + 名称 + 状态字典 + 右侧按钮/插槽 -->
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
              <svg-icon :iconClass="header.backIcon ?? 'fhs'" />
              {{ header.backText ?? "返回" }}
            </el-button>
          </div>
          <slot name="header-right" />
        </template>
      </div>
      <!-- 信息栅格：默认一行 3 个（span=8），可按项覆盖 -->
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
              <!-- 时间展示（示例：{{ parseTime(dppEtlTaskDetail.createTime, "{y}-{m}-{d} {h}:{i}") }}） -->
              <span
                v-else-if="isTimeItem(item)"
                :class="item.ellipsisClass || 'ellipsis'"
                :title="formatTime(resolveValue(item))"
              >
                {{ formatTime(resolveValue(item)) || merged.placeholder }}
              </span>
              <!-- 格式化函数展示 -->
              <span
                v-else-if="item.formatter"
                :class="item.ellipsisClass || 'ellipsis'"
                :title="stringify(resolveFormatted(item))"
              >
                {{ stringify(resolveFormatted(item)) || merged.placeholder }}
              </span>
              <!-- 普通文本展示（支持嵌套 key） -->
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
  <!-- 使用示例：
    <DetailInfo
      :title="data.name"
      :data="data"
      :items="[
        { label: '编号', key: 'id' },
        { label: '状态', key: 'status', dictOptions: sys_disable },
        { label: '描述', key: 'description', span: 12, ellipsisClass: 'ellipsis-2' },
        { label: '备注', key: 'remark', span: 12, ellipsisClass: 'ellipsis' },
        { label: '创建时间', formatter: (d) => proxy.parseTime(d.createTime, '{y}-{m}-{d} {h}:{i}') },
        { label: 'API请求地址', formatter: (d) => `/services/${d.apiVersion}${d.apiUrl}` },
      ]"
      :defaultSpan="8"
      :gutter="2"
    />
  -->
</template>

<script setup name="DetailInfo">
import { useI18n } from 'vue-i18n'
import { useRouter } from "vue-router";

const { t } = useI18n();
// 设计说明：
// - 通用详情页头部展示组件，系统内各模块统一使用；
// - 默认每行 3 项（span=8），通过 items[i].span 调整为 12（每行 2 项）或 24（独占一行）；
// - 支持四种展示方式：slot 插槽、字典组件、formatter 函数、普通文本；
// - 普通文本支持嵌套 key（如 'a.b.c'），为空时展示占位符；
// - 描述、备注这类字段建议设置 span=12，使其在一行展示 2 个；

const { proxy } = getCurrentInstance();

const props = defineProps({
  // 标题文本
  title: { type: String, default: "-" },
  // 是否显示
  show: { type: Boolean, default: true },
  // 头部配置（可展示：编号、名称、状态、返回按钮）
  header: {
    type: Object,
    default: null, // { idKey?, nameKey, statusKey, statusOptions, backText?, backIcon?, className? }
  },
  // 行间距
  gutter: { type: Number, default: 2 },
  // 默认每项 span（不传则一行 3 个）
  defaultSpan: { type: Number, default: 8 },
  // 详情数据源对象
  data: { type: Object, default: () => ({}) },
  // 配置项列表
  items: {
    type: Array,
    default: () => [],
  },
  // 空值占位符
  placeholder: { type: String, default: "-" },
  // 时间类型默认格式
  timeFormat: { type: String, default: "{y}-{m}-{d} {h}:{i}" },
  // 展示模式：'default' 使用固定布局；'free' 使用传入 items 布局
  mode: { type: String, default: "default" },
  // 可选配置对象，便于一次性设置不常改的参数
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

// 合并配置：config 优先，其次使用单独 props，最后使用默认
const merged = computed(() => ({
  gutter: props.config.gutter ?? props.gutter,
  defaultSpan: props.config.defaultSpan ?? props.defaultSpan,
  placeholder: props.config.placeholder ?? props.placeholder,
  timeFormat: props.config.timeFormat ?? props.timeFormat,
  mode: props.config.mode ?? props.mode,
}));

// 解析嵌套 key：支持 'a.b.c' 访问
function getByPath(obj, path) {
  if (!obj || !path) return undefined;
  if (typeof path !== "string") return obj[path];
  return path.split(".").reduce((acc, k) => (acc ? acc[k] : undefined), obj);
}

// 将任意值转为字符串（避免 [object Object]）
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

// 普通/字典项取值
function resolveValue(item) {
  // key 优先，其次直接读取 item.value，最后整个 data
  if (item.key) return getByPath(props.data, item.key);
  if (typeof item.value !== "undefined") return item.value;
  return props.data;
}

// 判断是否为时间项（type='time' 或 key 以 'time' 结尾）
function isTimeItem(item) {
  return (
    item?.type === "time" ||
    (typeof item?.key === "string" && /time$/i.test(item.key))
  );
}

// 时间格式化（模板中直接调用，简单直观）
function formatTime(val) {
  return val === null || val === undefined
    ? ""
    : proxy.parseTime?.(val, merged.value.timeFormat) ?? val;
}

// formatter 格式化取值
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
        label: t('common.texts.description'),
        key: "description",
        span: 24,
        ellipsisClass: "ellipsis",
        className: "mt2 mb2",
      },
      { label: t('common.texts.createdBy'), key: "createBy", span: 8 },
      { label: t('common.texts.createdTime'), key: "createTime", type: "time", span: 8 },
      { label: t('common.texts.updatedTime'), key: "updateTime", type: "time", span: 8 },
      {
        label: t('common.texts.remark'),
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
