<template>
    <el-dialog v-model="visibleDialog" draggable class="dialog" :title="title" style="width: 1500px" destroy-on-close>
        <el-table stripe height="450px" v-loading="loading" :data="jobLogList">
            <el-table-column label="编号" width="80" align="center" prop="id" />
            <el-table-column label="实例名称" align="center" prop="name" :show-overflow-tooltip="{effect: 'light'}" width="200" />
            <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="{effect: 'light'}" width="200" />
            <!-- <el-table-column label="执行类型" align="center" prop="taskName" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <dict-tag :options="da_discovery_task_log_status" :value="scope.row.jobGroup" />
                </template>
</el-table-column> -->
            <el-table-column label="执行类型 " align="center" prop="jobMessage" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span>任务运行</span>
                </template>
            </el-table-column>
            <el-table-column label=" 执行状态 " align="center" prop="status" :show-overflow-tooltip="{effect: 'light'}">
                <template #default="scope">
                    <span>
                        <dict-tag :options="da_discovery_task_log_status" :value="scope.row.status" /></span>
                </template>
            </el-table-column>
            <el-table-column label="开始时间" align="center" prop="createTime" width="150" >
                <template #default="scope">
                    <span>{{ parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
                </template>
            </el-table-column>
            <el-table-column label="结束时间" align="center" prop="endTime" width="150">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{i}") || "-" }}</span>
                </template>
            </el-table-column>
            <el-table-column label="新增表" align="center" prop="newTableCount" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column label="修改表 " align="center" prop="modifiedTableCount" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column label="删除表 " align="center" prop="deletedTableCount" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column label="责任人 " align="center" prop="contact" :show-overflow-tooltip="{effect: 'light'}" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
                <template #default="scope">
                    <el-button link type="primary" icon="View" @click="logDetailCatList(scope.row)"
                        v-hasPermi="['monitor:job:query']">查看</el-button>
                    <el-button link type="warning" @click="handleExport(scope.row)"
                        @mousedown="(e) => e.preventDefault()">
                        <i class="iconfont-mini icon-download-line mr5"></i>下载
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        <!-- <template #footer>
            <div style="text-align: right">
        <el-button @click="closeDialog">关闭</el-button>
        <el-button type="primary" @click="saveData">保存</el-button>
        </div>
</template> -->
    </el-dialog>
    <!-- 调度日志详细 -->
    <el-dialog title="查看日志" v-model="open" width="800px" :append-to="$refs['app-container']" draggable destroy-on-close>
        <div v-html="formattedText"></div>
        <!-- <template #footer>
            <div class="dialog-footer">
                <el-button @click="open = false">关 闭</el-button>
            </div>
        </template> -->
    </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, computed, watch } from 'vue';
import { listDaDiscoveryTaskLog, logDetailCat } from '@/api/da/discovery/discoveryTaskLog';
import request from '@/utils/request';
const { proxy } = getCurrentInstance();
const { sys_common_status, sys_job_group, da_discovery_task_log_status } = proxy.useDict(
    'sys_common_status',
    'sys_job_group',
    'da_discovery_task_log_status'
);
const props = defineProps({
    visible: { type: Boolean, default: true },
    title: { type: String, default: '表单标题' },
    data: { type: Object, default: () => ({}) }
});
const open = ref(false);
let form = ref();
let queryParams = ref({
    pageNum: 1,
    pageSize: 10,
    nodeId: undefined,
    taskId: undefined
});
const formattedText = computed(() => {
    return form.value.logContent.replace(/\n/g, '<br>'); // 将换行符替换为 <br> 标签
});
/** 导出按钮操作 */
async function handleExport(row) {
    const response = await logDetailCat({ handleMsg: row.path });
    if (response && response.content) {
        proxy.download(
            'da/daDiscoveryTaskLog/downloadLog',
            { handleMsg: row.path },
            `${new Date().getTime()}.log`
        );
    }
}
async function logDetailCatList(row) {
    try {
        form.value = {};
        const response = await logDetailCat({ handleMsg: row.path });
        if (response && response.content) {
            form.value = response.content;
            open.value = true;
        }
    } catch (error) { }
}

const total = ref(0);
const dateRange = ref([]);
let jobLogList = ref([]);
let loading = ref(false);
/** 查询调度日志列表 */
function getList() {
    loading.value = true;
    console.log('🚀 ~ getList ~ props.data:', props.data);

    listDaDiscoveryTaskLog({
        ...queryParams.value,
        // jobName: props.data.name,
        taskCode: props.data.taskCode
        // taskName: props.data.name,
    }).then((response) => {
        jobLogList.value = response.data.rows;
        console.log('🚀 ~ listDaDiscoveryTaskLog ~ jobLogList.value:', jobLogList.value);
        total.value = response.data.total;
        loading.value = false;
    });
}
const emit = defineEmits(['update:visible', 'confirm']);

watch(
    () => props.visible,
    (newVal) => {
        if (newVal) {
            getList();
        } else {
            jobLogList.value = [];
        }
    }
);

// 计算属性处理 v-model
const visibleDialog = computed({
    get() {
        return props.visible;
    },
    set(newValue) {
        emit('update:visible', newValue);
    }
});

// 关闭对话框的方法
const closeDialog = () => {
    emit('update:visible', false);
};

// 保存数据的方法
const saveData = () => {
    emit('confirm', localNode.value); // 向父组件提交本地数据
    emit('update:visible', false);
};
</script>
