<template>
    <el-dialog v-model="visible" class="dialog" draggable destroy-on-close>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="queryFormRef" :model="dataForm" :rules="rules" label-width="200px" @submit.prevent
            v-loading="loading">
            <!-- <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="是否填补唯一键" prop="fillUniqueKey" style="width: 100%;">
                        <el-radio-group v-model="fillUniqueKey" @change="onFillUniqueKeyChange">
                            <el-radio :label="1">需要</el-radio>
                            <el-radio :label="0">不需要</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
            </el-row> -->
            <el-row v-for="(item, index) in columnsTwo" :key="index" :gutter="20">
                <el-col :span="24">
                    <el-form-item :label="(item.columnComment || item.en) + '：'" :prop="item.columnName"
                        style="width: 100%;">
                        <template #label>
                            <el-tooltip effect="dark" :content="item.columnComment || item.en" placement="top-start">
                                <span class="label-ellipsis">
                                    {{ item.columnComment || item.en }}：
                                </span>
                            </el-tooltip>
                        </template>
                        <el-input v-model="dataForm[item.en]" :type="item.dataLength > 200 ? 'textarea' : 'input'"
                            :placeholder="'请输入' + item.en" style="width: 100%;" />
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <el-button type="primary" @click="submitForm" :disabled="loading">确定</el-button>
            <el-button @click="visible = false">取消</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { addDaAssetOperateLog } from '@/api/da/assetchild/operate/daAssetOperateLog.js';
import moment from 'moment';

// Props
const props = defineProps({
    columns: {
        type: Array,
        default: () => [],
    },
});

// Emits
const emit = defineEmits(['ok']);



const visible = ref(false); // 控制弹窗显示
const loading = ref(false); // 提交按钮加载状态
const dataForm = ref({});   // 表单数据
const oldData = ref({});    // 原始数据备份
const fillUniqueKey = ref(1); // 是否填补唯一键：1=是，0=否
const columnsTwo = ref([]); // 渲染字段列
const uniqueKeys = ref([]); // 唯一键字段数组
const rules = ref({});      // 表单校验规则

const queryFormRef = ref(); // 表单引用

// -------------------- 监听 columns 变化 --------------------
watch(
    () => props.columns,
    (arr) => {
        if (arr && arr.length > 0) {
            // 必填字段
            const requiredFields = arr.filter(item => item.columnNullable == true);
            // 所有非唯一键字段
            columnsTwo.value = arr.filter(item => item.columnKey == false);
            // 所有唯一键字段
            uniqueKeys.value = arr.filter(item => item.columnKey != false);

            // 构造表单校验规则
            const rulesObj = {};
            requiredFields.forEach(item => {

                rulesObj[item.en] = [
                    { required: true, message: `请输入${item.en}`, trigger: 'blur' },
                    { validator: noSpecialCharacters, trigger: 'blur' },
                ];
            });
            rules.value = rulesObj;
        }
    },
    { immediate: true }
);

// -------------------- 方法定义 --------------------

// 切换是否填补唯一键
function onFillUniqueKeyChange() {
    if (fillUniqueKey.value === 1) {
        columnsTwo.value = props.columns.filter(item => item.columnKey !== 1);
    } else {
        columnsTwo.value = props.columns;
    }
}

// 特殊字符校验
function noSpecialCharacters(rule, value, callback) {
    const datePattern = /^(19|20)\d{2}[-/](0[1-9]|1[0-2])[-/](0[1-9]|[12]\d|3[01])$/;
    const isValidInput = /^[a-zA-Z0-9\s]+$/.test(value);

    if (datePattern.test(value)) {
        callback();
    } else if (!value) {
        callback(new Error('输入不能为空'));
    } else if (!isValidInput) {
        callback(new Error('不能包含特殊字符'));
    } else {
        callback();
    }
}
let query = ref({})
let title = ref()
// 打开弹窗（传入初始数据）
function addRow(row, data) {
    console.log("🚀 ~ addRow ~ data:", data)
    query.value = {
        assetId: data.id,
        datasourceId: data.datasourceId,
        tableName: data.tableName,
        tableComment: data.tableComment,
        operateType: data.id ? '2' : '1'
    }
    title.value = data.id ? '修改' : '新增'
    visible.value = true;
    dataForm.value = { ...row };
    oldData.value = { ...row };
}
// 提交表单
function submitForm() {
    queryFormRef.value.validate((valid) => {
        if (!valid) return;

        loading.value = true;

        // 唯一键字段数组拼成字符串
        const commentKeyList = uniqueKeys.value.map(item => item.en).join(',');

        // 获取被修改的字段名数组
        const getModifiedFields = (oldData, newData) => {
            return Object.keys(newData).filter(key => newData[key] !== oldData[key]);
        };
        const modifiedFields = getModifiedFields(oldData.value, dataForm.value);
        const tableCommentList = modifiedFields.join(',');

        // 组装 map-json 结构对象
        const fieldNamesObj = {
            tableCommentList,
            commentKeyList,
        };
        function close() {
            visible.value = false
        }

        // 构造请求参数
        const params = {
            ...query.value,
            operateTime: moment().format("YYYY-MM-DDTHH:mm:ss.SSSZ"),
            updateBefore: JSON.stringify(oldData.value),
            updateAfter: JSON.stringify(dataForm.value),
            fieldNames: JSON.stringify(fieldNamesObj),
        };

        addDaAssetOperateLog(params)
            .then(res => {
                if (res.code == '200') {
                    close();
                    ElMessage.success('修改成功');
                    emit('ok');

                }
            })
            .finally(() => {
                loading.value = false;
            });
    });
}

// 对外暴露方法
defineExpose({ addRow });
</script>

<style scoped lang="scss">
::v-deep .el-form-item--small .el-form-item__content {
    line-height: 32px;
    width: 75%;
}

.label-ellipsis {
    display: inline-block;
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: middle;
}
</style>
