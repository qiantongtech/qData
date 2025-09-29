<template>
    <div class="app-container" ref="app-container" v-loading="loadingInstance" style="background-color: #f0f2f5;">
        <div class="custom-card">
            <div class="steps-inner">
                <ul class="zl-step">
                    <li v-for="(item, index) in stepsList" :key="index" :class="{
                        statusEnd: activeReult === index,
                        prevStep: index < activeReult,
                        cur: index > activeReult
                    }">
                        <div class="step-circle" :class="{ active: activeReult === index, prev: index < activeReult }">
                            <span>{{ index + 1 }}</span>
                        </div>

                        <!-- 步骤名称 -->
                        <span class="step-name">{{ item.name }}</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="pagecont-top" v-loading="loading" v-show="showSearch" style="padding-bottom: 15px">
            <div class="infotop">
                <div class="main">
                    <el-form ref="formRef" :model="form" label-width="170px" v-show="activeReult == 0"
                        style="padding-right: 90px;" :disabled="route.query.info">
                        <div class="h2-titles">基础信息</div>
                        <el-row :gutter="20">
                            <el-col :span="11">
                                <el-form-item label="任务名称" prop="taskName"
                                    :rules="[{ required: true, message: '请输入任务名称', trigger: 'blur' }]">
                                    <el-input v-model="form.taskName" placeholder="请输入任务名称" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="2">
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="任务分类" prop="catCode"
                                    :rules="[{ required: true, message: '请选择任务分类', trigger: 'change' }]">
                                    <el-tree-select filterable v-model="form.catCode" :data="deptOptions"
                                        :props="{ value: 'code', label: 'name', children: 'children' }" value-key="ID"
                                        placeholder="请选择任务分类" check-strictly />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="11">
                                <el-form-item label="执行策略" prop="strategy"
                                    :rules="[{ required: true, message: '请选择执行策略', trigger: 'blur' }]">
                                    <el-select class="el-form-input-width" v-model="form.strategy" placeholder="请选择执行策略"
                                        style="width: 100%">
                                        <el-option v-for="dict in dpp_etl_task_execution_type" :key="dict.value"
                                            :label="dict.label" :value="dict.value"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="2">
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="责任人" prop="contactId">
                                    <el-tree-select filterable v-model="form.contactId" :data="userList" :props="{
                                        value: 'userId',
                                        label: 'nickName',
                                        children: 'children',
                                    }" value-key="ID" placeholder="请选择责任人" check-strictly
                                        @change="handleContactChange" />
                                </el-form-item>
                            </el-col>
                        </el-row>

                        <el-row :gutter="20">
                            <el-col :span="11">
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
                            <el-col :span="2">
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="任务状态" prop="status">
                                    <el-radio-group v-model="form.status" class="el-form-input-width">
                                        <el-radio v-for="dict in da_discovery_task_status" :key="dict.value"
                                            :label="dict.value">
                                            {{ dict.label }}
                                        </el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="24">
                                <el-form-item label="任务描述" prop="description">
                                    <el-input v-model="form.description" type="textarea" placeholder="请输入任务描述" />
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <!--                        <el-divider content-position="center">-->
                        <!--                            <span class="blue-text">属性信息</span>-->
                        <!--                        </el-divider>-->
                        <!-- <div class="clearfix header-text">
                            <div class="header-left">
                                <div class="blue-bar"></div>

                            </div>
                        </div> -->
                        <div class="h2-titles">属性信息</div>
                        <el-row :gutter="20">
                            <el-col :span="11">
                                <el-form-item label="任务优先级" prop="priority"
                                    :rules="[{ required: true, message: '请选择任务优先级', trigger: 'change' }]">
                                    <el-select v-model="form.priority" placeholder="请选择任务优先级">
                                        <el-option v-for="dict in priorityOptions" :key="dict.value" :label="dict.label"
                                            :value="dict.value" />
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="2">
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="Worker分组" prop="workerGroup"
                                    :rules="[{ required: true, message: '请输入Worker分组', trigger: 'blur' }]">
                                    <el-input v-model="form.workerGroup" placeholder="请输入Worker分组" />
                                </el-form-item>
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="失败重试次数" prop="retryTimes">
                                    <el-input type="number" v-model="form.retryTimes" placeholder="请输入失败重试次数">
                                        <template #append>次</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="2">
                            </el-col>
                            <el-col :span="11">
                                <el-form-item label="延迟执行时间" prop="delayTime">
                                    <el-input type="number" v-model="form.delayTime" placeholder="请输入延迟执行时间">
                                        <template #append>分</template>
                                    </el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-form-item label="备注" prop="remark">
                            <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
                        </el-form-item>
                    </el-form>

                    <div v-loading="loadingList" v-show="activeReult == 1">
                        <div class="h2-titles">稽查对象信息</div>
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
                            <el-table-column label="稽查对象名称" align="center" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.name }}
                                </template>
                            </el-table-column>
                            <el-table-column label="数据连接名称" align="center" prop="type"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    <img :src="getDatasourceIcon(scope.row.datasourceType)" class="iconimg " />
                                    {{ scope.row.datasourceType }}
                                </template>
                            </el-table-column>
                            <el-table-column label="模式名称" align="center" prop="type"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    <template v-if="scope.row.datasourceConfig">
                                        <template v-if="JSON.parse(scope.row.datasourceConfig).dbname">
                                            {{ JSON.parse(scope.row.datasourceConfig).dbname }}
                                        </template>
                                    </template>
                                </template>
                            </el-table-column>

                            <el-table-column label="表名称" align="center" prop="tableName"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.tableName }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                                fixed="right" width="200" v-if="!route.query.info">
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
                        <div class="clearfix header-text" style="margin-top:10px;">
                            <div class="header-left">
                                <div class="blue-bar"></div>
                                稽查规则信息
                            </div>
                        </div>
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
                        <el-table stripe height="450px" :data="dppQualityTaskEvaluateSaveReqVO">
                            <el-table-column label="序号" type="index" align="center" />
                            <el-table-column label="评测名称" align="center" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.name || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="评测字段" align="center" prop="evaColumn"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.evaColumn || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="稽查规则" align="center" prop="ruleName"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.ruleName || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="规则描述" align="center" prop="ruleDescription"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ scope.row.ruleDescription || '-' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="质量维度" align="center" prop="dimensionType" width="120"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
                                </template>
                            </el-table-column>

                            <el-table-column label="状态" align="center" prop="status" width="100">
                                <template #default="scope">
                                    {{ scope.row.status == '1' ? '上线' : '下线' }}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                                fixed="right" width="200" v-if="!route.query.info">
                                <template #default="scope">
                                    <!--                                    <el-button link type="primary" icon="view"-->
                                    <!--                                        @click="openRuleDialog(scope.row, scope.$index + 1, true)">查看</el-button>-->
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
                    <el-button v-if="activeReult != 0" @click="handleLastStep">上一步</el-button>
                    <el-button type="primary" v-if="activeReult === 2 && !route.query.info" @click="submitForm"
                        :loading="loadingOptions.loading">
                        确定并退出
                    </el-button>
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
import InspectionTargetDialog from '../components/inspectionTarget.vue';
import RuleSelectorDialog from '../components/ruleBase.vue';
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttQualityCat } from "@/api/att/cat/qualityCat/qualityCat.js";
import {
    addDppQualityTask,
    updateDppQualityTask,
    getDppQualityTask,
} from "@/api/da/quality/qualityTask";
import Crontab from "@/components/Crontab/index.vue";
import {
    getColumnByAssetId,
} from '@/api/dpp/task/index.js';
import { treeData } from "../data.js"
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
async function handleNextStep() {
    try {
        await formRef.value?.validate();
    } catch (err) {
        console.warn('表单校验未通过：', err);
        ElMessage.warning("校验未通过，请检查必填项");
        loadingInstance.value = false
        return;
    }
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
    router.push("/da/quality/qualityTask");
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
    height: 74vh;
    position: relative;
    padding-bottom: 20px;
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

.custom-card {
    width: 100%;
    height: 100px;
    padding: 34px 177px 26px 189px;
    background: #fff;
    box-sizing: border-box;
    margin-bottom: 15px;

    .steps-inner {
        padding: 0 10px;
        padding-left: 20px;
        display: flex;
        width: auto;
        color: #303133;
        transition: 0.3s;
        transform: translateZ(0);

        &::-webkit-scrollbar {
            height: 5px;
        }

        .zl-step {
            list-style: none;
            width: 100%;
            height: 20px;
            padding: 0;
            margin: 20px auto;
            cursor: pointer;
            display: flex;
            align-items: flex-end;

            li {
                position: relative;
                flex: 1;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                background: #d7d8da;
                color: #666;
                font-weight: 500;
                transition: background 0.3s;

                &:first-child {
                    z-index: 2;
                    clip-path: polygon(0 0, calc(100% - 20px) 0, 100% 50%, calc(100% - 20px) 100%, 0 100%);
                }

                &:not(:first-child):not(:last-child) {
                    margin-left: -10px;
                    clip-path: polygon(0 0, calc(100% - 20px) 0, 100% 50%, calc(100% - 20px) 100%, 0 100%);
                    z-index: 1;

                    &::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 0;
                        width: 20px;
                        height: 100%;
                        background: #fff;
                        clip-path: polygon(0 0, 100% 50%, 0 100%);
                        z-index: 2;
                    }
                }

                &:last-child {
                    margin-left: -10px;
                    z-index: 0;
                    clip-path: polygon(0 0, 100% 0, 100% 100%, 0 100%);

                    &::before {
                        content: '';
                        position: absolute;
                        left: 0;
                        top: 0;
                        width: 20px;
                        height: 100%;
                        background: #fff;
                        clip-path: polygon(0 0, 100% 50%, 0 100%);
                        z-index: 2;
                    }
                }

                &.statusEnd {
                    background: linear-gradient(270deg, #e9effe 0%, #5589FA 100%);
                    color: #2666FB !important;
                }

                &.prevStep {
                    background: #E9EFFE !important;
                    font-weight: normal;
                    font-size: 16px !important;
                    color: #2666FB !important;
                }

                &.cur {
                    background: #F1F1F5;
                    color: #404040;
                    font-weight: 500;
                }
            }
        }

        .step-circle {
            width: 26px;
            height: 26px;
            border-radius: 50%;
            background: #f1f1f5;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 18px;
            font-weight: bold;
            margin-right: 11px;
            border: 1px solid #b2b2b2;
            flex-shrink: 0;
            transition: all 0.3s;

            &.active {
                background: #2666fb;
                color: #fff;
                border: 1px solid #fff;
            }

            &.prev {
                background: #f1f1f5 !important;
                border: 1px solid #2666fb !important;
                color: #2666fb !important;
            }
        }

        .step-name {
            font-family: PingFang SC, PingFang SC;
            font-weight: 500;
            font-size: 16px;
        }
    }
}

.button-style {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 0px 35px 25px 0px;
    background: #fff;
    text-align: right;
    z-index: 10;
}

.main {
    flex: 1;
    // margin: 15px;
    background-color: white;
    padding: 0px 25px 0;
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

.blue-bar {
    background-color: #2666fb;
    width: 5px;
    height: 20px;
    margin-right: 10px;
    border-radius: 2px;
}

.header-text {
    margin: 20px 0
}

.header-left {
    display: flex;
    align-items: center;
    font-size: 16px;
    line-height: 24px;
    font-style: normal;
}

.iconimg {
    width: 15px;
    height: 15px;
    font-size: 15px;
    vertical-align: middle;
}
</style>
