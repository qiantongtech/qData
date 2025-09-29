<template>
    <!-- 数值精度校验 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="小数位数" prop="scale">
                    <el-input v-model="form.scale" placeholder="请输入小数位数" type="number" min="0" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="忽略整数值" prop="skipInteger">
                    <el-radio-group v-model="form.skipInteger">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="8">
                <el-form-item label="忽略空值" prop="ignoreNullValue">
                    <el-radio-group v-model="form.ignoreNullValue">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
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

const exposedFields = ["scale", "skipInteger", "ignoreNullValue"];

function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (valid) {
                const result = Object.fromEntries(
                    exposedFields.map(key => [key, form[key]])
                );
                resolve({ valid: true, data: result });
            } else {
                resolve({ valid: false });
            }
        });
    });
}


defineExpose({ validate });
</script>
<style scoped></style>
