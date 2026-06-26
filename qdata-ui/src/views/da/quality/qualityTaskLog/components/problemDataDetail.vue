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
