<template>
    <div class="app-container stagingIndex" v-loading="loading">
        <!-- 顶部区域：评分 + 折线图 -->
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
                            <span class="score" :class="getScoreClass(overallScore)">
                                {{ overallScore || '-' }}
                            </span>
                        </div>
                        <el-table :data="summaryList" border size="small" style="margin-top: 12px" height="246">
                            <el-table-column prop="dimensionType" label="质量维度" align="center">
                                <template #default="scope">
                                    <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
                                </template>

                            </el-table-column>
                            <el-table-column prop="succesTotal" label="规则数" align="center">
                                <template #default="scope">{{ scope.row.succesTotal || '-' }}</template>
                            </el-table-column>
                            <el-table-column prop="proportion" label="问题数占比" align="center">
                                <template #default="scope">
                                    {{ scope.row.proportion != null ? scope.row.proportion + '%' : '-' }}
                                </template>

                            </el-table-column>
                            <el-table-column label="趋势" align="center">
                                <template #default="{ row }">
                                    <template v-if="row.trendType == '-3'">
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
                    <el-table stripe height="300px" v-loading="loading" :data="ruleList" lazy :show-overflow-tooltip="{effect: 'light'}">
                        <el-table-column v-if="getColumnVisibility(8)" label="评测名称" align="center"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                {{ getEvaluateName(scope.row) }}
                            </template>
                        </el-table-column>

                        <el-table-column v-if="getColumnVisibility(1)" label="数据库名称" align="center" prop="name"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">{{ scope.row.datasourceName || '-' }}</template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(2)" label="字段名/中文名" align="center" prop="name"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope"> {{ scope.row.columnLabel || '-' }}</template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(3)" label="质量维度" align="center" prop="dimensionType"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />

                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(5)" label="稽查名称" align="center" prop="ruleName"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">{{ scope.row.ruleName || '-' }}</template>
                        </el-table-column>

                        <el-table-column v-if="getColumnVisibility(7)" label="问题数据量占比" align="center" prop="proportion"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                {{
                                    (scope.row.problemTotal != -1 && scope.row.problemTotal != null)
                                        ? `${scope.row.problemTotal} /条 ${scope.row.proportion ?? '-'}%`
                                        : '-'
                                }}
                            </template>

                        </el-table-column>
                        <el-table-column label="操作" fixed="right" width="140" align="center">
                            <template #default="scope">
                                <el-button link type="primary" icon="View"
                                    @click="openDialog(scope.row)">查看问题数据</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-row>

        <!-- 问题数据弹窗 -->
        <ProblemDialog ref="problemDialogRef" />
    </div>
</template>

<script setup>
import * as echarts from 'echarts';
import { useRoute } from 'vue-router';
import { ref, onMounted, onBeforeUnmount } from 'vue';
import moment from 'moment';
const { proxy } = getCurrentInstance();
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import ProblemDialog from '../components/problemData.vue';
import {
    statisticsEvaluateOne,
    statisticsEvaluateTow,
    statisticsEvaluateTable
} from "@/api/da/quality/qualityTaskLog";
const { att_rule_audit_q_dimension, } = proxy.useDict(

    'att_rule_audit_q_dimension'
);
const getScoreClass = (score) => {
    if (score == null || score === '-') return 'score-null';
    if (score >= 85) return 'score-high';
    if (score >= 60) return 'score-medium';
    return 'score-low';
};
const route = useRoute();
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
// 评分和质量维度汇总
const loadScoreAndSummary = async (id) => {
    try {
        const res = await statisticsEvaluateOne(id);
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
const loadRuleTable = async (id) => {
    try {
        const res = await statisticsEvaluateTable(id);
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
const loadTrendChart = async (id) => {
    try {
        const range = Number(selectedRange.value);
        const today = moment().format('YYYY-MM-DD');
        const oldDate = moment().subtract(Number(selectedRange.value), 'days').format('YYYY-MM-DD');
        const type = selectedRange.value === '7' ? 0 : selectedRange.value === '15' ? 1 : 2;
        const res = await statisticsEvaluateTow({ id, deDate: today, oldDate, type });
        console.log("🚀 ~ loadTrendChart ~ res:", res)

        loadChartWithData(res?.data || []);
    } catch (err) {
        console.warn('折线图数据失败', err);
    }
};

//
const fetchData = async (id) => {
    loading.value = true;
    await Promise.all([
        loadScoreAndSummary(id),
        loadRuleTable(id),
        loadTrendChart(id)
    ]);
    loading.value = false;
};

const onRangeChange = () => {
    const id = route.query.id || 'default';
    loadTrendChart(id)
};

const handleResize = () => {
    chartInstance?.resize();
};

onMounted(() => {
    const id = route.query.id || 'default';
    overallScore.value = route.query.score
    fetchData(id);
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
</style>
