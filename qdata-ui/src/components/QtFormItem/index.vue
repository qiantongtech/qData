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

  Business License: https://docs.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <!-- No! of -->
  <el-form-item v-bind="$attrs">
    <template #default="scope">
      <div class="default-wrap">
        <slot name="default" v-bind="scope || {}" />
      </div>
    </template>

    <template #label="scope">
        <slot name="label" v-bind="scope">
          {{ scope.label }}
        </slot>
    </template>
  </el-form-item>
</template>

<script setup name="QtFromItem">
import { computed } from "vue";

const props = defineProps({
  tip: {
    type: [String, Object],
    default: "",
  },
});

const isString = computed(() => {
  return typeof props.tip === "string";
});
</script>

<style lang="scss" scoped>
.default-wrap {
  width: 100%;
  position: relative;

  .tip-content {
    display: flex;
    align-items: center;
    gap: 2px;
    color: #888;
    font-size: 12px;
    line-height: 1.5;
    padding-top: 4px;
    white-space: wrap;
  }
}

.label-wrap {
  display: flex;
  align-items: center;
  gap: 2px;
  .el-icon {
    color: #888;
  }
}

::v-deep(.el-form-item__error) {
  padding-top: 6px;
}

.el-form-item.is-error {
  padding-bottom: 16px;
  .tip-content {
    display: none;
  }
}
</style>
