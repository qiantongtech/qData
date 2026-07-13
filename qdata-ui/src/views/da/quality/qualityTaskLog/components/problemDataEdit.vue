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
    <el-dialog v-model="visible" :title="td('da.qualityTaskLog.problemDataEdit.title')" class="medium-dialog" @close="handleClose" destroy-on-close>
        <el-form :model="formData" label-width="240px" :label-position="labelPosition">
            <el-form-item v-for="item in parsedFields" :key="item.name" :label="item.name" :label-position="labelPosition">
                <el-input v-model="formData[item.name]" :disabled="!item.editable" />
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button @click="handleClose">{{ td('common.button.close') }}</el-button>
            <el-button type="primary" @click="handleok">{{ td('common.button.save') }}</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"
import { ref, defineExpose, defineEmits } from 'vue'

const { td } = useDefaultLang();
const emit = defineEmits(['ok'])

const visible = ref(false)
const detailData = ref(null)
const parsedFields = ref([])
const formData = ref({})

function open(data, value, columnName) {
    detailData.value = data
    visible.value = true

    try {
        const json = JSON.parse(data.dataJsonStr || '{}')
        console.log("🚀 ~ open ~ json:", json)

        const labelMap = (value && Array.isArray(value.evaColumns))
            ? value.evaColumns.reduce((map, col) => {
                map[col.name.toLowerCase()] = col.label || col.name
                return map
            }, {})
            : {}

        const editableFields = columnName
            ? columnName.split(',').map(n => n.trim().toLowerCase())
            : []

        formData.value = {}
        parsedFields.value = Object.entries(json).map(([key, val]) => {
            const lowerKey = key.toLowerCase()
            const label = labelMap[lowerKey] || key

            formData.value[label] = val

            const isEditable = editableFields.includes(lowerKey)

            return {
                label: key,
                name: label,
                value: val,
                editable: isEditable,
            }
        })
    } catch (e) {
        parsedFields.value = []
        formData.value = {}
    }
}

function handleClose() {
    visible.value = false
    detailData.value = null
    parsedFields.value = []
    formData.value = {}
}

function handleok() {
    const originalJson = JSON.parse(detailData.value?.dataJsonStr || '{}')
    const keyWordData = {}

    for (const field of parsedFields.value) {
        if (!field.editable) continue

        const label = field.name   // Chinese display name
        const newValue = formData.value[label]
        const originKey = Object.keys(originalJson).find(
            k => k.toLowerCase() === field.label.toLowerCase()
        )
        const oldValue = originalJson[originKey]

        // ⚠️ Use field.label as key
        if (newValue != oldValue) {
            keyWordData[field.label] = newValue
        }
    }

    if (Object.keys(keyWordData).length === 0) {
        ElMessage.warning(td('da.qualityTaskLog.problemDataEdit.noChanges'))
        return
    }

    // Finally formData is converted to label-key object
    const result = Object.entries(formData.value).reduce((acc, [key, val]) => {
        const field = parsedFields.value.find(f => f.name === key)
        if (field) {
            acc[field.label] = val
        }
        return acc
    }, {})

    emit('ok', { ...result }, detailData.value, keyWordData)
    console.log("🚀 ~ handleok ~ result:", result)
    console.log("🚀 ~ handleok ~ keyWordData:", keyWordData)
    handleClose()
}


defineExpose({
    open,
    close: handleClose,
})
</script>

<style scoped lang="scss">
.el-form-item {
    margin-bottom: 20px;
}
</style>
