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
  <!-- 通用描述信息组件：基于 Element Plus el-descriptions，支持字典、时间、标签、格式化 -->
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

        <!-- 自定义插槽 -->
        <slot v-if="item.slot" :name="item.slot" :item="item" :data="data" />

        <!-- 标签类型 -->
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

        <!-- 字典类型 -->
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

        <!-- 时间类型 -->
        <span v-else-if="isTimeItem(item)">
          {{ formatTime(resolveValue(item)) || placeholder }}
        </span>

        <!-- 自定义格式化 -->
        <span v-else-if="item.formatter">
          {{ stringify(resolveFormatted(item)) || placeholder }}
        </span>

        <!-- 普通文本 -->
        <span v-else>
          {{ stringify(resolveValue(item)) || placeholder }}
        </span>
      </el-descriptions-item>
    </el-descriptions>
  </div>
  <!-- 使用示例：
    <DescriptionsInfo
      title="基础信息"
      :data="detail"
      :items="[
        { label: '文件名称', key: 'fileName' },
        { label: '状态', key: 'status', dictOptions: dp_document_status },
        { label: '标签', key: 'tags', type: 'tags' },
        { label: '创建时间', key: 'createTime', type: 'time' },
        { label: '创建人', key: 'createBy' },
        { label: '更新时间', key: 'updateTime', type: 'time' },
        { label: '备注', key: 'remark', span: 2 },
        { label: '创建类型', key: 'createType', formatter: (v) => v === 1 ? '虚拟资产创建' : '完整资产创建' },
      ]"
    />
  -->
</template>

<script setup name="DescriptionsInfo">
/**
 * 设计说明：
 * - 通用全局组件，统一封装 el-descriptions 的常见展示能力；
 * - 支持四类展示：tags 标签、字典 dictOptions、时间 type='time'、自定义 formatter、普通文本；
 * - items 支持 span、className、key（支持嵌套 a.b.c）、value（直接传值）、dictOptions、formatter；
 * - 通过 props.placeholder 控制空值展示占位符（默认 "-"）；
 * - 通过 props.timeFormat 控制时间格式（默认 "{y}-{m}-{d} {h}:{i}"）；
 */

const { proxy } = getCurrentInstance();

const props = defineProps({
  // 标题（与 el-descriptions 保持一致）
  title: { type: String, default: "" },
  // 数据源对象
  data: { type: Object, default: () => ({}) },
  // 配置项
  items: { type: Array, default: () => [] },
  // 列数
  column: { type: Number, default: 2 },
  // 是否显示边框
  border: { type: Boolean, default: true },
  // 标签类名
  labelClass: { type: String, default: "base-label" },
  // 内容类名
  contentClass: { type: String, default: "base-content" },
  // 空值占位
  placeholder: { type: String, default: "-" },
  // 默认时间格式
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
