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
-->

<template>
  <div :class="['dict-tag-container', `dict-tag-size-${size}`]">
    <template v-if="$slots.default">
      <span
          v-if="
          (type == 'default' || type == '') &&
          (className == '' || className == null)
        "
          class="dict-tag-span"
          :class="className"
      >
        <img
            v-if="currentIconUrl"
            :src="currentIconUrl"
            class="dict-tag-icon"
        />
        <span><slot></slot></span>
      </span>
      <el-tag
          v-else
          :disable-transitions="true"
          :size="size"
          :type="type === 'primary' ? '' : type"
          :class="['dict-tag-el', className]"
      >
        <img
            v-if="currentIconUrl"
            :src="currentIconUrl"
            class="dict-tag-icon"
        />
        <span><slot></slot></span>
      </el-tag>
    </template>
    <template v-else-if="renderedItems.length > 0">
      <template v-for="(item, index) in renderedItems" :key="item.key || index">
        <span v-if="item.isSpan" class="dict-tag-span" :class="item.elTagClass">
          <img v-if="item.iconUrl" :src="item.iconUrl" class="dict-tag-icon" />
          <span>{{ item.label }}</span>
        </span>
        <el-tag
            v-else
            :disable-transitions="true"
            :size="size"
            :type="item.elTagType === 'primary' ? '' : item.elTagType"
            :class="['dict-tag-el', item.elTagClass]"
        >
          <img v-if="item.iconUrl" :src="item.iconUrl" class="dict-tag-icon" />
          <span>{{ item.label }}</span>
        </el-tag>
      </template>
    </template>
    <template v-else>
      <span class="empty-placeholder">-</span>
    </template>
  </div>
</template>

<script setup>
import { getDatasourceIcon } from "@/utils/datasource";

const props = defineProps({
  // 数据
  options: {
    type: Array,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
  // 当未找到匹配的数据时，显示value
  showValue: {
    type: Boolean,
    default: true,
  },
  separator: {
    type: String,
    default: ",",
  },
  // 标签尺寸
  size: {
    type: String,
    default: "default",
  },
  // 标签类型 (用于直接使用 slot 或无 options 时)
  type: {
    type: String,
    default: "",
  },
  // 图标地址 (用于直接使用 slot 或无 options 时)
  icon: String,
  // 数据源类型 (用于直接使用 slot 或无 options 时获取图标)
  datasourceType: String,
  // 自定义类名 (用于直接使用 slot 或无 options 时)
  className: String,
});

const values = computed(() => {
  if (
      props.value === null ||
      typeof props.value === "undefined" ||
      props.value === ""
  )
    return [];
  return Array.isArray(props.value)
      ? props.value.map((item) => "" + item)
      : String(props.value).split(props.separator);
});

/** 需要渲染的项目列表 */
const renderedItems = computed(() => {
  const items = [];
  if (props.options && props.options.length > 0) {
    // 1. 处理有 options 的情况
    values.value.forEach((val) => {
      const option = props.options.find(
          (opt) => String(opt.value) === String(val)
      );
      if (option) {
        items.push({
          ...option,
          key: option.value,
          label: option.label,
          elTagType: option.elTagType || "",
          elTagClass: option.elTagClass || "",
          iconUrl: getIconUrl(option),
          isSpan:
              (option.elTagType == "default" || option.elTagType == "") &&
              (option.elTagClass == "" || option.elTagClass == null),
        });
      } else if (props.showValue) {
        // 未匹配且开启显示原值
        items.push({
          key: val,
          label: val,
          elTagType: props.type,
          elTagClass: props.className,
          iconUrl:
              props.icon ||
              (props.datasourceType
                  ? getDatasourceIcon(props.datasourceType)
                  : ""),
          isSpan:
              (props.type == "default" || props.type == "") &&
              (props.className == "" || props.className == null),
        });
      }
    });
  } else {
    // 2. 处理无 options 的情况 (直接展示 value)
    values.value.forEach((val) => {
      items.push({
        key: val,
        label: val,
        elTagType: props.type,
        elTagClass: props.className,
        iconUrl:
            props.icon ||
            (props.datasourceType ? getDatasourceIcon(props.datasourceType) : ""),
        isSpan:
            (props.type == "default" || props.type == "") &&
            (props.className == "" || props.className == null),
      });
    });
  }
  return items;
});

/** 直接使用 slot 时的图标地址 */
const currentIconUrl = computed(() => {
  if (props.icon) return props.icon;
  if (props.datasourceType) return getDatasourceIcon(props.datasourceType);
  return "";
});

/** 获取图标地址 */
function getIconUrl(item) {
  if (item.icon) return item.icon;
  if (item.datasourceType) return getDatasourceIcon(item.datasourceType);
  if (item.DATASOURCE_TYPE) return getDatasourceIcon(item.DATASOURCE_TYPE);
  return "";
}
</script>

<style lang="scss" scoped>
.dict-tag-container {
  display: inline-block;
}
.el-tag + .el-tag {
  //margin-left: 10px;
}

.dict-tag-el {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  vertical-align: middle;

  :deep(.el-tag__content) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
}

.dict-tag-span {
  display: inline-flex;
  align-items: center;
  vertical-align: middle;

  & + .dict-tag-span {
    margin-left: 10px;
  }
}

.dict-tag-icon {
  width: 1.1em;
  height: 1.1em;
  margin-right: 4px;
  object-fit: contain;
  flex-shrink: 0;
}

.empty-placeholder {
  color: #999;
}

/* 针对 small 尺寸进行深度压缩 */
:deep(.el-tag--small) {
  height: 20px;
  padding: 3px 6px;
  font-size: 10px !important;
  line-height: 18px;
  .dict-tag-icon {
    width: 12px;
    height: 12px;
    margin-right: 3px;
  }
}

.dict-tag-size-small {
  :deep(.el-tag + .el-tag) {
    margin-left: 6px;
  }

  .dict-tag-span + .dict-tag-span {
    margin-left: 6px;
  }

  .dict-tag-span {
    font-size: 12px;
    .dict-tag-icon {
      width: 12px;
      height: 12px;
      margin-right: 3px;
    }
  }

  .empty-placeholder {
    font-size: 12px;
  }
}
</style>
