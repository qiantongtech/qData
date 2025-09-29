<template>
<!--  数值边界调整  -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="最小值" prop="min" :rules="[{ required: true, message: '请输入最小值', trigger: 'blur' }]">
                    <el-input v-model="form.min" placeholder="不填写表示不限制最小值" type="number" style="width: 290px;" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="最大值" prop="max" :rules="[{ required: true, message: '请输入最大值', trigger: 'blur' }]">
                    <el-input v-model="form.max" placeholder="不填写表示不限制最大值" type="number" style="width: 290px;" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" class="hasMsg">
                <el-form-item label="处理方式" prop="handleType"
                    :rules="[{ required: true, message: '请选择处理方式', trigger: 'blur' }]">
                    <el-radio-group v-model="form.handleType">
                        <el-radio :value="'3'">超出最大值时调整为最大值</el-radio>
                        <el-radio :value="'2'">超出最小值时调整为最小值</el-radio>
                        <el-radio :value="'1'">两种情况都调整到对应的边界值</el-radio>
                    </el-radio-group>
                    <div class="msg">
                        <div v-for="(msg, index) in boundaryExamples" :key="index">
                            <el-icon>
                                <InfoFilled />
                            </el-icon>
                            <span>{{ msg }}</span>
                        </div>
                    </div>
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
    "min",
    "max",
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
