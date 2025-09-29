<template>
    <!-- 资产质量 tab-->
    <div v-loading="loading">
        <div class="quality-header" v-if="taskData.id">
            <div class="quality-info">
                <div class="quality-item">
                    <span class="quality-label">质量评估任务：</span>
                    <span class="quality-value">{{ taskData.taskName }}</span>
                </div>
                <div class="quality-item">
                    <span class="quality-label">上一次评估时间：</span>
                    <span class="quality-value"> {{ parseTime(taskData.lastExecuteTime, '{y}-{m}-{d} {h}:{i}') || '-'
                    }}</span>
                </div>
            </div>
            <div class="quality-actions">
                <el-button type="primary" class="btn-margin-right" icon="Edit" @click="openQualityDialog(taskData)"
                    :disabled="taskData.status != '1'">
                    修改资产质量任务
                </el-button>
                <el-button type="primary" :disabled="taskData.status != '1'" class="btn-margin-right" icon="Setting"
                    @click="handleJobLog(taskData)">
                    调度配置
                </el-button>
                <el-button type="primary" class="btn-margin-right" icon="VideoPlay" @click="handleExecuteOnce(taskData)"
                    :disabled="taskData.status == '1'">
                    执行一次
                </el-button>
                <el-button :type="taskData.status == '0' ? 'danger' : 'primary'"
                    :icon="taskData.status == '0' ? 'CircleClose' : 'VideoPlay'"
                    @click="handleStatusChange(taskData.status == '1' ? 0 : 1, taskData)">
                    {{ taskData.status == '0' ? '停止任务' : '启动任务' }}
                </el-button>
            </div>
        </div>
        <div class="app-container  stagingIndex" v-loading="loading" v-if="taskData.id">
            <el-row gutter="20" class="top-section">
                <!-- 左侧评分 -->
                <el-col :xs="24" :sm="24" :md="12" class="stats-panel">
                    <div class="module-8 border-item">
                        <div class="border-item-head">
                            <span class="head-title">数据质量维度统计 </span>
                        </div>
                        <div class="border-item-body">
                            <div class="overall-score">
                                <span>整体数据质量评分：</span>
                                <span class="score" :class="getScoreClass(taskData?.score)">
                                    {{ taskData?.score || '0' }}
                                </span>
                            </div>
                            <el-table :data="summaryList" border size="small" style="margin-top: 12px" height="246">
                                <el-table-column prop="dimensionType" label="质量维度" align="left">
                                    <template #default="scope">
                                        <dict-tag :options="att_rule_audit_q_dimension"
                                            :value="scope.row.dimensionType" />
                                    </template>

                                </el-table-column>
                                <el-table-column prop="succesTotal" label="规则数" align="left">
                                    <template #default="scope">{{ scope.row.succesTotal || '-' }}</template>
                                </el-table-column>
                                <el-table-column prop="proportion" label="问题数占比" align="left">
                                    <template #default="scope">
                                        {{ scope.row.proportion != null ? scope.row.proportion + '%' : '-' }}
                                    </template>

                                </el-table-column>
                                <el-table-column label="趋势" align="left">
                                    <template #default="{ row }">
                                        <template v-if="row.trendType == '-1'">
                                            -
                                        </template>
                                        <template v-else-if="row.trendType == '1'">
                                            <el-icon color="green">
                                                <ArrowUp />
                                            </el-icon>
                                        </template>
                                        <template v-else>
                                            <el-icon color="red">
                                                <ArrowDown />
                                            </el-icon>
                                        </template>
                                    </template>
                                </el-table-column>

                            </el-table>
                        </div>
                    </div>
                </el-col>

                <!-- 右侧折线图 -->
                <el-col :xs="24" :sm="24" :md="12" class="trend-chart-panel">
                    <div class="module-8 border-item">
                        <div class="border-item-head">
                            <span class="head-title">治理数据量变化趋势</span>
                            <el-select v-model="selectedRange" size="small" placeholder="选择时间范围" style="width: 120px"
                                @change="onRangeChange">
                                <el-option v-for="item in rangeOptions" :key="item.value" :label="item.label"
                                    :value="item.value" />
                            </el-select>
                        </div>
                        <div class="border-item-body">
                            <div ref="chartRef" class="echart-container"></div>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 规则列表 -->
            <el-row>
                <div class="module-8 border-item" style="width: 100%">
                    <div class="border-item-head">
                        <span class="head-title">规则列表</span>
                    </div>
                    <div class="border-item-body" style="height: 320px;">
                        <el-table stripe height="300px" v-loading="loading" :data="ruleList" lazy
                            :show-overflow-tooltip="{ effect: 'light' }">
                            <el-table-column v-if="getColumnVisibility(8)" label="评测名称" align="left"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{ getEvaluateName(scope.row) }}
                                </template>
                            </el-table-column>

                            <el-table-column v-if="getColumnVisibility(1)" label="数据库名称" align="left" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">{{ scope.row.datasourceName || '-' }}</template>
                            </el-table-column>
                            <el-table-column v-if="getColumnVisibility(2)" label="字段名/中文名" align="left" prop="name"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope"> {{ scope.row.columnLabel || '-' }}</template>
                            </el-table-column>
                            <el-table-column v-if="getColumnVisibility(3)" label="质量维度" align="left"
                                prop="dimensionType" :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />

                                </template>
                            </el-table-column>
                            <el-table-column v-if="getColumnVisibility(5)" label="稽查名称" align="left" prop="ruleName"
                                :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">{{ scope.row.ruleName || '-' }}</template>
                            </el-table-column>

                            <el-table-column v-if="getColumnVisibility(7)" label="问题数据量占比" align="left"
                                prop="proportion" :show-overflow-tooltip="{ effect: 'light' }">
                                <template #default="scope">
                                    {{
                                        (scope.row.problemTotal != -1 && scope.row.problemTotal != null)
                                            ? `${scope.row.problemTotal} /条 ${scope.row.proportion ?? '-'}%`
                                            : '-'
                                    }}
                                </template>

                            </el-table-column>
                            <el-table-column label="操作" fixed="right" width="140" align="left">
                                <template #default="scope">
                                    <el-button link type="primary" icon="View"
                                        @click="openDialog(scope.row)">查看问题数据</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-row>


        </div>
        <el-empty description="暂无资产质量任务" v-else>
            <el-button type="primary" @click="openQualityDialog(undefined)">
                <i class="iconfont-mini icon-xinzeng mr5"></i>新增资产质量任务
            </el-button>
        </el-empty>
        <!-- 问题数据弹窗 -->
        <ProblemDialog ref="problemDialogRef" />
        <qualityTaskDialog ref="qualityDialog" @submit-success="fetchData" />
        <DataViewDialog :visible="DataView" :taskType="3" @update:visible="DataView = $event" :data="form"
            title="执行记录" />
        <el-dialog title="调度周期" v-model="openCron" :append-to="$refs['app-container']" destroy-on-close
            :appendTo="'#app'">
            <crontab ref="crontabRef" @hide="openCron = false" @fill="crontabFill" :expression="expression">
            </crontab>
            <!--      <crontab-->
            <!--        ref="crontabRef"-->
            <!--        @hide="openCron = false"-->
            <!--        @fill="crontabFill"-->
            <!--        :expression="expression"-->
            <!--        :Crontab="false"-->
            <!--      >-->
            <!--      </crontab>-->
        </el-dialog>
    </div>

    <!-- 引入弹窗组件，绑定visible -->

