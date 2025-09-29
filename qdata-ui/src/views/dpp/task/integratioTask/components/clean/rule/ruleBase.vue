<template>
    <!-- 清洗规则基础页面   -->
    <el-dialog v-model="dialogVisible" draggable class="medium-dialog"
        :class="{ 'max-dialogs-status0': dialogStatus === 0 }" :title="dialogTitle" destroy-on-close
        :append-to="$refs['app-container']">
        <div class="content" v-if="dialogStatus == 0">
            <SideMenu :dialogStatus="dialogStatus" @card-click="handleCardClick" ref="SideMenus" :type="type" />
        </div>
        <div class="content" style="height: 600px; padding-right: 10px;" v-show="dialogStatus == 1 || dialogStatus == 2"
            :disabled="dialogStatus == 2">
            <el-form ref="formRef" :model="form" label-width="130px">
                <div class="h2-title">基础信息</div>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="清洗名称" prop="name"
                            :rules="[{ required: true, message: '请输入清洗名称名称', trigger: 'blur' }]">
                            <el-input v-model="form.name" placeholder="请输入清洗名称" :disabled="falg" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="清洗规则编号" prop="ruleCode">
                            <el-input v-model="form.ruleCode" placeholder="请输入清洗规则编号" disabled />
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="清洗规则名称" prop="ruleName">
                            <el-input v-model="form.ruleName" placeholder="请输入清洗规则名称" disabled />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="状态" prop="status">
                            <el-radio-group v-model="form.status" :disabled="falg">
                                <el-radio :value="'1'">上线</el-radio>
                                <el-radio :value="'0'">下线</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="规则描述" prop="ruleDesc">
                            <el-input type="textarea" v-model="form.ruleDesc" placeholder="请输入规则描述" :disabled="falg" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="Where 条件" prop="whereClause">
                            <el-input type="textarea" v-model="form.whereClause" placeholder="请输入 Where 条件"
                                :disabled="falg" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 规则配置 -->
                <div class="h2-title">规则配置</div>
                <el-row v-if="type != 3">
                    <el-col :span="24">
                        <el-form-item label="清洗字段" prop="columns" :disabled="falg"
                            :rules="[{ required: true, message: '请选择清洗字段', trigger: 'blur' }]">
                            <el-select v-if="isMultipleSelect" v-model="form.columns" placeholder="请选择清洗字段" multiple
                                clearable>
                                <el-option v-for="dict in processedFields" :key="dict.columnName" :label="dict.label"
                                    :value="dict.columnName" />
                            </el-select>
                            <!-- 单选 -->
                            <el-select v-else v-model="form.columns" placeholder="请选择清洗字段" clearable>
                                <el-option v-for="dict in processedFields" :key="dict.columnName" :label="dict.label"
                                    :value="dict.columnName" :disabled="form?.ruleCode == '039' && !(
                                        dict.columnType?.toUpperCase().includes('DATE') ||
                                        dict.columnType?.toUpperCase().startsWith('TIMESTAMP') ||
                                        dict.columnType?.toUpperCase() === 'TIME' ||
                                        dict.columnType?.toUpperCase() === 'YEAR'
                                    )
                                        " />

                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <component :is="currentRuleComponent" ref="ruleComponentRef" :form="form.ruleConfig"
                    :inputFields="processedFields" :falg="falg" :columnList="columnList" />
            </el-form>
        </div>
        <template #footer>
            <template v-if="dialogStatus == 1"><el-button type="primary" @click="handleSave" v-if="!falg">确定</el-button>
                <el-button @click="handleBack" v-if="!mode">返回</el-button>
                <!-- <el-button type="warning" @click="handleSpotCheck">预览</el-button> -->
            </template>
            <el-button @click="closeDialog" v-else>关闭</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import SideMenu from "./ruleSelectorMenu.vue";
// 数值边界值调整
import NumberRangeSelector from "./numberBoundaryRule.vue";
// 字段前缀/后缀统一
import AffixEditor from "./affixEditorRule.vue";
// 枚举值映射标准化
import EnumRule from "./enumMapRule/index.vue";
// 按组合字段去重
import FieldCombiner from "./combinerFieldUniqueRule.vue";

// 站位组件
import EmptyRule from "./emptyRule.vue";
import moment from "moment";
let falg = ref(false)
const { proxy } = getCurrentInstance();
const { quality_warning_status, } =
    proxy.useDict(
        "quality_warning_status",
    );
const emit = defineEmits(["confirm"]);
// 父组件传入评测对象列表
const props = defineProps({
    inputFields: {
        type: Array,
        default: () => [],
    },
    type: {
        type: String,
        default: ''
    },
});

const { inputFields } = toRefs(props);
const processedFields = computed(() => {
    return inputFields.value.map(item => ({
        ...item,
        label: item.columnComment
            ? `${item.columnName} / ${item.columnComment}`
            : item.columnName
    }))
})

const dialogVisible = ref(false);
const dialogStatus = ref(1);
const dialogTitle = ref("");
const formRef = ref();

