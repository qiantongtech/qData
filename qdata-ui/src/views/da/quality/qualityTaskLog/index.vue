<template>
    <div class="app-container" ref="app-container">
        <div class="pagecont-top" v-show="showSearch">
            <el-form class="btn-style" :model="queryParams" ref="queryRef" :inline="true" label-width="75px"
                v-show="showSearch" @submit.prevent>
                <el-form-item label="任务名称" prop="name">
                    <el-input class="el-form-input-width" v-model="queryParams.name" placeholder="请输入任务名称" clearable
                        @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item label="执行状态" prop="successFlag">
                    <el-select v-model="queryParams.successFlag" placeholder="请选择执行状态" clearable
                        class="el-form-input-width">
                        <el-option v-for="dict in quality_log_success_flag" :key="dict.value" :label="dict.label"
                            :value="dict.value" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary" @click="handleQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                    </el-button>
                    <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
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
                <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" width="80" />
                <el-table-column v-if="getColumnVisibility(1)" label="任务名称" align="center" prop="name">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" label="质量评分" align="center" prop="score"
                    sortable="custom" column-key="score" :sort-orders="['descending', 'ascending']">
                    <template #default="scope">
                        {{ scope.row.score }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" label="问题数据" align="center" prop="problemData"
                    :show-overflow-tooltip="{ effect: 'light' }" width="300">
                    <template #default="scope">
                        {{ scope.row.problemData || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" label="执行状态" align="center" prop="successFlag">
                    <template #default="scope">
                        <dict-tag :options="quality_log_success_flag" :value="scope.row.successFlag" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" label="开始时间" align="center" prop="startTime" width="160"
                    sortable="custom" column-key="start_time" :sort-orders="['descending', 'ascending']"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(6)" label="结束时间" align="center" prop="endTime" width="160"
                    sortable="custom" column-key="end_time" :sort-orders="['descending', 'ascending']"
                    :show-overflow-tooltip="{ effect: 'light' }">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" v-if="getColumnVisibility(7)" align="center"
                    class-name="small-padding fixed-width" fixed="right" width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="view" @click="
                            routeTo('/da/quality/qualityTaskLog/detail', {
                                ...scope.row,
                                info: true,
                            })
                            " v-hasPermi="['dp:qualityLog:edit']">详情</el-button>
                        <!-- <el-button link type="primary" style="padding-left: 14px" @click="sendMessage(scope.row)"
                            v-hasPermi="['dp:qualityLog:edit']" :disabled="scope.row.status == 1">
                            <svg-icon iconClass="damessage" style="margin-right: 6px;" />通知处理
                        </el-button> -->
                    </template>
                </el-table-column>

                <template #empty>
                    <div class="emptyBg">
                        <img src="@/assets/system/images/no_data/noData.png" alt="" />
                        <p>暂无记录</p>
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
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router"
import { ref } from "vue";
const defaultSort = ref({ columnKey: 'start_time', order: 'desc' });
const { quality_log_success_flag } = proxy.useDict(

    'quality_log_success_flag'
);
const DppQualityLogList = ref([]);
// 列显隐信息
const columns = ref([
    { key: 0, label: "编号", visible: true },
    { key: 1, label: "任务名称", visible: true },
    { key: 2, label: "质量评分", visible: true },
    { key: 3, label: "问题数据", visible: true },
    { key: 4, label: "执行状态", visible: true },
    { key: 5, label: "开始时间", visible: true },
    { key: 6, label: "结束时间", visible: true },
    { key: 7, label: "操作", visible: true },
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

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
    queryParams.value.orderByColumn = column?.columnKey || prop;
    queryParams.value.isAsc = column.order;
    getList();
}

/** 查询数据质量日志列表 */
function getList() {
    loading.value = true;
    listDppQualityLog(queryParams.value).then(response => {
        DppQualityLogList.value = response.data.rows;
        total.value = response.data.total;
        loading.value = false;
    });
}
/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
/** 重置按钮操作 */
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
        proxy.$modal.msgWarning("无效的任务id，请刷新后重试");
        return;
    }
    const res = await doSendMessage(row.id);
    if (Number(res?.code) === 200) {
        proxy.$modal.msgSuccess("发送成功");
    } else {
        proxy.$modal.msgWarning(res?.msg || "发送失败");
    }
}

getList();
</script>
