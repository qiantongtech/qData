<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" content="将宽表数据拆分为多行，每行以键值对形式展示" placement="top">
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
                    <el-form-item label="key字段" prop="taskParams.keyField" :rules="[
                        { required: true, message: '请输入key字段', trigger: 'change' }]">
                        <template #label>
                            <div class="justify-center">
                                <span>key字段</span>
                                <el-tooltip effect="light" content="新增一列，用来存放每行拆分出的字段名（例如：name、age）" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-input v-model="form.taskParams.keyField" placeholder="未设置时，key 的值与字段名相同"
                            style="width: 100%" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-divider content-position="center">
                <span class="blue-text">字段</span>
            </el-divider>
            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField(2)">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>

                </el-row>
            </div>
            <el-table stripe height="470px" :data="tableFields" v-loading="loadingList">
                <el-table-column label="序号" type="index" width="80" align="left" />
                <el-table-column label="字段名" align="left" prop="columnName">
                    <template #header>
                        <div class="justify-center">
                            <span>字段名</span>
                            <el-tooltip effect="light" content="输入字段中的某个字段，用于确定转化后键值对中的值（value）" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-select v-model="scope.row.columnName" placeholder="字段名" style="width: 100%">
                            <el-option v-for="item in inputFields" :key="item.value" :label="item.label"
                                :value="item.columnName"
                                :disabled="tableFields.some(row => row !== scope.row && row.columnName === item.columnName)" />
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column label="Key" align="left" prop="columnKey">
                    <template #header>
                        <div class="justify-center">
                            <span>Key</span>
                            <el-tooltip effect="light" content="用户自定义的值，用于确定转化后键值对中的键（key），并将该键写入到 key 字段中"
                                placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-input v-model="scope.row.columnKey" placeholder="输入Key" style="width: 100%" />
                    </template>
                </el-table-column>
                <el-table-column label="输出字段" align="left" prop="columnValue">
                    <template #header>
                        <div class="justify-center">
                            <span>输出字段</span>
                            <el-tooltip effect="light" content="根据【字段名】从原表每条数据中提取对应的值，并写入指定字段" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-input v-model="scope.row.columnValue" placeholder="请输入Value" style="width: 100%" />

                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="120">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete"
                            @click="handleDelete(scope.$index + 1, 2)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">关闭</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
                <!-- <el-button type="primary" @click="handleFetchFields(2)">获取字段</el-button> -->
            </div>
        </template>
    </el-dialog>
    <FieldConflictDialog v-model="showConflictDialog" :existingFields="targetFieldsRef" :newFields="inputFields"
        @resolve="onResolveFields" />
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
import FieldConflictDialog from "../fieldDetection.vue";
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const columntype = [
    { value: 'long', label: 'long' },
    { value: 'boolean', label: 'boolean' },
    { value: 'string', label: 'string' },
    { value: 'date', label: 'date' },
    { value: 'double', label: 'double' }
];
const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: "表单标题" },
    currentNode: { type: Object, default: () => ({}) },
    info: { type: Boolean, default: false },
    graph: { type: Object, default: () => ({}) },
});



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
let groupFields = ref([]);
function handleAddField(type) {
    const targetFields = type === 2 ? tableFields.value : groupFields.value;

    if (!Array.isArray(inputFields.value) || inputFields.value.length === 0) {
        proxy.$message.warning("输入字段为空，无法添加字段");
        return;
    }

    if (!Array.isArray(targetFields)) {
        proxy.$message.warning("目标字段数据不存在");
        return;
    }
    const missingFields = new Set();

    for (const field of targetFields) {
        if (!field.columnName || !field.columnName.toString().trim()) {
            missingFields.add("字段名");
        }
        if (!field.columnKey || !field.columnKey.toString().trim()) {
            missingFields.add("Key");
        }
        if (!field.columnValue || !field.columnValue.toString().trim()) {
            missingFields.add("Value");
        }
    }

    if (missingFields.size > 0) {
        proxy.$message.warning(`请填写以下必填字段后再添加：${[...missingFields].join("，")}`);
        return;
    }

    if (type === 1) {
        const usedNames = new Set(targetFields.map(item => item.columnName));
        const nextField = inputFields.value.find(item => !usedNames.has(item.columnName));
        if (!nextField) {
            proxy.$message.warning("无可添加的字段");
            return;
        }
        targetFields.push({
            columnName: nextField.columnName,
            columnType: nextField.columnType || "",
            source: form.value.name,
            columnKey: '',
            columnValue: ''
        });
    } else if (type === 2) {
        targetFields.push({
            columnName: null,
            columnType: "",
            columnKey: '',
            columnValue: ''
        });
    }
}

