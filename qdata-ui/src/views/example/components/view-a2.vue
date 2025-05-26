<template>
    <dp-main>
        <template #header>
            <el-form :model="params" ref="realForm" :inline="true">
                <el-form-item label="时段选择：" prop="avgTypeId">
                    <el-select style="width: 80px" v-model="params.avgTypeId" placeholder="请选择">
                        <el-option
                            v-for="item in avgTypeIdOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
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
                <el-form-item>
                    <el-button type="primary" :icon="Search"> 查询 </el-button>
                </el-form-item>
            </el-form>
        </template>
        <dp-shrink width="600px" placement="right" @change="handleShrinkChange">
            <template #flex>
                <div class="flex-content">
                    <dp-table :fn="getTableData" :column="tableColumn" />
                </div>
            </template>

            <view-a2-chart ref="viewA2ChartRef" />
        </dp-shrink>
    </dp-main>
</template>

<script setup name="DetailPopResViewA2">
    import ViewA2Chart from './view-a2-chart.vue';
    import { Search } from '@element-plus/icons-vue';
    import moment from 'moment';

    const params = ref({
        avgTypeId: '1',
        startDate: moment().subtract(7, 'days').format('YYYY-MM-DD'),
        endDate: moment().format('YYYY-MM-DD')
    });

    const avgTypeIdOptions = [
        {
            value: '1',
            label: '日均'
        },
        {
            value: '4',
            label: '旬均'
        },
        {
            value: '5',
            label: '月均'
        },
        {
            value: '6',
            label: '年均'
        }
    ];

    const tableColumn = ref([
        {
            prop: 'tb1',
            label: '时间',
            width: '150'
        },
        {
            prop: 'tb2',
            label: '库水位',
            unit: '(m)'
        },
        {
            prop: 'tb3',
            label: '入库流量',
            unit: '(m³/s)'
        },
        {
            prop: 'tb4',
            label: '出库流量',
            unit: '(m³/s)'
        },
        {
            prop: 'tb5',
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
                        tb1: '2024-10-21 08:00:00',
                        tb2: 863.2,
                        tb3: 0,
                        tb4: undefined,
                        tb5: 100 + i
                    });
                }
                resolve(data);
            }, 2000);
        });
    }

    const viewA2ChartRef = ref(null);
    function handleShrinkChange(e) {
        console.log('收缩区域状态改变', e);
        setTimeout(() => {
            viewA2ChartRef.value.resize();
        }, 300);
    }
</script>
