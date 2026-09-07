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
    <div class="app-container" ref="app-container">
        <qt-wrap :columns="tableStore.columns" :tableRef="tableRef">
            <template #search>
                <qt-search-bar v-bind="searchStore" :params="tableStore.params" @query="handleQuery" @reset="resetQuery"
                    :tableRef="tableRef" />
            </template>

            <qt-table v-bind="tableStore" ref="tableRef" :params="tableStore.params">
                <template #action="{ row }">
                    <el-button link type="primary" icon="view" @click="
                        routeTo('/da/quality/qualityTaskLog/detail', {
                            ...row,
                            info: true,
                        })
                        " v-hasPermi="['dp:qualityLog:edit']">{{ td('common.button.details') }}</el-button>
                </template>
            </qt-table>
        </qt-wrap>
    </div>
</template>

<script setup name="DppQualityLog">
import { listDppQualityLog } from "@/api/da/quality/qualityTaskLog";
import { useRouter } from "vue-router";
import { ref, reactive } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const router = useRouter();
const tableRef = ref(null);

const { quality_log_success_flag } = proxy.useDict('quality_log_success_flag');

const tableStore = reactive({
    config: {
        initResquest: true,
    },
    columns: [
        { label: td('da.qualityTaskLog.columnLabels.id'), prop: 'id', width: 60, align: 'left', sortable: true },
        { label: td('da.qualityTaskLog.columnLabels.taskName'), prop: 'name', width: 260, align: 'left', showOverflowTooltip: { effect: 'light' } },
        { label: td('da.qualityTaskLog.columnLabels.qualityScore'), prop: 'score', width: 160, align: 'left', sortable: true },
        { label: td('da.qualityTaskLog.columnLabels.problemData'), prop: 'problemData', width: 300, align: 'left', showOverflowTooltip: { effect: 'light' } },
        { label: td('da.qualityTaskLog.columnLabels.executionStatus'), prop: 'successFlag', width: 160, align: 'left', dict: 'quality_log_success_flag' },
        { label: td('da.qualityTaskLog.columnLabels.startTime'), prop: 'startTime', width: 160, align: 'left', sortable: true, date: true },
        { label: td('da.qualityTaskLog.columnLabels.endTime'), prop: 'endTime', width: 160, align: 'left', sortable: true, date: true },
        { label: td('common.texts.operation'), slot: 'action', width: 120, align: 'center', fixed: 'right' },
    ],
    func: listDppQualityLog,
    params: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        successFlag: null,
    },
});

const searchStore = reactive({
    items: [
        {
            label: td('da.qualityTaskLog.taskName'),
            prop: 'name',
            component: { is: 'input', placeholder: td('da.qualityTaskLog.taskNamePlaceholder') }
        },
        {
            label: td('da.qualityTaskLog.executionStatus'),
            prop: 'successFlag',
            component: {
                is: 'select',
                placeholder: td('da.qualityTaskLog.executionStatusPlaceholder'),
                options: quality_log_success_flag
            }
        },
    ]
});

/** Search button operation */
function handleQuery() {
    tableStore.params.pageNum = 1;
}

/** Reset button operation */
function resetQuery() {
    handleQuery();
}

function routeTo(link, row) {
    if (link !== "" && link.indexOf("http") !== -1) {
        window.location.href = link;
        return
    }
    if (link !== "") {
        if (link === router.currentRoute.value.path) {
            window.location.reload();
        } else {
            router.push({
                path: link,
                query: {
                    id: row.id,
                    score: row.score
                }
            });
        }
    }
}
</script>

