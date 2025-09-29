<template>
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="24">
                <el-form-item label="去除空格规则" prop="handleType">
                    <el-radio-group v-model="form.handleType">
                        <el-radio :value="'1'">去除前后空格</el-radio>
                        <el-radio :value="'2'">去除所有空格</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
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
function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (valid) {
                const exposedFields = ["handleType"];
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
