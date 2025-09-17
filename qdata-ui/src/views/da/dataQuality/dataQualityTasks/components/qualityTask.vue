<template>
    <div class="app-container" ref="app-container" v-loading="loadingInstance">
        <div class="pagecont-top" v-loading="loading" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="steps-inner">
                    <ul class="zl-step" style="width: 99.5%">
                        <li v-for="(item, index) in stepsList" :key="index" :id="'li' + item.id" :class="active >= index && activeReult != index
                            ? 'cur'
                            : activeReult == index
                                ? 'statusEnd'
                                : 'cur'" :style="{ width: '33%' }">
                            <span>{{ item.name }}</span>
                            <div v-if="index < stepsList.length - 1" :id="'div' + item.id" :class="active >= index && activeReult != index
                                ? 'jiaoActive'
                                : activeReult == index
                                    ? 'titleItem'
                                    : 'jiaoActive'"></div>
                            <div class="interval"></div>
                        </li>
                    </ul>
                </div>

                <div class="main">
                    <el-form ref="formRef" :model="form" label-width="170px" v-show="activeReult == 0"
                        :disabled="route.query.info">
                        <el-form-item label="任务名称" prop="taskName"
                            :rules="[{ required: true, message: '请输入任务名称', trigger: 'blur' }]">
                            <el-input v-model="form.taskName" placeholder="请输入任务名称" />
                        </el-form-item>
                        <el-form-item label="任务分类" prop="catCode"
                            :rules="[{ required: true, message: '请选择任务分类', trigger: 'change' }]">
                            <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                                :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID"
                                placeholder="请选择任务分类" check-strictly />
                        </el-form-item>

                        <el-form-item label="责任人" prop="contactId">
                            <el-tree-select filterable v-model="form.contactId" :data="userList" :props="{
                                value: 'userId',
                                label: 'nickName',
                                children: 'children',
                            }" value-key="ID" placeholder="请选择责任人" check-strictly @change="handleContactChange" />
                        </el-form-item>
                        <el-row :gutter="20">
                            <el-col :span="24">
                                <el-form-item label="执行策略" prop="strategy"
                                    :rules="[{ required: true, message: '请选择执行策略', trigger: 'blur' }]">
                                    <el-select class="el-form-input-width" v-model="form.strategy" placeholder="请选择执行策略"
                                        style="width: 100%">
                                        <el-option v-for="dict in dpp_etl_task_execution_type" :key="dict.value"
                                            :label="dict.label" :value="dict.value"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="调度周期" prop="cycle"
                                    :rules="[{ required: true, message: '请选择调度周期', trigger: 'blur' }]">
                                    <el-input v-model="form.cycle" placeholder="请选择调度周期">
                                        <template #append>
                                            <el-button type="primary" @click="handleShowCron"
                                                style="background-color: #2666fb; color: #fff">
                                                配置
                                                <i class="el-icon-time el-icon--right"></i>
                                            </el-button>
                                        </template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="任务状态" prop="status">
                                    <el-radio-group v-model="form.status" class="el-form-input-width">
                                        <el-radio v-for="dict in da_discovery_task_status" :key="dict.value"
                                            :label="dict.value" disabled>
                                            {{ dict.label }}
                                        </el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-divider content-position="left">
                            <span class="blue-text">属性信息</span>
                        </el-divider>
                        <el-row :gutter="20">
                            <el-col :span="12">
                                <el-form-item label="任务优先级" prop="priority"
                                    :rules="[{ required: true, message: '请选择任务优先级', trigger: 'change' }]">
                                    <el-select v-model="form.priority" placeholder="请选择任务优先级">
                                        <el-option v-for="dict in priorityOptions" :key="dict.value" :label="dict.label"
                                            :value="dict.value" />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="Worker分组" prop="workerGroup"
                                    :rules="[{ required: true, message: '请输入Worker分组', trigger: 'blur' }]">
                                    <el-input v-model="form.workerGroup" placeholder="请输入Worker分组" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="失败重试次数" prop="retryTimes"
                                              :rules="[{ validator: createNatureIntegerValidator('失败重试次数'), trigger: 'change' }]">
                                    <el-input type="number" v-model.number="form.retryTimes" placeholder="请输入失败重试次数" :min="0">
                                        <template #append>次</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="延迟执行时间" prop="delayTime"
                                              :rules="[{ validator: createNatureIntegerValidator('延迟执行时间'), trigger: 'change' }]">
                                    <el-input type="number" v-model.number="form.delayTime" placeholder="请输入延迟执行时间">
                                        <template #append>分</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="任务描述" prop="description">
                            <el-input v-model="form.description" type="textarea" placeholder="请输入任务描述" />
                        </el-form-item>
                    </el-form>
                    <div v-loading="loadingList" v-show="activeReult == 1">
                        <div class="justify-between mb15">
                            <el-row :gutter="15" class="btn-style">
                                <el-col :span="1.5">
                                    <el-button type="primary" icon="Plus" @click="openDialog(undefined)"
                                        v-if="!route.query.info">新增</el-button>
                                </el-col>
                            </el-row>
                        </div>
                        <el-table stripe height="500px" :data="dppQualityTaskObjSaveReqVO">
                            <el-table-column label="序号" type="index" align="center" />
                            <el-table-column label="稽查对象名称" align="center" prop="name" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.name }}
                                </template>
                            </el-table-column>
                            <el-table-column label="数据连接名称" align="center" prop="type" show-overflow-tooltip>
                                <template #default="scope">
                                    <img :src="getDatasourceIcon(scope.row.datasourceType)" class="iconimg " />
                                    {{ scope.row.datasourceType }}
                                </template>
                            </el-table-column>
                            <el-table-column label="模式名称" align="center" prop="type" show-overflow-tooltip>
                                <template #default="scope">
                                    <template v-if="scope.row.datasourceConfig">
                                        <template v-if="JSON.parse(scope.row.datasourceConfig).dbname">
                                            {{ JSON.parse(scope.row.datasourceConfig).dbname }}
                                        </template>
                                    </template>
                                </template>
                            </el-table-column>

                            <el-table-column label="表名称" align="center" prop="tableName" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.tableName }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                                fixed="right" width="120" v-if="!route.query.info">
                                <template #default="scope">
                                    <el-button link type="primary" icon="Edit"
                                        @click="openDialog(scope.row, scope.$index + 1)">修改</el-button>
                                    <el-button link type="danger" icon="Delete"
                                        @click="handleDelete(scope.row)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div v-loading="loadingList" v-show="activeReult == 2">
                        <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                            @submit.prevent>
                            <el-form-item label="规则名称" prop="name">
                                <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入规则名称"
                                    clearable @keyup.enter="handleQuery" />
                            </el-form-item>
                            <el-form-item label="质量维度" prop="dimensionType">
                                <el-select v-model="queryParams.dimensionType" placeholder="请选择质量维度"
                                    style="width: 210px;">
                                    <el-option v-for="dict in att_rule_audit_q_dimension" :key="dict.value"
                                        :label="dict.label" :value="dict.value"></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="状态" prop="publishStatus">
                                <el-select v-model="queryParams.publishStatus" placeholder="请选择状态" clearable
                                    class="el-form-input-width">
                                    <el-option label="上线" value="online" />
                                    <el-option label="下线" value="offline" />
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-button plain type="primary" @click="handleQuery"
                                    @mousedown="(e) => e.preventDefault()">
                                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                                </el-button>
                                <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                                    <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                                </el-button>
                            </el-form-item>
                        </el-form>
                        <div class="justify-between mb15">
                            <el-row :gutter="15" class="btn-style">
                                <el-col :span="1.5">
                                    <el-button type="primary" icon="Plus" @click="openRuleSelector(undefined)"
                                        v-if="!route.query.info">新增</el-button>
                                </el-col>
                                <el-col :span="1.5">
                                    <el-tooltip content="会自动获取资产关联的数据元中的稽查规则" placement="top">
                                        <el-button type="warning" @click="selectInspectionRule(undefined)"
                                            v-if="!route.query.info">
                                            <el-icon style="margin-right: 4px;">
                                                <Refresh />
                                            </el-icon>
                                            获取稽查规则
                                        </el-button>
                                    </el-tooltip>
                                </el-col>
                            </el-row>
                        </div>
                        <el-table stripe height="500px" :data="dppQualityTaskEvaluateSaveReqVO">
                            <el-table-column label="序号" type="index" align="center" />
                            <el-table-column label="评测名称" align="center" prop="name" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.name || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="评测字段" align="center" prop="evaColumn" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.evaColumn || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="稽查规则" align="center" prop="ruleName" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.ruleName || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="规则描述" align="center" prop="ruleDescription" show-overflow-tooltip>
                                <template #default="scope">
                                    {{ scope.row.ruleDescription || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="质量维度" align="center" prop="dimensionType" show-overflow-tooltip>
                                <template #default="scope">
                                    <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
                                </template>
                            </el-table-column>

                            <el-table-column label="状态" align="center" prop="status">
                                <template #default="scope">
                                    {{ scope.row.status == '1' ? '上线' : '下线' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                                fixed="right" width="180" v-if="!route.query.info">
                                <template #default="scope">
                                    <el-button link type="primary" icon="view"
                                        @click="openRuleDialog(scope.row, scope.$index + 1, true)">查看</el-button>
                                    <el-button link type="primary" icon="Edit"
                                        @click="openRuleDialog(scope.row, scope.$index + 1)">修改</el-button>
                                    <el-button link type="danger" icon="Delete"
                                        @click="handleRuleDelete(scope.$index + 1)">删除</el-button>

                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="button-style">
                    <el-button type="primary" @click="handleSuccess">返回列表</el-button>
                    <el-button type="primary" v-if="activeReult === 2 && !route.query.info" @click="submitForm"
                        :loading="loadingOptions.loading">
                        保存并退出
                    </el-button>
                    <el-button :disabled="activeReult === 0" @click="handleLastStep">上一步</el-button>
                    <el-button v-if="activeReult !== 2" @click="handleNextStep">下一步</el-button>
                </div>
            </div>
        </div>
        <el-dialog title="Cron表达式生成器" v-model="openCron" destroy-on-close>
            <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression"> </crontab>
        </el-dialog>
        <InspectionTargetDialog ref="inspectionTargetDialog" @confirm="Inspectionconfirm" />
        <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm"
            :dppQualityTaskObjSaveReqVO="dppQualityTaskObjSaveReqVO" />



    </div>
</template>

<script setup name="qualityTask">
import { ref, reactive, toRefs, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import InspectionTargetDialog from './InspectionTargetDialog.vue';
import RuleSelectorDialog from './RuleSelectorDialog.vue';
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttQualityCat } from "@/api/att/cat/attQualityCat/AttQualityCat.js";
import { getDppQualityTask, } from "@/api/dpp/qa/DppQualityTask"
import {
    addDppQualityTask,
    updateDppQualityTask
} from "@/api/da/dppQuality/DppQualityTask";
import Crontab from "@/components/Crontab/index.vue";
import {
    getColumnByAssetId,
} from '@/api/dpp/etl/dppEtlTask';
import {createNatureIntegerValidator} from '@/utils/validator'
import { treeData } from "./data.js"
const { proxy } = getCurrentInstance();
const route = useRoute();
const loading = ref(false);
const showSearch = ref(true);
let id = route.query.id || '';
const router = useRouter();
const { att_rule_audit_q_dimension, da_discovery_task_status, dpp_etl_task_execution_type } = proxy.useDict(

    'att_rule_audit_q_dimension', 'da_discovery_task_status', 'dpp_etl_task_execution_type'
);
let dppQualityTaskObjSaveReqVO = ref([

])
// 图标
const getDatasourceIcon = (type) => {
    switch (type) {
        case "DM8": return new URL("@/assets/system/images/dpp/DM.png", import.meta.url).href;
        case "Oracle11": return new URL("@/assets/system/images/dpp/oracle.png", import.meta.url).href;
        case "MySql": return new URL("@/assets/system/images/dpp/mysql.png", import.meta.url).href;
        case "Hive": return new URL("@/assets/system/images/dpp/Hive.png", import.meta.url).href;
        case "Sqlerver": return new URL("@/assets/system/images/dpp/sqlServer.png", import.meta.url).href;
        case "Kafka": return new URL("@/assets/system/images/dpp/kafka.png", import.meta.url).href;
        case "HDFS": return new URL("@/assets/system/images/dpp/hdfs.png", import.meta.url).href;
        case "SHELL": return new URL("@/assets/system/images/dpp/SHELL.png", import.meta.url).href;
        case "Kingbase8": return new URL("@/assets/system/images/dpp/kingBase.png", import.meta.url).href;
        default: return null;
    }
};

let loadingInstance = ref(null)  // 全局 loading 实例
let originList = ref([

])

const dppQualityTaskEvaluateSaveReqVO = ref([...originList.value]);

let loadingList = ref(false)
const handleQuery = () => {
    dppQualityTaskEvaluateSaveReqVO.value = originList.value.filter(item => {
        if (queryParams.value.name && !item.name.includes(queryParams.value.name)) return false;
        if (queryParams.value.dimensionType && item.dimensionType !== queryParams.value.dimensionType) return false;
        if (queryParams.value.publishStatus) {
            const statusVal = queryParams.value.publishStatus === 'online' ? '1' : '0';
            if (item.status !== statusVal) return false;
        }
        return true;
    });
};
function renameRuleToRuleConfig(data, obj) {
    return data
        .filter(col => Array.isArray(col.cleanRuleList) && col.cleanRuleList.length > 0)
        .flatMap(col =>
            col.cleanRuleList.map(item => {
                let parsedRule = {};
                try {
                    parsedRule = JSON.parse(item.rule || '{}');
                } catch (e) {
                    console.warn(`rule JSON 解析失败: ${item.rule}`, e);
                }

                const evaColumnStr = col.columnName;

                return {
                    ...item,
                    id: undefined,
                    warningLevel: '2',
                    datasourceId: obj?.datasourceId || '',
                    tableName: obj?.tableName || col.tableName,
                    evaColumn: evaColumnStr,
                    rule: JSON.stringify({
                        ...parsedRule,
                        evaColumn: evaColumnStr,
                    })
                };
            })
        );
}

async function selectInspectionRule() {
    loading.value = true;

    try {
        for (const item of dppQualityTaskObjSaveReqVO.value || []) {
            try {
                const res = await getColumnByAssetId({
                    withRule: 1,
                    id: item.datasourceId,
                    tableName: item.tableName
                });

                if (res?.data?.length) {
                    const rowsWithSource = res.data.map(row => ({
                        ...row,
                        datasourceId: item.datasourceId,
                        tableName: item.tableName
                    }));

                    const obj = renameRuleToRuleConfig(rowsWithSource, item) || [];

                    let addedCount = 0;
                    obj.forEach(newRule => {
                        // 规则唯一标识
                        const key = `${newRule.tableName}_${newRule.evaColumn}_${newRule.ruleName}`;
                        // 查找是否已存在相同规则
                        const existIndex = originList.value.findIndex(
                            r => `${r.tableName}_${r.evaColumn}_${r.ruleName}` === key
                        );
                        if (existIndex > -1) {
                            // 覆盖
                            originList.value.splice(existIndex, 1, newRule);
                        } else {
                            // 追加
                            originList.value.push(newRule);
                            addedCount++;
                        }
                    });
                    dppQualityTaskEvaluateSaveReqVO.value = [...originList.value];

                    if (addedCount > 0) {
                        ElMessage.success(`已追加 ${addedCount} 条规则，来自表 ${item.tableName}`);
                    } else {
                        // ElMessage.info(`表 ${item.tableName} 没有新规则追加`);
                    }
                }
            } catch (err) {
                console.warn(`获取规则失败: datasourceId=${item.datasourceId}, tableName=${item.tableName}`, err);
            }
        }
    } finally {
        loading.value = false;
    }
}



const resetQuery = () => {
    queryParams.value = {
        name: '',
        qualityDim: '',
        publishStatus: '',
    };
    dppQualityTaskEvaluateSaveReqVO.value = [...originList.value];
};
let deptOptions = ref([])

let userList = ref([])
let openCron = ref(false);
const expression = ref("");
/** 调度周期按钮操作 */
function handleShowCron() {
    expression.value = form.value.cycle;
    openCron.value = true;
}
/** 确定后回传值 */
async function crontabFill(value) {
    form.value.cycle = value;
    await nextTick();
    formRef.value?.validateField('cycle');
}
function getDeptTree() {
    listAttQualityCat().then((response) => {
        deptOptions.value = proxy.handleTree(response.data, "id", "parentId");
        deptOptions.value = [
            {
                name: "数据质量类目",
                value: "",
                id: 0,
                children: deptOptions.value,
            },
        ];
    });
    deptUserTree().then((res) => {
        userList.value = res.data;
    });
}
const data = reactive({
    form: {
        taskName: '',
        catCode: "",
        status: '1',
        contactId: '',
        priority: '',
        workerGroup: 'default',
        retryCount: 0,
        retryInterval: 0,
        delayMinutes: 0,
        description: '',
        retryTimes: "",
        delayTime: "",
        cycle: "",
        strategy: "PARALLEL"

    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        qualityDim: '',
        publishStatus: ''
    },
    stepsList: [
        { name: '基础信息', id: 0 },
        { name: '稽查对象信息', id: 1 },
        { name: '稽查规则', id: 2 }
    ],
    activeReult: 0,
    active: 0,
    loadingOptions: { loading: false }
});

const { form, stepsList, activeReult, loadingOptions, queryParams, active } = toRefs(data);
const formRef = ref();

const priorityOptions = ref([
    { label: '高', value: 'high' },
    { label: '中', value: 'medium' },
    { label: '低', value: 'low' }
]);

function handleLastStep() {
    activeReult.value--;
}
const inspectionTargetDialog = ref();
const openDialog = (row, index) => {
    inspectionTargetDialog.value.openDialog(row, index);
};
function handleDelete(row) {
    const idxTable = dppQualityTaskObjSaveReqVO.value.findIndex((item) => item.ruleName == row.ruleName);
    if (idxTable !== -1) {
        dppQualityTaskObjSaveReqVO.value.splice(idxTable, 1);
    } else {
        proxy.$message.warning("删除失败，字段未找到");
    }
}
function handleRuleDelete(index) {
    const realIndex = Number(index) - 1;
    originList.value.splice(realIndex, 1);
    dppQualityTaskEvaluateSaveReqVO.value = originList.value
}
function handleNextStep() {
    activeReult.value++;
}

function Inspectionconfirm(obj, mode) {
    const index = Number(mode) - 1;
    const list = dppQualityTaskObjSaveReqVO.value;
    const isDuplicate = list.some((item, i) => {
        if (index >= 0) {
            return i != index && item.name == obj.name;
        } else {
            return item.name == obj.name;
        }
    });

    if (isDuplicate) {
        proxy.$message.warning("校验未通过，稽查对象名称不能重复");
        return;
    }

    if (!isNaN(index) && index >= 0 && index < list.length) {
        list.splice(index, 1, obj);
    } else {
        list.push(obj);
    }

    inspectionTargetDialog.value.closeDialog();
}



let ruleSelectorDialog = ref()
const openRuleSelector = (row) => {
    ruleSelectorDialog.value.openDialog(row,);
};
const openRuleDialog = (row, index, falg) => {

    ruleSelectorDialog.value.openDialog(row, index, falg);
};
function RuleSelectorconfirm(obj, mode) {
    const index = Number(mode) - 1;
    const list = originList.value;
    const isDuplicate = list.some((item, i) => {
        if (index >= 0) {
            return i !== index && item.name == obj.name;
        } else {
            return item.name === obj.name;
        }
    });

    if (isDuplicate) {
        proxy.$message.warning("校验未通过，评测名称不能重复");
        return;
    }

    if (!isNaN(index) && index >= 0 && index < list.length) {
        list.splice(index, 1, obj);
    } else {
        list.push(obj);
    }

    dppQualityTaskEvaluateSaveReqVO.value = list;
    ruleSelectorDialog.value.closeDialog();
}

// 页面跳转
const handleSuccess = () => {
    router.push("/da/dataQuality/dataQualityTasks");
};
async function submitForm() {
    loadingInstance.value = true
    try {
        await formRef.value?.validate();
    } catch (err) {
        console.warn('表单校验未通过：', err);
        ElMessage.warning("校验未通过，请检查必填项");
        loadingInstance.value = false
        return;
    }
    try {
        const res = form.value.id
            ? await updateDppQualityTask({
                ...form.value,
                dppQualityTaskObjSaveReqVO: dppQualityTaskObjSaveReqVO.value,
                dppQualityTaskEvaluateSaveReqVO: dppQualityTaskEvaluateSaveReqVO.value
            })
            : await addDppQualityTask({
                ...form.value,
                dppQualityTaskObjSaveReqVO: dppQualityTaskObjSaveReqVO.value,
                dppQualityTaskEvaluateSaveReqVO: dppQualityTaskEvaluateSaveReqVO.value
            });

        // 响应处理
        if (res.code == '200') {
            proxy.$modal.msgSuccess(res.msg);
            handleSuccess();
        } else {
            ElMessage.warning(res.msg || "提交失败！");
        }
    } catch (err) {

    } finally {
        loadingInstance.value = false
    }
}

function code(obj) {
    dppQualityTaskObjSaveReqVO.value = [...obj];
}

async function getDppQualityTaskinfo() {
    loadingInstance.value = true
    const _id = id;
    try {
        const response = await getDppQualityTask(_id);
        const {
            dppQualityTaskObjSaveReqVO,//对象
            dppQualityTaskEvaluateRespVOS,// 规则

            ...obj
        } = response.data;
        originList.value = dppQualityTaskEvaluateRespVOS
        dppQualityTaskEvaluateSaveReqVO.value = dppQualityTaskEvaluateRespVOS;
        code(dppQualityTaskObjSaveReqVO)
        Object.assign(form.value, obj);
        form.value.contactId = Number(form.value.contactId)

    } catch (error) {
        console.error('获取质量任务失败:', error);
        ElMessage.warning('获取质量任务信息失败，请稍后重试');
    } finally {
        loadingInstance.value = false
    }
}

// 监听 id 变化
watch(
    () => route.query.id,
    (newId) => {
        id = newId;  // 如果 id 为空，使用默认值 1
        if (id) {
            getDppQualityTaskinfo();

        }

    },
    { immediate: true }  //
);

getDeptTree()
</script>
<style lang="scss" scoped>
.el-card ::v-deep .el-card__body {
    overflow-y: auto;
}

.pagecont-top {
    height: 85vh;
}

.steps-wrap {
    //width: 87.5vw;
    height: 80px;
    padding: 20px 20px;
    step-height: 40px;
    //box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 4px;
    border: 0px solid #ebeef5;
    background-color: #fff;
    margin: 15px 15px -34px 15px;
}

.steps-inner {
    display: flex;
    width: auto;
    //overflow: auto hidden;
    color: #303133;
    transition: 0.3s;

    // padding: 20px 0;
    //margin-left: 200px;
    &::-webkit-scrollbar {
        height: 5px;
    }

    .zl-step {
        list-style: none;
        font-size: 14px;
        height: 20px;
        display: flex;
        align-items: flex-end;
        margin: 20px auto;
        cursor: pointer;
        padding: 0;

        li {
            height: 40px;
            background: #d7d8da;
            color: #666;
            text-align: center;
            line-height: 40px;
            font-weight: 500;
            /*width: 20%;*/
            /*flex-basis: 100%;*/
            position: relative;
            padding-left: 10px;
        }

        .statusEnd {
            width: 33%;
            color: rgb(255, 255, 255);
            background: var(--el-color-primary) !important;
        }

        .cur {
            color: rgb(255, 255, 255);
            border-left-color: rgba(120, 140, 160, 0.2) !important;
        }

        .reult {
            color: rgb(102, 102, 102);
            background: rgb(215, 216, 218);
        }

        /*三角形绘制*/

        .jiao {
            width: 0;
            height: 0;
            border-top: 15px solid transparent;
            /*高度一半*/
            border-left: 20px solid #e8514a;
            /*调整宽度*/
            border-bottom: 15px solid transparent;
            /*高度一半*/
            color: rgb(102, 102, 102);
            border-left-color: rgb(215, 216, 218);
            position: absolute;
            right: -20px;
            /*跟宽度保持一致*/
            top: 0;
            z-index: 9999;
        }

        .interval {
            width: 0;
            height: 0;
            border-top: 26px solid transparent;
            /*高度一半*/
            border-left: 26px solid #fff;
            /*调整宽度*/
            border-bottom: 26px solid transparent;
            /*高度一半*/
            position: absolute;
            right: -26px;
            /*跟宽度保持一致*/
            top: -6px;
            z-index: 1;
        }

        .titleItem {
            width: 0;
            height: 0;
            border-top: 20px solid transparent;
            /*高度一半*/
            border-left: 20px solid #e8514a;
            /*调整宽度*/
            border-bottom: 20px solid transparent;
            /*高度一半*/
            color: rgb(255, 255, 255);
            border-left-color: var(--el-color-primary) !important;
            position: absolute;
            right: -20px;
            /*跟宽度保持一致*/
            top: 0;
            z-index: 2;
        }

        .jiaoActive {
            width: 0;
            height: 0;
            border-top: 20px solid transparent;
            /*高度一半*/
            border-left: 20px solid #e8514a;
            /*调整宽度*/
            border-bottom: 20px solid transparent;
            /*高度一半*/
            color: rgb(255, 255, 255);
            border-left-color: #d7d8da !important;

            position: absolute;
            right: -20px;
            /*跟宽度保持一致*/
            top: 0;
            z-index: 2;
        }
    }
}

.button-style {
    position: fixed;
    right: 50px;
    bottom: 40px;
    text-align: right;
    padding: 20px;
}

.main {
    flex: 1;
    margin: 15px;
    background-color: white;
    padding: 15px 25px 0;
}

.home {
    display: flex;
    flex-direction: column;
    height: 88vh;

    .clearfix {
        width: 100%;
        height: 36px;
        background-color: #f8f8f9;
        display: flex;
        align-items: center;
        padding-left: 10px;
        margin-bottom: 10px;
    }

    .clearfix span {
        display: flex;
        align-items: center;
    }

    .blue-bar {
        background-color: #2666FB; // 蓝条颜色
        width: 5px; // 宽度5px
        height: 20px; // 高度20px
        margin-right: 10px; // 图片与文字之间的间距
    }
}

.option-item {
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
}

.blue-text {
    color: var(--el-color-primary);
}

.iconimg {
    width: 15px;
    height: 15px;
    font-size: 15px;
    vertical-align: middle;
}
</style>