</template>

<script setup>
import * as echarts from 'echarts';
import { useRoute } from 'vue-router';
import { ref, onMounted, onBeforeUnmount } from 'vue';
import moment from 'moment';
const { proxy } = getCurrentInstance();

import qualityTaskDialog from '../components/qualityTaskAdd.vue';
import ProblemDialog from '@/views/da/quality/qualityTaskLog/components/problemData.vue';

import {
    statisticsEvaluateOne,
    statisticsEvaluateTow,
    statisticsEvaluateTable
} from "@/api/da/quality/qualityTaskLog";
import {
    listDppQualityTask,
    delDppQualityTask,
    updateDppQualityTaskStatus,
    startDppQualityTask,
    updateDaDiscoveryTaskCronExpression,
    getQualityTaskAsset
} from "@/api/da/quality/qualityTask";
const route = useRoute();
import Crontab from "@/components/Crontab/index.vue";

const id = ref(route.query.id)

const { att_rule_audit_q_dimension, } = proxy.useDict(

    'att_rule_audit_q_dimension'
);
const props = defineProps({
    form1: {
        type: Object,
        default: {}
    }
});
const taskInfo = {
    name: '用户注册信息校验',
    datasource: '用户库',
    owner: '张三'
}
const qualityDialog = ref(null);

