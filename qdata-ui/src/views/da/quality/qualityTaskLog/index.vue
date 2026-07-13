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
        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true"
                v-show="showSearch" @submit.prevent>
                <el-form-item :label="td('da.qualityTaskLog.taskName')" prop="name">
                    <el-input class="el-form-input-width" v-model="queryParams.name" :placeholder="td('da.qualityTaskLog.taskNamePlaceholder')" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item :label="td('da.qualityTaskLog.executionStatus')" prop="successFlag">
                    <el-select v-model="queryParams.successFlag" :placeholder="td('da.qualityTaskLog.executionStatusPlaceholder')" clearable
                        class="el-form-input-width">
                        <el-option v-for="dict in quality_log_success_flag" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>{{ td('common.button.query') }}
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>{{ td('common.button.reset') }}
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="pagecont-bottom">
            <div class="justify-between mb15">
                <div class="justify-end top-right-btn">
                    <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"
                        :columns="columns"></right-toolbar>
                </div>
            </div>
            <el-table stripe v-loading="loading" :data="DppQualityLogList" :default-sort="defaultSort"
                @sort-change="handleSortChange">
                <el-table-column v-if="getColumnVisibility(0)" :label="td('da.qualityTaskLog.columnLabels.id')" align="center" prop="id" width="80" />
                <el-table-column v-if="getColumnVisibility(1)" :label="td('da.qualityTaskLog.columnLabels.taskName')" align="center" prop="name">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" :label="td('da.qualityTaskLog.columnLabels.qualityScore')" align="center" prop="score"
                    sortable="custom" column-key="score" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        {{ scope.row.score }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" :label="td('da.qualityTaskLog.columnLabels.problemData')" align="center" prop="problemData"
                    :show-overflow-tooltip="{ effect: 'light' }" width="300">
                    <template #default="scope">
                        {{ scope.row.problemData || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" :label="td('da.qualityTaskLog.columnLabels.executionStatus')" align="center" prop="successFlag">
                    <template #default="scope">
                        <dict-tag :options="quality_log_success_flag" :value="scope.row.successFlag" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" :label="td('da.qualityTaskLog.columnLabels.startTime')" align="center" prop="startTime" width="160"
                    sortable="custom" column-key="start_time" :sort-orders="['descending', 'ascending']"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(6)" :label="td('da.qualityTaskLog.columnLabels.endTime')" align="center" prop="endTime" width="160"
                    sortable="custom" column-key="end_time" :sort-orders="['descending', 'ascending']"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column :label="td('common.texts.operation')" v-if="getColumnVisibility(7)" align="center"
                    class-name="small-padding fixed-width" fixed="right" width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="view" @click="
                            routeTo('/da/quality/qualityTaskLog/detail', {
                                ...scope.row,
                                info: true,
                            })
                            " v-hasPermi="['dp:qualityLog:edit']">{{ td('common.button.details') }}</el-button>
                        <!-- <el-button link type="primary" style="padding-left: 14px" @click="sendMessage(scope.row)"
                            v-hasPermi="['dp:qualityLog:edit']" :disabled="scope.row.status == 1">
                            <svg-icon iconClass="damessage" style="margin-right: 6px;" />Notification processing
                        </el-button> -->
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="@/assets/images/system/no_data/empty-nodata.png" alt="" />
                        <p>{{td('common.noData')}}</p>
                    </div>
                </template>
            </el-table>

            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </div>
    </div>
</template>

<script setup name="DppQualityLog">
import { listDppQualityLog, doSendMessage } from "@/api/da/quality/qualityTaskLog";
import { useRoute, useRouter } from "vue-router";
import { ref } from "vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { td } = useDefaultLang();
const { proxy } = getCurrentInstance();
const defaultSort = ref({ columnKey: 'start_time', order: 'desc' });
const { quality_log_success_flag } = proxy.useDict(

    'quality_log_success_flag'
);
const DppQualityLogList = ref([]);
// Show hidden information
const columns = ref([
    { key: 0, label: td('da.qualityTaskLog.columnLabels.id'), visible: true },
    { key: 1, label: td('da.qualityTaskLog.columnLabels.taskName'), visible: true },
    { key: 2, label: td('da.qualityTaskLog.columnLabels.qualityScore'), visible: true },
    { key: 3, label: td('da.qualityTaskLog.columnLabels.problemData'), visible: true },
    { key: 4, label: td('da.qualityTaskLog.columnLabels.executionStatus'), visible: true },
    { key: 5, label: td('da.qualityTaskLog.columnLabels.startTime'), visible: true },
    { key: 6, label: td('da.qualityTaskLog.columnLabels.endTime'), visible: true },
    { key: 7, label: td('common.texts.operation'), visible: true },
]);
const getColumnVisibility = (key) => {
    const column = columns.value.find(col => col.key == key);
    if (!column) return true;
    return column.visible;
};
const loading = ref(false);
const showSearch = ref(true);
const total = ref(0);
const router = useRouter();
const data = reactive({
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        successFlag: null,
        startTime: null,
        endTime: null,
        qualityId: null,
        score: null,
        problemData: null,
        createTime: null,
    },

});

const { queryParams, } = toRefs(data);

/** Sorting trigger events */
function handleSortChange({ column, prop, order }) {
    queryParams.value.orderByColumn = column?.columnKey || prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** Query the data quality log list */
function getList() {
    loading.value = true;
    listDppQualityLog(queryParams.value).then(response => {
        DppQualityLogList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}
/** Search button action */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
/** reset button action */
function resetQuery() {
    proxy.resetForm("queryRef");
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

async function sendMessage(row) {
    if (!row?.id) {
        proxy.$modal.msgWarning(td('da.qualityTaskLog.invalidTaskId'));
        return;
    }
    const res = await doSendMessage(row.id);
    if (Number(res?.code) === 200) {
        proxy.$modal.msgSuccess(td('da.qualityTaskLog.sendSuccess'));
    } else {
        proxy.$modal.msgWarning(res?.msg || td('da.qualityTaskLog.sendFailed'));
    }
}

getList();
</script>
