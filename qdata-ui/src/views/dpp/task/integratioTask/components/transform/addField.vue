<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
        <template #header>
            <div class="justify">
                <span class="el-dialog__title">{{ currentNode?.data?.name }}</span>
                <el-tooltip effect="light" content="为数据集中的每一行生成唯一的序列号，常用于为记录分配递增的标识符" placement="top">
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
                    <el-form-item label="序列字段名称" prop="taskParams.sequenceFieldName" :rules="[
                        { required: true, message: '请输入序列字段名称', trigger: 'change' }
                    ]">
                        <template #label>
                            <div class="justify-center">
                                <span>序列字段名称</span>
                                <el-tooltip effect="light" content="会将自动生成的序列输出到这一列" placement="top">
                                    <el-icon class="tip-icon">
                                        <InfoFilled />
                                    </el-icon>
                                </el-tooltip>
                            </div>
                        </template>
                        <el-input v-model="form.taskParams.sequenceFieldName" placeholder="请输入序列字段名称" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="ID生成方式" prop="taskParams.idGenerateType">
                        <el-radio-group v-model="form.taskParams.idGenerateType">
                            <el-radio value="1">增加序列</el-radio>
                            <el-radio value="2">增加UUID</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>

            </el-row>

            <template v-if="form.taskParams.idGenerateType == '1'">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="起始值" prop="taskParams.sequenceStartValue" :rules="[
                            { required: true, message: '请输入起始值', trigger: 'change' },
                            { pattern: /^[1-9]\d*$/, message: '只能输入正整数', trigger: 'change' }
                        ]">
                            <el-input-number v-model="form.taskParams.sequenceStartValue" placeholder="请输入起始值"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="递增幅度" prop="taskParams.sequenceStep" :rules="[
                            { required: true, message: '请输入递增幅度', trigger: 'change' },
                            { pattern: /^[1-9]\d*$/, message: '只能输入正整数', trigger: 'change' }
                        ]">
                            <el-input-number v-model="form.taskParams.sequenceStep" placeholder="请输入递增幅度"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="最大值" prop="taskParams.sequenceMaxValue" :rules="[
                            { required: true, message: '请输入最大值', trigger: 'change' },
                            { pattern: /^[1-9]\d*$/, message: '只能输入正整数', trigger: 'change' }
                        ]">
                            <el-input-number v-model="form.taskParams.sequenceMaxValue" placeholder="请输入最大值"
                                style="width: 100%" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="描述" prop="description">
                        <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                    </el-form-item>
                </el-col>
            </el-row>

        </el-form>
        <template #footer>
            <div style="text-align: right">
                <el-button @click="closeDialog">关闭</el-button>
                <el-button type="primary" @click="saveData" v-if="!info">保存</el-button>
                <!-- <el-button type="primary" @click="handleFetchFields">获取字段字段</el-button> -->
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

        const taskParams = form.value.taskParams || {};

        if (taskParams.idGenerateType === '2') {
            taskParams.sequenceStartValue = null;
            taskParams.sequenceStep = null;
            taskParams.sequenceMaxValue = null;
        } else {
            const { sequenceStartValue, sequenceStep, sequenceMaxValue } = taskParams;
            const start = parseInt(sequenceStartValue, 10);
            const step = parseInt(sequenceStep, 10);
            const max = parseInt(sequenceMaxValue, 10);
            if (max <= start) {
                proxy.$message.warning("校验未通过，最大值必须大于起始值");
                return;
            }

            if (start + step > max) {
                proxy.$message.warning("校验未通过，起始值加递增幅度不能大于最大值");
                return;
            }
        }

        // 如果没有 code，获取唯一 key
        if (!form.value.code) {
            loading.value = true;
            const response = await getNodeUniqueKey({
                projectCode: userStore.projectCode,
                projectId: userStore.projectId,
            });
            loading.value = false;
            form.value.code = response.data;
        }

        // 输出字段新增目标字段
        taskParams.outputFields = [
            ...inputFields.value,
            {
                columnName: form.value.taskParams.sequenceFieldName,
                source: form.value.name
            }
        ];

        taskParams.tableFields = tableFields.value;
        form.value.taskParams = taskParams;

        console.log("保存数据:", JSON.parse(JSON.stringify(taskParams)));
        emit("confirm", form.value);
        closeDialog();
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
    form.value = deepCopy({
        ...props.currentNode?.data,
        taskParams: {
            idGenerateType: '1',
            ...(props.currentNode?.data?.taskParams || {})
        }
    });
    nodeOptions.value = createNodeSelect(props.graph, props.currentNode.id);
    let taskParams = deepCopy(props.currentNode?.data?.taskParams || {});
    inputFields.value = taskParams?.inputFields || [];
    tableFields.value = taskParams?.tableFields || [];
    originalTableFieldsBackup.value = deepCopy(taskParams?.tableFields || []);

});


</script>

<style scoped lang="less">
.blue-text {
    color: #2666fb;
}
</style>
