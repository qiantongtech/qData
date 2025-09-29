<template>
    <!-- 字段组完整性校验 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="24">
                <el-form-item label="字段完整性" prop="fillStrategy">
                    <el-radio-group v-model="form.fillStrategy">
                        <el-radio :value="'1'">必须全部填写（部分为空为异常）</el-radio>
                        <el-radio :value="'2'">要么全部为空，要么全部填写（部分填写为异常）</el-radio>
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
                const exposedFields = ["fillStrategy",];
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
