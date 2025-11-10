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
  For brand customization, please contact the official team for authorization.
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
  如需定制品牌，请通过官方渠道申请品牌授权。
   *
  更多信息请访问：https://qdata.qiantong.tech/business.html
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
