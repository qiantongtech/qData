<!-- 右侧边栏 -->
<template>
  <el-dialog
    v-model="visibleDialog"
    draggable
    class="medium-dialog"
    :title="title"
    destroy-on-close
    width="1200px"
  >
    <div class="hint-div"  >
      <div style="display: flex; align-items: center">
        <el-icon color="#2A7BFD" size="16px">
          <InfoFilled />
        </el-icon>
        <span v-if="activeTab === 'all'">
          {{ td("dpp.integratioTask.instanceSummaryPrefix", "当前共有") }}
          {{ td("dpp.integratioTask.running", "运行中") }}
          <span
            class="count-number running"
            @click="openLatestRunningTaskLog"
            >{{ runningCount }}</span>
          {{ td("dpp.integratioTask.summarySeparator", "个，") }}
          {{ td("dpp.integratioTask.success", "成功") }}
          <span class="count-number success">{{ successCount }}</span>
          {{ td("dpp.integratioTask.summarySeparator", "个，") }}
          {{ td("dpp.integratioTask.failed", "失败") }}
          <span class="count-number failed">{{ failedCount }}</span>
          {{ td("dpp.integratioTask.instanceCountUnit", "个实例") }}
        </span>
        <span v-else-if="activeTab === 'running'">
          {{ td("dpp.integratioTask.instanceSummaryPrefix", "当前共有") }}
          <span
            class="count-number running"
            @click="openLatestRunningTaskLog"
            >{{ totalCount }}</span>
          {{ td("dpp.integratioTask.runningInstanceSuffix", "个实例运行中") }}
        </span>
        <span v-else-if="activeTab === 'success'">
          {{ td("dpp.integratioTask.instanceSummaryPrefix", "当前共有") }}
          <span class="count-number success">{{ totalCount }}</span>
          {{ td("dpp.integratioTask.successInstanceSuffix", "个实例成功") }}
        </span>
        <span v-else-if="activeTab === 'failed'">
          {{ td("dpp.integratioTask.instanceSummaryPrefix", "当前共有") }}
          <span class="count-number failed">{{ totalCount }}</span>
          {{ td("dpp.integratioTask.failedInstanceSuffix", "个实例失败") }}
        </span>
      </div>
      <div class="refresh-info">
        <span class="refresh-time">{{ td("dpp.integratioTask.lastRefreshTime", "最后刷新时间") }}：{{ lastRefreshTime }}</span>
        <el-button
          :icon="Refresh"
          link
          type="primary"
          :loading="isLoading"
          @click="handleRefresh"
        ></el-button>
      </div>
    </div>
    <div class="tabs-header">
      <el-tabs
        v-model="activeTab"
        @tab-change="handleTabChange"
        class="instance-tabs"
      >
        <el-tab-pane :label="td('dpp.integratioTask.all', '全部')" name="all" />
        <el-tab-pane :label="td('dpp.integratioTask.running', '运行中')" name="running" />
        <el-tab-pane :label="td('dpp.integratioTask.success', '成功')" name="success" />
        <el-tab-pane :label="td('dpp.integratioTask.failed', '失败')" name="failed" />
      </el-tabs>
    </div>
    <qt-table v-bind="tableStore" ref="tableRef">
      <template #taskInstanceName="scope">
        {{ scope.row.name || "-" }}
      </template>
      <template #commandType="scope">
        <dict-tag
          :options="dpp_etl_task_instance_command_type"
          :value="scope.row.commandType.trim()"
        />
      </template>
      <template #status="scope">
        <div @click="logDetailCatList(scope.row)">
          <StatusTag :status="scope.row.currentStatus.trim()" />
        </div>
      </template>
      <template #startTime="scope">
        <span>{{
          parseTime(scope.row.startTime, "{y}-{m}-{d} {h}:{i}") || "-"
        }}</span>
      </template>
      <template #endTime="scope">
        <span>{{
          parseTime(scope.row.endTime, "{y}-{m}-{d} {h}:{i}") || "-"
        }}</span>
      </template>
      <template #extractionCount> - </template>
      <template #writeCount> - </template>
      <template #personChargeName="scope">
        {{ scope.row.personChargeName || "-" }}
      </template>
      <template #handle="{ row }">
        <el-button
          link
          type="primary"
          icon="View"
          @click="logDetailCatList(row)"
          >{{
            (row.status || "").trim() === "1"
              ? td("dpp.integratioTask.realTimeLog", "实时日志")
              : td("dpp.integratioTask.viewLog", "查看日志")
          }}</el-button>
        <el-button
          link
          type="warning"
          @click="handleExport(row)"
          icon="Download"
          @mousedown="(e) => e.preventDefault()"
        >
          {{ td("dpp.integratioTask.downloadLog", "下载日志") }}
        </el-button>
      </template>
    </qt-table>
    <template #footer>
      <div class="dialog-footer-right">
        <el-button @click="visibleDialog = false">{{ td("dpp.integratioTask.close", "关闭") }}</el-button>
      </div>
    </template>
  </el-dialog>
  <TaskLogDialog ref="logDialogRef" />
