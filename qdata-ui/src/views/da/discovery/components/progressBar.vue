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