<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" content="字段拆分组件用于将指定字段按分隔符或正则表达式拆分为多个子字段" placement="top">
                    <el-icon class="tip-icon">
                        <InfoFilled />
                    </el-icon>
                </el-tooltip>
            </div>
        </template>
        <el-form ref="dpModelRefs" :model="form" label-width="140px" @submit.prevent v-loading="loading"
            :disabled="info">

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="节点名称" prop="name" :rules="[
                        { required: true, message: '请输入节点名称', trigger: 'change' }
                    ]">
                        <el-input v-model="form.name" placeholder="请输入节点名称" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="类型" prop="taskParams.typeName">
                        <el-select v-model="form.taskParams.typeName" placeholder="请输入类型" filterable disabled>
                            <el-option v-for="dict in typeList" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item label="需要拆分的字段" prop="taskParams.splitField" :rules="[
                        { required: true, message: '请输入拆分的字段', trigger: 'change' }
                    ]">
                        <el-select v-model="form.taskParams.splitField" placeholder="请输入拆分的字段" style="width: 100%">
                            <el-option v-for="item in inputFields" :key="item.value" :label="item.label"
                                :value="item.columnName" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="拆分类型" prop="taskParams.splitType" :rules="[
                        { required: true, message: '请输入拆分类型', trigger: 'change' }
                    ]">
                        <el-radio-group v-model="form.taskParams.splitType">
                            <el-radio value="delimiter">分隔符</el-radio>
                            <el-radio value="regex">正则表达式</el-radio>
                        </el-radio-group>

                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :span="12" v-if="form.taskParams.splitType === 'delimiter'">
                    <el-form-item label="分隔符" prop="taskParams.delimiter" :rules="[
                        { required: true, message: '请输入分隔符', trigger: 'change' }
                    ]">
                        <el-input v-model="form.taskParams.delimiter" placeholder="请输入分隔符" />
                    </el-form-item>
                </el-col>
                <el-col :span="12" v-if="form.taskParams.splitType === 'regex'">
                    <el-form-item label="正则表达式" prop="taskParams.regex" :rules="[
                        { required: true, message: '请输入正则表达式', trigger: 'change' }
                    ]">
                        <el-input v-model="form.taskParams.regex" placeholder="请输入正则表达式" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="包裹字符" prop=" taskParams.enclosure">
                        <template #label>
                            <div class="justify-center">
                                <span>包裹字符</span>
                                <el-tooltip effect="light" content="指定字段值包裹符号，避免分隔符被错误拆分" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-input v-model="form.taskParams.enclosure" placeholder="请输入包裹字符" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-divider content-position="center">
                <span class="blue-text">拆分字段</span>
            </el-divider>
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                </el-row>
            </div>
            <el-table stripe height="310px" :data="tableFields" v-loading="loadingList">
                <el-table-column label="序号" type="index" width="80" align="left" />

                <el-table-column label="字段名称" align="left" prop="columnName" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.columnName" placeholder="请输入字段" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="字段类型" align="left" prop="columnType" width="150">
                    <template #default="scope">
                        <el-select v-model="scope.row.columnType" placeholder="请选择字段类型" style="width: 100%">
                            <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="字段长度" align="left" prop="length" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.length" placeholder="请输入字段长度" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="字段精度" align="left" prop="precision" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.precision" placeholder="请输入字段精度" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="格式" align="left" prop="format" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.format" placeholder="请输入格式" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="去除空格类型" prop="trimType" width="160">
                    <template #default="scope">
                        <el-select v-model="scope.row.trimType" placeholder="请选择" style="width: 100%">
                            <el-option label="去除左空格" value="left" />
                            <el-option label="去除右空格" value="right" />
                            <el-option label="去除两边空格" value="both" />
                            <el-option label="不去空格" value="noBoth" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="120">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">关闭</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script setup>
