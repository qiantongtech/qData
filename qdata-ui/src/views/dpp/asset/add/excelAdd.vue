<template>
    <!-- 矢量数据 -->
    <div v-if="visible">
        <el-form ref="dpModelRefs" :model="form" label-width="110px" @submit.prevent>
            <el-row :gutter="20" v-if="
                form.daAssetFiles.url != undefined &&
                form.daAssetFiles.url.indexOf('.xls') == -1 &&
                form.daAssetFiles.url.indexOf('.xlsx') == -1
            ">
                <el-col :span="12">
                    <el-form-item label="上传附件" prop="daAssetFiles.url"
                        :rules="[{ required: true, message: '请上传附件', trigger: 'change' }]">
                        <FileUploadbtn :limit="1" v-model="form.daAssetFiles.url" :dragFlag="false" :file-type="[]"
                            :fileSize="50" @handleRemove="handleRemove" @customEvent="dataFileName" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-button v-if="form.daAssetFiles.url.indexOf('.csv') != -1" type="primary" plain
                        @click="parseExcel" style="margin-left: 60px" :disabled="isButtonDisabled">
                        解析CSV
                    </el-button>
                </el-col>
            </el-row>
            <el-row :gutter="20" v-if="
                form.daAssetFiles.url == undefined ||
                form.daAssetFiles.url.indexOf('.xls') != -1 ||
                form.daAssetFiles.url.indexOf('.xlsx') != -1
            ">
                <el-col :span="12">
                    <el-form-item label="上传附件" prop="daAssetFiles.url"
                        :rules="[{ required: true, message: '请上传附件', trigger: 'change' }]">
                        <FileUploadbtn :limit="1" v-model="form.daAssetFiles.url" :dragFlag="false" :file-type="[]"
                            :fileSize="50" @handleRemove="handleRemove" @customEvent="dataFileName" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="起始行" prop="daAssetFiles.startData"
                        :rules="[{ required: true, message: '请输入起始行', trigger: 'change' }]">
                        <el-input-number :step="1" step-strictly placeholder="请输入起始行"
                            v-model="form.daAssetFiles.startData" style="width: 100%" controls-position="right" :min="1"
                            value-on-clear="min" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20" v-if="
                form.daAssetFiles.url == undefined ||
                form.daAssetFiles.url.indexOf('.xls') != -1 ||
                form.daAssetFiles.url.indexOf('.xlsx') != -1
            ">
                <el-col :span="12">
                    <el-form-item label="起始列" prop="daAssetFiles.startColumn"
                        :rules="[{ required: true, message: '请输入起始列', trigger: 'change' }]">
                        <el-input-number :step="1" step-strictly placeholder="请输入起始列"
                            v-model="form.daAssetFiles.startColumn" style="width: 100%" controls-position="right"
                            :min="1" value-on-clear="min" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-button type="primary" plain @click="parseExcel" style="margin-left: 60px"
                        :disabled="isButtonDisabled">
                        解析Excel
                    </el-button>
                </el-col>
            </el-row>
            <el-divider content-position="center" v-if="
                form.daAssetFiles.url &&
                (form.daAssetFiles.url.indexOf('.csv') != -1 ||
                    form.daAssetFiles.url.indexOf('.xls') != -1 ||
                    form.daAssetFiles.url.indexOf('.xlsx') != -1)
            ">
                <span class="blue-text">属性字段</span>
            </el-divider>
            <el-table stripe height="310px" v-loading="loadingList" :data="ColumnByAssettab" v-if="
                form.daAssetFiles.url &&
                (form.daAssetFiles.url.indexOf('.csv') != -1 ||
                    form.daAssetFiles.url.indexOf('.xls') != -1 ||
                    form.daAssetFiles.url.indexOf('.xlsx') != -1)
            ">
                <el-table-column label="序号" type="index" width="80" align="left">
                    <template #default="scope">
                        <span>{{ scope.$index + 1 }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="字段名称" align="left" prop="columnName"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        {{ scope.row.columnName || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="字段类型" align="left" prop="columnType">
                    <template #default="scope">
                        {{ scope.row.columnType || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="日期格式" align="left" prop="format">
                    <template #default="scope">
                        {{ scope.row.format || '-' }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="openDialog(scope.row)">修改</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
    </div>
</template>
<script setup>
import { getToken } from '@/utils/auth.js';
import { typeList } from '@/utils/graph.js';
import { getNodeUniqueKey, getExcelColumn, getCsvColumn } from '@/api/dpp/task/index.js';
const { proxy } = getCurrentInstance();
import useUserStore from '@/store/system/user.js';
const userStore = useUserStore();
const props = defineProps({
    title: { type: String, default: '表单标题' },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    objData: { type: Object, default: () => ({}) }
});
const emit = defineEmits(['update', 'confirm']);
const visible = ref(false);
// 变量定义
let loading = ref(false);
let loadingList = ref(false);
let TablesByDataSource = ref([]);
let ColumnByAssettab = ref();
// 修改
const open = ref(false);
let row = ref({});
const openDialog = (obj) => {
    row.value = obj;
    open.value = true;
};
// 属性字段修改新增
const handletaskConfig = (form) => {
    ColumnByAssettab.value = ColumnByAssettab.value.map((column) => {
        if (column.id == form.id) {
            return { ...column, ...form };
        }
        return column;
    });
};

let dpModelRefs = ref();
let form = ref();
const tableFields = ref([]); // 来源表格
// 计算属性：判断按钮是否禁用
const isButtonDisabled = computed(() => {
    console.log(form.value.daAssetFiles.url);
    return (
        !form.value.daAssetFiles.startData ||
        !form.value.daAssetFiles.startColumn ||
        !form.value.daAssetFiles.url
    );
});
// 获取列数据
const parseExcel = async (id) => {
    if (form.value.daAssetFiles.url.indexOf('.csv') != -1) {
        if (!form.value.daAssetFiles.url) {
            ElMessage.warning('操作失败，请添加附件');
            return;
        }

        loading.value = true; // Assuming 'loading' is a global loading state variable
        try {
            let res = await getCsvColumn({
                file: form.value.daAssetFiles.url
            });

            if (res?.data?.csvFile) {
                form.value.daAssetFiles.csvFile = res.data.csvFile;
                ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
                    id: index,
                    columnName: item,
                    columnType: 'string'
                }));
                ElMessage.success('CSV 解析成功，请确认属性字段类型！');
            } else {
                ElMessage.warning('CSV 解析失败，未获取到有效数据！');
            }
        } catch (error) {
            ElMessage.warning('解析文件时发生错误，请检查后重试');
            console.error(error);
        } finally {
            loading.value = false; // Ensure loading is turned off regardless of success or failure
        }
    } else {
        if (!form.value.daAssetFiles.startData) {
            ElMessage.warning('操作失败，请添加起始行');
            return;
        }
        if (!form.value.daAssetFiles.startColumn) {
            ElMessage.warning('操作失败，请添加起始列');
            return;
        }
        if (!form.value.daAssetFiles.url) {
            ElMessage.warning('操作失败，请添加附件');
            return;
        }
        loadingList.value = true;
        try {
            let res = await getExcelColumn({
                startData: form.value.daAssetFiles.startData,
                startColumn: form.value.daAssetFiles.startColumn,
                excelFile: form.value.daAssetFiles.url
            });

            if (res?.data?.csvFile) {
                form.value.daAssetFiles.csvFile = res.data.csvFile;
                ColumnByAssettab.value = res.data.columnList.map((item, index) => ({
                    id: index,
                    columnName: item,
                    columnType: 'string'
                }));

                ElMessage.success('Excel解析成功，请确认属性字段类型！');
            } else {
                ElMessage.warning('Excel解析失败，未获取到有效数据！');
            }
        } catch (error) {
            if (response.code == 200) ElMessage.warning('Excel解析失败，请检查文件格式或内容！');
        } finally {
            loadingList.value = false;
        }
    }
};

const off = () => {
    proxy.resetForm('dpModelRefs');
    // 清空表格字段数据
    ColumnByAssettab.value = [];
    TablesByDataSource.value = [];
    tableFields.value = [];
};
// 保存数据
const saveData = async () => {
    try {
        // 异步验证表单
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;
        if (
            form.value?.daAssetFiles.type == '1' &&
            (!ColumnByAssettab.value || ColumnByAssettab.value.length == 0)
        ) {
            return proxy.$message.warning('校验未通过，请选择属性字段');
        }
        // 如果没有 code，就调用接口获取唯一的 code
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || '133545087166112',
                projectId: userStore.projectId
            });
            loading.value = false; // 结束加载状态
            form.value.code = response.data; // 设置唯一的 code
        }
        const daAssetFiles = form.value?.daAssetFiles;
        daAssetFiles.tableFields = ColumnByAssettab.value;
        daAssetFiles.columnsList = ColumnByAssettab.value.map(({ columnName, columnType }) => ({
            colName: columnName,
            dataType: columnType
        }));
        daAssetFiles.columns = daAssetFiles.tableFields.map((item) => {
            return {
                index: item.id,
                columnName: item.columnName,
                type: item.columnType,
                format: item.format
            };
        });
        emit('confirm', form.value);
        emit('update', false);
    } finally {
        loadingList.value = false;
    }
};

