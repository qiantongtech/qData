<template>
    <!-- 数据预览的修改记录弹窗 -->
    <el-dialog v-model="visible" class="dialog" width="1200px" draggable destroy-on-close>
        <template #header="{ close, titleId, titleClass }">
            <span role="heading" aria-level="2" class="el-dialog__title">
                {{ title }}
            </span>
        </template>
        <el-form ref="queryForm" :model="queryParams" inline>
            <el-form-item label="时间" prop="dataTime">
                <el-date-picker v-model="queryParams.dataTime" style="width: 250px" :clearable="false" type="daterange"
                    align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                    @change="handleDateChange" />
            </el-form-item>
            <el-form-item label="创建人" prop="createBy">
                <el-input v-model="queryParams.createBy" placeholder="请输入创建人" style="width: 180px; margin-right: 10px"
                    class="filter-item" />
            </el-form-item>
            <el-form-item>
                <el-button style="margin-left: 7px" plain type="primary" @click="fetchData"
                    @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22377 mr5"></i>查询
                </el-button>
                <el-button @click="resetQuery" @mousedown="(e) => e.preventDefault()">
                    <i class="iconfont-mini icon-a-zu22378 mr5"></i>重置
                </el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="list" stripe :default-sort="defaultSort" @sort-change="handleSortChange"
            tooltip-effect="dark" :size="tableSize" :height="tableHeight" style="width: 100%; margin: 15px 0;">
            <el-table-column v-if="tableColumns.length > 0" label="编号" width="75" align="left">
                <!--                <template #default="{ $index }">-->
                <!--                    <span>{{ $index + 1 }}</span>-->
                <!--                </template>-->
                <template #default="scope">
                    <div>{{ scope.row.id || '-' }}</div>
                </template>
            </el-table-column>

            <!-- <el-table-column v-for="(item, index) in tableColumns" :key="index" :prop="item.prop" :label="item.label"
                :show-overflow-tooltip="{effect: 'light'}"  align="left" /> -->

            <el-table-column label="创建人" align="left">
                <template #default="scope">
                    <div>{{ scope.row.createBy || '-' }}</div>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="left" sortable="custom" column-key="create_time"
                :sort-orders="['descending', 'ascending']">
                <template #default="scope">
                    <div>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}") || '-' }}</div>
                </template>
            </el-table-column>
            <el-table-column label="更新人" align="left">
                <template #default="scope">
                    <div>{{ scope.row.updateBy || '-' }}</div>
                </template>
            </el-table-column>
            <el-table-column label="更新时间" align="left" sortable="custom" column-key="update_time"
                :sort-orders="['descending', 'ascending']">
                <template #default="scope">
                    <div>{{ parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}") || '-' }}</div>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="left">
                <template #default="scope">
                    <dict-tag :options="da_asset_operate_status" :value="scope.row.status" />
                </template>
            </el-table-column>
            <el-table-column label="查看前后对比" align="left" width="150px">
                <template #default="{ row }">
                    <el-button link v-if="row.updateBefore" type="primary" icon="view"
                        @click="showDataDialog(row.id, row.updateBefore, row.updateAfter)">查看</el-button>
                    <el-button link icon="Stopwatch" :disabled="row.status == 5" type="primary"
                        @click="rollBackrollBack(row)">回滚</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <dataDiffDialog ref="dataDiff" @ok="ok" />
    </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from "vue";
// import { page } from "@/api/metadata/contentsTypeTaUp";
import dataDiffDialog from "./previewEditDiff.vue";
import { getDaAssetList, rollBack } from '@/api/da/assetchild/operate/daAssetOperateLog.js';

const { proxy } = getCurrentInstance();
const { da_asset_operate_status, } = proxy.useDict(
    "da_asset_operate_status",
);
const emit = defineEmits(['success']);
const props = defineProps({
    columns: {
        type: Array,
        default: () => [],
    },
});

const title = "修改记录";
const loading = ref(false);
const visible = ref(false);
const tableSize = ref("medium");
const list = ref([]);
const total = ref(0);
const tableHeight = ref(document.body.offsetHeight - 650 + "px");
const queryForm = ref(null);
const dataDiff = ref(null);
const tableColumns = reactive([
    { prop: "updateTime", label: "修改时间", show: true, width: 150 },
    { prop: "createBy ", label: "修改人", show: true, width: 150 },
]);
const defaultSort = ref({ columnKey: 'create_time', order: 'desc' });

const queryParams = reactive({
    startTime: null,
    endTime: null,
    dataTime: [],
    pageNum: 1,
    pageSize: 10,
    creatorId: ""
    // updateWhere: {},
});

/** 排序触发事件 */
function handleSortChange({ column, prop, order }) {
    queryParams.orderByColumn = column?.columnKey || prop;
    queryParams.isAsc = column.order;
    getList();
}

function formatDateTime(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}-${day}`;
}

function handleDateChange(value) {
    if (value && value.length === 2) {
        queryParams.startTime = formatDateTime(value[0]) + " 00:00:00";
        queryParams.endTime = formatDateTime(value[1]) + " 23:59:59";
    } else {
        queryParams.startTime = "";
        queryParams.endTime = "";
    }
}
function resetQuery() {
    queryParams.pageNum = 1;
    queryParams.startTime = null;
    queryParams.endTime = null;
    queryParams.dataTime = [];
    queryParams.creatorId = "";
    queryParams.createBy = "";
    // queryForm.value.resetFields
    getList();
}
function fetchData() {
    queryParams.pageNum = 1;
    getList();
}

function showDataDialog(id, updateBefore, updateAfter) {
    dataDiff.value.show(id, updateBefore, updateAfter);
}
let columnsTwo = ref([])
function show(row, data) {
    queryParams.pageNum = 1;
    queryParams.startTime = null;
    queryParams.endTime = null;
    queryParams.dataTime = [];
    queryParams.creatorId = "";
    visible.value = true;
    list.value = [];
    queryParams.updateBefore = JSON.stringify(row)
    getList();

}
let uniqueKeys = ref([])
watch(
    () => props.columns,
    (arr) => {
        if (arr && arr.length > 0) {
            // 必填字段
            const requiredFields = arr.filter(item => item.columnNullable == true);
            // 所有非唯一键字段
            columnsTwo.value = arr.filter(item => item.columnKey == false);
            // 所有唯一键字段
            uniqueKeys.value = arr.filter(item => item.columnKey != false);
        }
    },
    { immediate: true }
);

function getList() {
    loading.value = true;
    // 唯一键字段数组拼成字符串
    const commentKeyList = uniqueKeys.value.map(item => item.en).join(',');
    const tableCommentList = [];
    // 组装 map-json 结构对象
    const fieldNamesObj = {
        tableCommentList,
        commentKeyList,
    };
    getDaAssetList({ ...queryParams, fieldNames: JSON.stringify(fieldNamesObj) }).then((response) => {
        loading.value = false;
        if (response.code == '200') {
            const { data } = response;
            list.value = data.rows;
            total.value = parseInt(data.total);
        }
    });
}

function rollBackrollBack(row) {
    loading.value = true
    rollBack(row.id)
        .then(res => {
            if (res.code == '200') {
                getList()
                emit('success');
            }
        })
        .finally(() => {
            loading.value = false
        })
}
function ok() {
    queryParams.pageNum = 1;
    getList();
}
defineExpose({ show });
</script>