</template>

<script setup>
/**
 * Instance 组件
 * 用于展示任务实例列表的弹窗，支持按状态筛选、刷新、查看日志和下载日志功能。
 *
 * @props {
 *   visible: boolean; // 控制弹窗的显示与隐藏，通过 v-model 双向绑定
 *   title: string;    // 弹窗的标题
 *   taskType: number; // 任务类型
 *   data: Object;     // 任务相关数据，至少包含任务ID `id`
 * }
 *
 * @emits {
 *   (event: 'update:visible', value: boolean): void; // 更新弹窗可见状态
 * }
 */
import {
  defineProps,
  defineEmits,
  ref,
  computed,
  watch,
  reactive,
  getCurrentInstance,
} from "vue";
import {
  listDppEtlTaskInstance,
  getEtlTaskInstanceStatistics,
} from "@/api/dpp/instance/job";
import TaskLogDialog from "./taskLog.vue";
import { InfoFilled, Refresh } from "@element-plus/icons-vue";
import { parseTime } from "@/utils/anivia";
import StatusTag from "@/views/dpp/components/logs/StatusTag.vue";
import useDefaultLang from "@/composables/useDefaultLang";

const { proxy } = getCurrentInstance();
const { td } = useDefaultLang();
const { dpp_etl_task_instance_command_type } = proxy.useDict(
  "dpp_etl_task_instance_command_type"
);
const props = defineProps({
  visible: { type: Boolean, default: true },
  title: { type: String, default: "" },
  taskType: { type: Number, default: 1 },
  data: { type: Object, default: () => ({}) },
});

const tableRef = ref(null);
const logDialogRef = ref(null);
const activeTab = ref("all");
const totalCount = ref(0); // This will hold the count for the current active tab
const lastRefreshTime = ref(parseTime(new Date()));

// These counts will be updated directly from the statistics API
const runningCount = ref(0);
const successCount = ref(0);
const failedCount = ref(0);
const isLoading = ref(false); // New loading state variable

async function fetchStatistics() {
  if (props.data.id) {
    // Ensure taskId is available
    const response = await getEtlTaskInstanceStatistics({
      taskId: props.data.id,
      taskType: props.taskType,
      projectId: props.data.projectId,
      projectCode: props.data.projectCode,
    });
    if (response.code === 200) {
      runningCount.value = response.data.runningCount;
      successCount.value = response.data.successCount;
      failedCount.value = response.data.failCount; // API returns failCount
      lastRefreshTime.value = response.data.refreshTime; // Update refresh time from API
      // When activeTab is 'all', totalCount should reflect allCount from statistics API
      if (activeTab.value === "all") {
        totalCount.value = response.data.allCount;
      }
    }
  }
}

const instanceColumns = computed(() => [
  { label: td("dpp.integratioTask.number", "编号"), prop: "id", width: 150, sortable: true },
  {
    label: td("dpp.integratioTask.taskName", "任务名称"),
    prop: "name",
    align: "left",
    width: 240,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.integratioTask.executionType", "执行类型"),
    prop: "commandType",
    width: 100,
    slot: "commandType",
  },
  {
    label: td("dpp.integratioTask.executeStatus", "执行状态"),
    prop: "status",
    width: 100,
    slot: "status",
  },
  {
    label: td("dpp.integratioTask.currentNode", "当前节点"),
    prop: "currentNode",
    width: 180,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.integratioTask.startTime", "开始时间"),
    prop: "startTime",
    width: 160,
    sortable: true,
    date: true,
    showOverflowTooltip: { effect: "light" },
  },
  {
    label: td("dpp.integratioTask.runDuration", "运行时长"),
    prop: "duration",
    width: 100,
  },
  {
    label: td("dpp.integratioTask.operation", "操作"),
    width: 200,
    fixed: "right",
    slot: "handle",
  },
]);

