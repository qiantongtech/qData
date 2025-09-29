<template>
    <el-dialog v-model="visibleDialog" :draggable="true" class="medium-dialog" :title="currentNode?.data?.name"
        showCancelButton :show-close="false" destroy-on-close>
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
            <el-divider content-position="center">
                <span class="blue-text">字段</span>
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

                <el-table-column label="结果字段名称" align="left" prop="columnName" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.columnName" placeholder="请输入字段" style="width: 100%"
                            @blur="() => handleColumnNameChange(scope.row)" />
                    </template>
                </el-table-column>
                <el-table-column label="计算类型" align="left" prop="calcType" width="250">
                    <template #default="scope">
                        <el-select v-model="scope.row.calcType" placeholder="请选择计算类型" filterable style="width: 100%"
                            @change="val => onCalcTypeChange(scope.row, val)">
                            <el-option v-for="item in calcTypeOptions" :key="item.type" :label="item.type"
                                :value="item.type" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="字段A" align="left" prop="fieldA" width="130">
                    <template #default="scope">
                        <el-select v-if="showField(scope.row, 'fieldA')" v-model="scope.row.fieldA" placeholder="请选择字段A"
                            style="width: 100%">
                            <el-option v-for="item in getFilteredFields(scope.row.columnName)" :key="item.columnName"
                                :label="item.columnName" :value="item.columnName" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="字段B" align="left" prop="fieldB" width="130">
                    <template #default="scope">
                        <el-select v-if="showField(scope.row, 'fieldB')" v-model="scope.row.fieldB" placeholder="请选择字段B"
                            style="width: 100%">
                            <el-option v-for="item in getFilteredFields(scope.row.columnName)" :key="item.columnName"
                                :label="item.columnName" :value="item.columnName" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="字段C" align="left" prop="fieldC" width="130">
                    <template #default="scope">
                        <el-select v-if="showField(scope.row, 'fieldC')" v-model="scope.row.fieldC" placeholder="请选择字段C"
                            style="width: 100%">
                            <el-option v-for="item in getFilteredFields(scope.row.columnName)" :key="item.columnName"
                                :label="item.columnName" :value="item.columnName" />
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column label="值类型" align="left" prop="columnType" width="130">
                    <template #default="scope">
                        <!-- <el-select v-model="scope.row.columnType" placeholder="请选择字段类型" style="width: 100%">
                            <el-option v-for="dict in columntype" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select> -->
                        {{ scope.row.columnType }}
                    </template>
                </el-table-column>
                <!-- <el-table-column label="字段长度"  align="left" prop="length" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.length" placeholder="请输入字段长度" style="width: 100%" />
                    </template>
                </el-table-column>

                <el-table-column label="字段精度"  align="left" prop="precision" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.precision" placeholder="请输入字段精度" style="width: 100%" />
                    </template>
                </el-table-column>
                <el-table-column label="移除否"  align="left" prop="decimalSymbol" width="150">
                    <template #default="scope">
                        <el-input v-model="scope.row.decimalSymbol" placeholder="请输入小数符号" style="width: 100%" />
                    </template>
                </el-table-column> -->

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
                <!-- <el-button type="primary" @click="handleFetchFields">获取字段</el-button> -->
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

const calcTypeOptions = [
    {
        "type": "A + B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "两个数相加"
    },
    {
        "type": "A - B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "两个数相减"
    },
    {
        "type": "A * B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "两个数相乘"
    },
    {
        "type": "A / B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "两个数相除"
    },
    {
        "type": "A + B + C",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": true,
        "description": "三个数相加"
    },
    {
        "type": "Min(A, B)",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "返回较小值"
    },
    {
        "type": "Max(A, B)",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "返回较大值"
    },
    {
        "type": "Abs(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "取绝对值"
    },
    {
        "type": "Round(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "四舍五入"
    },
    {
        "type": "Ceil(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "向上取整"
    },
    {
        "type": "Floor(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "向下取整"
    },
    {
        "type": "A MOD B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "取模（余数）"
    },
    {
        "type": "A ^ B",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "A 的 B 次幂"
    },
    {
        "type": "SQRT(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "平方根"
    },
    {
        "type": "A + B Days",
        "value": "date",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "在日期 A 上加 B 天"
    },
    {
        "type": "A - B (in days)",
        "value": "number",
        "fieldA": true,
        "fieldB": true,
        "fieldC": false,
        "description": "日期 A 与 B 相差的天数"
    },
    {
        "type": "Year of date A",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "提取日期 A 的年份"
    },
    {
        "type": "Month of date A",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "提取日期 A 的月份"
    },
    {
        "type": "Day of date A",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "提取日期 A 的日"
    },
    {
        "type": "String length(A)",
        "value": "number",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "字符串长度"
    },
    {
        "type": "Upper case(A)",
        "value": "string",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "转大写"
    },
    {
        "type": "Lower case(A)",
        "value": "string",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "转小写"
    },
    {
        "type": "Init cap(A)",
        "value": "string",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "首字母大写（每个单词）"
    },
    {
        "type": "Trim(A)",
        "value": "string",
        "fieldA": true,
        "fieldB": false,
        "fieldC": false,
        "description": "去除前后空格"
    }
]

