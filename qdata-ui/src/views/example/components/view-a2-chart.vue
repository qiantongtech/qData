<template>
    <div class="dp-chart">
        <div class="dp-chart--title">察布查尔锡伯自治县-加格斯台水库日均均值</div>
        <div class="dp-chart--main" ref="chartRef"></div>
    </div>
</template>

<script setup name="ViewA2Chart">
    import * as echarts from 'echarts';
    const chartRef = ref(null);
    /**
     * xf_relate 相反关联
     * xt_relate 相同关联
     * ya_relate y轴关联
     */
    const chartRules = {
        库水位: {
            ya_relate: {
                type: 'value',
                name: '库水位(m)',
                index: 0
            }
        },
        入库流量: {
            xf_relate: ['蓄水量'],
            ya_relate: {
                type: 'value',
                name: '流量(m³/s)',
                index: 1
            }
        },
        出库流量: {
            xf_relate: ['蓄水量'],
            ya_relate: {
                type: 'value',
                name: '流量(m³/s)',
                index: 1
            }
        },
        蓄水量: {
            xf_relate: ['入库流量', '出库流量'],
            ya_relate: {
                type: 'value',
                name: '蓄水量(10⁶m³)',
                index: 1
            }
        }
    };

    const chartOptions = {
        color: ['#333333', '#0000ff', '#ff0000', '#008000'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: 'rgb(0, 0, 0)'
                }
            },
            formatter: function (params) {
                // params 是一个数组，数组中包含每个系列的数据信息
                let result = params[0]?.name + '<br />';
                let unit = '';
                params.forEach(function (item) {
                    // item 是每一个系列的数据
                    const seriesName = item.seriesName; // 系列名称
                    const value = item.value == null ? '-' : item.value; // 数据值
                    const marker = item.marker; // 标志图形
                    switch (seriesName) {
                        case '库水位':
                        case '蓄水量':
                        case '正常蓄水位':
                        case '汛限水位':
                        case '防洪高水位':
                        case '设计洪水位':
                        case '死水位':
                        case '校核洪水位':
                        case '历史最高库水位':
                            unit = 'm';
                            break;
                        case '入库流量':
                        case '出库流量':
                            unit = 'm³/s';
                            break;
                    }
                    result += `${marker}${seriesName}: ${value}${unit}<br/>`;
                });
                return result;
            }
        },
        grid: {
            top: '15%',
            bottom: '3%',
            left: '3%',
            right: '3%',
            containLabel: true
        },
        legend: {
            selected: {
                库水位: true,
                入库流量: false,
                出库流量: true,
                蓄水量: false
            }
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            boundaryGap: false,
            axisTick: {
                show: false
            },
            axisLabel: {
                fontSize: 14
            },
            axisLine: {
                lineStyle: {
                    color: '#000000'
                }
            }
        },
        yAxis: [
            {
                type: 'value',
                name: '库水位(m)',
                nameTextStyle: {
                    fontSize: 14,
                    align: 'center',
                    padding: [0, 0, 0, 0]
                },
                axisLabel: {
                    interval: 0,
                    fontSize: 14
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: 'rgba(0, 0, 0, 1)',
                        width: 1
                    }
                }
            },
            {
                type: 'value',
                name: '流量(m³/s)',
                nameTextStyle: {
                    fontSize: 14,
                    align: 'center',
                    padding: [0, 0, 0, 0]
                },
                axisLabel: {
                    interval: 0,
                    fontSize: 12
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: 'rgba(0, 0, 0, 1)',
                        width: 1
                    }
                }
            }
        ],
        series: [
            {
                name: '库水位',
                data: [120, 132, 101, 134, 90, 230, 210],
                type: 'line',
                connectNulls: true,
                yAxisIndex: 0
            },
            {
                name: '入库流量',
                data: [220, 182, 191, 234, 290, 330, 310],
                type: 'line',
                connectNulls: true,
                yAxisIndex: 1
            },
            {
                name: '出库流量',
                data: [150, 232, 201, 154, 190, 330, 410],
                type: 'line',
                connectNulls: true,
                yAxisIndex: 1
            },
            {
                name: '蓄水量',
                data: [320, 332, 301, 334, 390, 330, 320],
                type: 'line',
                connectNulls: true,
                yAxisIndex: 1
            }
        ]
    };
    const chartIntance = shallowRef(null);

    function initLeftChart() {
        chartIntance.value = echarts.init(chartRef.value, 'macarons');
        chartIntance.value.setOption(chartOptions, true);
        chartIntance.value.on('legendselectchanged', function ({ name, selected }) {
            if (!chartRules[name]) {
                chartOptions.legend.selected = selected;
                return;
            }
            const state = selected[name];
            const { ya_relate, xf_relate, xt_relate } = chartRules[name];
            if (ya_relate && state) {
                chartOptions.yAxis[ya_relate.index].show = true;
                chartOptions.yAxis[ya_relate.index].name = ya_relate.name;
            }
            if (ya_relate && !state) {
                chartOptions.yAxis[ya_relate.index].show = false;
                chartOptions.yAxis[ya_relate.index].name = '';
                chartIntance.value.setOption(chartOptions, true);
            }
            if (!state) {
                chartOptions.legend.selected = selected;
                return;
            }
            if (xf_relate) {
                xf_relate.forEach((item) => {
                    selected[item] = !state;
                });
            }

            if (xt_relate) {
                xt_relate.forEach((item) => {
                    selected[item] = state;
                });
            }
            chartOptions.legend.selected = selected;
            chartIntance.value.setOption(chartOptions, true);
        });
        nextTick(() => {
            resize();
        });
    }

    function resize() {
        chartIntance.value && chartIntance.value.resize();
    }

    window.addEventListener('resize', function () {
        resize();
    });

    defineExpose({ resize });
    onMounted(() => {
        initLeftChart();
    });
</script>
