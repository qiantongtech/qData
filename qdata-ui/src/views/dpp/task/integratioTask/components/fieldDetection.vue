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
    <el-dialog v-model="visible" :draggable="true" :title="td('dpp.integration.fieldConflictTitle', 'Field Conflict Handling')" :show-close="false" destroy-on-close
        class="MessageBox">
        <div style="padding: 10px 0;">
            {{ td('dpp.integration.fieldConflictMsg', 'Already have {existing} fields, detected {new} new fields. How to handle?', { existing: existingFields.length, new: Math.max(0, newFields.length - existingFields.length) }) }}
        </div>
        <template #footer>
            <el-button type="warning" @click="handleClick('addNewOnly')">{{ td('dpp.integration.addNewOnly', 'Add New Only') }}</el-button>
            <el-button type="primary" @click="handleClick('addAll')">{{ td('dpp.integration.addAll', 'Add All') }}</el-button>
            <el-button type="danger" @click="handleClick('clearAndAddAll')">{{ td('dpp.integration.clearAndAddAll', 'Clear and Add All') }}</el-button>
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
