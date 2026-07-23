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
    <div class="dp-chart">
        <div class="dp-chart--title">察布查尔锡伯自治县-加格斯台水库日均均值</div>
        <div class="dp-chart--main" ref="chartRef"></div>
    </div>
</template>

<script setup name="ViewA2Chart">
    import * as echarts from 'echarts';
    const chartRef = ref(null);
    /**
     * xf_relate opposite association
     * xt_relate same association
     * ya_relate y-axis correlation
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
                // params is an array that contains data information for each series
                let result = params[0]?.name + '<br />';
                let unit = '';
                params.forEach(function (item) {
                    // item is the data of each series
                    const seriesName = item.seriesName; // Series name
                    const value = item.value == null ? '-' : item.value; // data value
                    const marker = item.marker; // logo graphic
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