function handleDelete(index, type) {
    const realIndex = Number(index) - 1;
    tableFields.value.splice(realIndex, 1);
}
const showConflictDialog = ref(false);


let type = ref(null)
let targetFieldsRef = ref(null)
function onResolveFields(payload) {
    if (!payload || !payload.action) return;

    const targetFieldsRef = type.value == 2 ? tableFields : groupFields;
    type.value = ''
    if (!targetFieldsRef || !targetFieldsRef.value) {
        proxy.$message.warning("目标字段未找到，操作失败");
        return;
    }

    switch (payload.action) {
        case "addNewOnly": {
            console.log("父组件：只增加新字段");

            const existingNames = targetFieldsRef.value.map(f => f.columnName);
            const newUniqueFields = inputFields.value.filter(
                f => !existingNames.includes(f.columnName)
            );
            targetFieldsRef.value = targetFieldsRef.value.concat(deepCopy(newUniqueFields));
            break;
        }

        case "addAll": {
            console.log("父组件：增加所有字段");
            targetFieldsRef.value = deepCopy(inputFields.value);
            break;
        }

        case "clearAndAddAll": {
            console.log("父组件：清空并增加所有字段");
            targetFieldsRef.value = deepCopy(inputFields.value);
            break;
        }

        case "cancel": {
            console.log("父组件：取消操作");
            break;
        }
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

        // 校验所有必填字段（columnName、columnKey、columnValue 必填）
        const missingFields = new Set();
        for (const field of tableFields.value) {
            if (!field.columnName || !field.columnName.toString().trim()) missingFields.add("字段名");
            if (!field.columnKey || !field.columnKey.toString().trim()) missingFields.add("Key");
            if (!field.columnValue || !field.columnValue.toString().trim()) missingFields.add("Value");
        }
        if (missingFields.size > 0) {
            proxy.$message.warning(`请填写以下必填字段后再保存：${[...missingFields].join("，")}`);
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
        const usedColumnNames = new Set(tableFields.value.map(item => item.columnName));
        const outputFields = inputFields.value.filter(item => !usedColumnNames.has(item.columnName));

        // 利用 Map 来根据 columnValue 去重，保留后面的（最新的）
        const fieldMap = new Map();
        for (const field of tableFields.value) {
            const colValue = field.columnValue.trim();
            fieldMap.set(colValue, {
                ...field,
                columnName: colValue,
                source: form.value.name,
            });
        }
        const mappedTableFields = Array.from(fieldMap.values());

        outputFields.push(...mappedTableFields);
        outputFields.push({
            source: form.value.name,
            columnName: form.value.taskParams.keyField,
        });

        taskParams.outputFields = outputFields;
        taskParams.tableFields = tableFields.value;

        const excludedNames = new Set(
            tableFields.value.flatMap(field => [field.columnName, field.columnKey]).filter(Boolean)
        );
        const groupFields = inputFields.value
            .filter(field => field.columnName && !excludedNames.has(field.columnName))
            .map(field => field.columnName);
        taskParams.groupFields = groupFields;
        console.log("🚀 ~ saveData ~ groupFields:", taskParams.groupFields)

        form.value.taskParams = taskParams;
        emit("confirm", form.value);
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
