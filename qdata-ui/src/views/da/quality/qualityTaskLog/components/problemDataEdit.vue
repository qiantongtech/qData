<template>
    <el-dialog v-model="visible" title="修改问题数据" class="medium-dialog" @close="handleClose" destroy-on-close>
        <el-form :model="formData" label-width="240px">
            <el-form-item v-for="item in parsedFields" :key="item.name" :label="item.name">
                <el-input v-model="formData[item.name]" :disabled="!item.editable" />
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button @click="handleClose">关闭</el-button>
            <el-button type="primary" @click="handleok">保存</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, defineExpose, defineEmits } from 'vue'

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

        const label = field.name   // 中文展示名
        const newValue = formData.value[label]
        const originKey = Object.keys(originalJson).find(
            k => k.toLowerCase() === field.label.toLowerCase()
        )
        const oldValue = originalJson[originKey]

        // ⚠️ 用 field.label 作为 key
        if (newValue != oldValue) {
            keyWordData[field.label] = newValue
        }
    }

    if (Object.keys(keyWordData).length === 0) {
        ElMessage.warning('未修改任何字段，无法保存')
        return
    }

    // 最终 formData 转 label-key 的对象
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