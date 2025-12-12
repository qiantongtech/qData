<template>
    <!-- 小数位统一 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12" class="hasMsg">
                <el-form-item label="小数位数" prop="stringValue"
                    :rules="[{ required: true, message: '请输入小数位数', trigger: 'change' }]">
                    <el-input v-model="form.stringValue" placeholder="请输入小数位数" type="number" min="0" />
                    <span class="msg"><el-icon>
                            <InfoFilled />
                        </el-icon>如“2”表示保留两位小数</span>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
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

const exposedFields = ["stringValue",];

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
