<template>
    <el-dialog v-model="localVisible" :title="title" draggable class="warn-dialog" destroy-on-close>
        <el-form ref="dpModelRefs" :model="form" :rules="rules" label-width="100px" @submit.prevent>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="关联标准" prop="dataElemId">
                        <el-select v-model="form.dataElemId" placeholder="请选择关联标准" @change="handleDatasourceChange"
                            filterable clearable>
                            <el-option v-for="dict in DpData" :key="dict.id" :label="dict.name"
                                :value="dict.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="中文名称" prop="cnName">
                        <el-input v-model="form.cnName" placeholder="请输入中文名称" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="英文名称" prop="engName">
                        <el-input v-model="form.engName" placeholder="请输入英文名称"
                            @input="convertToUpperCase('engName', form.engName)" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="数据类型" prop="columnType">
                        <el-select v-model="form.columnType" placeholder="请选择数据类型">
                            <el-option v-for="dict in column_type" :key="dict.value" :label="dict.label"
                                :value="dict.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="属性长度" prop="columnLength"
                        :rules="form.columnType === 'DATE' ? [] : [{ required: true, message: '请输入属性长度', trigger: 'change' }]">
                        <el-input-number :step="1" step-strictly v-model="form.columnLength" style="width: 100%"
                            controls-position="right" :min="1" :max="9999999999" placeholder="请输入属性长度" />

                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <!-- DECIMAL  NUMBER  NUMERIC -->
                    <el-form-item label="小数位数" prop="columnScale">
                        <el-input-number :step="1" :disabled="form.columnType !== 'DECIMAL' &&
                            form.columnType !== 'NUMBER' &&
                            form.columnType !== 'NUMERIC' &&
                            form.columnType !== 'FLOAT' &&
                            form.columnType !== 'DOUBLE'
                            " step-strictly v-model="form.columnScale" style="width: 100%" controls-position="right"
                            :min="0" :max="9999999999" placeholder="请输入小数长度" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="modelComment">
                        <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="默认值" prop="defaultValue">
                        <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否主键" prop="pkFlag">
                        <el-radio-group v-model="form.pkFlag" @change="handlePkFlagChange">
                            <el-radio v-for="dict in dp_model_column_pk_flag" :key="dict.value" :value="dict.value">{{
                                dict.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="是否必填" prop="nullableFlag">
                        <el-radio-group v-model="form.nullableFlag" :disabled="form.pkFlag == 1">
                            <el-radio v-for="dict in dp_model_column_nullable_flag" :key="dict.value"
                                :value="dict.value">{{ dict.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row>

        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="closeDialog">取消</el-button>
                <el-button type="primary" @click="confirmDialog"> 确认 </el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue';

import { getDpDataElemList } from '@/api/dp/dataElem/dataElem';

const { proxy } = getCurrentInstance();
const { column_type, dp_model_column_pk_flag, dp_model_column_nullable_flag } = proxy.useDict(
    'column_type',
    'dp_model_column_pk_flag',
    'dp_model_column_nullable_flag'
);

// 接收父组件传递的属性
const props = defineProps({
    visible: { type: Boolean, default: true },
    deptOptions: { type: Array, default: () => [] },
    column_type: { type: Array, default: () => [] },
    userList: { type: Array, default: () => [] },
    deptList: { type: Array, default: () => [] },
    row: { type: Object, default: () => ({}) },
    data: { type: Object, default: () => { } }
});
let title = ref();
watch(
    () => props.visible,
    (newVal) => {
        console.log('Object.keys(props.row).length === 0', props.row.index);
        if (newVal) {
            getDpDataElem();
            if (props.row && props.row.index !== undefined) {
                // 编辑状态
                title.value = '编辑表字段';
                Object.assign(form.value, props.row);
                form.value.authorityDept = Number(form.value.authorityDept);
            } else {
                // 新增状态
                title.value = '新增表字段';
                // 重置表单
                form.value = {
                    id: '',
                    dataElemId: '',
                    cnName: '', // 使用可选链操作符
                    engName: '',
                    columnType: '',
                    columnLength: '',
                    pkFlag: '0', // 设置默认值
                    authorityDept: null,
                    modelComment: '',
                    nullableFlag: '0', // 设置默认值
                    defaultValue: '',
                    columnScale: '',
                    modelId: props.data?.id // 保存模型ID
                };
            }
        }
    },
    { immediate: true } // 新增immediate属性，确保组件挂载时就执行一次
);
let DpData = ref([]);
const handlePkFlagChange = (value) => {
    if (value == 1) {
        form.value.nullableFlag = '1';
    }
}
const getDpDataElem = async () => {
    try {
        const response = await getDpDataElemList();
        DpData.value = response.data;
        console.log('DpData', DpData.value);
    } catch (error) {
        console.error('请求失败:', error);
    }
};
const handleDatasourceChange = (value) => {
    const selectedDatasource = DpData.value.find((item) => item.id === value);
    if (selectedDatasource) {
        form.value.dataElemName = selectedDatasource.name;
        form.value.cnName = selectedDatasource.name;
        form.value.engName = selectedDatasource.engName;
        form.value.columnType = selectedDatasource.columnType;
    }
};
// 定义向父组件发送事件的接口
const emit = defineEmits(['update:dialogFormVisible', 'confirm']);

// 处理弹窗显示状态
const localVisible = computed({
    get() {
        return props.visible;
    },
    set(value) {
        emit('update:dialogFormVisible', value);
    }
});

// 表单数据和验证规则
const form = ref({
    id: '',
    dataElemId: '',
    cnName: '',
    engName: '',
    columnType: '',
    columnLength: '1',
    pkFlag: '',
    authorityDept: null,
    modelComment: '',
    nullableFlag: '',
    defaultValue: '',
    columnScale: ''
});

const rules = ref({
    // 示例规则（可以根据需求添加或修改）
    cnName: [{ required: true, message: '中文名称不能为空', trigger: 'blur' }],
    engName: [
        { required: true, message: '英文名称不能为空', trigger: 'blur' },
        {
            pattern: /^[A-Za-z][A-Za-z0-9_]*$/,
            message: '表名只能包含字母、数字和下划线，且必须以字母开头',
            trigger: 'blur'
        }
    ],
    columnType: [{ required: true, message: '数据类型不能为空', trigger: 'blur' }],

    // pkFlag: [{ required: true, message: '请选择是否主键', trigger: 'blur' }],
    // nullableFlag: [{ required: true, message: '请选择是是否必填', trigger: 'blur' }],
    defaultValue: [
        {
            validator: (rule, value, callback) => {
                if (!value) {
                    callback();
                    return;
                }

                // 去除首尾的单引号后再计算长度
                let actualValue = value;
                if (value.startsWith("'") && value.endsWith("'")) {
                    actualValue = value.slice(1, -1);
                }

                // 对于数值类型，检查数值长度
                if (
                    ['NUMBER', 'DECIMAL', 'NUMERIC', 'FLOAT', 'DOUBLE'].includes(
                        form.value.columnType
                    )
                ) {
                    const numStr = actualValue.toString().replace('.', ''); // 移除小数点再计算长度
                    if (numStr.length > form.value.columnLength) {
                        callback(
                            new Error(`默认值长度不能超过属性长度${form.value.columnLength}`)
                        );
                        return;
                    }
                }
                // 对于字符类型，直接检查字符串长度
                else if (actualValue.length > form.value.columnLength) {
                    callback(new Error(`默认值长度不能超过属性长度${form.value.columnLength}`));
                    return;
                }
                callback();
            },
            trigger: ['blur', 'change']
        }
    ]
});

// 添加对属性长度改变的监听
watch(
    () => form.value.columnLength,
    (newVal) => {
        // 当属性长度改变时，触发默认值的校验
        if (form.value.defaultValue) {
            proxy.$refs['dpModelRefs']?.validateField('defaultValue');
        }
    }
);

// 关闭对话框
const closeDialog = () => {
    proxy.resetForm('dpModelRefs');
    localVisible.value = false;
    form.value = {
        id: '',
        dataElemId: '',
        cnName: '',
        engName: '',
        columnType: '',
        columnLength: '1',
        pkFlag: '',
        authorityDept: null,
        modelComment: '',
        nullableFlag: '',
        defaultValue: '',
        columnScale: ''
    };
};
// 转换输入值为大写
const convertToUpperCase = (key, value) => {
    const uppercasedValue = value.replace(/[a-z]/g, (char) => char.toUpperCase());

    form.value[key] = uppercasedValue;

    console.log('🚀 ~ convertToUpperCase ~ form.value[key]:', form.value[key]);
};

// 确认操作
const confirmDialog = () => {
    proxy.$refs['dpModelRefs'].validate((valid) => {
        if (valid) {
            emit('confirm', form.value);
            closeDialog();
        } else {
            console.log('表单验证失败');
        }
    });
};
</script>

<style scoped lang="less">
.warn-dialog .el-dialog__body {
    max-height: 500px;
    overflow-y: auto;
}

.dialog-footer {
    text-align: right;
}

.dialog-footer .el-button {
    margin-left: 10px;
}
</style>
