<template>
    <!-- 字段长度范围校验 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="false">

        <el-row>
            <el-col :span="12">
                <el-form-item label="最小长度" prop="minLength">
                    <el-input v-if="!falg" v-model="form.minLength" placeholder="不填写表示不限制最小长度" type="number" min="0"
                        class="rule-half" />
                    <div v-else class="form-readonly">{{ form.minLength ?? '-' }}</div>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="最大长度" prop="maxLength">
                    <el-input v-if="!falg" v-model="form.maxLength" placeholder="不填写表示不限制最大长度" type="number" min="0"
                        class="rule-half" />
                    <div v-else class="form-readonly">{{ form.maxLength ?? '-' }}</div>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <el-form-item label="忽略空值" prop="ignoreNullValue">
                    <el-radio-group v-if="!falg" v-model="form.ignoreNullValue">
                        <el-radio :value="'1'">是</el-radio>
                        <el-radio :value="'0'">否</el-radio>
                    </el-radio-group>
                    <div v-else class="form-readonly">{{ form.ignoreNullValue === '1' ? '是' : form.ignoreNullValue === '0' ? '否' : '-' }}</div>
                </el-form-item>
            </el-col>
        </el-row>

    </el-form>
</template>

<script setup>
import { reactive, ref, watch } from "vue";
const props = defineProps({
    form: Object,
    dppQualityTaskObjSaveReqVO: Array,
    falg: Boolean,
});

const emit = defineEmits(["update:form"]);
const formRef = ref(null);
const form = reactive({ ...props.form });
function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (valid) {
                const data = Object.fromEntries(
                    ['minLength', 'maxLength', 'ignoreNullValue'].map(key => [key, form[key]])
                );
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
<style scoped>
.rule-half { width: 100%; }
.form-readonly { min-height: 32px; line-height: 32px; }
</style>