import {
    defineProps,
    defineEmits,
    ref,
    computed,
    watchEffect,
    getCurrentInstance,
} from "vue";
import { typeList } from "@/utils/graph.js";
import { getNodeUniqueKey } from "@/api/dpp/task/index.js";
import useUserStore from "@/store/system/user.js";
import {

    createNodeSelect,
    getParentNode,
} from "@/views/dpp/utils/opBase.js";
import draggable from "vuedraggable";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const columntype = [
    { value: "BigNumber", label: "BigNumber" },
    { value: "Binary", label: "Binary" },
    { value: "Boolean", label: "Boolean" },
    { value: "Date", label: "Date" },
    { value: "Integer", label: "Integer" },
    { value: "InternetAddress", label: "InternetAddress" },
    { value: "Number", label: "Number" },
    { value: "String", label: "String" },
    { value: "Timestamp", label: "Timestamp" },
];
const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: "表单标题" },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    graph: { type: Object, default: () => ({}) },
});
function handleAddField() {
    // 如果有任意一个已有字段 columnName 为空，阻止新增
    const lastEmpty = tableFields.value.find((item) => !item.columnName);
    if (lastEmpty) {
        proxy.$message.warning("请先填写当前字段名称");
        return;
    }

    // 获取所有字段名称的数组
    const names = tableFields.value.map((item) => item.columnName?.trim());
    if (new Set(names).size !== names.length) {
        proxy.$message.warning("存在重复的字段名称，请修改后再新增");
        return;
    }

    // 新增字段
    tableFields.value.push({
        columnName: null,
        columnType: 'String',
        length: null,
        precision: null,
        format: null,
        trimType: null,
        source: form.value.name,
    });
}



const showConflictDialog = ref(false);





const isOptionDisabled = (optionValue, currentRow) => {
    return tableFields.value.some(
        (row) => row !== currentRow && row.columnName === optionValue
    );
};

const emit = defineEmits(["update", "confirm"]);

const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit("update", newValue);
    },
});

let tableFields = ref([]);
let originalTableFieldsBackup = ref([]);
let inputFields = ref([]);
let loading = ref(false);
let loadingList = ref(false);
let opens = ref(false);
let row = ref();
let dpModelRefs = ref();
let form = ref({});

function handleDelete(row) {
    // 从 tableFields 中删除对应字段
    const idxTable = tableFields.value.findIndex(
        (item) => item.columnName === row.columnName
    );
    if (idxTable !== -1) {
        tableFields.value.splice(idxTable, 1);
    } else {
        proxy.$message.warning("字段未找到，删除失败");
    }
}



const off = () => {
    proxy.resetForm("dpModelRefs");
    tableFields.value = [];
    originalTableFieldsBackup.value = [];
    form.value = {};
    row.value = {};
};

const saveData = async () => {
    try {
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;

        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning("校验未通过，请至少添加一个字段");
            return;
        }

        if (tableFields.value.length > 0) {
            const hasEmptyName = tableFields.value.some(item => !item.columnName?.trim());
            if (hasEmptyName) {
                proxy.$message.warning("字段名称不能为空");
                return;
            }
        }

        // 拆分字段名称不能与 tableFields 字段名称重复
        // 校验字段名称唯一性
        const names = tableFields.value.map(item => item.columnName.trim());
        if (new Set(names).size !== names.length) {
            proxy.$message.warning("字段名称存在重复，请修改后再保存");
            return;
        }

        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode || "133545087166112",
                projectId: userStore.projectId,
            });
            loading.value = false;
            form.value.code = response.data;
        }

        const taskParams = form.value.taskParams || {};
        const inputWithoutSplit = inputFields.value.filter(item => item.columnName != form.value.taskParams.splitField);
        taskParams.outputFields = [
            ...inputWithoutSplit,
            ...tableFields.value.map(item => ({ ...item }))
        ];
        taskParams.tableFields = tableFields.value;
        form.value.taskParams = taskParams;
        emit("confirm", form.value);
        closeDialog()
    } catch (error) {
        console.error("保存数据失败:", error);
        loading.value = false;
    }
};



const closeDialog = () => {
    off();
    emit("update", false);
};

function deepCopy(data) {
    if (data === undefined || data === null) {
        return {};
    }
    try {
        return JSON.parse(JSON.stringify(data));
    } catch (e) {
        return {};
    }
}

let nodeOptions = ref([]);
watchEffect(() => {
    if (!props.visible) {
        off();
        return;
    }
    form.value = deepCopy(props.currentNode?.data || {});
    nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
    let taskParams = deepCopy(props.currentNode?.data?.taskParams || {});
    originalTableFieldsBackup.value = deepCopy(props.currentNode?.data?.taskParams?.tableFields || []);
    inputFields.value = taskParams?.inputFields || [];
    tableFields.value = taskParams?.tableFields || [];
});

</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
