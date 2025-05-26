<template>
    <dp-main>
        <template #header>
            <el-form :model="params" ref="realForm" :inline="true">
                <el-form-item label="开始时间：" prop="startDate">
                    <el-date-picker
                        v-model="params.startDate"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="开始时间"
                        :clearable="false"
                        style="width: 140px"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间：" prop="endDate">
                    <el-date-picker
                        v-model="params.endDate"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="结束时间"
                        :clearable="false"
                        style="width: 140px"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="比较年：" prop="years">
                    <el-dropdown trigger="click" placement="bottom-start" :hide-on-click="false">
                        <el-input style="width: 120px" v-model="params.years" readonly></el-input>
                        <template #dropdown>
                            <el-checkbox-group v-model="params.years" style="width: 120px">
                                <el-dropdown-item
                                    v-for="(item, index) in yearsOptions"
                                    :key="index"
                                >
                                    <el-checkbox :value="item.label" style="width: 100%">
                                        {{ item.label }}
                                    </el-checkbox>
                                </el-dropdown-item>
                            </el-checkbox-group>
                        </template>
                    </el-dropdown>
                </el-form-item>
                <el-form-item label="" label-width="0px" prop="years">
                    <el-radio-group v-model="params.radio">
                        <el-radio
                            :value="item.value"
                            :key="item.value"
                            v-for="item in radioOptions"
                        >
                            {{ item.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :icon="Search" @click="getChartData">
                        查询
                    </el-button>
                </el-form-item>
            </el-form>
        </template>
        <dp-shrink width="600px" placement="right" @change="handleShrinkChange">
            <template #flex>
                <div class="flex-content">
                    <dp-table :fn="getTableData" :pageSize="13" :column="tableColumn" />
                </div>
            </template>

            <dp-chart-a
                ref="dpChartARef"
                title="历史同期对比"
                :xData="dpChartAXData"
                :yData="dpChartAYData"
            />
        </dp-shrink>
    </dp-main>
</template>

<script setup name="DetailPopResViewA3">
    
    import { Search } from '@element-plus/icons-vue';
    import moment from 'moment';

    const params = ref({
        startDate: moment().subtract(7, 'days').format('YYYY-MM-DD'),
        endDate: moment().format('YYYY-MM-DD'),
        years: ['2024', '2023'],
        radio: 'otq'
    });

    const yearsOptions = [
        {
            label: '2024'
        },
        {
            label: '2023'
        },
        {
            label: '2022'
        },
        {
            label: '2021'
        },
        {
            label: '2020'
        },
        {
            label: '2019'
        }
    ];

    const radioOptions = [
        { value: 'rz', label: '库水位', unit: 'm' },
        { value: 'inq', label: '入库流量', unit: 'm³/s' },
        { value: 'otq', label: '出库流量', unit: 'm³/s' },
        { value: 'w', label: '蓄水量', unit: '10⁶m³' }
    ];

    const activeRadioConfig = computed(() => {
        return radioOptions.find((item) => item.value === params.value.radio);
    });

    const tableColumn = ref([
        {
            prop: 'tm',
            label: '时间'
        },
        {
            prop: 'y2023',
            label: '2023'
        },
        {
            prop: 'y2024',
            label: '2024'
        }
    ]);

    function getTableData() {
        return new Promise((resolve) => {
            setTimeout(() => {
                const data = [];
                for (let i = 0; i < 23; i++) {
                    data.push({
                        tm: '2024-10-21 08:00:00',
                        y2023: (i + 1) * 20,
                        y2024: (i + 2) * 20
                    });
                }
                resolve(data);
            }, 2000);
        });
    }

    const dpChartARef = ref(null);
    const dpChartAXData = ref([]);
    const dpChartAYData = ref([]);
    function getChartData() {
        setTimeout(() => {
            const { label, unit } = activeRadioConfig.value;
            dpChartAXData.value = ['x0', 'x1', 'x2', 'x3', 'x4'];
            dpChartAYData.value = [
                {
                    name: `2023-${label}`,
                    data: [150, 230, 224, 218, 135],
                    type: 'line'
                },
                {
                    name: `2024-${label}`,
                    data: [220, 182, 191, 234, 290],
                    type: 'line'
                }
            ];

            // dp-chart-a自定义配置
            const { chartOptions } = dpChartARef.value.useDpChartA();

            chartOptions.value.yAxis.name = `${label}(${unit}) `;
            chartOptions.value.yAxis.max = function (value) {
                if (value.max === value.min) {
                    return parseFloat((value.max + value.max + 0.3).toFixed(1));
                }
                var maxN = value.max + (value.max - value.min) * 0.3 + 0.1;
                return parseFloat(maxN.toFixed(1));
            };
            chartOptions.value.yAxis.min = function (value) {
                if (value.max === value.min) {
                    var minN1 = value.min - value.min * 0.2;
                    if (minN1 < 0) {
                        return 0;
                    } else {
                        return parseFloat(minN1.toFixed(1));
                    }
                }
                var minN = value.min - (value.max - value.min) * 0.3;
                if (minN < 0) {
                    return 0;
                } else {
                    return parseFloat(minN.toFixed(1));
                }
            };

            // dp-chart-a更新echarts
            dpChartARef.value.updateEcharts();
        }, 1000);
    }
    getChartData();

    function handleShrinkChange(e) {
        console.log('收缩区域状态改变', e);
        setTimeout(() => {
            dpChartARef.value.resize();
        }, 300);
    }
</script>