let form = reactive({
    name: '',
    ruleName: "",//清洗规则名称：
    ruleCode: "",//清洗规则编号：
    status: "1",
    // warningLevel: "2",
    whereClause: "",
    columns: '',
    tableName: "",
    ruleDesc: "",
    type: '',
    ruleConfig: {
        //数值边界调整
        max: '100',
        min: "0",
        handleType: "1",
        // 去除字符串空格
        handleType: "1",//"1-去除前后空格，2-去除所有空格"
        // 正则表达式替换
        pattern: "",//表达式
        replacement: "",//replacement
        ruleValue: [],
        deduplicationStrategy: "1",
        dataRangeValue: moment().format("YYYY-MM-DD"),
        // 数据添加值
        stringValue: "",//添加值

    }
});
const isMultipleSelect = computed(() => {
    return form.ruleCode == '019' || form.ruleCode == '029';
})
let title = ref()
const ruleConfigMap = {
    // 数值范围
    "001": {
        label: "数值边界值调整",
        component: NumberRangeSelector,
    },
    //
    "010": {
        label: "字段前缀/后缀统一",
        component: AffixEditor,
    },
    "019": {
        label: "组合字段为空删除",
        component: EmptyRule,
    },

    "024": {
        label: "枚举值映射标准化",
        component: EnumRule,
    },
    "029": {
        label: "按组合字段去重",
        component: FieldCombiner,
    },

};

// 计算属性：当前规则配置
const currentRuleConfig = computed(() => {
    return ruleConfigMap[form.ruleCode] || null;
});

// 计算属性：当前规则组件
const currentRuleComponent = computed(() => {
    return currentRuleConfig.value?.component || null;
});

let loading = ref(false);
let columnList = ref([]);

let ruleComponentRef = ref()
async function handleSave() {
    await nextTick();
    try {
        await formRef?.value?.validate();
    } catch (err) {
        proxy.$message.warning("请完善必填项");
        return;
    }
    let res = { valid: true, data: {} };
    res = await ruleComponentRef.value?.validate();
    if (!res.valid) return;
    if (!isMultipleSelect.value) {
        form.columns = [form.columns]

    }
    if (form.ruleCode == '035') {

    }
    const formCopy = JSON.parse(JSON.stringify({
        ...form,
        ruleConfig: JSON.stringify({
            columns: form.columns,
            ...res.data,
            parentName: form.parentName
        }),
    }));

    emit('confirm', formCopy, mode.value);
}
let sampleCheckMsg = ref()

function handleCardClick(data) {
    resetForm()
    form.ruleName = data?.name;
    form.ruleCode = data?.code;
    form.ruleType = data?.strategyKey;
    form.type = data?.type;
    form.parentName = data?.parentName;
    form.dimensionType = data?.qualityDim
    console.log("🚀 ~ handleCardClick ~ data:", data)
    console.log("🚀 ~ handleCardClick ~  form.dimensionType:", form.dimensionType)
    dialogTitle.value = `新增清洗规则${data?.name ? '-' + data.name : ''}`
    dialogStatus.value = 1;
}
let mode = ref();
async function openDialog(record, index, fg) {
    falg.value = fg;
    mode.value = index;
    resetForm();
    dialogTitle.value = `${mode.value ? '修改' : '新增'}清洗规则${record?.ruleName ? `-${record.ruleName}` : ''}`;
    if (falg?.value) {
        dialogTitle.value = `清洗规则${record?.ruleName ? `-${record.ruleName}` : ''}`;
    }
    dialogStatus.value = mode.value ? 1 : 0;
    dialogVisible.value = true;

    if (index) {
        dialogStatus.value = 1;
        const { ruleType, ruleConfig, columns, ...rest } = record;
        Object.assign(form, rest);
        form.ruleType = ruleType;

        try {
            form.ruleConfig = typeof ruleConfig == 'string' ? JSON.parse(ruleConfig) : ruleConfig;
        } catch (e) {
            form.ruleConfig = {};
        }
        if (isMultipleSelect.value) {
            form.columns = Array.isArray(columns) ? columns : [];
        } else {
            form.columns = Array.isArray(columns) && columns.length > 0 ? columns[0] : '';
        }
    } else {
        resetForm();
    }
}

const initialForm = () => ({
    id: '',
    name: '',
    type: '',
    ruleName: "",//清洗规则名称：
    ruleCode: "",//清洗规则编号：
    status: "1",
    whereClause: "",
    columns: isMultipleSelect.value ? [] : '',
    tableName: "",
    ruleDesc: "",
    ruleConfig: {

        //数值边界调整
        max: '100',
        min: "0",
        handleType: "1",
        // 去除字符串空格
        handleType: "1",//"1-去除前后空格，2-去除所有空格"
        // 正则表达式替换
        pattern: "",//表达式
        replacement: "",//replacement

        ruleValue: [],
        deduplicationStrategy: "1",
        // 枚举值映射标准化
        stringValue: [],
        dataRange: "1",// 0：固定时间范围，1：具体日期
        dataRangeType: "1",// 0：天前
        dataRangeValue: moment().format("YYYY-MM-DD"),
        handleType: "1",// 0：过期处理方式，1：删除记录
        handleColumns: "",// // 标记字段     选中过期处理方式才会有
        handleValue: "",// 标记值       选中过期处理方式才会有

    }
});

function resetForm() {
    Object.assign(form, initialForm());
    columnList.value = [];
    title.value = ''
    sampleCheckMsg.value = ''
}

function closeDialog() {
    dialogVisible.value = false;
    resetForm();
}

function handleBack() {
    dialogStatus.value = 0;
    dialogTitle.value = `新增评测规则`
    resetForm()
}
defineExpose({ openDialog, closeDialog })
</script>

<style scoped>
.blue-text {
    color: var(--el-color-primary);
}

.medium-dialog {
    width: 800px;
}
</style>
<style>
.el-dialog.max-dialogs-status0 .el-dialog__body {
    padding: 0 !important;
    padding-left: 10px !important;
}
</style>
