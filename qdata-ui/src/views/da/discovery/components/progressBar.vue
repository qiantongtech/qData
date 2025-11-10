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
    <div class="progress-container">
        <div class="progress-bar">
            <div class="progress completed" :style="{ width: completedWidth + '%' }"></div>
            <div class="progress in-progress" :style="{ width: inProgressWidth + '%' }"></div>
            <div class="progress not-started" :style="{ width: notStartedWidth + '%' }"></div>
        </div>
        <div class="labels">
            <div class="label ">
                <span class="dot" :style="{ backgroundColor: '#4CAF50' }"></span> 已提交 {{ completed }}
            </div>
            <div class="label ">
                <span class="dot" :style="{ backgroundColor: '#FFA500' }"></span> 待提交 {{ inProgress }}
            </div>
            <div class="label ">
                <span class="dot" :style="{ backgroundColor: '#B0B0B0' }"></span> 已忽略 {{ notStarted }}
            </div>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
    completed: {
        type: Number,
        required: true,
        default: 0
    },
    inProgress: {
        type: Number,
        required: true,
        default: 0
    },
    notStarted: {
        type: Number,
        required: true,
        default: 0

    },
});

// 确保 `total` 是有效的数字，避免 NaN
const total = computed(() => {
    const validCompleted = Number(props.completed) || 0;
    const validInProgress = Number(props.inProgress) || 0;
    const validNotStarted = Number(props.notStarted) || 0;
    return validCompleted + validInProgress + validNotStarted;
});

const completedWidth = computed(() => {
    return total.value ? (props.completed / total.value) * 100 : 0;
});

const inProgressWidth = computed(() => {
    return total.value ? (props.inProgress / total.value) * 100 : 0;
});

const notStartedWidth = computed(() => {
    return total.value ? (props.notStarted / total.value) * 100 : 0;
});
</script>

<style scoped>
.progress-container {
    width: 100%;
    max-width: 600px;
    margin: 0 auto;
}

.progress-bar {
    display: flex;
    height: 10px;
    overflow: hidden;
    margin-bottom: 5px;
}

.progress {
    height: 100%;
}

.completed {
    background-color: #4CAF50;
}

.in-progress {
    background-color: #FFA500;
}

.not-started {
    background-color: #B0B0B0;
}

.labels {
    display: flex;
    justify-content: space-between;
}

.label {
    font-size: 14px;
}

.dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    margin-right: 6px;
    display: inline-block;
    vertical-align: middle;
}
</style>