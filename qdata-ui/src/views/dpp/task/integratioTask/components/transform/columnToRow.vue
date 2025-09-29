<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" content="将多条键值对数据转化为宽表，每个键（key）对应一列" placement="top">
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
                    <el-form-item label="节点名称" prop="name"
                        :rules="[{ required: true, message: '请输入节点名称', trigger: 'change' }]">
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
                    <el-form-item label="key字段" prop="taskParams.keyField"
                        :rules="[{ required: true, message: '请输入key字段', trigger: 'change' }]">
                        <template #label>
                            <div class="justify-center">
                                <span>key字段</span>
                                <el-tooltip effect="light" content="解析键值对数据中的键所在列，基于此字段生成宽表" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-select v-model="form.taskParams.keyField" placeholder="请输入key字段" style="width: 100%">
                            <el-option v-for="item in inputFields" :key="item.columnName" :label="item.label"
                                :value="item.columnName" />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-divider content-position="center">
                <span class="blue-text">分组字段</span>
            </el-divider>

            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField(1)">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="warning" plain @click="handleFetchFields(1)" v-if="!info">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>获取字段
                        </el-button>
                    </el-col>
                </el-row>
            </div>

            <el-table stripe height="310px" :data="groupTableField" v-loading="loadingList">
                <el-table-column label="序号" type="index" width="80" align="left" />
                <el-table-column label="分组字段" align="left" prop="columnName">
                    <template #header>
                        <div class="justify-center">
                            <span>分组字段</span>
                            <el-tooltip effect="light" content="配置无需转化的字段组，用于数据分组并确保数据的唯一性（如id + name）" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-select v-model="scope.row.columnName" placeholder="请选择分组字段" style="width: 100%">
                            <el-option v-for="item in inputFields" :key="item.columnName" :label="item.label"
                                :value="item.columnName" :disabled="isOptionDisabled(item.columnName, scope.row)" />
                        </el-select>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="120">
                    <template #default="scope">
                        <el-button link type="danger" icon="Delete"
                            @click="handleDelete(scope.$index + 1, 1)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-divider content-position="center">
                <span class="blue-text">新增字段</span>
            </el-divider>

            <div class="justify-between mb15">
                <el-row :gutter="15" class="btn-style">
                    <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleAddField(2)">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>新增
                        </el-button>
                    </el-col>
                    <!-- <el-col :span="1.5">
                        <el-button type="primary" plain @click="handleFetchFields(2)">
                            <i class="iconfont-mini icon-xinzeng mr5"></i>获取字段
                        </el-button>
                    </el-col> -->
                </el-row>
            </div>

            <el-table stripe height="310px" :data="tableFields" v-loading="loadingList">
                <el-table-column label="序号" type="index" width="80" align="left" />
                <el-table-column label="新增字段" align="left" prop="targetColumnName" width="150">
                    <template #header>
                        <div class="justify-center">
                            <span>新增字段</span>
                            <el-tooltip effect="light" content="指定原表中的列，用于填充【目标字段】（结合关键字字段使用）" placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-input v-model="scope.row.targetColumnName" placeholder="请输入字段" style="width: 100%" />
                    </template>
                </el-table-column>
                <el-table-column label="数据字段" align="left" prop="dataColumn" width="150">
                    <template #header>
                        <div class="justify-center">
                            <span>数据字段</span>
                            <el-tooltip effect="light" content="指定原表中的列，用于填充【目标字段】中的值（需结合关键字字段来确保数据正确映射）"
                                placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-select v-model="scope.row.dataColumn" placeholder="请选择数据字段" style="width: 100%">
                            <el-option v-for="item in filteredInputFieldsForDataColumn" :key="item.columnName"
                                :label="item.label" :value="item.columnName" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="关键字" align="left" prop="keyColumnValue" width="130">
                    <template #header>
                        <div class="justify-center">
                            <span>关键字</span>
                            <el-tooltip effect="light" content="可填写【key字段】列中的全部数据。结合【key字段】+ 分组 + 【目标字段】，锁定具体值并写入目标字段"
                                placement="top">
                                <el-icon class="tip-icon">
                                    <InfoFilled />
                                </el-icon>
                            </el-tooltip>
                        </div>
                    </template>
                    <template #default="scope">
                        <el-input v-model="scope.row.keyColumnValue" placeholder="请输入关键字" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="字段类型" align="left" prop="columnType" width="130">
                    <template #default="scope">
                        <el-select v-model="scope.row.columnType" placeholder="请选择字段类型" style="width: 100%">
                            <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="格式" align="left" prop="format" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.format" placeholder="请输入格式" style="width: 100%" />
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

                <el-table-column label="null If" align="left" prop="nullIf" width="120">
                    <template #default="scope">
                        <el-input v-model="scope.row.nullIf" placeholder="请输入nullIf" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="聚合方式" align="left" prop="aggregation" width="150">
                    <template #default="scope">
                        <el-select v-model="scope.row.aggregation" placeholder="请选择聚合方式" style="width: 100%" clearable
                            @clear="() => scope.row.aggregation = ''">
                            <el-option label="Sum" value="Sum" />
                            <el-option label="Average" value="Average" />
                            <el-option label="Minimum" value="Minimum" />
                            <el-option label="Maximum" value="Maximum" />
                        </el-select>
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
import { createNodeSelect } from "@/views/dpp/utils/opBase.js";
import FieldConflictDialog from "../fieldDetection.vue";

