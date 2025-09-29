<template>
    <!-- 时间字段先后顺序校验 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-form-item label="">
            <div class="field-line" style="display: flex; align-items: center; flex-wrap: wrap; gap: 8px;">
                <el-select v-if="form.conditions.length > 0" v-model="form.conditions[0].leftField" placeholder="字段"
                    style="width: 120px;" @change="onLeftFieldChange($event, 0)">
                    <el-option v-for="col in timeColumns" :key="col.columnName" :label="col.columnName"
                        :value="col.columnName" />
                </el-select>
                <div v-else style="width: 120px;"></div>
                <template v-for="(cond, index) in form.conditions" :key="index">
                    <el-select v-model="cond.operator" placeholder="符号" style="width: 50px;">
                        <el-option label="<" value="<" />
                        <el-option label="≤" value="≤" />
                    </el-select>

                    <el-select v-model="cond.rightField" placeholder="字段" style="width: 120px;">
                        <el-option v-for="col in timeColumns" :key="col.columnName" :label="col.columnName"
                            :value="col.columnName" />
                    </el-select>
                    <el-button v-if="index === form.conditions.length - 1" icon="Delete" type="danger" circle
                        @click="removeLastGroup" :disabled="form.conditions.length === 0" />
                </template>

                <!-- 添加按钮 -->
                <el-button icon="Plus" type="primary" circle @click="addGroup" />
            </div>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { reactive, ref, computed, watch, onMounted } from "vue";

const props = defineProps({
    form: Object,
    falg: Boolean,
    columnList: Array,
});
const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({
    conditions: props.form.conditions?.length
        ? JSON.parse(JSON.stringify(props.form.conditions))
        : [{ leftField: "", operator: "<", rightField: "" }],
});

const columnList = ref([]);

const timeColumns = computed(() =>
    columnList.value.filter((col) => {
        if (!col.columnType) return false;
        const type = col.columnType.toUpperCase();
        return (
            type === "DATE" ||
            type.startsWith("TIMESTAMP") ||
            type === "TIME" ||
            type === "YEAR"
        );
    })
);

watch(
    () => props.columnList,
    (newVal) => {
        columnList.value = newVal || [];
    },
    { immediate: true }
);

function addGroup() {
    form.conditions.push({ leftField: "", operator: "<", rightField: "" });
}

function removeLastGroup() {
    if (form.conditions.length > 1) {
        form.conditions.pop();
    }
}

function onLeftFieldChange(value, index) {
    form.conditions[0].leftField = value;
    for (let i = 1; i < form.conditions.length; i++) {
        form.conditions[i].leftField = form.conditions[i - 1].rightField;
    }
}

watch(
    () => form.conditions.map((c) => c.rightField),
    () => {
        for (let i = 1; i < form.conditions.length; i++) {
            form.conditions[i].leftField = form.conditions[i - 1].rightField;
        }
    }
);

function validateCalculationGroups() {
    if (form.conditions.length === 0) {
        ElMessage.warning("校验未通过，请至少添加一组计算条件");
        return false;
    }
    for (let i = 0; i < form.conditions.length; i++) {
        const group = form.conditions[i];
        if (!group.leftField) {
            ElMessage.warning(`校验未通过，请填写第 ${i + 1} 个计算组的左字段`);
            return false;
        }
        if (!group.operator || !["<", "≤"].includes(group.operator)) {
            ElMessage.warning(`校验未通过，第 ${i + 1} 个计算组的符号无效`);
            return false;
        }
        if (!group.rightField) {
            ElMessage.warning(`校验未通过，请填写第 ${i + 1} 个计算组的右字段`);
            return false;
        }
    }
    return true;
}

function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (!valid) {
                ElMessage.warning("校验未通过，请完善表单必填项");
                resolve({ valid: false });
                return;
            }
            if (!validateCalculationGroups()) {
                resolve({ valid: false });
                return;
            }
            resolve({
                valid: true,
                data: {
                    conditions: JSON.parse(JSON.stringify(form.conditions)),
                },
            });
        });
    });
}

defineExpose({ validate });
</script>

<style scoped>
.field-line {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
}
</style>
