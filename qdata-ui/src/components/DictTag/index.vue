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
  <div :class="['dict-tag-container', `dict-tag-size-${size}`]">
    <template v-if="$slots.default">
      <span
        v-if="type === 'dot'"
        class="dict-tag-dot"
        :class="[className, `dict-tag-dot-type-${type}`]"
        :style="{ '--dot-color': dotColor }"
      ></span>
      <span
        v-else-if="
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
        <span class="dict-tag-label" :style="{ maxWidth }">{{ labelPrefix }}<slot></slot>{{ labelSuffix }}</span>
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
        <span class="dict-tag-label" :style="{ maxWidth }">{{ labelPrefix }}<slot></slot>{{ labelSuffix }}</span>
      </el-tag>
    </template>
    <template v-else-if="renderedItems.length > 0">
      <template v-for="(item, index) in renderedItems" :key="item.key || index">
        <span
          v-if="type === 'dot'"
          class="dict-tag-dot"
          :class="[item.elTagClass, `dict-tag-dot-type-${item.elTagType}`]"
          :style="{ '--dot-color': getItemDotColor(item) }"
        ></span>
        <span
          v-else-if="item.isSpan"
          class="dict-tag-span"
          :class="item.elTagClass"
        >
          <img v-if="item.iconUrl" :src="item.iconUrl" class="dict-tag-icon" />
          <span class="dict-tag-label" :style="{ maxWidth }">{{ labelPrefix }}{{ item.label }}{{ labelSuffix }}</span>
        </span>
        <el-tag
          v-else
          :disable-transitions="true"
          :size="size"
          :type="item.elTagType === 'primary' ? '' : item.elTagType"
          :class="['dict-tag-el', item.elTagClass]"
        >
          <img v-if="item.iconUrl" :src="item.iconUrl" class="dict-tag-icon" />
          <span class="dict-tag-label" :style="{ maxWidth }">{{ labelPrefix }}{{ item.label }}{{ labelSuffix }}</span>
        </el-tag>
      </template>
    </template>
    <template v-else>
      <span v-if="showPlaceholder" class="empty-placeholder">-</span>
    </template>
  </div>
</template>

<script setup>
import { getDatasourceIcon } from "@/utils/datasource";
import { useDict } from "@/utils/dict";
import { ElTag } from "element-plus";

const props = defineProps({
  // data
  options: {
    type: Array,
    default: null,
  },
  // dictionary name
  dict: String,
  // current value
  value: [Number, String, Array],
  // When no matching data is found, value is displayed
  showValue: {
    type: Boolean,
    default: true,
  },
  separator: {
    type: String,
    default: ",",
  },
  // label size
  size: {
    type: String,
    default: "default",
  },
  // Tag type (used when using slot directly or without options)
  type: {
    type: String,
    default: "",
  },
  // Icon address (used when using slot directly or without options)
  icon: String,
  // Data source type (used to get the icon when using slot directly or without options)
  datasourceType: String,
  // Custom class name (for when using slot directly or without options)
  className: String,
  // Custom color (for dot type)
  color: String,
  // Custom label text, if passed, overrides the default label
  label: {
    type: String,
    default: "",
  },
  // label prefix
  labelPrefix: {
    type: String,
    default: "",
  },
  // label suffix
  labelSuffix: {
    type: String,
    default: "",
  },
  // Whether to display the placeholder "-" when there is no value, default is true
  showPlaceholder: {
    type: Boolean,
    default: true,
  },
  // Whether to show the icon
  showIcon: {
    type: Boolean,
    default: true,
  },
  // Maximum width for ellipsis
  maxWidth: {
    type: String,
    default: "none",
  },
});

// Get dictionary data
let dictData = ref([]);
if (props.dict) {
  const dictResult = useDict(props.dict);
  dictData = dictResult[props.dict];
}

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

/** List of items that need to be rendered */
const renderedItems = computed(() => {
  const items = [];
  const currentOptions =
    props.options && props.options.length > 0
      ? props.options
      : dictData && dictData.value
      ? dictData.value
      : [];

  if (currentOptions.length > 0) {
    // 1. Handle the situation with options or dictionary
    values.value.forEach((val) => {
      const option = currentOptions.find(
        (opt) => String(opt.value) === String(val)
      );
      if (option) {
        items.push({
          ...option,
          key: option.value,
          label: props.label || option.label,
          elTagType: option.elTagType || "",
          elTagClass: option.elTagClass || "",
          color: option.color,
          iconUrl: getIconUrl(option),
          isSpan:
            (option.elTagType == "default" || option.elTagType == "") &&
            (option.elTagClass == "" || option.elTagClass == null),
        });
      } else if (props.showValue) {
        // Unmatched and enabled to display original value
        items.push({
          key: val,
          label: props.label || val,
          elTagType: props.type,
          elTagClass: props.className,
          color: props.color,
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
    // 2. Handle the situation without options (display value directly)
    values.value.forEach((val) => {
      items.push({
        key: val,
        label: props.label || val,
        elTagType: props.type,
        elTagClass: props.className,
        color: props.color,
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

/** Get dot color */
function getDotColor(tagType) {
  const colorMap = {
    primary: "var(--el-color-primary)",
    success: "var(--el-color-success)",
    warning: "var(--el-color-warning)",
    danger: "var(--el-color-danger)",
    info: "var(--el-color-info)",
    "": "var(--el-color-primary)",
    default: "var(--el-color-primary)",
  };
  return colorMap[tagType] || "var(--el-color-primary)";
}

/** Get item dot color */
function getItemDotColor(item) {
  if (item.color) return item.color;
  return getDotColor(item.elTagType);
}

/** Icon address when using slot directly */
const currentIconUrl = computed(() => {
  if (!props.showIcon) return "";
  if (props.icon) return props.icon;
  if (props.datasourceType) return getDatasourceIcon(props.datasourceType);
  return "";
});

/** Get dot color */
const dotColor = computed(() => {
  if (props.color) return props.color;
  return getDotColor(props.type);
});

/** Get icon address */
function getIconUrl(item) {
  if (!props.showIcon) return "";
  if (item.icon) return item.icon;
  if (item.datasourceType) return getDatasourceIcon(item.datasourceType);
  if (item.DATASOURCE_TYPE) return getDatasourceIcon(item.DATASOURCE_TYPE);
  return "";
}
</script>

<style lang="scss" scoped>
.dict-tag-container {
  display: inline-block;
  max-width: 100%;
}
.el-tag + .el-tag {
  //margin-left: 10px;
}

.dict-tag-el {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  vertical-align: middle;
  max-width: 100%;

  :deep(.el-tag__content) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.dict-tag-span {
  display: inline-flex;
  align-items: center;
  vertical-align: middle;
  max-width: 100%;

  & + .dict-tag-span {
    margin-left: 10px;
  }
}

.dict-tag-label {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}

.dict-tag-icon {
  width: 1.1em;
  height: 1.1em;
  margin-right: 4px;
  object-fit: contain;
  flex-shrink: 0;
}

.dict-tag-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: var(--dot-color);
  margin-right: 8px;
  vertical-align: middle;
}

.empty-placeholder {
  color: #999;
}

/* Modify el-tag border color */
:deep(.el-tag) {
  border-color: rgba(50, 100, 255, 0.2);
}

/* Deep compression for small sizes */
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

  .dict-tag-dot + .dict-tag-dot {
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