function openQualityDialog(row) {
    qualityDialog.value.open(
        JSON.parse(JSON.stringify(props.form1)),
        JSON.parse(JSON.stringify(taskData.value))
    );
    console.log("🚀 ~ openQualityDialog ~ taskData.value:", taskData.value)

}

let expression = ref("");
let openCron = ref(false)
/** 运行实例按钮操作 */
function handleJobLog(data) {

    openCron.value = true;
    expression.value = data.cycle || "";
}
function crontabFill(value) {
    updateDaDiscoveryTaskCronExpression({
        cycle: value,
        status: '1',
        id: Number(taskData.value?.id),
    }).then((response) => {
        proxy.$modal.msgSuccess("操作成功");
        fetchData()
    });
}
/** 改变启用状态值 */
function handleStatusChange(status, row, e) {
    const text = row?.status == "1" ? "上线" : '下线';
    proxy.$modal
        .confirm('确认要"' + text + '","' + row.taskName + '"质量任务吗？')
        .then(function () {
            loading.value = true;
            updateDppQualityTaskStatus({
                id: row.id,
                status: Number(status)
            })
                .then((response) => {
                    fetchData()
                    proxy.$modal.msgSuccess("操作成功");
                })
                .finally(() => {
                    loading.value = false;
                });
        })

}
const getScoreClass = (score) => {
    if (score == null || score === '-') return 'score-null';
    if (score >= 85) return 'score-high';
    if (score >= 60) return 'score-medium';
    return 'score-low';
};
const chartRef = ref(null);
let chartInstance = null;
let problemDialogRef = ref();
function getEvaluateName(row) {
    if (!row.rule) return '-';
    try {
        return JSON.parse(row.rule)?.evaluateName || '-';
    } catch {
        return '-';
    }
}
const openDialog = (row) => {
    problemDialogRef.value?.open(row);
};

const selectedRange = ref('7');
const rangeOptions = [
    { label: '近7天', value: '7' },
    { label: '近15天', value: '15' },
    { label: '近30天', value: '30' }
];

const ruleList = ref([]);
const overallScore = ref();
const summaryList = ref([]);
const loading = ref(false);

const columns = ref([

    { key: 8, label: "规则名称", visible: true },
    { key: 1, label: "数据库名称", visible: true },
    { key: 2, label: "字段名/中文名", visible: true },
    { key: 3, label: "质量维度", visible: true },
    { key: 5, label: "规则名称", visible: true },
    { key: 7, label: "问题数据量占比", visible: true },
]);
function getLabelsByColumnName(row, columnName) {
    if (!row.rule || !columnName) return '-';
    let evaColumns = [];
    try {
        const ruleObj = typeof row.rule === 'string' ? JSON.parse(row.rule) : row.rule;
        evaColumns = Array.isArray(ruleObj.evaColumns)
            ? ruleObj.evaColumns
            : Object.values(ruleObj.evaColumns || {});
    } catch (err) {
        console.warn('规则字段解析失败', err);
        return '-';
    }

    if (!Array.isArray(evaColumns)) return '-';

    const names = columnName.split(',').map(n => n.trim());
    const labels = names.map(name => {
        const match = evaColumns.find(col => col.name === name);
        return match?.label || name;
    });

    return labels.join(' , ');
}


const getColumnVisibility = (key) => {
    const column = columns.value.find((col) => col.key === key);
    return column ? column.visible : true;
};