function handleAddField() {
    const lastField = tableFields.value[tableFields.value.length - 1];

    // 校验重复字段名
    const names = tableFields.value.map(item => item.columnName?.trim());
    if (new Set(names).size !== names.length) {
        proxy.$message.warning("存在重复的字段名称，请修改后再新增");
        return;
    }

    if (lastField) {
        const missingFields = [];

        if (!lastField.columnName) {
            missingFields.push('字段名');
        }
        if (!lastField.calcType) {
            missingFields.push('计算类型');
        }

        // 只有计算类型有了，才去校验字段A/B/C
        if (lastField.calcType) {
            const rule = calcTypeOptions.find(item => item.type === lastField.calcType);
            const keyLabelMap = {
                fieldA: '字段A',
                fieldB: '字段B',
                fieldC: '字段C'
            };

            if (rule) {
                for (const key of ['fieldA', 'fieldB', 'fieldC']) {
                    if (rule[key] && !lastField[key]) {
                        missingFields.push(keyLabelMap[key]);
                    }
                }
            }
        }

        if (missingFields.length > 0) {
            proxy.$message.warning(
                `字段【${lastField.columnName || '未命名字段'}】缺少必填项：${missingFields.join('，')}，请填写完整后再新增`
            );
            return;
        }
    }

    // 新增字段
    tableFields.value.push({
        columnName: null,
        calcType: null,
        fieldA: null,
        fieldB: null,
        fieldC: null,
        columnType: null,
        source: form.value.name,
    });
}
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
        proxy.$message.warning("删除失败，字段未找到");
    }
}
const off = () => {
    proxy.resetForm("dpModelRefs");
    tableFields.value = [];
    originalTableFieldsBackup.value = [];
    form.value = {};
    row.value = {};
};
function getFilteredFields(currentName) {
    return inputFields.value.concat(tableFields.value).filter(item => item.columnName != currentName);
}
function showField(row, fieldKey) {
    const match = calcTypeOptions.find(item => item.type === row.calcType);
    return match?.[fieldKey] ?? false;
}

function onCalcTypeChange(row, newVal) {
    const newRule = calcTypeOptions.find(item => item.type == newVal);
    console.log("🚀 ~ onCalcTypeChange ~ newRule:", newRule)
    const oldRule = calcTypeOptions.find(item => item.type == row.calcType);
    row.calcType = newVal;

    // 自动设置 columnType
    row.columnType = newRule?.value || 'string';

    // 字段A/B/C 处理逻辑
    ['fieldA', 'fieldB', 'fieldC'].forEach(field => {
        const wasRequired = oldRule?.[field];
        const nowRequired = newRule?.[field];
        if (!nowRequired && row[field]) {
            // proxy.$message.info(`字段 ${field.toUpperCase()} 不再需要，已清空`);
            row[field] = '';
        }
    });
}
const saveData = async () => {
    try {
        const valid = await dpModelRefs.value.validate();
        if (!valid) return;

        if (!tableFields.value || tableFields.value.length === 0) {
            proxy.$message.warning("校验未通过，请至少添加一个字段");
            return;
        }

        // 字段名不能为空且不能重复
        const hasEmptyName = tableFields.value.some(item => !item.columnName?.trim());
        if (hasEmptyName) {
            proxy.$message.warning("校验未通过，字段名称不能为空");
            return;
        }
        const names = tableFields.value.map(item => item.columnName.trim());
        if (new Set(names).size !== names.length) {
            proxy.$message.warning("字段名称存在重复，请修改后再保存");
            return;
        }

        // 校验每行缺少哪些必填项，收集后统一提示
        for (const row of tableFields.value) {
            const missingFields = [];

            if (!row.columnName) missingFields.push('字段名');
            if (!row.calcType) missingFields.push('计算类型');

            const keyLabelMap = {
                fieldA: '字段A',
                fieldB: '字段B',
                fieldC: '字段C'
            };
            if (row.calcType) {
                const rule = calcTypeOptions.find(item => item.type === row.calcType);
                if (rule) {
                    for (const key of ['fieldA', 'fieldB', 'fieldC']) {
                        if (rule[key] && !row[key]) {
                            missingFields.push(keyLabelMap[key]);
                        }
                    }
                }
            }

            if (missingFields.length > 0) {
                proxy.$message.warning(
                    `字段【${row.columnName || '未命名字段'}】缺少必填项：${missingFields.join('，')}，请补充完整`
                );
                return;
            }
        }

        // 获取唯一code
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
        taskParams.outputFields = [...inputWithoutSplit, ...tableFields.value.map(item => ({ ...item }))];
        taskParams.tableFields = tableFields.value;
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
