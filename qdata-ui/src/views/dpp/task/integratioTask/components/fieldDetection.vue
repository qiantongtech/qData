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
    <el-dialog v-model="visible" :draggable="true" :title="td('dpp.integration.fieldConflictTitle', '字段冲突处理')" :show-close="false" destroy-on-close
        class="MessageBox">
        <div style="padding: 10px 0;">
            {{ td('dpp.integration.fieldConflictMsg', '已有 {existing} 个字段，检测到 {new} 个新字段，如何处理？').replace('{existing}', existingFields.length).replace('{new}', Math.max(0, newFields.length - existingFields.length)) }}
        </div>
        <template #footer>
            <el-button type="warning" @click="handleClick('addNewOnly')">{{ td('dpp.integration.addNewOnly', '增加新的') }}</el-button>
            <el-button type="primary" @click="handleClick('addAll')">{{ td('dpp.integration.addAll', '增加所有') }}</el-button>
            <el-button type="danger" @click="handleClick('clearAndAddAll')">{{ td('dpp.integration.clearAndAddAll', '清除并增加所有') }}</el-button>
            <el-button @click="onCancel">{{ td('common.button.cancel') }}</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import useDefaultLang from "@/composables/useDefaultLang"

const { td } = useDefaultLang();
const props = defineProps({
    modelValue: Boolean,
    existingFields: Array,
    newFields: Array
})

const emit = defineEmits(['update:modelValue', 'resolve'])

const visible = ref(props.modelValue)

watch(() => props.modelValue, val => {
    visible.value = val
})

watch(visible, val => {
    emit('update:modelValue', val)
})

const isAddNewOnlyDisabled = computed(() => {
    if (!props.existingFields || !props.newFields) return true
    const existingNames = props.existingFields.map(f => f.columnName)
    return props.newFields.every(f => existingNames.includes(f.columnName))
})

const isAddAllDisabled = computed(() => {
    return !props.newFields || props.newFields.length === 0
})

const handleClick = (actionType) => {
    emit('resolve', { action: actionType })
    visible.value = false
}

const onCancel = () => {
    emit('resolve', { action: 'cancel' })
    visible.value = false
}
</script>
