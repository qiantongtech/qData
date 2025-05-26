<template>
    <el-form ref="formRef" :model="{ parameters }" label-width="80px">
        <div v-for="(paramIndex, index) in fieldIndices" :key="index" style="position: relative; margin-bottom: 16px">
            <!-- 参数字段 -->
            <el-form-item :label="'参数' + (index + 1)" :prop="`parameters.${paramIndex}.value`"
                :rules="[{ required: true, message: '请选择参数', trigger: 'change' }]">

                <el-select v-model="parameters[paramIndex].value" placeholder="请选择" filterable>
                    <el-option v-for="dict in tableFields" :key="dict.columnName" :label="dict.columnName"
                        :value="dict.columnName"></el-option>
                </el-select>
            </el-form-item>
            <!-- 运算符（非最后一个字段） -->
            <el-form-item v-if="index < fieldIndices.length - 1" label="计算符号"
                :prop="`parameters.${paramIndex + 1}.value`"
                :rules="[{ required: true, message: '请选择符号', trigger: 'change' }]">
                <el-radio-group v-model="parameters[paramIndex + 1].value">
                    <el-radio :value="'+'">+</el-radio>
                    <el-radio :value="'-'">-</el-radio>
                    <el-radio :value="'*'">*</el-radio>
                    <el-radio :value="'/'">/</el-radio>
                </el-radio-group>

            </el-form-item>
        </div>
        <div class="hint">例：参数1 + 参数2 =字段值</div>
        <!-- 添加 / 删除按钮 -->
        <el-button type="primary" @click="addParameter" link>
            <el-icon :size="20">
                <Plus />
            </el-icon>
        </el-button>
        <el-button @click="removeLastParameter" type="danger" circle link v-show="fieldIndices.length > 2">
            <el-icon :size="20">
                <minus />
            </el-icon>
        </el-button>
    </el-form>
</template>

<script setup>
const props = defineProps({
    tableFields: { type: Array, default: () => ([]) },
});
const parameters = defineModel('parameters', {
    type: Array,
    default: () => [],
});
const formRef = ref(null);
// 获取所有字段（field）的索引
const fieldIndices = computed(() =>
    parameters.value
        .map((item, idx) => (item.type === 'field' ? idx : -1))
        .filter((idx) => idx !== -1)
);
// 添加一个字段和运算符
const addParameter = () => {
    parameters.value.push(
        { type: 'operator', value: '+', valueType: "", },
        { type: 'field', value: props?.tableFields[0]?.columnName, valueType: "" }
    );
};
// 删除最后一组
const removeLastParameter = () => {
    if (parameters.value.length > 3) {
        parameters.value.pop(); // 删除字段
        parameters.value.pop(); // 删除操作符
    }
};
defineExpose({
    formRef,
    validate: () => formRef.value.validate(),
});


</script>

<style scoped>
.hint {
    color: red;
    font-size: 12px;
}
</style>