const loadChartWithData = (data = []) => {
    let { title = [], value = [] } = data;
    if (!chartInstance && chartRef.value) {
        chartInstance = echarts.init(chartRef.value);
    }

    const range = Number(selectedRange.value);
    const dateList = Array.from({ length: range }, (_, i) =>
        moment().subtract(range - i - 1, 'days').format('MM-DD')
    );

    const maxValue = Math.max(...value, 0);
    const minYMax = 30;
    const yMax = Math.max(minYMax, Math.ceil(maxValue / 5) * 5);

    const option = {
        legend: {
            data: ['质量趋势'],
            left: 'center',
        },
        tooltip: { trigger: 'axis' },
        xAxis: {
            type: 'category',
            data: title,
            axisTick: { show: false },
            axisLine: {
                lineStyle: { color: 'rgba(0,0,0,0.15)' }
            },
            axisLabel: {
                margin: 14,
                fontSize: 12,
                color: 'rgba(0,0,0,0.65)',
                fontFamily: 'PingFangSC, PingFang SC',
            }
        },
        yAxis: {
            type: 'value',
            min: 0,
            max: yMax,
            interval: 5,
            nameTextStyle: {
                color: 'rgba(0,0,0,0.85)',
                fontSize: 14,
                padding: [0, 0, 10, -18],
                fontFamily: 'PingFangSC, PingFang SC',
            },
            axisLine: {
                lineStyle: { color: 'rgba(0,0,0,0.15)' }
            },
            axisLabel: {
                fontSize: 12,
                color: 'rgba(0,0,0,0.65)',
                fontFamily: 'PingFangSC, PingFang SC',
            }
        },
        grid: { left: '3%', right: '4%', bottom: '0%', containLabel: true },
        series: [{
            name: '质量趋势',
            type: 'line',
            data: value,
            symbolSize: 8,
            itemStyle: {
                color: '#427afd',
                borderColor: '#427afd',
                borderWidth: 1
            },
            lineStyle: {
                color: '#5285fd',
                width: 2
            },
            areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: 'rgba(204, 220, 254, 1)' },
                    { offset: 1, color: 'rgba(204, 220, 254, 0)' }
                ])
            }
        }]
    };

    chartInstance.setOption(option);
};



// 评分和质量维度汇总
const loadScoreAndSummary = async () => {
    try {
        const res = await statisticsEvaluateOne(id.value);
        const result = res?.data || [];
        // 构造一个维度映射，用于快速查找
        const resultMap = result.reduce((map, item) => {
            map[item.dimensionType] = item;
            return map;
        }, {});
        summaryList.value = att_rule_audit_q_dimension.value.map(dim => {
            return resultMap[dim.value] || {
                dimensionType: dim.value,
                succesTotal: 0,
                proportion: 0,
                trendType: '-1',
            };
        });
    } catch (err) {
        console.warn('评分/维度汇总失败', err);
    }
};
// 规则列表
const loadRuleTable = async () => {
    try {
        const res = await statisticsEvaluateTable(id.value);
        if (res.data && Array.isArray(res.data)) {
            ruleList.value = res.data.map(item => {
                return {
                    ...item,
                    columnLabel: getLabelsByColumnName(item, item.columnName)
                };
            });
        } else {
            ruleList.value = [];
        }
    } catch (err) {
        console.warn('规则列表失败', err);
    } finally {
    }
};


// 折线图数据
const loadTrendChart = async () => {
    try {
        const range = Number(selectedRange.value);
        const today = moment().format('YYYY-MM-DD');
        const oldDate = moment().subtract(Number(selectedRange.value), 'days').format('YYYY-MM-DD');
        const type = selectedRange.value === '7' ? 0 : selectedRange.value === '15' ? 1 : 2;
        const res = await statisticsEvaluateTow({ id: id.value, deDate: today, oldDate, type });
        console.log("🚀 ~ loadTrendChart ~ res:", res)

        loadChartWithData(res?.data || []);
    } catch (err) {
        console.warn('折线图数据失败', err);
    }
};

function fetchTrendData(range) {
    return new Promise((resolve) => {
        setTimeout(() => {
            const title = Array.from({ length: range }, (_, i) =>
                moment().subtract(range - i - 1, 'days').format('MM-DD')
            );
            const value = [
                0,
                0,
                0,
                0,
                0,
                0,
                0
            ];
            resolve({ title, value });
        }, 500);
    });
}

