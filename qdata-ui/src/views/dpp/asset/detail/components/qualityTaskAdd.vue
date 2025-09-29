<template>
    <!-- 资产质量的弹窗 -->
    <el-dialog v-model="visible" :title="title" class="medium-dialog" @close="handleClose" destroy-on-close>
        <div ref="app-container" v-loading="loadingInstance">
            <!--            <div class="pagecont-top" v-loading="loading" v-show="showSearch" style="padding-bottom: 15px">-->
            <!--                <div class="infotop">-->
            <!--                    <div class="main">-->
            <div v-loading="loadingList">
                <!-- <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                                label-width="75px" @submit.prevent>
                                <el-form-item label="规则名称" prop="name">
                                    <el-input class="el-form-input-width" v-model="queryParams.name"
                                        placeholder="请输入规则名称" clearable @keyup.enter="handleQuery" />
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
                            </el-form> -->
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

                <el-table stripe height="550px" :data="dppQualityTaskEvaluateSaveReqVO">
                    <el-table-column label="编号" type="index" align="left">
                        <template #default="scope">
                            {{ scope.row.id || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="评测名称" align="left" prop="name" :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.name || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="评测字段" align="left" prop="evaColumn"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.evaColumn || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="稽查规则" align="left" width="200" prop="ruleName"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.ruleName || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="规则描述" align="left" prop="ruleDescription"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            {{ scope.row.ruleDescription || '-' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="质量维度" align="left" prop="dimensionType" width="100"
                        :show-overflow-tooltip="{ effect: 'light' }">
                        <template #default="scope">
                            <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
                        </template>
                    </el-table-column>

                    <el-table-column label="状态" align="left" prop="status" width="80">
                        <template #default="scope">
                            {{ scope.row.status == '1' ? '上线' : '下线' }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                        width="180" v-if="!route.query.info">
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
                <!--                        </div>-->
                <!--                    </div>-->
                <!--                </div>-->
            </div>
        </div>
        <RuleSelectorDialog ref="ruleSelectorDialog" @confirm="RuleSelectorconfirm" v-if="visible"
            :dppQualityTaskObjSaveReqVO="dppQualityTaskObjSaveReqVO" :type="2" :tableName="formData?.tableName" />
        <template #footer>
            <el-button @click="handleClose">取消</el-button>
            <el-button type="primary" @click="submitForm" :loading="loadingOptions.loading">
                确定
            </el-button>
        </template>
    </el-dialog>
</template>

<script setup name="qualityTask">
import { ref, reactive, toRefs, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import RuleSelectorDialog from '@/views/da/quality/qualityTask/components/ruleBase.vue';
import { deptUserTree } from "@/api/system/system/user.js";
import { listAttQualityCat } from "@/api/att/cat/qualityCat/qualityCat.js";
import {
    addDppQualityTask,
    updateDppQualityTask
} from "@/api/da/quality/qualityTask";
const { proxy } = getCurrentInstance();
const route = useRoute();
const loading = ref(false);
const showSearch = ref(true);
import moment from 'moment';
import {
    getColumnByAssetId,
} from '@/api/dpp/task/index.js';
let id = route.query.id || '';
const router = useRouter();
const { att_rule_audit_q_dimension, da_discovery_task_status, dpp_etl_task_execution_type } = proxy.useDict(

    'att_rule_audit_q_dimension', 'da_discovery_task_status', 'dpp_etl_task_execution_type'
);
let dppQualityTaskObjSaveReqVO = ref([

])
function convertAssetToTask(asset) {
    return [{
        datasourceId: asset.datasourceId,
        name: asset.tableComment,
        datasourceType: asset.datasourceType,
        tableName: asset.tableName
    }];
}

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
function getIconByValue(value) {
    const node = treeData.find(item => item.value?.toLowerCase() === value?.toLowerCase())
    return node ? node.icon : ''
}
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
        assetFlag: '1',
        assetId: '',
        taskName: '',
        catCode: "-1",
        status: '1',
        contactId: '',
        priority: 'medium',
        workerGroup: 'default',
        retryCount: 0,
        retryInterval: 0,
        delayMinutes: 0,
        description: '',
        retryTimes: "",
        delayTime: "",
        cycle: "0 0 0 * * ?",
        strategy: "PARALLEL"

    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        qualityDim: '',
        publishStatus: ''
    },
    activeReult: 0,
    active: 0,
    loadingOptions: { loading: false }
});

const { form, stepsList, activeReult, loadingOptions, queryParams, active } = toRefs(data);
const formRef = ref();
let formData = ref({})
let visible = ref(false)
let title = ref("")
function open(data, row) {
    handleClose()
    form.value.assetId = data.id
    form.value.taskName = `${data.name}_${data.tableName}_${moment().format('YYYYMMDDHHmmss')}`;
    getDeptTree()
    title.value = row?.id ? "修改资产质量任务" : "新增资产质量任务"
    console.log("🚀 ~ open ~ row?.nam:", row)
    formData.value = data
    visible.value = true
    if (row?.id) {
        getDppQualityTaskinfo(row)
    } else {
        dppQualityTaskObjSaveReqVO.value = JSON.parse(JSON.stringify(convertAssetToTask(data)));
    }
}
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
                        ElMessage.info(`表 ${item.tableName} 没有新规则追加`);
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

const inspectionTargetDialog = ref();
function handleRuleDelete(index) {
    const realIndex = Number(index) - 1;
    originList.value.splice(realIndex, 1);
    dppQualityTaskEvaluateSaveReqVO.value = originList.value
}

let ruleSelectorDialog = ref()
const openRuleSelector = (row) => {
    ruleSelectorDialog.value.openDialog(undefined, undefined, undefined);
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
        proxy.$message.warning("评测名称不能重复！");
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

const emit = defineEmits(['submit-success'])
async function submitForm() {
    loadingInstance.value = true
    try {
        await formRef.value?.validate();
    } catch (err) {
        ElMessage.warning("表单校验未通过，请检查必填项！");
        loadingInstance.value = false
        return;
    }
    try {
        dppQualityTaskObjSaveReqVO.value = dppQualityTaskObjSaveReqVO.value.map(item => ({
            ...item,
            name: item.name && item.name.trim() ? item.name : `资产质量${moment().format('YYYYMMDDHHmmss')}`
        }));


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
            handleClose()
            emit('submit-success')
        } else {
            ElMessage.error(res.msg || "提交失败！");
        }
    } catch (err) {

    } finally {
        loadingInstance.value = false
    }
}

function code(obj) {
    dppQualityTaskObjSaveReqVO.value = [...obj];
    console.log("🚀 ~ code ~ dppQualityTaskObjSaveReqVO.value:", dppQualityTaskObjSaveReqVO.value)
}


function getDppQualityTaskinfo(data) {
    loadingInstance.value = true;

    try {
        const {
            dppQualityTaskObjSaveReqVO,//对象
            dppQualityTaskEvaluateRespVOS,// 规则
            ...obj
        } = data;
        originList.value = dppQualityTaskEvaluateRespVOS
        dppQualityTaskEvaluateSaveReqVO.value = dppQualityTaskEvaluateRespVOS;
        code(dppQualityTaskObjSaveReqVO)
        Object.assign(form.value, obj);
        form.value.contactId = Number(form.value.contactId)
    } finally {
        loadingInstance.value = false;
    }
}
const handleClose = () => {
    formData.value == {}
    dppQualityTaskEvaluateSaveReqVO.value = []
    dppQualityTaskObjSaveReqVO.value = []
    originList.value = []
    visible.value = false
}

defineExpose({
    open
});
</script>
<style scoped lang="scss"></style>
