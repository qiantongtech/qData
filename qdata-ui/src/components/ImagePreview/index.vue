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
  <el-image
    :src="`${realSrc}`"
    fit="cover"
    :style="`width:${realWidth};height:${realHeight};`"
    :preview-src-list="realSrcList"
    preview-teleported
  >
    <template #error>
      <div class="image-slot">
        <el-icon><picture-filled /></el-icon>
      </div>
    </template>
  </el-image>
</template>

<script setup>
import { isExternal } from "@/utils/validate";

const props = defineProps({
  src: {
    type: String,
    default: ""
  },
  width: {
    type: [Number, String],
    default: ""
  },
  height: {
    type: [Number, String],
    default: ""
  }
});

const realSrc = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src = props.src.split(",")[0];
  if (isExternal(real_src)) {
    return real_src;
  }
  return import.meta.env.VITE_APP_BASE_API + real_src;
});

const realSrcList = computed(() => {
  if (!props.src) {
    return;
  }
  let real_src_list = props.src.split(",");
  let srcList = [];
  real_src_list.forEach(item => {
    if (isExternal(item)) {
      return srcList.push(item);
    }
    return srcList.push(import.meta.env.VITE_APP_BASE_API + item);
  });
  return srcList;
});

const realWidth = computed(() =>
  typeof props.width == "string" ? props.width : `${props.width}px`
);

const realHeight = computed(() =>
  typeof props.height == "string" ? props.height : `${props.height}px`
);
</script>

<style lang="scss" scoped>
.el-image {
  border-radius: 5px;
  background-color: #ebeef5;
  box-shadow: 0 0 5px 1px #ccc;
  :deep(.el-image__inner) {
    transition: all 0.3s;
    cursor: pointer;
    &:hover {
      transform: scale(1.2);
    }
  }
  :deep(.image-slot) {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    color: #909399;
    font-size: 30px;
  }
}
</style>
