<template>
<!-- 正则表达式替换   -->
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="正则表达式" prop="regex" :rules="[
                    {
                        required: true,
                        message: '请输入正则表达式',
                        trigger: 'blur'
                    }
                ]">
                    <el-input v-model="form.regex" placeholder="请输入正则表达式" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="替换为" prop="replacement"
                    :rules="[{ required: true, message: '请输入替换内容', trigger: 'blur' }]">
                    <el-input v-model="form.replacement" placeholder="请输入替换内容" />
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
    inputFields: Array,
    falg: Boolean,
});
const emit = defineEmits(["update:form"]);

const formRef = ref(null);
const form = reactive({ ...props.form });
// 表单校验规则
const rules = {
    regex: [
        {
            validator: (rule, value, callback) => {
                if (form.pattern && !value) {
                    callback(new Error("请输入正则表达式"));
                } else {
                    callback();
                }
            },
            trigger: "blur",
        },
    ],
    allowedChars: [
        {
            type: "array",
            required: true,
            min: 1,
            message: "请选择允许的字符类型",
            trigger: "change",
        },
    ],
    ignoreNullValue: [{ required: true, message: "请选择忽略空值", trigger: "change" }],
};
const exposedFields = ["regex", "replacement",];
function validate() {
    return new Promise((resolve, reject) => {
        formRef.value.validate((valid) => {
            if (valid) {
                const result = Object.fromEntries(
                    exposedFields.map((key) => [key, form[key]])
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
