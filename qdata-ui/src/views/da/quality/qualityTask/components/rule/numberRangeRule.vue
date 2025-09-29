<template>
    <!-- 数值字段范围校验 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="最小值" prop="minValue">
                    <el-input v-model="form.minValue" placeholder="不填写表示不限制最小值" type="number" style="width: 290px;" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="最大值" prop="maxValue">
                    <el-input v-model="form.maxValue" placeholder="不填写表示不限制最大值" type="number" style="width: 290px;" />
                </el-form-item>
            </el-col>
        </el-row>

        <el-row>
            <el-col :span="8">
                <el-form-item label="是否包含边界值" prop="includeBoundary">
                    <el-radio-group v-model="form.includeBoundary">
                        <el-radio :value="'1'">包含</el-radio>
                        <el-radio :value="'0'">不包含</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>

    </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
import { getColumnByAssetId } from "@/api/dpp/task/index.js";

const props = defineProps({
    form: Object,
    dppQualityTaskObjSaveReqVO: Array,
    falg: Boolean,
});

const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({ ...props.form });
const columnList = ref([]);
// 评测对象改变时，更新相关字段和字段列表
async function handleTargetObjectChange(selectedName, falg) {
    const selected = props.auditTargets.find((item) => item.datasourceName == selectedName);
    if (selected) {
        form.datasourceId = selected.datasourceId;
        form.assetid = selected.assetid || "";
        form.checkField = "";
        await fetchColumns();
    } else {
        form.datasourceId = null;
        form.assetid = "";
        form.checkField = "";
        columnList.value = [];
    }
}

const loading = ref(false);

async function fetchColumns() {
    if (!form.datasourceId || !form.assetid) {
        columnList.value = [];
        return;
    }
    loading.value = true;
    try {
        const res = await getColumnByAssetId({
            id: form.datasourceId,
            tableName: form.assetid,
        });
        if (res.code == "200") {
            columnList.value = res.data;
        } else {
            columnList.value = [];
        }
    } catch (error) {
        columnList.value = [];

    } finally {
        loading.value = false;
    }
}

const exposedFields = [
    "targetObject",
    "datasourceId",
    "assetid",
    "checkField",
    "minValue",
    "maxValue",
    "includeBoundary"
];

function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (valid) {
                const data = Object.fromEntries(exposedFields.map(key => [key, form[key]]));
                resolve({
                    valid: true,
                    data,
                });
            } else {
                resolve({ valid: false });
            }
        });
    });
}


onMounted(() => {
    if (form.targetObject) {
        fetchColumns();
    }
    console.log('子组件 mounted hook');
});

defineExpose({ validate });
</script>
<style scoped></style>
