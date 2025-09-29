<template>
<!--  字段前缀/后缀统一  -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="标记值" prop="stringValue"
                    :rules="[{ required: true, message: '请输入标记值', trigger: 'blur' }]">
                    <el-input v-model="form.stringValue" placeholder="请输入添加值" style="width: 290px;" />
                </el-form-item>
            </el-col>
            <el-col :span="12" class="hasMsg">
                <el-form-item label="处理方式" prop="handleType"
                    :rules="[{ required: true, message: '请选择处理方式', trigger: 'blur' }]">
                    <el-radio-group v-model="form.handleType">
                        <el-radio :value="'1'">加前綴</el-radio>
                        <el-radio :value="'2'">加后綴</el-radio>
                        <el-radio :value="'3'">去除前缀</el-radio>
                        <el-radio :value="'4'">去除后缀</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
        </el-row>
    </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
const props = defineProps({
    form: Object,
    inputFields: Array,
    falg: Boolean,
});

const emit = defineEmits(["update:form"]);

const formRef = ref(null);

const form = reactive({ ...props.form });
const boundaryExamples = computed(() => {
    switch (form.handleType) {
        case '3':
            return ['示例: 如果年龄 > 150，则设置为 150。'];
        case '2':
            return ['示例: 如果收入 < 1000，则设置为 1000。'];
        case '1':
            return [
                '示例1: 如果年龄 > 150，则设置为 150。如果收入 < 1000，则设置为 1000。',
            ];
        default:
            return [];
    }
});
const loading = ref(false);
const exposedFields = [
    "stringValue",
    "handleType"
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




defineExpose({ validate });
</script>
<style scoped></style>