function dataFileName(file) {
    form.value.daAssetFiles.name = file.originalFilename;
    form.value.daAssetFiles.type = file.ext;
    console.log(form.value, '451561649861');
}
const closeDialog = () => {
    off();
    // 关闭对话框
    emit('update', false);
};

// 监听属性变化
function deepCopy(data) {
    if (data === undefined || data === null) {
        return {}; // 或者返回一个默认值
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {}; // 或者返回一个默认值
    }
}
const show = (data) => {
    visible.value = false;
    console.log(data, '文件类型。。。。。。。。。');
    if (data.createType == '2' && data.type == '6') {
        visible.value = true;
        form.value = deepCopy(data);
        ColumnByAssettab.value = data.daAssetFiles.tableFields;
    }
    if (data.id != undefined && data.type == '6') {
        visible.value = true;
        if (data.daAssetFiles == undefined) {
            data.daAssetFiles = {
                url: null,
                startData: '',
                tableFields: [],
                startColumn: ''
            };
        }
        form.value = deepCopy(data);
        ColumnByAssettab.value = data.daAssetFiles.tableFields;
    }
};
// 监听属性变化
// watchEffect(() => {
//   console.log(userStore)
//   if (props.visible) {
//     // 数据源
//     console.log(props.objData,'==========')
//     form.value = deepCopy(props.data);
//     ColumnByAssettab.value = props.data.daAssetFiles.tableFields;
//   } else {
//     off();
//   }
// });
// 文件删除
function handleRemove() {
    ColumnByAssettab.value = [];
    form.value.daAssetFiles.url = undefined;
}
defineExpose({ show, form });
</script>
<style scoped lang="scss">
.blue-text {
    color: #2666fb;
}
</style>
