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
            <el-table stripe height="60vh" v-loading="loading" :data="DppQualityLogList">
                <el-table-column v-if="getColumnVisibility(0)" label="编号" align="center" prop="id" />
                <el-table-column v-if="getColumnVisibility(1)" label="任务名称" align="center" prop="name">
                    <template #default="scope">
                        {{ scope.row.name || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(2)" label="质量评分" align="center" prop="score">
                    <template #default="scope">
                        {{ scope.row.score }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(3)" label="执行状态" align="center" prop="successFlag">
                    <template #default="scope">
                        <dict-tag :options="quality_log_success_flag" :value="scope.row.successFlag" />
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(4)" label="问题数据" align="center" prop="problemData"
                    show-overflow-tooltip>
                    <template #default="scope">
                        {{ scope.row.problemData || '-' }}
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(5)" label="开始时间" align="center" prop="startTime" width="180"
                    show-overflow-tooltip>
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column v-if="getColumnVisibility(6)" label="结束时间" align="center" prop="endTime" width="180"
                    show-overflow-tooltip>
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right"
                    width="240">
                    <template #default="scope">
                        <el-button link type="primary" icon="view" @click="
                            routeTo('/da/dataQuality/dataQualityTaskLogs/detail/info', {
                                ...scope.row,
                                info: true,
                            })
                            " v-hasPermi="['dp:dppQualityLog:dppqualitylog:edit']">详情</el-button>

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
import { listDppQualityLog, } from "@/api/da/dppQuality/DppQualityLog";
const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router"
const { quality_log_success_flag } = proxy.useDict(

    'quality_log_success_flag'
);
const DppQualityLogList = ref([]);
// 列显隐信息
const columns = ref([
    { key: 1, label: "任务名称", visible: true },
    { key: 2, label: "质量评分", visible: true },
    { key: 3, label: "执行状态", visible: true },
    { key: 4, label: "问题数据", visible: true },
    { key: 5, label: "开始时间", visible: true },
    { key: 6, label: "结束时间", visible: true },

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

getList();
</script>
