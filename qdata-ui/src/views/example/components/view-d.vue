<template>
    <dp-main>
        <dp-shrink width="600px" placement="right" @change="handleShrinkChange">
            <template #flex>
                <div class="flex-content">
                    <div class="dp-main--h3">库容数据</div>
                    <dp-table :fn="getTableData" :pageSize="14" :column="tableColumn" />
                </div>
            </template>

            <dp-chart-a
                ref="dpChartARef"
                title="库容曲线"
                :xData="dpChartAXData"
                :yData="dpChartAYData"
            />
        </dp-shrink>
    </dp-main>
</template>

<script setup name="DetailPopResViewD">
    

    const tableColumn = ref([
        {
            prop: 'rz',
            label: '库水位',
            unit: '(m)'
        },
        {
            prop: 'w',
            label: '蓄水量',
            unit: '(10⁶m³)'
        }
    ]);

    function getTableData() {
        return new Promise((resolve) => {
            setTimeout(() => {
                const data = [];
                for (let i = 0; i < 23; i++) {
                    data.push({
                        tm: '2024-10-21 08:00:00',
                        rz: 863.2,
                        inq: 0,
                        otq: undefined,
                        w: 100 + i
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
            dpChartAXData.value = ['x0', 'x1', 'x2', 'x3', 'x4'];
            dpChartAYData.value = [
                {
                    name: `库水位`,
                    data: [220, 182, 191, 234, 290],
                    barWidth: 10,
                    type: 'line',
                    smooth: true
                }
            ];

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