const { proxy } = getCurrentInstance();
const userStore = useUserStore();

const columntype = [
    { value: "long", label: "long" },
    { value: "boolean", label: "boolean" },
    { value: "string", label: "string" },
    { value: "date", label: "date" },
    { value: "double", label: "double" },
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
    set(newVal) {
        emit("update", newVal);
    },
});

let form = ref({});
let inputFields = ref([]);
let groupTableField = ref([]);
let tableFields = ref([]);

let loading = ref(false);
let loadingList = ref(false);
let dpModelRefs = ref();

const showConflictDialog = ref(false);
const type = ref(null);
const targetFieldsRef = ref(null);

const isOptionDisabled = (optionValue) => {
    // 已添加的字段名
    const tableUsedNames = groupTableField.value.map((item) => item.columnName);
    return tableUsedNames.some((row) => row === optionValue);
};

// 计算排除关键字段 + 分组字段的集合
const excludedFields = computed(() => {
    const excludeSet = new Set();
    if (form.value.taskParams?.keyField) excludeSet.add(form.value.taskParams.keyField);
    groupTableField.value.forEach((item) => {
        if (item.columnName) excludeSet.add(item.columnName);
    });
    return excludeSet;
});

// 过滤后的字段，供目标字段的dataColumn使用
const filteredInputFieldsForDataColumn = computed(() => {
    return inputFields.value.filter((item) => !excludedFields.value.has(item.columnName));
});
function validateTargetFields() {
    const errors = [];
    const groupFieldNames = groupTableField.value.map(f => f.columnName);
    const keyField = form.value.taskParams?.keyField;

    const targetKeyPairs = new Set();

    tableFields.value.forEach((row, index) => {
        const rowIndex = index + 1;

        // 必填项校验
        if (!row.targetColumnName) {
            errors.push(`第 ${rowIndex} 行：目标字段不能为空`);
        }
        if (!row.dataColumn) {
            errors.push(`第 ${rowIndex} 行：数据字段不能为空`);
        }
        if (!row.keyColumnValue) {
            errors.push(`第 ${rowIndex} 行：关键字不能为空`);
        }

        // 数据字段不能是关键字段
        if (row.dataColumn === keyField) {
            errors.push(`第 ${rowIndex} 行：数据字段不能与关键字段相同`);
        }

        // 数据字段不能是分组字段
        if (groupFieldNames.includes(row.dataColumn)) {
            errors.push(`第 ${rowIndex} 行：数据字段不能与分组字段重复`);
        }

        // 目标字段和关键字组合唯一校验
        if (row.targetColumnName && row.keyColumnValue) {
            const pairKey = `${row.targetColumnName}__${row.keyColumnValue}`;
            if (targetKeyPairs.has(pairKey)) {
                errors.push(`第 ${rowIndex} 行：目标字段和关键字的组合 "${row.targetColumnName} - ${row.keyColumnValue}" 已重复`);
            } else {
                targetKeyPairs.add(pairKey);
            }
        }
    });

    if (errors.length > 0) {
        proxy.$message.warning(errors.join('\n'));
        return false;
    }
    return true;
}


