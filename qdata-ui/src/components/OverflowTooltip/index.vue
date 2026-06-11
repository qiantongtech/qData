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
