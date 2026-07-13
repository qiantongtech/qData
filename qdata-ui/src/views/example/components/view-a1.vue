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
                <el-form-item>
                    <el-button type="primary" :icon="Search"> 查询 </el-button>
                </el-form-item>
            </el-form>
        </template>
        <dp-shrink width="600px" placement="right" @change="handleShrinkChange">
            <template #flex>
                <div class="flex-content">
                    <div class="opinion-wrap">
                        <div class="dp-main--h3">调度意见</div>
                        <p>{{ td('common.noOpinion') }}</p>
                    </div>
                    <dp-table :fn="getTableData" :column="tableColumn" />
                </div>
            </template>

            <view-a1-chart ref="viewA1ChartRef" />
        </dp-shrink>
    </dp-main>
</template>

<script setup name="DetailPopResViewA1">
    import useDefaultLang from "@/composables/useDefaultLang";
    const { td } = useDefaultLang();
    import ViewA1Chart from './view-a1-chart.vue';
    import { Search } from '@element-plus/icons-vue';
    import moment from 'moment';

    const params = ref({
        startDate: moment().subtract(7, 'days').format('YYYY-MM-DD'),
        endDate: moment().format('YYYY-MM-DD')
    });

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

    const viewA1ChartRef = ref(null);
    function handleShrinkChange(e) {
        console.log("Collapse area state changed", e);
        setTimeout(() => {
            viewA1ChartRef.value.resize();
        }, 300);
    }
</script>