// 新增字段
function handleAddField(typeVal) {
    const targetFields = typeVal == 2 ? tableFields.value : groupTableField.value;

    if (!inputFields.value || inputFields.value.length === 0) {
        proxy.$message.warning("操作失败，请添加输入字段");
        return;
    }
    if (!targetFields) {
        proxy.$message.warning("操作失败，目标字段数据不存在");
        return;
    }

    if (typeVal === 1) {
        // 分组字段依然按已有逻辑，防止重复
        const usedNames = targetFields.map((item) => item.columnName);
        const nextField = inputFields.value.find((item) => !usedNames.includes(item.columnName));
        if (!nextField) {
            proxy.$message.warning("新增失败，已无可添加的字段");
            return;
        }
        targetFields.push({
            columnName: nextField.columnName,
            columnType: nextField.columnType || "string",
        });
    }

    if (typeVal === 2) {
        // 目标字段允许重复dataColumn，直接新增空白行，不用找过滤字段
        if (!validateTargetFields()) return;
        targetFields.push({
            columnName: "",        // 空白，用户填写
            columnType: "string",
            targetColumnName: "",
            dataColumn: "",        // 允许重复，留空让用户选
            keyColumnValue: "",
            format: "",
            length: 50,
            precision: 0,
            nullIf: "",
            aggregation: "",
        });
    }
}

function handleDelete(index, typeVal) {
    const realIndex = Number(index) - 1;
    const targetFields = typeVal == 2 ? tableFields.value : groupTableField.value;
    targetFields.splice(realIndex, 1);
}
let newFields = ref([]);
const handleFetchFields = (flag) => {
    type.value = flag;
    targetFieldsRef.value = flag == 2 ? tableFields.value : groupTableField.value;
    if (!targetFieldsRef.value) {
        proxy.$message.warning("操作失败，目标字段未找到");
        return;
    }
    if (flag == 1) {
        newFields.value = [...tableFields.value];
    } else if (flag == 2) {
        const keyFieldNames = form.value?.taskParams?.keyField || [];
        const groupedFieldNames = new Set(groupTableField.value.map(f => f.columnName));
        newFields.value = tableFields.value.filter(
            f => !groupedFieldNames.has(f.columnName) && !keyFieldNames.includes(f.columnName)
        );
    }
    showConflictDialog.value = true;
};

function onResolveFields(payload) {
    if (!payload || !payload.action) return;

    const targetFields = type.value === 2 ? tableFields : groupTableField;
    type.value = "";
    if (!targetFields.value) {
        proxy.$message.warning("操作失败，目标字段未找到");
        return;
    }
    switch (payload.action) {
        case "addNewOnly": {
            const existingNames = targetFields.value.map((f) => f.columnName);
            const newUniqueFields = inputFields.value.filter((f) => !existingNames.includes(f.columnName));
            targetFields.value = targetFields.value.concat(JSON.parse(JSON.stringify(newUniqueFields)));
            break;
        }
        case "addAll":
        case "clearAndAddAll": {
            targetFields.value = JSON.parse(JSON.stringify(inputFields.value));
            break;
        }
    }
}

function off() {
    proxy.resetForm("dpModelRefs");
    tableFields.value = [];
    groupTableField.value = [];
    form.value = {};
}

const saveData = async () => {
    try {
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;

        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning("校验未通过，请至少添加一个目标字段");
            return;
        }
        if (!groupTableField.value || groupTableField.value.length === 0) {
            proxy.$message.warning("校验未通过，请至少添加一个分组字段");
            return;
        }
        let falg = validateTargetFields(tableFields.value);
        if (!falg) return
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
        const outputFields = [...inputFields.value.filter(item => item.columnName != form.value.taskParams?.keyField && !tableFields.value.some(f => f.dataColumn === item.columnName)), ...tableFields.value.map(f => ({ ...f, columnName: f.targetColumnName || f.columnName, source: form.value.name }))];
        taskParams.outputFields = outputFields;
        taskParams.tableFields = tableFields.value;
        taskParams.groupTableField = groupTableField.value
        taskParams.groupTableFields = groupTableField.value.map(item => item.columnName);
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

watchEffect(() => {
    if (!props.visible) {
        off();
        return;
    }
    form.value = JSON.parse(JSON.stringify(props.currentNode?.data || {}));
    const taskParams = JSON.parse(JSON.stringify(props.currentNode?.data?.taskParams || {}));
    inputFields.value = taskParams.inputFields || [];
    tableFields.value = taskParams.tableFields || [];
    groupTableField.value = taskParams.groupTableField || [];
});
</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
