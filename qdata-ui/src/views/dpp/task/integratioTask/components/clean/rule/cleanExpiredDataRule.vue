<template>
    <!-- 清理过期数据 -->
    <el-form ref="formRef" :model="form" label-width="130px" :disabled="falg">
        <el-row>
            <el-col :span="12">
                <el-form-item label="时间范围">
                    <el-radio-group v-model="form.dataRange" @change="handleDataRangeChange">
                        <el-radio label="0">最近时间范围</el-radio>
                        <el-radio label="1">具体日期</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item v-if="form.dataRange == '0'" label="维持时间范围"
                    :rules="[{ required: true, message: '请输入维持时间范围', trigger: 'change' }]" prop="dataRangeValue">
                    <el-input-number v-model="form.dataRangeValue" :min="1" />
                    <el-select v-model="form.dataRangeType" style="margin-left: 10px; width: 100px;">
                        <el-option label="天前" value="1" />
                        <el-option label="月前" value="2" />
                        <el-option label="年前" value="3" />
                    </el-select>
                </el-form-item>

                <el-form-item v-if="form.dataRange == '1'" label="具体日期"
                    :rules="[{ required: true, message: '请选择具体日期', trigger: 'change' }]" prop="dataRangeValue">
                    <el-date-picker v-model="form.dataRangeValue" type="date" format="YYYY/MM/DD" style="width: 200px;"
                        value-format="YYYY-MM-DD" placeholder="选择日期" />
                    <el-select v-model="form.dataRangeType" style="margin-left: 10px; width: 100px;">
                        <el-option label="之前" value="1" />
                        <el-option label="之后" value="2" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <el-form-item label="处理方式">
                    <el-radio-group v-model="form.handleType">
                        <el-radio label="0">标记为过期</el-radio>
                        <el-radio label="1">删除记录</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="标记字段" v-if="form.handleType == '0'"
                    :rules="[{ required: true, message: '请选择标记字段', trigger: 'change' }]">
                    <el-select v-model="form.handleColumns" placeholder="请选择清洗字段" clearable style="width: 200px;">
                        <el-option v-for="dict in inputFields" :key="dict.columnName" :label="dict.label"
                            :value="dict.columnName" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <el-form-item label="标记值" v-if="form.handleType == '0'"
                    :rules="[{ required: true, message: '请输入标记值', trigger: 'change' }]">
                    <el-input v-model="form.handleValue" placeholder="请输入标记值" style="width: 200px;" />
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>

<script setup>
import { reactive, ref } from "vue";
import moment from "moment";

const props = defineProps({
    form: Object,
    inputFields: Array,
    falg: Boolean,
});

const emit = defineEmits(["update:form"]);
const formRef = ref(null);

const dataRange = props.form?.dataRange ?? "0";

const form = reactive({ ...props.form });

// 用change事件监听dataRange变化，切换时如果dataRangeValue为空则赋默认值
function handleDataRangeChange(newVal) {
    if (newVal === "0") {
        form.dataRangeValue = 30;
    } else if (newVal === "1") {
        form.dataRangeValue = moment().format("YYYY-MM-DD");
    }
}

// 验证并返回清洗后的有效数据
function validate() {
    return new Promise((resolve) => {
        formRef.value.validate((valid) => {
            if (!valid) return resolve({ valid: false });

            // 只保存当前页面实际有的字段
            const cleanedData = {
                dataRange: form.dataRange,
                dataRangeValue: form.dataRangeValue,
                handleType: form.handleType,
                dataRangeType: form.dataRangeType,
            };

            if (form.dataRange === "0") {
                // 最近时间范围，dataRangeValue 是数字
                cleanedData.dataRangeValue = form.dataRangeValue;
            } else if (form.dataRange === "1") {
                // 具体日期，dataRangeValue 是日期字符串
                cleanedData.dataRangeValue = form.dataRangeValue;
            }

            if (form.handleType === "0") {
                cleanedData.handleColumns = form.handleColumns;
                cleanedData.handleValue = form.handleValue;
            }

            resolve({
                valid: true,
                data: cleanedData,
            });
        });
    });
}

defineExpose({ validate });
</script>
