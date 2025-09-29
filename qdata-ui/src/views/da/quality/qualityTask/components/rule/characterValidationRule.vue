<template>
    <!-- 字符串类型校验 -->
    <el-form ref="formRef" :model="form" :rules="rules" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <!-- 评测对象下拉 -->
                <el-form-item label="使用正则" prop="useRegexFlag">
                    <el-checkbox v-model="form.useRegexFlag" :true-value="1" :false-value="0">使用正则</el-checkbox>

                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="!form.useRegexFlag" class="hasMsg">
                <el-form-item label="允许字符类型" prop="allowedChars">
                    <el-checkbox-group v-model="form.allowedChars" name="chars">
                        <el-checkbox :value="'1'">数字</el-checkbox>
                        <el-checkbox :value="'2'">字母</el-checkbox>
                        <el-checkbox :value="'3'">空格</el-checkbox>
                        <el-checkbox :value="'4'">特殊符号</el-checkbox>
                    </el-checkbox-group>
                    <span class="msg"><el-icon>
                            <InfoFilled />
                        </el-icon>若选中数字和字母，系统自动识别为“仅允许字母与数字的组合</span>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="!form.useRegexFlag">
                <el-form-item label="忽略空值" prop="ignoreNullValue">
                    <el-radio-group v-model="form.ignoreNullValue">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.useRegexFlag">
                <el-form-item label="正则表达式" prop="regex" :rules="[
                    {
                        required: form.useRegexFlag,
                        message: '请输入正则表达式',
                        trigger: 'blur',
                        validator: (rule, value, callback) => {
                            if (form.useRegexFlag && !value) {
                                callback(new Error('请输入正则表达式'));
                            } else {
                                callback();
                            }
                        },
                    },
                ]">
                    <el-input v-model="form.regex" placeholder="请输入正则表达式" />
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
watch(
    () => form.useRegexFlag,
    (val) => {
        if (val) {
            form.allowedChars = ["1"];
            form.ignoreNullValue = "1";
        }
    }
);
// 表单校验规则
const rules = {
    regex: [
        {
            validator: (rule, value, callback) => {
                if (form.useRegexFlag && !value) {
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


const exposedFields = ["useRegexFlag", "allowedChars", "ignoreNullValue", "regex"];

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