let taskData = ref({})
const fetchData = async () => {
    loading.value = true;
    console.log("🚀 ~ fetchData ~ id.value:", id.value)

    const res = await getQualityTaskAsset({ assetId: props.form1?.id });
    if (res.data) {
        taskData.value = res.data
        console.log("🚀 ~ fetchData ~ res.data:", taskData.value)
        console.log("🚀 ~ fetchData ~  taskData.value:", taskData.value.status)
        summaryList.value = att_rule_audit_q_dimension.value.map(dim => ({
            dimensionType: dim.value,
            succesTotal: 0,
            proportion: 0,
            trendType: '-1',
        }));
        console.log("✅ 接口返回的任务状态是：", res.data.status);
        console.log("✅ 设置后的 taskData.status 是：", taskData.value.status);
        const data = await fetchTrendData(Number(selectedRange.value));
        loadChartWithData(data);
    }
    if (res.data?.logId) {


        id.value = res.data.logId
        await Promise.all([
            loadScoreAndSummary(id),
            loadRuleTable(id),
            loadTrendChart(id)
        ]);
    }

    loading.value = false;
};
const handleExecuteOnce = async (row) => {
    if (!row?.id) {
        proxy.$modal.msgError("无效的任务 ID");
        return;
    }
    loading.value = true;
    try {
        const res = await startDppQualityTask(row.id);

        if (Number(res?.code) === 200) {

        } else {
        }
    } finally {
        loading.value = false;
    }
};

const onRangeChange = () => {
    loadTrendChart(id)
};

const handleResize = () => {
    chartInstance?.resize();
};


onMounted(async () => {
    overallScore.value = route.query.score

    fetchData()
    window.addEventListener('resize', handleResize);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', handleResize);
});
</script>

<style lang="scss" scoped>
.top-section {
    margin-bottom: 20px;
}

.echart-container {
    height: 100%;
    width: 100%;
}

.border-item {
    width: 100%;
    background: #fff;
    border-radius: 2px;

    .border-item-head {
        height: 50px;
        padding: 0 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #e8e8e8;

        .head-title {
            font-size: 16px;
            //color: var(--el-color-primary);
            font-weight: 500;
            display: flex;
            align-items: center;

            &::before {
                content: "";
                display: inline-block;
                width: 3px;
                height: 20px;
                background: var(--el-color-primary);
                margin-right: 10px;
                border-radius: 2px;
            }
        }
    }

    .border-item-body {
        height: 360px;
        padding: 10px 20px;
        background-color: #fff;
    }
}

.overall-score {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    margin-bottom: 10px;

    .score {
        font-size: 24px;
        font-weight: 700;
        margin-left: 8px;
    }
}

.score-high {
    color: #16a34a;
    ;
}

.score-medium {
    color: #faad14;
}

.score-low {
    color: #f5222d;
}

.score-null {
    color: #999;
}

.app-container {
    margin: 0px !important;
    padding: 10px;
    background-color: #f0f2f5
}

.quality-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 24px;
    border-bottom: 1px solid #ebeef5;
    flex-wrap: wrap;

    .quality-info {
        display: flex;
        flex-wrap: wrap;

        .quality-item {
            display: flex;
            align-items: center;
            margin-right: 24px;
            margin-bottom: 6px;

            .quality-label {
                font-weight: 600;
                font-size: 16px;
                color: #000;
            }

            .quality-value {
                font-weight: 500;
                font-size: 14px;
                margin-left: 8px;
                color: #333;
            }
        }
    }

    .quality-actions {
        display: flex;
        flex-wrap: wrap;
        align-items: center;

        .btn-margin-right {
            margin-right: 8px;
            margin-bottom: 6px;
        }
    }
}


.info-left {
    display: flex;
    flex-wrap: wrap;
    gap: 24px;
}

.info-item {
    font-size: 14px;
}

.label {
    color: #909399;
    font-weight: 500;
    margin-right: 4px;
}

.btn-right {
    display: flex;
    gap: 10px;
}
</style>
