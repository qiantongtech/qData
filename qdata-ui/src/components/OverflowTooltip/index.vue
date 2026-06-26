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
    <el-tooltip effect="dark" :content="text" placement="top-start" :disabled="!isOverflow">
        <span ref="textRef" class="overflow-text" :style="{ maxWidth }">{{ text }}</span>
    </el-tooltip>
</template>

<script setup>
import { ref, onMounted, nextTick, watch, onBeforeUnmount } from 'vue';

const props = defineProps({
    text: { type: String, required: true },
    maxWidth: { type: String, default: '200px' }, // 必须带单位
});

const textRef = ref(null);
const isOverflow = ref(false);

// 测量文字是否溢出
const measure = () => {
    if (textRef.value) {
        isOverflow.value = textRef.value.scrollWidth > textRef.value.offsetWidth;
    }
};

// 初始化和窗口 resize
onMounted(() => {
    nextTick(() => requestAnimationFrame(measure));
    window.addEventListener('resize', measure);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', measure);
});

// 监听文字或 maxWidth 变化
watch([() => props.text, () => props.maxWidth], () => {
    nextTick(() => requestAnimationFrame(measure));
});
</script>

<style scoped>
.overflow-text {
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
}
</style>