const tableStore = reactive({
  config: {
    table: {
      stripe: true,
      rowKey: "id",
      height: "580px",
    },
  },
  columns: instanceColumns,
  func: async (params) => {
    const queryParams = { ...params };
    if (!queryParams.taskId) {
      totalCount.value = 0; // Ensure totalCount is 0 if no taskId
      return { data: { rows: [], total: 0 } };
    }

    if (queryParams.status === "all") {
      queryParams.status = undefined;
    }

    const response = await listDppEtlTaskInstance(queryParams);
    const rows = Array.isArray(response.data)
      ? response.data
      : response.data?.rows || [];
    totalCount.value = response.data?.total || rows.length;
    return response;
  },
  params: {
    nodeId: undefined,
    taskId: undefined,
    status: undefined,
  },
});

/** 导出按钮操作 */
async function handleExport(row) {
  proxy.download(
    "/dpp/etlTaskInstance/downloadLog",
    {
      taskInstanceId: row.id,
    },
    `${row.name}.log`
  );
}

async function logDetailCatList(row) {
  if (logDialogRef.value) {
    logDialogRef.value.open(row.id);
  }
}

async function openLatestRunningTaskLog() {
  try {
    const response = await listDppEtlTaskInstance({
      taskId: props.data.id,
      taskType: props.taskType,
      projectId: props.data.projectId,
      projectCode: props.data.projectCode,
      status: "running",
      pageNum: 1,
      pageSize: 1,
    });
    if (
      response.code === 200 &&
      response.data.rows &&
      response.data.rows.length > 0
    ) {
      logDetailCatList(response.data.rows[0]);
    } else {
      proxy.$modal.msgWarning(
        td("dpp.integratioTask.noRunningInstance", "没有找到运行中的任务实例。")
      );
    }
  } catch (error) {
    console.error("获取最新运行中任务失败:", error);
    proxy.$modal.msgError(
      td("dpp.integratioTask.fetchLatestRunningFailed", "获取最新运行中任务失败。")
    );
  }
}

function handleRefresh() {
  isLoading.value = true;
  Promise.all([
    fetchStatistics(), // Fetch statistics
    tableRef.value?.getList(), // Refresh table data
  ]).finally(() => (isLoading.value = false));
}

function handleTabChange(tabName) {
  if (tabName === "all") {
    tableStore.params.status = undefined;
  } else {
    tableStore.params.status = tabName;
  }
  tableRef.value?.getList();
}

const emit = defineEmits(["update:visible", "confirm"]);

watch(
  () => props.visible,
  (newVal) => {
    if (newVal) {
      isLoading.value = true; // Set loading true when dialog opens
      activeTab.value = "all";
      totalCount.value = 0; // Reset before fetching
      tableStore.params.taskId = props.data.id;
      tableStore.params.taskType = props.taskType;
      tableStore.params.projectId = props.data.projectId;
      tableStore.params.projectCode = props.data.projectCode;
      tableStore.params.status = undefined;
      Promise.all([
        fetchStatistics(), // Call to fetch statistics
        tableRef.value?.getList(), // And refresh table data
      ]).finally(() => (isLoading.value = false));
    }
  }
);

// 计算属性处理 v-model
const visibleDialog = computed({
  get() {
    return props.visible;
  },
  set(newValue) {
    emit("update:visible", newValue);
  },
});
</script>

<style lang="scss" scoped>
.hint-div {
  margin: 0;
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.tabs-header {
}

.refresh-info {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #606266;
}

.count-number {
  font-weight: bold;
}

.running {
  color: var(--el-color-primary);
}

.success {
  color: var(--el-color-success);
}

.failed {
  color: var(--el-color-danger);
}
.dialog-footer-right {
  text-align: right;
}
</style>
