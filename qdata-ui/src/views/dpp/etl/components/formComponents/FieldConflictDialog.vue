<template>
    <el-dialog v-model="visible" :draggable="true" title="字段冲突处理" :show-close="false" destroy-on-close
        class="MessageBox">
        <div style="padding: 10px 0;">
            已有 {{ existingFields.length }} 个字段，检测到
            {{ Math.max(0, newFields.length - existingFields.length) }} 个新字段，如何处理？
        </div>
        <template #footer>
            <el-button type="warning" @click="handleClick('addNewOnly')">增加新的</el-button>
            <el-button type="primary" @click="handleClick('addAll')">增加所有</el-button>
            <el-button type="danger" @click="handleClick('clearAndAddAll')">清除并增加所有</el-button>
            <el-button @click="onCancel">取消</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
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
