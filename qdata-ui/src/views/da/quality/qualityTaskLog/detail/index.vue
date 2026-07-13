<!--
  Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.

  This file is part of qData Data Middle Platform (Open Source Edition).

  qData is licensed under Apache License 2.0 with additional qData terms.
  You may use qData for commercial purposes, but you may not remove, hide,
  modify, or replace the qData logo, copyright notices, license notices,
  or attribution information without a separate commercial license.

  White-label use, OEM distribution, rebranding, or presenting qData as
  another product requires separate commercial authorization from
  Jiangsu Qiantong Technology Co., Ltd.

  Business License: https://community.qdata.tech/business/policy.html
  See the LICENSE file in the project root for full license information.
-->

<template>
    <div class="app-container stagingIndex" v-loading="loading">
        <!-- Top area: Rating + Line Chart -->
        <el-row gutter="20" class="top-section">
            <!-- Rating on the left -->
            <el-col :xs="24" :sm="24" :md="12" class="stats-panel">
                <div class="module-8 border-item">
                    <div class="border-item-head">
                        <span class="head-title">{{ td('da.qualityTaskLog.detail.qualityDimensionStats') }}</span>
                    </div>
                    <div class="border-item-body">
                        <div class="overall-score">
                            <span>{{ td('da.qualityTaskLog.detail.overallQualityScore') }}</span>
                            <span class="score" :class="getScoreClass(overallScore)">
                                {{ overallScore || '-' }}
                            </span>
                        </div>
                        <el-table :data="summaryList" border size="small" style="margin-top: 12px" height="246">
                            <el-table-column prop="dimensionType" :label="td('da.qualityTaskLog.detail.qualityDimension')" align="center">
                                <template #default="scope">
                                    <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />
                                </template>

                            </el-table-column>
                            <el-table-column prop="succesTotal" :label="td('da.qualityTaskLog.detail.ruleCount')" align="center">
                                <template #default="scope">{{ scope.row.succesTotal || '-' }}</template>
                            </el-table-column>
                            <el-table-column prop="proportion" :label="td('da.qualityTaskLog.detail.problemRatio')" align="center">
                                <template #default="scope">
                                    {{ scope.row.proportion != null ? scope.row.proportion + '%' : '-' }}
                                </template>

                            </el-table-column>
                            <el-table-column :label="td('da.qualityTaskLog.detail.trend')" align="center">
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

            <!-- Line chart on the right -->
            <el-col :xs="24" :sm="24" :md="12" class="trend-chart-panel">
                <div class="module-8 border-item">
                    <div class="border-item-head">
                        <span class="head-title">{{ td('da.qualityTaskLog.detail.dataGovernanceTrend') }}</span>
                        <el-select v-model="selectedRange" size="small" :placeholder="td('da.qualityTaskLog.detail.selectTimeRange')" style="width: 120px"
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

        <!-- Rule list -->
        <el-row>
            <div class="module-8 border-item" style="width: 100%">
                <div class="border-item-head">
                    <span class="head-title">{{ td('da.qualityTaskLog.detail.ruleList') }}</span>
                </div>
                <div class="border-item-body" style="height: 320px;">
                    <el-table stripe height="300px" v-loading="loading" :data="ruleList" lazy :show-overflow-tooltip="{effect: 'light'}">
                        <el-table-column v-if="getColumnVisibility(8)" :label="td('da.qualityTaskLog.detail.evaluationName')" align="center"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                {{ getEvaluateName(scope.row) }}
                            </template>
                        </el-table-column>

                        <el-table-column v-if="getColumnVisibility(1)" :label="td('da.qualityTaskLog.detail.dbName')" align="center" prop="name"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">{{ scope.row.datasourceName || '-' }}</template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(2)" :label="td('da.qualityTaskLog.detail.fieldInfo')" align="center" prop="name"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope"> {{ scope.row.columnLabel || '-' }}</template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(3)" :label="td('da.qualityTaskLog.detail.qualityDimension')" align="center" prop="dimensionType"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                <dict-tag :options="att_rule_audit_q_dimension" :value="scope.row.dimensionType" />

                            </template>
                        </el-table-column>
                        <el-table-column v-if="getColumnVisibility(5)" :label="td('da.qualityTaskLog.detail.inspectionName')" align="center" prop="ruleName"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">{{ scope.row.ruleName || '-' }}</template>
                        </el-table-column>

                        <el-table-column v-if="getColumnVisibility(7)" :label="td('da.qualityTaskLog.detail.problemDataRatio')" align="center" prop="proportion"
                            :show-overflow-tooltip="{effect: 'light'}">
                            <template #default="scope">
                                {{
                                    (scope.row.problemTotal != -1 && scope.row.problemTotal != null)
                                        ? `${scope.row.problemTotal} /${td('da.qualityTaskLog.detail.problemTotalUnit')} ${scope.row.proportion ?? '-'}%`
                                        : '-'
                                }}
                            </template>

                        </el-table-column>
                        <el-table-column :label="td('common.texts.operation')" fixed="right" width="190" align="center">
                            <template #default="scope">
                                <el-button link type="primary" icon="View"
                                    @click="openDialog(scope.row)">{{ td('da.qualityTaskLog.detail.viewProblemData') }}</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-row>

        <!-- Question data pop-up window -->
        <ProblemDialog ref="problemDialogRef" />
    </div>
</template>

<script setup>
import * as echarts from 'echarts';
import { useRoute } from 'vue-router';
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import moment from 'moment';
const { proxy } = getCurrentInstance();
import { ArrowUp, ArrowDown } from '@element-plus/icons-vue';
import ProblemDialog from '../components/problemData.vue';
import useDefaultLang from "@/composables/useDefaultLang";
const { td, locale } = useDefaultLang();
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
    { label: td('da.qualityTaskLog.detail.last7Days'), value: '7' },
    { label: td('da.qualityTaskLog.detail.last15Days'), value: '15' },
    { label: td('da.qualityTaskLog.detail.last30Days'), value: '30' }
];

const ruleList = ref([]);
const overallScore = ref();
const summaryList = ref([]);
const loading = ref(false);

const columns = ref([

    { key: 8, label: td('da.qualityTaskLog.detail.columnLabels.ruleName'), visible: true },
    { key: 1, label: td('da.qualityTaskLog.detail.columnLabels.dbName'), visible: true },
    { key: 2, label: td('da.qualityTaskLog.detail.columnLabels.fieldInfo'), visible: true },
    { key: 3, label: td('da.qualityTaskLog.detail.columnLabels.qualityDimension'), visible: true },
    { key: 5, label: td('da.qualityTaskLog.detail.columnLabels.ruleName'), visible: true },
    { key: 7, label: td('da.qualityTaskLog.detail.columnLabels.problemDataRatio'), visible: true },
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
        console.warn("Failed to parse rule fields", err);
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
            data: [td('common.qualityTrends')],
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
            name: td('common.qualityTrends'),
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


// Summary of ratings and quality dimensions
// Summary of ratings and quality dimensions
const loadScoreAndSummary = async (id) => {
    try {
        const res = await statisticsEvaluateOne(id);
        const result = res?.data || [];

        // Construct a dimensional map for quick search
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
        console.warn("Failed to aggregate scores and dimensions", err);
    }
};
// Rule list
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
        console.warn("Failed to load rule list", err);
    } finally {
    }
};

// Line chart data
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
        console.warn("Failed to load line chart data", err);
    }
};

// Monitor language changes and re-render charts
watch(locale, () => {
    if (chartInstance) {
        chartInstance.dispose();
        chartInstance = null;
    }
    const id = route.query.id || 'default';
    loadTrendChart(id);
});

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
