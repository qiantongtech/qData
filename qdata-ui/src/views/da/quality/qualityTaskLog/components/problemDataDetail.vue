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
    <el-dialog v-model="visible" :title="td('da.qualityTaskLog.problemDataDetail.title')" class="medium-dialog" @close="handleClose" destroy-on-close>
        <el-descriptions v-if="parsedFields.length" :column="1" border label-class-name="desc-label">
            <el-descriptions-item v-for="(item, index) in parsedFields" :key="index" :label="item.name">
                {{ item.value }}
            </el-descriptions-item>
        </el-descriptions>
        <template #footer>
            <el-button @click="handleClose">{{ td('common.button.close') }}</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, defineExpose, watch } from 'vue'

const { td } = useDefaultLang();
const visible = ref(false)
const detailData = ref(null)
const parsedFields = ref([])

function open(data, value) {
    console.log("🚀 ~ open ~ value:", value)
    detailData.value = data
    visible.value = true
    try {
        const json = JSON.parse(data.dataJsonStr || '{}')

        const labelMap = (value && Array.isArray(value.evaColumns))
            ? value.evaColumns.reduce((map, col) => {
                map[col.name.toLowerCase()] = col.label || col.name
                return map
            }, {})
            : {}

        parsedFields.value = Object.entries(json).map(([key, val]) => ({
            name: labelMap[key.toLowerCase()] || key,
            value: val,
        }))
    } catch (e) {
        parsedFields.value = []
    }
}

function handleClose() {
    visible.value = false
    detailData.value = null
    parsedFields.value = []
}

defineExpose({
    open,
    close: handleClose,
})
</script>
<style scoped lang="scss">
:deep(.el-descriptions__label) {
    width: 300px !important;
    white-space: nowrap;
}
</style>
